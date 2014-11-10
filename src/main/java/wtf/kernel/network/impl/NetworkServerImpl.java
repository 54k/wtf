package wtf.kernel.network.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import wtf.kernel.TaskManager;
import wtf.kernel.network.NetworkServer;
import wtf.kernel.network.NetworkSession;
import wtf.kernel.network.internal.NetworkServerInternal;
import wtf.util.Handler;
import wtf.util.NamingThreadFactory;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicReference;

public class NetworkServerImpl implements NetworkServer, NetworkServerInternal {

	@Inject
	private TaskManager taskManager;

	private final ChannelGroup channelGroup;
	private final ServerBootstrap bootstrap;
	private final EventLoopGroup eventLoop;
	private final AtomicReference<Handler<NetworkSession>> channelHandlerRef;

	public NetworkServerImpl() {
		channelGroup = new DefaultChannelGroup("ALL CHANNELS", GlobalEventExecutor.INSTANCE);
		eventLoop = new NioEventLoopGroup(2, new NamingThreadFactory("network-server"));
		channelHandlerRef = new AtomicReference<>();

		bootstrap = new ServerBootstrap();
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.group(eventLoop);
		bootstrap.childHandler(new TransportInitializer());
	}

	public void bind(int port) {
		validate();
		channelGroup.add(bootstrap.bind(port).syncUninterruptibly().channel());
	}

	private void validate() {
		if (channelHandlerRef.get() == null) {
			throw new IllegalStateException("channelHandler is not set");
		}
	}

	public void shutdown() {
		channelGroup.close().syncUninterruptibly();
		eventLoop.shutdownGracefully().syncUninterruptibly();
	}

	public void onConnection(Handler<NetworkSession> channelHandler) {
		if (!channelHandlerRef.compareAndSet(null, channelHandler)) {
			throw new IllegalStateException("channelHandler is already set");
		}
	}

	private class TransportInitializer extends ChannelInitializer<NioSocketChannel> {
		@Override
		protected void initChannel(NioSocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new HttpServerCodec());
			pipeline.addLast(new HttpObjectAggregator(65536));
			pipeline.addLast(new WebSocketServerProtocolHandler("/"));
			pipeline.addLast(new WebSocketAcceptor());
		}
	}

	private class WebSocketAcceptor extends ChannelInboundHandlerAdapter {
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
				channelGroup.add(ctx.channel());
				taskManager.execute(() -> channelHandlerRef.get().handle(new NetworkSessionImpl(1, ctx.channel())));
			}
		}
	}
}

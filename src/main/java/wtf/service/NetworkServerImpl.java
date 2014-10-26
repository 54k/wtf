package wtf.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import wtf.util.Handler;
import wtf.util.NamingThreadFactory;

import javax.inject.Inject;

public class NetworkServerImpl implements NetworkServer {

    private final ChannelGroup channels;
    private final ServerBootstrap serverBootstrap;

    private Handler<NetworkSession> channelHandler;
    @Inject
    private TaskManager taskManager;

    public NetworkServerImpl() {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(new NioEventLoopGroup(2, new NamingThreadFactory("network-server")));
        serverBootstrap.childHandler(new TransportInitializer());
    }

    public void bind(int port) {
        validate();
        channels.add(serverBootstrap.bind(port).syncUninterruptibly().channel());
    }

    private void validate() {
        if (channelHandler == null) {
            throw new IllegalStateException();
        }
    }

    public void shutdown() {
        channels.close().syncUninterruptibly();
    }

    public void onConnection(Handler<NetworkSession> channelHandler) {
        this.channelHandler = channelHandler;
    }

    private class TransportInitializer extends ChannelInitializer<NioSocketChannel> {

        @Override
        protected void initChannel(NioSocketChannel ch) throws Exception {
            ch.pipeline().addLast(new HttpServerCodec());
            ch.pipeline().addLast(new HttpObjectAggregator(65536));
            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/"));
            ch.pipeline().addLast(new WebSocketAcceptor());
        }
    }

    private class WebSocketAcceptor extends ChannelInboundHandlerAdapter {

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
                channels.add(ctx.channel());
                taskManager.execute(() -> channelHandler.handle(new NetworkSessionImpl(taskManager, ctx)));
            }
        }
    }
}

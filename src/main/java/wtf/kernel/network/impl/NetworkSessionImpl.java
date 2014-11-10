package wtf.kernel.network.impl;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import wtf.kernel.network.NetworkSession;
import wtf.kernel.network.internal.SettableFutureWrapper;
import wtf.util.Handler;

import java.net.SocketAddress;
import java.util.concurrent.Executor;

public class NetworkSessionImpl implements NetworkSession {

	private final Object id;
	private final Channel channel;

	private volatile Executor executor;
	private volatile Handler<? super Object> messageHandler;
	private volatile Handler<Void> closeHandler;

	public NetworkSessionImpl(Object id, Channel channel) {
		Preconditions.checkNotNull(id);
		Preconditions.checkNotNull(channel);

		this.id = id;
		this.channel = channel;
		this.channel.pipeline().addLast(new WebSocketFrameHandler());
	}

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public ListenableFuture<?> send(Object message) {
		SettableFuture<Object> settableFuture = SettableFuture.create();
		channel.writeAndFlush(new TextWebSocketFrame((String) message)).addListener(new SettableFutureWrapper(settableFuture));
		return settableFuture;
	}

	@Override
	public ListenableFuture<?> close() {
		SettableFuture<Object> settableFuture = SettableFuture.create();
		channel.writeAndFlush(new CloseWebSocketFrame()).addListener(new SettableFutureWrapper(settableFuture));
		return settableFuture;
	}

	@Override
	public SocketAddress getLocalAddress() {
		return channel.localAddress();
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return channel.remoteAddress();
	}

	@Override
	public boolean isActive() {
		return channel.isActive();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<? extends T> wrappedClass) {
		if (wrappedClass.isAssignableFrom(channel.getClass())) {
			return (T) channel;
		}
		return null;
	}

	@Override
	public void setExecutor(Executor executor) {
		Preconditions.checkNotNull(executor);
		this.executor = executor;
	}

	@Override
	public void setMessageHandler(Handler<Object> handler) {
		Preconditions.checkNotNull(handler);
		this.messageHandler = handler;
	}

	@Override
	public void setCloseHandler(Handler<Void> handler) {
		Preconditions.checkNotNull(handler);
		this.closeHandler = handler;
	}

	private final class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

		@Override
		public boolean acceptInboundMessage(Object msg) throws Exception {
			return msg instanceof TextWebSocketFrame;
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
			String text = msg.text();
			if (messageHandler != null) {
				executor.execute(() -> messageHandler.handle(text));
			}
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			if (closeHandler != null) {
				executor.execute(() -> closeHandler.handle(null));
			}
		}
	}
}

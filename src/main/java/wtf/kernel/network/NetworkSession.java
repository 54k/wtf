package wtf.kernel.network;

import com.google.common.util.concurrent.ListenableFuture;
import wtf.util.Handler;

import java.net.SocketAddress;
import java.util.concurrent.Executor;

public interface NetworkSession {

	Object getId();

	ListenableFuture<?> send(Object message);

	ListenableFuture<?> close();

	SocketAddress getLocalAddress();

	SocketAddress getRemoteAddress();

	boolean isActive();

	<T> T unwrap(Class<? extends T> wrappedClass);

	void setExecutor(Executor executor);

	void setMessageHandler(Handler<Object> handler);

	void setCloseHandler(Handler<Void> handler);
}

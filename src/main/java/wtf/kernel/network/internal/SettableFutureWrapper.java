package wtf.kernel.network.internal;

import com.google.common.util.concurrent.SettableFuture;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public final class SettableFutureWrapper implements ChannelFutureListener {

	private final SettableFuture<?> wrapped;

	public SettableFutureWrapper(SettableFuture<?> wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void operationComplete(ChannelFuture channelFuture) throws Exception {
		if (channelFuture.isSuccess()) {
			wrapped.set(null);
		} else if (channelFuture.isCancelled()) {
			wrapped.cancel(true);
		} else {
			wrapped.setException(channelFuture.cause());
		}
	}
}

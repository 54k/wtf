package wtf.kernel;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import wtf.util.NamingThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskManagerImpl implements TaskManager {

	private final ListeningScheduledExecutorService delegate;

	public TaskManagerImpl() {
		ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1, new NamingThreadFactory("main-eventloop"));
		delegate = MoreExecutors.listeningDecorator(executor);
	}

	@Override
	public void execute(Runnable command) {
		delegate.execute(command);
	}

	@Override
	public ListenableFuture<?> submit(Runnable task) {
		return delegate.submit(task);
	}

	@Override
	public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return delegate.schedule(command, delay, unit);
	}

	@Override
	public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return delegate.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	@Override
	public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return delegate.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}
}

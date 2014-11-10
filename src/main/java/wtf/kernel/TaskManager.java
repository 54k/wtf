package wtf.kernel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.TimeUnit;

public interface TaskManager {

	void execute(Runnable task);

	ListenableFuture<?> submit(Runnable task);

	ListenableFuture<?> schedule(Runnable task, long delay, TimeUnit unit);

	ListenableFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit);

	ListenableFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit);
}

package wtf.service;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableScheduledFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public interface TaskManager {

    void execute(Runnable task);

    ListenableFuture<?> submit(Runnable task);

    <T> ListenableFuture<T> submit(Runnable task, T result);

    <T> ListenableFuture<T> submit(Callable<T> task);

    ListenableFuture<?> schedule(Runnable task, long delay, TimeUnit unit);

    <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

    ListenableFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit);

    ListenableFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit);
}

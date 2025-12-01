package org.example;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

// internally, the concrete implementation of the executor service will use a thread pool to execute the task and return a future to the call.
// the caller doesn't wait for anything, it simply submits the task and gets the future object, and later it'll use this future object to get the result.
// the caller can use this future object to inspect the state of the particular task submitted, get the completed result or get the exception thrown by the task.
// If you wrap the executor service within a try-with-resources block, the close method will be automatically called.
public interface ExecutorService extends Executor,AutoCloseable {

    // only the most important methods are shown here

    // submit a runnable or a callable task
    <T> Future<T> submit(Callable<T> task);
    Future<?> submit(Runnable task);

    // orderly shutdown. All new submissions after this method will be rejected but ES will proceed with all the already submitted tasks.
    // this method doesn't wait for all tasks to complete, but it'll return immediately.
    void shutdown();

    // attempts to stop all executing tasks by sending interrupts to them
    // interrupts are the preferred mechanism to stop the threads
    // waiting tasks will not be processed
    List<Runnable> shutdownNow();

    // shuts down the service
    // but waits for all tasks to finish
    default void close() {

    }
}

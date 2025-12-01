package org.example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// Future represents the future result of an asynchronous task.
public interface Future<V> {

    // get the result of the task execution.
    // wait till the result is available.
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;

    // get the result immediately
    // assume that the task is completed
    default V resultNow() {
        return null;
    }

    default Throwable exceptionNow() {
        return null;
    }

    // Attempts to cancel the execution of the task.
    //
    // - If the task has not started yet, it will be removed from the queue.
    // - If the task is already running and `true` is passed, the ExecutorService
    //   will try to cancel it by interrupting the thread.
    //
    // Note:
    // - interrupt() does NOT force-stop a thread.
    // - It only sets a flag saying "please stop".
    // - The running code must check this interrupt flag.
    // - If the code sees the flag, it should stop work and exit cleanly.
    // - If the task never checks the flag, interrupt has no effect.
    boolean cancel(boolean mayInterruptIfRunning);


    // computation state : running, success(completed without exceptions), failed(completed with exceptions), cancelled
    default java.util.concurrent.Future.State state() {
        return null;
    }
    boolean isCancelled();
}

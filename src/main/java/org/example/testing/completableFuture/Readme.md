# CompletableFuture in Java

Servlets do not mandate that the user request threads be alive when the final data is sent back to the user.  
This has opened the doors for reactive style of programming.  
Java CompletableFuture is equivalent to Javascript's promises.


### Completable Future Pipeline

```java
import org.example.testing.futures.callable.TaskResult;
import java.util.concurrent.CompletableFuture;

// Tasks to execute in parallel
Supplier<TaskResult> task1 = () -> doTask("task1", 3, false);
Supplier<TaskResult> task2 = () -> doTask("task2", 5, false);

// CompletableFuture static method supplyAsync() will start Task1 asynchronously
// this method will return immediately after initiating the task1 in a separate thread
// task2 is also initiated in the same way. 2 tasks have been started asynchronously.
// once the two tasks have completed successfully, the lambda function specified in the thenCombine method is called to handle the result(result1 and result2)
// the returned data from the lambda function is then fed as input to the next stage i.e. thenApply() stage
// finally the thenAccept consumer is called to handle the final result.
// the final string printed is shown as Result
// each stage adds something to the string
// output of the previous stage is an input to the next stage
// the code below simply specifies the stages of the pipeline but does not trigger all the stages
// for each stage it specifies the code to be executed in the form of a supplier or a function or a by-function or a consumer
// when a stage is triggered, the code associated with the stage is run
// think about this entire pipeline as a specification for orchestrating the results coming from the asynchronous tasks(1 and 2)
// the pipeline itself is broken up into different stages where the data is transformed, combined or accepted
// so when the control passes to the next statement i.e. do something else, the only thing guaranteed is that the two tasks have started executing in their own thread in the pipeline created
// there is no guarantee that the pipeline has been fully executed. In fact, the chances are very less that the pipeline has fully executed.
// the thread which created the pipeline can be released back to the thread pool or terminated. It doesn't have to block on anything(like result of the pipeline)
// in our case when both the tasks have completed(after 5 seconds since both are running concurrently), the thenCombine method is triggered.
// once completed, thenApply is triggered and so on till the whole pipeline finishes execution.
// The final result can then be used to do whatever required.
// If you call the join method on the pipeline, then it will wait for the pipeline to complete(becomes blocking)
// in typical case it's usually a part of the app server, say spring boot controller, then the completable future can be returned to the app server for it to get the result and send back to the user
// completable future implements both Future and CompletionStage interface. 
// since it implements future interface, you also get a get() method to block as well. This of course is not encouraged.
// much of the pipelining ability of the completable future comes from CompletionStage interface
CompletableFuture.supplyAsync(task1))
        .thenCombine(
                CompletableFuture.supplyAsync(task2),
                (result1, result2) -> String.format("Combined (%s : %s)", result1.taskName(), result2.taskName()))
        .thenApply(data -> data + " :: Handled Apply")
        .thenAccept(data -> {
            System.out.println(data + " :: Handled Accept");
        })

// do something else
```
```java
Result = Combined(task1 : task2) :: Handled Apply :: Handled Accept
```

### CompletableFuture class

```java 
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {

    // methods to start a new task on a new thread. Overloaded methods available to use Executor
    // methods to start a task asynchronously
    // Completable Future methods take runnable or supplier to start a new task unlike Executor Service that takes a runnable or a callable
    // Completable Futures are designed with runtime exceptions in mind
    // Supplier interface doesn't throw any checked exception hence used in CompletableFuture
    // A callable can't directly in a CompletableFuture, though it can be wrapped in a try-catch and by rethrowing exceptions.
    public static java.util.concurrent.CompletableFuture<Void> runAsync(Runnable runnable);
    public static <U> java.util.concurrent.CompletableFuture<U> supplyAsync(Supplier<U> supplier);

    // methods to help with the pipeline. Overloaded methods available to use Executor
    public <U> java.util.concurrent.CompletableFuture<U> thenApply(Function<? super T, ? extends U> fn);
    public <U> java.util.concurrent.CompletableFuture<U> thenCompose(
            Function<? super T, ? extends CompletionStage<U>> fn);
    public java.util.concurrent.CompletableFuture<Void> thenAccept(Consumer<? super T> action);
    public java.util.concurrent.CompletableFuture<Void> thenRun(Runnable action);

    // combine results of 2 tasks
    public <U, V> java.util.concurrent.CompletableFuture<V> thenCombine(
            CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn);

    // handle multiple completable futures
    public static java.util.concurrent.CompletableFuture<Void> allOf(java.util.concurrent.CompletableFuture<?>... cfs);
    public static java.util.concurrent.CompletableFuture<Objects> anyOf(java.util.concurrent.CompletableFuture<?>... cfs);

    // complete a completableFuture
    public boolean complete(T value);
    public boolean completeExceptionally(Throwable ex);

    // methods to avoid because they block
    public T get() throws InterruptedException, ExecutionException;
    public T join();
}
```

### Pipeline Creation vs Pipeline Execution
1. runAsync(), thenApply(), thenAccept() are all called during pipeline creation phase. These methods are called on a single thread.
2. The suppliers, consumers and the functions that are supplied to the above methods are executed during the pipeline execution phase. These may be executed on different threads.
3. But what happens when doTask() throws a runtime exception? After, the supplyAsync() you need to handle it.
4. What happens when there are multiple asynchronous tasks running already in different threads and one of the stages throws an exception. If there are no exception handlers, the entire pipeline errors up.
5. The running tasks are no automatically cancelled, they keep running in the background but no one would be interested in their results.

### Exception Recovery - exceptionally()

```java 
import org.example.testing.futures.callable.Main;
import org.example.testing.futures.callable.TaskResult;

import java.util.concurrent.CompletableFuture;

// execute a task in common pool
// then apply a function
// then recover from exception if necessary
// then Accept the result which will be consumed by consumer
CompletableFuture pipeline = CompletableFuture.supplyAsync(() -> Main.doTask("some task", 3, true))
        .thenApply(taskResult -> taskResult.secs)
        .exceptionally(t -> 0)
        .thenAccept(time -> {
            System.out.println(time);
        })

// then compose is used when a function itself returns a completable future (unlike then apply which returns an integer)
// then apply is triggered only after the completable future returned by handle task result completes
// all the stages run one after the other
CompletableFuture pipeline = CompletableFuture.supplyAsync(() -> Main.doTask("task", 3, false))
        .thenCompose(taskResult -> CompletableFuture.handleTaskResult(taskResult))
        .thenApply(data -> data + " :: Handled apply")
        .thenAccept(data -> {
            System.out.println(data + ":: Handled accept");
        });

private static CompletableFuture<String> handleTaskResult(TaskResult taskResult) {
    return CompletableFuture.supplyAsync(() -> {
        return taskResult + " :: Handled Compose";
    });
}

// Result = TaskResult[taskName= task, secs = 3] :: Handled Compose :: Handled apply :: Handled accept
```

### thenCombine(...)

```java
// Tasks to execute asynchronously and in parallel

import org.example.testing.futures.callable.Main;
import org.example.testing.futures.callable.TaskResult;

import java.util.concurrent.CompletableFuture;

Supplier<TaskResult> task1 = () -> Main.doTask("task1", 3, false);
Supplier<TaskResult> task2 = () -> Main.doTask("task2", 5, false);

// thenCombine will combine the results of task1 and task2
// thenApply will operate on this combined result

// the pipeline starts by running task1 asynchronously which will run on the common fork join pool
// in fact before the pipeline is fully created, two tasks would have started running in parallel.
// task 1 completes in 3 seconds, but the pipeline execution doesn't progress till the task 2 is completed
// once the task2 is completed, the lambda for combining the task results is triggered
// fuze method just combines the results in one string
// after this, thenApply and thenAccept stages are triggered as usual.
CompletableFuture pipeline = CompletableFuture.supplyAsync(task1)
        .thenCombine(
                CompletableFuture.supplyAsync(task2),
                (result1, result2) -> fuze(result1.taskName(), result2.taskName()))
        .thenApply(data -> data + " :: Handled apply")
        .thenAccept(data -> {
            System.out.println(data + " :: Handled accept");
        });

private static String fuze(String s1, String s2) {
    return String.format("Combined (%s : %s)", s1, s2);
} 

// Result = Combined (task1 : task2) :: Handled Apply :: Handled accept
```

package org.example.testing.completableFuture;

import org.example.testing.futures.callable.TaskResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
/*    public static void main(String[] args) {

        // execute a task in the common fork join pool of jvm
        // runAsync takes a runnable, but if runAsync is submitting a task for execution.which executor service is it using?
        // since we haven't specified anything, the default ES is the common Fork join pool of jvm used for CPU intensive operations
        // this is used to run the task
        // runAsync also has an overloaded method where another executor service can be passed as an additional parameter.
        CompletableFuture<Void> taskFuture = CompletableFuture.runAsync(() -> org.example.testing.futures.callable.Main.doTask("task1", 3, false));

        try {

            // wait till Task future is completed(no returned data)
            taskFuture.get();

            // proceed to do other things

        } catch (InterruptedException | ExecutionException exception) {
            System.out.println(exception);
        }
    }*/

/*    public static void main(String[] args) {

        // execute a task in the common fork join pool of jvm
        // example of starting async task by using supply async method
        // Here we use supplier interface instead of runnable interface
        // supplier interface is used when we want a result to be returned when the task is completed

        CompletableFuture<TaskResult> taskFuture = CompletableFuture.supplyAsync(() -> org.example.testing.futures.callable.Main.doTask("task1", 3, false));

        try {

            // wait till Task future is completed(task result is available)
            // join() method can be used instead of get()
            // the join() method does not throw checked exception
            TaskResult taskResult = taskFuture.get();
            System.out.println(taskResult);

            // proceed to handle task result

        } catch (InterruptedException | ExecutionException exception) {
            System.out.println(exception);
        }
    }*/

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // execute a task in the common fork join pool of jvm
        // then apply a function
        // then accept the result which will be consumed by consumer
        CompletableFuture pipeline =
                CompletableFuture.supplyAsync(() -> org.example.testing.futures.callable.Main.doTask("task1",3,false))
                        // exceptionally() is put to handle the exception thrown by doTask() method, in case exception occurs the default task result object will then be passed to the thenApply stage
                        // important thing here is the exception handler has to throw the type which is expected by the next stage
                        .exceptionally(t -> new TaskResult("some task", 0))
                        .thenApply(taskResult -> taskResult.secs)
                        .thenApply(time -> time * 1000)
                        .thenAccept(System.out::println);

//        System.out.println(pipeline.get());
    }
}

package org.example.testing.completableFuture;

import org.example.testing.futures.callable.TaskResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

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

/*    public static void main(String[] args) throws ExecutionException, InterruptedException {

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
    }*/

/*    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Supplier<TaskResult> task1 = () -> org.example.testing.futures.callable.Main.doTask("task1", 3, false);
        Supplier<TaskResult> task2 = () -> org.example.testing.futures.callable.Main.doTask("task2", 4, false);
        Supplier<TaskResult> task3 = () -> org.example.testing.futures.callable.Main.doTask("task3", 5, false);
        Supplier<TaskResult> task4 = () -> org.example.testing.futures.callable.Main.doTask("task4", 6, false);

        // let's run all of them in parallel
        var future1 = CompletableFuture.supplyAsync(task1);
        var future2 = CompletableFuture.supplyAsync(task2);
        var future3 = CompletableFuture.supplyAsync(task3);
        var future4 = CompletableFuture.supplyAsync(task4);

        // now chain the task executions
        CompletableFuture pipeline =
                future1.thenCombine(future2, (result1, result2) -> fuze(result1.name, result2.name))
                        .thenCombine(future3, (s, taskResult) -> fuze(s, taskResult.name))
                        .thenCombine(future4, (s, taskResult) -> fuze(s, taskResult.name))
                        .thenApply(data -> data + " :: Handled apply")
                        .thenAccept(data -> {
                            System.out.println(data + " :: Handled accept");
                        });

        System.out.println(pipeline.get());
    }*/

    // create a pipeline to do the following
    // run task1 and task2 in parallel
    // after they complete, apply a function on the result
    // then run task3 and task4 in parallel
    // after task3 and task4 complete, accept the result
    // total time to run the pipeline should be around 10 seconds
/*    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Supplier<TaskResult> task1 = () -> org.example.testing.futures.callable.Main.doTask("task1", 3, false);
        Supplier<TaskResult> task2 = () -> org.example.testing.futures.callable.Main.doTask("task2", 4, false);
        Supplier<TaskResult> task3 = () -> org.example.testing.futures.callable.Main.doTask("task3", 5, false);
        Supplier<TaskResult> task4 = () -> org.example.testing.futures.callable.Main.doTask("task4", 6, false);

        long start = System.currentTimeMillis();
        // let's run all of them in parallel
        var future1 = CompletableFuture.supplyAsync(task1);
        var future2 = CompletableFuture.supplyAsync(task2);

        // now chain the task executions
        CompletableFuture pipeline =
                future1.thenCombine(future2, (result1, result2) -> fuze(result1.name, result2.name))
                        .thenApply(s -> s + " :: Glue")
                        .thenCompose(s -> {

                            // let's run task3 and task4 in parallel
                            // note we do not start the tasks until task1 and task2 are completed
                            var future3 = CompletableFuture.supplyAsync(task3);
                            var future4 = CompletableFuture.supplyAsync(task4);
                            return future3.thenCombine(future4, (result1, result2) -> s + " :: " + fuze(result1.name, result2.name));
                        })
                        .thenAccept(data -> {
                            System.out.println(data + " :: Handled accept");
                        });
        System.out.println(pipeline.get());

        System.out.println(System.currentTimeMillis() - start);

    }*/

/*    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // tasks we want to run in parallel
        Supplier<TaskResult> task1 = () -> org.example.testing.futures.callable.Main.doTask("task1", 3, false);
        Supplier<TaskResult> task2 = () -> org.example.testing.futures.callable.Main.doTask("task2", 4, false);
        Supplier<TaskResult> task3 = () -> org.example.testing.futures.callable.Main.doTask("task3", 5, false);
        Supplier<TaskResult> task4 = () -> org.example.testing.futures.callable.Main.doTask("task4", 6, false);

        // let's run all of them in parallel
        var future1 = CompletableFuture.supplyAsync(task1);
        var future2 = CompletableFuture.supplyAsync(task2);
        var future3 = CompletableFuture.supplyAsync(task3);
        var future4 = CompletableFuture.supplyAsync(task4);

        // returns a completable future which completes when all 4 futures are completed
        // Note :: allOf(..) does not "wait" for all 4 to complete. It simply returns a completable future
        // even if one of the 4 futures complete exceptionally, the final future will complete exceptionally
        // return type is Void
        CompletableFuture future = CompletableFuture.allOf(future1, future2, future3, future4);

        CompletableFuture<Void> pipeline =
                future.thenAccept(unused -> {
                    System.out.println(List.of(future1.join(), future2.join(), future3.join(), future4.join()));
                })
                        .exceptionally(throwable -> handleErrors(throwable));

    }*/

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // tasks we want to run in parallel
        Supplier<TaskResult> task1 = () -> org.example.testing.futures.callable.Main.doTask("task1", 3, false);
        Supplier<TaskResult> task2 = () -> org.example.testing.futures.callable.Main.doTask("task2", 4, false);
        Supplier<TaskResult> task3 = () -> org.example.testing.futures.callable.Main.doTask("task3", 5, false);
        Supplier<TaskResult> task4 = () -> org.example.testing.futures.callable.Main.doTask("task4", 6, false);

        // let's run all of them in parallel
        var future1 = CompletableFuture.supplyAsync(task1);
        var future2 = CompletableFuture.supplyAsync(task2);
        var future3 = CompletableFuture.supplyAsync(task3);
        var future4 = CompletableFuture.supplyAsync(task4);

        // returns a completable future which completes when any of the 4 futures complete
        // the remaining tasks are not cancelled
        CompletableFuture future = CompletableFuture.anyOf(future1, future2, future3, future4);

        CompletableFuture<Object> pipeline =
                future.thenAccept(result -> {
                            System.out.println("Handling accept :: " + result);
                        })
                        .exceptionally(throwable -> handleErrors(throwable));

    }

    private static Object handleErrors(Object throwable) {
        return null;
    }

    private static String fuze(String s1, String s2) {
        return String.format("Combined (%s : %s)", s1, s2);
    }
}

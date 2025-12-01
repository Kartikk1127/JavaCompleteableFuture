package org.example.testing.callable;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        // here if you submit more than 3 tasks, the remaining tasks will be queued till a thread is free.
/*        try(ExecutorService service = Executors.newFixedThreadPool(3)) {

            Future<TaskResult> future1 = service.submit(() -> doTask("SimpleTask",3, false));
            Future<TaskResult> future2 = service.submit(() -> doTask("SimpleTask",2, false));
            Future<TaskResult> future3 = service.submit(() -> doTask("SimpleTask",1, true));

            // supposed to do some other work

            // in the below try block, notice that the task 2 and 3 execute early since they wait for 2 and 1 seconds, whereas task1 completes in 3 seconds
            // but the below design doesn't allow us to process this until task 1 is completed(since get() is blocking on task1)
            // also if task3 throws an exception, then the calling thread cannot respond to it immediately because it is still waiting on the task1 future.get()
            // hence, even though we are able to execute the tasks concurrently quite efficiently, we are unable to process the results efficiently.
            // THIS IS ONE PROBLEM WITH THE FUTURE. SEE THE README SECTION OF CALLABLE PACKAGE.
            // In order to solve the problem of inefficient handling of the returned results, we will use a class called ExecutorCompletionService which acts like a decorator on any executor service.

            try {
                // handle task1, get() will block till task1 completes
                TaskResult result1 = future1.get();
                System.out.println(result1);

                // get() will block till task2 completes
                TaskResult result2 = future2.get();
                System.out.println(result2);

                // get() will block till task3 completes
                TaskResult result3 = future3.get();
                System.out.println(result3);
            } catch (InterruptedException | ExecutionException exception) {
                System.out.println(exception);
            }
        }*/

        try(ExecutorService service = Executors.newFixedThreadPool(3)) {
            ExecutorCompletionService srv = new ExecutorCompletionService(service);

            Callable<TaskResult> callable1 = () -> doTask("task1",3, false);
            Callable<TaskResult> callable2 = () -> doTask("task2",2, false);
            Callable<TaskResult> callable3 = () -> doTask("task3",1, true);

            Future<TaskResult> future1 = srv.submit(callable1);
            Future<TaskResult> future2 = srv.submit(callable2);
            Future<TaskResult> future3 = srv.submit(callable3);

            try {
                for (int j = 0; j < 2; j++) {

                    // the take() method on ECS will wait till any of the futures is completed
                    // the ECS uses a class called FutureTask to do its job.
                    Future future = srv.take();
                    if (future==future1) {
                        // handle task 1 future
                        System.out.println(future.get());
                    } else if (future== future2) {
                        // handle task 2 future
                        System.out.println(future.get());
                    } else if (future == future3) {
                        // handle task 3 future
                        System.out.println(future.get());
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e);
            }
        }
    }
    public static TaskResult doTask(String name, int secs, boolean fail) {
        System.out.printf("%s : Starting task %s\n", Thread.currentThread().getName(), name);

        try {
            TimeUnit.SECONDS.sleep(secs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (fail) {
            throw new RuntimeException("Task failed");
        }

        System.out.printf("%s : Ending task %s\n", Thread.currentThread().getName(), name);
        return new TaskResult(name, secs);
    }
}

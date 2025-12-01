package org.example.testing.runnable;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("hello");

        // The execution policy is determined by which concrete implementation of executor service you're using :: in this case single thread executor

        try(ExecutorService service = Executors.newSingleThreadExecutor()) {
            Future<?> future = service.submit(Main::doSimpleTask);

            // do other tasks here

            //wait for future to complete
            System.out.println(future.get());
            System.out.println("testing this");
            // do something else
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void doSimpleTask() {
        System.out.printf("%s : Starting simple task\n", Thread.currentThread().getName());

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Task interrupted");
        }

        System.out.printf("%s : Ending simple task \n", Thread.currentThread().getName());
    }
}

package org.example;

import java.util.concurrent.TimeUnit;

public class SimpleRunnable implements Runnable{
    @Override
    public void run() {
        try {
            System.out.println("Starting simple thread");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        System.out.println("Ending simple thread");
    }
}

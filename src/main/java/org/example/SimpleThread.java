package org.example;

import java.util.concurrent.TimeUnit;

public class SimpleThread extends Thread{
    private final int sec;

    SimpleThread(String name, int sec) {
        this.sec = sec;
        this.setName(name);
    }

    @Override
    public void run() {
        System.out.printf("%s : Starting a simple thread\n", this.getName());
        try {
            TimeUnit.SECONDS.sleep(this.sec);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        System.out.printf("%s : Ending simple thread\n", this.getName());
    }
}

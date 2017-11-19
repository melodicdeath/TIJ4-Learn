package org.melodicdeath.concurrency;

import java.util.concurrent.TimeUnit;

public class InterrupTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new Runnable() {
            int count = 0;

            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    System.out.println(count++);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        System.err.println("interrup exception");
                        break;
                    }
                }

                System.out.println("interrup!");
            }
        });

        thread1.start();

        TimeUnit.SECONDS.sleep(5);
        thread1.interrupt();
        TimeUnit.SECONDS.sleep(10);
        System.exit(0);
    }
}

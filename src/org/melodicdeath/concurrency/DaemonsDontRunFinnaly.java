package org.melodicdeath.concurrency;

import java.util.concurrent.TimeUnit;

class ADaemon implements Runnable{

    @Override
    public void run() {
        try {
            System.out.println("Starting ADaemon");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("This should always run?");
        }
    }
}
public class DaemonsDontRunFinnaly {
    public static void main(String[] args) {
        Thread thread = new Thread(new ADaemon());
        thread.setDaemon(true);//当所有非守护线程中断，守护线程会突然中断，因而不会执行finnaly方法
        thread.start();
    }
}

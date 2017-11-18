package org.melodicdeath.concurrency;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}

class DaemonThreadPoolExecutor extends ThreadPoolExecutor {
    public DaemonThreadPoolExecutor() {
        super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new DaemonThreadFactory());
    }
}

class DaemonSpawn implements Runnable {
    @Override
    public void run() {
        while (true)
            Thread.yield();
    }
}

class Daemon implements Runnable {
    private Thread[] threads = new Thread[10];

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new DaemonSpawn());
            threads[i].start();
            System.out.println("DaemonSpawn " + i + " started.");
        }

        for (int i = 0; i < 10; i++) {
            System.out.print("threads[" + i + "].isDaemon() = " + threads[i].isDaemon() + ",");
        }

        while (true)
            Thread.yield();
    }
}

public class Daemons {
    public static void main(String[] args) {
        Thread thread = new Thread(new Daemon());
        thread.setDaemon(true);
        thread.start();
        System.out.println("thread.isDaemon() = " + thread.isDaemon() + ",");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

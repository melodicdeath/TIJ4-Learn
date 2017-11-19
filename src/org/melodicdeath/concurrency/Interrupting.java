package org.melodicdeath.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class SleepBlocked implements Runnable {
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("interrupted exception");
        }

        System.out.println("Exiting SleepBlocked.run()");
    }
}

class IOBlocked implements Runnable {
    private InputStream in;

    IOBlocked(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        System.out.println("waiting for read():");
        try {
            in.read();
        } catch (IOException e) {
            if(Thread.currentThread().isInterrupted()){
                System.out.println("interrupted form blocked I/O");
            }else{
                throw new RuntimeException(e);
            }
        }

        System.out.println("Exiting IOBlocked.run()");
    }
}

class SynchronizedBlocked implements Runnable{
    public synchronized void f(){
        while (true){
            Thread.yield();
        }
    }

    SynchronizedBlocked(){
        new Thread(() -> f()).start();
    }

    @Override
    public void run() {
        System.out.println("Trying to call f()");
        f();
        System.out.println("Exiting SynchronizedBlocked.run()");
    }
}

public class Interrupting {
    private static ExecutorService executorService= Executors.newCachedThreadPool();

    static void test(Runnable r) throws InterruptedException {
        Future<?> future = executorService.submit(r);
        TimeUnit.MICROSECONDS.sleep(100);
        System.out.println("interruping " + r.getClass().getSimpleName());
        future.cancel(true);
        System.out.println("interruping sent to " + r.getClass().getSimpleName());
    }

    public static void main(String[] args) throws InterruptedException {
        //thread.sleep可以中断
        test(new SleepBlocked());
        //IO和Synchronized都不可中断
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());

        TimeUnit.SECONDS.sleep(3);
        System.out.println("Aborting with System.exit(0)");
        System.exit(0);
    }
}

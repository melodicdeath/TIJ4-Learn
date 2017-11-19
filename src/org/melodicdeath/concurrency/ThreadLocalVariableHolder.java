package org.melodicdeath.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Accessor implements Runnable {
    private final int id;

    Accessor(int id) {
        this.id = id;
    }

    ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        @Override
        protected synchronized Integer initialValue() {
            return id;
        }
    };

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            value.set(value.get() + 1);
            System.out.println(this);
            Thread.yield();
        }
    }

    @Override
    public String toString() {
        return "#" + id + ":" + value.get();
    }
}

public class ThreadLocalVariableHolder {
//    private static ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
//        private Random random = new Random();
//
//        @Override
//        protected synchronized Integer initialValue() {
//            return random.nextInt(10000);
//        }
//    };
//
//    public static void increment() {
//        value.set(value.get() + 1);
//    }
//
//    public static int get() {
//        return value.get();
//    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Accessor(i));
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdownNow();
    }
}

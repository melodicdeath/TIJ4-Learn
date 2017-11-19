package org.melodicdeath.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BlockedMutex {
    private Lock lock = new ReentrantLock();

    BlockedMutex() {
        lock.lock();
    }

    public void f() {
        try {
            //已经获取了锁，在第二个任务不能获取锁，除非第一个任务被中断
            //在ReentrantLock上阻塞的任务可以被中断，synchornized方法不能被中断
            lock.lockInterruptibly();
            System.out.println("lock acquired in f()");
        } catch (InterruptedException e) {
            System.out.println("Interrupted form lock acquisition in f()");
        }
    }
}

class Blocked2 implements Runnable {
    BlockedMutex blockedMutex = new BlockedMutex();

    @Override
    public void run() {
        System.out.println("Waiting for f in BlockedMutex");
        blockedMutex.f();
        System.out.println("Broken out of blocked call");
    }
}

public class Interruping2 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Blocked2());
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}

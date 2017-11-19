package org.melodicdeath.concurrency;

public class MultiLock {
    public synchronized void f1(int count) {
        if (count-- > 0) {
            System.out.println("f1() calling f2() with count " + count);
            f2(count);
        }
    }

    public synchronized void f2(int count) {
        if (count-- > 0) {
            System.out.println("f2() calling f1() with count " + count);
            f1(count);
        }
    }

    public static void main(String[] args) {
        MultiLock multiLock = new MultiLock();
        new Thread(() -> {
            multiLock.f1(10);
        }).start();

        // 已经在第一个对f1的调用获得了对象锁，因此同一个任务在对f2的调用将再次获得锁。
    }
}

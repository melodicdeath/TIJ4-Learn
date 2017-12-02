package org.melodicdeath.concurrency;

//Exchanger工具类的使用案例
//本文给出一个简单的例子，实现两个线程之间交换数据，用Exchanger来做非常简单。


import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicReference;

class ThreadA implements Runnable {

    private final Exchanger<Integer> exchanger;

    private final AtomicReference<Integer> last = new AtomicReference<Integer>(
            5);

    public ThreadA(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    public void run() {
        try {
            while (true) {
                last.set(exchanger.exchange(last.get()));
                System.out.println(" After calling exchange. Thread A has value: " + last.get());
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class ThreadB implements Runnable {

    private Exchanger<Integer> exchanger;

    private final AtomicReference<Integer> last = new AtomicReference<Integer>(
            10);

    public ThreadB(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    public void run() {
        try {
            while (true) {
                last.set(exchanger.exchange(last.get()));
                System.out.println(" After calling exchange. Thread B has value: " + last.get());
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

public class ExchangerDemo2 {

    public static void main(String[] args) {
        Exchanger<Integer> exchanger = new Exchanger<Integer>();
        new Thread(new ThreadA(exchanger)).start();
        new Thread(new ThreadB(exchanger)).start();
    }

}
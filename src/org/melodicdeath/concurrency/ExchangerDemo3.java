package org.melodicdeath.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ExchangerDemo3 {

    public static void main(String[] args) {
        Exchanger<List<Integer>> exchanger = new Exchanger<List<Integer>>();
        new Thread1(exchanger).start();
        new Thread2(exchanger).start();
    }

}

class Thread1 extends Thread {
    List<Integer> list = new ArrayList<Integer>();
    Exchanger<List<Integer>> exchanger = null;

    public Thread1(Exchanger<List<Integer>> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        Random rand = new Random();
        list.add(rand.nextInt(10000));
        list.add(rand.nextInt(10000));
        list.add(rand.nextInt(10000));
        list.add(rand.nextInt(10000));
        list.add(rand.nextInt(10000));
        System.out.println("\nThread1:list-->" + list.size() + "\n" + list);
        for (int i = 0; i < 10; i++) {
            try {
                list = exchanger.exchange(list);
                System.out.println("\nThread1:sizeoflist-->" + list.size()
                        + "\n" + list);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

class Thread2 extends Thread {
    List<Integer> list = new ArrayList<Integer>();
    Exchanger<List<Integer>> exchanger = null;

    public Thread2(Exchanger<List<Integer>> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Random rand = new Random();
        list.add(rand.nextInt(10000));
        list.add(rand.nextInt(10000));
        list.add(rand.nextInt(10000));
        list.add(rand.nextInt(10000));
        list.add(rand.nextInt(10000));
        System.out.println("\nThread2:list-->" + list.size() + "\n" + list);
        for (int i = 0; i < 10; i++) {
            try {
                list = exchanger.exchange(list);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("\nThread2:sizeoflist-->" + list.size() + "\n"
                    + list);
        }
    }
}
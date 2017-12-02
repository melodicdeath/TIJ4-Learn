package org.melodicdeath.concurrency;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Pool<T> {
    private int size;
    private List<T> items = new ArrayList<>();
    private volatile boolean[] checkedOut;
    private Semaphore avalibale;

    public Pool(Class<T> clazz, int size) {
        this.size = size;
        checkedOut = new boolean[size];
        avalibale = new Semaphore(size, true);

        for (int i = 0; i < size; i++) {
            try {
                items.add(clazz.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public T checkOut() throws InterruptedException {
        avalibale.acquire();
        return getItem();
    }

    public void checkIn(T x) {
        if (releaseItem(x)) {
            avalibale.release();
        }
    }

    private synchronized T getItem() {
        for (int i = 0; i < size; i++) {
            if (!checkedOut[i]) {
                checkedOut[i] = true;
                return items.get(i);
            }
        }

        return null;
    }

    private synchronized boolean releaseItem(T item) {
        int index = items.indexOf(item);
        if (index == -1)
            return false;
        if (checkedOut[index]) {
            checkedOut[index] = false;
            return true;
        }
        return false;
    }
}

class Fat {
    private volatile double d;
    private static int counter = 0;
    private final int id = counter++;

    public Fat() {
        for (int i = 0; i < 10000; i++) {
            d += (Math.PI + Math.E) / (double) i;
        }
    }

    public void operation() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Fat id: " + id;
    }
}

class CheckoutTask<T> implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private Pool<T> pool;

    public CheckoutTask(Pool<T> pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            T item = pool.checkOut();
            System.out.println(this + " checked out " + item);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(this + "checking in " + item);
            pool.checkIn(item);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "CheckoutTask " + id + " ";
    }
}

class SemaphoreDemo {
    final static int SIZE = 25;

    public static void main(String[] args) throws InterruptedException {
        final Pool<Fat> pool = new Pool<>(Fat.class, SIZE);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < SIZE; i++) {
            executorService.execute(new CheckoutTask<>(pool));
        }

        System.out.println("All CheckoutTasks created.");

        List<Fat> fatList = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            Fat fat = pool.checkOut();
            System.out.println(i + ": main() thread checked out");
            fat.operation();
            fatList.add(fat);
        }

        Future<?> blocked = executorService.submit(() -> {
            try {
                pool.checkOut();
            } catch (InterruptedException e) {
                System.out.println("checkOut() interrupted");
            }
        });

        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true);
        System.out.println("Checking in objects in " + fatList);
        for (Fat fat : fatList) {
            pool.checkIn(fat);
        }
        for (Fat fat : fatList) {
            pool.checkIn(fat);
        }

        executorService.shutdown();
    }
}

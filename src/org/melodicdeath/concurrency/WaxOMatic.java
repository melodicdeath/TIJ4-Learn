package org.melodicdeath.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Car {
    private boolean waxOn = false;

    public synchronized void waxed() {
        waxOn = true;
        notifyAll();
    }

    public synchronized void buffed() {
        waxOn = false;
        notifyAll();
    }

    public synchronized void waitForWaxing() throws InterruptedException {
        while (!waxOn)
            wait();
    }

    public synchronized void waitingForBuffing() throws InterruptedException {
        while (waxOn)
            wait();
    }
}

class WaxOn implements Runnable {
    private Car car;

    public WaxOn(Car c) {
        car = c;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.print("Wax On! ");
                car.waxed();
                TimeUnit.MILLISECONDS.sleep(1000);
                car.waitingForBuffing();
            }
        } catch (InterruptedException e) {
            System.err.println("Exiting via interrupt");
        }

        System.out.println("Ending Wax On task");
    }
}

class WaxOff implements Runnable {
    private Car car;

    public WaxOff(Car c) {
        this.car = c;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                car.waitForWaxing();
                System.out.print("Wax Off! ");
                TimeUnit.MILLISECONDS.sleep(1000);
                car.buffed();
            }
        } catch (InterruptedException e) {
            System.err.println("Exiting via interrupt");
        }

        System.out.println("Ending Wax Off task");
    }
}

public class WaxOMatic {
    public static void main(String[] args) throws InterruptedException {
        Car car = new Car();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new WaxOn(car));
        executorService.execute(new WaxOff(car));

        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();
    }
}

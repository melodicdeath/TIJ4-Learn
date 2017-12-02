package org.melodicdeath.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class Horse implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private int strides = 0;
    private static Random random = new Random(47);
    private static CyclicBarrier barrier;

    public Horse(CyclicBarrier cyclicBarrier) {
        barrier = cyclicBarrier;
    }

    public synchronized int getStrides() {
        return strides;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    strides += random.nextInt(3);
                }
                barrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Horse " + id + " ";
    }

    public String tracks() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            stringBuilder.append("*");
        }
        stringBuilder.append(id);
        return stringBuilder.toString();
    }
}

public class HorseRace {
    static final int FINSH_LINE = 75;
    private List<Horse> horseList = new ArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private CyclicBarrier cyclicBarrier;

    public HorseRace(int horses, final int pause) {
        cyclicBarrier = new CyclicBarrier(horses, () -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < FINSH_LINE; i++) {
                stringBuilder.append("=");
            }
            System.out.println(stringBuilder.toString());

            for (Horse horse : horseList) {
                System.out.println(horse.tracks());
            }

            for (Horse horse : horseList) {
                if (horse.getStrides() >= FINSH_LINE) {
                    System.out.println(horse + "won!");
                    executorService.shutdownNow();
                    return;
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                    System.out.println("barrier-action sleep interrupted");
                }
            }
        });

        for (int i = 0; i < horses; i++) {
            Horse horse = new Horse(cyclicBarrier);
            horseList.add(horse);
            executorService.execute(horse);
        }
    }

    public static void main(String[] args) {
        int horses = 7;
        int pause = 200;
        new HorseRace(horses, pause);
    }
}

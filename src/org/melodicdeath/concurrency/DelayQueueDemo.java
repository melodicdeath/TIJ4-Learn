package org.melodicdeath.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

class DelayedTask implements Runnable, Delayed {
    private static int counter = 0;
    private final int id = counter++;
    private final int delta;
    private final long trigger;

    protected static List<DelayedTask> sequence = new ArrayList<>();

    public DelayedTask(int delayInMilliseconds) {
        delta = delayInMilliseconds;
        trigger = System.nanoTime() + NANOSECONDS.convert(delta, MILLISECONDS);
        sequence.add(this);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger - System.nanoTime(), NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask) o;
        if (trigger < that.trigger)
            return -1;
        if (trigger > that.trigger)
            return 1;

        return 0;
    }

    @Override
    public void run() {
        System.out.print(this + " ");
    }

    @Override
    public String toString() {
        return String.format("[%1$-4d]", delta) + " Task " + id;
    }

    public String summary() {
        return "(" + id + ":" + delta + ")";
    }

    public static class EndSentinel extends DelayedTask {
        private ExecutorService executorService;

        public EndSentinel(int delayInMilliseconds, ExecutorService executorService) {
            super(delayInMilliseconds);
            this.executorService = executorService;
        }

        @Override
        public void run() {
            System.out.println("");
            for (DelayedTask delayedTask : sequence) {
                System.out.print(delayedTask.summary() + " ");
            }
            System.out.println("\n" + this + " Calling shutdownNow()");
            executorService.shutdownNow();
        }
    }
}

class DelayedTaskConsumer implements Runnable {
    private DelayQueue<DelayedTask> delayedTaskDelayQueue;

    public DelayedTaskConsumer(DelayQueue<DelayedTask> delayedTaskDelayQueue) {
        this.delayedTaskDelayQueue = delayedTaskDelayQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                delayedTaskDelayQueue.take().run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished DelayedTaskConsumer");
    }
}

public class DelayQueueDemo {
    public static void main(String[] args) {
        Random random = new Random(47);
        ExecutorService executorService = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> delayedTaskDelayQueue = new DelayQueue<>();
        for (int i = 0; i < 20; i++) {
            delayedTaskDelayQueue.put(new DelayedTask(random.nextInt(5000)));
        }
        delayedTaskDelayQueue.add(new DelayedTask.EndSentinel(5000, executorService));
        executorService.execute(new DelayedTaskConsumer(delayedTaskDelayQueue));
    }
}

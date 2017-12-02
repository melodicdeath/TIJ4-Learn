package org.melodicdeath.concurrency;

import java.util.List;
import java.util.concurrent.*;

class ExchangerConsumer<T> implements Runnable {
    private Exchanger<List<T>> listExchanger;
    private List<T> holder;
    private volatile T value;

    public ExchangerConsumer(Exchanger<List<T>> listExchanger, List<T> holder) {
        this.listExchanger = listExchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                holder = listExchanger.exchange(holder);
                for (T x : holder) {
                    value = x;
                    holder.remove(x);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final value: " + value);
    }
}

interface Generator<T> {
    T next();
}

class BasicGenerator<T> implements Generator<T> {
    private Class<T> type;

    public BasicGenerator(Class<T> type) {
        this.type = type;
    }

    public T next() {
        try {
            // Assumes type is a public class:
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Produce a Default generator given a type token:
    public static <T> Generator<T> create(Class<T> type) {
        return new BasicGenerator<T>(type);
    }
}

class ExchangerProducter<T> implements Runnable {
    private Generator<T> generator;
    private Exchanger<List<T>> listExchanger;
    private List<T> holder;

    public ExchangerProducter(Generator<T> generator, Exchanger<List<T>> listExchanger, List<T> holder) {
        this.generator = generator;
        this.listExchanger = listExchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            for (int i = 0; i < ExchangerDemo.size; i++) {
                holder.add(generator.next());
                try {
                    holder = listExchanger.exchange(holder);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class ExchangerDemo {
    static int size = 10;
    static int delay = 5;

    public static void main(String[] args) throws InterruptedException {
        Exchanger<List<Fat>> fatListExchanger = new Exchanger<>();
        List<Fat> producterList = new CopyOnWriteArrayList<>();
        List<Fat> consumerList = new CopyOnWriteArrayList<>();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new ExchangerProducter<>(BasicGenerator.create(Fat.class), fatListExchanger, producterList));
        executorService.execute(new ExchangerConsumer<>(fatListExchanger, consumerList));

        TimeUnit.SECONDS.sleep(delay);

        executorService.shutdownNow();
    }
}

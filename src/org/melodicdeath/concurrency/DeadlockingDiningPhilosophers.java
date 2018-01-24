package org.melodicdeath.concurrency;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
* 哲学家就餐问题
* 5个哲学家，他们将花部分时间思考、部分时间就餐。每两个人间放置一根筷子。
* 当他们思考的时候不需要共享任何资源；但当他们就餐的时候需要同时使用左右两根筷子。
* 如果左边或右边已经有人在使用筷子，那么这个哲学家就必须等待，直至可得到必需的筷子。
*
* 死锁发生的条件：
* 1.互斥条件：任务使用的资源中至少有一个是不能共享的。这里，一根Chopstick就一次只能被一个哲学家使用。
* 2.请求与保持条件：至少有一个任务必须持有一个资源且正在等待获取一个当前被别的任务持有的资源。也就是说，
* 要发生死锁，哲学家必须拿着一根筷子并且等待另一根。
* 3.不剥夺条件：资源不能被任务抢占，任务必须把资源当做普通事件。哲学家很有礼貌，他们不会从另一个哲学家手机抢筷子。
* 4.循环等待条件：必须有循环等待，这是，一个任务等待其他任务所持有的资源，后者又在等待另一个任务所持有的资源，这样一直下去，
* 知道有一个任务在等待第一个任务所持有的资源，使得大家都被锁住。在本例中，因为每个哲学家都试图先得到右边的筷子，
* 然后得到左边的筷子，所以发生了循环等待。
*
* 要发生死锁的话，所有这些条件都必须全部满足；所以要防止死锁的话，只需破坏其中的一个即可。在程序中，防止死锁最容易的
* 办法是破坏第4个条件。有这个条件的原因是每个哲学家都试图用先右后左的顺序拿筷子。正因为如此，就可能发生"每人都拿着右边
* 的筷子并等待左边的筷子"的情况，这就是循环等待条件。然而，如果最有一个哲学家被初始化为先拿左边的筷子，再拿右边的筷子，
* 那么这个哲学家将永远不会阻止其右边的哲学家拿起他们的筷子。
*
* 这只是问题的解决方案之一，也可以通过破坏其他条件来防止死锁。
* */

class Chopstick {
    private boolean taken = false;

    public synchronized void take() throws InterruptedException {
        while (taken)
            wait();
        taken = true;
    }

    public synchronized void drop() {
        taken = false;
        notifyAll();
    }
}

class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponerFactor;
    private Random random = new Random(47);

    private void pause() throws InterruptedException {
        if (ponerFactor == 0)
            return;
        TimeUnit.MILLISECONDS.sleep(random.nextInt(ponerFactor * 250));
    }

    public Philosopher(Chopstick left, Chopstick right, int id, int ponerFactor) {
        this.left = left;
        this.right = right;
        this.id = id;
        this.ponerFactor = ponerFactor;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(this + " thinking");
                pause();
                System.out.println(this + " grabbing right");
                right.take();
                System.out.println(this + " grabbing left");
                left.take();
                System.out.println(this + " eating");
                pause();
                right.drop();
                left.drop();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}

public class DeadlockingDiningPhilosophers {
    public static void main(String[] args) throws InterruptedException, IOException {
        int ponder = 5;
        if (args.length > 0) {
            ponder = Integer.parseInt(args[0]);
        }

        int size = 5;
        if (args.length > 1) {
            size = Integer.parseInt(args[1]);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        Chopstick[] chopsticks = new Chopstick[size];
        for (int i = 0; i < size; i++) {
            chopsticks[i] = new Chopstick();
        }
        for (int i = 0; i < size; i++) {
            if (i < size - 1)
                executorService.execute(new Philosopher(chopsticks[i], chopsticks[(i + 1) % size], i, ponder));
            else
                executorService.execute(new Philosopher(chopsticks[(i + 1) % size], chopsticks[i], i, ponder));
        }

        if (args.length == 3 && args[2].equals("timeout")) {
            TimeUnit.SECONDS.sleep(5);
        } else {
            System.out.println("Press 'Enter' to quit");
            System.in.read();
        }

        executorService.shutdownNow();
    }
}



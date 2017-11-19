package org.melodicdeath.concurrency;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Pair {
    private int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void incrementX() {
        x++;
    }

    public void incrementY() {
        y++;
    }

    @Override
    public String toString() {
        return "x:" + x + ",y:" + y;
    }

    class PairValuesNotEqualException extends RuntimeException {
        PairValuesNotEqualException() {
            super("Pair values not equal:" + Pair.this);
        }
    }

    public void checkState() {
        if (x != y)
            throw new PairValuesNotEqualException();
    }
}

abstract class PairManager {
    AtomicInteger checkCounter = new AtomicInteger(0);
    protected Pair p = new Pair();
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<>());

    public synchronized Pair getPair() {
        return new Pair(p.getX(), p.getY());
    }

    protected void store(Pair p) {
        storage.add(p);
        try {
            TimeUnit.MICROSECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void increment();
}

class PairManager1 extends PairManager {

    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}

class PairManager2 extends PairManager {

    @Override
    public void increment() {
        Pair temp;
        // 查看运行结果发现同步块加锁比方法加锁时间更短，短到夸张。这也是宁愿使用同步块而不是同步方法的原因：
        // 使得其他线程能更多的访问（在安全的情况下尽可能多）
        synchronized (this) {
            p.incrementY();
            p.incrementX();
            temp = getPair();
        }

        store(temp);
    }
}

class PairManager3 extends PairManager {
    private Lock lock = new ReentrantLock();

    @Override
    public synchronized void increment() {
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            store(getPair());
        } finally {
            lock.unlock();
        }
    }
}

class PairManager4 extends PairManager {
    private Lock lock = new ReentrantLock();

    @Override
    public void increment() {
        Pair temp;
        lock.lock();//只用lock不用synchronized会出现问题？
        try {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        } finally {
            lock.unlock();
        }

        store(temp);
    }
}

class PairManipulator implements Runnable {
    private PairManager pm;

    public PairManipulator(PairManager pm) {
        this.pm = pm;
    }

    @Override
    public void run() {
        while (true)
            pm.increment();
    }

    @Override
    public String toString() {
        return "Pair: " + pm.getPair() + " checkCounter = " + pm.checkCounter.get();
    }
}

class PairChecker implements Runnable {
    private PairManager pm;

    public PairChecker(PairManager pm) {
        this.pm = pm;
    }

    @Override
    public void run() {
        while (true) {
            pm.checkCounter.incrementAndGet();
            pm.getPair().checkState();
        }
    }
}

public class CriticalSection {
    static void testApproaches(PairManager pairManager1, PairManager pairManager2, PairManager pairManager3, PairManager pairManager4) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        PairManipulator pairManipulator1 = new PairManipulator(pairManager1);
        PairManipulator pairManipulator2 = new PairManipulator(pairManager2);
        PairManipulator pairManipulator3 = new PairManipulator(pairManager3);
        PairManipulator pairManipulator4 = new PairManipulator(pairManager4);
        PairChecker pairChecker1 = new PairChecker(pairManager1);
        PairChecker pairChecker2 = new PairChecker(pairManager2);
        PairChecker pairChecker3 = new PairChecker(pairManager3);
        PairChecker pairChecker4 = new PairChecker(pairManager4);

        executorService.execute(pairManipulator1);
        executorService.execute(pairManipulator2);
        executorService.execute(pairManipulator3);
        executorService.execute(pairManipulator4);
        executorService.execute(pairChecker1);
        executorService.execute(pairChecker2);
        executorService.execute(pairChecker3);
        executorService.execute(pairChecker4);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("pm1: " + pairManipulator1 + "\npm2: " +
                pairManipulator2 + "\npm3: " + pairManipulator3 + "\npm4: " + pairManipulator4);
        System.exit(0);
    }

    public static void main(String[] args) {
        PairManager pairManager1 = new PairManager1(),
                pairManager2 = new PairManager2(),
                pairManager3 = new PairManager3(),
                pairManager4 = new PairManager4();
        testApproaches(pairManager1, pairManager2, pairManager3, pairManager4);
    }
}

package org.melodicdeath.concurrency;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class InterrupTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new Runnable() {
            int count = 0;

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println(count++);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        //抛出异常，清除中断，线程继续运行，继续打印
                        System.err.println("interrup exception.clear status.");

                        //恢复中断状态（继续保持中断）
                        Thread.currentThread().interrupt();
                    }
                }

                System.out.println("interruped: " + Thread.currentThread().isInterrupted());

                try {
                    System.out.println(LocalDateTime.now());
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println(LocalDateTime.now());
                } catch (InterruptedException e) {
                    System.err.println("interruped");//这里又会清除中断状态,tag:1又会打印false
                }
                System.out.println("done.");
            }
        });

        thread1.start();

        TimeUnit.SECONDS.sleep(5);
        System.out.println("threa1 isInterrupted:" + thread1.isInterrupted());
        thread1.interrupt();
        System.out.println("threa1 isInterrupted:" + thread1.isInterrupted());
        TimeUnit.SECONDS.sleep(2);
        //tag:1
        System.out.println("threa1 isInterrupted:" + thread1.isInterrupted());

        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }
}

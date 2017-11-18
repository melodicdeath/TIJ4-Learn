package org.melodicdeath.concurrency;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimpleDaemons implements Runnable {
    @Override
    public void run() {
        while (true){
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread() + " " + this);
            } catch (InterruptedException e) {
                System.err.println("sleep interruped");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<10;i++){
//            Thread daemo = new Thread(new SimpleDaemons());
//            daemo.setDaemon(true);//守护线程在主线程退出时会被终止，如果不设置的话则不会
//            daemo.start();

            //默认是非守护线程
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new SimpleDaemons());
            executorService.shutdown();
        }

        System.out.println("all daemons started");
        TimeUnit.SECONDS.sleep(5);
    }
}

package com.cyk.es.test;/**
 * Created by zhangshipeng on 3/8/2018.
 */

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @author Administrator
 * @ClassName: TestJoker
 * @date 2018-03-08 10:04 AM
 **/
public class TestJoker {


    public static void main(String[] args) {
        test2();

    }

    static void test1(){

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,2,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " run");

            }
        };


        executor.execute(myRunnable);
        executor.execute(myRunnable);
        executor.execute(myRunnable);
        System.out.println("---先开三个---");
        System.out.println("核心线程数" + executor.getCorePoolSize());
        System.out.println("线程池数" + executor.getPoolSize());
        System.out.println("队列任务数" + executor.getQueue().size());
        executor.execute(myRunnable);
        executor.execute(myRunnable);
        executor.execute(myRunnable);
        System.out.println("---再开三个---");
        System.out.println("核心线程数" + executor.getCorePoolSize());
        System.out.println("线程池数" + executor.getPoolSize());
        System.out.println("队列任务数" + executor.getQueue().size());
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("----8秒之后----");
        System.out.println("核心线程数" + executor.getCorePoolSize());
        System.out.println("线程池数" + executor.getPoolSize());
        System.out.println("队列任务数" + executor.getQueue().size());

        executor.shutdown();






    }


    static void test2(){
        ExecutorService executorService = Executors.newScheduledThreadPool(4);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                DateFormat ddtf = DateFormat.getDateTimeInstance();
                System.out.println(ddtf.format(new Date()));

            }

        },3,2,TimeUnit.SECONDS);


    }


}

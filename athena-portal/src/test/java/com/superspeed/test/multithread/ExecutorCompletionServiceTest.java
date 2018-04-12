package com.superspeed.test.multithread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.*;

/**
 * ExecutorCompletionServiceTest
 *
 * Created by yanweiwen on 2018/4/12.
 */
public class ExecutorCompletionServiceTest {

    public static void main(String[] args) throws Throwable {

        int maxThreadNum = 10;

        ExecutorCompletionService<String> executorCompletionService = new ExecutorCompletionService<>(MyThreadPool.getExecutor());

        for (int i = 1; i <= maxThreadNum; i++) {
            Thread.sleep(100l);
            executorCompletionService.submit(new MyTask(i));
        }
        for (int i = 1; i <= maxThreadNum; i++)
            System.out.println(executorCompletionService.take().get());
        MyThreadPool.getExecutor().shutdown();
    }
}

class MyThreadPool {

    private static class exe {

        private static NamedThreadFactory threadFactory = new NamedThreadFactory.Builder()
                .daemon(false).namingPattern("MyThread-pool-%d").build();

        private static ExecutorService executor = new ThreadPoolExecutor(16, 50,
                30L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100, true), threadFactory);
    }

    private MyThreadPool() {
    }

    public static ExecutorService getExecutor() {
        return exe.executor;
    }
}

class MyTask implements Callable<String> {

    private volatile int i;

    MyTask(int i) {
        this.i = i;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(1000l);
        return Thread.currentThread().getName() + "任务 :" + i;
    }

}

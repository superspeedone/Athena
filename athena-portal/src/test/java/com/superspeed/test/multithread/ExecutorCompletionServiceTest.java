package com.superspeed.test.multithread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.*;

/**
 * ExecutorCompletionServiceTest
 *
 * submit tasks to thread pool and get all executing results.
 *
 * Created by yanweiwen on 2018/4/12.
 */
public class ExecutorCompletionServiceTest {

    public static void main(String[] args) throws Throwable {

        int maxThreadNum = 100;
        // 读写安全ArrayList,采用ReentrantLock实现锁
        CopyOnWriteArrayList<Future<String>> futureList = new CopyOnWriteArrayList<>();
        // 线程池
        ExecutorService executor = ExecutorServiceFactory.getExecutor();
        // create object of ExecutorCompletionService，实现将任务执行结果按照任务提交顺序进行存放
        ExecutorCompletionService<String> executorCompletionService = new ExecutorCompletionService<>(executor);
        // add task
        for (int i = 0; i < maxThreadNum; i++) {
            futureList.add(executorCompletionService.submit(new CallableTask(i+1)));
        }
        // print result of task executing
        for (int i = 0; i < maxThreadNum; i++) {
            //System.out.println(executorCompletionService.take().get());
            System.out.println(futureList.get(i).get());
        }
        // close threadPool
        ExecutorServiceFactory.getExecutor().shutdown();
    }
}

class ExecutorServiceFactory {

    private static class ThreadPool {

        private static int corePoolSize = 10;
        private static int maximumPoolSize = 50;
        private static long keepAliveTime = 30L;

        // custom thread naming factory
        private static NamedThreadFactory threadFactory = new NamedThreadFactory.Builder()
                .daemon(true)
                .namingPattern("Custom-thread-pool-%d")
                .build();

        // create threadPool
        private static ExecutorService executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(maximumPoolSize, true),
                threadFactory);
    }

    private ExecutorServiceFactory() {
    }

    public static ExecutorService getExecutor() {
        return ThreadPool.executor;
    }
}

/**
 * custom task
 */
class CallableTask implements Callable<String> {

    private final int i;

    CallableTask(int i) {
        this.i = i;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(1000l);
        return String.format("%d Thread[%s] is executing, TaskOrder -> [%d].",
                System.currentTimeMillis(),
                Thread.currentThread().getName() , i);
    }

}

package com.superspeed.test.multithread.cas;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 采用多线程测试锁竞争
 * 采用CAS乐观锁实现
 * 创建相同数量的元素，对比CAS多线程和单线程消耗时间
 */
public class NoneBlockStackTest {

    public static void main(String[] args) {
        AtomicLong costTime = new AtomicLong(0);
        NoneBlockStack<Long> noneBlockStack = new NoneBlockStack<Long>();

        Integer nodeNum = 1000;
        Integer threadNum = 2;

        // 添加元素节点
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new PushNodeTask(noneBlockStack, costTime, nodeNum, threadNum));
        System.out.printf("[MultiThread] Push %d node cost %d ms \n", nodeNum, costTime.get());


        long startTime = System.currentTimeMillis();
        for (int i = 0; i < nodeNum; i++) {
            Long item = noneBlockStack.pop();
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("[SingleThread] Push %d node cost %d ms \n", nodeNum, endTime - startTime);

        /*startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            Long item = noneBlockStack.pop();
            //System.out.println(item);
        }
        endTime = System.currentTimeMillis();
        System.out.printf("Pop %d node cost %d ms \n", 100000, endTime - startTime);*/
    }

    static class PushNodeTask extends RecursiveAction {

        private final NoneBlockStack<Long> noneBlockStack;
        private final AtomicLong costTime;
        // 剩余要创建的节点数量
        private final Integer nodeNum;
        // 需要创建的线程数
        private final Integer threadNum;

        public PushNodeTask(NoneBlockStack<Long> noneBlockStack, AtomicLong costTime,
                            Integer nodeNum, Integer threadNum) {
            this.noneBlockStack = noneBlockStack;
            this.costTime = costTime;
            this.nodeNum = nodeNum;
            this.threadNum = threadNum;
        }

        /**
         * The main computation performed by this task.
         */
        @Override
        protected void compute() {
            // 保护
            if (nodeNum == 0 || threadNum == 0) {
                return;
            }
            // 根据当前线程数创建线程
            // 判断当前节点数量是否大于1
            if (threadNum > 1) {
                // 每个线程创建的堆栈节点数量
                int num = nodeNum / threadNum;
                // 是否整除
                boolean isDivisible = nodeNum % threadNum == 0;

                for (int i = 1; i <= threadNum; i++) {
                    if (threadNum == i && !isDivisible) {
                        invokeAll(new PushNodeTask(noneBlockStack, costTime, nodeNum - (i - 1) * num, 1));
                    } else {
                        invokeAll(new PushNodeTask(noneBlockStack, costTime, num, 1));
                    }
                }
            } else {
                // 当线程数为1时，开始执行创建堆栈节点任务
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < nodeNum; i++) {
                    noneBlockStack.push(Long.valueOf(i));
                }
                long endTime = System.currentTimeMillis();
                long during = endTime - startTime;
                costTime.addAndGet(during);
            }
        }
    }


}

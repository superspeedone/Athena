package com.superspeed.test.multithread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class ForJoinDemo {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        forkJoinPool.invoke(new AddTask(nums, atomicInteger));
        System.out.println(atomicInteger);

    }

    static class AddTask extends RecursiveAction {

        private final List<Integer> nums;

        private final AtomicInteger atomicInteger;

        public AddTask(List<Integer> nums, AtomicInteger atomicInteger) {
            this.nums = nums;
            this.atomicInteger = atomicInteger;
        }

        @Override
        protected void compute() {
            int size = nums.size();

            if (size > 1) {
                int part = size / 2;

                List<Integer> leftpart = nums.subList(0, part);
                AddTask lefttask = new AddTask(leftpart, atomicInteger);

                List<Integer> rightpart = nums.subList(part, size);
                AddTask rigthtask = new AddTask(rightpart, atomicInteger);

                invokeAll(lefttask, rigthtask); //fork/invoke/join

            } else {
                if (size == 0) {
                    return;
                }
                int num = nums.get(0);
                System.out.println(num);
                atomicInteger.addAndGet(num);
            }

        }
    }

}

package com.superspeed.test.jvm;

import java.util.ArrayList;
import java.util.List;

public class JConsoleTest {

    static class OOMObject {
        public byte[] placehoder = new byte[1024*1024];
    }
    
    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();

        for (int i = 0; i < num; i++) {
            Thread.sleep(2000);
            list.add(new OOMObject());
        }

        //System.gc();
    }

    /**
     * 死循环测试
     */
    public static void createBusyThread() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true);
            }
        }, "testBusyThread");
        thread.start();
    }

    /**
     * 锁等待测试
     */
    public static void createLockThread(final Object lock) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "testLockThread");
        thread.start();
    }

    /**
     * 死锁测试
     */
    static class SynAddRunnable implements Runnable {
        int a,b;

        public SynAddRunnable() {}

        public SynAddRunnable(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a+b);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 垃圾回收
        /*try {
            fillHeap(30);
            System.gc();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // 锁等待
        /*Object lock = new Object();
        createLockThread(lock);*/

        // 死锁测试
        /*for (int i = 1; i <= 200; i++) {
            new Thread(new SynAddRunnable(1, 2), "SynAddRunnable-" + (i * 2 - 1)).start();
            new Thread(new SynAddRunnable(2, 1), "SynAddRunnable-" + i * 2).start();
        }*/

        OOMObject object;

        for (int i = 0; i < 30; i++) {
            Thread.sleep(30000);
            object = new OOMObject();
        }

    }

}

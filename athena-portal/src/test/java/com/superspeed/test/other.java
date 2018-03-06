package com.superspeed.test;

import com.superspeed.test.multithread.LinkedBlockingQueue;

import java.util.Iterator;
import java.util.Vector;

public class other {

    public static void main(String[] args) {
        /*long time = 1509693464246L;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sf.format(new Date(time)));*/

        LinkedBlockingQueue queue = new LinkedBlockingQueue();

        for (int i = 0; i < 20; i++) {
            try {
                queue.offer(new Object());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        final java.util.concurrent.LinkedBlockingQueue blockingQueue = new java.util.concurrent.LinkedBlockingQueue(10);

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    blockingQueue.offer(new Object());
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    blockingQueue.offer(new Object());
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        System.out.println(thread1.getState());
        System.out.println(thread2.getState());


    }


    public static void vectorSafe() {
        final Vector<Integer> vectors = new Vector<>();

        for (int i = 0; i < 1000; i++) {
            vectors.add(i);
        }

        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                Iterator<Integer> iterator = vectors.iterator();

                while (iterator.hasNext()) {
                    if (i == 200) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    iterator.next();
                    i++;
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                vectors.remove(new Integer(280));
            }
        });


        thread1.start();
        thread2.start();
    }
}

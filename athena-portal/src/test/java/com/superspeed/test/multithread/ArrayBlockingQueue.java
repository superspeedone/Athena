package com.superspeed.test.multithread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟阻塞队列
 * @author xc.yanww
 *
 */
public class ArrayBlockingQueue {
	private Lock lock = new ReentrantLock();
	private Condition notEmpty = lock.newCondition();
	private Condition notFull = lock.newCondition();
	private final List<Object> items = new ArrayList<Object>();
	private int default_capacity = 10;



	public Object take() throws InterruptedException {
		lock.lock();

		try {
			if (items.size() == 0) {
				notEmpty.await();
			}

			if (items.size() == default_capacity) {
				notFull.signalAll();
			}

			return items.remove(0);

		} finally {
			lock.unlock();
		}

	}
	
	public void offer(Object object) throws InterruptedException {
		lock.lock();

		try {

			if (items.size() == 0) {
				items.add(object);
				notEmpty.signalAll();
			}

			if (items.size() == default_capacity) {
				notFull.await();
			}

		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue();
		arrayBlockingQueue.offer(new Integer(1));
		arrayBlockingQueue.offer(new Integer(2));
		arrayBlockingQueue.offer(new Integer(3));
		System.out.println(arrayBlockingQueue.take());
		arrayBlockingQueue.offer(new Integer(4));
		System.out.println(arrayBlockingQueue.take());
	}

}

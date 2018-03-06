package com.superspeed.test.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟阻塞队列
 * @author xc.yanww
 *
 */
public class LinkedBlockingQueue {
	private Lock lock = new ReentrantLock();
	private Condition notEmpty = lock.newCondition();
	private Condition notFull = lock.newCondition();
	private Queue<Object> linkedList = new LinkedList<Object>();
	private int default_capacity = 10;

	public Object take() throws InterruptedException {
		lock.lock();

		try {
			if (linkedList.size() == 0) {
				notEmpty.await();
			}

			if (linkedList.size() == default_capacity) {
				notFull.signalAll();
			}

			return linkedList.poll();

		} finally {
			lock.unlock();
		}

	}
	
	public void offer(Object object) throws InterruptedException {
		lock.lock();

		try {

			if (linkedList.size() == 0) {
				linkedList.add(object);
				notEmpty.signalAll();
			}

			if (linkedList.size() == default_capacity) {
				notFull.await();
			}

		} finally {
			lock.unlock();
		}

	}

}

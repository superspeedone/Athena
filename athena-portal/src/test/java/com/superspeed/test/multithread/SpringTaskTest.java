package com.superspeed.test.multithread;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 使用BlockingQueue实现阻塞定时任务 （模拟spring-task阻塞定时任务）
 * 上一个线程任务未执行完毕，下一个任务等待上一个任务执行完毕之后才会执行
 * @author xc.yanww
 *
 */
public class SpringTaskTest {
	
	/**
	 * 创建阻塞队列
	 */
	final BlockingQueue<Object> blockingQ = new ArrayBlockingQueue<Object>(10);
	/**
	 * 任务序号
	 */
	final Map<String, Integer> index = new HashMap<String, Integer>();
	
	public void addTask() {
		//定时任务 ，生产者，添加任务
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				synchronized (blockingQ) {
					String now = String.valueOf(System.currentTimeMillis());
					if (blockingQ.size() < 10) {//队列不满，则添加任务
						//添加需要同步块，避免线程不安全导致的数据问题
						synchronized (index) {
							System.out.println("添加定时任务 - " + now);
							blockingQ.notifyAll();
							Integer indexVal = (null == index.get("index") ? 1 : index.get("index").intValue() + 1);
							index.put("index", indexVal);
							blockingQ.add("执行第" + indexVal + "个定时定时任务");
						}
					} else {
						System.out.println("任务队列任务已满 - " + now);
					}
				}
			}
		}, 0, 1000);
	}
	
	//消费者，执行任务
	/*public void run() {
		Thread thread = new Thread("1234") {
			@Override
			public void run() {
				for (;;) {
					try {
						Object object = blockingQ.poll(3, TimeUnit.SECONDS);
						if (null == object) {
							continue;
						}
						if (0 == (Calendar.getInstance().get(Calendar.SECOND) % 5)) {
							Object obj = blockingQ.take();
							taskRun(obj.toString());
						}
					} catch (InterruptedException e) {
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}*/
	
	public void run() {
		//定时任务执行频率  每5秒执行一次    支持指定日期执行
		final int frequece = 5;
		
		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {//每秒轮询异一次，到点执行
			
			@Override
			public void run() {
				try {
					if (0 == (Calendar.getInstance().get(Calendar.SECOND) % frequece)) {
						//从队列中同步取任务，没有返回则一直等待
						Object object = blockingQ.take();
						//执行任务
						taskRun(object.toString());
					}
				} catch (InterruptedException e) {//线程异常中断，抛出异常
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}
	
	/**
	 * 要执行的任务
	 * @param str
	 * @throws InterruptedException
	 */
	public static void taskRun(String str) throws InterruptedException {
		System.out.println(str + "开始" + " - " + System.currentTimeMillis());
		for (int i = 0; i < 6; i++) {
			System.out.println("执行任务耗时" + ( i + 1 ) + "s - " + System.currentTimeMillis());
			Thread.sleep(1000);
		}
		System.out.println(str + "结束" + " - " + System.currentTimeMillis());
	}

}

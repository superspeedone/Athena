package com.superspeed.test.multithread;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 线程小例子
 * @author xc.yanww
 *
 */
public class ThreadTest {
	
	public static void main(String[] args) {
		
		try {
			//test1();
			//test2();
			//test3();
			test4();
			//test6();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void test1() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("===========" + Thread.currentThread().getName());
			}
		}, "123");
		
		thread.start();
	}
	
	/**
	 * 任务提交者  - 任务执行者   
	 * ExecutorService模拟生产者和消费者
	 * 阻塞模式
 	 */
	public static void test2() {
		Future<String> future = null;
		try {
			ExecutorService executor = Executors.newSingleThreadExecutor();
			
			Callable<String> callable = new Callable<String>() {

				@Override
				public String call() throws Exception {
					for (int i = 0; i < 10; i++) {
						System.out.println(i + 1 + "s");
						Thread.sleep(1000);
					}
					return "======";
				}
			};
			
			future = executor.submit(callable);
//			future.cancel(true);
			System.out.println(future.get());   //一直等待，直到获取到执行返回结果
			System.out.println(future.get(3, TimeUnit.SECONDS));  //设置获取执行返回结果，等待最大时间为3s
			System.out.println(future.isDone());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println(future.isCancelled());
	}
	
	/**
	 * 使用BlockingQueue实现阻塞定时任务 （模拟spring-task阻塞定时任务）
	 * 上一个线程任务未执行完毕，下一个任务等待上一个任务执行完毕之后才会执行
	 */
	public static void test3() {
		final BlockingQueue<Object> blockingQ = new ArrayBlockingQueue<Object>(10);
		final Map<String, Integer> index = new HashMap<String, Integer>();
		
		//定时任务 ，生产者，添加任务
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				synchronized (blockingQ) {
					String now = String.valueOf(System.currentTimeMillis());
					if (blockingQ.size() < 10) {
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
		
		//消费者，执行任务
//		Thread thread = new Thread("1234") {
//			@Override
//			public void run() {
//				for (;;) {
//					try {
////						Object object = blockingQ.poll(3, TimeUnit.SECONDS);
////						if (null == object) {
////							continue;
//						}
//						if (0 == (Calendar.getInstance().get(Calendar.SECOND) % 5)) {
//							Object object = blockingQ.take();
//							test5(object.toString());
//						}
//					} catch (InterruptedException e) {
//						break;
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//		thread.start();
		//定时任务执行频率  每5秒执行一次    支持指定日期执行
		final int frequece = 5;
		
		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					if (0 == (Calendar.getInstance().get(Calendar.SECOND) % frequece)) {
						Object object = blockingQ.take();
						test5(object.toString());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}
	
	public static void test5(String str) throws InterruptedException {
		System.out.println(str + "开始" + " - " + System.currentTimeMillis());
		for (int i = 0; i < 6; i++) {
			System.out.println("执行任务耗时" + ( i + 1 ) + "s - " + System.currentTimeMillis());
			Thread.sleep(1000);
		}
		System.out.println(str + "结束" + " - " + System.currentTimeMillis());
	}
	
	/**
	 * ScheduledExecutorService的使用
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void test4() throws InterruptedException, ExecutionException, IOException {
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(10);
		Callable<String> callable1 = new Callable<String>() {

			@Override
			public String call() throws Exception {
				for (int i = 0; i < 10; i++) {
					System.out.printf("Thread[%d] callable1 %d \n", Thread.currentThread().getId(), i + 1);
					Thread.sleep(500);
				}
				return String.format("Thread[%d] callable1 executes completly", Thread.currentThread().getId());
			}
		};
		
		Callable<String> callable2 = new Callable<String>() {

			@Override
			public String call() throws Exception {
				for (int i = 0; i < 10; i++) {
					System.out.printf("Thread[%d] callable2 %d \n", Thread.currentThread().getId(), i + 1);
					Thread.sleep(500);
				}
				return String.format("Thread[%d] callable2 executes completly", Thread.currentThread().getId());
			}
		};
		
		/*schedule.submit(callable1);
		schedule.submit(callable2);*/
		List<Future<String>> futureList = schedule.invokeAll(Arrays.asList(callable1, callable2));
		for (int i = 0; i < futureList.size(); i++) {
			System.out.println(futureList.get(i).get());
		}

		schedule.shutdown();

	}
	
	/**
	 * 并发流控制   多个线程同时执行某个任务
	 */
	public static void test6() {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		
		for (int i = 0; i < 5; i++) {
			Thread thread = new MyThread(i + 1, countDownLatch);
			thread.start();
		}
		
		countDownLatch.countDown();
	}

}

class MyThread extends Thread {
	
	private Integer index;
	
	private CountDownLatch countDownLatch;

	public MyThread(Integer index, CountDownLatch countDownLatch) {
		super();
		this.index = index;
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void run() {
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Thread - " + this.index);
	}
	
}

/*
 * Created on 2006-11-6
 *
 */
package com.yuanluesoft.jeaf.util.threadpool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class ThreadPool {
	private int maxThreads; //连接池最大线程数量
	private int maxIdleThreads; //连接池最大空闲线程数
	private int waitForIdleThread = 5000; //等待空闲线程的时间,以毫秒为单位
	private List runningThreads = new ArrayList(); //在运行的线程列表
	private List idleThreads = new ArrayList(); //空闲的线程列表

	/**
	 * 构造线程池
	 * @param maxThreads 连接池最大线程数量
	 * @param maxIdleThreads 连接池最大空闲线程数
	 * @param waitForIdleThread 等待空闲线程的时间,以毫秒为单位
	 */
	public ThreadPool(int maxThreads, int maxIdleThreads, int waitForIdleThread) {
		this.maxThreads = maxThreads;
		this.maxIdleThreads = maxIdleThreads;
		this.waitForIdleThread = waitForIdleThread;
	}
	
	/**
	 * 执行任务
	 * @param task
	 * @throws ThreadPoolException
	 */
	public void execute(Task task) throws ThreadPoolException {
		synchronized(this) {
			for(int i=0; i<2; i++) {
				if(!idleThreads.isEmpty()) { //有空闲线程
					TaskThread thread = (TaskThread)idleThreads.get(0);
					idleThreads.remove(0);
					thread.task = task;
					synchronized(thread) {
						thread.notifyAll();
					}
					runningThreads.add(thread);
					return;
				}
				if(runningThreads.size()<maxThreads) { //线程池未满
					TaskThread thread = new TaskThread(this);
					thread.task = task;
					thread.start();
					runningThreads.add(thread);
					return;
				}
				else if(i==0) { //线程池满
					try {
						//等待空闲线程
						if(waitForIdleThread<=0) {
							wait();
						}
						else {
							wait(waitForIdleThread);
						}
					}
					catch (InterruptedException e) {

					}
				}
			}
			throw new ThreadPoolException("all threads are busy");
		}
	}
	
	/**
	 * 销毁线程池
	 * @throws ThreadPoolException
	 */
	public void destroyPool() throws ThreadPoolException {
		synchronized(this) {
			idleThreads.addAll(runningThreads);
			for(Iterator iterator = idleThreads.iterator(); iterator.hasNext();) {
				TaskThread taskThread = (TaskThread)iterator.next();
				taskThread.interrupt();
				synchronized(taskThread) {
					taskThread.notifyAll();
				}
				iterator.remove();
			}
		}
	}
	
	/**
	 * 任务线程
	 * @author linchuan
	 *
	 */
	private class TaskThread extends Thread {
		private ThreadPool pool;
		private Task task = null; //任务
		
		public TaskThread(ThreadPool pool) {
			this.pool = pool;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while(!Thread.interrupted()) {
				if(task!=null) {
					try {
						task.process();
					}
					catch(Error e) {
						e.printStackTrace();
						Logger.error(e.getMessage());
					}
					catch(Exception e) {
						Logger.exception(e);
					}
					task = null;
				}
				synchronized(this) {
					synchronized(pool) {
						pool.runningThreads.remove(this);
						if(pool.idleThreads.size()>=pool.maxIdleThreads) { //空闲线程池已满
							pool.notify();
							return;
						}
						pool.idleThreads.add(this);
						pool.notify();
					}
					try {
						wait();
					}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
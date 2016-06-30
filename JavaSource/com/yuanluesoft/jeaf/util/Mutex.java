package com.yuanluesoft.jeaf.util;


/**
 * 
 * @author linchuan
 *
 */
public class Mutex {
	private int workThreads = 0; //当前正在工作的线程数
	
	/**
	 × 锁定
	 * @param maxThreads //最大并发数
	 * @param maxWait //等待时间,以毫秒为单位,如果超出这个时间,抛出异常
	 * @throws Exception
	 */
	public void lock(int maxThreads, int maxWait) {
		synchronized(this) {
			if(workThreads>=maxThreads) {
				try {
					this.wait(maxWait); //等待
				}
				catch (InterruptedException e) {
					
				}
				if(workThreads>=maxThreads) {
					throw new Error("Mutex lock exception");
				}
			}
			workThreads++;
		}
	}
	
	/**
	 * 解锁
	 *
	 */
	public void unlock() {
		synchronized(this) {
			if(workThreads>0) {
				workThreads--;
			}
			this.notify();
		}
	}
}
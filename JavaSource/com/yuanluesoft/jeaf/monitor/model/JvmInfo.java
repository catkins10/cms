package com.yuanluesoft.jeaf.monitor.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class JvmInfo implements Serializable {
	private List memoryUsages; //内存使用情况
	private List classInstances; //类实例列表
	private int threadCount; //总共线程数
	private int deadlockedThreads; //死锁线程数
	private int peakThreadCount; //JVM从启动开始最大线程数量
	private List threads; //线程列表
	
	/**
	 * @return the threads
	 */
	public List getThreads() {
		return threads;
	}
	/**
	 * @param threads the threads to set
	 */
	public void setThreads(List threads) {
		this.threads = threads;
	}
	/**
	 * @return the memoryUsages
	 */
	public List getMemoryUsages() {
		return memoryUsages;
	}
	/**
	 * @param memoryUsages the memoryUsages to set
	 */
	public void setMemoryUsages(List memoryUsages) {
		this.memoryUsages = memoryUsages;
	}
	/**
	 * @return the deadlockedThreads
	 */
	public int getDeadlockedThreads() {
		return deadlockedThreads;
	}
	/**
	 * @param deadlockedThreads the deadlockedThreads to set
	 */
	public void setDeadlockedThreads(int deadlockedThreads) {
		this.deadlockedThreads = deadlockedThreads;
	}
	/**
	 * @return the peakThreadCount
	 */
	public int getPeakThreadCount() {
		return peakThreadCount;
	}
	/**
	 * @param peakThreadCount the peakThreadCount to set
	 */
	public void setPeakThreadCount(int peakThreadCount) {
		this.peakThreadCount = peakThreadCount;
	}
	/**
	 * @return the threadCount
	 */
	public int getThreadCount() {
		return threadCount;
	}
	/**
	 * @param threadCount the threadCount to set
	 */
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	/**
	 * @return the classInstances
	 */
	public List getClassInstances() {
		return classInstances;
	}
	/**
	 * @param classInstances the classInstances to set
	 */
	public void setClassInstances(List classInstances) {
		this.classInstances = classInstances;
	}
}
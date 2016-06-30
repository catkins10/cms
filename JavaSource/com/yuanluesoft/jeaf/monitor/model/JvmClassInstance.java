package com.yuanluesoft.jeaf.monitor.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class JvmClassInstance implements Serializable {
	private String className; //类名称	
	private int instanceCount; //数量	
	private long memoryUsed; //占用内存
	
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the instanceCount
	 */
	public int getInstanceCount() {
		return instanceCount;
	}
	/**
	 * @param instanceCount the instanceCount to set
	 */
	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}
	/**
	 * @return the memoryUsed
	 */
	public long getMemoryUsed() {
		return memoryUsed;
	}
	/**
	 * @param memoryUsed the memoryUsed to set
	 */
	public void setMemoryUsed(long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
}
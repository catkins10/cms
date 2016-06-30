package com.yuanluesoft.jeaf.monitor.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class JvmMemoryUsage implements Serializable {
	private String name;
	private long used; //使用量
	private double ratio; //使用率
	
	public JvmMemoryUsage(String name, long used, double ratio) {
		super();
		this.name = name;
		this.used = used;
		this.ratio = ratio;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the ratio
	 */
	public double getRatio() {
		return ratio;
	}
	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	/**
	 * @return the used
	 */
	public long getUsed() {
		return used;
	}
	/**
	 * @param used the used to set
	 */
	public void setUsed(long used) {
		this.used = used;
	}
}
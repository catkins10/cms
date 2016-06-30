package com.yuanluesoft.charge.servicemanage.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceDeducting implements Serializable, Cloneable {
	private String id;
	private String deductTime;
	private String nextDeductTime;
	private double deductMoney;
	
	/**
	 * @return the deductMoney
	 */
	public double getDeductMoney() {
		return deductMoney;
	}
	/**
	 * @param deductMoney the deductMoney to set
	 */
	public void setDeductMoney(double deductMoney) {
		this.deductMoney = deductMoney;
	}
	/**
	 * @return the deductTime
	 */
	public String getDeductTime() {
		return deductTime;
	}
	/**
	 * @param deductTime the deductTime to set
	 */
	public void setDeductTime(String deductTime) {
		this.deductTime = deductTime;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the nextDeductTime
	 */
	public String getNextDeductTime() {
		return nextDeductTime;
	}
	/**
	 * @param nextDeductTime the nextDeductTime to set
	 */
	public void setNextDeductTime(String nextDeductTime) {
		this.nextDeductTime = nextDeductTime;
	}
}

package com.yuanluesoft.charge.servicemanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 计费:服务扣款策略(charge_service_deduct_policy)
 * @author linchuan
 *
 */
public class ServiceDeductPolicy extends Record {
	private long servicePriceId; //服务报价ID
	private int dayMin; //区间最小值
	private int dayMax; //区间最大值
	private double percentage; //扣除百分百
	private String description; //描述
	
	/**
	 * @return the dayMax
	 */
	public int getDayMax() {
		return dayMax;
	}
	/**
	 * @param dayMax the dayMax to set
	 */
	public void setDayMax(int dayMax) {
		this.dayMax = dayMax;
	}
	/**
	 * @return the dayMin
	 */
	public int getDayMin() {
		return dayMin;
	}
	/**
	 * @param dayMin the dayMin to set
	 */
	public void setDayMin(int dayMin) {
		this.dayMin = dayMin;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	/**
	 * @return the servicePriceId
	 */
	public long getServicePriceId() {
		return servicePriceId;
	}
	/**
	 * @param servicePriceId the servicePriceId to set
	 */
	public void setServicePriceId(long servicePriceId) {
		this.servicePriceId = servicePriceId;
	}
}

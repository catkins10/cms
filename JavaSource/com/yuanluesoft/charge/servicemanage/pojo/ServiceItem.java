package com.yuanluesoft.charge.servicemanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 计费:服务包条目(charge_service_item)
 * @author linchuan
 *
 */
public class ServiceItem extends Record {
	private long serviceId; //服务包ID
	private String item; //条目名称
	
	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}
	/**
	 * @return the serviceId
	 */
	public long getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
}

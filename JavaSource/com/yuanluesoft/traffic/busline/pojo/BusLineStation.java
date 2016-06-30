package com.yuanluesoft.traffic.busline.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公交线路:途径的车站(traffic_busline_station)
 * @author linchuan
 *
 */
public class BusLineStation extends Record {
	private long busLineId; //公交线路ID
	private String name; //站点名称
	private char direction = '2'; //上行/下行,0/上行, 1/下行, 2/上下行
	private char status = '0'; //状态,0/正常,1/临时增加,2/临时取消
	private double priority; //优先级
	
	/**
	 * 获取站点全称,包括站名、上下行、是否临时取消、是否临时增加
	 * @return
	 */
	public String getFullName() {
		String fullName = null;
		if(direction!='2') {
			fullName = direction=='0' ? "上行" : "下行";
		}
		if(status!='0') {
			fullName = (fullName==null ? "" : fullName + "，") + (status=='1' ? "临时增加" : "临时取消");
		}
		return name + (fullName==null ? "" : "(" + fullName + ")");
	}
	
	/**
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
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
	 * @return the busLineId
	 */
	public long getBusLineId() {
		return busLineId;
	}

	/**
	 * @param busLineId the busLineId to set
	 */
	public void setBusLineId(long busLineId) {
		this.busLineId = busLineId;
	}

	/**
	 * @return the direction
	 */
	public char getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(char direction) {
		this.direction = direction;
	}

	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(char status) {
		this.status = status;
	}
}
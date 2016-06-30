package com.yuanluesoft.jeaf.point.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 积分(point_point)
 * @author linchuan
 *
 */
public class Point extends Record {
	private long personId; //用户ID
	private int point; //积分
	
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}
}
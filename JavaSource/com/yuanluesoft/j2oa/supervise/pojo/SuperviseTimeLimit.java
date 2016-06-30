package com.yuanluesoft.j2oa.supervise.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 督办:完成时限(supervise_supervise_time_limit)
 * @author linchuan
 *
 */
public class SuperviseTimeLimit extends Record {
	private long superviseId; //督办ID
	private Timestamp timeLimit; //时限
	private Timestamp created; //设置时间
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the superviseId
	 */
	public long getSuperviseId() {
		return superviseId;
	}
	/**
	 * @param superviseId the superviseId to set
	 */
	public void setSuperviseId(long superviseId) {
		this.superviseId = superviseId;
	}
	/**
	 * @return the timeLimit
	 */
	public Timestamp getTimeLimit() {
		return timeLimit;
	}
	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(Timestamp timeLimit) {
		this.timeLimit = timeLimit;
	}
}
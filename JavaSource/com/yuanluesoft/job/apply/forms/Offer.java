package com.yuanluesoft.job.apply.forms;

import java.sql.Timestamp;

import com.yuanluesoft.job.apply.pojo.JobApplyOffer;

/**
 * 
 * @author linchuan
 *
 */
public class Offer extends Apply {
	private JobApplyOffer offer = new JobApplyOffer();
	private Timestamp entryTime; //入职时间

	/**
	 * @return the offer
	 */
	public JobApplyOffer getOffer() {
		return offer;
	}

	/**
	 * @param offer the offer to set
	 */
	public void setOffer(JobApplyOffer offer) {
		this.offer = offer;
	}

	/**
	 * @return the entryTime
	 */
	public Timestamp getEntryTime() {
		return entryTime;
	}

	/**
	 * @param entryTime the entryTime to set
	 */
	public void setEntryTime(Timestamp entryTime) {
		this.entryTime = entryTime;
	}
}
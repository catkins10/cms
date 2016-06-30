/*
 * Created on 2005-4-22
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * @author linchuan
 *
 */
public class TransactLog implements Serializable {
	private Timestamp time; //办理完成时间
	private String agentId; //实际办理人ID
	private String agentName; //实际办理人姓名
	private String forwards; //去向列表
	
	/**
	 * @return Returns the agentName.
	 */
	public String getParticipantName() {
		return agentName;
	}
	/**
	 * @param agentName The agentName to set.
	 */
	public void setParticipantName(String agentName) {
		this.agentName = agentName;
	}
	/**
	 * @return Returns the agentId.
	 */
	public String getParticipantId() {
		return agentId;
	}
	/**
	 * @param agentId The agentId to set.
	 */
	public void setParticipantId(String agentId) {
		this.agentId = agentId;
	}
	/**
	 * @return Returns the forwards.
	 */
	public String getForwards() {
		return forwards;
	}
	/**
	 * @param forwards The forwards to set.
	 */
	public void setForwards(String forwards) {
		this.forwards = forwards;
	}
	/**
	 * @return Returns the time.
	 */
	public Timestamp getTime() {
		return time;
	}
	/**
	 * @param time The time to set.
	 */
	public void setTime(Timestamp time) {
		this.time = time;
	}
}

package com.yuanluesoft.j2oa.attendance.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class PunchCard extends ActionForm {
	private Timestamp punchTime; //打开时间
	private boolean onDuty; //是否上班
	
	/**
	 * @return the onDuty
	 */
	public boolean isOnDuty() {
		return onDuty;
	}
	/**
	 * @param onDuty the onDuty to set
	 */
	public void setOnDuty(boolean onDuty) {
		this.onDuty = onDuty;
	}
	/**
	 * @return the punchTime
	 */
	public Timestamp getPunchTime() {
		return punchTime;
	}
	/**
	 * @param punchTime the punchTime to set
	 */
	public void setPunchTime(Timestamp punchTime) {
		this.punchTime = punchTime;
	}
}
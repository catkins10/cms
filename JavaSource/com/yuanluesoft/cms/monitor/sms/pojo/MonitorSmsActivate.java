package com.yuanluesoft.cms.monitor.sms.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.cms.monitor.pojo.MonitorRecord;

/**
 * 监察:短信开通(monitor_sms_activate)
 * @author linchuan
 *
 */
public class MonitorSmsActivate extends MonitorRecord {
	private int activate; //是否开通
	private Timestamp activateTime; //开通时间
	
	/**
	 * @return the activate
	 */
	public int getActivate() {
		return activate;
	}
	/**
	 * @param activate the activate to set
	 */
	public void setActivate(int activate) {
		this.activate = activate;
	}
	/**
	 * @return the activateTime
	 */
	public Timestamp getActivateTime() {
		return activateTime;
	}
	/**
	 * @param activateTime the activateTime to set
	 */
	public void setActivateTime(Timestamp activateTime) {
		this.activateTime = activateTime;
	}
}
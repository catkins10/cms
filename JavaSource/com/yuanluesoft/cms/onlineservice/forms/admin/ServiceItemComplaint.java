package com.yuanluesoft.cms.onlineservice.forms.admin;

import com.yuanluesoft.cms.onlineservice.interactive.complaint.pojo.OnlineServiceComplaint;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;


/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemComplaint extends ServiceItemInteractive {
	private OnlineServiceComplaint complaint = new OnlineServiceComplaint(); //投诉
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItemInteractive#getInteractiveRecord()
	 */
	public OnlineServiceInteractive getInteractiveRecord() {
		return complaint;
	}
	/**
	 * @return the complaint
	 */
	public OnlineServiceComplaint getComplaint() {
		return complaint;
	}
	/**
	 * @param complaint the complaint to set
	 */
	public void setComplaint(OnlineServiceComplaint complaint) {
		this.complaint = complaint;
	}
}
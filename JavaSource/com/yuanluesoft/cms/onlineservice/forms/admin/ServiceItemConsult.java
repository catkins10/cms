package com.yuanluesoft.cms.onlineservice.forms.admin;

import com.yuanluesoft.cms.onlineservice.interactive.consult.pojo.OnlineServiceConsult;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;


/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemConsult extends ServiceItemInteractive {
	private OnlineServiceConsult consult = new OnlineServiceConsult(); //咨询
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItemInteractive#getInteractiveRecord()
	 */
	public OnlineServiceInteractive getInteractiveRecord() {
		return consult;
	}
	/**
	 * @return the consult
	 */
	public OnlineServiceConsult getConsult() {
		return consult;
	}
	/**
	 * @param consult the consult to set
	 */
	public void setConsult(OnlineServiceConsult consult) {
		this.consult = consult;
	}
}
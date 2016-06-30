package com.yuanluesoft.j2oa.info.forms;

import com.yuanluesoft.j2oa.info.pojo.InfoSendHigher;

/**
 * 
 * @author linchuan
 *
 */
public class SendHigher extends Info {
	private InfoSendHigher sendHigher = new InfoSendHigher(); //报送情况	
	private boolean used; //是否被录用

	/**
	 * @return the sendHigher
	 */
	public InfoSendHigher getSendHigher() {
		return sendHigher;
	}

	/**
	 * @param sendHigher the sendHigher to set
	 */
	public void setSendHigher(InfoSendHigher sendHigher) {
		this.sendHigher = sendHigher;
	}

	/**
	 * @return the used
	 */
	public boolean isUsed() {
		return used;
	}

	/**
	 * @param used the used to set
	 */
	public void setUsed(boolean used) {
		this.used = used;
	}
}
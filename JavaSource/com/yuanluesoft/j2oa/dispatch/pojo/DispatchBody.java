/*
 * Created on 2005-8-31
 *
 */
package com.yuanluesoft.j2oa.dispatch.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 发文正文(dispatch_dispatch_body)
 * @author linchuan
 *
 */
public class DispatchBody extends Record {
	private long dispatchId; //发文单ID
	private String htmlBody; //HTML正文
	
	/**
	 * @return the dispatchId
	 */
	public long getDispatchId() {
		return dispatchId;
	}
	/**
	 * @param dispatchId the dispatchId to set
	 */
	public void setDispatchId(long dispatchId) {
		this.dispatchId = dispatchId;
	}
	/**
	 * @return the htmlBody
	 */
	public String getHtmlBody() {
		return htmlBody;
	}
	/**
	 * @param htmlBody the htmlBody to set
	 */
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}
}
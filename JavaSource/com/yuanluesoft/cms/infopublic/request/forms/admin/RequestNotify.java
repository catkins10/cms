package com.yuanluesoft.cms.infopublic.request.forms.admin;

import com.yuanluesoft.cms.infopublic.request.pojo.PublicRequestNotify;

/**
 * 
 * @author linchuan
 *
 */
public class RequestNotify extends Request {
	private PublicRequestNotify requestNotify = new PublicRequestNotify(); //告知书

	/**
	 * @return the requestNotify
	 */
	public PublicRequestNotify getRequestNotify() {
		return requestNotify;
	}

	/**
	 * @param requestNotify the requestNotify to set
	 */
	public void setRequestNotify(PublicRequestNotify requestNotify) {
		this.requestNotify = requestNotify;
	}
}
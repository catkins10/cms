package com.yuanluesoft.bidding.enterprise.model;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 招投标会话信息
 * @author yuanlue
 *
 */
public class BiddingSessionInfo extends SessionInfo {
	private String certs; //企业资质列表
	
	/**
	 * 是否招标代理
	 * @return
	 */
	public boolean isAgent() {
		return ("," + certs + ",").indexOf(",招标代理,")!=-1;
	}

	/**
	 * @return the certs
	 */
	public String getCerts() {
		return certs;
	}

	/**
	 * @param certs the certs to set
	 */
	public void setCerts(String certs) {
		this.certs = certs;
	}
}

package com.yuanluesoft.bidding.enterprise.forms.admin;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterpriseCert;

/**
 * 
 * @author linchuan
 *
 */
public class EnterpriseCert extends Enterprise {
	private BiddingEnterpriseCert cert = new BiddingEnterpriseCert();
	
	/**
	 * @return the cert
	 */
	public BiddingEnterpriseCert getCert() {
		return cert;
	}

	/**
	 * @param cert the cert to set
	 */
	public void setCert(BiddingEnterpriseCert cert) {
		this.cert = cert;
	}
}
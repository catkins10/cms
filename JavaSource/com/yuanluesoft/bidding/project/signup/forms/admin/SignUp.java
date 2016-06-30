package com.yuanluesoft.bidding.project.signup.forms.admin;

import com.yuanluesoft.bidding.project.signup.forms.BiddingSignUp;

/**
 * 
 * @author linchuan
 *
 */
public class SignUp extends BiddingSignUp {
	private boolean paymentPledge; //是否在平台上支付保证金

	/**
	 * @return the paymentPledge
	 */
	public boolean isPaymentPledge() {
		return paymentPledge;
	}

	/**
	 * @param paymentPledge the paymentPledge to set
	 */
	public void setPaymentPledge(boolean paymentPledge) {
		this.paymentPledge = paymentPledge;
	}
}
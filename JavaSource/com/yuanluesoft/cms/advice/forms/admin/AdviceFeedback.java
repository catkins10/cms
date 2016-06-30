package com.yuanluesoft.cms.advice.forms.admin;


/**
 * 
 * @author linchuan
 *
 */
public class AdviceFeedback extends AdviceTopic {
	private com.yuanluesoft.cms.advice.pojo.AdviceFeedback adviceFeedback = new com.yuanluesoft.cms.advice.pojo.AdviceFeedback();

	/**
	 * @return the adviceFeedback
	 */
	public com.yuanluesoft.cms.advice.pojo.AdviceFeedback getAdviceFeedback() {
		return adviceFeedback;
	}

	/**
	 * @param adviceFeedback the adviceFeedback to set
	 */
	public void setAdviceFeedback(
			com.yuanluesoft.cms.advice.pojo.AdviceFeedback adviceFeedback) {
		this.adviceFeedback = adviceFeedback;
	}
}
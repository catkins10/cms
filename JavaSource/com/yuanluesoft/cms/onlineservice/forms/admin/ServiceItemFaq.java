package com.yuanluesoft.cms.onlineservice.forms.admin;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemFaq;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemFaq extends ServiceItem {
	private OnlineServiceItemFaq faq = new OnlineServiceItemFaq();

	/**
	 * @return the faq
	 */
	public OnlineServiceItemFaq getFaq() {
		return faq;
	}

	/**
	 * @param faq the faq to set
	 */
	public void setFaq(OnlineServiceItemFaq faq) {
		this.faq = faq;
	}
}
package com.yuanluesoft.fdi.customer.forms.admin;

import com.yuanluesoft.fdi.customer.pojo.FdiCustomerContactDiscuss;

/**
 * 
 * @author linchuan
 *
 */
public class Discuss extends Contact {
	private FdiCustomerContactDiscuss discuss = new FdiCustomerContactDiscuss();
	private boolean openFromContact; //是否从联系人页面打开  

	/**
	 * @return the discuss
	 */
	public FdiCustomerContactDiscuss getDiscuss() {
		return discuss;
	}

	/**
	 * @param discuss the discuss to set
	 */
	public void setDiscuss(FdiCustomerContactDiscuss discuss) {
		this.discuss = discuss;
	}

	/**
	 * @return the openFromContact
	 */
	public boolean isOpenFromContact() {
		return openFromContact;
	}

	/**
	 * @param openFromContact the openFromContact to set
	 */
	public void setOpenFromContact(boolean openFromContact) {
		this.openFromContact = openFromContact;
	}
}
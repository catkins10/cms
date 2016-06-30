package com.yuanluesoft.fdi.customer.forms.admin;

import com.yuanluesoft.fdi.customer.pojo.FdiCustomerContact;

/**
 * 
 * @author linchuan
 *
 */
public class Contact extends Company {
	private FdiCustomerContact companyContact = new FdiCustomerContact();

	/**
	 * @return the companyContact
	 */
	public FdiCustomerContact getCompanyContact() {
		return companyContact;
	}

	/**
	 * @param companyContact the companyContact to set
	 */
	public void setCompanyContact(FdiCustomerContact companyContact) {
		this.companyContact = companyContact;
	}
}
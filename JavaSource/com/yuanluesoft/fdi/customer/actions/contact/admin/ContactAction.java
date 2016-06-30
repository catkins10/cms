package com.yuanluesoft.fdi.customer.actions.contact.admin;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.fdi.customer.actions.company.admin.CompanyAction;
import com.yuanluesoft.fdi.customer.pojo.FdiCustomerCompany;
import com.yuanluesoft.fdi.customer.pojo.FdiCustomerContact;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ContactAction extends CompanyAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(component instanceof FdiCustomerContact) {
			((FdiCustomerContact)component).setCompanyName(((FdiCustomerCompany)mainRecord).getName());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}	
}
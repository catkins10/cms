package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem.faq;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem.ServiceItemAction;
import com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItemFaq;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemFaq;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemFaqAction extends ServiceItemAction  {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		ServiceItemFaq faq = (ServiceItemFaq)form;
		faq.getFaq().setCreator(sessionInfo.getUserName());
		faq.getFaq().setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) {
			OnlineServiceItemFaq faq = (OnlineServiceItemFaq)component;
			faq.setCreator(sessionInfo.getUserName());
			faq.setCreatorId(sessionInfo.getUserId());
			faq.setCreated(DateTimeUtils.now());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}
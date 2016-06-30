package com.yuanluesoft.cms.smssubscription.actions.smsservice.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.smssubscription.forms.admin.SmsService;
import com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class SmsServiceAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		SmsService smsServiceForm = (SmsService)form;
		if(smsServiceForm.getIsValid()==0) {
			smsServiceForm.setIsValid('1');
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		SmsService smsServiceForm = (SmsService)form;
		if(smsServiceForm.getSubscribePrefixRule().equals(smsServiceForm.getUnsubscribeRule())) {
			smsServiceForm.setError("订阅规则不能和退订规则相同");
			throw new ValidateException();
		}
		SmsSubscriptionService smsSubscriptionService = (SmsSubscriptionService)getService("smsSubscriptionService");
		try {
			smsSubscriptionService.valideteRule(smsServiceForm.getSubscribePrefixRule(), smsServiceForm.getUnsubscribeRule(), smsServiceForm.getId());
		}
		catch(ServiceException se) {
			smsServiceForm.setError(se.getMessage());
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.cms.smssubscription.pojo.SmsService service = (com.yuanluesoft.cms.smssubscription.pojo.SmsService)record;
		//不区分大小写,转换为大写
		service.setSubscribePrefixRule(service.getSubscribePrefixRule().toUpperCase());
		if(service.getUnsubscribeRule()!=null) {
			service.setUnsubscribeRule(service.getUnsubscribeRule().toUpperCase());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
package com.yuanluesoft.cms.inquiry.actions.inquiry.admin;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.yuanluesoft.cms.inquiry.actions.inquirysubject.admin.InquirySubjectAction;
import com.yuanluesoft.cms.inquiry.forms.admin.Inquiry;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
/**
 * 
 * @author lmiky
 *
 */
public class InquiryAction extends InquirySubjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		Inquiry inquiryForm = (Inquiry)form;
		inquiryForm.getInquiry().setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		com.yuanluesoft.cms.inquiry.pojo.Inquiry inquiry = (com.yuanluesoft.cms.inquiry.pojo.Inquiry)component;
		//如果是单选,则最小最大投票数都改为1
		if(inquiry.getIsMultiSelect()=='0') {
			inquiry.setMinVote(1);
			inquiry.setMaxVote(1);
		}
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct()) && (component instanceof com.yuanluesoft.cms.inquiry.pojo.Inquiry)) {
			((com.yuanluesoft.cms.inquiry.pojo.Inquiry)component).setCreated(DateTimeUtils.now());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.dao.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
    public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
    	List errors = validateForm((ActionForm)formToValidate, forceValidateCode, request);
    	if(errors == null) {
    		errors = new ArrayList();
    	}
		Inquiry inquiryForm = (Inquiry)formToValidate;
		if(inquiryForm.getInquiry().getIsMultiSelect()=='1') {
			if(inquiryForm.getInquiry().getMinVote()==0) {
				errors.add("最小投票数不能为零");
			}
			if(inquiryForm.getInquiry().getMaxVote()==0) {
				errors.add("最大投票数不能为零");
			}
			if(inquiryForm.getInquiry().getMinVote() > inquiryForm.getInquiry().getMaxVote()) {
				errors.add("最小投票数不能大于最大投票数");
			}
		}
		if(errors!=null && !errors.isEmpty()) {
			inquiryForm.setErrors(errors);
			throw new ValidateException();
		}
    }
}
package com.yuanluesoft.cms.publicservice.actions.smsformat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.publicservice.forms.admin.SmsFormat;
import com.yuanluesoft.cms.publicservice.pojo.PublicServiceSms;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SmsFormatAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		SmsFormat smsFormatForm = (SmsFormat)form;
		PublicServiceSms smsFormat = (PublicServiceSms)record;
		return getAcl(smsFormat==null ? smsFormatForm.getApplicationName() : smsFormat.getApplicationName(), sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(form.getId()==0) { //新记录
			SmsFormat smsFormatForm = (SmsFormat)form;
			PublicService publicService = (PublicService)getService("publicService");
			return publicService.loadSmsFormatConfig(smsFormatForm.getApplicationName(), smsFormatForm.getSiteId());
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}
}
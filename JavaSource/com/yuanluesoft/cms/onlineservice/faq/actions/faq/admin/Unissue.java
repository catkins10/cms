package com.yuanluesoft.cms.onlineservice.faq.actions.faq.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq;
import com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Unissue extends FaqAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, false, null, "撤销成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.resource.ResourceAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		OnlineServiceFaqService onlineServiceFaqService = (OnlineServiceFaqService)getService("onlineServiceFaqService");
		OnlineServiceFaq faq = (OnlineServiceFaq)record;
		onlineServiceFaqService.unissue(faq);
		return record;
	}
}
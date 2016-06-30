package com.yuanluesoft.cms.inquiry.actions.inquirysubject.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 设置投票数
 * @author linchuan
 *
 */
public class SetVoteNumber extends InquirySubjectAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, true, "result", null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.inquiry.actions.inquirysubject.admin.InquirySubjectAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		InquiryService inquiryService = (InquiryService)getService("inquiryService");
		long optionId = RequestUtils.getParameterLongValue(request, "optionId");
		if(optionId>0) {
			inquiryService.setVoteNumber(optionId, RequestUtils.getParameterIntValue(request, "voteNumber"));
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
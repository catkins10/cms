package com.yuanluesoft.cms.inquiry.actions.inquiryoption.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.pojo.InquiryVote;
import com.yuanluesoft.jeaf.database.DatabaseService;

/**
 * 
 * @author lmiky
 *
 */
public class Load extends InquiryOptionAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		
		databaseService.findRecordsByHql("from InquiryVote InquiryVote where ");
		return executeLoadComponentAction(mapping, form, "option", request, response);
		
	}
}

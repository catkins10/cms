package com.yuanluesoft.bidding.project.actions.project.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.payment.exception.TransferPasswordMismatchException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.MD5PasswordMatcher;

/**
 * 
 * @author linchuan
 *
 */
public class PledgeReturnTransfer extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Project projectForm = (Project)form;
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, "returnPledge", "success", null);
    	if("success".equals(projectForm.getActionResult())) { //执行成功
    		response.sendRedirect("project.shtml?act=edit&id=" + projectForm.getId() + "&seq=" + System.currentTimeMillis());
    		return null;
    	}
    	return forward;
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.actions.project.admin.ProjectAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		BiddingProject project = (BiddingProject)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Project projectForm = (Project)form;
		BiddingService biddingService = (BiddingService)getService("biddingService");
		try {
			biddingService.pledgeReturnTransfer(project, projectForm.getTransferPassword(), new MD5PasswordMatcher(request), request);
		}
		catch(TransferPasswordMismatchException pe) {
			projectForm.setError("转账密码不正确");
			throw new ValidateException();
		}
		return project;
	}
}
package com.yuanluesoft.job.talent.actions.talentview.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.job.talent.pojo.JobTalent;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author chuan
 *
 */
public class Approval extends ApplicationViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()!=null && !viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
			String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
			for(int i=0; i<ids.length; i++) {
				jobTalentService.approvalTalent((JobTalent)jobTalentService.load(JobTalent.class, Long.parseLong(ids[i])), "true".equals(request.getParameter("approvalPass")), request.getParameter("failedReason"), sessionInfo);
			}
			viewForm.getViewPackage().setSelectedIds(null); //清空列表
		}
		return mapping.getInputForward();
	}
}
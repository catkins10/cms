package com.yuanluesoft.bidding.project.report.actions.pledgereport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.report.forms.admin.ProjectPledgeReport;
import com.yuanluesoft.bidding.project.report.service.BiddingProjectReportService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Write extends FormAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
			//会话检查
			SessionInfo sessionInfo = getSessionInfo(request, response);
			ProjectPledgeReport report = (ProjectPledgeReport)form;
			BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
			BiddingProject project = biddingProjectService.getProject(report.getProjectId());
			//检查加载权限
			BiddingService biddingService = (BiddingService)getService("biddingService");
			if(!biddingService.isPledgeVisible(project, sessionInfo)) {
				throw new PrivilegeException();
			}
			BiddingProjectReportService biddingProjectReportService = (BiddingProjectReportService)getService("biddingProjectReportService");
	        biddingProjectReportService.writeProjectPledgeReport(project, report.getStatus(), response);
		}
		catch(Exception e) {
    		return transactException(e, mapping, form, request, response, true);
        }
    	return null;
    }
}
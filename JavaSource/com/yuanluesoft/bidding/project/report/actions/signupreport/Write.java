package com.yuanluesoft.bidding.project.report.actions.signupreport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.report.forms.admin.ProjectSignUpReport;
import com.yuanluesoft.bidding.project.report.service.BiddingProjectReportService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 输出项目投标汇总
 * @author lmiky
 *
 */
public class Write extends FormAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
			//会话检查
			ProjectSignUpReport report = (ProjectSignUpReport)form;
			BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
			BiddingProject project = biddingProjectService.getProject(report.getProjectId());
			//检查加载权限
			if(project.getPlan()==null || project.getPlan().getBuyDocumentEnd()==null || !project.getPlan().getBuyDocumentEnd().before(DateTimeUtils.now())) { //已经过了购买标书截止时间
				throw new PrivilegeException();
			}
			BiddingProjectReportService biddingProjectReportService = (BiddingProjectReportService)getService("biddingProjectReportService");
	        biddingProjectReportService.writeProjectSignUpReport(project, response);
		}
		catch(Exception e) {
    		return transactException(e, mapping, form, request, response, true);
        }
    	return null;
    }
}
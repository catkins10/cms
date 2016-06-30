package com.yuanluesoft.bidding.project.actions.project.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 发布招标文件、招标公告
 * @author yuanlue
 *
 */
public class PublicTender extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, null, null);
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Project projectForm = (Project)workflowForm;
		BiddingProject project = (BiddingProject)record;
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		try {
			//拷贝招标文件到评标系统
			biddingProjectService.sendBiddingDocumentToEvaluateSystem(project);
		}
		catch(ServiceException e) {
			projectForm.setError("传输到标书服务器时出错");
			throw new ValidateException();
		}
		//发布招标公告
		biddingProjectService.publicProjectComponent(projectForm.getTender(), sessionInfo);
		//发布实质性内容
		if(projectForm.getMaterial().getId()>0) {
			biddingProjectService.publicProjectComponent(projectForm.getMaterial(), sessionInfo);
		}
		//发布时间安排
		if(projectForm.getPlan().getId()>0) {
			biddingProjectService.publicProjectComponent(projectForm.getPlan(), sessionInfo);
		}
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}
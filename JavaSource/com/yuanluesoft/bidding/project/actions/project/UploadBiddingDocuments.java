package com.yuanluesoft.bidding.project.actions.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 完成招标文件上传
 * @author yuanlue
 *
 */
public class UploadBiddingDocuments extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, null, "submitted");
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		Project projectForm = (Project)form;
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		if(projectForm.getPlan().getBidopeningTime()==null) {
			projectForm.setError("开标时间不能为空");
			throw new ValidateException();
		}
		//检查当天的开标项目数是否超过开标室的数量
		if(biddingProjectService.isBidopeningProjectOverflow(projectForm.getPlan(), ((BiddingProject)record).getCity())) {
			projectForm.setError("开标室已满，请重新选择开标时间。");
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		BiddingProject project = (BiddingProject)record;
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		try {
			biddingProjectService.uploadBiddingDocument(project, sessionInfo);
		}
		catch (ServiceException e) {
			workflowForm.setError(e.getMessage());
			throw new ValidateException();
		}
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}
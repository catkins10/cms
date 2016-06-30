package com.yuanluesoft.chd.evaluation.actions.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.chd.evaluation.forms.admin.DirectoryForm;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;

/**
 * 
 * @author linchuan
 *
 */
public class DirectoryAction extends com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#getEditablePopedomNames()
	 */
	protected String getEditablePopedomNames() {
		return "manager,leader";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(((ChdEvaluationDirectory)record).getSourceDirectoryId()!=0) { //系统自动复制的目录
			throw new PrivilegeException();
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}
	
	/**
	 * 工作流配置
	 * @param directoryForm
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void workflowConfig(DirectoryForm directoryForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=chdEvaluationDirectoryService&directoryId=" + directoryForm.getId() + "&workflowType=" + request.getParameter("workflowType");
    	String returnURL = Environment.getWebApplicationUrl() + "/chd/evaluation/" + directoryForm.getFormDefine().getName() + ".shtml?act=edit&id=" + directoryForm.getId();
    	//String testURL = Environment.getWebApplicationUrl() + "/chd/evaluation/admin/article.shtml?act=create&columnId=" + directoryForm.getId();
    	String workflowId = ("dataWorkflow".equals(request.getParameter("workflowType")) ? directoryForm.getDataWorkflowId() : directoryForm.getSelfEvalWorkflowId());
    	if(workflowId==null || workflowId.isEmpty()) { //没有配置过
    		workflowConfigureService.createWorkflow(("dataWorkflow".equals(request.getParameter("workflowType")) ? "chd/evaluation/data" : "chd/evaluation/selfeval"), ("dataWorkflow".equals(request.getParameter("workflowType")) ? "data" : "selfeval"), null, notifyURL, returnURL, null, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow(("dataWorkflow".equals(request.getParameter("workflowType")) ? "chd/evaluation/data" : "chd/evaluation/selfeval"), ("dataWorkflow".equals(request.getParameter("workflowType")) ? "data" : "selfeval"), null, workflowId, notifyURL, returnURL, null, response, sessionInfo);
    	}
	}
}
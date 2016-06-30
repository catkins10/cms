package com.yuanluesoft.bidding.project.actions.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 完成登记
 * @author yuanlue
 *
 */
public class CompleteCreate extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeRunAction(mapping, form, request, response, true, "登记完成", "submitted");
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		Project projectForm = (Project)formToValidate;
		if(!"随机抽签".equals(projectForm.getAgentMode()) && projectForm.getBiddingAgent().getAgentId()==0) {
			projectForm.setError("尚未指定代理");
			throw new ValidateException();
		}
		if("否".equals(projectForm.getAgentEnable()) && projectForm.getOwnerId()==0) { //自主招标,建设单位ID不能为0
			projectForm.setError("自主招标时，建设单位必须是后台注册的企业");
			throw new ValidateException();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, boolean, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		BiddingProject project = (BiddingProject)record;
		project.setRegistTime(DateTimeUtils.now()); //设置完成登记时间
		if("是".equals(project.getAgentEnable())) { //委托代理招标
			BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
			biddingProjectService.saveOrUpdateOwner(project.getOwner(), project.getOwnerType(), project.getOwnerRepresentative(), project.getOwnerLinkman(), project.getOwnerLinkmanIdCard(), project.getOwnerTel(), project.getOwnerFax(), project.getOwnerMail());
		}
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}
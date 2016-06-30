package com.yuanluesoft.enterprise.project.actions.project.projectcontract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.actions.project.ProjectAction;
import com.yuanluesoft.enterprise.project.forms.ProjectContract;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProject;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectContract;
import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowInterface;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectContractAction extends ProjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		ProjectContract projectContractForm = (ProjectContract)form;
		//获取工作流界面
		WorkflowInterface workflowInterface;
		try {
			workflowInterface = getWorkflowInterface(projectContractForm, request, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, sessionInfo);
		}
		catch (Exception e) {
			throw new PrivilegeException(e.getMessage());
		}
		if("ContractCreate".equals(workflowInterface.getSubForm()) || "ContractApproval".equals(workflowInterface.getSubForm())) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(acl.contains("manageUnit_contractQuery")) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentResource(com.yuanluesoft.jeaf.form.ActionForm, org.apache.struts.action.ActionMapping, java.lang.Object, java.util.List, char, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
			ProjectContract projectContractForm = (ProjectContract)form;
			//检查是否已经创建过合同
			AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
			List attachments = attachmentService.list(form.getFormDefine().getApplicationName(), "contract", projectContractForm.getContract().getId(), false, 0, request);
			projectContractForm.setContractCreated(attachments!=null && !attachments.isEmpty());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		ProjectContract contractForm = (ProjectContract)form;
		contractForm.getContract().setContractName(contractForm.getName()); //合同名称
		contractForm.getContract().setContractUnit(contractForm.getOwner()); //合同单位
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		EnterpriseProjectContract contract = (EnterpriseProjectContract)component;
		//编号
		if(contract.getContractNo()==null || contract.getContractNo().equals("")) {
			EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
			EnterpriseProject project = (EnterpriseProject)mainRecord;
			contract.setContractNo(enterpriseProjectService.generateContractNumber(project.getType()));
		}
		if("createComponent".equals(form.getAct())) { //新记录
			contract.setCreated(DateTimeUtils.now());
			contract.setCreator(sessionInfo.getUserName());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}
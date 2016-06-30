package com.yuanluesoft.enterprise.project.actions.project.projectcollect;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.project.actions.project.ProjectAction;
import com.yuanluesoft.enterprise.project.forms.ProjectCollect;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectContract;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectCollectAction extends ProjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manageUnit_accounting")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		else if(acl.contains("manageUnit_accountingQuery")) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo,	request);
		ProjectCollect projectCollectForm = (ProjectCollect)form;
		//检查合同列表,如果只有一个合同,自动设置为当前合同
		if(projectCollectForm.getContracts()!=null && projectCollectForm.getContracts().size()==1) {
			EnterpriseProjectContract contract = (EnterpriseProjectContract)projectCollectForm.getContracts().iterator().next();
			projectCollectForm.getCollect().setContractId(contract.getId());
			projectCollectForm.setContractName(contract.getContractName());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillComponentForm(ActionForm form, Record component, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.fillComponentForm(form, component, mainRecord, componentName, sessionInfo, request);
		ProjectCollect projectCollectForm = (ProjectCollect)form;
		EnterpriseProjectContract contract = projectCollectForm.getCollect().getContract();
		if(contract!=null) {
			projectCollectForm.getCollect().setContractId(contract.getId());
			projectCollectForm.setContractName(contract.getContractName());
		}
	}
}
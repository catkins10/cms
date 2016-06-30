package com.yuanluesoft.msa.fees.actions.fees.item;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.msa.fees.actions.fees.FeesAction;
import com.yuanluesoft.msa.fees.pojo.MsaFeesItem;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowInterface;

/**
 * 
 * @author linchuan
 *
 */
public class FeesItemAction extends FeesAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		char accessLevel = super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
		if(accessLevel>RecordControlService.ACCESS_LEVEL_READONLY) { //有编辑权限
			try {
				WorkflowInterface workflowInterface = getWorkflowInterface((WorkflowForm)form, request, record, form.getAct(), sessionInfo);
				if(workflowInterface.getSubForm()==null || SUBFORM_READ.equals(workflowInterface.getSubForm())) { //只读表单
					accessLevel = RecordControlService.ACCESS_LEVEL_READONLY;
				}
			}
			catch (Exception e) {
				
			}
		}
		return accessLevel;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeleteComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeleteComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		super.checkDeleteComponentPrivilege(form, request, record, component, acl, sessionInfo);
		WorkflowInterface workflowInterface = null;
		try {
			workflowInterface = getWorkflowInterface((WorkflowForm)form, request, record, form.getAct(), sessionInfo);
		}
		catch (Exception e) {
			
		}
		if(!SUBFORM_EDIT.equals(workflowInterface.getSubForm())) { //不是编辑状态
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentResource(com.yuanluesoft.jeaf.form.ActionForm, org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.util.List, char, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		MsaFeesItem feesItem = (MsaFeesItem)component;
		boolean transact = false;
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) {
			form.setSubForm("Create");
		}
		else if("Transact".equals(getWorkflowInterface((WorkflowForm)form, request, record, form.getAct(), sessionInfo).getSubForm())) { //办理状态
			form.setSubForm("Transact");
			transact = feesItem.getCompleteTime()==null;
		}
		super.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		if(!transact) {
			form.getFormActions().removeFormAction("办结");
		}
	}
}
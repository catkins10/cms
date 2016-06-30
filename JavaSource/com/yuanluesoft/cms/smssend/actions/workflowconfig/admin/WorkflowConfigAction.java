package com.yuanluesoft.cms.smssend.actions.workflowconfig.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.smssend.pojo.SmsSendWorkflow;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowConfigAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			List directoryIds;
			try {
				directoryIds = getOrgService().listDirectoryIds("root,category,area,unit", "manager", false, sessionInfo, 0, 1); //检查是否是某一级目录的管理员
			}
			catch (ServiceException e) {
				throw new PrivilegeException(e);
			}
			if(directoryIds!=null && !directoryIds.isEmpty()) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		SmsSendWorkflow workflowConfig = (SmsSendWorkflow)record;
		com.yuanluesoft.cms.smssend.forms.admin.SmsSendWorkflow workflowConfigForm = (com.yuanluesoft.cms.smssend.forms.admin.SmsSendWorkflow)form;
		try {
			if(getOrgService().isOrgManager(workflowConfig==null ? workflowConfigForm.getOrgId() : workflowConfig.getOrgId(), sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		} 
		catch (ServiceException e) {
			throw new PrivilegeException(e);
		}
		throw new PrivilegeException();
	}
}
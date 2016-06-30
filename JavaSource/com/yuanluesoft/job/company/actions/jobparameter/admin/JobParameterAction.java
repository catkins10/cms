package com.yuanluesoft.job.company.actions.jobparameter.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.job.company.forms.admin.JobParameter;
import com.yuanluesoft.job.company.pojo.JobParameterDirectory;
import com.yuanluesoft.job.company.service.JobParameterService;

/**
 * 
 * @author linchuan
 *
 */
public class JobParameterAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		JobParameter jobParameterForm = (JobParameter)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			jobParameterForm.setSubForm("Regist");
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		JobParameterService jobParameterService = (JobParameterService)getService("jobParameterService");
		form.setFormTitle(jobParameterService.getDirectoryTypeTitle(record==null ? jobParameterForm.getDirectoryType() : ((JobParameterDirectory)record).getDirectoryType()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRefreshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		if("delete".equals(currentAction)) {
			return "DialogUtils.getDialogOpener().setTimeout(\"window.tree.deleteNode('" + form.getId() + "')\", 10);";
		}
		else if("save".equals(currentAction)) {
			Directory directory = (Directory)record;
			if(OPEN_MODE_CREATE.equals(openMode)) { //新目录
				return "DialogUtils.getDialogOpener().setTimeout(\"window.tree.reloadChildNodes('" + (directory==null ? ((JobParameter)form).getParentDirectoryId() : directory.getParentDirectoryId()) + "')\", 10);";
			}
			else { //更新目录
				return "DialogUtils.getDialogOpener().setTimeout(\"window.tree.renameNode('" + directory.getId() + "', '" + directory.getDirectoryName() + "')\", 10);";
			}
		}
		return super.generateRefreshOpenerScript(form, record, openMode, currentAction, actionResult, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		JobParameter jobParameterForm = (JobParameter)form;
		JobParameterService jobParameterService = (JobParameterService)getService("jobParameterService");
		if(OPEN_MODE_CREATE.equals(openMode)) {
			String[] parameters = jobParameterForm.getChildParameters().split("\n");
			for(int i=0; i<parameters.length; i++) {
				parameters[i] = parameters[i].trim();
				if(parameters[i].isEmpty()) {
					continue;
				}
				JobParameterDirectory parameter = (JobParameterDirectory)jobParameterService.createDirectory(UUIDLongGenerator.generateId(), jobParameterForm.getParentDirectoryId(), parameters[i], jobParameterForm.getDirectoryType(), null, sessionInfo.getUserId(), sessionInfo.getUserName());
				parameter.setPriority(parameters.length-i);
				jobParameterService.update(parameter);
			}
			return null;
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
package com.yuanluesoft.jeaf.application.builder.actions.applicationform.field;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.builder.actions.applicationform.ApplicationFormAction;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationField;
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
public class FieldAction extends ApplicationFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		char accessLevel = super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
		ApplicationField applicationField = (ApplicationField)component;
		if(applicationField!=null && applicationField.getIsPersistence()!=1) { //非数据库字段字段
			accessLevel = RecordControlService.ACCESS_LEVEL_READONLY;
		}
		return accessLevel;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeleteComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeleteComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		ApplicationField applicationField = (ApplicationField)component;
		if(applicationField!=null && applicationField.getIsPresetting()==1) { //预设字段
			throw new PrivilegeException();
		}
		super.checkDeleteComponentPrivilege(form, request, record, component, acl, sessionInfo);
	}
}
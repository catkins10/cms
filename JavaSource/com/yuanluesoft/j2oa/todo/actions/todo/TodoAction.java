/*
 * Created on 2005-11-14
 *
 */
package com.yuanluesoft.j2oa.todo.actions.todo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.todo.pojo.Todo;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
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
public class TodoAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.core.forms.ActionForm, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建,允许所有人
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		Todo pojoTodo = (Todo)record;
		if(pojoTodo.getPersonId()==sessionInfo.getUserId()) {
			return pojoTodo.getCompleted()=='1' ?  RecordControlService.ACCESS_LEVEL_READONLY : RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkSavePrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建,允许所有人
			return;
		}
		Todo pojoTodo = (Todo)record;
		if(pojoTodo.getPersonId()!=sessionInfo.getUserId()) {
			throw new PrivilegeException();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#saveRecord(com.yuanluesoft.jeaf.core.forms.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Todo pojoTodo = (Todo)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			pojoTodo.setPersonId(sessionInfo.getUserId());
			pojoTodo.setPersonName(sessionInfo.getUserName());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
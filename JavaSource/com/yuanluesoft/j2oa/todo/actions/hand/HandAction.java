/*
 * Created on 2005-11-16
 *
 */
package com.yuanluesoft.j2oa.todo.actions.hand;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.todo.forms.Hand;
import com.yuanluesoft.j2oa.todo.pojo.HandPerson;
import com.yuanluesoft.j2oa.todo.pojo.Todo;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 *
 * @author linchuan
 *
 */
public class HandAction extends FormAction {
	
	/**
	 * 获取当前交办用户
	 * @param pojoTodo
	 * @param sessionInfo
	 * @return
	 */
	protected HandPerson getCurrentHandPerson(Todo pojoTodo, SessionInfo sessionInfo) {
		return (HandPerson)ListUtils.findObjectByProperty(pojoTodo.getHandPersons(), "personId", new Long(sessionInfo.getUserId()));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.core.forms.ActionForm, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建,允许有权限创建的人员
			return acl.contains("manageUnit_createHand") ?  RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_NONE;
		}
		Todo pojoTodo = (Todo)record;
		if(pojoTodo.getPersonId()==sessionInfo.getUserId()) { //交办人
			return pojoTodo.getCompleted()=='1' ? RecordControlService.ACCESS_LEVEL_READONLY : RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(getCurrentHandPerson(pojoTodo, sessionInfo)!=null){ //是被交办人
			if(pojoTodo.getCompleted()=='1') { //已办结
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
			if(pojoTodo.getIsSent()=='1') { //已交办
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Todo pojoTodo = (Todo)record;
		if(pojoTodo.getPersonId()!=sessionInfo.getUserId()) { //只有交办人有删除权限
			throw new PrivilegeException();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Hand formHand = (Hand)form;
		Todo pojoTodo = (Todo)record;
		//设置子表单
		if(accessLevel==RecordControlService.ACCESS_LEVEL_EDITABLE && pojoTodo!=null) {
			if(pojoTodo.getIsSent()!='1') {
				formHand.setSubForm("Edit");
			}
			else {
				formHand.setSubForm(getCurrentHandPerson(pojoTodo, sessionInfo)!=null ? "Feedback" : "Read");
			}
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//设置按钮
		boolean undoHandEnable = false;
		boolean completeEnable = false;
		if(pojoTodo!=null && pojoTodo.getPersonId()==sessionInfo.getUserId()) {
			if(pojoTodo.getCompleted()!='1'){
				completeEnable = true;
				undoHandEnable = (pojoTodo.getIsSent()=='1');
			}
		}
		if(!undoHandEnable) {
			removeFormAction(form, "撤销");
		}
		if(!completeEnable) {
			removeFormAction(form, "办结");
		}
	}
	
	/**
	 * 删除按钮
	 * @param form
	 * @param actionTitle
	 */
	private void removeFormAction(ActionForm form, String actionTitle) {
		form.getFormActions().remove(ListUtils.findObjectByProperty(form.getFormActions(), "title", actionTitle));
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
		pojoTodo.setIsHand('1');
		Hand formHand = (Hand)form;
		String handPersonIds = formHand.getHandPersonIds();
		if(handPersonIds!=null) {
			long id = formHand.getId();
			HashSet handPersonSet = new HashSet();
			String[] ids = handPersonIds.split(",");
			for(int i=0; i<ids.length; i++) {
				HandPerson handPerson = new HandPerson();
				handPerson.setId(UUIDLongGenerator.generateId());
				handPerson.setMainRecordId(id);
				handPerson.setPersonId(Long.parseLong(ids[i]));
				handPersonSet.add(handPerson);
			}
			pojoTodo.setHandPersons(handPersonSet);
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#fillForm(com.yuanluesoft.jeaf.core.forms.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Todo pojoTodo = (Todo)record;
		Hand formHand = (Hand)form;
		if(pojoTodo.getHandPersons()!=null) {
			String handPersonIds = "";
			for(Iterator iterator = pojoTodo.getHandPersons().iterator(); iterator.hasNext();) {
				HandPerson handPerson = (HandPerson)iterator.next();
				handPersonIds += (handPersonIds.equals("") ? "":",") + handPerson.getPersonId();
			}
			formHand.setHandPersonIds(handPersonIds);
			PersonService personService = (PersonService)getService("personService");
	    	formHand.setHandPersonNames(ListUtils.join(personService.listPersons(handPersonIds), "name", ",", false));
		}
    	//设置反馈内容
    	HandPerson handPerson = getCurrentHandPerson(pojoTodo, sessionInfo);
    	if(handPerson!=null) {
    		formHand.setFeedback(handPerson.getFeedback());
    	}
	}
}

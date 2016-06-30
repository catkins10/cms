package com.yuanluesoft.telex.receive.cryptic.actions.agentperson;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSignAgent;
import com.yuanluesoft.telex.receive.cryptic.forms.AgentPerson;

/**
 * 
 * @author linchuan
 *
 */
public class CreateRegularPerson extends AgentPersonAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		TelegramSignAgent signAgent = (TelegramSignAgent)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		AgentPerson agentPersonForm = (AgentPerson)form;
		PersonService personService = (PersonService)getService("personService");
		agentPersonForm.setLoginName(personService.generateLoginName(signAgent.getName())); //生成登录用户名
		agentPersonForm.setPassword(UUIDStringGenerator.generateId().substring(0, 6).toLowerCase());
		fillForm(form, record, RecordControlService.ACCESS_LEVEL_EDITABLE, null, sessionInfo, request, response);
		return signAgent;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		AgentPerson agentPersonForm = (AgentPerson)form;
		agentPersonForm.setFormTitle("注册正式用户");
		agentPersonForm.setInnerDialog("createRegularPerson.jsp");
		agentPersonForm.getFormActions().addFormAction(-1, "注册", "FormUtils.doAction('doCreateRegularPerson')", true);
		addReloadAction(agentPersonForm, "取消", request, -1, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#lock(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void lock(com.yuanluesoft.jeaf.form.ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
			
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#isLockByMe(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isLockByMe(com.yuanluesoft.jeaf.form.ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return true;
	}
}
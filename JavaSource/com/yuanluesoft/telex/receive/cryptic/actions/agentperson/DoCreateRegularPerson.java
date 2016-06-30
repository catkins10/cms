package com.yuanluesoft.telex.receive.cryptic.actions.agentperson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSignAgent;
import com.yuanluesoft.telex.receive.cryptic.forms.AgentPerson;

/**
 * 
 * @author linchuan
 *
 */
public class DoCreateRegularPerson extends AgentPersonAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "注册成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		TelegramSignAgent signAgent = (TelegramSignAgent)record;
		AgentPerson agentPersonForm = (AgentPerson)form;
		PersonService personService = (PersonService)getService("personService");
		personService.addEmployee(agentPersonForm.getId(), signAgent.getName(), agentPersonForm.getLoginName(), agentPersonForm.getPassword(), signAgent.getSex(), null, null, null, null, null, "" + signAgent.getOrgId(), sessionInfo.getUserId(), sessionInfo.getUserName());
		deleteRecord(form, form.getFormDefine(), record, request, response, sessionInfo); //删除代理人
		return null;
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
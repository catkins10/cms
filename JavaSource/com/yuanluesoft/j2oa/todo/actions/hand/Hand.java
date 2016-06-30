package com.yuanluesoft.j2oa.todo.actions.hand;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.todo.pojo.HandPerson;
import com.yuanluesoft.j2oa.todo.pojo.Todo;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author LinChuan
*
 */
public class Hand extends HandAction {
	   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "完成交办！", null);
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#saveRecord(com.yuanluesoft.jeaf.core.forms.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Todo pojoTodo = (Todo)record;
		pojoTodo.setIsSent('1');
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//通知接收人
		if(pojoTodo.getHandPersons()!=null) {
			String message = pojoTodo.getSubject() + "(" + sessionInfo.getUserName() + "交办)";
			MessageService messageService = (MessageService)getService("messageService");
			for(Iterator iterator = pojoTodo.getHandPersons().iterator(); iterator.hasNext();) {
				HandPerson handPerson = (HandPerson)iterator.next();
				messageService.sendMessageToPerson(handPerson.getPersonId(), message, sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, pojoTodo.getId(), null, null, null, null, 0, null);
				messageService.sendMessageToPerson(handPerson.getPersonId(), message, sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, pojoTodo.getId(), null, pojoTodo.getBeginTime(), null, null, 0, null);
			}
		}
		return record;
	}
}
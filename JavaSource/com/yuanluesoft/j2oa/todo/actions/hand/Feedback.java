
package com.yuanluesoft.j2oa.todo.actions.hand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.todo.forms.Hand;
import com.yuanluesoft.j2oa.todo.pojo.HandPerson;
import com.yuanluesoft.j2oa.todo.pojo.Todo;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author LinChuan
*
 */
public class Feedback extends HandAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#saveRecord(com.yuanluesoft.jeaf.core.forms.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Todo pojoTodo = (Todo)record;
		HandPerson handPerson = getCurrentHandPerson(pojoTodo, sessionInfo);
		Hand formHand = (Hand)form;
		handPerson.setFeedback(formHand.getFeedback());
		handPerson.setFeedbackTime(DateTimeUtils.now());
		getBusinessService(null).update(handPerson);
		return record;
	}
}
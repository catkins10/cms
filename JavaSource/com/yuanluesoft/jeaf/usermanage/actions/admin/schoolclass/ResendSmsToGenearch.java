package com.yuanluesoft.jeaf.usermanage.actions.admin.schoolclass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.RegistPersonService;

/**
 * 
 * @author linchuan
 *
 */
public class ResendSmsToGenearch extends SchoolClassAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSaveAction(mapping, form, request, response, false, null, "短信重发完成!", null);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.kd9191edu.usermanage.actions.admin.school.SchoolAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		RegistPersonService registPersonService = (RegistPersonService)getService("registPersonService");
		registPersonService.resendSmsToGenearch(form.getId());
		return record;
	}
}
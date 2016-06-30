package com.yuanluesoft.educ.student.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.educ.student.forms.admin.StudentForm;
import com.yuanluesoft.educ.student.service.StudentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Alter extends StudentAction {
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getOpenMode(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getOpenMode(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request) {
		form.setAct(OPEN_MODE_EDIT);
		return OPEN_MODE_EDIT;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(com.yuanluesoft.jeaf.form.ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		StudentForm studentForm = (StudentForm)form;
		StudentService studentService = (StudentService)getService("studentService");
		return studentService.createAlter(studentForm.getAlterStudentId(), sessionInfo);
	}
}
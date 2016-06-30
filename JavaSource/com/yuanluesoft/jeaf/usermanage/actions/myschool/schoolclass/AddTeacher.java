package com.yuanluesoft.jeaf.usermanage.actions.myschool.schoolclass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.usermanage.forms.SchoolClass;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class AddTeacher extends SchoolClassAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, true, "teacher", null, null);
    }
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.org.OrgAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//添加教师
		SchoolClass schoolClassForm = (SchoolClass)form;
		getOrgService().addClassTeacher(schoolClassForm.getId(), schoolClassForm.getTeacherId(), schoolClassForm.getTeacherTitle());
		schoolClassForm.setTeacherId(0);
		schoolClassForm.setTeacherName(null);
		schoolClassForm.setTeacherTitle(null);
		return record;
	}
}
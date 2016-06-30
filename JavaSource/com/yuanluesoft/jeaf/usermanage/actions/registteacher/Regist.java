package com.yuanluesoft.jeaf.usermanage.actions.registteacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.usermanage.pojo.RegistTeacher;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Regist extends RegistTeacherAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) {
    		throw new Exception();
    	}
        return executeSaveAction(mapping, form, request, response, false, null, "注册成功！", null); //您的帐号将在管理员审核后生效！
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
    	RegistTeacher registTeacher = (RegistTeacher)record;
    	String orgName = registTeacher.getOrgFullName();
    	registTeacher.setOrgFullName(orgName.substring(orgName.lastIndexOf('/') + 1));
    	String className = registTeacher.getChargeClassName();
    	if(className!=null && !className.equals("")) {
    		registTeacher.setChargeClassName(className.substring(className.lastIndexOf('/') + 1));
    	}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
/*
 * Created on 2007-4-19
 *
 */
package com.yuanluesoft.jeaf.usermanage.actions.admin.org;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction;
import com.yuanluesoft.jeaf.usermanage.forms.admin.Org;

/**
 * 
 * @author linchuan
 * 
 */
public class OrgAction extends DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#executeDeleteAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public ActionForward executeDeleteAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, String message, String forwardName) throws Exception {
		return super.executeDeleteAction(mapping, form, request, response, message, null);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#executeSaveAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean, java.lang.String)
	 */
	public ActionForward executeSaveAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String tabSelected, String message, String forwardName) throws Exception {
		Org orgForm = (Org)form;
		if(message==null) {
			message = OPEN_MODE_EDIT.equals(orgForm.getAct()) ? "保存成功！" : "注册成功！";
		}
        return super.executeSaveAction(mapping, form, request, response, reload, tabSelected, message, null);
	}
}
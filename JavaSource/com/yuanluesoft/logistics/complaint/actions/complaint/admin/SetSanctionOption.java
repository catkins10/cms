package com.yuanluesoft.logistics.complaint.actions.complaint.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.logistics.complaint.forms.admin.Complaint;

/**
 * 
 * @author linchuan
 *
 */
public class SetSanctionOption extends ComplaintAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Complaint complaintForm = (Complaint)form;
		//设置发布选择内置对话框
		complaintForm.setFormTitle("办结");
		complaintForm.setInnerDialog("/logistics/complaint/admin/sanctionOption.jsp");
		complaintForm.getFormActions().addFormAction(-1, "完成", "FormUtils.doAction('doSanction')", true);
		addReloadAction(complaintForm, "取消", request, -1, false); //取消
	}
}
package com.yuanluesoft.j2oa.dispatch.actions.dispatch;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.dispatch.forms.Dispatch;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class AdjustDocWord extends DispatchAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Dispatch dispatchForm = (Dispatch)form;
        dispatchForm.setAct(OPEN_MODE_OPEN);
    	return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.actions.dispatch.DispatchAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Dispatch dispatchForm = (Dispatch)form;
		dispatchForm.setFormTitle("调整文件字");
		dispatchForm.getFormActions().addFormAction(-1, "确定", "doOK()", true);
		dispatchForm.getFormActions().addFormAction(-1, "取消", "DialogUtils.closeDialog()", false);
	}
}
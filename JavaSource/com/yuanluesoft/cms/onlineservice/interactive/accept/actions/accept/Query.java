package com.yuanluesoft.cms.onlineservice.interactive.accept.actions.accept;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.interactive.accept.forms.Accept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.cms.onlineservice.interactive.actions.OnlineServiceInteractiveAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Query extends OnlineServiceInteractiveAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		OnlineServiceAccept accept = (OnlineServiceAccept)record;
		Accept acceptForm = (Accept)form;
		if(accept==null) { //未公开
			acceptForm.setSubForm("onlineServiceAcceptQueryFailed"); //查询失败页面
		}
		else { //查询成功
			acceptForm.setSubForm("onlineServiceAcceptQueryResult");
		}
	}
}
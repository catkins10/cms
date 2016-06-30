package com.yuanluesoft.cms.onlineservice.interactive.consult.actions.consult;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.interactive.actions.OnlineServiceInteractiveAction;
import com.yuanluesoft.cms.onlineservice.interactive.consult.forms.Consult;
import com.yuanluesoft.cms.onlineservice.interactive.consult.pojo.OnlineServiceConsult;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ConsultAction extends OnlineServiceInteractiveAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		OnlineServiceConsult consult = (OnlineServiceConsult)record;
		Consult consultForm = (Consult)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			consultForm.setSubForm("onlineServiceConsult"); //提交页面
		}
		else if(consult==null || consult.getPublicPass()!='1') { //未公开
			consultForm.setSubForm("onlineServiceConsultFailed"); //查询失败页面
		}
		else if(consult.getPublicBody()=='1' && consult.getPublicWorkflow()=='1') { //完全公开
			consultForm.setSubForm("fullyOnlineServiceConsult");
		}
		else if(consult.getPublicBody()=='1') { //公开正文
			consultForm.setSubForm("originalOnlineServiceConsult");
		}
		else if(consult.getPublicWorkflow()=='1') { //公开办理过程
			consultForm.setSubForm("processingOnlineServiceConsult");
		}
		else { //公开主题
			consultForm.setSubForm("poorOnlineServiceConsult");
		}
	}
}
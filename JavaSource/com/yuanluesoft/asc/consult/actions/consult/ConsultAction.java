package com.yuanluesoft.asc.consult.actions.consult;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.asc.consult.forms.Consult;
import com.yuanluesoft.asc.consult.pojo.AscConsult;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ConsultAction extends PublicServiceAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.ApplicationAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		AscConsult consult = (AscConsult)record;
		Consult consultForm = (Consult)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			consultForm.setSubForm("consult"); //提交页面
		}
		else if(consult==null || consult.getPublicPass()!='1') { //未公开
			consultForm.setSubForm("consultFailed"); //查询失败页面
		}
		else if(consult.getPublicBody()=='1' && consult.getPublicWorkflow()=='1') { //完全公开
			consultForm.setSubForm("fullyConsult");
		}
		else if(consult.getPublicBody()=='1') { //公开正文
			consultForm.setSubForm("originalConsult");
		}
		else if(consult.getPublicWorkflow()=='1') { //公开办理过程
			consultForm.setSubForm("processingConsult");
		}
		else { //公开主题
			consultForm.setSubForm("poorConsult");
		}
	}
}
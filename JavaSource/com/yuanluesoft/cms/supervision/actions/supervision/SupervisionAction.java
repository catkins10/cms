package com.yuanluesoft.cms.supervision.actions.supervision;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.cms.supervision.pojo.Supervision;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SupervisionAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Supervision supervision = (Supervision)record;
		com.yuanluesoft.cms.supervision.forms.Supervision supervisionForm = (com.yuanluesoft.cms.supervision.forms.Supervision)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			supervisionForm.setSubForm("supervision"); //提交页面
		}
		else if(supervision==null || supervision.getPublicPass()!='1') { //未公开
			supervisionForm.setSubForm("supervisionFailed"); //查询失败页面
		}
		else if(supervision.getPublicBody()=='1' && supervision.getPublicWorkflow()=='1') { //完全公开
			supervisionForm.setSubForm("fullySupervision");
		}
		else if(supervision.getPublicBody()=='1') { //公开正文
			supervisionForm.setSubForm("originalSupervision");
		}
		else if(supervision.getPublicWorkflow()=='1') { //公开办理过程
			supervisionForm.setSubForm("processingSupervision");
		}
		else { //公开主题
			supervisionForm.setSubForm("poorSupervision");
		}
	}
}
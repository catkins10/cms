package com.yuanluesoft.jeaf.application.builder.actions.standardforms.publicservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PublicServiceFormAction extends PublicServiceAction {
	
	public PublicServiceFormAction() {
		super();
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		}
		catch(Exception e) {
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.ApplicationAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		PublicService publicService = (PublicService)record;
		String formName = form.getFormDefine().getName();
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			form.setSubForm(formName); //提交页面
		}
		else if(publicService==null || publicService.getPublicPass()!='1') { //未公开
			form.setSubForm(formName + "Failed"); //查询失败页面
		}
		else if(publicService.getPublicBody()=='1' && publicService.getPublicWorkflow()=='1') { //完全公开
			form.setSubForm("fully" + StringUtils.capitalizeFirstLetter(formName));
		}
		else if(publicService.getPublicBody()=='1') { //公开正文
			form.setSubForm("original" + StringUtils.capitalizeFirstLetter(formName));
		}
		else if(publicService.getPublicWorkflow()=='1') { //公开办理过程
			form.setSubForm("processing" + StringUtils.capitalizeFirstLetter(formName));
		}
		else { //公开主题
			form.setSubForm("poor" + StringUtils.capitalizeFirstLetter(formName));
		}
	}
}
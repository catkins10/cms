/*
 * Created on 2007-7-3
 *
 */
package com.yuanluesoft.cms.sitemanage.actions.webdirectory.reference;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.actions.webdirectory.WebDirectoryAction;
import com.yuanluesoft.cms.sitemanage.forms.Reference;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;

/**
 * 
 * @author linchuan
 * 
 */
public class ReferenceAction extends WebDirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.webdirectory.WebDirectoryAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		fillViewParameter(form, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.webdirectory.WebDirectoryAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		fillViewParameter(form, sessionInfo);
	}
	
	/**
	 * 填充视图参数
	 * @param form
	 * @param sessionInfo
	 * @throws Exception
	 */
	public void fillViewParameter(ActionForm form, SessionInfo sessionInfo) throws Exception {
		Reference referenceForm = (Reference)form;
		if(referenceForm.getViewName()!=null && !referenceForm.getViewName().isEmpty()) {
			//设置参数配置需要引入的脚本
			ViewDefineService viewDefineService = (ViewDefineService)getService("viewDefineService");
			View view = viewDefineService.getView(referenceForm.getApplicationName(), referenceForm.getViewName(), sessionInfo);
			referenceForm.setSiteReferenceConfigureJs(ListUtils.generateList((String)view.getExtendParameter("siteReferenceConfigureJs"), ","));
			referenceForm.setSiteReferenceConfigure((String)view.getExtendParameter("siteReferenceConfigure"));
		}
	}
}
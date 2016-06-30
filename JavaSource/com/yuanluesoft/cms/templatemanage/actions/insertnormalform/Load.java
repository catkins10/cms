package com.yuanluesoft.cms.templatemanage.actions.insertnormalform;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor;
import com.yuanluesoft.cms.templatemanage.forms.InsertNormalForm;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends com.yuanluesoft.jeaf.htmleditor.actions.editordialog.Load {
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
    	InsertNormalForm insertForm = (InsertNormalForm)form;
    	FormDefineService formDefineService = (FormDefineService)getService("formDefineService");
    	//获取表单定义
    	Form formDefine = formDefineService.loadFormDefine(insertForm.getApplicationName(), insertForm.getFormName());
     	//获取默认css列表
    	String normalCssFile = formDefine.getExtendedParameter("normalCssFile");
    	if(normalCssFile==null) {
    		normalCssFile = FormProcessor.FORM_NORMAL_CSS_COMPUTER;
    	}
    	request.setAttribute("normalCssFile", normalCssFile); //获取css列表时使用
    	//设置HTML
    	TemplateService templateService = (TemplateService)getService("templateService");
    	insertForm.setPredefinedPage(templateService.getNormalFormHTML(insertForm.getApplicationName(), insertForm.getFormName(), insertForm.getThemeType()));
	}
}
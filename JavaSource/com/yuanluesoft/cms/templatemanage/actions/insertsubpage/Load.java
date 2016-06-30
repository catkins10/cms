package com.yuanluesoft.cms.templatemanage.actions.insertsubpage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteSubPage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor;
import com.yuanluesoft.cms.templatemanage.forms.InsertSubPage;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

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
    	InsertSubPage insertForm = (InsertSubPage)form;
    	PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
    	//设置子页面
    	SitePage sitePage = pageDefineService.getSitePage(insertForm.getApplicationName(), insertForm.getPageName());
    	if(insertForm.getSubPageName()==null || insertForm.getSubPageName().isEmpty()) {
    		insertForm.setSubPageName(((SiteSubPage)sitePage.getSubPages().get(0)).getName());
    	}
    	SiteSubPage subPage = (SiteSubPage)ListUtils.findObjectByProperty(sitePage.getSubPages(), "name", insertForm.getSubPageName());
    	String normalCssFile = subPage.getNormalCssFile();
    	if(normalCssFile==null) {
    		if("iframe".equals(subPage.getType())) {
    			normalCssFile = "/cms/css/application.css";
    		}
    		else {
    			normalCssFile = FormProcessor.FORM_NORMAL_CSS_COMPUTER;
    		}
    	}
		//设置预置页面
    	request.setAttribute("normalCssFile", normalCssFile);
    	//设置HTML
    	TemplateService templateService = (TemplateService)getService("templateService");
    	insertForm.setPredefinedPage(templateService.getSubPageHTML(insertForm.getApplicationName(), insertForm.getSubPageName(), insertForm.getThemeType()));
	}
}
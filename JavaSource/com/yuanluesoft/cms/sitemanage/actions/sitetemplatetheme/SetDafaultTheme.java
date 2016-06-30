package com.yuanluesoft.cms.sitemanage.actions.sitetemplatetheme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.forms.SiteTemplateTheme;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SetDafaultTheme extends SiteTemplateThemeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, false, null, "设置成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.actions.templatetheme.TemplateThemeAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		SiteTemplateTheme themeForm = (SiteTemplateTheme)form;
		//设为默认主题
		((SiteTemplateThemeService)getBusinessService(form.getFormDefine())).setAsDefaultTheme(record.getId(), themeForm.getPageSiteId(), themeForm.getTemporary()==0, themeForm.getTemporary()==1);
		return record;
	}
}
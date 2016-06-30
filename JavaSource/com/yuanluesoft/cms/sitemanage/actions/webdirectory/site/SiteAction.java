/*
 * Created on 2007-7-3
 *
 */
package com.yuanluesoft.cms.sitemanage.actions.webdirectory.site;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.actions.webdirectory.WebDirectoryAction;
import com.yuanluesoft.cms.sitemanage.forms.Site;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 * 
 */
public class SiteAction extends WebDirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Site siteForm = (Site)form;
		siteForm.setUseSiteTemplate('0');
		siteForm.setIsInternal('0'); //默认不是内部网站
	}
}
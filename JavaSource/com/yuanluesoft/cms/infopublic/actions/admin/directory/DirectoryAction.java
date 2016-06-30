package com.yuanluesoft.cms.infopublic.actions.admin.directory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.infopublic.forms.admin.Directory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanluesoft
 *
 */
public class DirectoryAction extends com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Directory directoryForm = (Directory)form;
		directoryForm.setEditorDeleteable('0'); //允许编辑删除,0/从上级继承,1/允许,2/不允许
		directoryForm.setEditorReissueable('0'); //允许编辑撤销,0/从上级继承,1/允许,2/不允许
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Directory directoryForm = (Directory)form;
		SiteService siteService = (SiteService)getService("siteService");
		//设置同步的网站栏目名称
		if(directoryForm.getSynchSiteIds()!=null && !"".equals(directoryForm.getSynchSiteIds())) {
			String synchSiteIds = null;
			String synchSiteNames = null;
			String[] ids = directoryForm.getSynchSiteIds().split(",");
			for(int i=0; i<ids.length; i++) {
				try {
					synchSiteNames = (synchSiteNames==null ? "" : synchSiteNames + ",") + siteService.getDirectoryFullName(Long.parseLong(ids[i]), "/", "site");
					synchSiteIds = (synchSiteIds==null ? "" : synchSiteIds + ",") + ids[i];
				}
				catch(Exception e) {
					
				}
			}
			directoryForm.setSynchSiteIds(synchSiteIds);
			directoryForm.setSynchSiteNames(synchSiteNames);
		}
	}
}
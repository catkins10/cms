package com.yuanluesoft.cms.onlineservice.actions.admin.directory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

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
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		fillSynchColumns(form, "item"); //设置办理事项同步的网站栏目名称
		fillSynchColumns(form, "complaint"); //设置投诉同步的网站栏目名称
		fillSynchColumns(form, "consult"); //设置咨询同步的网站栏目名称
	}
	
	/**
	 * 设置同步的网站栏目名称
	 * @param type
	 * @param form
	 * @throws Exception
	 */
	private void fillSynchColumns(ActionForm form, String type) throws Exception {
		SiteService siteService = (SiteService)getService("siteService");
		//设置同步的网站栏目名称
		String currentSynchSiteIds = (String)PropertyUtils.getProperty(form, type + "SynchSiteIds");
		if(currentSynchSiteIds!=null && !currentSynchSiteIds.isEmpty()) {
			String synchSiteIds = null;
			String synchSiteNames = null;
			String[] ids = currentSynchSiteIds.split(",");
			for(int i=0; i<ids.length; i++) {
				try {
					synchSiteNames = (synchSiteNames==null ? "" : synchSiteNames + ",") + siteService.getDirectoryFullName(Long.parseLong(ids[i]), "/", "site");
					synchSiteIds = (synchSiteIds==null ? "" : synchSiteIds + ",") + ids[i];
				}
				catch(Exception e) {
					
				}
			}
			PropertyUtils.setProperty(form, type + "SynchSiteIds", synchSiteIds);
			PropertyUtils.setProperty(form, type + "SynchSiteNames", synchSiteNames);
		}
	}
}
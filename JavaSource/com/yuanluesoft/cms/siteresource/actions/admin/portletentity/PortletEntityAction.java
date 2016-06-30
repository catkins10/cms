package com.yuanluesoft.cms.siteresource.actions.admin.portletentity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class PortletEntityAction extends com.yuanluesoft.portal.container.actions.portletentity.PortletEntityAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.portal.container.pojo.PortletEntity portletEntity = (com.yuanluesoft.portal.container.pojo.PortletEntity)record;
		if(!OPEN_MODE_CREATE.equals(openMode)) { //旧记录
			return super.saveRecord(form, record, openMode, request, response, sessionInfo);
		}
		String value = portletEntity.getPreferenceValue("bindSiteIds");
		if(value==null || value.isEmpty()) {
			return null;
		}
		SiteService siteService = (SiteService)getService("siteService");
		String[] bindSiteIds = value.split(",");
		String[] bindSiteNames = portletEntity.getPreferenceValue("bindSiteNames").split(",");
		for(int i=0; i<bindSiteIds.length; i++) {
			portletEntity.setId(UUIDLongGenerator.generateId());
			portletEntity.setEntityName(bindSiteNames[i]);
			portletEntity.addPreference("bindSiteIds", bindSiteIds[i]);
			portletEntity.addPreference("bindSiteNames", bindSiteNames[i]);
			WebDirectory webDirectory = (WebDirectory)siteService.getDirectory(Long.parseLong(bindSiteIds[i]));
			if(webDirectory.getDescription()!=null && !webDirectory.getDescription().isEmpty()) {
				portletEntity.setDescription(webDirectory.getDescription());
			}
			else {
				portletEntity.setDescription("文章列表");
			}
			super.saveRecord(form, portletEntity, openMode, request, response, sessionInfo);
		}
		return null;
	}
}
package com.yuanluesoft.cms.infopublic.actions.admin.portletentity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
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
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			return super.saveRecord(form, record, openMode, request, response, sessionInfo);
		}
		String value = portletEntity.getPreferenceValue("bindDirectoryIds");
		if(value==null || value.isEmpty()) {
			return null;
		}
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		String[] bindDirectoryIds = value.split(",");
		String[] bindDirectoryNames = portletEntity.getPreferenceValue("bindDirectoryNames").split(",");
		for(int i=0; i<bindDirectoryIds.length; i++) {
			portletEntity.setId(UUIDLongGenerator.generateId());
			portletEntity.addPreference("bindDirectoryIds", bindDirectoryIds[i]);
			portletEntity.addPreference("bindDirectoryNames", bindDirectoryNames[i]);
			portletEntity.setEntityName(bindDirectoryNames[i]);
			PublicDirectory publicDirectory = (PublicDirectory)publicDirectoryService.getDirectory(Long.parseLong(bindDirectoryIds[i]));
			if(publicDirectory.getDescription()!=null && !publicDirectory.getDescription().isEmpty()) {
				portletEntity.setDescription(publicDirectory.getDescription());
			}
			else {
				portletEntity.setDescription("信息列表");
			}
			super.saveRecord(form, portletEntity, openMode, request, response, sessionInfo);
		}
		return null;
	}
}
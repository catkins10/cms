/*
 * Created on 2006-9-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.actions.securelevel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.archives.actions.base.ArchivesConfigAction;
import com.yuanluesoft.archives.forms.ArchivesSecureLevel;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public class SecureLevelAction extends ArchivesConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		ArchivesSecureLevel configForm = (ArchivesSecureLevel)form;
		//设置密级对应的访问者
		RecordVisitorList visitors = getRecordControlService().getVisitors(configForm.getId(), com.yuanluesoft.archives.pojo.ArchivesSecureLevel.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(visitors!=null) {
			configForm.setVisitors(visitors);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存密级对应的访问者
		ArchivesSecureLevel configForm = (ArchivesSecureLevel)form;
		com.yuanluesoft.archives.pojo.ArchivesSecureLevel secureLevel = (com.yuanluesoft.archives.pojo.ArchivesSecureLevel)record;
		if(configForm.getVisitors().getVisitorIds()!=null) { //提交的内容中必须包含分发范围
			getRecordControlService().updateVisitors(secureLevel.getId(), com.yuanluesoft.archives.pojo.ArchivesSecureLevel.class.getName(), configForm.getVisitors(), RecordControlService.ACCESS_LEVEL_PREREAD);
		}
		return record;
	}
}

package com.yuanluesoft.cms.infopublic.actions.admin.directory.maindirectory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.infopublic.actions.admin.directory.DirectoryAction;
import com.yuanluesoft.cms.infopublic.forms.admin.MainDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicGuide;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanluesoft
 *
 */
public class MainDirectoryAction extends DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		MainDirectory directoryForm = (MainDirectory)form;
		//设置信息公开指南
		if(directoryForm.getGuide()==null) {
			directoryForm.setGuide(directoryForm.getGuides()!=null && !directoryForm.getGuides().isEmpty() ? ((PublicGuide)directoryForm.getGuides().iterator().next()).getGuide() : null);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		MainDirectory directoryForm = (MainDirectory)form;
		//是否允许手工编号
		directoryForm.setManualCodeEnabled('0');
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", null, true);
		form.getTabs().addTab(-1, "publicGuide", "信息公开指南", null, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		MainDirectory directoryForm = (MainDirectory)form;
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//更新信息公开指南
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		publicDirectoryService.updateGuide(directoryForm.getId(), directoryForm.getGuide());
		return record;
	}
}
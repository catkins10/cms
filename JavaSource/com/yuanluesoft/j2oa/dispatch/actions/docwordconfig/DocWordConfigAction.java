/*
 * Created on 2006-9-18
 *
 */
package com.yuanluesoft.j2oa.dispatch.actions.docwordconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.dispatch.forms.DocWordConfig;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchDocWordConfig;
import com.yuanluesoft.j2oa.dispatch.service.DispatchDocWordService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 *
 * @author linchuan
 *
 */
public class DocWordConfigAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		DocWordConfig docWordConfigForm = (DocWordConfig)form;
		if(docWordConfigForm.getFormat()==null) {
			docWordConfigForm.setFormat("<文件字>[<年度>]<序号*4>号");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		DocWordConfig docWordConfigForm = (DocWordConfig)form;
		DispatchDocWordConfig pojoDocWordConfig = (DispatchDocWordConfig)record;
		//设置联合编号文件字列表
		DispatchDocWordService docWordService = (DispatchDocWordService)getService("dispatchDocWordService");
		docWordConfigForm.setUnionDocWords(docWordService.getUnionDocWords(pojoDocWordConfig));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		DocWordConfig docWordConfigForm = (DocWordConfig)form;
		DispatchDocWordConfig pojoConfig = (DispatchDocWordConfig)record;
		//更新组
		DispatchDocWordService docWordService = (DispatchDocWordService)getService("dispatchDocWordService");
		docWordService.updateGroup(pojoConfig, docWordConfigForm.getUnionDocWords());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
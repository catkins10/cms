/*
 * Created on 2006-9-20
 *
 */
package com.yuanluesoft.j2oa.dispatch.actions.templateconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.dispatch.forms.TemplateConfig;
import com.yuanluesoft.j2oa.dispatch.service.DispatchDocWordService;
import com.yuanluesoft.j2oa.document.service.DocumentService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 *
 * @author linchuan
 *
 */
public class TemplateConfigAction extends FormAction {

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
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
        TemplateConfig templateConfigForm = (TemplateConfig)form;
        //设置字段列表
        templateConfigForm.setFields(FieldUtils.listRecordFields(com.yuanluesoft.j2oa.dispatch.pojo.Dispatch.class.getName(), null, null, null, "hidden,none", false, true, false, false, 1));
        //设置文种列表
        templateConfigForm.setAllDocTypes(((DocumentService)getService("documentService")).getDocumentOption().getDocType());
        //设置文件字列表
        DispatchDocWordService docWordService = (DispatchDocWordService)getService("dispatchDocWordService");
        templateConfigForm.setAllDocWords(ListUtils.join(docWordService.listDocWords(), ",", false));
    }
}
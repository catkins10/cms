package com.yuanluesoft.cms.siteresource.report.actions.ensurecolumnconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.report.forms.EnsureColumnConfig;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class EnsureColumnConfigAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		EnsureColumnConfig configForm = (EnsureColumnConfig)form;
		SiteService siteService = (SiteService)getService("siteService");
		if(siteService.checkPopedom(configForm.getSiteId(), "manager", sessionInfo)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		EnsureColumnConfig configForm = (EnsureColumnConfig)form;
		configForm.setColumnType(3); //栏目类型,0/全部栏目,1/其他应保障栏目,2/政府信息公开,3/自定义
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		EnsureColumnConfig configForm = (EnsureColumnConfig)form;
		configForm.setColumnType(configForm.getColumnIds().length()<2 ? Integer.parseInt(configForm.getColumnIds()) : 3); //栏目类型,0/全部栏目,1/其他应保障栏目,2/政府信息公开,3/自定义
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		EnsureColumnConfig configForm = (EnsureColumnConfig)form;
		com.yuanluesoft.cms.siteresource.report.pojo.EnsureColumnConfig ensureColumnConfig = (com.yuanluesoft.cms.siteresource.report.pojo.EnsureColumnConfig)record;
		if(configForm.getColumnType()==0) { //栏目类型,0/全部栏目,1/其他应保障栏目,2/政府信息公开,3/自定义
			ensureColumnConfig.setColumnIds("0");
			ensureColumnConfig.setColumnNames("全部栏目");
		}
		else if(configForm.getColumnType()==1) { //栏目类型,0/全部栏目,1/其他应保障栏目,2/政府信息公开,3/自定义
			ensureColumnConfig.setColumnIds("1");
			ensureColumnConfig.setColumnNames("其他应保障栏目");
		}
		else if(configForm.getColumnType()==2) { //栏目类型,0/全部栏目,1/其他应保障栏目,2/政府信息公开,3/自定义
			ensureColumnConfig.setColumnIds("2");
			ensureColumnConfig.setColumnNames("政府信息公开");
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
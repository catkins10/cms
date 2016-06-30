package com.yuanluesoft.logistics.usermanage.actions.blacklist.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.logistics.usermanage.forms.admin.Blacklist;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsBlacklist;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsUser;
import com.yuanluesoft.logistics.usermanage.service.LogisticsUserService;

/**
 * 
 * @author linchuan
 *
 */
public class BlacklistAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manageUnit_regist")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Blacklist blacklistForm = (Blacklist)form;
		if(blacklistForm.getId()==0 && blacklistForm.getUserId()>0) {
			LogisticsUserService logisticsUserService = (LogisticsUserService)getService("logisticsUserService");
			LogisticsUser user = (LogisticsUser)logisticsUserService.load(LogisticsUser.class, blacklistForm.getUserId());
			if(logisticsUserService.inBlacklist(user)) {
				return (LogisticsBlacklist)user.getBlacklists().iterator().next();
			}
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Blacklist blacklistForm = (Blacklist)form;
		LogisticsUserService logisticsUserService = (LogisticsUserService)getService("logisticsUserService");
		LogisticsUser user = (LogisticsUser)logisticsUserService.load(LogisticsUser.class, blacklistForm.getUserId());
		blacklistForm.setUserName(user.getName());
		blacklistForm.setBlacklistBegin(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		LogisticsBlacklist blacklist = (LogisticsBlacklist)record;
		if(blacklist==null || blacklist.getBlacklistBegin()==null || (blacklist.getBlacklistEnd()!=null && blacklist.getBlacklistEnd().before(DateTimeUtils.now()))) {
			form.getFormActions().removeFormAction("从黑名单中删除");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			LogisticsBlacklist blacklist = (LogisticsBlacklist)record;
			LogisticsUserService logisticsUserService = (LogisticsUserService)getService("logisticsUserService");
			LogisticsUser user = (LogisticsUser)logisticsUserService.load(LogisticsUser.class, blacklist.getUserId());
			blacklist.setUserName(user.getName());
			blacklist.setBlacklistBegin(DateTimeUtils.now());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
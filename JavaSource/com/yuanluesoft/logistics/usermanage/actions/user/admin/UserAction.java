package com.yuanluesoft.logistics.usermanage.actions.user.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.logistics.usermanage.forms.admin.User;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsUser;
import com.yuanluesoft.logistics.usermanage.service.LogisticsUserService;

/**
 * 
 * @author linchuan
 *
 */
public class UserAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manageUnit_regist")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		User userForm = (User)form;
		userForm.setCreated(DateTimeUtils.now());
		userForm.setCreator(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(record!=null) {
			LogisticsUser user = (LogisticsUser)record;
			if(user.getIsApproval()!='0') { //需要审核
				form.getFormActions().removeFormAction("加入黑名单");
				form.getFormActions().removeFormAction("从黑名单中删除");
			}
			else {
				form.getFormActions().removeFormAction("同意注册");
				LogisticsUserService logisticsUserService = (LogisticsUserService)getService("logisticsUserService");
				if(logisticsUserService.inBlacklist(user)) { //在黑名单中
					form.getFormActions().removeFormAction("加入黑名单");
				}
				else {
					form.getFormActions().removeFormAction("从黑名单中删除");
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		LogisticsUser user = (LogisticsUser)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			user.setCreated(DateTimeUtils.now());
			user.setCreator(sessionInfo.getUserName());
			user.setCreatorId(sessionInfo.getUserId());
			user.setCreatorIP(request.getRemoteAddr());
		}
		if(user.getIsCompany()!='1') { //个人
			user.setLinkman(user.getName());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
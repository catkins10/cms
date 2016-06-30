package com.yuanluesoft.microblog.actions.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.microblog.forms.Account;
import com.yuanluesoft.microblog.platform.MicroblogPlatform;
import com.yuanluesoft.microblog.pojo.MicroblogAccount;
import com.yuanluesoft.microblog.service.MicroblogService;

/**
 * 
 * @author linchuan
 *
 */
public class AccountAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			MicroblogAccount account = (MicroblogAccount)record;
			Account accountForm = (Account)form;
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || getOrgService().isOrgManager(OPEN_MODE_CREATE.equals(openMode) ? accountForm.getUnitId() : account.getUnitId(), sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch (ServiceException e) {
		
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Account accountForm = (Account)form;
		MicroblogAccount account = (MicroblogAccount)record;
		MicroblogService microblogService = (MicroblogService)getService("microblogService");
		accountForm.setUnitName(getOrgService().getDirectoryName(accountForm.getUnitId()));
		accountForm.setParameters(microblogService.listMicroblogAccountParameters(accountForm.getPlatform(), account));
		//设置消息接收URL
		String url = RequestUtils.getRequestURL(request, false);
		accountForm.setReceiveMessageURL(url.substring(0, url.indexOf("/", url.indexOf("://") + 3)) + request.getContextPath() + "/microblog/receiveMessage.shtml?accountId=" + accountForm.getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Account accountForm = (Account)form;
		MicroblogService microblogService = (MicroblogService)getService("microblogService");
		//设置单位名称
		accountForm.setUnitName(getOrgService().getDirectoryName(accountForm.getUnitId()));
		//获取平台列表,如果只有一个,则设为平台名称
		List platforms = microblogService.getPlatforms();
		if(platforms!=null && platforms.size()==1) {
			accountForm.setPlatform(((MicroblogPlatform)platforms.get(0)).getName());
		}
		//设置参数列表
		accountForm.setParameters(microblogService.listMicroblogAccountParameters(accountForm.getPlatform(), null));		//设置网址
		//设置网址
		String url = request.getRequestURL().toString();
		accountForm.setSiteUrl(url.substring(0, url.indexOf('/', url.indexOf("://")+3) + 1));
		//设置消息接收URL
		url = RequestUtils.getRequestURL(request, false);
		accountForm.setReceiveMessageURL(url.substring(0, url.indexOf("/", url.indexOf("://") + 3)) + request.getContextPath() + "/microblog/receiveMessage.shtml?accountId=" + accountForm.getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		MicroblogAccount account = (MicroblogAccount)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		MicroblogService microblogService = (MicroblogService)getService("microblogService");
		microblogService.saveMicroblogAccountParameters(account, request.getParameterValues("parameterValue"));
		return account;
	}
}
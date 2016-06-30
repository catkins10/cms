package com.yuanluesoft.wechat.actions.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;
import com.yuanluesoft.wechat.forms.Account;
import com.yuanluesoft.wechat.pojo.WechatAccount;
import com.yuanluesoft.wechat.service.WechatService;

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
			WechatAccount account = (WechatAccount)record;
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
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Record record = super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(record!=null) {
			return record;
		}
		Account accountForm = (Account)form;
		WechatService wechatService = (WechatService)getService("wechatService");
		return wechatService.getWechatAccountByUnitId(accountForm.getUnitId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Account accountForm = (Account)form;
		WechatAccount account = (WechatAccount)record;
		accountForm.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		accountForm.getTabs().addTab(-1, "menu", "菜单配置", "menu.jsp", false);
		//设置消息接收URL
		String url = RequestUtils.getRequestURL(request, false);
		accountForm.setReceiveMessageURL(url.substring(0, url.indexOf("/", url.indexOf("://") + 3)) + request.getContextPath() + "/wechat/receiveMessage.shtml?unitId=" + (account==null ? accountForm.getUnitId() : account.getUnitId()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Account accountForm = (Account)form;
		WechatAccount account = (WechatAccount)record;
		WechatService wechatService = (WechatService)getService("wechatService");
		accountForm.setUnitName(getOrgService().getDirectoryName(account.getUnitId()));
		accountForm.setMenuTree(wechatService.createMenuTree(account));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Account accountForm = (Account)form;
		WechatService wechatService = (WechatService)getService("wechatService");
		accountForm.setUnitName(getOrgService().getDirectoryName(accountForm.getUnitId()));
		accountForm.setMenuTree(wechatService.createMenuTree(null));
		accountForm.setToken(UUIDStringGenerator.generateId().substring(0, 32).toLowerCase());
		//设置网址
		String url = request.getRequestURL().toString();
		accountForm.setSiteUrl(url.substring(0, url.indexOf('/', url.indexOf("://")+3) + 1));
	}
}
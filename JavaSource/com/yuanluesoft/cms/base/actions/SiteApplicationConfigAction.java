package com.yuanluesoft.cms.base.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 站点应用配置操作
 * @author linchuan
 *
 */
public class SiteApplicationConfigAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //应用程序的管理员,总是允许编辑
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		//检查用户对站点的权限，如果是管理员允许编辑，如果是站点编辑，需要有撤销发布的权限
		SiteService siteService = (SiteService)getService("siteService");
		//检查用户对站点的权限
		long siteId = getSiteId(form, record);
		List popedoms = siteService.listDirectoryPopedoms(siteId, sessionInfo);
		if(popedoms==null) {
			throw new PrivilegeException();
		}
		if(popedoms.contains("manager")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		try {
			if(popedoms.contains("editor") && siteService.isEditorReissueable(siteId)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		} 
		catch (ServiceException e) {
			
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//检查用户是否站点编辑，如果是，要求有删除文章的权限
		SiteService siteService = (SiteService)getService("siteService");
		long siteId = getSiteId(form, record);
		List popedoms = siteService.listDirectoryPopedoms(siteId, sessionInfo);
		if(popedoms!=null && !popedoms.contains("manager") && popedoms.contains("editor")) { //不是管理员，仅是编辑
			try {
				if(!siteService.isEditorDeleteable(siteId)) { //不允许编辑删除
					throw new PrivilegeException();
				}
			}
			catch (ServiceException e) {
		
			}
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}
	
	/**
	 * 获取站点ID
	 * @param form
	 * @param record
	 * @return
	 */
	private long getSiteId(ActionForm form, Record record) {
		try {
			return ((Long)PropertyUtils.getProperty((record==null ? (Object)form : record), "siteId")).longValue();
		}
		catch (Exception e) {
			return 0;
		}
	}
}

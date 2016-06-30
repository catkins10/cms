package com.yuanluesoft.appraise.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

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
public class AppraiseFormAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//检查用户的评议管理权限
		if(record==null) { //新记录
			Long areaId = null;
			try {
				areaId = getManagedAreaId(request, sessionInfo);
			} 
			catch (Exception e) {
				
			}
			if(areaId==null || areaId.longValue()<=0) {
				throw new PrivilegeException();
			}
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //应用管理
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		Long areaId = null;
		try {
			areaId = (Long)PropertyUtils.getProperty(record, "areaId");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if(getOrgService().checkPopedom(areaId.longValue(), "appraiseManager", sessionInfo)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Long areaId = getManagedAreaId(request, sessionInfo);
		PropertyUtils.setProperty(form, "areaId", areaId);
		PropertyUtils.setProperty(form, "area", getOrgService().getDirectoryName(areaId.longValue()));
	}
	
	/**
	 * 获取用户有评议管理权限的地区ID
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected Long getManagedAreaId(HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Long areaId = (Long)request.getAttribute("managedAreaId");
		if(areaId!=null) {
			return areaId;
		}
		List ids = getOrgService().listDirectoryIds("area", "appraiseManager", true, sessionInfo, 0, 1);
		areaId = (ids==null || ids.isEmpty() ? new Long(0) : (Long)ids.get(0));
		request.setAttribute("managedAreaId", areaId);
		return areaId;
	}
}
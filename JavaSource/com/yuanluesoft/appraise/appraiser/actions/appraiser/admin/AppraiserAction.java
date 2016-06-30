package com.yuanluesoft.appraise.appraiser.actions.appraiser.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.appraise.appraiser.pojo.Appraiser;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Area;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiserAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException(); //不允许直接新建
		}
		//检查用户是否对应目录的评议管理员或者上级目录的评议管理员
		OrgService orgService = getOrgService();
		Appraiser appraiser = (Appraiser)record;
		try {
			Org org = orgService.getOrg(appraiser.getOrgId());
			if(!(org instanceof Area)) {
				List areas = orgService.listParentDirectories(appraiser.getOrgId(), "area");
				org = (Area)areas.get(0);
			}
			if(orgService.checkPopedom(org.getId(), "appraiseManager", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch(ServiceException e) {
			
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}
}
package com.yuanluesoft.jeaf.tools.regeneratestaticpages.actions;

import java.util.List;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class RegenerateStaticPagesAction extends BaseAction {

    /**
	 * 权限检查
	 * @param sessionInfo
	 * @throws Exception
	 */
	public void checkPrivilege(SessionInfo sessionInfo) throws Exception {
		//检查用户有没有用户管理的权限
		AccessControlService accessControlService = (AccessControlService)getService("accessControlService");
		List acl = accessControlService.getAcl("jeaf/usermanage", sessionInfo);
		if(!acl.contains("application_manager")) {
			throw new PrivilegeException();
		}
	}
}
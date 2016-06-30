package com.yuanluesoft.j2oa.databank.actions.adjustdirectorypriority;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.databank.service.DatabankDirectoryService;
import com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction;
import com.yuanluesoft.jeaf.dialog.forms.AdjustPriority;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 调整优先级
 * @author linchuan
 *
 */
public class AdjustDirectoryPriorityAction extends AdjustPriorityAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewApplicationName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewApplicationName(AdjustPriority adjustPriorityForm) {
		return "j2oa/databank";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewName(AdjustPriority adjustPriorityForm) {
		return "directory";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#resetView(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void resetView(AdjustPriority adjustPriorityForm, View view, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		long directoryId = getParentDirectoryId(request);
		String where = "DatabankDirectory.parentDirectoryId=" + directoryId + 
					   (directoryId==0 ? " and DatabankDirectory.id!=0" : "");
		view.addWhere(where);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#checkAdjustPrivilege(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkAdjustPrivilege(AdjustPriority form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		DatabankDirectoryService databankDirectoryService = (DatabankDirectoryService)getService("databankDirectoryService");
		//检查用户的管理权限
		if(databankDirectoryService.checkPopedom(getParentDirectoryId(request), "manager", sessionInfo)) {
			return;
		}
	    throw new PrivilegeException();
	}
	
	/**
	 * 获取父目录ID
	 * @param request
	 * @return
	 */
	private long getParentDirectoryId(HttpServletRequest request) {
		String parentDirectoryId = request.getParameter("parentDirectoryId");
		return (parentDirectoryId==null ? 0 : Long.parseLong(parentDirectoryId));
	}
}
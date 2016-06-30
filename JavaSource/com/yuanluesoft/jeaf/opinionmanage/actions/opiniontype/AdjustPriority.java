package com.yuanluesoft.jeaf.opinionmanage.actions.opiniontype;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class AdjustPriority  extends AdjustPriorityAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewApplicationName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewApplicationName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority adjustPriorityForm) {
		return "jeaf/opinionmanage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority adjustPriorityForm) {
		return "opinionType";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#checkAdjustPrivilege(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkAdjustPrivilege(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
			BusinessObject businessObject = businessDefineService.getBusinessObject(request.getParameter("businessClassName"));
			acl = getAcl(businessObject.getApplicationName(), sessionInfo);
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
				return;
			}
		}
		catch(Exception e) {
			
		}
		throw new PrivilegeException();
	}
}
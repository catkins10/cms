package com.yuanluesoft.chd.evaluation.actions.admin.adjustsitepriority;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction;
import com.yuanluesoft.jeaf.dialog.forms.AdjustPriority;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class AdjustDirectoryPriorityAction extends AdjustPriorityAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewApplicationName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewApplicationName(AdjustPriority adjustPriorityForm) {
		return "chd/evaluation";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewName(AdjustPriority adjustPriorityForm) {
		return "admin/evaluationDirectory";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#resetView(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void resetView(AdjustPriority adjustPriorityForm, View view, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		long parentDirectoryId = RequestUtils.getParameterLongValue(request, "parentDirectoryId");
		String where = "ChdEvaluationDirectory.parentDirectoryId=" + parentDirectoryId +
					   (parentDirectoryId==0 ? " and ChdEvaluationDirectory.id!=0" : "");
		view.addWhere(where);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#checkAdjustPrivilege(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkAdjustPrivilege(AdjustPriority form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		//获取用户对站点/栏目的权限
	   	if(evaluationDirectoryService.checkPopedom(RequestUtils.getParameterLongValue(request, "parentDirectoryId"), "manager", sessionInfo)) {
			return;
		}
	    throw new PrivilegeException();
	}
}
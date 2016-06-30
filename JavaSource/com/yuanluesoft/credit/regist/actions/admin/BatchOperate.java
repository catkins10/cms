/**
 * 
 */
package com.yuanluesoft.credit.regist.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.credit.regist.pojo.admin.CreditUser;
import com.yuanluesoft.credit.regist.service.spring.RegistServiceImpl;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * @author zyh
 */
public class BatchOperate extends ApplicationViewAction {
	
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()!=null && !viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			changeState(mapping, viewForm, request, response, sessionInfo, 1);
			viewForm.getViewPackage().setSelectedIds(null); //清空列表
		}
		return mapping.getInputForward();
	}
	
	/**
	 * 更改状态
	 * @param mapping
	 * @param viewForm
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException 
	 */
	private void changeState(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo,int state) throws ServiceException{
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		DatabaseService databaseService=(DatabaseService) getService("databaseService");
		for(int i=0; i<ids.length; i++) {
			CreditUser user=(CreditUser) databaseService.findRecordById(CreditUser.class.getName(), Long.parseLong(ids[i]));
			if(user.getPassword()!=null &&
					   !"".equals(user.getPassword()) &&
					   (!user.getPassword().startsWith("{") ||
					    !user.getPassword().endsWith("}"))) {
				user.setPassword("{" + user.getPassword() + "}");
					}
//			检查用户是否被使用
			MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
			if(user.getLoginName()!=null && memberServiceList.isLoginNameInUse(user.getLoginName(), user.getId())) {
				user.setRemark("登录用户名已经被使用");
				user.setStatus(2);
				user.setApproveDate(DateTimeUtils.now());
				user.setApprover(sessionInfo.getUserName());
				user.setApproverId(sessionInfo.getUserId());
			}else{
				user.setStatus(1);
				user.setApproveDate(DateTimeUtils.now());
				user.setApprover(sessionInfo.getUserName());
				user.setApproverId(sessionInfo.getUserId());
			}
			RegistServiceImpl registServiceImpl=(RegistServiceImpl) getService("memberRegistService");
			registServiceImpl.update(user);
		}
	}
}

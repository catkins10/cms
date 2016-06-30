package com.yuanluesoft.appraise.appraiser.actions.appraiserview.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.appraise.appraiser.forms.admin.AppraiserView;
import com.yuanluesoft.appraise.appraiser.service.AppraiserService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiserViewAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "appraise/appraiser";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		AppraiserView appraiserViewForm = (AppraiserView)viewForm;
		return appraiserViewForm.getViewName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		AppraiserView appraiserViewForm = (AppraiserView)viewForm;
		OrgService orgService = getOrgService();
		//获取用户对组织机构的权限
		List popedoms = orgService.listDirectoryPopedoms(appraiserViewForm.getOrgId(), sessionInfo);
		if(popedoms==null || popedoms.isEmpty()) { //没有任何权限
	    	view.addWhere("Appraiser.id=-1"); //不输出任何数据
	    	return;
	    }
		if(!"admin/recipient".equals(appraiserViewForm.getViewName())) { //不是服务对象视图
			if(!appraiserViewForm.isDirect()) { //不是市直、县直
				view.addWhere("Appraiser.orgId in (select OrgSubjection.directoryId from OrgSubjection OrgSubjection where OrgSubjection.parentDirectoryId=" + appraiserViewForm.getOrgId() + ")");
			}
			else if("admin/appraiser".equals(appraiserViewForm.getViewName()) || "admin/delegate".equals(appraiserViewForm.getViewName())) { //市直、县直, 基础库或者评议代表
				view.addWhere("Appraiser.orgId=" + appraiserViewForm.getOrgId());
			}
			else { //市直、县直, 服务对象
				view.addWhere("Appraiser.orgId in (select Org.id from Org Org where Org.parentDirectoryId=" + appraiserViewForm.getOrgId() + ")");
			}
		}
	    List actions = new ArrayList();
		if("admin/appraiser".equals(appraiserViewForm.getViewName()) && popedoms.contains("appraiseManager")) { //基础库,评议管理员
			ViewAction viewAction = new ViewAction();
			viewAction.setTitle("导入基础库评议员");
			viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/appraise/appraiser/admin/importAppraiser.shtml?orgId=" + appraiserViewForm.getOrgId() + "&appraiserType=" + AppraiserService.APPRAISER_TYPE_BASIC + "', 430, 200)");
			actions.add(viewAction);
			viewAction = new ViewAction();
			viewAction.setTitle("清空基础库评议员");
			viewAction.setExecute("if(confirm('是否确定要清空基础库评议员？'))FormUtils.doAction('emptyAppraisers', 'appraiserType=" + AppraiserService.APPRAISER_TYPE_BASIC + "')");
			actions.add(viewAction);
		}
		else if("admin/delegate".equals(appraiserViewForm.getViewName()) && popedoms.contains("appraiseManager")) { //评议代表,评议管理员
			ViewAction viewAction = new ViewAction();
			viewAction.setTitle("导入评议代表");
			viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/appraise/appraiser/admin/importAppraiser.shtml?orgId=" + appraiserViewForm.getOrgId() + "&appraiserType=" + AppraiserService.APPRAISER_TYPE_DELEGATE + "', 430, 200)");
			actions.add(viewAction);
			viewAction = new ViewAction();
			viewAction.setTitle("清空评议代表");
			viewAction.setExecute("if(confirm('是否确定要清空评议代表？'))FormUtils.doAction('emptyAppraisers', 'appraiserType=" + AppraiserService.APPRAISER_TYPE_DELEGATE + "')");
			actions.add(viewAction);
		}
		else if("admin/recipient".equals(appraiserViewForm.getViewName()) && popedoms.contains("appraiseTransactor")) { //评议责任人
			ViewAction viewAction = new ViewAction();
			viewAction.setTitle("导入服务对象");
			viewAction.setExecute("PageUtils.newrecord('appraise/appraiser', 'admin/importRecipient', 'mode=fullscreen', 'unitId=" + appraiserViewForm.getOrgId() + "')");
			actions.add(viewAction);
		}
		if(view.getActions()!=null && !view.getActions().isEmpty()) {
			actions.addAll(view.getActions());
		}
		view.setActions(actions);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		AppraiserView appraiserViewForm = (AppraiserView)viewForm;
		location.remove(location.size()-1);
		String fullName = getOrgService().getDirectoryFullName(appraiserViewForm.getOrgId(), "/", null);
		if(fullName!=null) {
			String[] names = fullName.split("/");
			for(int i=0; i<names.length; i++) {
				location.add(names[i]);
			}
		}
	}
}
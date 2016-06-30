package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitemview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItemView;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;


/**
 * 
 * @author linchuan
 *
 */
public class ImportServiceItems extends ServiceItemViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ServiceItemView serviceItemView = (ServiceItemView)viewForm;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		if(onlineServiceDirectoryService.checkPopedom(serviceItemView.getDirectoryId(), "manager,transactor", sessionInfo)) { //检查用户权限
			OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
			//引入办理事项
			onlineServiceItemService.importOnlineServiceItems(serviceItemView.getDirectoryId(), serviceItemView.getImportServiceItemIds());
		}
		return mapping.getInputForward();
	}
}
package com.yuanluesoft.portal.server.actions.addportlet;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.portal.container.model.PortletGroup;
import com.yuanluesoft.portal.container.model.PortletEntity;
import com.yuanluesoft.portal.container.service.PortletContainer;
import com.yuanluesoft.portal.server.actions.PortalDialogFormAction;
import com.yuanluesoft.portal.server.forms.AddPortlet;
import com.yuanluesoft.portal.server.model.Portal;
import com.yuanluesoft.portal.server.model.PortalPage;
import com.yuanluesoft.portal.server.model.PortletInstance;
import com.yuanluesoft.portal.server.service.PortalService;

/**
 * 
 * @author linchuan
 *
 */
public class AddPortletAction extends PortalDialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		AddPortlet addPortletForm = (AddPortlet)form;
		loadPortletGroups(addPortletForm, sessionInfo);
	}
	
	/**
	 * 加载portlet分组
	 * @param addPortletForm
	 * @param sessionInfo
	 * @throws Exception
	 */
	private void loadPortletGroups(AddPortlet addPortletForm, SessionInfo sessionInfo) throws Exception {
		PortletContainer portletContainer = (PortletContainer)getService("portletContainer");
		//获取PORTLET实体分类列表
		List portletGroups = portletContainer.listPortletGroups(addPortletForm.getUserId(), addPortletForm.getSiteId(), sessionInfo);
		addPortletForm.setPortletGroups(portletGroups);
		if(portletGroups==null) {
			return;
		}
		//获取PORTLET
		PortalService portalService = (PortalService)getService("portalService");
		Portal portal = portalService.loadPortal(addPortletForm.getApplicationName(), addPortletForm.getPageName(), addPortletForm.getUserId(), addPortletForm.getSiteId(), sessionInfo);
		//获取PORTAL页面
		PortalPage portalPage = portal==null ? null : (PortalPage)ListUtils.findObjectByProperty(portal.getPortalPages(), "id", new Long(addPortletForm.getPageId()));
		if(portalPage==null || portalPage.getPortletInstances()==null) {
			return;
		}
		//设置已添加标志
		for(Iterator iterator = portletGroups.iterator(); portalPage!=null && iterator.hasNext();) {
			PortletGroup portletGroup = (PortletGroup)iterator.next();
			for(Iterator iteratorEntity = portletGroup.getPortletEntities().iterator(); iteratorEntity.hasNext();) {
				PortletEntity portletEntity = (PortletEntity)iteratorEntity.next();
				for(Iterator iteratorInstance = portalPage.getPortletInstances().iterator(); iteratorInstance.hasNext();) {
					PortletInstance portletInstance = (PortletInstance)iteratorInstance.next();
					if(portletEntity.getHandle().equals(portletInstance.getPortletHandle()) &&
					   (portletEntity.getWsrpProducerId()==null ? "" : portletEntity.getWsrpProducerId()).equals(portletInstance.getWsrpProducerId()==null ? "" : portletInstance.getWsrpProducerId())) {
						portletEntity.setAdded(true);
						break;
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		AddPortlet addPortletForm = (AddPortlet)form;
		PortalService portalService = (PortalService)getService("portalService");
		portalService.addPortlets(addPortletForm.getApplicationName(), addPortletForm.getPageName(), addPortletForm.getUserId(), addPortletForm.getSiteId(), addPortletForm.getPageId(), addPortletForm.getSelectedWsrpProducerIds(), addPortletForm.getSelectedPortletHandles(), addPortletForm.getSelectedPortletTitles(), addPortletForm.getPortletStyle(), addPortletForm.getColumnIndex()-1, sessionInfo);
	}
}
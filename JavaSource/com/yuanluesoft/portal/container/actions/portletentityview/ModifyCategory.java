package com.yuanluesoft.portal.container.actions.portletentityview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.portal.container.forms.PortletEntityView;
import com.yuanluesoft.portal.container.pojo.PortletEntity;
import com.yuanluesoft.portal.container.service.PortletDefinitionService;

/**
 * 
 * @author linchuan
 *
 */
public class ModifyCategory extends PortletEntityViewAction {
    
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()==null || viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			return mapping.getInputForward();
		}
		PortletEntityView portletEntityView = (PortletEntityView)viewForm;
		if(portletEntityView.getCategory()==null || portletEntityView.getCategory().isEmpty()) {
			return mapping.getInputForward();
		}
		PortletDefinitionService portletDefinitionService = (PortletDefinitionService)getService("portletDefinitionService"); //PORTLET定义服务
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		for(int i=0; i<ids.length; i++) {
			PortletEntity portletEntity = (PortletEntity)portletDefinitionService.load(PortletEntity.class, Long.parseLong(ids[i]));
			portletEntity.setCategory(portletEntityView.getCategory());
			portletDefinitionService.update(portletEntity);
		}
		viewForm.getViewPackage().setSelectedIds(null); //清空列表
		portletEntityView.setCategory(null);
		return mapping.getInputForward();
	}
}
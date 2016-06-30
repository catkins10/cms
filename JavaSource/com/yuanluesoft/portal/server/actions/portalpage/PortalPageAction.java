package com.yuanluesoft.portal.server.actions.portalpage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.portal.server.actions.PortalDialogFormAction;
import com.yuanluesoft.portal.server.forms.PortalPage;
import com.yuanluesoft.portal.server.model.Portal;
import com.yuanluesoft.portal.server.service.PortalService;

/**
 * 
 * @author linchuan
 *
 */
public class PortalPageAction extends PortalDialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#loadFormResource(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void loadFormResource(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.loadFormResource(form, acl, sessionInfo, request);
		PortalPage portalPageForm = (PortalPage)form;
		if("create".equals(portalPageForm.getAct())) {
			portalPageForm.getFormActions().removeFormAction("删除");
			portalPageForm.getFormActions().removeFormAction("还原默认设置");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		PortalPage portalPageForm = (PortalPage)form;
		if("edit".equals(portalPageForm.getAct())) { //编辑页面
			//获取PORTAL配置
			PortalService portalService = (PortalService)getService("portalService");
			Portal portal = portalService.loadPortal(portalPageForm.getApplicationName(), portalPageForm.getPageName(), portalPageForm.getUserId(), portalPageForm.getSiteId(), sessionInfo);
			com.yuanluesoft.portal.server.model.PortalPage portalPage = portal==null ? null : (com.yuanluesoft.portal.server.model.PortalPage)ListUtils.findObjectByProperty(portal.getPortalPages(), "id", new Long(portalPageForm.getId()));
			if(portalPage!=null) {
				PropertyUtils.copyProperties(portalPageForm, portalPage);
			}
		}
	}
}
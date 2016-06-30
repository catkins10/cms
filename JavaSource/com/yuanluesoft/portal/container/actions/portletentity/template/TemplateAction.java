package com.yuanluesoft.portal.container.actions.portletentity.template;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.portal.container.forms.PortletEntityTemplate;
import com.yuanluesoft.portal.container.pojo.PortletEntity;
import com.yuanluesoft.portal.container.service.PortletDefinitionService;
import com.yuanluesoft.portal.container.service.PortletTemplateService;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateAction extends com.yuanluesoft.cms.templatemanage.actions.template.TemplateAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		com.yuanluesoft.portal.container.pojo.PortletEntityTemplate template = (com.yuanluesoft.portal.container.pojo.PortletEntityTemplate)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(template==null) {
			PortletTemplateService portletTemplateService = (PortletTemplateService)getService("portletTemplateService");
			PortletEntityTemplate templateForm = (PortletEntityTemplate)form;
			template = portletTemplateService.getPortletEntityTemplate(templateForm.getEntityId(), templateForm.getApplicationName(), templateForm.getPageName());
		}
		return template;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.actions.template.TemplateAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		PortletEntityTemplate templateForm = (PortletEntityTemplate)form;
		templateForm.setTemplateName("PORTLET模板");
		//获取预置模板
		PortletTemplateService portletTemplateService = (PortletTemplateService)getService("portletTemplateService");
		PortletEntity portletEntity = getPortletEntity(templateForm, null, request);
		templateForm.setPageHTML(portletTemplateService.getNormalTemplate(portletEntity.getPortletApplication(), portletEntity.getPortletName(), templateForm.getApplicationName(), templateForm.getPageName()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.actions.template.TemplateAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		PortletEntityTemplate templateForm = (PortletEntityTemplate)form;
		com.yuanluesoft.portal.container.pojo.PortletEntityTemplate template = (com.yuanluesoft.portal.container.pojo.PortletEntityTemplate)record;
		PortletEntity portletEntity;
		try {
			portletEntity = getPortletEntity(templateForm, template, request);
		}
		catch (ServiceException e) {
			throw new PrivilegeException(e);
		}
		if(portletEntity.getSiteId()==-1) {
			if(getOrgService().checkPopedom(portletEntity.getOrgId(), "manager", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else {
			if(((SiteService)getService("siteService")).checkPopedom(portletEntity.getSiteId(), "manager", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		throw new PrivilegeException();
	}
	
	/**
	 * 获取PORTLET实体
	 * @param templateForm
	 * @param template
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private PortletEntity getPortletEntity(PortletEntityTemplate templateForm, com.yuanluesoft.portal.container.pojo.PortletEntityTemplate template, HttpServletRequest request) throws ServiceException {
		PortletEntity portletEntity = (PortletEntity)request.getAttribute("portletEntity");
		if(portletEntity!=null) {
			return portletEntity;
		}
		PortletDefinitionService portletDefinitionService = (PortletDefinitionService)getService("portletDefinitionService");
		portletEntity = (PortletEntity)portletDefinitionService.load(PortletEntity.class, template==null ? templateForm.getEntityId() : template.getEntityId());
		request.setAttribute("portletEntity", portletEntity);
		return portletEntity;
	}
}
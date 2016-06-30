package com.yuanluesoft.portal.container.actions.portletentity;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.DynamicFormField;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.portal.container.model.PortletDefinition;
import com.yuanluesoft.portal.container.pojo.PortletEntity;
import com.yuanluesoft.portal.container.pojo.PortletEntityPreference;
import com.yuanluesoft.portal.container.service.PortletDefinitionService;
import com.yuanluesoft.portal.portlet.BasePortlet;
import com.yuanluesoft.portal.portlet.PageBasedPortlet;
import com.yuanluesoft.portal.portlet.TemplateBasedPortlet;

/**
 * 
 * @author linchuan
 *
 */
public class PortletEntityAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		PortletEntity portletEntity = (PortletEntity)record;
		com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm = (com.yuanluesoft.portal.container.forms.PortletEntity)form;
		long orgId = portletEntity==null ? portletEntityForm.getOrgId() : portletEntity.getOrgId();
		long siteId = portletEntity==null ? portletEntityForm.getSiteId() : portletEntity.getSiteId();
		if(siteId==-1) {
			if(getOrgService().checkPopedom(orgId, "manager", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else {
			if(((SiteService)getService("siteService")).checkPopedom(siteId, "manager", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm = (com.yuanluesoft.portal.container.forms.PortletEntity)form;
		PortletDefinition portletDefinition = getPortletDefinition(portletEntityForm, null);
		portletEntityForm.setEntityName(portletDefinition.getDisplayName());
		portletEntityForm.setDescription(portletDefinition.getDescription());
		generatePreferenceFields(portletEntityForm, null); //生成自定义参数字段列表
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm = (com.yuanluesoft.portal.container.forms.PortletEntity)form;
		PortletEntity portletEntity = (PortletEntity)record;
		//设置访问者
		portletEntityForm.setEntityVisitors(getRecordControlService().getVisitors(portletEntityForm.getId(), PortletEntity.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
		generatePreferenceFields(portletEntityForm, portletEntity); //生成自定义参数字段列表
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRecord(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record generateRecord(ActionForm form, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		PortletEntity portletEntity = (PortletEntity)super.generateRecord(form, openMode, request, sessionInfo);
		com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm = (com.yuanluesoft.portal.container.forms.PortletEntity)form;
		portletEntity.setPreferences(null);
		//将自定义参数设为实体参数
		generatePreferenceFields(portletEntityForm, null);
		for(Iterator iterator = portletEntityForm.getFields().iterator(); iterator.hasNext();) {
			DynamicFormField dynamicFormField = (DynamicFormField)iterator.next();
			portletEntity.addPreference(dynamicFormField.getFieldDefine().getName(), request.getParameter(dynamicFormField.getFieldDefine().getName()));
		}
		return portletEntity;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存访问者
		PortletEntity portletEntity = (PortletEntity)record;
		com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm = (com.yuanluesoft.portal.container.forms.PortletEntity)form;
		if(portletEntityForm.getEntityVisitors().getVisitorNames()==null || portletEntityForm.getEntityVisitors().getVisitorNames().isEmpty()) { //提交的内容中没有指定访问者
			portletEntityForm.getEntityVisitors().setVisitorIds("0");
		}
		getRecordControlService().updateVisitors(portletEntity.getId(), PortletEntity.class.getName(), portletEntityForm.getEntityVisitors(), RecordControlService.ACCESS_LEVEL_READONLY);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		PortletEntity portletEntity = (PortletEntity)record;
		com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm = (com.yuanluesoft.portal.container.forms.PortletEntity)form;
		PortletDefinition portletDefinition = getPortletDefinition(portletEntityForm, portletEntity);
		//检查portlet是否从TemplateBasedPortlet继承
		if(OPEN_MODE_EDIT.equals(openMode) && 
		   (TemplateBasedPortlet.class.isAssignableFrom(Class.forName(portletDefinition.getPortletClass())) ||
			PageBasedPortlet.class.isAssignableFrom(Class.forName(portletDefinition.getPortletClass())))) {
			//判断是否有配置页面列表
			String ralationPages = portletDefinition.getInitParameterValue("ralationPages");
			if(ralationPages==null) { //没有指定
				//获取当前应用的index页面
				PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
				SitePage sitePage = pageDefineService.getSitePage(portletEntity==null ? portletEntityForm.getPortletApplication() : portletEntity.getPortletApplication(), "index");
				ralationPages = (sitePage==null ? "首页|cms/sitemanage_index" : "首页|" + sitePage.getApplicationName() + "_index");
			}
			String execute;
			if(ralationPages.indexOf("\\0")==-1) {
				String[] values = ralationPages.split("\\|")[1].split("_");
				execute = "PageUtils.newrecord('portal', 'template', 'mode=dialog,width=80%,height=80%', 'entityId=" + form.getId() + (portletEntity.getSiteId()>0 ? "&siteId=" + portletEntity.getSiteId() : "") + "&applicationName=" + values[0] + "&pageName=" + values[1] + "')";
			}
			else {
				//显示简单菜单,menuItems格式:菜单项标题1|菜单项ID1\0菜单项2\0...\0菜单项n  {selectedId}
				execute = "PopupMenu.popupMenu('" + ralationPages + "', function(menuItemId, menuItemTitle) {PageUtils.newrecord('portal', 'template', 'mode=dialog,width=80%,height=80%', 'entityId=" + form.getId() + (portletEntity.getSiteId()>0 ? "&siteId=" + portletEntity.getSiteId() : "") + "&applicationName=' + menuItemId.split('_')[0] + '&pageName=' + menuItemId.split('_')[1]);}, this, 160, 'right')"; 
			}
			form.getFormActions().addFormAction(0, "模板配置", execute, false);
		}
	}
	
	/**
	 * 获取PORTLET定义
	 * @param portletEntityForm
	 * @param portletEntity
	 * @return
	 * @throws Exception
	 */
	protected PortletDefinition getPortletDefinition(com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm, PortletEntity portletEntity) throws Exception {
		PortletDefinitionService portletDefinitionService = (PortletDefinitionService)getService("portletDefinitionService");
		return portletDefinitionService.getPortletDefinition(portletEntity==null ? portletEntityForm.getPortletApplication() : portletEntity.getPortletApplication(), portletEntity==null ? portletEntityForm.getPortletName() : portletEntity.getPortletName());
	}
	
	/**
	 * 生成自定义参数字段列表
	 * @param portletEntityForm
	 * @param portletEntity
	 * @throws Exception
	 */
	private void generatePreferenceFields(com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm, PortletEntity portletEntity) throws Exception {
		PortletDefinition portletDefinition = getPortletDefinition(portletEntityForm, portletEntity);
		//检查portlet是否从BasePortlet继承
		if(!BasePortlet.class.isAssignableFrom(Class.forName(portletDefinition.getPortletClass()))) {
			return;
		}
		BasePortlet portlet = (BasePortlet)Class.forName(portletDefinition.getPortletClass()).newInstance();
		List dynamicFormFields = portlet.generatePreferenceFields(portletDefinition, null, false);
		if(dynamicFormFields!=null && !dynamicFormFields.isEmpty()) {
			//从portlet实体参数中获取自定义参数值
			for(Iterator iterator = portletEntity==null ? null : dynamicFormFields.iterator(); iterator!=null && iterator.hasNext();) {
				DynamicFormField dynamicFormField = (DynamicFormField)iterator.next();
				PortletEntityPreference preference = (PortletEntityPreference)ListUtils.findObjectByProperty(portletEntity.getPreferences(), "name", dynamicFormField.getFieldDefine().getName());
				if(preference!=null) {
					dynamicFormField.setValue(preference.getValue());
				}
			}
			portletEntityForm.setFields(dynamicFormFields);
		}
	}
}
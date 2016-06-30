package com.yuanluesoft.jeaf.form.actions.handlers;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.exchange.client.exception.ExchangeException;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 
 * @author chuan
 *
 */
public class ComponentHandler {
	private FormAction formAction;
	
	public ComponentHandler(FormAction formAction) {
		this.formAction = formAction;
	}
	
	/**
	 * 加载主记录的组成部分,组成部分的表单必须继承主记录表单,且以表单的一个属性的形式存在,form-config可以不配置
	 * @param mapping
	 * @param form
	 * @param componentName 组成部分名称,也就是在form中的属性名称
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeLoadComponentAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			formAction.redirectToSecureLink(request, response);
        	return null;
        }
		try {
			formAction.loadComponent(mapping, form, componentName, request, response);
    	}
		catch(Exception e) {
    		return formAction.transactException(e, mapping, form, request, response, false);
        }
		ActionForward forward = mapping.findForward("load"); 
        return forward==null ? mapping.getInputForward() : forward;
	}
	
	/**
	 * 保存主记录的组成部分
	 * @param mapping
	 * @param form
	 * @param componentName
	 * @param tabSelected
	 * @param foreignKeyProperty 外键属性
	 * @param refreshActionName 更新主记录页面的操作名称
	 * @param reload 是否重载页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeSaveComponentAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, String tabSelected, String foreignKeyProperty, String refreshActionName, boolean reload, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			formAction.saveComponent(mapping, form, componentName, foreignKeyProperty, reload, request, response);
			if(reload) {
				return mapping.getInputForward();
			}
			//更新主记录页面
			formAction.refreshMainRecord((ActionForm)form, refreshActionName, tabSelected, request, response);
		}
		catch(Exception e) {
			return formAction.transactException(e, mapping, form, request, response, true);
        }
        return null;
	}
	
	/**
	 * 创建下一个
	 * @param mapping
	 * @param form
	 * @param componentName
	 * @param foreignKeyProperty
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeCreateNextComponentAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, String foreignKeyProperty, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			formAction.saveComponent(mapping, form, componentName, foreignKeyProperty, false, request, response);
			ActionForm oldForm = (ActionForm)form.getClass().newInstance();
			Object component = PropertyUtils.getProperty(oldForm, componentName);
			ActionForm newForm = (ActionForm)form;
			PropertyUtils.copyProperties(oldForm, newForm);
			PropertyUtils.setProperty(newForm, componentName, component);
			formAction.inheritComponentProperties(newForm, oldForm);
			newForm.setAct(FormAction.OPEN_MODE_CREATE_COMPONENT);
			formAction.loadComponent(mapping, newForm, componentName, request, response);
		}
		catch(Exception e) {
			return formAction.transactException(e, mapping, form, request, response, true);
        }
        return mapping.getInputForward();
	}
	
	/**
	 * 删除主记录的组成部分
	 * @param mapping
	 * @param form
	 * @param componentName
	 * @param tabSelected
	 * @param refreshActionName 更新主记录页面的操作名称
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeDeleteComponentAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, String tabSelected, String refreshActionName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			throw new Exception();
        }
		ActionForm formToDelete = (ActionForm)form;
		try {
			SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
			Form formDefine = formAction.loadFormDefine(formToDelete, request);
			//加载主记录
			Record record = formAction.loadRecord(formToDelete, formDefine, formToDelete.getId(), sessionInfo, request);
			List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT, sessionInfo);
			//获取组成部分
			Record component = formAction.loadComponentRecord(formToDelete, record, componentName, sessionInfo, request);
			//检查记录的删除权限
			formAction.checkDeleteComponentPrivilege(formToDelete, request, record, component, acl, sessionInfo);
			if(record!=null) { //锁定记录
				if(!formAction.isLockByMe(formToDelete, record, request, FormAction.OPEN_MODE_EDIT, sessionInfo)) {
					throw new LockException();
				}
			}
			//开始删除
			try {
				formAction.deleteComponentRecord(formToDelete, record, componentName, sessionInfo, request);
			}
			catch(ExchangeException exe) {
				reloadComponentForm(formToDelete, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
				throw exe;
			}
			//更新主记录页面
			formAction.refreshMainRecord(formToDelete, refreshActionName, tabSelected, request, response);
		}
    	catch(Exception e) {
    		return formAction.transactException(e, mapping, form, request, response, true);
        }
        return null;
	}
	
	/**
	 * 获取组成部分
	 * @param form
	 * @param mainRecord
	 * @param componentName
	 * @param sessionInfo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		long id = ((Number)PropertyUtils.getProperty(form, componentName + ".id")).longValue();
		return id==0 ? null : formAction.getBusinessService(form.getFormDefine()).load(PropertyUtils.getProperty(form, componentName).getClass(), id);
	}
	
	/**
	 * 初始化组成部分
	 * @param form
	 * @param mainRecord
	 * @param componentName
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(mainRecord!=null) {
			PropertyUtils.copyProperties(form, mainRecord);
		}
		//初始化ID
		Long componentId = (Long)PropertyUtils.getProperty(form, componentName + ".id");
		if(componentId==null || componentId.longValue()==0) {
			PropertyUtils.setProperty(form, componentName + ".id", new Long(UUIDLongGenerator.generateId()));
		}
		//设置初始值
		List fields = FieldUtils.listFormFields(form.getFormDefine(), null, "html,components,attachment,image,video", null, null, true, false, false, false, 0);
		for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(!field.getName().startsWith(componentName + ".")) {
				continue;
			}
			try {
				Object defaultValue = FieldUtils.getFieldDefaultValue(field, true, form.getFormDefine().getApplicationName(), form, request);
				if(defaultValue!=null) {
					PropertyUtils.setProperty(form, field.getName(), defaultValue);
				}
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 填充组成部分
	 * @param form
	 * @param component
	 * @param mainRecord
	 * @param componentName
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	public void fillComponentForm(ActionForm form, Record component, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		ActionForm formClone = null;
		if(isGet) {
			formClone = (ActionForm)form.clone();
		}
		if(mainRecord!=null) {
			PropertyUtils.copyProperties(form, mainRecord);
		}
		PropertyUtils.setProperty(form, componentName, component);
		if(isGet) {
			formAction.copyRequestParameters(form, formClone, request);
		}
	}
	
	/**
	 * 加载组成部分资源
	 * @param form
	 * @param mapping
	 * @param record
	 * @param component
	 * @param componentName
	 * @param acl
	 * @param accessLevel
	 * @param deleteEnable
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		//设置子表单
		String subForm = form.getSubForm();
		if(subForm==null) {
			subForm = FormAction.SUBFORM_READ;
			if(record==null || accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
				subForm = FormAction.SUBFORM_EDIT;
			}
		}
		String formName = (mapping.getInputForward()==null ? null : mapping.getInputForward().getPath());
		if(formName==null || formName.equals("")) {
			formName = request.getRequestURL().toString();
		}
		formName = formName.substring(formName.lastIndexOf('/') + 1, formName.lastIndexOf('.'));
		form.setSubForm(formName + subForm + ".jsp");
		//设置操作列表
		formAction.setFormActions(form, component, acl, accessLevel, deleteEnable, form.getAct(), subForm, request, sessionInfo);
		//设置窗口标题
		formAction.setFormTitle(form, component, request, sessionInfo);
		//设置解锁URL
		form.setUnlockUrl(form.isLocked() && (!form.isInternalForm() || !"dialog".equals(form.getDisplayMode())) ? formAction.generateUnlockUrl(form, record, request, sessionInfo) : null);
	}
	
	/**
	 * 重载组成部分页面
	 * @param form
	 * @param mapping
	 * @param record
	 * @param component
	 * @param componentName
	 * @param acl
	 * @param accessLevel
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	private void reloadComponentForm(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		boolean deleteEnable = false;
		try {
			formAction.checkDeleteComponentPrivilege(form, request, record, component, acl, sessionInfo);
			deleteEnable = true;
		}
		catch(PrivilegeException pe) {
			
		}
		formAction.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		if(FormAction.OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) { //新记录
			//初始化
			formAction.initComponentForm(form, record, componentName, sessionInfo, request);
		}
		else { //组成部分非空
			//填充表单
			component = formAction.loadComponentRecord(form, record, componentName, sessionInfo, request);
			formAction.fillComponentForm(form, component, record, componentName, sessionInfo, request);
		}
	}
	
	/**
	 * 加载组成部分
	 * @param mapping
	 * @param form
	 * @param componentName
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void loadComponent(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
		ActionForm formToLoad = (ActionForm)form;
		Form formDefine = formAction.loadFormDefine(formToLoad, request);
		Record record = formAction.loadRecord(formToLoad, formDefine, formToLoad.getId(), sessionInfo, request); //加载记录
		List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT, sessionInfo);
		//加载组件
		Record component = formAction.loadComponentRecord(formToLoad, record, componentName, sessionInfo, request);
		char accessLevel = formAction.checkLoadComponentPrivilege(formToLoad, request, record, component, acl, sessionInfo);
		formToLoad.setAct(component==null ? FormAction.OPEN_MODE_CREATE_COMPONENT : FormAction.OPEN_MODE_EDIT_COMPONENT); //设置操作
		boolean deleteEnable = false;
		try {
			formAction.checkDeleteComponentPrivilege(formToLoad, request, record, component, acl, sessionInfo);
			deleteEnable = true;
		}
		catch(PrivilegeException pe) {
			
		}
		if(record!=null && formToLoad.isDirectOpenComponent()) {
			try {
				formAction.lock(formToLoad, record, request, FormAction.OPEN_MODE_EDIT, sessionInfo);
				formToLoad.setLocked(true);
			}
			catch(LockException le) { //加锁不成功
				deleteEnable = false; //禁止删除
				String lockPersonName = formAction.getLockPersonName(formToLoad, record, request, FormAction.OPEN_MODE_EDIT, sessionInfo);
				if(lockPersonName!=null) {
					formToLoad.setPrompt(lockPersonName + "正在处理当前记录！");
				}
			}
		}
		//加载资源
		formAction.loadComponentResource(formToLoad, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		if(component==null) { //组成部分为空
			formAction.initComponentForm(formToLoad, record, componentName, sessionInfo, request); //初始化
		}
		else { //组成部分非空
			formAction.fillComponentForm(formToLoad, component, record, componentName, sessionInfo, request); //填充表单
		}
		//匿名页面,设置请求代码,防止恶意提交
		formAction.setRequestCode(formToLoad);
	}
	
	/**
	 * 保存组成部分
	 * @param mapping
	 * @param form
	 * @param componentName
	 * @param foreignKeyProperty
	 * @param reload
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void saveComponent(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, String foreignKeyProperty, boolean reload, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			throw new Exception();
        }
		ActionForm formToSave = (ActionForm)form;
		SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
		Form formDefine = formAction.loadFormDefine(formToSave, request);
		Record record = formAction.loadRecord(formToSave, formDefine, formToSave.getId(), sessionInfo, request); //加载主记录
		List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT, sessionInfo);
		Record component = formAction.generateComponentRecord(formToSave, record, componentName, sessionInfo, request, response);
		//设置外键值
		if(foreignKeyProperty!=null) {
			PropertyUtils.setProperty(component, foreignKeyProperty, new Long(formToSave.getId()));
		}
		//检查主记录的保存权限
		formAction.checkSaveComponentPrivilege(formToSave, request, record, component, acl, sessionInfo);
		if(record!=null) { //锁定记录
			if(!formAction.isLockByMe(formToSave, record, request, FormAction.OPEN_MODE_EDIT, sessionInfo)) {
				reloadComponentForm(formToSave, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
				throw new LockException();
			}
		}
		//校验
		try {
			formAction.validateComponent(formToSave, formToSave.getAct(), record, component, componentName, sessionInfo, request);
		}
		catch(ValidateException ve) {
			reloadComponentForm(formToSave, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
			throw ve;
		}
		boolean newComponent = FormAction.OPEN_MODE_CREATE_COMPONENT.equals(formToSave.getAct());
		//保存
		try {
			formAction.saveComponentRecord(formToSave, record, component, componentName, foreignKeyProperty, sessionInfo, request);
		}
		catch(Exception e) {
			reloadComponentForm(formToSave, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
			formAction.validateDataIntegrity(formAction.getBusinessService(formToSave.getFormDefine()), form, formToSave.getAct(), component, sessionInfo, request);
			throw e;
		}
		if(newComponent) { //新记录
			//从临时附件中注销
			((TemporaryFileManageService)formAction.getService("temporaryFileManageService")).unregistTemporaryAttachment(component.getClass().getName(), component.getId());
		}
		if(request.getParameter("validateCode")!=null) { //有验证码
			//清除验证码
			ValidateCodeService validateCodeService = (ValidateCodeService)formAction.getService("validateCodeService");
			validateCodeService.cleanCode(request, response);
		}
		if(reload) {
			formToSave.setAct(FormAction.OPEN_MODE_EDIT_COMPONENT);
			reloadComponentForm(formToSave, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
		}
	}
	
	/**
	 * 新建下一个时,继承原来输入的信息
	 * @param newForm
	 * @param oldForm
	 * @throws Exception
	 */
	public void inheritComponentProperties(ActionForm newForm, ActionForm oldForm) throws Exception {
		newForm.setId(oldForm.getId());
	}
	
	/**
	 * 组成部分校验
	 * @param form
	 * @param openMode
	 * @param mainRecord
	 * @param component
	 * @param componentName
	 * @param sessionInfo
	 * @param request
	 * @throws ValidateException
	 * @throws SystemUnregistException
	 */
	public void validateComponent(ActionForm form, String openMode, Record mainRecord, Record component, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		//表单校验
		formAction.validateForm(form, mainRecord, form.getAct(), sessionInfo, request);
		//业务逻辑校验
		BusinessService businessService = formAction.getBusinessService(form.getFormDefine());
		try {
			formAction.validateBusiness(businessService, form, openMode, component, sessionInfo, request);
		}
		catch(ValidateException e) {
			throw e;
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new ValidateException(e.getMessage());
		}
	}
	
	/**
	 * 生成组成部分
	 * @param form
	 * @param mainRecord
	 * @param componentName
	 * @param sessionInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Record generateComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Record component = (Record)PropertyUtils.getProperty(form, componentName);
		if(FormAction.OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) { //新记录
			return component;
		}
		component = formAction.loadComponentRecord(form, mainRecord, componentName, sessionInfo, request);
		Enumeration parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			String parameterName = (String)parameterNames.nextElement();
			if(!parameterName.startsWith(componentName + ".")) {
				continue;
			}
			String propertyName = parameterName.substring(componentName.length() + 1);
			if(PropertyUtils.isWriteable(component, propertyName) && PropertyUtils.isReadable(form, parameterName)) { //浏览器有提交的参数
				PropertyUtils.setProperty(component, propertyName, PropertyUtils.getProperty(form, parameterName)); //更新到原来的记录中
			}
		}
		return component;
	}
	
	/**
	 * 保存组成部分
	 * @param form
	 * @param mainRecord
	 * @param component
	 * @param componentName
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		BusinessService businessService = formAction.getBusinessService(form.getFormDefine());
		if(!FormAction.OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) { //修改记录
			businessService.update(component);
		}
		else { //新记录
			List fields = FieldUtils.listFormFields(form.getFormDefine(), null, "html,components,attachment,image,video", null, null, true, false, false, false, 0);
			for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
				Field field = (Field)iterator.next();
				if(!field.getName().startsWith(componentName + ".") || request.getParameter(field.getName())!=null) {
					continue;
				}
				try {
					Object defaultValue = FieldUtils.getFieldDefaultValue(field, true, form.getFormDefine().getApplicationName(), form, request);
					if(defaultValue!=null) {
						PropertyUtils.setProperty(component, field.getName().substring(componentName.length() + 1), defaultValue);
					}
				}
				catch(Exception e) {
					
				}
			}
			businessService.save(component);
			form.setAct(FormAction.OPEN_MODE_EDIT_COMPONENT);
		}
	}
	
	/**
	 * 删除组成部分
	 * @param form
	 * @param mainRecord
	 * @param componentName
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	public void deleteComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		BusinessService businessService = formAction.getBusinessService(form.getFormDefine());
		businessService.delete((Record)PropertyUtils.getProperty(form, componentName));
	}
	
	/**
	 * 检查加载组件的权限
	 * @param form
	 * @param request
	 * @param record
	 * @param component
	 * @param acl
	 * @param sessionInfo
	 * @return
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			char accessLevel = formAction.getRecordControlService().getRegistedRecordAccessLevel(form.getId(), request.getSession());
			if(accessLevel>RecordControlService.ACCESS_LEVEL_NONE) {
				return accessLevel;
			}
		}
		catch (ServiceException e) {
			
		}
		return formAction.checkLoadPrivilege(form, request, record, record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT, acl, sessionInfo);
	}
	
	/**
	 * 检查保存组件的权限
	 * @param form
	 * @param request
	 * @param record
	 * @param component
	 * @param acl
	 * @param sessionInfo
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	public void checkSaveComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(formAction.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo)<RecordControlService.ACCESS_LEVEL_EDITABLE) {
			throw new PrivilegeException();
		}
	}
	
	/**
	 * 检查删除组件的权限
	 * @param form
	 * @param request
	 * @param record
	 * @param component
	 * @param acl
	 * @param sessionInfo
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	public void checkDeleteComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(form.getAct()==null || form.getAct().isEmpty() || form.getAct().equals(FormAction.OPEN_MODE_CREATE) || form.getAct().equals(FormAction.OPEN_MODE_CREATE_COMPONENT)) {
			throw new PrivilegeException();
		}
		formAction.checkSaveComponentPrivilege(form, request, record, component, acl, sessionInfo);
	}
	
	/**
	 * 在组成部分保存或删除后更新主记录页面
	 * @param form
	 * @param refreshActionName
	 * @param tabSelected
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void refreshMainRecord(ActionForm form, String refreshActionName, String tabSelected, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String html = "<html>" +
					  " <head>" +
					  "  <script language=\"JavaScript\" charset=\"utf-8\" src=\"" + request.getContextPath() + "/jeaf/common/js/common.js\"></script>" +
					  "  <script>" +
					  "  function refreshMainRecord() {";
		if(!form.isDirectOpenComponent()) {
			html += (refreshActionName==null ? "" : "    DialogUtils.getDialogOpener().setTimeout(\"FormUtils.doAction('" + refreshActionName + "', 'tabSelected=" + tabSelected + "')\", 1);");
		}
		else {
			html += "if(DialogUtils.getDialogOpener().document.getElementsByName('viewPackage.recordCount').length>0) {" + //视图
				    "		DialogUtils.getDialogOpener().setTimeout(\"refreshView('viewPackage');\", 1);" +
				    "}";
		}
		html += 	  "    DialogUtils.closeDialog();" +
					  "  }" +
					  "  </script>" +
					  " </head>" +
					  " <body onload=\"refreshMainRecord()\"></body>" +
					  "</html>";
		response.getWriter().write(html);
	}
}
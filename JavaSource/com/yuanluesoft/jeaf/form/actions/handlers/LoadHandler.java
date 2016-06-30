package com.yuanluesoft.jeaf.form.actions.handlers;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.charge.servicemanage.service.ServiceManage;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.callback.HideConditionCheckCallback;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.formula.service.FormulaService;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 加载
 * @author chuan
 *
 */
public class LoadHandler {
	private FormAction formAction;
	
	public LoadHandler(FormAction formAction) {
		this.formAction = formAction;
	}
		
	/**
	 * 执行加载操作 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeLoadAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			formAction.redirectToSecureLink(request, response);
        	return null;
        }
		try {
			formAction.load(form, request, response);
    	}
		catch(Exception e) {
			ActionForward forward = formAction.transactException(e, mapping, form, request, response, false);
			if(forward!=null && forward.getPath()==null) {
				forward = mapping.findForward("load");
			}
			return forward;
        }
		ActionForward forward = mapping.findForward("load");
		return forward==null ? mapping.getInputForward() : forward;
	}
	
	/**
	 * 加载
	 * @param request
	 * @param response
	 * @param mapping
	 * @param formToLoad
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Record load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//会话检查
		SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
		ActionForm formToLoad = (ActionForm)form;
		Form formDefine = formAction.loadFormDefine(formToLoad, request);
		String openMode = formAction.getOpenMode(formToLoad, request);
		boolean isCreate = openMode.equals(FormAction.OPEN_MODE_CREATE);
		Record record = formAction.loadRecord(formToLoad, formDefine, formToLoad.getId(), sessionInfo, request); //加载记录
		if(record!=null) {
			formToLoad.setId(record.getId()); //设置ID
			if(isCreate) { //新建,且记录不为空
				formToLoad.setAct("edit"); //修改表单操作为编辑
				openMode = FormAction.OPEN_MODE_EDIT;
				isCreate = false;
			}
		}
		boolean deleteEnable = false;
		List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, openMode, sessionInfo);
		if(!isCreate) { //加载记录
			try {
				formAction.checkDeletePrivilege(formToLoad, request, record, acl, sessionInfo);
				deleteEnable = true;
			}
			catch(PrivilegeException pe) {
				
			}
		}
		char accessLevel = formAction.checkLoadPrivilege(formToLoad, request, record, openMode, acl, sessionInfo);
		if(accessLevel<RecordControlService.ACCESS_LEVEL_READONLY) {
			throw new PrivilegeException();
		}
		if(FormAction.OPEN_MODE_OPEN.equals(openMode) && accessLevel>RecordControlService.ACCESS_LEVEL_READONLY) {
			accessLevel = RecordControlService.ACCESS_LEVEL_READONLY;
		}
		//计费控制
		formAction.checkCharge(formToLoad, request, record, openMode, acl, sessionInfo);
		if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE && !isCreate) { //有编辑权限
			//加锁记录
			try {
				formAction.lock(formToLoad, record, request, openMode, sessionInfo);
				formToLoad.setLocked(true);
			}
			catch(LockException le) { //加锁不成功
				accessLevel = RecordControlService.ACCESS_LEVEL_READONLY;
				deleteEnable = false; //禁止删除
				String lockPersonName = formAction.getLockPersonName(formToLoad, record, request, openMode, sessionInfo);
				if(lockPersonName!=null) {
					formToLoad.setPrompt(lockPersonName + "正在处理当前记录！");
				}
			}
		}
		//加载表单资源
		formAction.loadResource(formToLoad, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//设置使用记录
		if(formAction.serviceItemInCharge!=null) {
			ServiceManage serviceManage = (ServiceManage)formAction.getService("serviceManage");
			serviceManage.useServiceItem(sessionInfo, formAction.serviceItemInCharge);
		}
		if(isCreate) { //初始化表单
			formAction.initForm(formToLoad, acl, sessionInfo, request, response);
		}
		else { //填充数据库记录到表单
			formAction.fillForm(formToLoad, record, accessLevel, acl, sessionInfo, request, response);
		}
		//注册记录权限,供组成部分页面使用
		formAction.getRecordControlService().registRecordAccessLevel(formToLoad.getId(), accessLevel, request.getSession());
		//设置选中的TAB
		if(formToLoad.getTabSelected()!=null && !formToLoad.getTabSelected().isEmpty()) {
			formToLoad.getTabs().setTabSelected(formToLoad.getTabSelected());
		}
		//匿名页面,设置请求代码,防止恶意提交
		formAction.setRequestCode(formToLoad);
		return record;
	}
	
	/**
	 * 加载表单资源,1.子表单 2.表单操作 3.TAB列表 4.选择项(由应用扩展)
	 * @param form
	 * @param record
	 * @param acl
	 * @param accessLevel
	 * @param deleteEnable
	 * @param openMode
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(form.getDisplayMode()==null || form.getDisplayMode().isEmpty()) {
			form.setDisplayMode("window");
		}
		if(form.getFormDefine()==null) {
			return;
		}
		//设置子表单
		String subForm = form.getSubForm();
		if(subForm==null || subForm.equals("")) {
			subForm = FormAction.SUBFORM_READ;
			if(openMode.equals(FormAction.OPEN_MODE_CREATE) || (openMode.equals(FormAction.OPEN_MODE_EDIT) && accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE)) {
				subForm = FormAction.SUBFORM_EDIT;
			}
		}
		if(!subForm.endsWith(".jsp")) {
			String formName = form.getFormDefine().getName();
			form.setSubForm(formName.substring(formName.lastIndexOf('/') + 1) + subForm + ".jsp");
		}
		//设置操作列表
		formAction.setFormActions(form, record, acl, accessLevel, deleteEnable, openMode, subForm, request, sessionInfo);
		//设置窗口标题
		formAction.setFormTitle(form, record, request, sessionInfo);
		//设置解锁URL
		form.setUnlockUrl(form.isLocked() && (!form.isInternalForm() || !"dialog".equals(form.getDisplayMode())) ? formAction.generateUnlockUrl(form, record, request, sessionInfo) : null);
		//设置重新打开页面的URL
		form.setReloadPageURL(formAction.generateReloadURL(form, request));
	}
	
	/**
	 * 初始化表单,当应用需要初始化表单字段值时,继承并扩展本方法
	 * @param form
	 * @param acl
	 * @param request
	 * @param response
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(form.getId()==0) {
			form.setId(UUIDLongGenerator.generateId()); //初始化ID
		}
		ActionForm oldForm = (ActionForm)request.getSession().getAttribute("internalForm");
		if(oldForm!=null) {
			request.getSession().removeAttribute("internalForm");
			formAction.inheritProperties(form, oldForm);
			form.setDisplayMode("window");
		}
		//设置初始值
		formAction.setFieldDefaultValue(form, request);
		if(!formAction.externalAction) { //不是对外的操作
			request.setAttribute("externalAction", "false");
		}
	}
	
	/**
	 * 填充数据库记录到表单,默认操作为将数据库字段一一拷贝到表单,当应用还有其他要求时,继承并扩展本方法
	 * @param form
	 * @param record
	 * @param accessLevel
	 * @param acl
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		ActionForm formClone = null;
		if(isGet) {
			formClone = (ActionForm)form.clone();
		}
		PropertyUtils.copyProperties(form, record); //拷贝数据库记录到表单
		if(isGet) {
			formAction.copyRequestParameters(form, formClone, request);
		}
		if(PropertyUtils.isWriteable(form, "readerNames")) {
		    RecordVisitorList visitors = formAction.getRecordControlService().getVisitors(form.getId(), record.getClass().getName(), RecordControlService.ACCESS_LEVEL_READONLY);
		    if(visitors!=null) {
		        PropertyUtils.setProperty(form, "readerNames", visitors.getVisitorNames());
		    }
		}
		if(!formAction.externalAction) { //不是对外的操作
			request.setAttribute("externalAction", "false");
		}
	}
	
	/**
	 * 加载持久层对象
	 * @param form
	 * @param formDefine
	 * @param id
	 * @param sessionInfo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(formDefine.getRecordClassName()==null) {
			return null;
		}
		if(FormAction.OPEN_MODE_CREATE.equals(formAction.getOpenMode(form, request)) && (id==0 || !"get".equalsIgnoreCase(request.getMethod()))) { //默认情况下,新建时不加载记录,如果有配置表单,记录是单一的,需要重载本函数
			return null;
		}
		return formAction.getBusinessService(formDefine).load(Class.forName(formDefine.getRecordClassName()), id);
	}
	
	/**
	 * 设置窗口标题
	 * @param form
	 * @param record
	 * @param request
	 * @param sessionInfo
	 */
	public void setFormTitle(ActionForm form, Record record, HttpServletRequest request, SessionInfo sessionInfo) {
		form.setFormTitle(form.getFormDefine().getTitle());
		if(record==null) {
			return;
		}
		String title = StringUtils.getBeanTitle(record);
		if(title!=null && !title.isEmpty()) {
			form.setFormTitle(StringUtils.slice(title, 50, "...") + " - " + form.getFormTitle());
		}
	}
	
	/**
	 * 设置操作列表
	 * @param form
	 * @param record
	 * @param acl
	 * @param accessLevel
	 * @param deleteEnable
	 * @param openMode
	 * @param subForm
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	public void setFormActions(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, String subForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(form.getFormDefine().getActions()==null) { //没有表单操作
			return;
		}
		form.getFormActions().clear();
		FormulaService formulaService = (FormulaService)formAction.getService("formulaService"); //获取公式服务
		HideConditionCheckCallback callback = new HideConditionCheckCallback(accessLevel, deleteEnable, openMode, subForm); //表单按钮隐藏条件判断回调
		int index = 0;
		for(Iterator iterator = form.getFormDefine().getActions().iterator(); iterator.hasNext();) {
			com.yuanluesoft.jeaf.form.model.FormAction formAction = (com.yuanluesoft.jeaf.form.model.FormAction)iterator.next();
			if(!formulaService.checkCondition(formAction.getHideCondition(), callback, form.getFormDefine().getApplicationName(), request, acl, sessionInfo)) {
				if("dialog".equals(form.getDisplayMode()) && ("关闭".equals(formAction.getTitle()) || "取消".equals(formAction.getTitle()))) { //对话框
					//把“关闭”或者“取消”,移动到最后
					form.getFormActions().add(formAction);
					formAction.setExecute(formAction.getExecute().replaceAll("top.\\window\\.close\\(\\)", "DialogUtils.closeDialog()").replaceAll("top\\.close\\(\\)", "DialogUtils.closeDialog()").replaceAll("window\\.close\\(\\)", "DialogUtils.closeDialog()"));
				}
				else {
					form.getFormActions().add(index++, formAction);
				}
				//设置命令中的参数值
				formAction.setExecute(StringUtils.fillParameters(formAction.getExecute(), false, true, false, "utf-8", record, request, null));
			}
		}
	}
	
	/**
	 * 匿名页面,设置请求代码,防止恶意提交
	 * @param formToLoad
	 * @throws Exception
	 */
	public void setRequestCode(ActionForm formToLoad) throws Exception {
		//匿名页面,设置请求代码,防止恶意提交
		if(formAction.anonymousAlways || formAction.anonymousEnable) {
			FormSecurityService formSecurityService = (FormSecurityService)formAction.getService("formSecurityService");
			if(formToLoad.getRequestCode()==null || formToLoad.getRequestCode().isEmpty()) {
				formToLoad.setRequestCode(formSecurityService.registRequest(false));
			}
			else {
				formSecurityService.setValidateCodeImageRequired(formToLoad.getRequestCode(), false);
			}
		}
	}
}
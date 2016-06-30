/*
 * Created on 2004-12-21
 *
 */
package com.yuanluesoft.jeaf.form.actions;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.charge.servicemanage.service.ServiceManage;
import com.yuanluesoft.exchange.client.exception.ExchangeException;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.base.validator.exception.ValidateException;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ChargeException;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.callback.HideConditionCheckCallback;
import com.yuanluesoft.jeaf.form.model.AttachmentSelector;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.formula.service.FormulaService;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.lock.service.LockService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.logger.pojo.ActionLog;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 *
 * @author linchuan
 *
 */
public class FormAction extends BaseAction {
	protected boolean isSecureAction = false; //是否强制安全的URL
	protected boolean forceValidateCode = false; //是否强制验证码校验
	protected boolean mainRecordAction = true; //是否主记录的操作
	protected String serviceItemInCharge = null; //应用在计费系统中的服务项名称,null表示不做计费检查
	//打开方式
	public static final String OPEN_MODE_CREATE = "create"; //创建
	public static final String OPEN_MODE_OPEN = "open"; //打开
	public static final String OPEN_MODE_EDIT = "edit"; //打开并修改
	
	public static final String OPEN_MODE_CREATE_COMPONENT = "createComponent"; //创建组成部分
	public static final String OPEN_MODE_EDIT_COMPONENT = "editComponent"; //修改组成部分
	
	//子表单
	public static final String SUBFORM_READ = "Read"; //只读子表单
	public static final String SUBFORM_EDIT = "Edit"; //编辑子表单
	
	/**
	 * 加载
	 * @param request
	 * @param response
	 * @param mapping
	 * @param formToLoad
	 * @param response
	 * @return
	 * @return
	 * @throws Exception
	 */
	protected Record load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//会话检查
		SessionInfo sessionInfo = getSessionInfo(request, response);
		ActionForm formToLoad = (ActionForm)form;
		Form formDefine = loadFormDefine(formToLoad, request);
		String openMode = getOpenMode(formToLoad, request);
		boolean isCreate = openMode.equals(OPEN_MODE_CREATE);
		
		Record record = loadRecord(formToLoad, formDefine, formToLoad.getId(), sessionInfo, request); //加载记录
		if(isCreate && record!=null) { //新建,且记录不为空
			formToLoad.setAct("edit"); //修改表单操作为编辑
			formToLoad.setId(record.getId()); //设置ID
			openMode = OPEN_MODE_EDIT;
			isCreate = false;
		}
		boolean deleteEnable = false;
		List acl = getAcl(formDefine.getApplicationName(), form, record, openMode, sessionInfo);
		if(!isCreate) { //加载记录
			try {
				checkDeletePrivilege(formToLoad, request, record, acl, sessionInfo);
				deleteEnable = true;
			}
			catch(PrivilegeException pe) {
				
			}
		}
		char accessLevel = checkLoadPrivilege(formToLoad, request, record, openMode, acl, sessionInfo);
		if(accessLevel<RecordControlService.ACCESS_LEVEL_READONLY) {
			throw new PrivilegeException();
		}
		if(OPEN_MODE_OPEN.equals(openMode) && accessLevel>RecordControlService.ACCESS_LEVEL_READONLY) {
			accessLevel = RecordControlService.ACCESS_LEVEL_READONLY;
		}
		//计费控制
		checkCharge(formToLoad, request, record, openMode, acl, sessionInfo);
		if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE && !isCreate) { //有编辑权限
			//加锁记录
			try {
				lock(formToLoad, record, request, openMode, sessionInfo);
				formToLoad.setLocked(true);
			}
			catch(LockException le) { //加锁不成功
				accessLevel = RecordControlService.ACCESS_LEVEL_READONLY;
				deleteEnable = false; //禁止删除
				String lockPersonName = getLockPersonName(formToLoad, record, request, openMode, sessionInfo);
				if(lockPersonName!=null) {
					formToLoad.setPrompt(lockPersonName + "正在处理当前记录！");
				}
			}
		}
		//加载表单资源
		loadResource(formToLoad, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//设置使用记录
		if(serviceItemInCharge!=null) {
			ServiceManage serviceManage = (ServiceManage)getService("serviceManage");
			serviceManage.useServiceItem(sessionInfo, serviceItemInCharge);
		}
		if(isCreate) { //初始化表单
			initForm(formToLoad, sessionInfo, request, response);
		}
		else { //填充数据库记录到表单
			fillForm(formToLoad, record, accessLevel, sessionInfo, request, response);
		}
		//注册记录权限,供组成部分页面使用
		getRecordControlService().registRecordAccessLevel(formToLoad.getId(), accessLevel, request.getSession());
		//设置选中的TAB
		if(formToLoad.getTabSelected()!=null && !formToLoad.getTabSelected().isEmpty()) {
			formToLoad.getTabs().setTabSelected(formToLoad.getTabSelected());
		}
		//匿名页面,设置请求代码,防止恶意提交
		if(anonymousAlways || anonymousEnable) {
			FormSecurityService formSecurityService = (FormSecurityService)getService("formSecurityService");
			if(formToLoad.getRequestCode()==null || formToLoad.getRequestCode().isEmpty()) {
				formToLoad.setRequestCode(formSecurityService.registRequest(false));
			}
			else {
				formSecurityService.setValidateCodeImageRequired(formToLoad.getRequestCode(), false);
			}
		}
		return record;
	}
	
	/**
	 * 加锁记录
	 * @param form
	 * @param request
	 * @param openMode
	 * @return
	 * @throws SystemUnregistException
	 */
	protected void lock(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		if(!sessionInfo.isAnonymous()) {
			getLockService().lock("" + form.getId(), sessionInfo.getUserId(), sessionInfo.getUserName());
		}
	}
	
	/**
	 * 解锁记录
	 * @param form
	 * @param request
	 * @param openMode
	 * @param sessionInfo
	 * @throws LockException
	 * @throws SystemUnregistException
	 */
	protected void unlock(ActionForm form, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		if(!sessionInfo.isAnonymous()) {
			getLockService().unlock("" + form.getId(), sessionInfo.getUserId());
		}
	}
	
	/**
	 * 获取当前加锁记录的用户名
	 * @param form
	 * @param record
	 * @param request
	 * @param openMode
	 * @param sessionInfo
	 * @return
	 * @throws LockException
	 * @throws SystemUnregistException
	 */
	protected String getLockPersonName(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return getLockService().getLockPersonName("" + form.getId());
	}
	
	/**
	 * 检查记录是否被当前用户加锁
	 * @param form
	 * @param request
	 * @param openMode
	 * @param sessionInfo
	 * @throws LockException
	 * @throws SystemUnregistException
	 */
	protected boolean isLockByMe(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return sessionInfo.isAnonymous() || getLockService().isLockByPerson("" + form.getId(), sessionInfo.getUserId());
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
			subForm = SUBFORM_READ;
			if(openMode.equals(OPEN_MODE_CREATE) || (openMode.equals(OPEN_MODE_EDIT) && accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE)) {
				subForm = SUBFORM_EDIT;
			}
		}
		if(!subForm.endsWith(".jsp")) {
			String formName = form.getFormDefine().getName();
			form.setSubForm(formName.substring(formName.lastIndexOf('/') + 1) + subForm + ".jsp");
		}
		
		//设置操作列表
		setFormActions(form, record, acl, accessLevel, deleteEnable, openMode, subForm, request, sessionInfo);
		
		//设置窗口标题
		setFormTitle(form, record, request, sessionInfo);
	}
	
	/**
	 * 初始化表单,当应用需要初始化表单字段值时,继承并扩展本方法
	 * @param form
	 * @param request
	 * @param response
	 */
	public void initForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(form.getId()==0) {
			form.setId(UUIDLongGenerator.generateId()); //初始化ID
		}
		//设置初始值
		setFieldDefaultValue(form, request);
	}
	
	/**
	 * 填充数据库记录到表单,默认操作为将数据库字段一一拷贝到表单,当应用还有其他要求时,继承并扩展本方法
	 * @param form
	 * @param record
	 * @param accessLevel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		ActionForm formClone = null;
		if(isGet) {
			formClone = (ActionForm)form.clone();
		}
		PropertyUtils.copyProperties(form, record); //拷贝数据库记录到表单
		if(isGet) {
			copyRequestParameters(form, formClone, request);
		}
		if(PropertyUtils.isWriteable(form, "readerNames")) {
		    RecordVisitorList visitors = getRecordControlService().getVisitors(form.getId(), record.getClass().getName(), RecordControlService.ACCESS_LEVEL_READONLY);
		    if(visitors!=null) {
		        PropertyUtils.setProperty(form, "readerNames", visitors.getVisitorNames());
		    }
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
	protected Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(formDefine.getRecordClassName()==null) {
			return null;
		}
		if(OPEN_MODE_CREATE.equals(getOpenMode(form, request)) && (id==0 || !"get".equalsIgnoreCase(request.getMethod()))) { //默认情况下,新建时不加载记录,如果有配置表单,记录是单一的,需要重载本函数
			return null;
		}
		return getBusinessService(formDefine).load(Class.forName(formDefine.getRecordClassName()), id);
	}
	
	/**
	 * 设置操作结果页面
	 * @param form
	 * @param record
	 * @param action save/delete/run
	 * @param request
	 * @param sessionInfo
	 */
	protected void setResultPage(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		//设置操作结果
        if(form.getActionResult()==null) {
        	form.setActionResult(actionResult);
        }
		//设置按钮列表
        form.getFormActions().clear();
        if(record!=null) {
        	addReloadAction(form, "返回", request, -1, true);
        }
        form.getFormActions().addFormAction(-1, "关闭", ("dialog".equals(form.getDisplayMode()) ? "closeDialog();" : "top.window.close();"), record==null);
	}
	
	/**
	 * 给内置对话框添加“取消”运行按钮
	 * @param form
	 * @param actionTitle
	 * @param request
	 */
	protected void addReloadAction(ActionForm form, String actionTitle, HttpServletRequest request, int actionIndex, boolean firstAction) {
	   	//添加“取消”操作
    	String script = "submitting = true;" +
    					"window.location = '" + generateReloadURL(form, actionTitle, request) + "';";
    	form.getFormActions().addFormAction(actionIndex, actionTitle==null ? "取消" : actionTitle, script, firstAction);
 	}
	
	/**
	 * 生成重新加载记录的URL
	 * @param form
	 * @param actionTitle
	 * @param request
	 * @return
	 */
	protected String generateReloadURL(ActionForm form, String actionTitle, HttpServletRequest request) {
		return request.getContextPath() + "/" + form.getFormDefine().getApplicationName() + "/" + form.getFormDefine().getName() + ".shtml" +
			   "?act=edit" +
			   "&id=" + form.getId() +
			   (form.getDisplayMode()==null || form.getDisplayMode().isEmpty() ? "" : "&displayMode=" + form.getDisplayMode()) +
			   (request.getParameter("siteId")==null ? "" : "&siteId=" + request.getParameter("siteId")) +
			   "&req=" + System.currentTimeMillis();
	}
	
	/**
	 * 设置窗口标题
	 * @param form
	 * @param record
	 * @param request
	 * @param sessionInfo
	 */
	protected void setFormTitle(ActionForm form, Record record, HttpServletRequest request, SessionInfo sessionInfo) {
		form.setFormTitle(form.getFormDefine().getTitle());
		if(record==null) {
			return;
		}
		String[] properties = new String[] {"subject", "title", "name", "projectName", "directoryName"};
		for(int i=0; i<properties.length; i++) {
			if(!PropertyUtils.isReadable(record, properties[i])) {
				continue;
			}
			try {
				String title = (String)PropertyUtils.getProperty(record, properties[i]);
				if(title!=null) {
					if(!title.isEmpty()) {
						form.setFormTitle(StringUtils.slice(title, 50, "...") + " - " + form.getFormTitle());
					}
				}
				break;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
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
	protected void setFormActions(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, String subForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(form.getFormDefine().getActions()==null) { //没有表单操作
			return;
		}
		form.getFormActions().clear();
		FormulaService formulaService = (FormulaService)getService("formulaService"); //获取公式服务
		HideConditionCheckCallback callback = new HideConditionCheckCallback(accessLevel, deleteEnable, openMode, subForm); //表单按钮隐藏条件判断回调
		int index = 0;
		for(Iterator iterator = form.getFormDefine().getActions().iterator(); iterator.hasNext();) {
			com.yuanluesoft.jeaf.form.model.FormAction formAction = (com.yuanluesoft.jeaf.form.model.FormAction)iterator.next();
			if(!formulaService.checkCondition(formAction.getHideCondition(), callback, form.getFormDefine().getApplicationName(), request, acl, sessionInfo)) {
				if("dialog".equals(form.getDisplayMode()) && ("关闭".equals(formAction.getTitle()) || "取消".equals(formAction.getTitle()))) { //对话框
					//把“关闭”或者“取消”,移动到最后
					form.getFormActions().add(formAction);
					formAction.setExecute(formAction.getExecute().replaceAll("top.\\window\\.close\\(\\)", "closeDialog()").replaceAll("top\\.close\\(\\)", "closeDialog()").replaceAll("window\\.close\\(\\)", "closeDialog()"));
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
	 * 生成更新主窗口的脚本
	 * @param form
	 * @param record
	 * @param openMode
	 * @param action
	 * @param actionResult
	 * @param request
	 * @param sessionInfo
	 */
	protected String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		return "try {var win=" + ("dialog".equals(form.getDisplayMode()) ? "getDialogOpener()" : "top.opener") + ";if(win)win.setTimeout(\"" +
			   " try{" +
			   "	if(document.getElementsByName('viewPackage.recordCount').length>0) {" + //视图
			   "		refreshView('viewPackage');" +
			   "	}" +
			   "	else {" +
			   "		window.location.reload();" +
			   "	}" +
			   " }" +
			   " catch(e) {" +
			   " };\", 10);" +
			   "}" +
			   "catch(e) {" +
			   "}";
	}
	
	
	/**
	 * 把页面提交上来的参数值写入目标对象
	 * @param to
	 * @param form
	 * @param request
	 * @throws Exception
	 */
	private void copyRequestParameters(Object to, ActionForm form, HttpServletRequest request) throws Exception {
		Enumeration parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			String parameterName = (String)parameterNames.nextElement();
			boolean isWriteable = false;
			try {
				isWriteable = PropertyUtils.isWriteable(to, parameterName);
			}
			catch(Exception e) {
			
			}
			if(isWriteable && PropertyUtils.isReadable(form, parameterName)) {
				PropertyUtils.setProperty(to, parameterName, PropertyUtils.getProperty(form, parameterName));
			}
		}
	}
	
	/**
	 *  保存
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param reload 是否重新加载
	 * @param tabSelected
	 * @param actionResult
	 * @return
	 * @throws Exception
	 */
	public Record save(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String tabSelected, String actionResult) throws Exception {
		//会话超时检测
		SessionInfo sessionInfo = getSessionInfo(request, response);
		ActionForm formToSave = (ActionForm)form;
		Form formDefine = loadFormDefine(formToSave, request);
		String openMode = getOpenMode(formToSave, request);
		boolean isCreate = openMode.equals(OPEN_MODE_CREATE);
		//生成持久层对象
		Record record = generateRecord(formToSave, openMode, request, sessionInfo);
		//权限控制
		List acl = getAcl(formDefine.getApplicationName(), form, record, openMode, sessionInfo);
		checkSavePrivilege(formToSave, request, record, openMode, acl, sessionInfo);
		//计费控制
		checkCharge(formToSave, request, record, openMode, acl, sessionInfo);
		if(!isCreate) {
			if(!isLockByMe(formToSave, record, request, openMode, sessionInfo)) {
				//未锁定记录
				reloadForm(formToSave, record, openMode, acl, sessionInfo, request, response);
				throw new LockException();
			}
		}
		try {
			//表单校验
			validateForm(formToSave, record, openMode, sessionInfo, mapping, request);
			
			//业务逻辑校验
			BusinessService businessService = getBusinessService(formDefine);
			validateBusiness(businessService, formToSave, openMode, record, sessionInfo, request);
			
			//写入日志
			logAction(formToSave.getFormDefine().getApplicationName(), formToSave.getId(), record, sessionInfo, OPEN_MODE_CREATE.equals(openMode) ? "save" : "update", request);
			//保存
			try {
				record = saveRecord(formToSave, record, openMode, request, response, sessionInfo);
				if(record!=null) {
					formToSave.setId(record.getId());
				}
			}
			catch(Exception e) { //数据完整性校验
				validateDataIntegrity(businessService, formToSave, openMode, record, sessionInfo, request);
				throw e;
			}
			//新记录,从临时附件中注销
			if(openMode.equals(OPEN_MODE_CREATE)) {
				((TemporaryFileManageService)getService("temporaryFileManageService")).unregistTemporaryAttachment(formToSave.getFormDefine().getRecordClassName(), formToSave.getId());
			}
			//从会话中删除权限记录
			getRecordControlService().unregistRecordAccessLevel(formToSave.getId(), request.getSession());
			
			//清除验证码
			if(request.getParameter("validateCode")!=null) {
				ValidateCodeService validateCodeService = (ValidateCodeService)getService("validateCodeService");
				validateCodeService.cleanCode(request, response);
			}
			if(!backgroundAction) {
				FormSecurityService formSecurityService = (FormSecurityService)getService("formSecurityService");
				formSecurityService.removeRequest(formToSave.getRequestCode());
				formToSave.setRequestCode(null);
			}
		}
		catch(ValidateException ve) {
			reloadForm(formToSave, record, openMode, acl, sessionInfo, request, response);
			throw ve;
		}
		catch(ExchangeException ve) {
			reloadForm(formToSave, record, openMode, acl, sessionInfo, request, response);
			throw ve;
		}
		//设置刷新父窗口的脚本
		formToSave.setRefeshOpenerScript(generateRefreshOpenerScript(formToSave, record, openMode, "save", actionResult, request, sessionInfo));
		if(reload) { //重新加载
			if(tabSelected!=null) {
				formToSave.setTabSelected(tabSelected);
			}
			if(record!=null) {
				openMode = OPEN_MODE_EDIT;
				formToSave.setAct(openMode);
				formToSave.setId(record.getId()); //更新ID
			}
			record = load(formToSave, request, response);
		}
		else {
			//设置操作结果页面
			setResultPage(formToSave, record, openMode, "save", (actionResult==null ? "保存成功" : actionResult), request, sessionInfo);
			if(!isCreate) {
				unlock(formToSave, request, openMode, sessionInfo);
				formToSave.setLocked(false);
			}
		}
		return record;
	}
	
	/**
	 * 重载表单
	 * @param formToSave
	 * @param record
	 * @param openMode
	 * @param acl
	 * @param sessionInfo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void reloadForm(ActionForm formToSave, Record record, String openMode, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    if(openMode.equals(OPEN_MODE_CREATE)) {
			loadResource(formToSave, record, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, false, openMode, request, sessionInfo); //加载表单资源
			initForm(formToSave, sessionInfo, request, response);
			return;
		}
		boolean deleteEnable = false;
		try {
			checkDeletePrivilege(formToSave, request, record, acl, sessionInfo);
			deleteEnable = true;
		}
		catch(PrivilegeException pe) {
			
		}
		char accessLevel = checkLoadPrivilege(formToSave, request, record, openMode, acl, sessionInfo);
		loadResource(formToSave, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo); //加载表单资源
		if(record!=null) {
			fillForm(formToSave, record, accessLevel, sessionInfo, request, response);
		}
	}
	
	/**
	 * 生成持久层对象
	 * @param form
	 * @param openMode
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected Record generateRecord(ActionForm form, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		String className = form.getFormDefine().getRecordClassName();
		if(className==null) {
			return null;
		}
		Record record;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录,将表单字段填充到持久层对象中
			record = (Record)Class.forName(className).newInstance();
			PropertyUtils.copyProperties(record, form);
			if(record.getId()==0 && RequestUtils.getRequestInfo(request).isClientRequest()) { //客户端请求
				record.setId(UUIDLongGenerator.generateId());
			}
		}
		else { //修改记录,将浏览器提交上来的字段填充到现有的持久层对象中
			record = loadRecord(form, form.getFormDefine(), form.getId(), sessionInfo, request);
			copyRequestParameters(record, form, request);
		}
		return record;
	}
	
	/**
     * struts表单校验,当表单中包含对象类型属性时并引发会出错时,继承并扩展本方法,在前后加上表单备份、表单恢复代码,或者自行校验
     * @param form
     * @param mapping
     * @param request
     * @return
	 * @throws SystemUnregistException
     */
    protected void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, ActionMapping mapping, HttpServletRequest request) throws ValidateException, SystemUnregistException {
    	List errors = validateForm((ActionForm)formToValidate, forceValidateCode, request);
		if(errors!=null && !errors.isEmpty()) {
			((ActionForm)formToValidate).setErrors(errors);
			throw new ValidateException();
		}
    }
    
    /**
     * 业务逻辑校验
     * @param validateService
     * @param form
     * @param openMode
     * @param record
     * @param sessionInfo
     * @param request
     * @return
     * @throws ServiceException
     * @throws SystemUnregistException
     * @throws Exception
     */
	protected void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		//业务逻辑校验
		List errors = validateService==null ? null : validateService.validateBusiness(record, OPEN_MODE_CREATE.equals(openMode) || OPEN_MODE_CREATE_COMPONENT.equals(openMode));
		if(errors!=null && !errors.isEmpty()) {
			((ActionForm)form).setErrors(errors);
			throw new ValidateException();
		}
	}
	
	/**
	 * 数据完整性校验,在数据更新异常时调用
	 * @param validateService
	 * @param form
	 * @param openMode
	 * @param record
	 * @param sessionInfo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected void validateDataIntegrity(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException {
		//数据完整性校验
		List errors = validateService==null ? null : validateService.validateDataIntegrity(record, OPEN_MODE_CREATE.equals(openMode) || OPEN_MODE_CREATE_COMPONENT.equals(openMode));
		if(errors!=null && !errors.isEmpty()) {
			((ActionForm)form).setErrors(errors);
			throw new ValidateException();
		}
	}
	
	/**
	 * 保存记录
	 * @param form
	 * @param record
	 * @param response TODO
	 * @param sessionInfo
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(record==null) {
			return null;
		}
		BusinessService businessService = getBusinessService(form.getFormDefine());
		if(!openMode.equals(OPEN_MODE_CREATE)) { //不是新记录
			return businessService.update(record); //更新记录
		}
		List fields = FieldUtils.listFormFields(form.getFormDefine(), null, "html,components,attachment,image,video", null, null, true, false, false, false, 0);
		for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(request.getParameter(field.getName())!=null) { //提交的数据里面有当前字段
				continue;
			}
			try {
				Object defaultValue = FieldUtils.getFieldDefaultValue(field, false, form.getFormDefine().getApplicationName(), request);
				if(defaultValue!=null) {
					PropertyUtils.setProperty(record, field.getName(), defaultValue);
				}
			}
			catch(Exception e) {
				
			}
		}
		return businessService.save(record);
	}
	
	/**
	 * 删除
	 * @param form
	 * @param request
	 * @param response
	 * @param response
	 * @param actionResult
	 * @param mapping
	 * @return
	 * @throws Exception
	 */
	protected void delete(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		SessionInfo sessionInfo = getSessionInfo(request, response);
		ActionForm formToDelete = (ActionForm)form;
		Form formDefine = loadFormDefine(formToDelete, request);
		LockService recordLockService = getLockService();
		//锁定记录
		recordLockService.lock("" + formToDelete.getId(), sessionInfo.getUserId(), sessionInfo.getUserName());
		Record record = loadRecord(formToDelete, formDefine, formToDelete.getId(), sessionInfo, request);
		//权限控制
		List acl = getAcl(formDefine.getApplicationName(), form, record, OPEN_MODE_EDIT, sessionInfo);
		checkDeletePrivilege(formToDelete, request, record, acl, sessionInfo);
		//计费控制
		checkCharge(formToDelete, request, record, OPEN_MODE_EDIT, acl, sessionInfo);
		//写入日志
		logAction(formDefine.getApplicationName(), formToDelete.getId(), record, sessionInfo, "delete", request);
		try {
			//删除数据
			deleteRecord(formToDelete, formDefine, record, request, sessionInfo);
		}
		catch(ExchangeException exe) {
			reloadForm(formToDelete, record, getOpenMode(formToDelete, request), acl, sessionInfo, request, response);
			throw exe;
		}
		//从会话中删除权限记录
		getRecordControlService().unregistRecordAccessLevel(formToDelete.getId(), request.getSession());
		//解锁
		recordLockService.unlock("" + formToDelete.getId(), sessionInfo.getUserId());
		//设置刷新父窗口的脚本
		formToDelete.setRefeshOpenerScript(generateRefreshOpenerScript(formToDelete, record, OPEN_MODE_EDIT, "delete", actionResult, request, sessionInfo));
		//设置操作结果页面
		setResultPage(formToDelete, null, OPEN_MODE_EDIT, "delete", (actionResult==null ? "删除成功" : actionResult), request, sessionInfo);
		//解锁
		unlock(formToDelete, request, OPEN_MODE_EDIT, sessionInfo);
		//清除请求编码
		FormSecurityService formSecurityService = (FormSecurityService)getService("formSecurityService");
		formSecurityService.removeRequest(formToDelete.getRequestCode());
		formToDelete.setRequestCode(null);
	}
	
	/**
	 * 写入日志
	 * @param formDefine
	 * @param recordId
	 * @param record
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void logAction(String applicationName, long recordId, Record record, SessionInfo sessionInfo, String actionType, HttpServletRequest request) throws Exception {
		if(record==null) {
			return;
		}
		//写入日志
		String content = getRecordDescribe(record);
		action(applicationName, this.getClass(), actionType, record, recordId, content, sessionInfo.getUserId(), sessionInfo.getUserName(), request);
	}
	
	public void action(String applicationName, Class actionClass, String actionType, Object pojo, long recordId, String recordContent, long personId, String personName, HttpServletRequest request) throws Exception {
		if(personId<=0) { //匿名用户不做日志
			return;
		}
		ActionLog actionLog = new ActionLog();
		actionLog.setId(UUIDLongGenerator.generateId());
		actionLog.setApplicationName(applicationName);
		if(pojo!=null) {
			actionLog.setRecordType(pojo.getClass().getName());
		}
		actionLog.setRecordId(recordId);
		actionLog.setContent(recordContent);
		actionLog.setPersonId(personId);
		if(personName!=null && personName.length()>30) {
			personName = personName.substring(0, 30);
		}
		actionLog.setPersonName(personName);
		actionLog.setActionTime(DateTimeUtils.now());
		actionLog.setActionName(actionClass.getName());
		actionLog.setActionType(actionType);
		actionLog.setIp(request.getRemoteAddr() + ":" + request.getRemotePort());
		((DatabaseService)Environment.getService("databaseService")).saveRecord(actionLog);
	}
	
	/**
	 * 获取记录的内容,当记录内容不是subject、title或name时继承并修改本函数
	 * @param record
	 * @return
	 * @throws Exception
	 */
	protected String getRecordDescribe(Record record) throws Exception {
		String[] properties = new String[] {"subject", "title", "name"};
		for(int i=0; i<properties.length; i++) {
			if(PropertyUtils.isReadable(record, properties[i])) {
				return "" + PropertyUtils.getProperty(record, properties[i]);
			}
		}
		return null;
	}
	
	/**
	 * 删除记录
	 * @param form
	 * @param formDefine
	 * @param record
	 * @param request
	 * @param sessionInfo
	 * @param id
	 * @throws Exception
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		getBusinessService(formDefine).delete(record);
	}
	
	/**
	 * 从当前表单继承属性,在新建下一个(createNext)时需要从当前表单集成属性时,继承并扩展本方法
	 * @param newForm
	 * @param currentForm
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		newForm.setDisplayMode(currentForm.getDisplayMode());
	}
	
	/**
	 * 执行附件操作
	 * @param actionForm
	 * @param fieldDefine
	 * @param recordClassName
	 * @param recordId
	 * @param attachmentAction
	 * @param uploadEnabled
	 * @param newRecord
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void doAttachmentAction(ActionForm actionForm, String recordClassName, long recordId, String attachmentAction, boolean uploadEnabled, boolean newRecord, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fieldName = actionForm.getAttachmentSelector().getField();
		if(fieldName==null) {
			fieldName = actionForm.getAttachmentSelector().getType();
		}
		Field field = FieldUtils.getFormField(actionForm.getFormDefine(), fieldName, request);
		if(field==null) {
			return;
		}
		int index = field.getName().lastIndexOf('.');
		if(index!=-1) {
			field.setName(field.getName().substring(index + 1));
		}
		String serviceName = field==null ? "attachmentService" : (String)field.getParameter("serviceName");
		AttachmentService attachmentService = (AttachmentService)getService(serviceName==null || serviceName.equals("") ? field.getType() + "Service" : serviceName);
		if(AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction)) { //创建上传许可
			if(!uploadEnabled) { //没有上传权限
				throw new PrivilegeException();
			}
			//获取附件配置
			int fileLength;
			try {
				fileLength = Integer.parseInt(request.getParameter("fileLength"));
			}
			catch(Exception e) {
				fileLength = 0;
			}
			String passport = attachmentService.createUploadPassport(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), field, recordId, fileLength, request);
			if(newRecord) {
				//新纪录,记录为临时附件
				((TemporaryFileManageService)getService("temporaryFileManageService")).registTemporaryAttachment(recordClassName, actionForm.getFormDefine().getApplicationName(), recordId, getTemporaryAttachmentExpiresHours());
			}
			response.getWriter().write(passport);
			return;
		}
		actionForm.getAttachmentSelector().setUploadDisabled(!uploadEnabled); //设置有没有上传权限
		//设置属性
		actionForm.getAttachmentSelector().setImage("image".equals(field.getType()));
		actionForm.getAttachmentSelector().setVideo("video".equals(field.getType()));
		actionForm.getAttachmentSelector().setTitle(field.getTitle());
		actionForm.getAttachmentSelector().setServiceName((String)field.getParameter("serviceName"));
		actionForm.getAttachmentSelector().setRecordId(recordId); //主记录ID
		actionForm.getAttachmentSelector().setValidatorJs((String)field.getParameter("validatorJs")); //附件校验JS
		actionForm.getAttachmentSelector().setSimpleMode("true".equals(field.getParameter("simpleMode"))); //是否精简模式
		try {
			actionForm.getAttachmentSelector().setMaxUpload(Integer.parseInt(field.getLength())); //最多允许上传的文件数
		}
		catch(Exception e) {
			
		}
		
		//设置文件扩展名
		String fileExtension = (String)field.getParameter("fileExtension");
		if(fileExtension==null) {
			fileExtension = FileUtils.getDefaultFileExtension(actionForm.getAttachmentSelector().isImage(), actionForm.getAttachmentSelector().isVideo());
		}
		actionForm.getAttachmentSelector().setFileExtension(fileExtension);
		//执行附件操作
		if(AttachmentSelector.SELECTOR_ACTION_UPLOAD.equals(attachmentAction)) { //上传
			if(!uploadEnabled) { //没有上传权限
				throw new PrivilegeException();
			}
			//执行上传
			try {
				String path = attachmentService.upload(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), field, recordId, actionForm.getAttachmentSelector().getUpload());
				afterAttachmentUpload(path, actionForm.getAttachmentSelector(), attachmentService, actionForm, recordClassName, recordId, newRecord, request, response);
			}
			catch(Exception e) {
				actionForm.setError("处理上传的文件时出错" + (e.getMessage()==null ? "" : ":" + e.getMessage()));
				throw new ValidateException();
			}
			
			if(newRecord) {
				//新纪录,记录为临时附件
				((TemporaryFileManageService)getService("temporaryFileManageService")).registTemporaryAttachment(recordClassName, actionForm.getFormDefine().getApplicationName(), actionForm.getId(), getTemporaryAttachmentExpiresHours());
			}
        }
		else if(AttachmentSelector.SELECTOR_ACTION_DELETE.equals(attachmentAction)) { //删除
			if(!uploadEnabled) { //没有上传权限
				throw new PrivilegeException();
			}
			//执行删除
        	attachmentService.delete(actionForm.getFormDefine().getApplicationName(), recordId, actionForm.getAttachmentSelector().getType(), actionForm.getAttachmentSelector().getSelectedNames());
        	actionForm.getAttachmentSelector().setSelectedNames(null);
        	actionForm.getAttachmentSelector().setSelectedTitles(null);
        }
		else if(AttachmentSelector.SELECTOR_ACTION_DELETE_ALL.equals(attachmentAction)) { //删除全部
			if(!uploadEnabled) { //没有上传权限
				throw new PrivilegeException();
			}
			//执行删除
			attachmentService.deleteAll(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), recordId);
        	actionForm.getAttachmentSelector().setSelectedNames(null);
        	actionForm.getAttachmentSelector().setSelectedTitles(null);
		}
		else if(AttachmentSelector.SELECTOR_ACTION_PROCESS_UPLOADED_FILES.equals(attachmentAction)) { //处理上传的文件
			if(actionForm.getAttachmentSelector().getLastUploadFiles()!=null) { //最后上传的文件不为空
				String[] files = actionForm.getAttachmentSelector().getLastUploadFiles().split("\\*");
				for(int i=0; i<files.length; i++) {
					try {
						String path = attachmentService.processUploadedFile(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), field, recordId, files[i]);
						afterAttachmentUpload(path, actionForm.getAttachmentSelector(), attachmentService, actionForm, recordClassName, recordId, newRecord, request, response);
					}
					catch(Exception e) {
						actionForm.setError("处理上传的文件时出错" + (e.getMessage()==null ? "" : ":" + e.getMessage()));
						throw new ValidateException();
					}
				}
			}
		}
		//文件上传或者处理上传的文件,设置最后上传的附件
		if(AttachmentSelector.SELECTOR_ACTION_UPLOAD.equals(attachmentAction) || AttachmentSelector.SELECTOR_ACTION_PROCESS_UPLOADED_FILES.equals(attachmentAction)) {
			//获取附件列表
			List attachments = attachmentService.list(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), recordId, 0, request);
	    	if(attachments!=null && !attachments.isEmpty()) {
	    		Attachment lastUploadedAttachment = null;
	    		for(Iterator iterator = attachments.iterator(); iterator.hasNext();) {
	    			Attachment attachment = (Attachment)iterator.next();
	    			if(lastUploadedAttachment==null || attachment.getLastModified()>lastUploadedAttachment.getLastModified()) {
	    				lastUploadedAttachment = attachment;
	    			}
	    		}
	    		actionForm.getAttachmentSelector().setLastUploadedAttachment(lastUploadedAttachment);
	    	}
		}
		//附件对话框
		if("dialog".equals(actionForm.getDisplayMode())) {
			//设置标题
			actionForm.setFormTitle(actionForm.getAttachmentSelector().getTitle() + "选择");
			
			//设置操作
			if(!actionForm.getAttachmentSelector().isUploadDisabled()) {
				actionForm.getFormActions().addFormAction(-1, "上传", "", true);
				actionForm.getFormActions().addFormAction(-1, "删除", "doDelete()", false);
				actionForm.getFormActions().addFormAction(-1, "全部删除", "doDeleteAll()", false);
			}
			actionForm.getFormActions().addFormAction(-1, "确定", "doOK()", actionForm.getAttachmentSelector().isUploadDisabled());
			actionForm.getFormActions().addFormAction(-1, "取消", "closeDialog()", false);
		}
	}
	
	/**
	 * 获取临时附件的过期小时数,超出这个时间,附件自动删除
	 * @return
	 */
	protected int getTemporaryAttachmentExpiresHours() throws Exception {
		return 3; //默认3小时
	}
	
	/**
	 * 获取水印
	 * @param attachmentConfig
	 * @param form
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected WaterMark getWaterMark(ActionForm form, HttpServletRequest request) throws Exception {
		return null;
	}
	
	/**
	 * 文件上传后
	 * @param attachmentPath
	 * @param attachmentSelector
	 * @param attachmentService
	 * @param actionForm
	 * @param recordClassName
	 * @param recordId
	 * @param newRecord
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void afterAttachmentUpload(String attachmentPath, AttachmentSelector attachmentSelector, AttachmentService attachmentService, ActionForm actionForm, String recordClassName, long recordId, boolean newRecord, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(attachmentSelector.isImage()) { //图片
			//获取水印
			WaterMark waterMark = getWaterMark(actionForm, request);
			if(waterMark!=null) {
				((ImageService)attachmentService).addWaterMark(attachmentPath, waterMark); //添加水印
			}
    	}
	}
	
	/**
	 * 获取权限控制列表
	 * @param form
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @return
	 */
	protected List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		return super.getAcl(applicationName, sessionInfo);
	}
	
	/**
	 * 检查加载权限,默认都没有权限
	 * @param form
	 * @param request
	 * @param sessionInfo
	 * @param formDefine
	 * @return 访问级别
	 * @throws SystemUnregistException
	 * @throws ServiceException
	 * @throws Exception
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		throw new PrivilegeException();
	}
	
	/**
	 * 检查保存权限,默认检查用户的加载权限
	 * @param form
	 * @param request
	 * @param sessionInfo
	 * @param formDefine
	 * @return
	 * @throws SystemUnregistException
	 * @throws Exception
	 */
	public void checkSavePrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo)<RecordControlService.ACCESS_LEVEL_EDITABLE) {
			throw new PrivilegeException();
		}
	}
	
	/**
	 * 检查删除权限,默认检查用户的加载权限
	 * @param form
	 * @param request
	 * @param sessionInfo
	 * @param formDefine
	 * @return
	 * @throws SystemUnregistException
	 * @throws Exception
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(checkLoadPrivilege(form, request, record, OPEN_MODE_EDIT, acl, sessionInfo)<RecordControlService.ACCESS_LEVEL_EDITABLE) {
			throw new PrivilegeException();
		}
	}
	
	/**
	 * 检查用户是否已经缴费
	 * @param form
	 * @param request
	 * @param record
	 * @param openMode
	 * @param acl
	 * @param sessionInfo
	 * @throws ChargeException
	 */
	public void checkCharge(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws ChargeException, SystemUnregistException {
		if(serviceItemInCharge==null) {
			return;
		}
		ServiceManage serviceManage = (ServiceManage)getService("serviceManage");
		try {
			if(!serviceManage.isServiceItemPaied(sessionInfo.getUserId(), serviceItemInCharge) && //没有付费
			   !serviceManage.isServiceItemTriable(sessionInfo, serviceItemInCharge)) { //不允许试用
				throw new ChargeException();
			}
		}
		catch (ServiceException e) {
			throw new ChargeException();
		}
	}
	
	/**
	 * 检查附件上传权限,默认和保存权限相同
	 * @param form
	 * @param request
	 * @param record
	 * @param acl
	 * @param sessionInfo
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	public void checkAttachmentUploadPrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			char accessLevel = getRecordControlService().getRegistedRecordAccessLevel(form.getId(), request.getSession());
			if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
				return;
			}
		}
		catch (ServiceException e) {
			
		}
		checkSavePrivilege(form, request, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, acl, sessionInfo);
	}
	
	/**
	 * 检查下载权限,默认和加载权限相同
	 * @param form
	 * @param request
	 * @param record
	 * @param acl
	 * @param sessionInfo
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	public void checkAttachmentDownloadPrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			char accessLevel = getRecordControlService().getRegistedRecordAccessLevel(form.getId(), request.getSession());
			if(accessLevel>=RecordControlService.ACCESS_LEVEL_READONLY) {
				return;
			}
		}
		catch (ServiceException e) {
			
		}
		checkLoadPrivilege(form, request, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, acl, sessionInfo);
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
			char accessLevel = getRecordControlService().getRegistedRecordAccessLevel(form.getId(), request.getSession());
			if(accessLevel>RecordControlService.ACCESS_LEVEL_NONE) {
				return accessLevel;
			}
		}
		catch (ServiceException e) {
			
		}
		return checkLoadPrivilege(form, request, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, acl, sessionInfo);
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
		if(checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo)<RecordControlService.ACCESS_LEVEL_EDITABLE) {
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
		if(form.getAct()==null || form.getAct().isEmpty() || form.getAct().equals(OPEN_MODE_CREATE) || form.getAct().equals(OPEN_MODE_CREATE_COMPONENT)) {
			throw new PrivilegeException();
		}
		checkSaveComponentPrivilege(form, request, record, component, acl, sessionInfo);
	}
	
	/**
	 * 获取服务,服务名称在表单配置中配置,为空时调用baseService
	 * @param formDefine
	 * @return
	 */
	protected BusinessService getBusinessService(Form formDefine) throws SystemUnregistException {
		String serviceName = null;
		if(formDefine==null) {
			serviceName = "businessService";
		}
		else {
			BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
			BusinessObject businessObject = null;
			try {
				businessObject = businessDefineService.getBusinessObject(formDefine.getRecordClassName());
			}
			catch(Exception e) {
				
			}
			serviceName = (businessObject==null || businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
		}
		Object service = getService(serviceName);
		return (service instanceof BusinessService) ? (BusinessService)service : null;
	}
	
	/**
	 * 获取表单打开方式
	 * @param form
	 * @param request
	 * @return
	 */
	protected String getOpenMode(ActionForm form, HttpServletRequest request) {
		if(form.getFormDefine()!=null && form.getFormDefine().getRecordClassName()==null) {
			return OPEN_MODE_CREATE;
		}
		String openMode = form.getAct();
		if(openMode==null || openMode.equals("")) {
			openMode = form.getId()>0 || request.getParameter("id")!=null ? OPEN_MODE_EDIT : OPEN_MODE_CREATE;
		}
		form.setAct(openMode);
		return openMode;
	}
	
	/**
	 * 获取记录加锁服务
	 * @return
	 */
	protected LockService getLockService() throws SystemUnregistException {
		return (LockService)getService("lockService");
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
	protected Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		long id = ((Number)PropertyUtils.getProperty(form, componentName + ".id")).longValue();
		return id==0 ? null : getBusinessService(form.getFormDefine()).load(PropertyUtils.getProperty(form, componentName).getClass(), id);
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
	protected void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
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
				Object defaultValue = FieldUtils.getFieldDefaultValue(field, true, form.getFormDefine().getApplicationName(), request);
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
	protected void fillComponentForm(ActionForm form, Record component, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
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
			copyRequestParameters(form, formClone, request);
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
			subForm = SUBFORM_READ;
			if(record==null || accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
				subForm = SUBFORM_EDIT;
			}
		}
		String formName = (mapping.getInputForward()==null ? null : mapping.getInputForward().getPath());
		if(formName==null || formName.equals("")) {
			formName = request.getRequestURL().toString();
		}
		formName = formName.substring(formName.lastIndexOf('/') + 1, formName.lastIndexOf('.'));
		form.setSubForm(formName + subForm + ".jsp");
		
		//设置操作列表
		setFormActions(form, component, acl, accessLevel, deleteEnable, form.getAct(), subForm, request, sessionInfo);
		
		//设置窗口标题
		setFormTitle(form, component, request, sessionInfo);
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
	protected void reloadComponentForm(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		boolean deleteEnable = false;
		try {
			checkDeleteComponentPrivilege(form, request, record, component, acl, sessionInfo);
			deleteEnable = true;
		}
		catch(PrivilegeException pe) {
			
		}
		loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) { //新记录
			//初始化
			initComponentForm(form, record, componentName, sessionInfo, request);
		}
		else { //组成部分非空
			//填充表单
			component = loadComponentRecord(form, record, componentName, sessionInfo, request);
			fillComponentForm(form, component, record, componentName, sessionInfo, request);
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
	protected void loadComponent(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo = getSessionInfo(request, response);
		ActionForm formToLoad = (ActionForm)form;
		Form formDefine = loadFormDefine(formToLoad, request);
		Record record = loadRecord(formToLoad, formDefine, formToLoad.getId(), sessionInfo, request); //加载记录
		List acl = getAcl(formDefine.getApplicationName(), form, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, sessionInfo);
		//加载组件
		Record component = loadComponentRecord(formToLoad, record, componentName, sessionInfo, request);
		char accessLevel = checkLoadComponentPrivilege(formToLoad, request, record, component, acl, sessionInfo);
		formToLoad.setAct(component==null ? OPEN_MODE_CREATE_COMPONENT : OPEN_MODE_EDIT_COMPONENT); //设置操作
		boolean deleteEnable = false;
		try {
			checkDeleteComponentPrivilege(formToLoad, request, record, component, acl, sessionInfo);
			deleteEnable = true;
		}
		catch(PrivilegeException pe) {
			
		}
		if(record!=null && formToLoad.isDirectOpenComponent()) {
			try {
				lock(formToLoad, record, request, OPEN_MODE_EDIT, sessionInfo);
				formToLoad.setLocked(true);
			}
			catch(LockException le) { //加锁不成功
				deleteEnable = false; //禁止删除
				String lockPersonName = getLockPersonName(formToLoad, record, request, OPEN_MODE_EDIT, sessionInfo);
				if(lockPersonName!=null) {
					formToLoad.setPrompt(lockPersonName + "正在处理当前记录！");
				}
			}
		}
		//加载资源
		loadComponentResource(formToLoad, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		if(component==null) { //组成部分为空
			initComponentForm(formToLoad, record, componentName, sessionInfo, request); //初始化
		}
		else { //组成部分非空
			fillComponentForm(formToLoad, component, record, componentName, sessionInfo, request); //填充表单
		}
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
	protected void saveComponent(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, String foreignKeyProperty, boolean reload, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
        }
		ActionForm formToSave = (ActionForm)form;
		SessionInfo sessionInfo = getSessionInfo(request, response);
		Form formDefine = loadFormDefine(formToSave, request);
		Record record = loadRecord(formToSave, formDefine, formToSave.getId(), sessionInfo, request); //加载主记录
		List acl = getAcl(formDefine.getApplicationName(), form, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, sessionInfo);
		Record component = generateComponentRecord(formToSave, record, componentName, sessionInfo, request, response);
		//设置外键值
		if(foreignKeyProperty!=null) {
			PropertyUtils.setProperty(component, foreignKeyProperty, new Long(formToSave.getId()));
		}
		//检查主记录的保存权限
		checkSaveComponentPrivilege(formToSave, request, record, component, acl, sessionInfo);
		if(record!=null) { //锁定记录
			if(!isLockByMe(formToSave, record, request, OPEN_MODE_EDIT, sessionInfo)) {
				reloadComponentForm(formToSave, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
				throw new LockException();
			}
		}
		//校验
		try {
			validateComponent(formToSave, formToSave.getAct(), record, component, componentName, sessionInfo, request, mapping);
		}
		catch(ValidateException ve) {
			reloadComponentForm(formToSave, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
			throw ve;
		}
		boolean newComponent = OPEN_MODE_CREATE_COMPONENT.equals(formToSave.getAct());
		//保存
		try {
			saveComponentRecord(formToSave, record, component, componentName, foreignKeyProperty, sessionInfo, request);
		}
		catch(Exception e) {
			reloadComponentForm(formToSave, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
			validateDataIntegrity(getBusinessService(formToSave.getFormDefine()), form, formToSave.getAct(), component, sessionInfo, request);
			throw e;
		}
		if(newComponent) { //新记录
			//从临时附件中注销
			((TemporaryFileManageService)getService("temporaryFileManageService")).unregistTemporaryAttachment(component.getClass().getName(), component.getId());
		}
		if(request.getParameter("validateCode")!=null) { //有验证码
			//清除验证码
			ValidateCodeService validateCodeService = (ValidateCodeService)getService("validateCodeService");
			validateCodeService.cleanCode(request, response);
		}
		if(reload) {
			formToSave.setAct(OPEN_MODE_EDIT_COMPONENT);
			reloadComponentForm(formToSave, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
		}
	}
	
	/**
	 * 新建下一个时,继承原来输入的信息
	 * @param newForm
	 * @param oldForm
	 * @throws Exception
	 */
	protected void inheritComponentProperties(ActionForm newForm, ActionForm oldForm) throws Exception {
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
	 * @param mapping
	 * @throws ValidateException
	 * @throws SystemUnregistException
	 */
	protected void validateComponent(ActionForm form, String openMode, Record mainRecord, Record component, String componentName, SessionInfo sessionInfo, HttpServletRequest request, ActionMapping mapping) throws ValidateException, SystemUnregistException {
		//表单校验
		validateForm(form, mainRecord, form.getAct(), sessionInfo, mapping, request);
		//业务逻辑校验
		BusinessService businessService = getBusinessService(form.getFormDefine());
		try {
			validateBusiness(businessService, form, openMode, component, sessionInfo, request);
		}
		catch (ServiceException e) {
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
	protected Record generateComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Record component = (Record)PropertyUtils.getProperty(form, componentName);
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) { //新记录
			return component;
		}
		component = loadComponentRecord(form, mainRecord, componentName, sessionInfo, request);
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
		BusinessService businessService = getBusinessService(form.getFormDefine());
		if(!OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) { //修改记录
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
					Object defaultValue = FieldUtils.getFieldDefaultValue(field, true, form.getFormDefine().getApplicationName(), request);
					if(defaultValue!=null) {
						PropertyUtils.setProperty(component, field.getName().substring(componentName.length() + 1), defaultValue);
					}
				}
				catch(Exception e) {
					
				}
			}
			businessService.save(component);
			form.setAct(OPEN_MODE_EDIT_COMPONENT);
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
	protected void deleteComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		BusinessService businessService = getBusinessService(form.getFormDefine());
		businessService.delete((Record)PropertyUtils.getProperty(form, componentName));
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
	protected void refreshMainRecord(ActionForm form, String refreshActionName, String tabSelected, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String html = "<html>" +
					  " <head>" +
					  "  <script language=\"JavaScript\" charset=\"utf-8\" src=\"" + request.getContextPath() + "/jeaf/common/js/common.js\"></script>" +
					  "  <script>" +
					  "  function refreshMainRecord() {";
		if(!form.isDirectOpenComponent()) {
			html += (refreshActionName==null ? "" : "    getDialogOpener().setTimeout(\"doAction('" + refreshActionName + "', 'tabSelected=" + tabSelected + "')\", 1);");
		}
		else {
			html += "if(getDialogOpener().document.getElementsByName('viewPackage.recordCount').length>0) {" + //视图
				    "		getDialogOpener().setTimeout(\"refreshView('viewPackage');\", 1);" +
				    "}";
		}
		html += 	  "    closeDialog();" +
					  "  }" +
					  "  </script>" +
					  " </head>" +
					  " <body onload=\"refreshMainRecord()\"></body>" +
					  "</html>";
		response.getWriter().write(html);
	}
	
	/**
	 * 处理异常
	 * @param e
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward transactException(Exception e, ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean resumeAction) throws Exception {
		if(e instanceof SessionException) { //会话异常
			return redirectToLogin(this, mapping, form, request, response, e, resumeAction);
		}
		else if(e instanceof PrivilegeException) { //权限异常
			return redirectToLogin(this, mapping, form, request, response, e, resumeAction);
		}
		else if(e instanceof ValidateException) { //校验异常
			ActionForm actionForm = (ActionForm)form;
			actionForm.setError(e.getMessage());
			return mapping.getInputForward();
		}
		else if(e instanceof ExchangeException) { //数据交换异常
			ActionForm actionForm = (ActionForm)form;
			if(actionForm.getPrompt()==null) {
				actionForm.setPrompt("数据交换失败,请联系管理员检查服务器是否正常！");
			}
			return mapping.getInputForward();
		}
		else if(e instanceof LockException) { //锁定异常
			ActionForm actionForm = (ActionForm)form;
			if(actionForm.getPrompt()==null) {
				actionForm.setPrompt("不能锁定当前记录,操作不能完成！");
			}
			return mapping.getInputForward();
		}
		else if(e instanceof ChargeException) { //计费异常
			if(SessionService.ANONYMOUS.equals(getSessionInfo(request, response).getLoginName())) { //尚未登录
				return redirectToLogin(this, mapping, form, request, response, e, resumeAction);
			}
			return redirectToChargePrompt(this, mapping, form, request, response);
		}
		else if(e instanceof SystemUnregistException) {
        	return redirectToRegist(request, response);
        }
		throw e;
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
		if(isSecureAction && !isSecureURL(request)){
        	redirectToSecureLink(request, response);
        	return null;
        }
		try {
    		load(form, request, response);
    	}
		catch(Exception e) {
			ActionForward forward = transactException(e, mapping, form, request, response, false);
			if(forward!=null && forward.getPath()==null) {
				forward = mapping.findForward("load");
			}
			return forward;
        }
		ActionForward forward = mapping.findForward("load");
		return forward==null ? mapping.getInputForward() : forward;
	}
	
	/**
	 * 执行保存操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param reload
	 * @param tabSelected
	 * @param forwardName
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeSaveAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String tabSelected, String actionResult, String forwardName) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
		}
		ActionForm actionForm = (ActionForm)form;
        if(tabSelected!=null) {
        	actionForm.setTabSelected(tabSelected); //选中的TAB
        }
		try {
        	save(mapping, form, request, response, reload, tabSelected, actionResult);
    	}
		catch(Exception e) {
			if((e instanceof PrivilegeException) && reload) { //没有权限、需要重新加载、并且是刷新页面
				load(form, request, response);
			}
			else {
				return transactException(e, mapping, form, request, response, true);
			}
        }
    	return reload ? mapping.getInputForward() : mapping.findForward(forwardName==null ? "result" : forwardName);
	}
	
	/**
	 * 执行删除操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param forwardName
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeDeleteAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult, String forwardName) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
		}
		try {
    		delete(form, request, response, actionResult);
    	}
    	catch(Exception e) {
    		return transactException(e, mapping, form, request, response, true);
        }
    	return mapping.findForward(forwardName==null ? "result" : forwardName);
    }
	
	/**
	 * 执行新建下一个操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeCreateNextAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
		}
		try {
			save(mapping, form, request, response, false, null, null);
			ActionForm newForm = (ActionForm)form.getClass().newInstance();
			ActionForm oldForm = (ActionForm)form;
			inheritProperties(newForm, oldForm);
			newForm.setRefeshOpenerScript(oldForm.getRefeshOpenerScript());
			PropertyUtils.copyProperties(oldForm, newForm);
			oldForm.setAct(OPEN_MODE_CREATE);
			load(oldForm, request, response);
		}
		catch(Exception e) {
    		return transactException(e, mapping, form, request, response, true);
        }
		return mapping.getInputForward();
	}
	
	/**
	 * 附件操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param isImage
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeAttachmentAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
		}
		ActionForm actionForm = (ActionForm)form;
		String attachmentAction = actionForm.getAttachmentSelector().getAction();
		try {
			//获取会话
			SessionInfo sessionInfo = getSessionInfo(request, response);
			//加载主记录
			Form formDefine = loadFormDefine(actionForm, request);
			Record record = loadRecord(actionForm, formDefine, actionForm.getId(), sessionInfo, request); //加载记录
			List acl = getAcl(formDefine.getApplicationName(), form, record, (record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT), sessionInfo);
			//检查上传权限
			boolean uploadEnabled = false;
			try {
				checkAttachmentUploadPrivilege(actionForm, request, record, acl, sessionInfo);
				uploadEnabled = true;
			}
			catch(PrivilegeException e) {
				
			}
			if(attachmentAction==null || "".equals(attachmentAction) || AttachmentSelector.SELECTOR_ACTION_LOAD.equals(attachmentAction)) { //加载
				//检查附件下载权限
				checkAttachmentDownloadPrivilege(actionForm, request, record, acl, sessionInfo);
			}
			doAttachmentAction(actionForm, actionForm.getFormDefine().getRecordClassName(), actionForm.getId(), attachmentAction, uploadEnabled, (record==null), request, response);
		}
		catch(Exception e) {
			if(AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction)) { //创建上传许可证
				if(e instanceof SessionException) {
					response.getWriter().write("SESSION_EXCEPTION");
		    	}
				else if(e instanceof PrivilegeException) {
		    		response.getWriter().write("NO_PRIVILEGE");
		    	}
				else if(e instanceof LockException) {
		        	response.getWriter().write("LOCK_FAILED");
		        }
				return null;
			}
			else if(AttachmentSelector.SELECTOR_ACTION_PROCESS_UPLOADED_FILES.equals(attachmentAction) || //控件方式:处理上传完成后的文件
					AttachmentSelector.SELECTOR_ACTION_UPLOAD.equals(attachmentAction)) { //非控件方式上传文件
				if(e instanceof SessionException) { //打开登录对话框,重新登录
					actionForm.setError("会话无效");
				}
				else {
					actionForm.setError(e.getMessage());
				}
			}
			else {
				return transactException(e, mapping, form, request, response, false);
			}
	    }
		return (AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction) ? null : mapping.findForward("load"));
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
		if(isSecureAction && !isSecureURL(request)){
        	redirectToSecureLink(request, response);
        	return null;
        }
		try {
			loadComponent(mapping, form, componentName, request, response);
    	}
		catch(Exception e) {
    		return transactException(e, mapping, form, request, response, false);
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
			saveComponent(mapping, form, componentName, foreignKeyProperty, reload, request, response);
			if(reload) {
				return mapping.getInputForward();
			}
			//更新主记录页面
			refreshMainRecord((ActionForm)form, refreshActionName, tabSelected, request, response);
		}
		catch(Exception e) {
			return transactException(e, mapping, form, request, response, true);
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
			saveComponent(mapping, form, componentName, foreignKeyProperty, false, request, response);
			ActionForm oldForm = (ActionForm)form.getClass().newInstance();
			Object component = PropertyUtils.getProperty(oldForm, componentName);
			ActionForm newForm = (ActionForm)form;
			PropertyUtils.copyProperties(oldForm, newForm);
			PropertyUtils.setProperty(newForm, componentName, component);
			inheritComponentProperties(newForm, oldForm);
			newForm.setAct(OPEN_MODE_CREATE_COMPONENT);
			loadComponent(mapping, newForm, componentName, request, response);
		}
		catch(Exception e) {
			return transactException(e, mapping, form, request, response, true);
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
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
        }
		ActionForm formToDelete = (ActionForm)form;
		try {
			SessionInfo sessionInfo = getSessionInfo(request, response);
			Form formDefine = loadFormDefine(formToDelete, request);
			//加载主记录
			Record record = loadRecord(formToDelete, formDefine, formToDelete.getId(), sessionInfo, request);
			List acl = getAcl(formDefine.getApplicationName(), form, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, sessionInfo);
			//获取组成部分
			Record component = loadComponentRecord(formToDelete, record, componentName, sessionInfo, request);
			//检查记录的删除权限
			checkDeleteComponentPrivilege(formToDelete, request, record, component, acl, sessionInfo);
			if(record!=null) { //锁定记录
				if(!isLockByMe(formToDelete, record, request, OPEN_MODE_EDIT, sessionInfo)) {
					throw new LockException();
				}
			}
			//开始删除
			try {
				deleteComponentRecord(formToDelete, record, componentName, sessionInfo, request);
			}
			catch(ExchangeException exe) {
				reloadComponentForm(formToDelete, mapping, record, component, componentName, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, request, sessionInfo);
				throw exe;
			}
			//更新主记录页面
			refreshMainRecord(formToDelete, refreshActionName, tabSelected, request, response);
		}
    	catch(Exception e) {
    		return transactException(e, mapping, form, request, response, true);
        }
        return null;
	}
	
	/**
	 * 组成部分附件操作
	 * @param mapping
	 * @param form
	 * @param componentName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeComponentAttachmentAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
		}
		ActionForm componentForm = (ActionForm)form;
		String attachmentAction = componentForm.getAttachmentSelector().getAction();
		try {
			SessionInfo sessionInfo = getSessionInfo(request, response);
			Form formDefine = loadFormDefine(componentForm, request);
			Record record = loadRecord(componentForm, formDefine, componentForm.getId(), sessionInfo, request); //加载主记录
			List acl = getAcl(formDefine.getApplicationName(), form, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, sessionInfo);
			Record component = loadComponentRecord(componentForm, record, componentName, sessionInfo, request);
			//检查保存权限(上传权限)
			boolean uploadEnabled = false;
			try {
				checkSaveComponentPrivilege(componentForm, request, record, component, acl, sessionInfo);
				uploadEnabled = true;
			}
			catch(PrivilegeException e) {
				
			}
			if(attachmentAction==null || "".equals(attachmentAction) || AttachmentSelector.SELECTOR_ACTION_LOAD.equals(attachmentAction)) { //加载
				//检查加载权限(附件下载权限)
				checkLoadComponentPrivilege(componentForm, request, record, component, acl, sessionInfo);
			}
			Class ccomponentClass = PropertyUtils.getProperty(form, componentName).getClass();
			long componentId = ((Number)PropertyUtils.getProperty(form, componentName + ".id")).longValue();
			doAttachmentAction(componentForm, ccomponentClass.getName(), componentId, attachmentAction, uploadEnabled, component==null, request, response);
		}
		catch(Exception e) {
			if(!AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction)) {
				return transactException(e, mapping, form, request, response, false);
			}
			else if(e instanceof SessionException) {
				response.getWriter().write("SESSION_EXCEPTION");
	    	}
			else if(e instanceof PrivilegeException) {
	    		response.getWriter().write("NO_PRIVILEGE");
	    	}
			else if(e instanceof LockException) {
	        	response.getWriter().write("LOCK_FAILED");
	        }
        }
		if(AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction)) {
			return null;
		}
        return mapping.findForward("load");
	}
}
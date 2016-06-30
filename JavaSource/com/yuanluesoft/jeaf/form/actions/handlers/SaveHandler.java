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
import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 
 * @author chuan
 *
 */
public class SaveHandler {
	private FormAction formAction;
	
	public SaveHandler(FormAction formAction) {
		this.formAction = formAction;
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
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			throw new Exception();
		}
		ActionForm actionForm = (ActionForm)form;
        if(tabSelected!=null) {
        	actionForm.setTabSelected(tabSelected); //选中的TAB
        }
		try {
			formAction.save(mapping, form, request, response, reload, tabSelected, actionResult);
    	}
		catch(Exception e) {
			if((e instanceof PrivilegeException) && reload) { //没有权限、需要重新加载、并且是刷新页面
				formAction.load(form, request, response);
			}
			else {
				return formAction.transactException(e, mapping, form, request, response, true);
			}
        }
		if(reload) {
			request.setAttribute("reload", Boolean.TRUE);
			actionForm.setReloadPageURL(formAction.generateReloadURL(actionForm, request));
		}
		return reload ? mapping.getInputForward() : mapping.findForward(forwardName==null ? "result" : forwardName);
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
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			throw new Exception();
		}
		try {
			formAction.save(mapping, form, request, response, false, null, null);
			ActionForm oldForm = (ActionForm)form;
			if(oldForm.isInternalForm() && "dialog".equals(oldForm.getDisplayMode())) {
				request.getSession().setAttribute("internalForm", oldForm);
				request.setAttribute("reload", Boolean.TRUE);
				oldForm.setReloadPageURL(oldForm.getReloadPageURL().substring(0, oldForm.getReloadPageURL().lastIndexOf('?') + 1) + StringUtils.removeQueryParameters(oldForm.getQueryString(), "seq") + "&seq=" + System.currentTimeMillis());
				return mapping.getInputForward();
			}
			ActionForm newForm = (ActionForm)form.getClass().newInstance();
			formAction.inheritProperties(newForm, oldForm);
			newForm.setRefeshOpenerScript(oldForm.getRefeshOpenerScript());
			PropertyUtils.copyProperties(oldForm, newForm);
			oldForm.setAct(FormAction.OPEN_MODE_CREATE);
			formAction.load(oldForm, request, response);
		}
		catch(Exception e) {
    		return formAction.transactException(e, mapping, form, request, response, true);
        }
		return mapping.getInputForward();
	}
	
	/**
	 * 保存
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
		SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
		ActionForm formToSave = (ActionForm)form;
		Form formDefine = formAction.loadFormDefine(formToSave, request);
		String openMode = formAction.getOpenMode(formToSave, request);
		boolean isCreate = openMode.equals(FormAction.OPEN_MODE_CREATE);
		//生成持久层对象
		Record record = formAction.generateRecord(formToSave, openMode, request, sessionInfo);
		//权限控制
		List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, openMode, sessionInfo);
		formAction.checkSavePrivilege(formToSave, request, record, openMode, acl, sessionInfo);
		//计费控制
		formAction.checkCharge(formToSave, request, record, openMode, acl, sessionInfo);
		if(!isCreate) {
			if(!formAction.isLockByMe(formToSave, record, request, openMode, sessionInfo)) {
				//未锁定记录
				formAction.reloadForm(formToSave, record, openMode, acl, sessionInfo, request, response);
				throw new LockException();
			}
		}
		try {
			//表单校验
			formAction.validateForm(formToSave, record, openMode, sessionInfo, request);
			//业务逻辑校验
			BusinessService businessService = formAction.getBusinessService(formDefine);
			formAction.validateBusiness(businessService, formToSave, openMode, record, sessionInfo, request);
			//写入日志
			formAction.logAction(formToSave.getFormDefine().getApplicationName(), formToSave.getId(), record, sessionInfo, FormAction.OPEN_MODE_CREATE.equals(openMode) ? "save" : "update", request);
			//保存
			try {
				record = formAction.saveRecord(formToSave, record, openMode, request, response, sessionInfo);
				if(record!=null) {
					formToSave.setId(record.getId());
				}
			}
			catch(Exception e) { //数据完整性校验
				formAction.validateDataIntegrity(businessService, formToSave, openMode, record, sessionInfo, request);
				throw e;
			}
			//新记录,从临时附件中注销
			if(openMode.equals(FormAction.OPEN_MODE_CREATE)) {
				((TemporaryFileManageService)formAction.getService("temporaryFileManageService")).unregistTemporaryAttachment(formToSave.getFormDefine().getRecordClassName(), formToSave.getId());
			}
			//从会话中删除权限记录
			formAction.getRecordControlService().unregistRecordAccessLevel(formToSave.getId(), request.getSession());
			//清除验证码
			if(request.getParameter("validateCode")!=null) {
				ValidateCodeService validateCodeService = (ValidateCodeService)formAction.getService("validateCodeService");
				validateCodeService.cleanCode(request, response);
			}
			if(!formAction.backgroundAction) {
				FormSecurityService formSecurityService = (FormSecurityService)formAction.getService("formSecurityService");
				formSecurityService.removeRequest(formToSave.getRequestCode());
				formToSave.setRequestCode(null);
			}
		}
		catch(ValidateException ve) {
			formAction.reloadForm(formToSave, record, openMode, acl, sessionInfo, request, response);
			throw ve;
		}
		catch(ExchangeException ve) {
			formAction.reloadForm(formToSave, record, openMode, acl, sessionInfo, request, response);
			throw ve;
		}
		if(record!=null) {
			formToSave.setAct(FormAction.OPEN_MODE_EDIT);
			formToSave.setId(record.getId()); //更新ID
		}
		//设置刷新父窗口的脚本
		formToSave.setRefeshOpenerScript(formAction.generateRefreshOpenerScript(formToSave, record, openMode, "save", actionResult, request, sessionInfo));
		//设置重新打开页面的URL
		formToSave.setReloadPageURL(record==null ? "javascript:window.close();" : formAction.generateReloadURL(formToSave, request));
		//重新加载
		if(reload) {
			if(tabSelected!=null) {
				formToSave.setTabSelected(tabSelected);
			}
			record = formAction.load(formToSave, request, response);
		}
		else {
			//设置操作结果页面
			formAction.setResultPage(formToSave, record, openMode, "save", (actionResult==null ? "保存成功" : actionResult), request, sessionInfo);
			if(!isCreate) {
				formAction.unlock(formToSave, request, openMode, sessionInfo);
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
	public void reloadForm(ActionForm formToSave, Record record, String openMode, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(formToSave.isInternalForm() && "dialog".equals(formToSave.getDisplayMode())) {
			return;
		}
		if(openMode.equals(FormAction.OPEN_MODE_CREATE)) {
	    	formAction.loadResource(formToSave, record, acl, RecordControlService.ACCESS_LEVEL_EDITABLE, false, openMode, request, sessionInfo); //加载表单资源
	    	formAction.initForm(formToSave, acl, sessionInfo, request, response);
			return;
		}
		boolean deleteEnable = false;
		try {
			formAction.checkDeletePrivilege(formToSave, request, record, acl, sessionInfo);
			deleteEnable = true;
		}
		catch(PrivilegeException pe) {
			
		}
		char accessLevel = formAction.checkLoadPrivilege(formToSave, request, record, openMode, acl, sessionInfo);
		formAction.loadResource(formToSave, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo); //加载表单资源
		if(record!=null) {
			formAction.fillForm(formToSave, record, accessLevel, acl, sessionInfo, request, response);
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
	public Record generateRecord(ActionForm form, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		String className = form.getFormDefine().getRecordClassName();
		if(className==null) {
			return null;
		}
		Record record;
		if(FormAction.OPEN_MODE_CREATE.equals(openMode)) { //新记录,将表单字段填充到持久层对象中
			record = (Record)Class.forName(className).newInstance();
			PropertyUtils.copyProperties(record, form);
			if(record.getId()==0 && RequestUtils.getRequestInfo(request).isClientRequest()) { //客户端请求
				record.setId(UUIDLongGenerator.generateId());
			}
		}
		else { //修改记录,将浏览器提交上来的字段填充到现有的持久层对象中
			record = formAction.loadRecord(form, form.getFormDefine(), form.getId(), sessionInfo, request);
			formAction.copyRequestParameters(record, form, request);
		}
		return record;
	}
	
	/**
	 * struts表单校验,当表单中包含对象类型属性时并引发会出错时,继承并扩展本方法,在前后加上表单备份、表单恢复代码,或者自行校验
	 * @param formToValidate
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @param request
	 * @throws ValidateException
	 * @throws SystemUnregistException
	 */
    public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
    	List errors = formAction.validateForm((ActionForm)formToValidate, formAction.forceValidateCode, request);
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
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		//业务逻辑校验
		List errors = validateService==null ? null : validateService.validateBusiness(record, FormAction.OPEN_MODE_CREATE.equals(openMode) || FormAction.OPEN_MODE_CREATE_COMPONENT.equals(openMode));
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
	public void validateDataIntegrity(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException {
		//数据完整性校验
		List errors = validateService==null ? null : validateService.validateDataIntegrity(record, FormAction.OPEN_MODE_CREATE.equals(openMode) || FormAction.OPEN_MODE_CREATE_COMPONENT.equals(openMode));
		if(errors!=null && !errors.isEmpty()) {
			((ActionForm)form).setErrors(errors);
			throw new ValidateException();
		}
	}
	
	/**
	 * 保存记录
	 * @param form
	 * @param record
	 * @param response
	 * @param sessionInfo
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(record==null) {
			return null;
		}
		BusinessService businessService = formAction.getBusinessService(form.getFormDefine());
		if(!openMode.equals(FormAction.OPEN_MODE_CREATE)) { //不是新记录
			return businessService.update(record); //更新记录
		}
		List fields = FieldUtils.listFormFields(form.getFormDefine(), null, "html,components,attachment,image,video", null, null, true, false, false, false, 0);
		for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(request.getParameter(field.getName())!=null) { //提交的数据里面有当前字段
				continue;
			}
			try {
				Object defaultValue = FieldUtils.getFieldDefaultValue(field, false, form.getFormDefine().getApplicationName(), form, request);
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
	 * 把页面提交上来的参数值写入目标对象
	 * @param to
	 * @param form
	 * @param request
	 * @throws Exception
	 */
	public void copyRequestParameters(Object to, ActionForm form, HttpServletRequest request) throws Exception {
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
	 * 设置操作结果页面
	 * @param form
	 * @param record
	 * @param action save/delete/run
	 * @param request
	 * @param sessionInfo
	 */
	public void setResultPage(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		//设置操作结果
        if(form.getActionResult()==null) {
        	form.setActionResult(actionResult);
        }
		//设置按钮列表
        form.getFormActions().clear();
        if(record!=null) {
        	formAction.addReloadAction(form, "返回", request, -1, true);
        }
        form.getFormActions().addFormAction(-1, "关闭", (!form.isInternalForm() && "dialog".equals(form.getDisplayMode()) ? "DialogUtils.closeDialog();" : "top.window.close();"), record==null);
	}
	
	/**
	 * 给内置对话框添加“取消”运行按钮
	 * @param form
	 * @param actionTitle
	 * @param request
	 */
	public void addReloadAction(ActionForm form, String actionTitle, HttpServletRequest request, int actionIndex, boolean firstAction) {
	   	//添加“取消”操作
    	String script = (form.isInternalForm() && "dialog".equals(form.getDisplayMode()) ? "DialogUtils.closeDialog();" : "window.location = '" + formAction.generateReloadURL(form, request) + "';");
    	form.getFormActions().addFormAction(actionIndex, actionTitle==null ? "取消" : actionTitle, script, firstAction);
 	}
	
	/**
	 * 生成重新加载记录的URL
	 * @param form
	 * @param request
	 * @return
	 */
	public String generateReloadURL(ActionForm form, HttpServletRequest request) {
		if(FormAction.OPEN_MODE_CREATE.equals(form.getAct())) {
			return null;
		}
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		return request.getContextPath() + "/" + form.getFormDefine().getApplicationName() + "/" + form.getFormDefine().getName() + ".shtml" +
			   "?act=edit" +
			   "&id=" + form.getId() +
			   (form.isInternalForm() || form.getDisplayMode()==null || form.getDisplayMode().isEmpty() ? "" : "&displayMode=" + form.getDisplayMode()) +
			   (form.getTabSelected()==null || form.getTabSelected().isEmpty() ? "" : "&tabSelected=" + form.getTabSelected()) +
			   (siteId==0 ? "" : "&siteId=" + request.getParameter("siteId")) +
			   "&req=" + System.currentTimeMillis();
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
	public String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		return "try {var win=" + ("dialog".equals(form.getDisplayMode()) && !form.isInternalForm() ? "DialogUtils.getDialogOpener()" : "top.opener") + ";if(win)win.setTimeout(\"" +
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
}
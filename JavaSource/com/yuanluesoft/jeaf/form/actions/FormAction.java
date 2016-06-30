/*
 * Created on 2004-12-21
 *
 */
package com.yuanluesoft.jeaf.form.actions;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.charge.servicemanage.service.ServiceManage;
import com.yuanluesoft.exchange.client.exception.ExchangeException;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ChargeException;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.handlers.AttachmentHandler;
import com.yuanluesoft.jeaf.form.actions.handlers.ComponentHandler;
import com.yuanluesoft.jeaf.form.actions.handlers.DeleteHandler;
import com.yuanluesoft.jeaf.form.actions.handlers.DocumentHandler;
import com.yuanluesoft.jeaf.form.actions.handlers.LoadHandler;
import com.yuanluesoft.jeaf.form.actions.handlers.SaveHandler;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.lock.service.LockService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 *
 * @author linchuan
 *
 */
public class FormAction extends BaseAction {
	public boolean isSecureAction = false; //是否强制安全的URL
	public boolean forceValidateCode = false; //是否强制验证码校验
	public boolean mainRecordAction = true; //是否主记录的操作
	public String serviceItemInCharge = null; //应用在计费系统中的服务项名称,null表示不做计费检查
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
	 * @throws Exception
	 */
	public Record load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new LoadHandler(this).load(form, request, response);
	}
	
	/**
	 * 为匿名页面设置请求代码,防止恶意提交
	 * @param formToLoad
	 * @throws Exception
	 */
	public void setRequestCode(ActionForm formToLoad) throws Exception {
		new LoadHandler(this).setRequestCode(formToLoad);
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
		new LoadHandler(this).loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
	}
	
	/**
	 * 初始化表单,当应用需要初始化表单字段值时,继承并扩展本方法
	 * @param form
	 * @param acl
	 * @param request
	 * @param response
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		new LoadHandler(this).initForm(form, acl, sessionInfo, request, response);
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
		new LoadHandler(this).fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
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
		return new LoadHandler(this).loadRecord(form, formDefine, id, sessionInfo, request);
	}
	
	/**
	 * 加锁记录
	 * @param form
	 * @param request
	 * @param openMode
	 * @return
	 * @throws SystemUnregistException
	 */
	public void lock(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		if(!sessionInfo.isAnonymous()) {
			((LockService)getService("lockService")).lock("" + form.getId(), sessionInfo.getUserId(), sessionInfo.getUserName());
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
	public void unlock(ActionForm form, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		if(!sessionInfo.isAnonymous()) {
			((LockService)getService("lockService")).unlock("" + form.getId(), sessionInfo.getUserId());
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
	public String getLockPersonName(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return ((LockService)getService("lockService")).getLockPersonName("" + form.getId());
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
	public boolean isLockByMe(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return sessionInfo.isAnonymous() || ((LockService)getService("lockService")).isLockByPerson("" + form.getId(), sessionInfo.getUserId());
	}
	
	/**
	 * 生成解锁URL
	 * @param form
	 * @param record
	 * @param openMode
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	public String generateUnlockUrl(ActionForm form, Record record, HttpServletRequest request, SessionInfo sessionInfo) {
		return request.getContextPath() + "/jeaf/lock/unlock.shtml?lockTarget=" + form.getId() + "&personId=" + sessionInfo.getUserId();
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
		new SaveHandler(this).setResultPage(form, record, openMode, currentAction, actionResult, request, sessionInfo);
	}
	
	/**
	 * 给内置对话框添加“取消”运行按钮
	 * @param form
	 * @param actionTitle
	 * @param request
	 */
	public void addReloadAction(ActionForm form, String actionTitle, HttpServletRequest request, int actionIndex, boolean firstAction) {
		new SaveHandler(this).addReloadAction(form, actionTitle, request, actionIndex, firstAction);
 	}
	
	/**
	 * 生成重新加载记录的URL
	 * @param form
	 * @param request
	 * @return
	 */
	public String generateReloadURL(ActionForm form, HttpServletRequest request) {
		return new SaveHandler(this).generateReloadURL(form, request);
	}
	
	/**
	 * 设置窗口标题
	 * @param form
	 * @param record
	 * @param request
	 * @param sessionInfo
	 */
	public void setFormTitle(ActionForm form, Record record, HttpServletRequest request, SessionInfo sessionInfo) {
		new LoadHandler(this).setFormTitle(form, record, request, sessionInfo);
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
		new LoadHandler(this).setFormActions(form, record, acl, accessLevel, deleteEnable, openMode, subForm, request, sessionInfo);
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
		return new SaveHandler(this).generateRefreshOpenerScript(form, record, openMode, currentAction, actionResult, request, sessionInfo);
	}
	
	/**
	 * 把页面提交上来的参数值写入目标对象
	 * @param to
	 * @param form
	 * @param request
	 * @throws Exception
	 */
	public void copyRequestParameters(Object to, ActionForm form, HttpServletRequest request) throws Exception {
		new SaveHandler(this).copyRequestParameters(to, form, request);
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
		return new SaveHandler(this).save(mapping, form, request, response, reload, tabSelected, actionResult);
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
	    new SaveHandler(this).reloadForm(formToSave, record, openMode, acl, sessionInfo, request, response);
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
		return new SaveHandler(this).generateRecord(form, openMode, request, sessionInfo);
	}
	
	/**
     * struts表单校验,当表单中包含对象类型属性时并引发会出错时,继承并扩展本方法,在前后加上表单备份、表单恢复代码,或者自行校验
	 * @param request
	 * @param form
     * @return
	 * @throws SystemUnregistException
     */
    public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
    	new SaveHandler(this).validateForm(formToValidate, record, openMode, sessionInfo, request);
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
		new SaveHandler(this).validateBusiness(validateService, form, openMode, record, sessionInfo, request);
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
		new SaveHandler(this).validateDataIntegrity(validateService, form, openMode, record, sessionInfo, request);
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
		return new SaveHandler(this).saveRecord(form, record, openMode, request, response, sessionInfo);
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
	public void delete(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		new DeleteHandler(this).delete(form, request, response, actionResult);
	}
	
	/**
	 * 写入日志
	 * @param applicationName
	 * @param recordId
	 * @param record
	 * @param sessionInfo
	 * @param actionType
	 * @param request
	 * @throws Exception
	 */
	public void logAction(String applicationName, long recordId, Record record, SessionInfo sessionInfo, String actionType, HttpServletRequest request) throws Exception {
		if(record==null) {
			return;
		}
		String content = StringUtils.getBeanTitle(record); //获取记录的内容,当记录内容不是subject、title或name时继承并修改本函数
		Logger.action(applicationName, this.getClass(), actionType, record, recordId, content, sessionInfo.getUserId(), sessionInfo.getUserName(), request);
	}
	
	/**
	 * 删除记录
	 * @param form
	 * @param formDefine
	 * @param record
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @param id
	 * @throws Exception
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
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
	 * 输出远程文档
	 * @param documentApplicationName
	 * @param documentCommand
	 * @param documentCommandParameter
	 * @param documentRelatedApplicationName
	 * @param documentRelatedFormName
	 * @param documentFiles
	 * @param form
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws Exception
	 */
	public void writeRemoteDocument(String documentApplicationName, String documentCommand, Map documentCommandParameter, String documentRelatedApplicationName, String documentRelatedFormName, List documentFiles, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		new DocumentHandler(this).writeRemoteDocument(documentApplicationName, documentCommand, documentCommandParameter, documentRelatedApplicationName, documentRelatedFormName, documentFiles, form, request, response, sessionInfo);
	}
	
	/**
	 * 创建附件上传许可
	 * @param actionForm
	 * @param field
	 * @param attachmentService
	 * @param fileLength
	 * @param recordClassName
	 * @param recordId
	 * @param uploadEnabled
	 * @param newRecord
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String createAttachmentUploadPassport(ActionForm actionForm, Field field, AttachmentService attachmentService, int fileLength, String recordClassName, long recordId, boolean uploadEnabled, boolean newRecord, HttpServletRequest request) throws Exception {
		return new AttachmentHandler(this).createAttachmentUploadPassport(actionForm, field, attachmentService, fileLength, recordClassName, recordId, uploadEnabled, newRecord, request);
	}
	
	/**
	 * 上传HTTP文件形式的附件,并返回附件完整路径
	 * @param actionForm
	 * @param field
	 * @param attachmentService
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public String uploadAttachmentFormFile(ActionForm actionForm, Field field, AttachmentService attachmentService, long recordId) throws Exception {
		return new AttachmentHandler(this).uploadAttachmentFormFile(actionForm, field, attachmentService, recordId);
	}
	
	/**
	 * 处理上传的附件文件,并返回附件对象
	 * @param actionForm
	 * @param fileName
	 * @param field
	 * @param attachmentService
	 * @param recordId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Attachment processUploadedAttachmentFile(ActionForm actionForm, String fileName, Field field, AttachmentService attachmentService, long recordId, HttpServletRequest request) throws Exception {
		return new AttachmentHandler(this).processUploadedAttachmentFile(actionForm, fileName, field, attachmentService, recordId, request);
	}
	
	/**
	 * 获取临时附件的过期小时数,超出这个时间,附件自动删除
	 * @return
	 */
	public int getTemporaryAttachmentExpiresHours() throws Exception {
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
	public WaterMark getWaterMark(ActionForm form, HttpServletRequest request) throws Exception {
		return null;
	}
	
	/**
	 * 获取权限控制列表
	 * @param form
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @return
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
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
		new AttachmentHandler(this).checkAttachmentUploadPrivilege(form, request, record, acl, sessionInfo);
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
		new AttachmentHandler(this).checkAttachmentDownloadPrivilege(form, request, record, acl, sessionInfo);
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
		return new ComponentHandler(this).checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
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
		new ComponentHandler(this).checkSaveComponentPrivilege(form, request, record, component, acl, sessionInfo);
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
		new ComponentHandler(this).checkDeleteComponentPrivilege(form, request, record, component, acl, sessionInfo);
	}
	
	/**
	 * 获取服务,服务名称在表单配置中配置,为空时调用businessService
	 * @param formDefine
	 * @return
	 */
	public BusinessService getBusinessService(Form formDefine) throws SystemUnregistException {
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
	public String getOpenMode(ActionForm form, HttpServletRequest request) {
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
		return new ComponentHandler(this).loadComponentRecord(form, mainRecord, componentName, sessionInfo, request);
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
		new ComponentHandler(this).initComponentForm(form, mainRecord, componentName, sessionInfo, request);
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
		new ComponentHandler(this).fillComponentForm(form, component, mainRecord, componentName, sessionInfo, request);
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
		new ComponentHandler(this).loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
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
		new ComponentHandler(this).loadComponent(mapping, form, componentName, request, response);
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
		new ComponentHandler(this).saveComponent(mapping, form, componentName, foreignKeyProperty, reload, request, response);
	}
	
	/**
	 * 新建下一个时,继承原来输入的信息
	 * @param newForm
	 * @param oldForm
	 * @throws Exception
	 */
	public void inheritComponentProperties(ActionForm newForm, ActionForm oldForm) throws Exception {
		new ComponentHandler(this).inheritComponentProperties(newForm, oldForm);
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
		new ComponentHandler(this).validateComponent(form, openMode, mainRecord, component, componentName, sessionInfo, request);
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
		return new ComponentHandler(this).generateComponentRecord(form, mainRecord, componentName, sessionInfo, request, response);
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
		new ComponentHandler(this).saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
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
		new ComponentHandler(this).deleteComponentRecord(form, mainRecord, componentName, sessionInfo, request);
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
		new ComponentHandler(this).refreshMainRecord(form, refreshActionName, tabSelected, request, response);
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
		return new LoadHandler(this).executeLoadAction(mapping, form, request, response);
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
		return new SaveHandler(this).executeSaveAction(mapping, form, request, response, reload, tabSelected, actionResult, forwardName);
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
		return new DeleteHandler(this).executeDeleteAction(mapping, form, request, response, actionResult, forwardName);
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
		return new SaveHandler(this).executeCreateNextAction(mapping, form, request, response);
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
		return new AttachmentHandler(this).executeAttachmentAction(mapping, form, request, response);
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
		return new ComponentHandler(this).executeLoadComponentAction(mapping, form, componentName, request, response);
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
		return new ComponentHandler(this).executeSaveComponentAction(mapping, form, componentName, tabSelected, foreignKeyProperty, refreshActionName, reload, request, response);
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
		return new ComponentHandler(this).executeCreateNextComponentAction(mapping, form, componentName, foreignKeyProperty, request, response);
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
		return new ComponentHandler(this).executeDeleteComponentAction(mapping, form, componentName, tabSelected, refreshActionName, request, response);
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
		return new AttachmentHandler(this).executeComponentAttachmentAction(mapping, form, componentName, request, response);
	}
	
	/**
	 * 执行处理远程文档操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeOpenRemoteDocumentAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new DocumentHandler(this).executeOpenRemoteDocumentAction(mapping, form, request, response);
	}
	
	/**
	 * 执行处理组成部分的远程文档操作
	 * @param mapping
	 * @param form
	 * @param componentName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeOpenComponentRemoteDocumentAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, String componentName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new DocumentHandler(this).executeOpenComponentRemoteDocumentAction(mapping, form, componentName, request, response);
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
	public ActionForward transactException(Exception e, ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean resumeAction) throws Exception {
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
		throw e;
	}
}
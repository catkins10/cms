package com.yuanluesoft.jeaf.form.actions.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.AttachmentSelector;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.Mime;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author chuan
 *
 */
public class AttachmentHandler {
	private FormAction formAction;
	
	public AttachmentHandler(FormAction formAction) {
		this.formAction = formAction;
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
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			throw new Exception();
		}
		ActionForm actionForm = (ActionForm)form;
		String attachmentAction = actionForm.getAttachmentSelector().getAction();
		try {
			Form formDefine = formAction.loadFormDefine(actionForm, request);
			formDefine.getField("attachmentSelector").setInputMode("hidden"); //修改输入方式
			//获取会话
			SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
			//加载主记录
			Record record = formAction.loadRecord(actionForm, formDefine, actionForm.getId(), sessionInfo, request); //加载记录
			List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, (record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT), sessionInfo);
			//检查上传权限
			boolean uploadEnabled = false;
			try {
				formAction.checkAttachmentUploadPrivilege(actionForm, request, record, acl, sessionInfo);
				uploadEnabled = true;
			}
			catch(PrivilegeException e) {
				
			}
			if(attachmentAction==null || "".equals(attachmentAction) || AttachmentSelector.SELECTOR_ACTION_LOAD.equals(attachmentAction)) { //加载
				formAction.checkAttachmentDownloadPrivilege(actionForm, request, record, acl, sessionInfo); //检查附件下载权限
			}
			processAttachmentAction(actionForm, actionForm.getFormDefine().getRecordClassName(), actionForm.getId(), attachmentAction, uploadEnabled, (record==null), request, response);
			return (AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction) ? null : mapping.findForward("load"));
		}
		catch(Exception e) {
			return transactException(mapping, actionForm, attachmentAction, e, request, response);
	    }
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
		if(formAction.isSecureAction && !formAction.isSecureURL(request)){
			throw new Exception();
		}
		ActionForm componentForm = (ActionForm)form;
		String attachmentAction = componentForm.getAttachmentSelector().getAction();
		try {
			Form formDefine = formAction.loadFormDefine(componentForm, request);
			formDefine.getField("attachmentSelector").setInputMode("hidden"); //修改输入方式
			SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
			Record record = formAction.loadRecord(componentForm, formDefine, componentForm.getId(), sessionInfo, request); //加载主记录
			List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT, sessionInfo);
			Record component = formAction.loadComponentRecord(componentForm, record, componentName, sessionInfo, request);
			//检查保存权限(上传权限)
			boolean uploadEnabled = false;
			try {
				formAction.checkSaveComponentPrivilege(componentForm, request, record, component, acl, sessionInfo);
				uploadEnabled = true;
			}
			catch(PrivilegeException e) {
				
			}
			if(attachmentAction==null || "".equals(attachmentAction) || AttachmentSelector.SELECTOR_ACTION_LOAD.equals(attachmentAction)) { //加载
				formAction.checkLoadComponentPrivilege(componentForm, request, record, component, acl, sessionInfo); //检查加载权限(附件下载权限)
			}
			Class ccomponentClass = PropertyUtils.getProperty(form, componentName).getClass();
			long componentId = ((Number)PropertyUtils.getProperty(form, componentName + ".id")).longValue();
			processAttachmentAction(componentForm, ccomponentClass.getName(), componentId, attachmentAction, uploadEnabled, component==null, request, response);
			return (AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction) ? null : mapping.findForward("load"));
		}
		catch(Exception e) {
			return transactException(mapping, componentForm, attachmentAction, e, request, response);
        }
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
		String passport = attachmentService.createUploadPassport(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), field, recordId, fileLength, request);
		if(newRecord) { //新记录,记录为临时附件
			((TemporaryFileManageService)formAction.getService("temporaryFileManageService")).registTemporaryAttachment(recordClassName, actionForm.getFormDefine().getApplicationName(), recordId, formAction.getTemporaryAttachmentExpiresHours());
		}
		return passport;
	}
	
	/**
	 * 上传HTTP文件形式的附件,返回附件名称
	 * @param actionForm
	 * @param field
	 * @param attachmentService
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public String uploadAttachmentFormFile(ActionForm actionForm, Field field, AttachmentService attachmentService, long recordId) throws Exception {
		String path = attachmentService.upload(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), field, recordId, actionForm.getAttachmentSelector().getUpload());
		return new File(path).getCanonicalFile().getName();
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
		String path = attachmentService.processUploadedFile(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), field, recordId, fileName);
		if(path==null) {
			return null;
		}
		List attachments = attachmentService.list(actionForm.getFormDefine().getApplicationName(), actionForm.getAttachmentSelector().getType(), recordId, false, 0, request);
		return (Attachment)ListUtils.findObjectByProperty(attachments, "name", new File(path).getCanonicalFile().getName());
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
	private void processAttachmentAction(ActionForm actionForm, String recordClassName, long recordId, String attachmentAction, boolean uploadEnabled, boolean newRecord, HttpServletRequest request, HttpServletResponse response) throws Exception {
		AttachmentSelector selector = actionForm.getAttachmentSelector();
		String fieldName = selector.getField();
		if(fieldName==null) {
			fieldName = selector.getType();
		}
		Field field = FieldUtils.getFormField(actionForm.getFormDefine(), fieldName, request);
		if(field==null) {
			return;
		}
		String serviceName = field==null ? "attachmentService" : (String)field.getParameter("serviceName");
		AttachmentService attachmentService = (AttachmentService)formAction.getService(serviceName==null || serviceName.equals("") ? field.getType() + "Service" : serviceName);
		//创建上传许可证
		if(AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction)) {
			if(!uploadEnabled) { //没有上传权限
				throw new PrivilegeException();
			}
			String passport = formAction.createAttachmentUploadPassport(actionForm, field, attachmentService, RequestUtils.getParameterIntValue(request, "fileLength"), recordClassName, recordId, uploadEnabled, newRecord, request);
			writeUploadPassport(passport, null, request, response);
			return;
		}
		//初始化附件选择器
		int index = field.getName().lastIndexOf('.');
		if(index!=-1) {
			field.setName(field.getName().substring(index + 1));
		}
		selector.setUploadDisabled(!uploadEnabled); //设置有没有上传权限
		selector.setImage("image".equals(field.getType()));
		selector.setVideo("video".equals(field.getType()));
		selector.setTitle(field.getTitle());
		selector.setExtendJs((String)field.getParameter("extendJs")); //附件校验JS
		selector.setSimpleMode("true".equals(field.getParameter("simpleMode"))); //是否精简模式
		try {
			selector.setMaxUpload(Integer.parseInt(field.getLength())); //最多允许上传的文件数
		}
		catch(Exception e) {
			
		}
		if(selector.getMaxUpload()==1) { //只允许上传一个附件
			//修改附件选择方式
			Field selectedTitlesField = (Field)FieldUtils.getRecordField(AttachmentSelector.class.getName(), "selectedTitles", request).clone();
			selectedTitlesField.setName("attachmentSelector.selectedTitles");
			selectedTitlesField.setInputMode("text");
			actionForm.getFormDefine().getFields().add(selectedTitlesField);
		}
		//设置文件扩展名
		String fileExtension = (String)field.getParameter("fileExtension");
		if(fileExtension==null) {
			fileExtension = FileUtils.getDefaultFileExtension(selector.isImage(), selector.isVideo());
		}
		selector.setFileExtension(fileExtension);
		//处理其它附件操作
		if(AttachmentSelector.SELECTOR_ACTION_UPLOAD.equals(attachmentAction)) { //HTTP文件上传
			if(!uploadEnabled) { //没有上传权限
				throw new PrivilegeException();
			}
			//上传文件,并设为最后上传的文件
			selector.setLastUploadFiles(formAction.uploadAttachmentFormFile(actionForm, field, attachmentService, recordId));
		    if(newRecord) { //新纪录,记录为临时附件
				((TemporaryFileManageService)formAction.getService("temporaryFileManageService")).registTemporaryAttachment(recordClassName, actionForm.getFormDefine().getApplicationName(), actionForm.getId(), formAction.getTemporaryAttachmentExpiresHours());
			}
        }
		else if(AttachmentSelector.SELECTOR_ACTION_DELETE.equals(attachmentAction)) { //删除
			if(!uploadEnabled) { //没有上传权限
				throw new PrivilegeException();
			}
			//执行删除
        	attachmentService.delete(actionForm.getFormDefine().getApplicationName(), recordId, selector.getType(), selector.getSelectedNames());
        	selector.setLastUploadFiles(null);
        }
		else if(AttachmentSelector.SELECTOR_ACTION_DELETE_ALL.equals(attachmentAction)) { //删除全部
			if(!uploadEnabled) { //没有上传权限
				throw new PrivilegeException();
			}
			//执行删除
			attachmentService.deleteAll(actionForm.getFormDefine().getApplicationName(), selector.getType(), recordId);
			selector.setLastUploadFiles(null);
		}
		else if(AttachmentSelector.SELECTOR_ACTION_PROCESS_UPLOADED_FILES.equals(attachmentAction)) { //处理上传的文件
			selector.setLastUploadAttachments(new ArrayList());
			String[] lastUploadFiles = selector.getLastUploadFiles()==null || selector.getLastUploadFiles().isEmpty() ? null : selector.getLastUploadFiles().split("\\*");
			selector.setLastUploadFiles(null);
			for(int i=0; i<(lastUploadFiles==null ? 0 : lastUploadFiles.length); i++) {
				Attachment attachment = formAction.processUploadedAttachmentFile(actionForm, lastUploadFiles[i], field, attachmentService, recordId, request);
				if(attachment!=null) {
					addWaterMark(attachment.getFilePath(), selector, attachmentService, actionForm, recordClassName, recordId, newRecord, request, response);
					selector.getLastUploadAttachments().add(attachment);
					selector.setLastUploadFiles((selector.getLastUploadFiles()==null ? "" : selector.getLastUploadFiles() + "*") + attachment.getName());
				}
			}
		}
		//获取附件列表和数量
		selector.setAttachments(attachmentService.list(actionForm.getFormDefine().getApplicationName(), selector.getType(), recordId, true, 0, request));
		selector.setAttachmentCount(selector.getAttachments()==null ? 0 : selector.getAttachments().size());
		//设置选中的附件
	   	selector.setSelectedNames(null);
    	selector.setSelectedTitles(null);
    	String[] selectedNames = selector.getLastUploadFiles()==null || selector.getLastUploadFiles().isEmpty() ? null : selector.getLastUploadFiles().split("\\*");
    	selectedNames =	selectedNames!=null || selector.getAttachmentCount()==0 ? selectedNames : new String[]{((Attachment)selector.getAttachments().get(0)).getName()};
		if(!"dialog".equals(actionForm.getDisplayMode())) {
			selectedNames = selectedNames==null || selectedNames.length==0 ? null : new String[]{selectedNames[0]};
		}
		for(int i = 0; i < (selectedNames==null ? 0 : selectedNames.length); i++) {
			Attachment attachment = (Attachment)ListUtils.findObjectByProperty(selector.getAttachments(), "name", selectedNames[i]);
			selector.setSelectedNames((selector.getSelectedNames()==null ? "" : selector.getSelectedNames() + "*") + attachment.getName());
			selector.setSelectedTitles((selector.getSelectedTitles()==null ? "" : selector.getSelectedTitles() + "*") + attachment.getTitle());
		}
		//附件对话框
		if("dialog".equals(actionForm.getDisplayMode())) {
			//设置标题
			actionForm.setFormTitle(selector.getTitle() + "选择");
			//设置操作
			if(!selector.isUploadDisabled()) {
				actionForm.getFormActions().addFormAction(-1, "上传", "", true);
				actionForm.getFormActions().addFormAction(-1, "删除", "doDelete()", false);
				actionForm.getFormActions().addFormAction(-1, "全部删除", "doDeleteAll()", false);
			}
			actionForm.getFormActions().addFormAction(-1, "确定", "doOK()", selector.isUploadDisabled());
			actionForm.getFormActions().addFormAction(-1, "取消", "DialogUtils.closeDialog()", false);
		}
	}
	
	/**
	 * 输出上传许可
	 * @param passport
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void writeUploadPassport(String passport, Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(e!=null) {
			if(e instanceof SessionException) {
				passport = "SESSION_EXCEPTION";
			}
			else if(e instanceof PrivilegeException) {
				passport = "NO_PRIVILEGE";
	    	}
			else if(e instanceof LockException) {
				passport = "LOCK_FAILED";
	        }
			else {
				passport = "ERROR";
			}
		}
		if(!"true".equals(request.getParameter("writeAsJs"))) {
			response.getWriter().write(passport);
		}
		else {
			response.setContentType(Mime.MIME_JS);
			response.getWriter().write("window.onUploadPassportCreated('" + passport + "');");
		}
	}
	
	/**
	 * 处理异常
	 * @param mapping
	 * @param actionForm
	 * @param attachmentAction
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward transactException(ActionMapping mapping, ActionForm actionForm, String attachmentAction, Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
		e.printStackTrace();
		if(AttachmentSelector.SELECTOR_ACTION_PASSPORT.equals(attachmentAction)) { //创建上传许可证
			writeUploadPassport(null, e, request, response);
			return null;
		}
		else if(!AttachmentSelector.SELECTOR_ACTION_PROCESS_UPLOADED_FILES.equals(attachmentAction) && //控件方式:处理上传完成后的文件
				!AttachmentSelector.SELECTOR_ACTION_UPLOAD.equals(attachmentAction)) { //上传文件
			return formAction.transactException(e, mapping, actionForm, request, response, false);
		}
		if(e instanceof SessionException) { //打开登录对话框,重新登录
			actionForm.setError("会话无效");
		}
		else {
			String error = e.getMessage();
			actionForm.setError(error!=null ? error : (AttachmentSelector.SELECTOR_ACTION_UPLOAD.equals(attachmentAction) ? "上传文件时出错" : "处理上传的文件时出错"));
		}
		return mapping.findForward("load");
	}
	
	/**
	 * 给图片添加水印
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
	private void addWaterMark(String attachmentPath, AttachmentSelector attachmentSelector, AttachmentService attachmentService, ActionForm actionForm, String recordClassName, long recordId, boolean newRecord, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!attachmentSelector.isImage()) { //不是图片
			return;
    	}
		WaterMark waterMark = formAction.getWaterMark(actionForm, request); //获取水印
		if(waterMark!=null) {
			((ImageService)attachmentService).addWaterMark(attachmentPath, waterMark); //添加水印
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
			char accessLevel = formAction.getRecordControlService().getRegistedRecordAccessLevel(form.getId(), request.getSession());
			if(accessLevel >= RecordControlService.ACCESS_LEVEL_EDITABLE) {
				return;
			}
		}
		catch (ServiceException e) {
			
		}
		formAction.checkSavePrivilege(form, request, record, record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT, acl, sessionInfo);
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
			char accessLevel = formAction.getRecordControlService().getRegistedRecordAccessLevel(form.getId(), request.getSession());
			if(accessLevel >= RecordControlService.ACCESS_LEVEL_READONLY) {
				return;
			}
		}
		catch (ServiceException e) {
			
		}
		formAction.checkLoadPrivilege(form, request, record, record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT, acl, sessionInfo);
	}
}

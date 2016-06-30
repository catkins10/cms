package com.yuanluesoft.jeaf.form.actions.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author chuan
 *
 */
public class DocumentHandler {
	private FormAction formAction;
	
	public DocumentHandler(FormAction formAction) {
		this.formAction = formAction;
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
		try {
			openRemoteDocument(form, request, response);
    	}
		catch(Exception e) {
			transactRemoteDocumentException(form, e, response);
		}
		return null;
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
		try {
			openComponentRemoteDocument(form, componentName, request, response);
    	}
		catch(Exception e) {
			transactRemoteDocumentException(form, e, response);
		}
		return null;
	}
	
	/**
	 * 处理远程文档
	 * @param request
	 * @param response
	 * @param mapping
	 * @param formToLoad
	 * @param response
	 * @return
	 * @return
	 * @throws Exception
	 */
	public void openRemoteDocument(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//会话检查
		SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
		ActionForm formToHandle = (ActionForm)form;
		Form formDefine = formAction.loadFormDefine(formToHandle, request);
		String openMode = formAction.getOpenMode(formToHandle, request);
		//生成记录
		Record record = formAction.generateRecord(formToHandle, openMode, request, sessionInfo);
		List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, openMode, sessionInfo);
		char accessLevel = formAction.checkLoadPrivilege(formToHandle, request, openMode.equals(FormAction.OPEN_MODE_CREATE) ? null : record, openMode, acl, sessionInfo);
		if(accessLevel < RecordControlService.ACCESS_LEVEL_READONLY) {
			throw new PrivilegeException();
		}
		String documentCommand = request.getParameter("documentCommand");
		if(accessLevel < RecordControlService.ACCESS_LEVEL_EDITABLE && documentCommand.indexOf("view")==-1) { //没有编辑权限,且文档命令不是查看
			throw new PrivilegeException();
		}
		//计费控制
		formAction.checkCharge(formToHandle, request, record, openMode, acl, sessionInfo);
		//表单校验
		formAction.validateForm(formToHandle, record, openMode, sessionInfo, request);
		//业务逻辑校验
		BusinessService businessService = formAction.getBusinessService(formDefine);
		formAction.validateBusiness(businessService, formToHandle, openMode, record, sessionInfo, request);
		//填充表单
		formAction.fillForm(formToHandle, record, accessLevel, acl, sessionInfo, request, response);
		//输出文件
		formAction.writeRemoteDocument(null, documentCommand, generateDocumentCommandParameterMap(documentCommand, request), formDefine.getApplicationName(), formDefine.getName(), null, formToHandle, request, response, sessionInfo);
	}
	
	/**
	 * 处理组成部分的远程文档
	 * @param form
	 * @param componentName
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void openComponentRemoteDocument(org.apache.struts.action.ActionForm form, String componentName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo = formAction.getSessionInfo(request, response);
		ActionForm formToHandle = (ActionForm)form;
		Form formDefine = formAction.loadFormDefine(formToHandle, request);
		Record record = formAction.loadRecord(formToHandle, formDefine, formToHandle.getId(), sessionInfo, request); //加载主记录
		List acl = formAction.getAcl(formDefine.getApplicationName(), form, record, record==null ? FormAction.OPEN_MODE_CREATE : FormAction.OPEN_MODE_EDIT, sessionInfo);
		Record component = formAction.generateComponentRecord(formToHandle, record, componentName, sessionInfo, request, response);
		//检查主记录的保存权限
		char accessLevel = formAction.checkLoadComponentPrivilege(formToHandle, request, record, component, acl, sessionInfo);
		if(accessLevel < RecordControlService.ACCESS_LEVEL_READONLY) {
			throw new PrivilegeException();
		}
		String documentCommand = request.getParameter("documentCommand");
		if(accessLevel < RecordControlService.ACCESS_LEVEL_EDITABLE && documentCommand.indexOf("view")==-1) { //没有编辑权限,且文档命令不是查看
			throw new PrivilegeException();
		}
		formAction.validateComponent(formToHandle, formToHandle.getAct(), record, component, componentName, sessionInfo, request);
		//填充页面
		formAction.fillComponentForm(formToHandle, component, record, componentName, sessionInfo, request); //填充表单
		//输出文件
		formAction.writeRemoteDocument(null, documentCommand, generateDocumentCommandParameterMap(documentCommand, request), formDefine.getApplicationName(), formDefine.getName(), null, formToHandle, request, response, sessionInfo);
	}
	
	/**
	 * 获取命令参数列表,并转换为MAP
	 * @param documentCommand
	 * @param request
	 * @return
	 */
	private Map generateDocumentCommandParameterMap(String documentCommand, HttpServletRequest request) {
		Map documentCommandParameterMap = new HashMap();
		String documentCommandParameter = request.getParameter("documentCommandParameter");
		List parameters = documentCommandParameter==null || documentCommandParameter.isEmpty() ? null : StringUtils.getProperties(documentCommandParameter);
		for(Iterator iterator = parameters==null ? null : parameters.iterator(); iterator!=null && iterator.hasNext();) {
			Attribute attribute = (Attribute)iterator.next();
			documentCommandParameterMap.put(attribute.getName(), attribute.getValue());
		}
		if(RemoteDocumentService.COMMAND_VIEW_DOCUMENT.equals(documentCommand)) { //查看文档
			if(!documentCommandParameterMap.containsKey(RemoteDocumentService.COMMAND_PARAMETER_KEEP_FIELD_VALUE)) {
				documentCommandParameterMap.put(RemoteDocumentService.COMMAND_PARAMETER_KEEP_FIELD_VALUE, "true"); //增加参数,禁止更新字段
			}
			if(!documentCommandParameterMap.containsKey(RemoteDocumentService.COMMAND_PARAMETER_KEEP_RECORD_LIST)) {
				documentCommandParameterMap.put(RemoteDocumentService.COMMAND_PARAMETER_KEEP_RECORD_LIST, "true"); //增加参数,禁止更新记录列表
			}
		}
		return documentCommandParameterMap;
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
		RemoteDocumentService remoteDocumentService = (RemoteDocumentService)formAction.getService("remoteDocumentService");
		remoteDocumentService.writeRemoteDocument(documentApplicationName, documentCommand, documentCommandParameter, documentRelatedApplicationName, documentRelatedFormName, form, documentFiles, request, response, sessionInfo);
	}
	
	/**
	 * 处理远程文档异常
	 * @param e
	 * @param response
	 * @throws Exception
	 */
	private void transactRemoteDocumentException(org.apache.struts.action.ActionForm form, Exception e, HttpServletResponse response) throws Exception {
		String error = null;
		if(e instanceof SessionException) {
			error = "NOSESSIONINFO";
		}
		else if(e instanceof PrivilegeException) {
			error = "NOPRIVILEGE";
		}
		else if(e instanceof ValidateException) {
			ActionForm actionForm = (ActionForm)form;
			error = ListUtils.join(actionForm.getErrors(), ",", false);
		}
		else {
			Logger.exception(e);
			error = "ERROR";
		}
		response.setCharacterEncoding("utf-8");
		String html = "<html><body><script>parent.setTimeout('window.onRemoteDocumentProcessError(\"" + error + "\")', 1);</script></body></html>";
		response.getWriter().print(html);
	}
}
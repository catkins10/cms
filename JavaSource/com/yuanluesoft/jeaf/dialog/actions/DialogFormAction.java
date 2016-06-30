package com.yuanluesoft.jeaf.dialog.actions;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.ActionException;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 对话框表单操作
 * @author linchuan
 *
 */
public abstract class DialogFormAction extends BaseAction {
	protected boolean isSecureAction = false;
	
	/**
	 * 检查加载权限
	 * @param form
	 * @param request
	 * @param acl
	 * @param sessionInfo
	 * @return
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	public abstract void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException;
	
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
	protected void load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//会话检查
		SessionInfo sessionInfo = getSessionInfo(request, response);
		ActionForm formToLoad = (ActionForm)form;
		//加表单定义
		loadFormDefine(formToLoad, request);
		//获取访问控制列表
		List acl = getAcl(formToLoad.getFormDefine().getApplicationName(), sessionInfo);
		//检查加载权限
		checkLoadPrivilege(formToLoad, request, acl, sessionInfo);
		//设置表单操作
		loadFormResource(formToLoad, acl, sessionInfo, request);
		//初始化表单
		initForm(formToLoad, acl, sessionInfo, request);
		//设置默认值
		setFieldDefaultValue(formToLoad, request);
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
	}
	
	/**
	 * 初始化表单
	 * @param form
	 * @param acl
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		
	}

	/**
	 * 加载表单操作
	 * @param form
	 * @param acl
	 * @param sessionInfo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public void loadFormResource(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(form.getFormDefine().getActions()!=null) {
			form.getFormActions().clear();
			form.getFormActions().addAll(form.getFormDefine().getActions());
		}
		for(Iterator iterator = form.getFormActions()==null ? null : form.getFormActions().iterator(); iterator.hasNext();) {
			//设置命令中的参数值
			FormAction formAction = (FormAction)iterator.next();
			formAction.setExecute(StringUtils.fillParameters(formAction.getExecute(), false, true, false, "utf-8", form, request, null));
			if("window.close()".equals(formAction.getExecute()) || "window.close();".equals(formAction.getExecute())) {
				formAction.setExecute("DialogUtils.closeDialog();");
			}
		}
		form.setFormTitle(form.getFormDefine().getTitle());
	}
	
	/**
	 * 校验
	 * @param form
	 * @param sessionInfo
	 * @param mapping
	 * @param request
	 * @throws ValidateException
	 * @throws ActionException
	 * @throws SystemUnregistException
	 */
    protected void validate(org.apache.struts.action.ActionForm form, SessionInfo sessionInfo, ActionMapping mapping, HttpServletRequest request) throws ValidateException, ActionException, SystemUnregistException {
    	List errors = validateForm((ActionForm)form, false, request);
		if(errors!=null && !errors.isEmpty()) {
			((ActionForm)form).setErrors(errors);
			throw new ValidateException();
		}
	}
    
    /**
     * 提交
     * @param form
     * @param sessionInfo
     * @param request
     * @param response
     * @throws Exception
     */
    public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
    		return transactException(e, mapping, form, request, response, false);
        }
        return mapping.findForward("load");
	}
	
	/**
	 * 执行提交操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param reload
	 * @param actionResult
	 * @param forwardName
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeSubmitAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String actionResult, String forwardName) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
		}
		ActionForm actionForm = (ActionForm)form;
		try {
			//会话检查
			SessionInfo sessionInfo = getSessionInfo(request, response);
			ActionForm formToSubmit = (ActionForm)form;
			//加表单定义
			loadFormDefine(formToSubmit, request);
			//获取访问控制列表
			List acl = getAcl(formToSubmit.getFormDefine().getApplicationName(), sessionInfo);
			//加载表单资源
			loadFormResource(formToSubmit, acl, sessionInfo, request);
			//检查加载权限
			checkLoadPrivilege(formToSubmit, request, acl, sessionInfo);
			//校验
			validate(form, sessionInfo, mapping, request);
			//执行提交操作
			submitForm(actionForm, sessionInfo, request, response);
			//设置操作结果
			formToSubmit.setActionResult(actionResult);
		}
		catch(ValidateException ve) {
			load(form, request, response); //重新载入对话框
			return mapping.getInputForward();
		}
		catch(Exception e) {
    		return transactException(e, mapping, form, request, response, true);
        }
		//重新载入
		if(reload) {
			load(form, request, response);
			return mapping.getInputForward();
		}
		if(forwardName!=null) {
			return mapping.findForward(forwardName);
		}
		//关闭对话框
		PrintWriter writer = response.getWriter();
		String script = generateScriptAfterSubmit();
		writer.write("<html>\n");
		writer.write("	<head>\n");
		writer.write("		<script language=\"JavaScript\" src=\"" + request.getContextPath() + "/jeaf/common/js/common.js\"></script>\n");
		writer.write("	</head>\n");
		writer.write("	<body>\n");
		if(actionForm.getRefeshOpenerScript()!=null) {
			writer.write("	<script language=\"JavaScript\">" + actionForm.getRefeshOpenerScript() + "</script>\n");
		}
		writer.write("		<script language=\"JavaScript\">" + (script==null ? "" : script + "\n") + "DialogUtils.closeDialog();</script>\n");
		writer.write("	</body>\n");
		writer.write("</html>");
		return null;
	}
	
	/**
	 * 生成提交后执行的脚本
	 * @return
	 */
	protected String generateScriptAfterSubmit() {
		return null;
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
		throw e;
	}
}
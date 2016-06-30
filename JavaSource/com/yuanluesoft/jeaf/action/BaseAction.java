/*
 * Created on 2004-12-20
 *
 */
package com.yuanluesoft.jeaf.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.business.validator.FieldValueValidator;
import com.yuanluesoft.jeaf.dialog.pages.DialogPageService;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.system.services.SystemService;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.SqlDateConverter;
import com.yuanluesoft.jeaf.util.SqlTimestampConverter;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 
 * @author linchuan
 *
 */
public class BaseAction extends Action {
	public boolean anonymousEnable = false; //是否允许匿名访问
	public boolean anonymousAlways = false; //是否总是匿名访问
	public boolean externalAction = false; //是否对外的操作
	public boolean backgroundAction = false; //是否后台操作,利用ajax或者iframe提交
	public String[] unallowableHtmlTags = null; //禁用的html标记
	
	public Class sessionInfoClass = SessionInfo.class;
	
	private WebApplicationContext webApplicationContext;
	private SystemService systemService; //系统服务
	
	static {
        ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
        ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
    }
	
	/**
	 * 获取权限控制列表
	 * @param form
	 * @param pojo
	 * @param openMode
	 * @param sessionInfo
	 * @return
	 */
	protected List getAcl(String applicationName, SessionInfo sessionInfo) throws Exception {
		return getAccessControlService().getAcl(applicationName, sessionInfo);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.action.Action#setServlet(org.apache.struts.action.ActionServlet)
	 */
	public void setServlet(ActionServlet actionServlet) {
		super.setServlet(actionServlet);
		ServletContext servletContext = actionServlet.getServletContext();
		webApplicationContext =	WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}
	
	/**
	 * 获取bean
	 * @param serviceName
	 * @return
	 * @throws BeansException
	 */
	public Object getBean(final String beanName) throws SystemUnregistException {
		return getService(beanName);
	}
	
    /**
	 * 获取服务
	 * @param serviceName
	 * @return
	 */
	public Object getService(final String serviceName) throws SystemUnregistException {
		Object service = webApplicationContext.getBean(serviceName);
		if(service instanceof DialogPageService) {
			return service;
		}
		if(systemService==null) {
			systemService = (SystemService)webApplicationContext.getBean("systemService");
		}
		if(serviceName.equals("systemService")) {
			return systemService;
		}
		try {
			if(!systemService.isSystemUseful()) {
				throw new SystemUnregistException();
			}
		}
		catch (ServiceException e) {
			throw new SystemUnregistException();
		}
		return service;
	}
	
	/**
	 * 获取访问控制服务
	 * @return
	 */
	public AccessControlService getAccessControlService() throws SystemUnregistException {
		return (AccessControlService)getService("accessControlService");
	}
	
	/**
	 * 获取记录访问控制服务
	 * @return
	 */
	public RecordControlService getRecordControlService() throws SystemUnregistException {
		return (RecordControlService)getService("recordControlService");
	}
	
	/**
	 * 获取用户管理服务
	 * @return
	 */
	public OrgService getOrgService() throws SystemUnregistException {
	    return (OrgService)getService("orgService");
	}
	
	/**
	 * 获取会话信息
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemUnregistException
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request, HttpServletResponse response) throws SessionException, SystemUnregistException {
		SessionService sessionService = (SessionService)getService("sessionService");
		if(anonymousAlways) {
	    	return sessionService.getSessionInfo(SessionService.ANONYMOUS);
	    }
	    return sessionService.getSessionInfo(request, sessionInfoClass, anonymousEnable);
	}
	
	/**
	 * 加载表单定义
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	public Form loadFormDefine(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request) throws Exception {
		FormDefineService formDefineService = (FormDefineService)getService("formDefineService");
		Form formDefine = formDefineService.loadFormDefine(form.getClass());
		if(formDefine==null) {
			throw new ServiceException("form define not found");
		}
		form.setFormDefine(formDefine);
		return formDefine;
	}
	
	/**
	 * 设置字段默认值
	 * @param form
	 * @param request
	 * @throws Exception
	 */
	public void setFieldDefaultValue(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request) throws Exception {
		if(form.getFormDefine()==null || form.getFormDefine().getClassName()==null) {
			loadFormDefine(form, request);
		}
		//设置初始值
		List fields = FieldUtils.listFormFields(form.getFormDefine(), null, "html,components,attachment,image,video", null, null, true, false, false, false, 0);
		for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			String parameter = request.getParameter(field.getName());
			if(parameter!=null && !parameter.isEmpty()) {
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
	 * 表单校验
	 * @param formToValidate
	 * @param forceValidateCode
	 * @param request
	 * @return
	 * @throws SystemUnregistException
	 */
	public List validateForm(com.yuanluesoft.jeaf.form.ActionForm formToValidate, boolean forceValidateCode, HttpServletRequest request) throws SystemUnregistException {
    	List errors = new ArrayList();
		for(Enumeration parameterNames = request.getParameterNames(); parameterNames.hasMoreElements();) {
			String parameterName = (String)parameterNames.nextElement();
			if("validateCode".equals(parameterName)) {
				continue;
			}
			String requestParameterValue = request.getParameter(parameterName);
			Object fieldValue = null;
			try {
				fieldValue = PropertyUtils.getProperty(formToValidate, parameterName);
			}
			catch (Exception e) {
				
			}
			//单个字段校验
			if(formToValidate.getFormDefine()==null || formToValidate.getFormDefine().getClassName()==null) {
				try {
					loadFormDefine(formToValidate, request);
				}
				catch (Exception e) {
					
				}
			}
			Field fieldDefine = FieldUtils.getFormField(formToValidate.getFormDefine(), parameterName, request);
			if(fieldDefine==null) {
				continue;
			}
			List fieldErrors = FieldValueValidator.validate(fieldDefine, fieldValue, requestParameterValue, "true".equals(formToValidate.getFormDefine().getExtendedParameter("sensitiveWordCheck")));
			if(fieldErrors!=null && !fieldErrors.isEmpty()) {
				errors.addAll(fieldErrors);
			}
			if(fieldValue!=null && unallowableHtmlTags!=null && (fieldValue instanceof String)) {
				String textValue = (String)fieldValue;
				for(int i=0; i<unallowableHtmlTags.length; i++) {
					if(textValue.matches("(?i).*<" + unallowableHtmlTags[i] + ".*") || textValue.matches("(?i).*" + unallowableHtmlTags[i] + ">.*")) {
						errors.add(fieldDefine.getTitle() + "不允许出现：" + unallowableHtmlTags[i]);
					}
				}
			}
		}
		if((anonymousAlways || anonymousEnable) && !RequestUtils.getRequestInfo(request).isClientRequest()) { //匿名页面,且不是客户端请求,校验请求代码和验证码
			FormSecurityService formSecurityService = (FormSecurityService)getService("formSecurityService");
			try {
				if(!formSecurityService.validateRequest(formToValidate.getRequestCode())) {
					errors.add("无效请求");
					formToValidate.setRequestCode(formSecurityService.registRequest(false)); //重新生成请求代码
					return errors;
				}
				if(!forceValidateCode) {
					forceValidateCode = formSecurityService.isValidateCodeImageRequired(formToValidate.getRequestCode());
				}
			}
			catch(Exception e) {
				Logger.exception(e);
				errors.add("系统错误");
				return errors;
			}
		}
		//校验验证码
		String validateCode = request.getParameter("validateCode");
		if(validateCode==null) { //验证码为空
			if(forceValidateCode && !RequestUtils.isWapRequest(request)) { //强制验证码校验,且不是WAP访问
				errors.add("验证码不能为空");
			}
		}
		else {
			ValidateCodeService validateCodeService = (ValidateCodeService)getService("validateCodeService");
    		//检查校验码
    		try {
				if(!validateCodeService.validateCode(validateCode, request)) {
					errors.add("验证码错误");
				}
			}
    		catch (ServiceException e) {
    			errors.add("验证码校验错误");
			}
		}
		return errors;
    }
	
	/**
	 * 重定向到登录页面
	 * @param action
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param exception
	 * @param resumeAction 登录成功后是否继续因会话超时而中断的操作
	 * @return
	 */
	public ActionForward redirectToLogin(Action action, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Exception exception, boolean resumeAction) {
		if(exception!=null && "redirected".equals(exception.getMessage())) {
	        return null;
	    }
		HttpSession session = request.getSession();
		String queryString = request.getQueryString();
		if(resumeAction) { //继续原操作
			String continuation = request.getParameter("continuation");
			if(continuation==null) {
				continuation = "" + UUIDLongGenerator.generateId();
				queryString = "continuation=" + continuation + (queryString==null || queryString.equals("") ? "" : "&" + queryString);
			}
			if(session.getAttribute(continuation + ".parameters")==null) {
				//把当前提交的参数保存到会话中
				HashMap parameters = (request.getParameterMap()==null ? new HashMap() : new HashMap(request.getParameterMap()));
				session.setAttribute(continuation + ".parameters", parameters);
			}
		}
		//删除ticket属性
		queryString = StringUtils.removeQueryParameters(queryString, "ticket");
		//设置登录后重定向的地址
		final String redirect = getLoginRedirectURL(queryString, request);
		boolean forceLogin =  false; //是否强制显示登录页面
		String loginReason = null;
		if(exception!=null) {
			if(exception instanceof PrivilegeException) { //没权限
				forceLogin = true;
				loginReason = "没有访问权限";
			}
			else if(SessionException.SESSION_FAILED.equals(exception.getMessage())) { //已登录但是会话创建失败
				forceLogin = true;
				loginReason = "没有访问权限";
			}
			else if(SessionException.SESSION_NO_MATCH.equals(exception.getMessage())) { //会话不匹配
				forceLogin = true;
				if(request.getParameter("ticket")!=null) { //用户登录后,会话类型不匹配,需要做出提示
					loginReason = "用户名错误";
				}
			}
			else if(SessionException.SESSION_TIMEOUT.equals(exception.getMessage())) { //超时
				loginReason = "超时，请重新登录";
			}
		}
		if(resumeAction) {
			if(loginReason==null) {
				loginReason = "超时，请重新登录";
			}
			loginReason += "，当前操作在登录后将继续。";
		}
		Link loginLink;
		try {
			loginLink = getLoginPageLink(form, request);
		}
		catch (SystemUnregistException e) {
			return null;
		}
		String loginURL = loginLink.getUrl();
		//检查是否有配置是否强制登录
		if(forceLogin && loginURL.indexOf("{PARAMETER:forceLogin}")==-1) {
			loginURL += (loginURL.lastIndexOf('?')==-1 ? "?" : "&") + "forceLogin={PARAMETER:forceLogin}";
		}
		//检查是否有配置是否允许匿名
		if(anonymousEnable && loginURL.indexOf("{PARAMETER:anonymousEnable}")==-1) {
			loginURL += (loginURL.lastIndexOf('?')==-1 ? "?" : "&") + "anonymousEnable={PARAMETER:anonymousEnable}";
		}
		//检查是否有配置登录原因
		if(loginReason!=null && loginURL.indexOf("{PARAMETER:loginReason}")==-1) {
			loginURL += (loginURL.lastIndexOf('?')==-1 ? "?" : "&") + "loginReason={PARAMETER:loginReason}";
		}
		//检查是否有配置重定向地址
		final boolean encryptURL = loginURL.indexOf("{PARAMETER:redirect}")==-1;
		if(encryptURL) {
			loginURL += (loginURL.lastIndexOf('?')==-1 ? "?" : "&") + "redirect={PARAMETER:redirect}";
		}
		//替换登录链接中的参数
		final String reason = loginReason;
		final boolean force = forceLogin;
		loginURL = StringUtils.fillParameters(loginURL, true, false, false, loginLink.getEncoding(), null, request, new FillParametersCallback() {
			public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
				if("forceLogin".equals(parameterName)) {
					return "" + force;
				}
				else if("loginReason".equals(parameterName)) {
					return reason;
				}
				else if("redirect".equals(parameterName)) {
					return encryptURL ? redirect : redirect;
				}
				else if("anonymousEnable".equals(parameterName)) {
					return "" + anonymousEnable;
				}
				return null;
			}
		});
		try {
			response.sendRedirect(loginURL); //重定向到登录界面
		}
		catch(Exception e) {
			
		}
		return null;
	}
	
	/**
	 * 获取登录后重定向的页面
	 * @param queryString
	 * @param request
	 * @return
	 */
	protected String getLoginRedirectURL(String queryString, HttpServletRequest request) {
		return RequestUtils.getRequestURL(request, false) + (queryString==null || queryString.equals("") ? "" : "?" + queryString);
	}
	
	/**
	 * 重定向到注销页面
	 * @param mapping
	 * @param form
	 * @param redirect
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward redirectToLogout(ActionMapping mapping, ActionForm form, final String redirect, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        //删除会话信息
    	SessionService sessionService = (SessionService)getService("sessionService");
        sessionService.removeSessionInfo(request);
        //重定向到SSO注销页面
        Link logoutLink = getLogoutPageLink(form, request);
        String logoutURL = logoutLink.getUrl();
		//检查是否有配置重定向地址
		if(logoutURL.indexOf("{PARAMETER:redirect}")==-1) {
			logoutURL += (logoutURL.lastIndexOf('?')==-1 ? "?" : "&") + "redirect={PARAMETER:redirect}";
		}
		logoutURL = StringUtils.fillParameters(logoutURL, true, false, false, logoutLink.getEncoding(), null, request, new FillParametersCallback() {
			public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
				if("redirect".equals(parameterName)) {
					return redirect;
				}
				return null;
			}
		});
		if(!logoutURL.startsWith("javascript:")) {
			response.sendRedirect(logoutURL);
		}
		else {
			String html = "<html>\r\n" +
						  "  <body>\r\n" +
						  "    <script>" + logoutURL.substring("javascript:".length()) + "</script>\r\n" +
						  "  </body>\r\n" +
						  "</html>";
			response.getWriter().write(html);
		}
    	return null;
    }
	
	/**
	 * 重定向到计费提示页面
	 * @param action
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward redirectToChargePrompt(Action action,ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			//重定向
			response.sendRedirect(getChargePromptURL(request));
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		return null;
	}

	/**
	 * 获取登录页面的URL
	 * @return
	 */
	protected String getChargePromptURL(HttpServletRequest request) {
		try {
			return (String)getBean("webApplicationUrl") + (String)getBean("chargePromptURL");
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		return null;
	}
	
	/**
	 * 获取登录页面的URL
	 * @param form
	 * @param request
	 * @return
	 * @throws SystemUnregistException
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		String name;
		if("dialog".equals(request.getParameter("displayMode")) ||
		   ((form instanceof com.yuanluesoft.jeaf.form.ActionForm) &&
		     "dialog".equals(((com.yuanluesoft.jeaf.form.ActionForm)form).getDisplayMode()))) { //对话框
			name = externalAction ? "externalLoginDialogUrl" : "internalLoginDialogUrl";
		}
		else {
			name = externalAction ? "externalLoginUrl" : "internalLoginUrl";
		}
		return (Link)getBean(name);
	}
	
	/**
	 * 获取注销页面的URL
	 * @param form
	 * @return
	 * @throws SystemUnregistException
	 */
	protected Link getLogoutPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		return (Link)getBean(externalAction ? "externalLogoutUrl" : "internalLogoutUrl");
	}
	
	/**
	 * 重定向到https
	 * @param request
	 * @param response
	 */
	public void redirectToSecureLink(HttpServletRequest request, HttpServletResponse response) {
		String queryString = request.getQueryString();
		try {
			response.sendRedirect(getBean("webApplicationSafeUrl") + request.getRequestURI() + (queryString==null || queryString.equals("") ? "" : "?" + queryString));
		}
		catch (Exception e) {
			throw new Error(e.getMessage());
		}
	}
	
	/**
	 * 检查URL是否是安全的
	 * @param request
	 * @return
	 * @throws SystemUnregistException
	 */
	public boolean isSecureURL(HttpServletRequest request) throws SystemUnregistException {
		String secureURL = (String)getBean("webApplicationSafeUrl");
		if(secureURL!=null && !secureURL.equals("") && secureURL.startsWith("http")) {
			return (secureURL.startsWith((request.isSecure() ? "https://" : "http://") + request.getServerName()));
		}
		return true;
	}
	
	/**
	 * 关闭对话框,并刷新父窗口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward closeDialogAndRefreshOpener(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String html = "<html>" +
					  " <head>" +
					  "  <script language=\"JavaScript\" charset=\"utf-8\" src=\"" + request.getContextPath() + "/jeaf/common/js/common.js\"></script>" +
					  " </head>" +
					  " <body onload=\"DialogUtils.getDialogOpener().location.reload();DialogUtils.closeDialog();\"></body>" +
					  "</html>";
		response.getWriter().write(html);
		return null;
	}
}
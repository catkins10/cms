package com.yuanluesoft.jeaf.sso.processor;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.fingerprint.service.FingerprintService;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 登录表单处理器
 * @author yuanlue
 *
 */
public class LoginFormProcessor extends FormProcessor {
	private HTMLParser htmlParser; //HTML解析器
	private FingerprintService fingerprintService; //指纹认证服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		addFingerPrintVerify(pageElement); //增加指纹认证功能
		super.writePageElement(pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
		String script;
		String changePasswordReason = request.getParameter("changePasswordReason");
		if(changePasswordReason!=null) {
			script = "openChangePasswordDialog('" + changePasswordReason + "', '" + pageElement.getId() + "');";
		}
		else {
			script = "initLoginForm('" + pageElement.getId() + "');";
		}
		HTMLScriptElement scriptElement = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
		htmlParser.setTextContent(scriptElement, script);
		pageElement.appendChild(scriptElement);
	}
	
	/**
	 * 增加指纹认证功能
	 * @param formElement
	 */
	private void addFingerPrintVerify(HTMLElement pageElement) {
		if(fingerprintService==null || !fingerprintService.isFingerprintSupport()) { //没有指纹认证服务，或者不支持指纹认证
			return;
		}
		//添加指纹样本字段
		HTMLInputElement featureData = (HTMLInputElement)pageElement.getOwnerDocument().createElement("input");
		featureData.setAttribute("type", "hidden");
		featureData.setName("fingerprintFeatureData");
		pageElement.appendChild(featureData);
		
		//添加指纹采集插件
		HTMLObjectElement plugin = (HTMLObjectElement)pageElement.getOwnerDocument().createElement("object");
		plugin.setAttribute("classid", "clsid:7564ABAA-07DC-45A9-8C95-BB83B055C99E");
		plugin.setId("FPCtl");
		plugin.setAttribute("style", "display:none");
		pageElement.appendChild(plugin);
		
		//OnVerifyComplete事件处理
		HTMLScriptElement script = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
		script.setLang("javascript");
		script.setAttribute("for", "FPCtl");
		script.setAttribute("event", "OnVerifyComplete(encodedFeature)");
		htmlParser.setTextContent(script, "document.getElementsByName('fingerprintFeatureData')[0].value = encodedFeature; FormUtils.submitForm();");
		pageElement.appendChild(script);
		
		//启动指纹认证
		script = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
		script.setLang("javascript");
		htmlParser.setTextContent(script, "try {document.getElementById('FPCtl').StartVerify();}catch(e) {}");
		pageElement.appendChild(script);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#getFieldValue(org.w3c.dom.html.HTMLFormElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.jeaf.form.model.Form, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected Object getFieldValue(HTMLFormElement formElement, SitePage sitePage, Form form, Object bean, Field field, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws Exception {
		if("userName".equals(field.getName())) { //用户名
			String userName = request.getParameter("userName");
			if((userName==null || userName.equals("")) && (userName = CookieUtils.getCookie(request, SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME))!=null) {
				try {
					userName = URLDecoder.decode(userName, "utf-8");
				}
				catch (Exception e) {
					
				}
			}
			return userName;
		}
		else if("rememberUserName".equals(field.getName())) { //记住用户名
			String rememberUserName = request.getParameter("rememberUserName");
			if(rememberUserName!=null) {
				return new Boolean(rememberUserName);
			}
			return new Boolean(!"false".equals(CookieUtils.getCookie(request, SsoSessionService.SSO_LOGIN_REMEMBER_LOGIN_NAME)));
		}
		else if("redirect".equals(field.getName())) { //重定向的地址
			try {
				String redirectPageURL = request.getParameter("redirect");
				if(redirectPageURL!=null && !redirectPageURL.equals("")) {
					return redirectPageURL;
				}
				//解析页面跳转属性
				String properties = formElement.getAction();
				String redirectMode = StringUtils.getPropertyValue(properties, "redirectMode");
				if(redirectMode==null || "default".equals(redirectMode)) { //默认方式
					return null;
				}
				//自定义调整页面
				String redirectLinkFormat = StringUtils.getPropertyValue(properties, "redirect");
				//解析并得到第一个链接
				HTMLDocument moreLinkDocument = (HTMLDocument)htmlParser.parseHTMLString(redirectLinkFormat, "utf-8");
				final NodeList nodes = moreLinkDocument.getElementsByTagName("A");
				if(nodes==null || nodes.getLength()==0) {
					return null;
				}
				HTMLAnchorElement a = (HTMLAnchorElement)nodes.item(0);
				getPageBuilder().processPageElement(a, webDirectory, parentSite, sitePage, RequestUtils.getRequestInfo(request), request);
				return a.getHref();
			}
			catch(Exception e) {
				Logger.exception(e);
				return null;
			}
		}
		else if("loginPage".equals(field.getName())) { //登录页面
			String loginPage = request.getParameter("loginPage");
			if(loginPage==null || loginPage.equals("")) {
				loginPage = RequestUtils.getRequestURL(request, true);
				if("true".equals(request.getParameter("writePageElementAsJs"))) { //静态页面
					loginPage = StringUtils.removeQueryParameters(loginPage, "writePageElementAsJs");
					loginPage = StringUtils.removeQueryParameters(loginPage, "elementId");
				}
				loginPage = StringUtils.removeQueryParameters(loginPage, "loginError"); //删除URL中的loginError
				loginPage = StringUtils.removeQueryParameters(loginPage, "loginReason"); //删除URL中的loginReason
				loginPage = StringUtils.removeQueryParameters(loginPage, "changePasswordReason"); //删除URL中的changePasswordReason
				loginPage = StringUtils.removeQueryParameters(loginPage, "userName"); //删除URL中的userName
				int index = loginPage.indexOf("://localhost");
				if(index!=-1) {
					loginPage = loginPage.substring(loginPage.indexOf('/', index + "://localhost".length()));
				}
			}
			return loginPage;
		}
		return super.getFieldValue(formElement, sitePage, form, bean, field, webDirectory, parentSite, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#getFormError(javax.servlet.http.HttpServletRequest)
	 */
	protected String getFormError(Form form, HttpServletRequest request) {
		//检查url中是否包含loginError,如果有,输出提示
		String loginError = request.getParameter("loginError"); //从URL参数中获取
		if(loginError!=null && !loginError.equals("")) {
			return loginError;
		}
		loginError = (String)request.getAttribute("loginError");
		if(loginError!=null && !loginError.equals("")) {
			return loginError;
		}
		String loginReason = request.getParameter("loginReason");
		if(loginReason!=null && !loginReason.equals("")) {
			return loginReason;
		}
		return super.getFormError(form, request);
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}

	/**
	 * @return the fingerprintService
	 */
	public FingerprintService getFingerprintService() {
		return fingerprintService;
	}

	/**
	 * @param fingerprintService the fingerprintService to set
	 */
	public void setFingerprintService(FingerprintService fingerprintService) {
		this.fingerprintService = fingerprintService;
	}
}
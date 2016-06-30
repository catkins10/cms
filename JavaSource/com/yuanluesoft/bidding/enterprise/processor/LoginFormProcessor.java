package com.yuanluesoft.bidding.enterprise.processor;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLObjectElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 登录表单处理器
 * @author yuanlue
 *
 */
public class LoginFormProcessor extends FormProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		super.writePageElement(pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
		
		//把action设置为安全的action
		pageElement.setAttribute("action", Environment.getWebApplicationSafeUrl() + "/jeaf/sso/submitlogin.shtml");
		
		//插入对控件的调用
		HTMLObjectElement objectKeyReader = (HTMLObjectElement)pageElement.getOwnerDocument().createElement("object");
		objectKeyReader.setAttribute("classid", "clsid:D75EFAFA-9D1D-4384-BB5D-39389D5862CB"); //控件ID
		objectKeyReader.setId("KeyReader");
		objectKeyReader.setAttribute("style", "width:0;height:0");
		pageElement.appendChild(objectKeyReader);
		
		//插入事件处理
		HTMLScriptElement script = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
		script.setAttribute("language", "javascript");
		script.setAttribute("for", "KeyReader");
		script.setAttribute("event", "readError(error)");
		script.setText("onReadError(error)");
		pageElement.appendChild(script);
		
		//插入隐藏字段loginPage,目标指向当前页面
		if(request.getParameter("loginPage")==null || request.getParameter("loginPage").equals("")) {
			String url;
			if(request.getRequestURI().indexOf("login.shtml")!=-1) {
				url = RequestUtils.getRequestURL(request, false);
			}
			else if(request.getServerName().equals("localhost")) {
				String queryString = request.getQueryString();
				url = Environment.getWebApplicationUrl() + "/cms/sitemanage/index.shtml";
				url += (queryString==null || queryString.equals("") ? "" : "?" + queryString);
			}
			else {
				url = RequestUtils.getRequestURL(request, true);
			}
			htmlParser.appendHiddenField("loginPage", url, pageElement);
		}
		
		//如果当前页面没有指定跳转目标,插入隐藏字段redirect,目标指向当企业用户首页
		if(request.getParameter("redirect")==null || request.getParameter("redirect").equals("")) {
			htmlParser.appendHiddenField("redirect", Environment.getWebApplicationUrl() + "/bidding/enterprise/index.shtml", pageElement);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#getFormError(javax.servlet.http.HttpServletRequest)
	 */
	protected String getFormError(Form form, HttpServletRequest request) {
		if(MemberService.LOGIN_INVALID_USERNAME_OR_PASSWORD.equals(request.getParameter("loginError"))) {
    		return "用户名或密码错误";
		}
		else if(MemberService.LOGIN_ACCOUNT_IS_HALT.equals(request.getParameter("loginError"))) {
			return "帐户未生效或已停用";
		}
		else if("error".equals(request.getParameter("loginError"))) {
			return "登录出错";
		}
		return super.getFormError(form, request);
	}
}
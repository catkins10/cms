/*
 * Created on 2006-3-9
 *
 */
package com.yuanluesoft.jeaf.sso.actions.login;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class LoginAction extends BaseAction {

	/**
	 * 重定向 
	 * @param url
	 * @param ssoSessionId
	 * @param response
	 * @throws Exception
	 */
	public void redirect(String redirect, String ticket,  HttpServletRequest request, HttpServletResponse response, boolean fullScreen) throws Exception {
		redirect = redirect.replaceAll("&amp;", "&");
		redirect = StringUtils.removeQueryParameters(redirect, "ticket"); //删除URL中的ticket
		redirect = StringUtils.removeQueryParameters(redirect, "loginError"); //删除URL中的loginError
		//把TICKET添加到URL
		redirect += (redirect.indexOf('?')==-1 ? "?" : "&") + "ticket=" + ticket;
		redirect = RequestUtils.resetRedirectURL(redirect);
		if(!fullScreen) {
			if(!request.isSecure() || request.getMethod().equalsIgnoreCase("get")) {
				response.sendRedirect(redirect);
			}
			else {
				PrintWriter writer = response.getWriter();
				writer.println("<html><script>");
				writer.println("window.location.replace('" + redirect + "');");
				writer.println("</script></html>");
			}
		}
		else {
			PrintWriter writer = response.getWriter();
			writer.println("<html><script language=\"Javascript1.1\" charset=\"utf-8\" src=\"" + request.getContextPath() + "/jeaf/common/js/common.js\"></script>");
			writer.println("<script>");
			writer.println("var newWindow = PageUtils.openurl(\"" + redirect + "\", \"mode=fullscreen\", \"_blank\");");
			writer.println("newWindow.opener = null");
			writer.println("window.top.opener = null; window.top.close();");
			writer.println("</script></html>");
		}
	}
}
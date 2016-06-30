/*
 * Created on 2004-8-26
 *
 */
package com.yuanluesoft.jeaf.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 *
 * @author LinChuan
 * 
 */
public class ActionServlet extends org.apache.struts.action.ActionServlet {
	private String ssoLoginServerName = null;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		//Logger.info("System initialize.");
		super.init();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		//初始化服务容器
		Environment.setWebApplicationContext(webApplicationContext);
		//SERVLET上下文
		Environment.setServletContext(getServletContext());
		//设置应用URL
		Environment.setWebApplicationUrl((String)webApplicationContext.getBean("webApplicationUrl"));
		Environment.setWebApplicationSafeUrl((String)webApplicationContext.getBean("webApplicationSafeUrl"));
		String localUrl = (String)webApplicationContext.getBean("webApplicationLocalUrl");
		Environment.setWebApplicationLocalUrl(localUrl);
		int index = localUrl.indexOf('/', localUrl.indexOf("://") + 3);
	    Environment.setContextPath(index==-1 ? "" : localUrl.substring(index));
		//获取登录服务器名称
		ssoLoginServerName = (String)webApplicationContext.getBean("webApplicationSafeUrl");
		if(ssoLoginServerName.startsWith("https://")) { //登录使用HTTPS协议,则禁止在非HTTS模式下访问登录服务器的主机名
			ssoLoginServerName = ssoLoginServerName.substring(ssoLoginServerName.indexOf(":") + 3);
			index = ssoLoginServerName.indexOf('/');
			if(index!=-1) {
				ssoLoginServerName = ssoLoginServerName.substring(0, index);
			}
			ssoLoginServerName = ssoLoginServerName.toLowerCase();
			if(ssoLoginServerName.equals("localhost")) {
				ssoLoginServerName = null;
			}
			else {
				String webApplicationUrl = (String)webApplicationContext.getBean("webApplicationUrl");
				if(webApplicationUrl.endsWith(ssoLoginServerName)) {
					ssoLoginServerName = null;
				}
			}
		}
		else {
			ssoLoginServerName = null;  //非https模式,不检查用户访问的服务器
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionServlet#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding ("UTF-8"); //提供UTF-8字符集支持
		//检查用户访问的主机名是否和认证服务器主机名相同,禁止在非HTTS模式下访问登录服务器的主机名,以避免造成SsoSessionId泄露
		if(!request.isSecure() &&
		   ssoLoginServerName!=null && 
		   request.getServerName().toLowerCase().equals(ssoLoginServerName)) {
			throw new ServletException();
		}
		/*for(Enumeration parameterNames = request.getParameterNames(); parameterNames.hasMoreElements();) {
			String parameterName = (String)parameterNames.nextElement();
			String[] values = request.getParameterValues(parameterName);
			for(int i=0; i<)
		}
		*/
		if(RequestUtils.isHackerURL(request.getQueryString())) { //是否黑客篡改过的URL
			Logger.error("Invalid request: " + RequestUtils.getRequestURL(request, true));
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE); 
			return;
		}
		//继续执行因会话超时而中断的操作
		String continuation = request.getParameter("continuation");
		if(continuation!=null && !continuation.equals("") && !(request instanceof ContinuationRequest)) {
			HttpSession session = request.getSession();
			Map parameters = (Map)session.getAttribute(continuation + ".parameters");
			if(parameters!=null) {
				session.removeAttribute(continuation + ".parameters");
				request = new ContinuationRequest(request, parameters); //构造新的请求
			}
		}
		super.process(request, response);
	}
}
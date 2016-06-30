package com.yuanluesoft.jeaf.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class RequestUtils {
	//模板路径和URL
	private static String tempatePath = null;
	private static String tempateUrl = null;
	//页面路径和URL
	private static String sitePagePath = null;
	private static String sitePageUrl = null;
	//静态页面路径和URL
	private static String staticPagePath = null;
	private static String staticPageUrl = null;
	//附件服务
	private static AttachmentService attachmentService;
	
	/**
	 * 从http请求中解析出用户访问的URL
	 * @param request
	 * @param containQueryString 是否包含queryString
	 * @return
	 */
	public static String getRequestURL(HttpServletRequest request, boolean containQueryString) {
		String queryString = request.getQueryString();
		String url = getServerURL(request);
		String uri = (String)request.getAttribute("javax.servlet.forward.request_uri"); //forward前的地址
		url += (uri==null ? request.getRequestURI() : uri);
		if(containQueryString && queryString!=null && !queryString.isEmpty()) {
			queryString  = StringUtils.removeQueryParameters(queryString, "ticket,staticPageId,client.system,client.model,client.systemVersion,client.deviceId,client.retrieveClientPage,client.pageWidth");
			if(queryString!=null && !queryString.isEmpty()) {
				url += "?" + queryString;
			}
		}
		if(url.startsWith("http://localhost")) { //静态页面生成服务的请求
			String applicationUrl = Environment.getWebApplicationUrl();
			int index = applicationUrl.indexOf("://");
			index = (applicationUrl.indexOf('/', index==-1 ? 0 : index + 3));
			if(index!=-1) {
				applicationUrl = applicationUrl.substring(0, index);
			}
			url = applicationUrl + url.substring(url.indexOf('/', url.indexOf("://") + 3));
		}
		return url;
	}
	
	/**
	 * 获取服务器URL,如:http://192.168.0.10:8080
	 * @param request
	 * @return
	 */
	public static String getServerURL(HttpServletRequest request) {
		int port = request.getServerPort();
		return (request.isSecure() ? "https" : "http") + "://" + 
			   request.getServerName() + 
				((request.isSecure() && port!=443) || (!request.isSecure() && port!=80) ? ":" + port : "");
	}
	
	/**
	 * 是否WAP请求
	 * @param request
	 * @return
	 */
	public static boolean isWapRequest(HttpServletRequest request) {
		if(request==null) {
			return false;
		}
		String accept = request.getHeader("accept");
		if(accept==null) {
			return false;
		}
		accept = accept.toLowerCase();
		if(accept.indexOf("text/vnd.wap.wml")!=-1) { //支持WAP协议, WinWAP支持的类型: application/vnd.wap.wmlc, text/vnd.wap.wml, image/vnd.wap.wbmp, image/gif, image/jpeg, image/png, application/vnd.wap.wmlscriptc, text/vnd.wap.wmlscript
			String userAgent = request.getHeader("user-agent");
			return accept.indexOf("text/html")==-1 || userAgent==null || userAgent.toLowerCase().indexOf("ucweb")!=-1; //不支持HTML的、或者浏览器是UCWEB
		}
		return false;
	}
	
	/**
	 * 是否微信请求
	 * @param request
	 * @return
	 */
	public static boolean isWechatRequest(HttpServletRequest request) {
		/*
		 	Android微信客户端
		 	connection=keep-alive
		  	accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,**;q=0.8
			accept-encoding=gzip,deflate
			accept-language=zh-CN,en-US;q=0.8
			host=www.guangze.gov.cn
			user-agent=Mozilla/5.0 (Linux; Android 4.4.2; HTC 802t Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 MicroMessenger/5.2.380
			x-requested-with=com.tencent.mm
			tomcatquery=siteId=10
			content-length=0
			
			苹果微信客户端
			connection=keep-alive
			accept=text/html,application/xhtml+xml,application/xml;q=0.9,**;q=0.8
			accept-encoding=gzip, deflate
			accept-language=zh-cn
			host=www.guangze.gov.cn
			user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Mobile/11D201 MicroMessenger/5.2.1
			tomcatquery=siteId=10
			content-length=0
		 */
		String userAgent = request==null ? null : request.getHeader("user-agent");
		return userAgent!=null && userAgent.indexOf("MicroMessenger")!=-1;
	}
	
	/**
	 * 获取终端类型,computer/android/iphone/symbian
	 * @param request
	 * @return
	 */
	public static String getTerminalType(HttpServletRequest request) {
		if(request==null) {
			return "computer";
		}
		String userAgent = request.getHeader("user-agent");
		if(userAgent==null) {
			return "computer";
		}
		userAgent = userAgent.toLowerCase();
		if(userAgent.indexOf("android")!=-1 || userAgent.indexOf("miui")!=-1) { //Android或者小米
			return "android";
		}
		else if(userAgent.indexOf("iphone")!=-1) {
			return "iphone";
		}
		else if(userAgent.indexOf("ipad")!=-1) {
			return "ipad";
		}
		else if(userAgent.indexOf("symbian")!=-1) {
			return "symbian";
		}
		return "computer";
	}
	
	/**
	 * 获取浏览器类型
	 * @param request
	 * @return
	 */
	public static String getBrowserType(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if(userAgent==null) {
			return "unknown";
		}
		if(userAgent.matches("(?i).*(msie\\s|trident.*rv:)[\\w.]+.*") && !userAgent.matches("(?i).*opera.*")) {
			return "ie";
		}
		if(userAgent.matches("(?i).*chrome.*")) {
			return "opera";
		};
		if(userAgent.matches("(?i).* applewebkit/.*")) {
			return "safari";
		};
		if(userAgent.matches("(?i).*opera.*")) {
			return "opera";
		};
		return "unknown";
	}
	
	/**
	 * 获取IE版本
	 * @param request
	 * @return
	 */
	public static double getIEVersion(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if(userAgent==null) {
			return -1;
		}
		Pattern pattern = Pattern.compile("(msie\\s|trident.*rv:)([\\w.]+)", Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(userAgent);
		if(match.find()) {
			return Double.parseDouble(match.group(2));
		}
		return -1;
	};
	
	/**
	 * 获取请求信息
	 * @param request
	 * @return
	 */
	public static RequestInfo getRequestInfo(HttpServletRequest request) {
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setWapRequest(isWapRequest(request)); //是否WAP请求
		requestInfo.setWechatRequest(isWechatRequest(request)); //是否微信请求
		requestInfo.setClientRequest(request.getParameter("client.system")!=null && request.getParameter("client.model")!=null); //是否客户端请求
		requestInfo.setTraditionalChinese("true".equals(CookieUtils.getCookie(request, "traditionalChinese"))); //是否繁体中文
		requestInfo.setTerminalType(requestInfo.isClientRequest() ? "client" : getTerminalType(request)); //终端类型,computer/android/iphone/ipad/symbian/client
		requestInfo.setFlashSupport(true); //是否支持flash
		if(!requestInfo.isWapRequest() && !requestInfo.isWechatRequest() && (requestInfo.isClientRequest() || !"computer".equals(requestInfo.getTerminalType()))) {
			String pageWidthCookie;
			if(requestInfo.isClientRequest()) { //客户端页面
				String clientPageWidth = request.getParameter("client.pageWidth");
				requestInfo.setPageWidth(clientPageWidth==null ? 0 : Integer.parseInt(clientPageWidth));
			}
			else if((pageWidthCookie = CookieUtils.getCookie(request, "PAGEWIDTH"))==null) {
				requestInfo.setPageWidth(-1); //终端类型为android/iphone/symbian时,需要显示的页面宽度,如果为-1,则表示用户尚未作出选择
			}
			else {
				requestInfo.setPageWidth(Integer.parseInt(pageWidthCookie)); //终端类型为android/iphone/symbian时,需要显示的页面宽度,如果为-1,则表示用户尚未作出选择
			}
			requestInfo.setFlashSupport("1".equals(CookieUtils.getCookie(request, "FLASHSUPPORT"))); //是否支持flash
		}
		//设置页面模式
		if(request.getParameter("staticPageId")!=null) {
			requestInfo.setPageType(RequestInfo.PAGE_TYPE_BUILD_STATIC_PAGE);
		}
		else if("true".equals(request.getParameter("editMode"))) {
			requestInfo.setPageType(RequestInfo.PAGE_TYPE_EDIT);
		}
		else if(!requestInfo.isClientRequest()) { //不是客户端
			requestInfo.setPageType(RequestInfo.PAGE_TYPE_NORMAL);
		}
		else if("POST".equalsIgnoreCase(request.getMethod()) || "true".equals(request.getHeader("client.redirectAfterPost"))) { //提交
			requestInfo.setPageType(RequestInfo.PAGE_TYPE_CLIENT_POST);
		}
		else if("true".equals(request.getParameter("client.retrieveClientPage"))) {
			requestInfo.setPageType(RequestInfo.PAGE_TYPE_CLIENT_PAGE);
		}
		else {
			requestInfo.setPageType(RequestInfo.PAGE_TYPE_CLIENT_DATA);
		}
		return requestInfo;
	}
	
	/**
	 * 保存请求的页面信息
	 * @param response
	 * @param pageWidth
	 * @param flashSupport
	 * @param maxAge 以秒为单位,-1:表示浏览器关闭后失效
	 */
	public static void savaRequestPageInfo(HttpServletResponse response, int pageWidth, boolean flashSupport, int maxAge) {
		CookieUtils.addCookie(response, "PAGEWIDTH", "" + pageWidth, maxAge, "/", null, null);
		CookieUtils.addCookie(response, "FLASHSUPPORT", "1", (flashSupport ? maxAge : 0), "/", null, null);
	}
	
	/**
	 * 获取web应用的url
	 * @param request
	 * @return
	 */
	public static String getWebApplicationPath(HttpServletRequest request) {
		int port = request.getServerPort();
		String url = (request.isSecure() ? "https" : "http") + "://" + 
		  			 request.getServerName() + 
		  			 ((request.isSecure() && port!=443) || (!request.isSecure() && port!=80) ? ":" + port : "") +
		  			request.getContextPath();
		return url;
	}
	
	/**
	 * 获取字符型参数值
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static String getParameterStringValue(HttpServletRequest request, String parameterName) {
		String parameterValue = request.getParameter(parameterName);
		return parameterValue==null || parameterValue.equals("") ? null : parameterValue;
	}
	
	/**
	 * 获取日期型参数值
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static Date getParameterDateValue(HttpServletRequest request, String parameterName) {
		try {
			String parameterValue = request.getParameter(parameterName);
			return parameterValue==null || parameterValue.equals("") ? null : DateTimeUtils.parseDate(parameterValue, null);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取日期型参数值
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static Timestamp getParameterTimestampValue(HttpServletRequest request, String parameterName) {
		try {
			String parameterValue = request.getParameter(parameterName);
			return parameterValue==null || parameterValue.equals("") ? null : DateTimeUtils.parseTimestamp(parameterValue, null);
		}
		catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 获取长整数型参数值
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static long getParameterLongValue(HttpServletRequest request, String parameterName) {
		try {
			String parameterValue = request.getParameter(parameterName);
			return parameterValue==null || parameterValue.equals("") ? 0 : Long.parseLong(parameterValue);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取整数型参数值
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static int getParameterIntValue(HttpServletRequest request, String parameterName) {
		try {
			String parameterValue = request.getParameter(parameterName);
			return parameterValue==null || parameterValue.equals("") ? 0 : Integer.parseInt(parameterValue);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取双精度型参数值
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static double getParameterDoubleValue(HttpServletRequest request, String parameterName) {
		try {
			String parameterValue = request.getParameter(parameterName);
			return parameterValue==null || parameterValue.equals("") ? 0 : Double.parseDouble(parameterValue);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取浮点数型参数值
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static float getParameterFloatValue(HttpServletRequest request, String parameterName) {
		try {
			String parameterValue = request.getParameter(parameterName);
			return parameterValue==null || parameterValue.equals("") ? 0 : Float.parseFloat(parameterValue);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取参数值
	 * @param url
	 * @param parameterName
	 * @param urlEncoding
	 * @return
	 */
	public static String getParameterValue(String url, String parameterName, String urlEncoding) {
		if(url==null) {
			return "";
		}
		int index = url.indexOf(parameterName + "=");
		if(index==-1) {
			return "";
		}
		index += parameterName.length() + 1;
		int indexEnd = url.indexOf("&", index);
		String value = indexEnd==-1 ? url.substring(index) : url.substring(index, indexEnd);
		try {
			return URLDecoder.decode(value, urlEncoding);
		}
		catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}
	}
	
	/**
	 * 获取会话
	 * @param request
	 * @return
	 */
	public static SessionInfo getSessionInfo(HttpServletRequest request) {
		if(request==null) {
			return SessionService.ANONYMOUS_SESSION;
		}
		return (SessionInfo)request.getSession().getAttribute("SessionInfo");
	}
	
	/**
	 * 获取客户端IP地址
	 * @param request
	 * @return
	 */
	public static String getRemoteAddress(HttpServletRequest request) {
		String clientAddress = request.getHeader("Client-Address");
		return clientAddress==null ? request.getRemoteAddr() : clientAddress;
	}
	
	/**
	 * 获取客户端端口
	 * @param request
	 * @return
	 */
	public static int getRemotePort(HttpServletRequest request) {
		String clientPort = request.getHeader("Client-Port");
		try {
			return clientPort==null ? request.getRemotePort() : Integer.parseInt(clientPort);
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * 检查是否黑客URL
	 * @param url
	 * @return
	 */
	public static boolean isHackerURL(String url) {
		if(url==null) {
			return false;
		}
		//非法字符,防止XSS攻击(正则表达式: ((\%3C)|<)((\%2F)|\/)*[a-z0-9\%]+((\%3E)|>)/ix), SQL注入
		String[] illegalKeywords = {"\'", "<", ">", "%3c", "%3e",
									" and", "%20and", "+and", ")and", "%29and", "\tand", "%09and", "\rand", "%0dand", "\nand", "%0aand",
									" or", "%20or", "+or", ")or", "%29or", "\tor", "%09or", "\ror", "%0dor", "\nor", "%0aor",
									"content-transfer-encoding"};
		url = url.toLowerCase();
		for(int i=0; i<illegalKeywords.length; i++) {
			if(url.indexOf(illegalKeywords[i])!=-1) { //含非法字符
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 重定向
	 * @param url
	 * @param response
	 */
	public static String resetRedirectURL(String url) {
		int index = url.indexOf("://");
		if(index==-1) {
			return url;
		}
		index += 3;
		int indexEnd = url.indexOf('/', index);
		String domain = url.substring(index, indexEnd==-1 ? url.length() : indexEnd);
		String ssoDomains = null;
		try {
			ssoDomains = (String)Environment.getService("ssoDomains");
		}
		catch(ServiceException e) {
			
		}
		if(ssoDomains==null || ("," + ssoDomains.toLowerCase() + ",").indexOf("," + domain.toLowerCase() + ",")==-1) {
			url = indexEnd==-1 ? "/" : url.substring(indexEnd);
		}
		return url;
	}
	
	/**
	 * 按URL获取存放路径
	 * @param url
	 * @return
	 */
	public static String getFilePath(String url) {
		String lowerCaseURL = url.toLowerCase();
		if(url.indexOf('?')!=-1 || lowerCaseURL.indexOf(".shtml")!=-1 || lowerCaseURL.indexOf(".jsp")!=-1 || lowerCaseURL.indexOf("web-inf")!=-1) {
			return null;
		}
		if(tempatePath==null) {
			try {
				tempatePath = (String)Environment.getService("tempatePath");
				tempateUrl = (String)Environment.getService("tempateUrl");
				sitePagePath = (String)Environment.getService("sitePagePath");
				sitePageUrl = (String)Environment.getService("sitePageUrl");
				staticPagePath = (String)Environment.getService("staticPagePath");
				staticPageUrl = (String)Environment.getService("staticPageUrl");
				attachmentService = (AttachmentService)Environment.getService("attachmentService");
			}
			catch (ServiceException e) {
				
			}
		}
		if(url.startsWith(Environment.getWebApplicationUrl() + "/attachments/") || url.startsWith(Environment.getWebApplicationSafeUrl() + "/attachments/")) { //附件
			try {
				return attachmentService.getDownloadFilePath(url);
			}
			catch(ServiceException e) {
				return null;
			}
		}
		String path = null;
		if(url.startsWith(tempateUrl)) {
			path = tempatePath + url.substring(tempateUrl.length());
		}
		else if(url.startsWith(sitePageUrl)) {
			path = sitePagePath + url.substring(sitePageUrl.length());
		}
		else if(url.endsWith("/") && url.startsWith(staticPageUrl)) {
			path = staticPagePath + url.substring(staticPageUrl.length()) + "index.html"; //访问index.html
		}
		else if(url.endsWith(".html") && url.startsWith(staticPageUrl)) { //静态页面
			path = staticPagePath + url.substring(staticPageUrl.length());
		}
		try {
			if(path!=null && (FileUtils.isExists(path) || FileUtils.isExists(path=URLDecoder.decode(path, "utf-8"))) && new File(path).isFile()) {
				return path;
			}
			path = Environment.getWebAppPath() + url.substring((url.startsWith(Environment.getWebApplicationSafeUrl()) ? Environment.getWebApplicationSafeUrl() : Environment.getWebApplicationUrl()).length() + 1);
			if((FileUtils.isExists(path) || FileUtils.isExists(path=URLDecoder.decode(path, "utf-8"))) && new File(path).isFile()) {
				return path;
			}
		}
		catch (UnsupportedEncodingException e) {
		
		}
		return null;
	}
}
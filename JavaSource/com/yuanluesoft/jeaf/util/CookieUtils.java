/*
 * Created on 2006-3-10
 *
 */
package com.yuanluesoft.jeaf.util;

import java.sql.Timestamp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author linchuan
 *
 */
public class CookieUtils {
	
	/**
	 * 获取cookie值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
    	for(int i = (cookies==null ? -1 : cookies.length-1); i>=0; i--) {
    		if(cookies[i].getName().equals(name)) {
    	 		return cookies[i].getValue();
    		}
    	}
		return null;
	}
	
	/**
	 * 添加cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge 以秒为单位,如果设置为负值的话,则为浏览器进程Cookie(内存中保存),关闭浏览器就失效,0为立即删除该Cookie
	 * @param path
	 * @param domain
	 * @param comment
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String path, String domain, String comment) {
    	if(response.isCommitted()) {
    		return;
    	}
		Cookie cookie = new Cookie(name, value);
    	cookie.setPath(path);
    	cookie.setMaxAge(maxAge);
    	if(domain!=null) {
    		cookie.setDomain(domain);
    	}
    	if(comment!=null) {
    		cookie.setComment(comment);
    	}
    	response.addCookie(cookie);
    	/*//cookie.setSecure(true); //只对https有效
    	StringBuffer buffer = new StringBuffer();
    	appendCookieValue(buffer, cookie.getVersion(), cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure());
    	//buffer.append("; HttpOnly"); //只对http请求有效,js请求无效
    	response.addHeader(cookie.getVersion()==1 ? "Set-Cookie2" : "Set-Cookie", buffer.toString());*/
    }

	/**
	 * 生成cookie header
	 * @param buf
	 * @param version
	 * @param name
	 * @param value
	 * @param path
	 * @param domain
	 * @param comment
	 * @param maxAge
	 * @param isSecure
	 */
	public static void appendCookieValue(StringBuffer buf, int version, String name, String value, String path, String domain, String comment, int maxAge, boolean isSecure) {
		buf.append(name);
		buf.append("=");
		maybeQuote(version, buf, value);
		if(version == 1) {
			buf.append("; Version=1");
			if (comment != null) {
				buf.append("; Comment=");
				maybeQuote(version, buf, comment);
			}
		}
		if (domain != null) {
			buf.append("; Domain=");
			maybeQuote(version, buf, domain);
		}
		if (maxAge >= 0) {
			if(version == 0) {
				buf.append("; Expires=");
				buf.append(DateTimeUtils.formatCookieTimestamp(new Timestamp(maxAge == 0 ? 10000l : System.currentTimeMillis() + maxAge * 1000L)));
			}
			else {
				buf.append("; Max-Age=");
				buf.append(maxAge);
			}
		}
		if(path != null) {
			buf.append("; Path=");
			maybeQuote(version, buf, path);
		}
		if (isSecure) {
			buf.append("; Secure");
		}
	}

	public static boolean isToken(String value) {
		if(value == null) {
			return true;
		}
		int len = value.length();
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);
			if ((c < ' ') || (c >= '') || (",;".indexOf(c) != -1)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 在需要时添加引号
	 * @param version
	 * @param buf
	 * @param value
	 */
	public static void maybeQuote(int version, StringBuffer buf, String value) {
		if (isToken(value)) {
			buf.append(value);
		}
		else {
			if (version == 0) {
				throw new IllegalArgumentException(value);
			}
			buf.append('"');
			buf.append(value);
			buf.append('"');
		}
	}

	/**
	 * 删除cookie
	 * @param response
	 * @param name
	 */
	public static void removeCookie(HttpServletResponse response, String name, String path, String domain) {
		addCookie(response, name, "", 0, path, domain, null);
	}
}
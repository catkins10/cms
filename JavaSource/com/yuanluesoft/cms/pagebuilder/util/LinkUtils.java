package com.yuanluesoft.cms.pagebuilder.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLAnchorElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;

/**
 * 链接处理器
 * @author linchuan
 *
 */
public class LinkUtils { 

	/**
	 * 根据链接打开方式重置链接
	 * @param linkElement
	 * @param url
	 * @param openMode
	 * @param columnId
	 * @param siteId
	 * @param record
	 * @param appendIdField
	 * @param removeIfEmptyURL
	 * @param sitePage
	 * @param request
	 */
	public static void writeLink(HTMLAnchorElement linkElement, String url, String openMode, final long columnId, final long siteId, Object record, boolean appendIdField, boolean removeIfEmptyURL, SitePage sitePage, HttpServletRequest request) {
		if(url==null) {
			if(removeIfEmptyURL) {
				linkElement.getParentNode().removeChild(linkElement);
			}
			return;
		}
		String href = regenerateURL(url, columnId, siteId, record, appendIdField, sitePage, request);
		if(href.equals("")) { //链接为空
			if(removeIfEmptyURL) {
				linkElement.getParentNode().removeChild(linkElement);
			}
			return;
		}
		//脚本
		if(href.startsWith("javascript:")) {
			linkElement.setHref("javascript:void();");
			linkElement.removeAttribute("target");
			linkElement.setAttribute("onclick", href.substring("javascript:".length()) + ";return false;");
			return;
		}
		
		//设置连接
		linkElement.setHref(href);
		//设置连接打开方式
		if(openMode==null || openMode.equals("")) {
			linkElement.removeAttribute("target");
		}
		else if("_blank".equals(openMode) || "_self".equals(openMode) || "_top".equals(openMode)) {
			linkElement.setAttribute("target", openMode);
		}
		else if(openMode.startsWith("dialog")) { //对话框模式
			linkElement.removeAttribute("target");
			if(linkElement.getHref().indexOf("javascript:")==-1) {
				String[] values = openMode.split("_");
				try {
					linkElement.setHref("javascript:DialogUtils.openDialog('" + URLEncoder.encode(linkElement.getHref(), "utf-8") + "', '" + values[1] + "', '" + values[2] + "')");
				}
				catch (UnsupportedEncodingException e) {
					
				}
			}
		}
		else if("newWindow".equals(openMode)) {
			linkElement.setAttribute("target", "_blank");
		}
		else if("currentWindow".equals(openMode)) {
			linkElement.setAttribute("target", "_self");
		}
		else if("curWindow".equals(openMode)) {
			linkElement.setAttribute("target", "_self");
		}
		else if("topWindow".equals(openMode)) {
			linkElement.setAttribute("target", "_top");
		}
		else {
			linkElement.setAttribute("target", openMode);
		}
	}
	
	/**
	 * 获取链接地址
	 * @param url
	 * @param columnId
	 * @param siteId
	 * @param record
	 * @param appendIdField
	 * @param sitePage
	 * @param request
	 * @return
	 */
	public static String regenerateURL(String url, final long columnId, final long siteId, Object record, boolean appendIdField, final SitePage sitePage, HttpServletRequest request) {
		if(url==null) {
			return null;
		}
		boolean appendContextPath = true;
		if(url.startsWith("{PARAMETER:") && url.indexOf('}', "{PARAMETER:".length())==url.length()-1) { //URL本身就是一个记录属性
			url = fillParameters(url, true, false, false, "utf-8", columnId, siteId, record, sitePage, request);
			appendContextPath = false;
		}
		if(url.startsWith("{FINAL}")) { //已经是最终的URL
			url = url.substring("{FINAL}".length());
			//追加siteId
			if(siteId>0 && url.indexOf(".shtml")!=-1 && url.startsWith("/") && url.indexOf("siteId=")==-1) {
				url += (url.indexOf('?')==-1 ? "?" : "&") + "siteId=" + siteId;
			}
			return url;
		}
		String href = fillParameters(url, true, false, false, "utf-8", columnId, siteId, record, sitePage, request);
		href = fillParameters(href, true, false, false, "utf-8", columnId, siteId, record, sitePage, request); //填充2次,以填充动态参数
		if(appendIdField && record!=null && (href.startsWith("/") || href.startsWith("http")) && url.indexOf("{PARAMETER:id}")==-1 && url.indexOf("id=")==-1) {
			//没有出现过ID
			try {
				href += (href.indexOf('?')==-1 ? "?" : "&") + "id=" + PropertyUtils.getProperty(record, "id");
			}
			catch(Exception e) {
				
			}
		}
		if(href.indexOf(".shtml")!=-1 || href.indexOf(".jsp")!=-1) {
			//追加siteId
			if(siteId>0 && (href.startsWith("/") || href.startsWith("http")) && url.indexOf("{PARAMETER:siteId}")==-1 && url.indexOf("siteId=")==-1) {
				href += (href.indexOf('?')==-1 ? "?" : "&") + "siteId=" + siteId;
			}
			//删除"siteId=0"
			if(href.indexOf("siteId=0")!=-1 && !href.startsWith("javascript:")) {
				href = StringUtils.removeQueryParameters(href, "siteId");
			}
		}
		if(appendContextPath && url.startsWith("/")) {
			href = Environment.getContextPath() + href;
		}
		return href;
	}
	
	/**
	 * 填充参数
	 * @param href
	 * @param isURL
	 * @param isScript
	 * @param isSql
	 * @param encoding
	 * @param columnId
	 * @param siteId
	 * @param record
	 * @param sitePage
	 * @param request
	 * @return
	 */
	public static String fillParameters(String href, boolean isURL, boolean isScript, boolean isSql, String encoding, final long columnId, final long siteId, Object record, final SitePage sitePage, HttpServletRequest request) {
		return StringUtils.fillParameters(href, isURL, isScript, isSql, encoding, record, request, new FillParametersCallback() {
			public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
				if("siteId".equals(parameterName)) {
					return "" + siteId;
				}
				else if("columnId".equals(parameterName)) {
					return "" + columnId;
				}
				else if("sitePageApplicationName".equals(parameterName)) {
					return sitePage==null ? null : sitePage.getApplicationName();
				}
				else if("sitePageName".equals(parameterName)) {
					return sitePage==null ? null : sitePage.getName();
				}
				return null;
			}
		});
	}
}
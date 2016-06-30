package com.yuanluesoft.cms.taglib;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author yuanluesoft
 *
 */
public class PageTag extends BodyTagSupport {
	//输入参数
	private String applicationName = null;
	private String pageName = null;
	private String pageServiceName = null;
	
	//动态pageName
	private String scopePageName = null; 
	private String namePageName = null;
	private String propertyPageName = null;

	//默认的页面服务
	private PageService defaultPageService = null;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		try {
			PageService pageService;
			if(pageServiceName!=null) {
				pageService = (PageService)Environment.getService(pageServiceName);
			}
			else if(defaultPageService==null) {
				defaultPageService = (PageService)Environment.getService("pageService"); //默认页面服务
				pageService = defaultPageService;
			}
			else {
				pageService = defaultPageService;
			}
			//动态pageName
			if(namePageName!=null || propertyPageName!=null) {
				if(namePageName==null) {
					namePageName = Constants.BEAN_KEY;
				}
				pageName = (String)TagUtils.getInstance().lookup(pageContext, namePageName, propertyPageName, scopePageName);
			}
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			if(!"true".equals(request.getParameter("writePageElementAsJs"))) { //不是按js输出页面元素
				pageService.writePage(applicationName, pageName, request, (HttpServletResponse)pageContext.getResponse(), new PrintWriter(pageContext.getOut()), false);
			}
			else {
				pageService.writePage(applicationName, pageName, request, (HttpServletResponse)pageContext.getResponse(), false);
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		return (SKIP_BODY);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	public void release() {
		super.release();
		applicationName = null;
		pageName = null;
		pageServiceName = null;
		
		scopePageName = null; 
		namePageName = null;
		propertyPageName = null;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	/**
	 * @return the defaultPageService
	 */
	public PageService getDefaultPageService() {
		return defaultPageService;
	}

	/**
	 * @param defaultPageService the defaultPageService to set
	 */
	public void setDefaultPageService(PageService defaultPageService) {
		this.defaultPageService = defaultPageService;
	}

	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * @return the pageServiceName
	 */
	public String getPageServiceName() {
		return pageServiceName;
	}

	/**
	 * @param pageServiceName the pageServiceName to set
	 */
	public void setPageServiceName(String pageServiceName) {
		this.pageServiceName = pageServiceName;
	}

	/**
	 * @return the namePageName
	 */
	public String getNamePageName() {
		return namePageName;
	}

	/**
	 * @param namePageName the namePageName to set
	 */
	public void setNamePageName(String namePageName) {
		this.namePageName = namePageName;
	}

	/**
	 * @return the propertyPageName
	 */
	public String getPropertyPageName() {
		return propertyPageName;
	}

	/**
	 * @param propertyPageName the propertyPageName to set
	 */
	public void setPropertyPageName(String propertyPageName) {
		this.propertyPageName = propertyPageName;
	}

	/**
	 * @return the scopePageName
	 */
	public String getScopePageName() {
		return scopePageName;
	}

	/**
	 * @param scopePageName the scopePageName to set
	 */
	public void setScopePageName(String scopePageName) {
		this.scopePageName = scopePageName;
	}
}
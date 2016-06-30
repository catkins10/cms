package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.Link;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewActionGroup;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;

/**
 * 视图操作链接处理器
 * @author linchuan
 *
 */
public class ViewActionProcessor implements PageElementProcessor {
	private ViewDefineService viewDefineService; //视图定义服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, final WebDirectory webDirectory, final WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement a = (HTMLAnchorElement)pageElement;
		String urn = a.getAttribute("urn");
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		//获取视图
		View view = null;
		try {
			view = viewDefineService.getView(StringUtils.getPropertyValue(urn, "applicationName"), StringUtils.getPropertyValue(urn, "viewName"), sessionInfo);
		}
		catch(Exception e) {
		
		}
		if(view==null) { //没找到视图,获取在没有访问权限
			a.getParentNode().removeChild(a);
			return;
		}
		String url = null;
		if(view!=null) {
			String actionTitle = StringUtils.getPropertyValue(urn, "title");
			if("查看全部".equals(actionTitle)) {
				//获取宿主页面链接
				Link hostLink = (Link)ListUtils.findObjectByProperty(view.getLinks(), "type", "hostLink"); //获取宿主页面链接
				if(hostLink!=null) { //有宿主页面链接
					url = hostLink.getUrl();
				}
				else if(view.getColumns()==null || view.getColumns().isEmpty()) { //没有配置视图列
					url = null;
				}
				else if(RequestUtils.getRequestURL(request, false).indexOf("/application.shtml")==-1) { //不是应用页面
					url = "/jeaf/application/application.shtml?applicationName=" + view.getApplicationName() + "&viewName=" + view.getName();
				}
				else { //应用页面
					a.setTarget("view");
					url = "{FINAL}" + ViewUtils.getViewURL(view, request);
				}
			}
			else {
				ViewUtils.getViewService(view).retrieveViewActions(view, request, sessionInfo);
				ViewAction viewAction = null;
				for(Iterator iterator = view.getActions()==null ? null : view.getActions().iterator(); viewAction==null && iterator!=null && iterator.hasNext();) {
					Object action = iterator.next();
					if(action instanceof ViewAction) {
						if(actionTitle.equals(((ViewAction)action).getTitle())) {
							viewAction = (ViewAction)action;
						}
					}
					else if(action instanceof ViewActionGroup) {
						viewAction = (ViewAction)ListUtils.findObjectByProperty(((ViewActionGroup)action).getActions(), "title", actionTitle);
					}
				}
				if(viewAction!=null) {
					url = "javascript:" + viewAction.getExecute();
				}
			}
		}
		if(url==null) { //没有找到操作
			a.getParentNode().removeChild(a);
			return;
		}
		a.removeAttribute("id");
		a.removeAttribute("urn");
		Object record = sitePage.getAttribute("record");
		
		//输出链接
		LinkUtils.writeLink(a, url, a.getTarget(), webDirectory.getId(), parentSite.getId(), record, false, true, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#createStaticPageRegenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}

	/**
	 * @return the viewDefineService
	 */
	public ViewDefineService getViewDefineService() {
		return viewDefineService;
	}

	/**
	 * @param viewDefineService the viewDefineService to set
	 */
	public void setViewDefineService(ViewDefineService viewDefineService) {
		this.viewDefineService = viewDefineService;
	}
}
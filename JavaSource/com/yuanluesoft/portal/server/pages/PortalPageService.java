package com.yuanluesoft.portal.server.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback;
import com.yuanluesoft.jeaf.htmlparser.model.StyleDefine;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.portal.container.internal.MarkupResponse;
import com.yuanluesoft.portal.container.internal.PortletURLGenerator;
import com.yuanluesoft.portal.container.internal.PortletWindow;
import com.yuanluesoft.portal.container.service.PortletContainer;
import com.yuanluesoft.portal.server.model.Portal;
import com.yuanluesoft.portal.server.model.PortalPage;
import com.yuanluesoft.portal.server.model.PortletInstance;
import com.yuanluesoft.portal.server.service.PortalService;
import com.yuanluesoft.portal.server.util.PortletUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PortalPageService extends PageService {
	private HTMLParser htmlParser; //HTML解析器
	private PortalService portalService; //PORTAL定制服务
	private PortletContainer portletContainer; //Portlet容器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#writePage(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean)
	 */
	public void writePage(String applicationName, String pageName, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException, IOException {
		if(CookieUtils.getCookie(request, "selectPortalPageId")!=null) {
			CookieUtils.removeCookie(response, "selectPortalPageId", "/", null);
		}
		super.writePage(applicationName, pageName, request, response, editMode);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		if(htmlParser==null) {
			return;
		}
		HTMLDivElement portalPageBody = (HTMLDivElement)template.getElementById("portalPageBody");
		if(portalPageBody==null) { //模板没有配置页面主体
			return;
		}
		
		//引用portal.js
		htmlParser.appendScriptFile(template, request.getContextPath() + "/portal/js/portal.js");
		
		long portalSiteId = sitePage.isInternal() ? -1 : siteId; //后台页面,portalSiteId=-1
		long userId = RequestUtils.getSessionInfo(request).getUserId();
		if("true".equals(request.getParameter("editPortal"))) { //管理员编辑PORTAL
			userId = portalSiteId==-1 ? RequestUtils.getParameterLongValue(request, "orgId") : -1;
		}
		
		long pageId = 0;
		String selectPortalPageId = CookieUtils.getCookie(request, "selectPortalPageId");
		if(selectPortalPageId!=null && !selectPortalPageId.isEmpty()) {
			pageId = Long.parseLong(selectPortalPageId);
		}
		//判断是否预览PORTLET
		boolean previewPortlet = "true".equals(request.getParameter("previewPortlet"));
		//获取PORTAL配置
		Portal portal;
		if(previewPortlet) { //预览portlet
			portal = new Portal();
			PortalPage portalPage = new PortalPage();
			portalPage.setStyle(null); //样式名称
			portalPage.setLayout("1column_100"); //布局,2column_40_60/2column_50_50/2column_60_40/3column_25_25_50/3column_25_50_25/3column_33_33_33/3column_40_30_30/3column_50_25_25/4column_25_25_25_25
			portalPage.setAlwaysDisplayPortletButtons(true); //是否总是显示PORTLET按钮
			portalPage.setTitle(""); //标题
			portalPage.setSelected(true); //是否选中
			PortletInstance portletInstance = new PortletInstance();
			portletInstance.setPortletTitle(request.getParameter("portletTitle")); //标题
			String wsrpProducerId = request.getParameter("wsrpProducerId");
			portletInstance.setWsrpProducerId(wsrpProducerId==null || wsrpProducerId.isEmpty() ? null : wsrpProducerId); //WSRP生产者ID
			portletInstance.setPortletHandle(request.getParameter("portletHandle")); //PORTLET句柄
			portletInstance.setPortletStyle(request.getParameter("portletStyle")); //PORTLET风格
			portletInstance.setColumnIndex(0); //列号
			portletInstance.setState(WindowState.NORMAL.toString()); //状态,minimized|最小化,normal|默认
			portalPage.setPortletInstances(ListUtils.generateList(portletInstance)); //PORTLET实例列表
			portal.setPortalPages(ListUtils.generateList(portalPage));
		}
		else if(pageId==0) { //没有指定PORTAL页面ID
			portal = portalService.loadPortal(applicationName, pageName, userId, portalSiteId, RequestUtils.getSessionInfo(request));
		}
		else { //指定PORTAL页面ID
			portal = portalService.selectPortalPage(applicationName, pageName, userId, portalSiteId, pageId, RequestUtils.getSessionInfo(request));
		}
		sitePage.setAttribute("portal", portal);
		PortalPage currentPortalPage = portal==null ? null : portal.getCurrentPortalPage();
		
		//处理设为“添加页面”按钮(addPortalPageButton)
		writePortalButton(template, "addPortalPageButton", "window.portal.addPage();");
		
		//处理设为“页面定制”按钮(editPortalPageButton)
		writePortalButton(template, "editPortalPageButton", currentPortalPage==null ? "" : "window.portal.editPage();");
		
		//处理设为“添加PORTLET”按钮(addPortletButton)
		writePortalButton(template, "addPortletButton", currentPortalPage==null ? "" :"window.portal.addPortlet();");
		
		String script = "var portalStyles = [";
		//处理样式配置,<meta content="蓝色##/cms/cms/templates/280283342528730000/images/Blue.png##96##74##blue.css$$绿色##/cms/cms/templates/280283342528730000/images/Green.png##96##74##blue.css$$橙色##/cms/cms/templates/280283342528730000/images/Orange.png##96##74##blue.css" name="styleDefineMeta" />
		List styleDefines = htmlParser.parseStyleDefines(htmlParser.getMeta(template, "styleDefineMeta"));
		htmlParser.removeMeta(template, "styleDefineMeta");
		NodeList links = htmlParser.getHTMLHeader(template, false).getElementsByTagName("link");
		String cssFile = null;
		HTMLLinkElement cssLink = null;
		for(int i=0; i<(styleDefines==null ? 0 : styleDefines.size()); i++) {
			StyleDefine styleDefine = (StyleDefine)styleDefines.get(i);
			script += "\r\nnew PortalStyle('" + styleDefine.getStyleName() + "', '" + styleDefine.getIconUrl() + "', '" + styleDefine.getIconWidth() + "', '" + styleDefine.getIconHeight() + "', '" + styleDefine.getCssFileName() + "')" + (i<styleDefines.size()-1 ? "," : "");
			//获取样式表
			for(int j=0; j<(links==null || cssLink!=null ? 0 : links.getLength()); j++) {
				HTMLLinkElement link = (HTMLLinkElement)links.item(j);
				if(link.getHref()!=null && link.getHref().endsWith("/" + styleDefine.getCssFileName())) {
					cssLink = link;
				}
			}
			//获取css文件
			if(currentPortalPage!=null && styleDefine.getStyleName().equals(currentPortalPage.getStyle())) {
				cssFile = styleDefine.getCssFileName();
			}
		}
		if(cssLink!=null && cssFile!=null) {
			cssLink.setHref(cssLink.getHref().substring(0, cssLink.getHref().lastIndexOf('/') + 1) + cssFile);
		}
		script += "];";
		
		//获取portlet列表
		List portletElements = new ArrayList();
		NodeList nodes = template.getElementsByName("portlet");
		script += "\r\nvar portletStyles = [";
		for(int i=0; i<nodes.getLength(); i++) {
			HTMLElement portletElement = (HTMLElement)nodes.item(i);
			portletElements.add(portletElement);
			script += "'" + portletElement.getAttribute("urn") + "'" + (i<nodes.getLength()-1 ? "," : "");
		}
		script += "];";
		
		//处理PORTAL内容区域(portalPageBody)
		createPortalPageBody(applicationName, pageName, userId, portalSiteId, currentPortalPage, template, portalPageBody, portletElements, request);
		
		//添加脚本
		if(!previewPortlet) {
			script += "\r\nwindow.portal = new Portal('" + applicationName + "', '" + pageName + "', '" + userId + "', '" + portalSiteId + "', '" + (currentPortalPage==null ? -1 : currentPortalPage.getId()) + "', portalStyles, portletStyles);";
			htmlParser.appendScript(template, script);
		}
		else { //预览
			//清空body,只保留portalPageBody
			HTMLDivElement div = (HTMLDivElement)template.createElement("div");
			div.setId("portalPageBody");
			div.setAttribute("style", portalPageBody.getAttribute("style"));
			nodes = portalPageBody.getChildNodes();
			Node firstNode = null;
			for(int i=(nodes==null ? -1 : nodes.getLength()-1); i>=0; i--) {
				if(firstNode==null) {
					firstNode = div.appendChild(nodes.item(i));
				}
				else {
					firstNode = div.insertBefore(nodes.item(i), firstNode);
				}
			}
			htmlParser.setTextContent(template.getBody(), null);
			template.getBody().appendChild(div);
		}
	}
	
	/**
	 * 输出按钮
	 * @param template
	 * @param buttonName
	 * @param onclick
	 */
	private void writePortalButton(HTMLDocument template, String buttonName, String onclick) {
		NodeList buttons = template.getElementsByName(buttonName);
		for(int i=0; i<(buttons==null ? -1 : buttons.getLength()); i++) {
			HTMLElement button = (HTMLElement)buttons.item(i);
			button.removeAttribute("id");
			button.removeAttribute("name");
			button.removeAttribute("title");
			button.setAttribute("onclick", onclick);
		}
	}
	
	/**
	 * 创建PORTAL页面主体
	 * @param applicationName
	 * @param pageName
	 * @param userId
	 * @param siteId
	 * @param portalPage
	 * @param template
	 * @param portalPageBody
	 * @param portletElements
	 * @param request
	 */
	private void createPortalPageBody(String applicationName, String pageName, long userId, long siteId, PortalPage portalPage, HTMLDocument template, HTMLElement portalPageBody, List portletElements, HttpServletRequest request) {
		//清空内容区域
		htmlParser.setTextContent(portalPageBody, null);
		portalPageBody.removeAttribute("title");
		if(portalPage==null || portletElements.isEmpty()) {
			return;
		}
		//解析布局
		String layout = portalPage.getLayout();
		if(layout==null || layout.isEmpty()) {
			layout = "3column_33.3_33.4_33.3";
		}
		String[] layoutParameters = layout.split("_");
		int columnCount = Integer.parseInt(layoutParameters[0].replace("column", ""));
		//创建表格
		HTMLTableElement layoutTable = (HTMLTableElement)template.createElement("table");
		layoutTable.setId("layoutTable");
		layoutTable.setBorder("0");
		layoutTable.setCellSpacing("0");
		layoutTable.setCellPadding("0");
		layoutTable.setWidth("100%");
		layoutTable.setAttribute("style", "table-layout: fixed;");
		HTMLTableRowElement tr = (HTMLTableRowElement)layoutTable.insertRow(-1);
		for(int i=0; i<columnCount; i++) {
			HTMLTableCellElement td = (HTMLTableCellElement)tr.insertCell(-1);
			td.setAttribute("width", layoutParameters[1+i] + "%");
			td.setAttribute("class", "portletColumn");
		}
		portalPageBody.appendChild(layoutTable);
		tr.setVAlign("top");
		for(Iterator iterator = (portalPage.getPortletInstances()==null ? null : portalPage.getPortletInstances().iterator()); iterator!=null && iterator.hasNext();) {
			PortletInstance portletInstance = (PortletInstance)iterator.next();
			try {
				writePortlet(applicationName, pageName, userId, siteId, portalPage, portletInstance, template, tr.getCells(), portletElements, request);
			}
			catch (Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 输出PORTLET
	 * @param applicationName
	 * @param pageName
	 * @param userId
	 * @param siteId
	 * @param portalPage
	 * @param portletInstance
	 * @param template
	 * @param layoutColumns
	 * @param portletElements
	 * @param request
	 * @throws Exception
	 */
	private void writePortlet(final String applicationName, final String pageName, final long userId, final long siteId, final PortalPage portalPage, final PortletInstance portletInstance, HTMLDocument template, HTMLCollection layoutColumns, List portletElements, final HttpServletRequest request) throws Exception {
		//获取PORTLET模板
		HTMLElement portletElement = null;
		for(Iterator iterator = portletElements.iterator(); iterator.hasNext();) {
			HTMLElement element = (HTMLElement)iterator.next();
			String portletStyle = element.getAttribute("urn");
			if(portletStyle.equals(portletInstance.getPortletStyle())) {
				portletElement = element;
				break;
			}
		}
		if(portletElement==null) { //没有找到
			portletElement = (HTMLElement)portletElements.get(0); //使用第一个模板
		}
		//使用portlet容器调用portle
		final PortletWindow portletWindow = new PortletWindow("" + portletInstance.getId(), portletInstance.getWsrpProducerId(), portletInstance.getPortletHandle(), new WindowState(portletInstance.getState()), PortletMode.VIEW, userId, siteId, new PortletURLGenerator());
		final MarkupResponse markupResponse = portletContainer.doRender(portletWindow, request);
		//输出portlet
		portletElement = (HTMLElement)portletElement.cloneNode(true);
		htmlParser.traversalChildNodes(portletElement.getChildNodes(), true, new HTMLTraversalCallback() {
			public boolean processNode(Node node) {
				HTMLElement element = (HTMLElement)node;
				if("portletTitle".equals(element.getId())) { //标题
					element.removeAttribute("id");
					htmlParser.setTextContent(element, portletWindow.getPortletTitle()==null || portletWindow.getPortletTitle().isEmpty() ? portletInstance.getPortletTitle() : portletWindow.getPortletTitle());
				}
				else if("portletTitleBar".equals(element.getId())) { //标题栏
					element.removeAttribute("id");
					element.removeAttribute("title");
					htmlParser.setStyle(element, "cursor", "move"); //设置鼠标为“移动”
					element.setAttribute("onselectstart", "return false;");
					element.setAttribute("onmousedown", "window.portal.startDragPortlet(event, this, '" + portletInstance.getId() + "');");
				}
				else if("portletBody".equals(element.getId())) { //内容区域
					element.removeAttribute("title");
					element.setId("portletBody_" + portletInstance.getId());
					//清空PORTLET内容
					htmlParser.setTextContent(element, null);
					HTMLDocument portletDocument = null;
					try {
						portletDocument = PortletUtils.createPortletDocument(markupResponse, "" + portletInstance.getId()); 
					}
					catch(Exception e) {
						Logger.exception(e);
					}
					if(portletDocument!=null) {
						htmlParser.importNodes(portletDocument.getBody().getChildNodes(), element, false);
					}
				}
				else if("portletButtonBar".equals(element.getId())) { //操作按钮栏
					element.removeAttribute("title");
					if(!portalPage.isAlwaysDisplayPortletButtons()) {
						htmlParser.setStyle(element, "display", "none");
					}
					element.setId("portletButtonBar_" + portletInstance.getId());
				}
				else if("portletMinimizeButton".equals(element.getId())) { //“最小化”按钮
					if(markupResponse.getPortletViewURL()==null || markupResponse.getPortletViewURL().isEmpty()) {
						element.getParentNode().removeChild(element);
					}
					else {
						element.setId("portletMinimizeButton_" + portletInstance.getId());
						element.setAttribute("onmousedown", "window.portal.minimizePortlet('" + portletInstance.getId() + "');");
						if(WindowState.MINIMIZED.toString().equals(portletInstance.getState())) { //状态,minimized|最小化,normal|默认
							htmlParser.setStyle(element, "display", "none");
						}
					}
				}
				else if("portletRestoreButton".equals(element.getId())) { //“还原”按钮
					if(markupResponse.getPortletViewURL()==null || markupResponse.getPortletViewURL().isEmpty()) {
						element.getParentNode().removeChild(element);
					}
					else {
						element.setId("portletRestoreButton_" + portletInstance.getId());
						element.setAttribute("onmousedown", "window.portal.restorePortlet('" + portletInstance.getId() + "', '" + markupResponse.getPortletViewURL() + "');");
						if(WindowState.NORMAL.toString().equals(portletInstance.getState())) { //状态,minimize|最小化,normal|默认
							htmlParser.setStyle(element, "display", "none");
						}
					}
				}
				else if("portletEditButton".equals(element.getId())) { //“编辑”按钮
					if(markupResponse.getPortletEditURL()==null || markupResponse.getPortletEditURL().isEmpty()) {
						element.getParentNode().removeChild(element);
					}
					else{
						element.setId("portletEditButton_" + portletInstance.getId());
						element.setAttribute("onmousedown", "window.portal.editPortlet('" + portletInstance.getId() + "', '" + markupResponse.getPortletEditURL() + "');");
						if(WindowState.MINIMIZED.toString().equals(portletInstance.getState())) { //状态,minimize|最小化,normal|默认
							htmlParser.setStyle(element, "display", "none");
						}
					}
				}
				else if("portletRefreshButton".equals(element.getId())) { //“刷新”按钮
					if(markupResponse.getPortletViewURL()==null || markupResponse.getPortletViewURL().isEmpty()) {
						element.getParentNode().removeChild(element);
					}
					else {
						element.setId("portletRefreshButton_" + portletInstance.getId());
						element.setAttribute("onmousedown", "window.portal.refreshPortlet('" + portletInstance.getId() + "', '" + markupResponse.getPortletViewURL() + "');");
						if(WindowState.MINIMIZED.toString().equals(portletInstance.getState())) { //状态,minimize|最小化,normal|默认
							htmlParser.setStyle(element, "display", "none");
						}
					}
				}
				else if("portletRemoveButton".equals(element.getId())) { //“删除”按钮
					element.setId("portletRemoveButton_" + portletInstance.getId());
					element.setAttribute("onmousedown", "window.portal.removePortlet('" + portletInstance.getId() + "');");
				}
				return false;
			}
		});
		//重设ID
		portletElement.setId("portletInstance_" + portletInstance.getId());
		if(!portalPage.isAlwaysDisplayPortletButtons()) { //不总是显示portlet操作按钮
			portletElement.setAttribute("onmouseover", "document.getElementById('portletButtonBar_" + portletInstance.getId() + "').style.display='';");
			portletElement.setAttribute("onmouseout", "document.getElementById('portletButtonBar_" + portletInstance.getId() + "').style.display='none';");
		}
		//清除urn、name和title属性
		portletElement.removeAttribute("urn");
		portletElement.removeAttribute("name");
		portletElement.removeAttribute("title");
		//重置portlet宽度
		htmlParser.setStyle(portletElement, "width", "100%");
		//添加到指定列
		layoutColumns.item(portletInstance.getColumnIndex() % layoutColumns.getLength()).appendChild(portletElement);
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
	 * @return the portalService
	 */
	public PortalService getPortalService() {
		return portalService;
	}

	/**
	 * @param portalService the portalService to set
	 */
	public void setPortalService(PortalService portalService) {
		this.portalService = portalService;
	}

	/**
	 * @return the portletContainer
	 */
	public PortletContainer getPortletContainer() {
		return portletContainer;
	}

	/**
	 * @param portletContainer the portletContainer to set
	 */
	public void setPortletContainer(PortletContainer portletContainer) {
		this.portletContainer = portletContainer;
	}
}
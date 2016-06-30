package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.navigator.Navigator;
import com.yuanluesoft.cms.pagebuilder.model.navigator.NavigatorItem;
import com.yuanluesoft.cms.pagebuilder.model.navigator.NavigatorItemList;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 导航栏处理器
 * @author linchuan
 *
 */
public class NavigatorProcessor implements PageElementProcessor {
	private PageBuilder pageBuilder;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//解析导航栏
		Navigator navigator = (Navigator)BeanUtils.generateBeanByProperties(Navigator.class, pageElement.getAttribute("urn"), null);
		//删除配置的属性
		pageElement.removeAttribute("urn");
		pageElement.removeAttribute("title");
		
		//设置导航栏ID
		String navigatorId = "" + Math.abs(new Random().nextInt()); //导航栏ID
		pageElement.setId("navigator_" + navigatorId);
		
		//添加导航脚本
		htmlParser.appendScriptFile((HTMLDocument)pageElement.getOwnerDocument(), Environment.getContextPath() + "/cms/js/navigator.js");
		
		List navigatorItems = new ArrayList();
		//处理导航栏项目列表
		NodeList elements = ((HTMLDocument)pageElement.getOwnerDocument()).getElementsByName("navigatorItemList_" + navigator.getName());
		for(int i=(elements==null ? -1 : elements.getLength()-1); i>=0; i--) {
			HTMLElement navigatorItemListElement = (HTMLElement)elements.item(i);
			writeNnavigatorItemList(navigator, navigatorItemListElement, navigatorId, navigatorItems, webDirectory, parentSite, htmlParser, sitePage, request);
		}
		
		//处理导航栏链接
		elements = ((HTMLDocument)pageElement.getOwnerDocument()).getElementsByName("navigatorLinkItem_" + navigator.getName());
		for(int i=(elements==null ? -1 : elements.getLength()-1); i>=0; i--) {
			HTMLElement navigatorLinkItemElement = (HTMLElement)elements.item(i);
			writeNnavigatorItem(navigator, navigatorLinkItemElement, false, navigatorId, navigatorItems, webDirectory, parentSite, htmlParser, sitePage, request);
		}
		
		//处理导航栏菜单
		elements = ((HTMLDocument)pageElement.getOwnerDocument()).getElementsByName("navigatorMenuItem_" + navigator.getName());
		for(int i=(elements==null ? -1 : elements.getLength()-1); i>=0; i--) {
			HTMLElement navigatorMenuItemElement = (HTMLElement)elements.item(i);
			writeNnavigatorItem(navigator, navigatorMenuItemElement, true, navigatorId, navigatorItems, webDirectory, parentSite, htmlParser, sitePage, request);
		}
		
		//返回的脚本
		String script = "var navigator_" + navigatorId + " = new Navigator('" + navigatorId + "', '" + navigator.getType() + "', '" + navigator.getLinkItemStyle() + "', '" + navigator.getLinkItemSelectedStyle() + "', '" + navigator.getMenuItemStyle() + "', '" + navigator.getMenuItemSelectedStyle() + "', '" + navigator.getMenuItemDropdownStyle() + "', '" + navigator.getMenuItemSelectedDropdownStyle() + "', '" + navigator.getPopupMenuStyle() + "', '" + navigator.getPopupMenuItemStyle() + "', '" + navigator.getPopupMenuItemSelectedStyle() + "');\r\n";
		for(int i=0; i<navigatorItems.size(); i++) {
			NavigatorItem navigatorItem = (NavigatorItem)navigatorItems.get(i);
			String menuItems = null;
			for(Iterator iterator = (navigatorItem.getMenuItems()!=null ? navigatorItem.getMenuItems().iterator() : null); iterator!=null && iterator.hasNext();) {
				String menuItem = (String)iterator.next();
				menuItems = (menuItems==null ? "" : menuItems + ",") + "'" + menuItem.replaceAll("'", "\\\\'") + "'";
			}
			script += "navigator_" + navigatorId + ".addItem(" + (menuItems==null ? "null" : "[" + menuItems + "]") + ", '" + navigatorItem.getLinkItemStyle() + "', '" + navigatorItem.getLinkItemSelectedStyle() + "', '" + navigatorItem.getMenuItemStyle() + "', '" + navigatorItem.getMenuItemSelectedStyle() + "', '" + navigatorItem.getMenuItemDropdownStyle() + "', '" + navigatorItem.getMenuItemSelectedDropdownStyle() + "');\r\n";
		}
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
		scriptElement.setLang("JavaScript");
		scriptElement.appendChild(pageElement.getOwnerDocument().createTextNode(script));
		((HTMLDocument)pageElement.getOwnerDocument()).getBody().appendChild(scriptElement);
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
	 * 输出导航栏项目列表
	 * @param navigator
	 * @param navigatorItemListElement
	 * @param navigatorId
	 * @param navigatorItems
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	private void writeNnavigatorItemList(Navigator navigator, HTMLElement navigatorItemListElement, String navigatorId, List navigatorItems, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		NavigatorItemList navigatorItemList = (NavigatorItemList)BeanUtils.generateBeanByProperties(NavigatorItemList.class, navigatorItemListElement.getAttribute("urn"), null);
		//构造一个内嵌记录列表,用来输出菜单项目
		RecordList nestedRecordList = new RecordList();
		nestedRecordList.setExtendProperties(navigatorItemList.getExtendProperties());
		nestedRecordList.setRecordListName(navigatorItemList.getRecordListName());
		nestedRecordList.setApplicationName(navigatorItemList.getApplicationName());
		nestedRecordList.setRecordCount(100); //最多显示100个菜单项
		nestedRecordList.setLinkOpenMode(navigatorItemList.getLinkOpenMode());
		nestedRecordList.setLinkTitle(navigatorItemList.getLinkTitle());
		nestedRecordList.setSeparatorMode("blank");
		String recordFormat = "<div id=\"navigatorMenuItem\">" + navigatorItemList.getItemFormat() + "</div>";
		nestedRecordList.setRecordFormat(recordFormat);
		
		//构造一个记录列表,用来输出导航栏项目
		RecordList recordList = new RecordList();
		recordList.setExtendProperties(navigatorItemList.getExtendProperties());
		recordList.setRecordListName(navigatorItemList.getRecordListName());
		recordList.setApplicationName(navigatorItemList.getApplicationName());
		recordList.setRecordCount(navigatorItemList.getItemCount());
		recordList.setLinkOpenMode(navigatorItemList.getLinkOpenMode());
		recordList.setLinkTitle(navigatorItemList.getLinkTitle());
		recordList.setSeparatorMode("blank");
		recordFormat = "<div id=\"navigatorRecord\">" +
					   "<div>" + navigatorItemList.getItemFormat() + "</div>" + //显示格式
					   "<div>" + RecordListUtils.gernerateRecordListElement(nestedRecordList, false) + "</div>" + //内嵌记录列表
					   "</div>";
		recordList.setRecordFormat(recordFormat);
		
		//解析记录列表
		HTMLDocument recordListDocument = htmlParser.parseHTMLString(RecordListUtils.gernerateRecordListElement(recordList, false), "utf-8");
		//处理记录列表
		pageBuilder.processPageElement(recordListDocument.getBody(), webDirectory, parentSite, sitePage, RequestUtils.getRequestInfo(request), request);
		
		//输出导航栏列表
		boolean firstItem = true;
		for(HTMLElement navigatorRecord = (HTMLElement)recordListDocument.getElementById("navigatorRecord"); navigatorRecord!=null; navigatorRecord=(HTMLElement)navigatorRecord.getNextSibling()) {
			if(!"navigatorRecord".equals(navigatorRecord.getId())) {
				continue;
			}
			if(!firstItem && navigatorItemList.getItemSpacing()>0) { //有设置间距并且不是第一个条目
				//插入分隔DIV
				HTMLElement div = (HTMLElement)navigatorItemListElement.getOwnerDocument().createElement("div");
				if(navigator.getType().startsWith("horizontal")) { //水平的
					div.setAttribute("style", "width:" + navigatorItemList.getItemSpacing() + "px; float:left");
				}
				else { //垂直的
					div.setAttribute("style", "height:" + navigatorItemList.getItemSpacing() + "px; float:none");
				}
				navigatorItemListElement.getParentNode().insertBefore(div, navigatorItemListElement);
			}
			firstItem = false;
			
			//处理菜单项列表
			NodeList navigatorChildNodes = navigatorRecord.getChildNodes();
			List menuItems = new ArrayList();
			NodeList divs = ((HTMLElement)navigatorChildNodes.item(1)).getElementsByTagName("div");
			for(int i=0; i<divs.getLength(); i++) {
				HTMLDivElement navigatorMenuItem = (HTMLDivElement)divs.item(i);
				if("navigatorMenuItem".equals(navigatorMenuItem.getId())) {
					String innerHTML = htmlParser.getElementInnerHTML(navigatorMenuItem, "utf-8");
					if(innerHTML!=null && !innerHTML.equals("")) {
						menuItems.add(innerHTML);
					}
				}
			}
			
			//创建导航栏项目
			HTMLElement navigatorItemDiv = (HTMLElement)navigatorItemListElement.getOwnerDocument().createElement("div");
			navigatorItemListElement.getParentNode().insertBefore(navigatorItemDiv, navigatorItemListElement);
			//设置样式
			String style = "float:" + (navigator.getType().startsWith("horizontal") ? "left" : "none") + ";"; //水平导航栏,float=left
			if(menuItems.isEmpty()) { //没有菜单
				style += navigatorItemList.getLinkItemStyle()==null || navigatorItemList.getLinkItemStyle().isEmpty() ? navigator.getLinkItemStyle() : navigatorItemList.getLinkItemStyle();
			}
			else { //有菜单
				style += navigatorItemList.getMenuItemStyle()==null || navigatorItemList.getMenuItemStyle().isEmpty() ? navigator.getMenuItemStyle() : navigatorItemList.getMenuItemStyle();
			}
			navigatorItemDiv.setAttribute("style", style);
			navigatorItemDiv.setId("navigatorItem_" + navigatorId + "_" + navigatorItems.size());
			//引入条目内容
			HTMLElement navigatorItemContent = (HTMLElement)navigatorItemListElement.getOwnerDocument().createElement("div");
			navigatorItemContent.setAttribute("style", "float:left;");
			navigatorItemDiv.appendChild(navigatorItemContent);
			htmlParser.importNodes(navigatorChildNodes.item(0).getChildNodes(), navigatorItemContent, false);
			
			if(!menuItems.isEmpty()) { //有菜单项
				//输出菜单标记
				HTMLElement dropdownFlag = (HTMLElement)navigatorItemListElement.getOwnerDocument().createElement("div");
				dropdownFlag.setId(navigatorItemDiv.getId() + "_dropdown");
				dropdownFlag.setAttribute("style", "float:left; font-size:1px;" + (navigatorItemList.getMenuItemDropdownStyle()==null || navigatorItemList.getMenuItemDropdownStyle().isEmpty() ? navigator.getMenuItemDropdownStyle() : navigatorItemList.getMenuItemDropdownStyle()));
				navigatorItemDiv.appendChild(dropdownFlag);
				
				//导航栏类型是下拉列表时,创建菜单DIV用来显示菜单
				if("verticalDropdown".equals(navigator.getType())) {
				 	HTMLElement div = (HTMLElement)navigatorItemListElement.getOwnerDocument().createElement("div");
					div.setId(navigatorItemDiv.getId() + "_menu");
					div.setAttribute("style", "height:0px; font-size:0px; float:none; display:none;");
					navigatorItemListElement.getParentNode().insertBefore(div, navigatorItemListElement);
				}
			}
			//添加到导航项目列表
			navigatorItems.add(new NavigatorItem(navigatorItemList.getLinkItemStyle(), navigatorItemList.getLinkItemSelectedStyle(), navigatorItemList.getMenuItemStyle(), navigatorItemList.getMenuItemSelectedStyle(), navigatorItemList.getMenuItemDropdownStyle(), navigatorItemList.getMenuItemSelectedDropdownStyle(), menuItems));
		}
		//删除配置元素
		navigatorItemListElement.getParentNode().removeChild(navigatorItemListElement);
	}
	
	/**
	 * 输出项目
	 * @param navigator
	 * @param navigatorItemElement
	 * @param menuItem 是否菜单
	 * @param navigatorId
	 * @param navigatorItems
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @throws ServiceException
	 */
	private void writeNnavigatorItem(Navigator navigator, HTMLElement navigatorItemElement, boolean menuItem, String navigatorId, List navigatorItems, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String urn = navigatorItemElement.getAttribute("urn");
		//解析项目
		NavigatorItem navigatorItem = (NavigatorItem)BeanUtils.generateBeanByProperties(NavigatorItem.class, urn, null);
		//删除属性
		navigatorItemElement.removeAttribute("id");
		navigatorItemElement.removeAttribute("urn");
		navigatorItemElement.removeAttribute("title");
		navigatorItemElement.removeAttribute("name");
		
		//创建导航栏项目
		HTMLElement navigatorItemDiv = (HTMLElement)navigatorItemElement.getOwnerDocument().createElement("div");
		navigatorItemDiv.setId("navigatorItem_" + navigatorId + "_" + navigatorItems.size());
		//设置样式
		String style = "float:" + (navigator.getType().startsWith("horizontal") ? "left" : "none") + ";"; //水平导航栏,float=left
		if(!menuItem) { //不是菜单
			style += navigatorItem.getLinkItemStyle()==null || navigatorItem.getLinkItemStyle().isEmpty() ? navigator.getLinkItemStyle() : navigatorItem.getLinkItemStyle();
		}
		else { //菜单
			style += navigatorItem.getMenuItemStyle()==null || navigatorItem.getMenuItemStyle().isEmpty() ? navigator.getMenuItemStyle() : navigatorItem.getMenuItemStyle();
		}
		navigatorItemDiv.setAttribute("style", style);
		//把navigatorItemElement内部的所有元素迁移到navigatorItemDiv
		NodeList nodes = navigatorItemElement.getChildNodes();
		if(nodes!=null && nodes.getLength()>0) {
			HTMLElement navigatorItemContent = (HTMLElement)navigatorItemElement.getOwnerDocument().createElement("div");
			navigatorItemContent.setAttribute("style", "float:left;");
			navigatorItemDiv.appendChild(navigatorItemContent);
			for(int i=0; i<nodes.getLength(); i++) {
				navigatorItemContent.appendChild(nodes.item(i).cloneNode(true));
			}
			htmlParser.setTextContent(navigatorItemElement, null);
		}
		//添加导航栏项目
		navigatorItemElement.appendChild(navigatorItemDiv);
		
		if(menuItem) {
			//构造一个列表,用来输出菜单项目
			String html = "";
			for(int i=0; i<100; i++) {
				String linkTitle = StringUtils.getPropertyValue(urn, "link" + i + "Title");
				if(linkTitle==null || linkTitle.isEmpty()) {
					break;
				}
				html += "<div id=\"navigatorMenuItem\" title=\"" + linkTitle + "\">" + StringUtils.getPropertyValue(urn, "link" + i) + "</div>";
			}
			//解析HTML
			HTMLDocument menuItemsDocument = htmlParser.parseHTMLString(html, "utf-8");
			//处理菜单项列表
			pageBuilder.processPageElement(menuItemsDocument.getBody(), webDirectory, parentSite, sitePage, RequestUtils.getRequestInfo(request), request);
			navigatorItem.setMenuItems(new ArrayList());
			NodeList divs = menuItemsDocument.getElementsByTagName("div");
			for(int i=0; i<divs.getLength(); i++) {
				HTMLDivElement div = (HTMLDivElement)divs.item(i);
				if(!"navigatorMenuItem".equals(div.getId())) {
					continue;
				}
				NodeList anchors = div.getElementsByTagName("a");
				if(anchors==null || anchors.getLength()==0) {
					continue;
				}
				HTMLAnchorElement anchorElement = (HTMLAnchorElement)anchors.item(0);
				htmlParser.setTextContent(anchorElement, div.getTitle());
				navigatorItem.getMenuItems().add(htmlParser.getElementHTML(anchorElement, "utf-8"));
			}
			//输出菜单标记
			HTMLElement dropdownFlag = (HTMLElement)navigatorItemElement.getOwnerDocument().createElement("div");
			dropdownFlag.setId(navigatorItemDiv.getId() + "_dropdown");
			dropdownFlag.setAttribute("style", "float:left; font-size:1px;" + (navigatorItem.getMenuItemDropdownStyle()==null || navigatorItem.getMenuItemDropdownStyle().isEmpty() ? navigator.getMenuItemDropdownStyle() : navigatorItem.getMenuItemDropdownStyle()));
			navigatorItemDiv.appendChild(dropdownFlag);
			
			//导航栏类型是下拉列表时,创建菜单DIV用来显示菜单
			if("verticalDropdown".equals(navigator.getType())) {
			 	HTMLElement div = (HTMLElement)navigatorItemElement.getOwnerDocument().createElement("div");
				div.setId(navigatorItemDiv.getId() + "_menu");
				div.setAttribute("style", "height:0px; font-size:0px; float:none; display:none;");
				navigatorItemElement.appendChild(div);
			}
		}
		//添加到导航项目列表
		navigatorItems.add(navigatorItem);
	}
	
	/**
	 * @return the pageBuilder
	 */
	public PageBuilder getPageBuilder() {
		return pageBuilder;
	}

	/**
	 * @param pageBuilder the pageBuilder to set
	 */
	public void setPageBuilder(PageBuilder pageBuilder) {
		this.pageBuilder = pageBuilder;
	}
}
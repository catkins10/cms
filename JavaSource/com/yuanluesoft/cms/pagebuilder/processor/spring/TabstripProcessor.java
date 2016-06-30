package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.tabstrip.TabstripBody;
import com.yuanluesoft.cms.pagebuilder.model.tabstrip.TabstripButton;
import com.yuanluesoft.cms.pagebuilder.model.tabstrip.TabstripButtonList;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class TabstripProcessor implements PageElementProcessor {
	private PageBuilder pageBuilder;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//解析TAB页属性
		TabstripBody tabstripBody = (TabstripBody)BeanUtils.generateBeanByProperties(TabstripBody.class, pageElement.getAttribute("urn"), null);
		
		//处理TAB选项
		NodeList elements = ((HTMLDocument)pageElement.getOwnerDocument()).getElementsByName("tabstripButton_" + tabstripBody.getName());
		if(elements==null || elements.getLength()==0) { //没有TAB选项
			pageElement.getParentNode().removeChild(pageElement);
			return;
		}
		
		List tabstripButtonElements = new ArrayList();
		for(int i=0; i<elements.getLength(); i++) {
			tabstripButtonElements.add(elements.item(i));
		}
		List tabstripButtons = new ArrayList();
		String tabstripId = "" + Math.abs(new Random().nextInt()); //TAB标签页ID
		//给每个TAB选项增加一个DIV,显示对应的内容,第一个显示,其他的隐藏
		for(Iterator iterator = tabstripButtonElements.iterator(); iterator.hasNext();) {
			HTMLElement element = (HTMLElement)iterator.next();
			if(element.getId().startsWith("tabstripButtonList_")) { //TAB选项列表
				writeTabstripButtonList(tabstripBody, pageElement, element, tabstripId, tabstripButtons, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
			}
			else { //TAB选项
				writeTabstripButton(tabstripBody, pageElement, element, tabstripId, tabstripButtons, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
			}
		}
		
		//返回的脚本
		String script = "var tabstrip_" + tabstripId + " = new Tabstrip('" + tabstripId + "', '" + tabstripBody.getSwitchMode() + "', " + tabstripBody.isTimeSwitch() + ", '" + tabstripBody.getTimeInterval() + "', " + tabstripBody.isClickOpenMore() + ");\r\n" ;
		for(int i=0; i<tabstripButtons.size(); i++) {
			TabstripButton tabstripButton = (TabstripButton)tabstripButtons.get(i);
			script += "tabstrip_" + tabstripId + ".addPage('" + tabstripButton.getMouseOverStyle() + "', '" + tabstripButton.getSelectedStyle() + "', '" + tabstripButton.getUnselectedStyle() + "');\r\n";
		}
		
		//处理“更多”
		HTMLElement tabstripMoreLink = (HTMLElement)pageElement.getOwnerDocument().getElementById("tabstripMoreLink_" + tabstripBody.getName());
		if(tabstripMoreLink!=null) {
			tabstripMoreLink.setId("tabstripMoreLink_" + tabstripId);
			tabstripMoreLink.removeAttribute("urn");
			script += "tabstrip_" + tabstripId + ".setMoreLink();\r\n";
		}
		
		//添加脚本
		htmlParser.appendScriptFile((HTMLDocument)pageElement.getOwnerDocument(), Environment.getContextPath() + "/cms/js/tabstrip.js");
		HTMLScriptElement scriptElement = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
		scriptElement.setLang("JavaScript");
		scriptElement.appendChild(pageElement.getOwnerDocument().createTextNode(script));
		((HTMLDocument)pageElement.getOwnerDocument()).getBody().appendChild(scriptElement);
		//删除配置元素
		pageElement.getParentNode().removeChild(pageElement);
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
	 * 输出单个TAB选项
	 * @param tabstripBody
	 * @param tabstripBodyElement
	 * @param tabstripButtonElement
	 * @param tabstripId
	 * @param tabstripButtons
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	private void writeTabstripButton(TabstripBody tabstripBody, HTMLElement tabstripBodyElement, HTMLElement tabstripButtonElement, String tabstripId, List tabstripButtons, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		TabstripButton tabstripButton = (TabstripButton)BeanUtils.generateBeanByProperties(TabstripButton.class, tabstripButtonElement.getAttribute("urn"), null);
		
		//重设TAB选项的属性
		tabstripButtonElement.setId("tabstripButton_" + tabstripId + "_" + tabstripButtons.size());
		tabstripButtonElement.removeAttribute("urn");
		tabstripButtonElement.removeAttribute("name");
		tabstripButtonElement.removeAttribute("title");
		
		String format = "<div>" + tabstripButton.getTabstripBody() + "</div>" + //TAB页面格式
						"<div>" + tabstripButton.getMoreLink() + "</div>"; //more链接格式;
		//解析记录列表
		HTMLDocument tabstripDocument = htmlParser.parseHTMLString(format, "utf-8");
		pageBuilder.processPageElement(tabstripDocument.getBody(), webDirectory, parentSite, sitePage, requestInfo, request);
		//输出TAB页面和more链接
		NodeList childNodes = tabstripDocument.getBody().getChildNodes();
		writeTabstripPage(tabstripBody, tabstripBodyElement, tabstripId, tabstripButtons.size(), (HTMLElement)childNodes.item(0), (HTMLElement)childNodes.item(1), htmlParser);
		//添加到按钮列表
		tabstripButtons.add(tabstripButton);
	}
	
	/**
	 * 输出TAB选项列表
	 * @param tabstripBody
	 * @param tabstripBodyElement
	 * @param tabstripButtonListElement
	 * @param tabstripId
	 * @param tabstripButtons
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	private void writeTabstripButtonList(TabstripBody tabstripBody, HTMLElement tabstripBodyElement, HTMLElement tabstripButtonListElement, String tabstripId, List tabstripButtons, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		TabstripButtonList tabstripButtonList;
		try {
			tabstripButtonList = (TabstripButtonList)BeanUtils.generateBeanByProperties(TabstripButtonList.class, tabstripButtonListElement.getAttribute("urn"), null);
		} 
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		
		//构造一个记录列表
		RecordList recordList = new RecordList();
		recordList.setExtendProperties(tabstripButtonList.getExtendProperties());
		recordList.setRecordListName(tabstripButtonList.getRecordListName());
		recordList.setApplicationName(tabstripButtonList.getApplicationName());
		recordList.setRecordCount(tabstripButtonList.getButtonCount());
		recordList.setSeparatorMode("blank");
		String recordFormat = "<div id=\"tabstripRecord\">" +
							  	"<div>" + tabstripButtonList.getTabstripButton() + "</div>" + //按钮格式
							  	"<div>" + tabstripButtonList.getTabstripBody() + "</div>" + //TAB页面格式
							  	"<div>" + tabstripButtonList.getMoreLink() + "</div>" + //more链接格式;
							  "</div>";
		recordList.setRecordFormat(recordFormat);
		//解析记录列表
		HTMLDocument recordListDocument = htmlParser.parseHTMLString(RecordListUtils.gernerateRecordListElement(recordList, false), "utf-8");
		//处理记录列表
		pageBuilder.processPageElement(recordListDocument.getBody(), webDirectory, parentSite, sitePage, requestInfo, request);
		boolean firstTab = true;
		for(HTMLElement tabstripRecord = (HTMLElement)recordListDocument.getElementById("tabstripRecord"); tabstripRecord!=null; tabstripRecord=(HTMLElement)tabstripRecord.getNextSibling()) {
			if(!"tabstripRecord".equals(tabstripRecord.getId())) {
				continue;
			}
			if(!firstTab && tabstripButtonList.getButtonSpacing()>0) {
				//插入分隔SPAN
				HTMLElement span = (HTMLElement)tabstripButtonListElement.getOwnerDocument().createElement("span");
				span.setAttribute("style", "width:" + tabstripButtonList.getButtonSpacing() + "px");
				tabstripButtonListElement.getParentNode().insertBefore(span, tabstripButtonListElement);
			}
			firstTab = false;
			
			//创建选项span
			HTMLElement tabButtonSpan = (HTMLElement)tabstripButtonListElement.getOwnerDocument().createElement("span");
			tabstripButtonListElement.getParentNode().insertBefore(tabButtonSpan, tabstripButtonListElement);
			if(tabstripButtonList.getUnselectedStyle()!=null) {
				tabButtonSpan.setAttribute(tabstripButtonList.getUnselectedStyle().indexOf(':')==-1 ? "class" : "style", tabstripButtonList.getUnselectedStyle());
			}
			tabButtonSpan.setId("tabstripButton_" + tabstripId + "_" + tabstripButtons.size());
			//输出TAB按钮
			NodeList childNodes = tabstripRecord.getChildNodes();
			htmlParser.importNodes(childNodes.item(0).getChildNodes(), tabButtonSpan, false);
			
			//输出TAB页面和more链接
			writeTabstripPage(tabstripBody, tabstripBodyElement, tabstripId, tabstripButtons.size(), (HTMLElement)childNodes.item(1), (HTMLElement)childNodes.item(2), htmlParser);
			
			//添加到按钮列表
			tabstripButtons.add(tabstripButtonList);
		}
		//删除配置元素
		tabstripButtonListElement.getParentNode().removeChild(tabstripButtonListElement);
	}
	
	/**
	 * 输出TAB页
	 * @param tabstripBody
	 * @param tabstripBodyElement
	 * @param tabstripId
	 * @param tabstripButton
	 * @param tabstripButton
	 * @param tabstripButtonElement
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @param editMode
	 * @param isGenerateStaticPage
	 * @return
	 * @throws ServiceException
	 */
	private void writeTabstripPage(TabstripBody tabstripBody, HTMLElement tabstripBodyElement, String tabstripId, int tabIndex, HTMLElement tabstripPageElement, HTMLElement tabstripMoreElement, HTMLParser htmlParser) throws ServiceException {
		//创建DIV,放置TAB页面
		HTMLDivElement tabstripBodyDiv = (HTMLDivElement)tabstripBodyElement.getOwnerDocument().createElement("div");
		tabstripBodyDiv.setId("tabstripBody_" + tabstripId + "_" + tabIndex); //ID
		//设置样式
		String style = (tabIndex==0 ? "" : "display:none;"); //第一个显示
		if(!"自动".equals(tabstripBody.getWidth()) && !"".equals(tabstripBody.getWidth())) {
			style += "width:" + tabstripBody.getWidth() + ";";
		}
		if(!"自动".equals(tabstripBody.getHeight()) && !"".equals(tabstripBody.getHeight())) {
			style += "height:" + tabstripBody.getHeight() + ";";
		}
		if(!style.equals("")) {
			tabstripBodyDiv.setAttribute("style", style);
		}
		tabstripBodyElement.getParentNode().insertBefore(tabstripBodyDiv, tabstripBodyElement); //插入DIV
		htmlParser.importNodes(tabstripPageElement.getChildNodes(), tabstripBodyDiv, false);
		
		//输出“更多”链接,复制tabMoreNodes的第一个链接
		NodeList nodes = tabstripMoreElement.getElementsByTagName("A");
		if(nodes!=null && nodes.getLength()>0) {
			HTMLDivElement divMore = (HTMLDivElement)tabstripBodyElement.getOwnerDocument().createElement("div");
			divMore.setAttribute("style", "display:none");
			divMore.setId("tabstripMore_" + tabstripId + "_" + tabIndex); //ID
			tabstripBodyElement.getParentNode().insertBefore(divMore, tabstripBodyElement);
			//插入链接
			divMore.appendChild(tabstripBodyElement.getOwnerDocument().importNode(nodes.item(0), true));
		}
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
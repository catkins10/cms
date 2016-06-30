package com.yuanluesoft.jeaf.client.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;

/**
 * 
 * @author linchuan
 *
 */
public class ClientFilePageService extends ClientPageService {
	private HTMLParser htmlParser; //HTML解析器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, final SitePage sitePage, final long siteId, final HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//获取确定按钮
		HTMLElement okButton = (HTMLElement)template.getElementById("okButton");
		okButton.setAttribute("onclick", "window.top.fileSystem.onOK();");
		okButton.removeAttribute("title");
		
		//获取新建文件夹按钮
		HTMLElement newFolderButton = (HTMLElement)template.getElementById("newFolderButton");
		if(newFolderButton!=null) {
			newFolderButton.removeAttribute("title");
			newFolderButton.setAttribute("onclick", "window.top.fileSystem.createNewFolder();");
		}
		
		//获取文件列表区域
		HTMLElement fileListArea = (HTMLElement)template.getElementById("fileListArea");
		//把所有的子元素移入fileRecord span
		HTMLElement fileRecord = (HTMLElement)template.createElement("span");
		fileRecord.setId("fileRecord");
		fileRecord.setAttribute("style", "display: none");
		htmlParser.moveChildNodes(fileListArea, fileRecord);
		fileListArea.appendChild(fileRecord);
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
}
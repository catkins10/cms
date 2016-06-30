package com.yuanluesoft.im.webim.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.im.model.message.ChatDetail;
import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.im.webim.model.WebimChat;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * WEBIM对话页面服务
 * @author linchuan
 *
 */
public class WebimChatPageService extends PageService {
	private IMService imService; //IM服务
	private HTMLParser htmlParser; //HTML解析器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//获取发言输入框,转换为IFRAME
		NodeList elements = template.getElementsByName("content");
		if(elements!=null && elements.getLength()>0) {
			HTMLInputElement contentInput = (HTMLInputElement)elements.item(0);
			HTMLIFrameElement contentIframe = (HTMLIFrameElement)template.createElement("iframe");
			contentIframe.setName("content");
			contentIframe.setId("content");
			contentIframe.setClassName(contentInput.getClassName());
			contentIframe.setAttribute("style", contentInput.getAttribute("style"));
			contentIframe.setFrameBorder("0");
			contentIframe.setScrolling("yes");
			contentIframe.setAttribute("allowtransparency", "true");
			contentInput.getParentNode().replaceChild(contentIframe, contentInput);
		}
		//处理设置字体操作按钮
		HTMLElement fontSettingAction = (HTMLElement)template.getElementById("fontSettingAction");
		if(fontSettingAction!=null) {
			fontSettingAction.setAttribute("onclick", "window.webimChat.showBox('fontSettingBox')");
		}
		//处理字体设置框
		HTMLElement fontSettingBox = (HTMLElement)template.getElementById("fontSettingBox");
		if(fontSettingBox!=null) {
			hideHtmlElement(fontSettingBox); //设为隐藏
			if(fontSettingBox.getTagName().equalsIgnoreCase("TD")) {
				hideHtmlElement((HTMLElement)fontSettingBox.getParentNode());
			}
		}
		//处理插入表情操作按钮
		HTMLElement insertExpressionAction = (HTMLElement)template.getElementById("insertExpressionAction");
		if(insertExpressionAction!=null) {
			insertExpressionAction.setAttribute("onclick", "window.webimChat.showBox('expressionBox')");
		}
		//处理表情框
		HTMLElement expressionBox = (HTMLElement)template.getElementById("expressionBox");
		if(expressionBox!=null) {
			hideHtmlElement(expressionBox); //设为隐藏
			if(expressionBox.getTagName().equalsIgnoreCase("TD")) {
				hideHtmlElement((HTMLElement)expressionBox.getParentNode());
			}
			expressionBox.setAttribute("onclick", "window.webimChat.insertExpressionImage(event);");
		}
		//处理文件上传提示框
		HTMLElement fileUploadingBox = (HTMLElement)template.getElementById("fileUploadingBox");
		if(fileUploadingBox!=null) {
			hideHtmlElement(fileUploadingBox);
		}
		//处理添加用户按钮
		HTMLElement addPersonAction = (HTMLElement)template.getElementById("addPersonAction");
		if(addPersonAction!=null) {
			addPersonAction.setAttribute("onclick", "window.webimChat.addChatPerson();");
		}
		//添加脚本引用
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/im/webim/js/webimChat.js");
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/jeaf/filetransfer/js/fileuploadclient.js");
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/jeaf/attachment/js/attachmentuploader.js");
		HTMLScriptElement scriptElement = (HTMLScriptElement)template.createElement("script");
		scriptElement.setLang("JavaScript");
		scriptElement.appendChild(template.createTextNode("new WebimChat('" + request.getParameter("chatId") + "');"));
		template.getBody().appendChild(scriptElement);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
		long chatId = RequestUtils.getParameterLongValue(request, "chatId");
		ChatDetail chat;
		if("chatOfCustomer".equals(pageName)) {
			chat = imService.loadChat(chatId, chatId);
		}
		else {
			SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
			chat = imService.loadChat(sessionInfo.getUserId(), chatId);
		}
		WebimChat webimChat = new WebimChat();
		try {
			PropertyUtils.copyProperties(webimChat, chat);
		}
		catch(Exception e) {
			
		}
		sitePage.setAttribute("record", webimChat);
	}
	
	/**
	 * 隐藏HTML元素
	 * @param htmlElement
	 * @throws ServiceException
	 */
	private void hideHtmlElement(HTMLElement htmlElement) throws ServiceException {
		String style = StringUtils.removeStyles(htmlElement.getAttribute("style"), "display") ;
		htmlElement.setAttribute("style", (style==null || style.isEmpty() ? "" : style + ";") + "display:none"); //设为隐藏
	}

	/**
	 * @return the imService
	 */
	public IMService getImService() {
		return imService;
	}

	/**
	 * @param imService the imService to set
	 */
	public void setImService(IMService imService) {
		this.imService = imService;
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
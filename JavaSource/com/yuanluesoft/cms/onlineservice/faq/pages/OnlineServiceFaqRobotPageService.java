package com.yuanluesoft.cms.onlineservice.faq.pages;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLIFrameElement;

import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq;
import com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.model.search.Condition;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceFaqRobotPageService extends PageService {
	private HTMLParser htmlParser; //HTML解析器
	private OnlineServiceFaqService onlineServiceFaqService; //常见问题服务
	private OnlineServiceDirectoryService onlineServiceDirectoryService; //网上办事目录服务
	private ViewDefineService viewDefineService; //视图定义服务
	private ViewService viewService; //视图服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//获取提示信息区域/promptArea
		HTMLElement promptArea = (HTMLElement)template.getElementById("promptArea");
		String action = request.getParameter("action");
		if("retrieveQuestionPrompt".equals(action)) { //获取问题提示
			//设置主目录ID,用于获取热点问题
			setMainDirectoryId(siteId, request);
			
			//获取记录列表
			HTMLElement element  = htmlParser.getElementById(promptArea, "a", "recordList");
			RecordList recordList = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, element.getAttribute("urn"), null);
			recordList.setSearchResults(true); //设为搜索结果
			RecordListUtils.setRecordListProperties(element, recordList);
			
			//清空body,把记录列表加入到body
			htmlParser.setTextContent(template.getBody(), null);
			template.getBody().appendChild(template.createTextNode("RELATION_FAQS_BEGIN"));
			template.getBody().appendChild(element);
			template.getBody().appendChild(template.createTextNode("RELATION_FAQS_END"));
			
			//删除header
			HTMLHeadElement header = htmlParser.getHTMLHeader(template, false);
			if(header!=null) {
				header.getParentNode().removeChild(header);
			}
		}
		else if("sendQuestion".equals(action)) { //提交问题
			//删除提示信息区域
			promptArea.getParentNode().removeChild(promptArea);
			
			//获取发言记录
			HTMLElement robotTalkRecord = (HTMLElement)template.getElementById("robotTalkRecord");
			HTMLElement questionerTalkRecord = (HTMLElement)template.getElementById("questionerTalkRecord");
			
			//获取相关问题列表
			HTMLElement element = getRelationFaqsRecordList(template);
			
			//重置BODY
			htmlParser.setTextContent(template.getBody(), null);
			template.getBody().appendChild(template.createTextNode("RELATION_FAQS_BEGIN"));
			template.getBody().appendChild(element);
			template.getBody().appendChild(template.createTextNode("RELATION_FAQS_END"));

			//输出发言
			template.getBody().appendChild(template.createTextNode("TALK_RECORDS_BEGIN"));
			generateTalkRecords(robotTalkRecord, questionerTalkRecord, siteId, request);
			template.getBody().appendChild(template.createTextNode("TALK_RECORDS_END"));
			
			//删除header
			HTMLHeadElement header = htmlParser.getHTMLHeader(template, false);
			if(header!=null) {
				header.getParentNode().removeChild(header);
			}
		}
		else {
			//设置主目录ID,用于获取热点问题
			setMainDirectoryId(siteId, request);
			
			//引用脚本
			htmlParser.appendScriptFile(template, Environment.getContextPath() + "/cms/onlineservice/faq/js/robot.js");
			
			//处理机器发言记录/robotTalkRecord、
			HTMLElement element = (HTMLElement)template.getElementById("robotTalkRecord");
			((HTMLElement)element.getParentNode()).setId("talkRecordArea"); //设为聊天记录区域
			//获取问候语/greeting
			String greeting = htmlParser.getMeta(template, "greeting");
			htmlParser.removeMeta(template, "greeting");
			if(greeting!=null && !greeting.isEmpty()) { //有问候语
				try {
					writeTalkRecord(URLDecoder.decode(greeting, "utf-8"), true, (HTMLElement)element.getParentNode(), element);
				}
				catch (UnsupportedEncodingException e) {
					Logger.exception(e);
				}
			}
			element.getParentNode().removeChild(element);
			
			//删除用户发言记录/questionerTalkRecord
			element = (HTMLElement)template.getElementById("questionerTalkRecord");
			element.getParentNode().removeChild(element);
			
			//处理提示信息区域/promptArea
			String style = promptArea.getAttribute("style");
			promptArea.setAttribute("style", (style==null || style.isEmpty() ? "" : style + ";") + "display:none;");
			//获取记录列表,并替换为span
			element  = htmlParser.getElementById(promptArea, "a", "recordList");
			HTMLElement span = (HTMLElement)template.createElement("span");
			span.setId("questionPrompts");
			element.getParentNode().replaceChild(span, element);
			
			//获取相关问题列表
			HTMLAnchorElement a = getRelationFaqsRecordList(template);
			RecordList recordList = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, a.getAttribute("urn"), null);
			span = (HTMLElement)template.createElement("span");
			span.setId("relationFaqs");
			if(recordList.getEmptyPrompt()!=null && !recordList.getEmptyPrompt().isEmpty()) {
				htmlParser.importNodes(htmlParser.parseHTMLString(recordList.getEmptyPrompt(), "utf-8").getChildNodes(), span, false);
			}
			a.getParentNode().replaceChild(span, a);
		}
	}
	
	/**
	 * 设置主目录ID,用于获取热点问题
	 * @param siteId
	 * @param request
	 * @throws ServiceException
	 */
	private void setMainDirectoryId(long siteId, HttpServletRequest request) throws ServiceException {
		OnlineServiceDirectory directory = onlineServiceDirectoryService.getDirectoryBySiteId(siteId);
		request.setAttribute("directoryId", new Long(directory==null ? 0 : directory.getId()));
	}
	
	/**
	 * 获取相关问题记录列表
	 * @param template
	 * @return
	 */
	private HTMLAnchorElement getRelationFaqsRecordList(HTMLDocument template) {
		NodeList elements = template.getElementsByTagName("a");
		for(int i=0; i<elements.getLength(); i++) {
			HTMLAnchorElement a = (HTMLAnchorElement)elements.item(i);
			if("recordList".equals(a.getId()) && a.getAttribute("urn").indexOf("relationOnlineServiceFaqs")!=-1) {
				return a;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#buildHTMLDocument(org.w3c.dom.html.HTMLDocument, long, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode) throws ServiceException, IOException {
		String action = request.getParameter("action");
		if(action!=null) {
			return getPageBuilder().buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode, false, false, false);
		}
		HTMLDocument document = super.buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode);
		//重设发言表单
		HTMLElement form = (HTMLElement)document.getElementsByName("cms/onlineservice/faq/chat").item(0);
		form.setAttribute("target", "chatIframe");

		//创建iframe,用于提交发言内容
		HTMLIFrameElement iframe = (HTMLIFrameElement)document.createElement("iframe");
		iframe.setName("chatIframe");
		iframe.setId("chatIframe");
		iframe.setAttribute("style", "display:none");
		form.getParentNode().insertBefore(iframe, form);
		
		//给输入框加入事件
		HTMLElement input = htmlParser.getElementByName(form, "input", "question");
		input.setAttribute("onkeyup", "onInputQuestion(this, '" + siteId + "')");
		return document;
	}
	
	/**
	 * 生成发言记录
	 * @param robotTalkRecord
	 * @param questionerTalkRecord
	 * @param siteId
	 * @param request
	 * @throws ServiceException
	 */
	private void generateTalkRecords(HTMLElement robotTalkRecord, HTMLElement questionerTalkRecord, long siteId, HttpServletRequest request) throws ServiceException {
		HTMLBodyElement body = (HTMLBodyElement)((HTMLDocument)robotTalkRecord.getOwnerDocument()).getBody();
		//获取问题及答案
		long faqId = RequestUtils.getParameterLongValue(request, "faqId");
		String question;
		String answer;
		if(faqId>0) {
			OnlineServiceFaq faq = (OnlineServiceFaq)onlineServiceFaqService.load(OnlineServiceFaq.class, faqId);
			question = faq.getQuestion();
			answer = faq.getAnswer();
			//增加提问次数
			faq.setAskTimes(faq.getAskTimes() + 1);
			onlineServiceFaqService.update(faq);
		}
		else {
			question = request.getParameter("question");
			//查找热点问题
			setMainDirectoryId(siteId, request); //设置主目录ID,用于获取热点问题
			View view = null;
			try {
				view = viewDefineService.getView("cms/onlineservice/faq", "hotOnlineServiceFaqs", SessionService.ANONYMOUS_SESSION);
			}
			catch (PrivilegeException e) {
				
			}
			view.setPageRows(10);
			//构造搜索条件
			Condition condition = new Condition(null, null, null, Condition.CONDITION_EXPRESSION_KEY, question, null);
			ViewPackage viewPackage = new ViewPackage();
			viewPackage.setSearchConditionList(ListUtils.generateList(condition)); //搜索条件列表
			viewPackage.setSearchMode(true); //是否搜索
			try {
				viewService.retrieveViewPackage(viewPackage, view, 0, true, true, false, request, RequestUtils.getSessionInfo(request));
			}
			catch(PrivilegeException e) {
				Logger.exception(e);
			}
			if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) {
				answer = "根据您当前提问内容，没有找到合适的答案，请您重新提问。";
			}
			else {
				answer = "请您根据实际情况进行选择：<br/>";
				for(Iterator iterator = viewPackage.getRecords().iterator(); iterator.hasNext();) {
					OnlineServiceFaq faq = (OnlineServiceFaq)iterator.next();
					answer += "<a href=\"javascript:void()\" onclick=\"openFaq('" + faq.getId() + "', '" + siteId + "')\">" + StringUtils.generateHtmlContent(faq.getQuestion()) + "</a><br/>";
				}
			}
		}
		
		//输出提问
		writeTalkRecord(question, false, body, questionerTalkRecord);
		//输出答案
		writeTalkRecord(answer, true, body, robotTalkRecord);
	}

	/**
	 * 输出发言记录
	 * @param talkContent
	 * @param talkRecordElement
	 * @return
	 * @throws ServiceException
	 */
	private HTMLElement writeTalkRecord(String talkContent, boolean htmlContent, HTMLElement parentElement, HTMLElement recordElement) throws ServiceException {
		HTMLElement talkRecordElement = (HTMLElement)recordElement.cloneNode(true);
		talkRecordElement.removeAttribute("id");
		talkRecordElement.removeAttribute("title");
		parentElement.appendChild(talkRecordElement);
		
		//获取发言内容元素
		HTMLElement talkContentElement = htmlParser.getElementById(talkRecordElement, "a", "talkContent");
		//输出发言内容
		if(!htmlContent) { //不是HTML
			talkContentElement.getParentNode().replaceChild(talkContentElement.getOwnerDocument().createTextNode(talkContent), talkContentElement);
		}
		else {
			HTMLDocument contentDocument = htmlParser.parseHTMLString(talkContent, "utf-8");
			//获取链接,设置target为_blank,避免在对话窗口中打开链接
			NodeList elements = contentDocument.getElementsByTagName("a");
			for(int i=0; i<(elements==null ? 0 : elements.getLength()); i++) {
				HTMLAnchorElement a = (HTMLAnchorElement)elements.item(i);
				if(a.getHref()!=null && !a.getHref().startsWith("javascript:")) {
					a.setTarget("_blank");
				}
			}
			htmlParser.importNodes(contentDocument.getBody().getChildNodes(), talkContentElement, true);
			talkContentElement.getParentNode().removeChild(talkContentElement);
		}
		
		//输出发言时间
		HTMLElement talkTimeElement = htmlParser.getElementById(talkRecordElement, "a", "talkTime");
		if(talkTimeElement!=null) {
			talkTimeElement.getParentNode().replaceChild(talkTimeElement.getOwnerDocument().createTextNode(DateTimeUtils.formatTimestamp(DateTimeUtils.now(), "yyyy-MM-dd HH:mm:ss")), talkTimeElement);
		}
		return talkRecordElement;
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
	 * @return the onlineServiceFaqService
	 */
	public OnlineServiceFaqService getOnlineServiceFaqService() {
		return onlineServiceFaqService;
	}

	/**
	 * @param onlineServiceFaqService the onlineServiceFaqService to set
	 */
	public void setOnlineServiceFaqService(
			OnlineServiceFaqService onlineServiceFaqService) {
		this.onlineServiceFaqService = onlineServiceFaqService;
	}

	/**
	 * @return the onlineServiceDirectoryService
	 */
	public OnlineServiceDirectoryService getOnlineServiceDirectoryService() {
		return onlineServiceDirectoryService;
	}

	/**
	 * @param onlineServiceDirectoryService the onlineServiceDirectoryService to set
	 */
	public void setOnlineServiceDirectoryService(
			OnlineServiceDirectoryService onlineServiceDirectoryService) {
		this.onlineServiceDirectoryService = onlineServiceDirectoryService;
	}

	/**
	 * @return the viewService
	 */
	public ViewService getViewService() {
		return viewService;
	}

	/**
	 * @param viewService the viewService to set
	 */
	public void setViewService(ViewService viewService) {
		this.viewService = viewService;
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
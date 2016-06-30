package com.yuanluesoft.cms.interview.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLTableRowElement;

import com.yuanluesoft.cms.interview.pojo.InterviewSpeak;
import com.yuanluesoft.cms.interview.pojo.InterviewSubject;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 访谈直播列表处理器
 * @author linchuan
 *
 */
public class InterviewLiveSpeaksProcessor extends RecordListProcessor {
	private InterviewService interviewService; //访谈服务
	private HTMLParser htmlParser;
	private DatabaseService databaseService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String recordListName = StringUtils.getPropertyValue(pageElement.getAttribute("urn"), "recordListName");
		if("interviewLiveSpeaks".equals(recordListName)) { //发言
			if(request.getRequestURL().indexOf("interviewLive.shtml")!=-1) { //直播页面
				//创建iframe,获取新的发言
				HTMLIFrameElement iFrameElement = (HTMLIFrameElement)pageElement.getOwnerDocument().createElement("iframe");
				iFrameElement.setId("updateSpeaksFrame");
				iFrameElement.setName("updateSpeaksFrame");
				iFrameElement.setAttribute("style", "display:none");
				HTMLElement body = ((HTMLDocument)pageElement.getOwnerDocument()).getBody();
				body.appendChild(iFrameElement);
				//创建隐藏字段,存放下载最新的发言的链接
				HTMLInputElement hidden = (HTMLInputElement)pageElement.getOwnerDocument().createElement("input");
				hidden.setAttribute("type", "hidden");
				hidden.setName("liveSpeaksUpdateUrl");
				hidden.setValue(interviewService.getLiveSpeaksUpdateUrl(Long.parseLong(request.getParameter("id"))));
				body.appendChild(hidden);
			}
		}
		else if("toApprovalSpeaks".equals(recordListName)) { //待审核发言列表
			if(request.getRequestURL().indexOf("liveUpdate.shtml")==-1) { //不是获取待审核发言页面
				//创建iframe,获取待审核发言、提交审核结果
				HTMLIFrameElement iFrameElement = (HTMLIFrameElement)pageElement.getOwnerDocument().createElement("iframe");
				iFrameElement.setId("approvalSpeaksFrame");
				iFrameElement.setName("approvalSpeaksFrame");
				iFrameElement.setAttribute("style", "display:none");
				HTMLElement body = ((HTMLDocument)pageElement.getOwnerDocument()).getBody();
				body.appendChild(iFrameElement);
				
				//创建span将作为页面元素的父元素
				HTMLElement div = (HTMLElement)pageElement.getOwnerDocument().createElement("div");
				div.setId("toApprovalSpeakList");
				pageElement.getParentNode().replaceChild(div, pageElement);
				div.appendChild(pageElement);
				//添加鼠标事件处理,判断鼠标是否在审核区域中,如果是且不是静止不动,则不更新待审核发言列表
				div.setAttribute("onmousemove", "onMouseMoveApprovalSpeaks(event)");
				div.setAttribute("onmouseout", "onMouseOutApprovalSpeaks(event)");
				div.setAttribute("style", "float:left");
			}
			else if(request.getRequestURL().indexOf("liveUpdate.shtml")!=-1) { //获取待审核发言页面
				//插入更新待审核发言的脚本
				((HTMLDocument)pageElement.getOwnerDocument()).getBody().setAttribute("onload", "parent.setTimeout('doUpdateToApprovalSpeaks()', 1, 'javascript');");
			}
		}
		else if("interviewImages".equals(recordListName)) { //访谈图片
			if(request.getRequestURL().indexOf("interviewLive.shtml")!=-1) { //访谈直播页面
				//创建iframe,更新图片列表
				HTMLIFrameElement iFrameElement = (HTMLIFrameElement)pageElement.getOwnerDocument().createElement("iframe");
				iFrameElement.setId("updateImagesFrame");
				iFrameElement.setName("updateImagesFrame");
				iFrameElement.setAttribute("style", "display:none");
				HTMLElement body = ((HTMLDocument)pageElement.getOwnerDocument()).getBody();
				body.appendChild(iFrameElement);
				
				//创建div将作为页面元素的父元素
				HTMLElement div = (HTMLElement)pageElement.getOwnerDocument().createElement("div");
				div.setId("interviewImageList");
				pageElement.getParentNode().replaceChild(div, pageElement);
				div.appendChild(pageElement);
				
				//创建隐藏字段,存放下载图片列表的链接
				HTMLInputElement hidden = (HTMLInputElement)pageElement.getOwnerDocument().createElement("input");
				hidden.setAttribute("type", "hidden");
				hidden.setName("liveImagesUpdateUrl");
				hidden.setValue(interviewService.getLiveImagesUpdateUrl(Long.parseLong(request.getParameter("id"))));
				body.appendChild(hidden);
			}
			else if(request.getRequestURL().indexOf("liveUpdate.shtml")!=-1) { //获取图片列表的页面
				//插入更新待审核发言的脚本
				((HTMLDocument)pageElement.getOwnerDocument()).getBody().setAttribute("onload", "parent.setTimeout('doUpdateImages()', 1, 'javascript');");
			}
		}
		super.writePageElement(pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String where = null;
		long subjectId = RequestUtils.getParameterLongValue(request, "id");
		if("interviewLiveSpeaks".equals(view.getName())) {  //发言(访谈直播)
			String lastSpeakTime = request.getParameter("lastSpeakTime"); //最后发言时间限制
			where = "InterviewSpeak.subjectId=" + subjectId +
			 		(lastSpeakTime==null ? "" : " and InterviewSpeak.publishTime>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(new Timestamp(Long.parseLong(lastSpeakTime)), null) + ")");
		}
		else if("toApprovalSpeaks".equals(view.getName())) {  //待审核发言
			SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
			//获取访谈主题
			InterviewSubject interviewSubject = interviewService.getInterviewSubject(subjectId);
			//获取用户角色
			String role = interviewSubject==null ? "" : interviewService.getInterviewRole(interviewSubject, sessionInfo);
			where = "InterviewSpeak.subjectId=" + subjectId + " and InterviewSpeak.approvalRole='" + JdbcUtils.resetQuot(role) + "'";
		}
		view.addWhere(where);
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writeRecord(com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor, java.lang.Object, int, int, org.w3c.dom.NodeList, org.w3c.dom.Element, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.RecordList, com.yuanluesoft.cms.sitemanage.model.SitePage, com.yuanluesoft.cms.sitemanage.model.SitePage, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest, boolean, boolean)
	 */
	protected void writeRecord(View view, Object record, int recordIndex, int offset, NodeList recordFormatNodes, Element recordContainer, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, HTMLParser htmlParser, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if("interviewLiveSpeaks".equals(recordListModel.getRecordListName())) { //发言
			Node node = recordFormatNodes.item(0);
			if(node instanceof HTMLTableRowElement) { //表格
				((HTMLTableRowElement)node).setId("speak_" + ((InterviewSpeak)record).getId());
			}
			else { //非表格
				//创建span,用来显示记录,并设置记录ID
				Element span = recordContainer.getOwnerDocument().createElement("span");
				recordContainer.appendChild(span);
				recordContainer = span;
				span.setAttribute("id", "speak_" + ((InterviewSpeak)record).getId());
			}
		}
		super.writeRecord(view, record, recordIndex, offset, recordFormatNodes, recordContainer, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writeRecords(java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.RecordList, org.w3c.dom.NodeList, int, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean, boolean)
	 */
	protected void writeRecords(List records, View view, RecordList recordListModel, NodeList recordFormatNodes, int pageIndex, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if(records!=null && !records.isEmpty() && request.getRequestURL().indexOf("liveUpdate.shtml")!=-1 && "interviewLiveSpeaks".equals(recordListModel.getRecordListName())) { //发言
			InterviewSpeak previousSpeak = null;
			if(records.size()<recordListModel.getRecordCount()) { //有发言时间限制,并且获取的记录数不足limit
				//获取之前最后一次发言的id,客户端用来判断是否缺发言记录
				String hql = " from InterviewSpeak InterviewSpeak" +
							 " where InterviewSpeak.subjectId=" + request.getParameter("id") +
							 " and InterviewSpeak.publishTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(((InterviewSpeak)records.get(records.size() - 1)).getSpeakTime(), null) + ")" +
							 " order by InterviewSpeak.publishTime DESC";
				previousSpeak = (InterviewSpeak)getDatabaseService().findRecordByHql(hql);
				if(previousSpeak!=null) {
					records.add(previousSpeak); //追加一行,以显示分隔符
				}
			}
			//插入更新发言的脚步
			HTMLScriptElement script = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
			htmlParser.setTextContent(script, "parent.setTimeout(\"doUpdateSpeaks('" + (previousSpeak==null ? "0" : "" + previousSpeak.getId()) + "', " + records.size() + ", " + recordListModel.getRecordCount() + ")\", 1, \"javascript\");");
			((HTMLDocument)pageElement.getOwnerDocument()).getBody().appendChild(script);
		}
		super.writeRecords(records, view, recordListModel, recordFormatNodes, pageIndex, pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
	}
	
	/**
	 * @return the interviewService
	 */
	public InterviewService getInterviewService() {
		return interviewService;
	}

	/**
	 * @param interviewService the interviewService to set
	 */
	public void setInterviewService(InterviewService interviewService) {
		this.interviewService = interviewService;
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
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}
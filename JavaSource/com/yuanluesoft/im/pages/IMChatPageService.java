package com.yuanluesoft.im.pages;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.ClientPageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.im.model.IMChat;
import com.yuanluesoft.im.model.message.ChatDetail;
import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * IM对话页面服务
 * @author linchuan
 *
 */
public class IMChatPageService extends ClientPageService {
	private IMService imService; //IM服务
	private MemberServiceList memberServiceList; //成员服务列表
	private OrgService orgService; //组织机构服务
	
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
			contentIframe.setAttribute("class", contentInput.getAttribute("class"));
			contentIframe.setAttribute("style", contentInput.getAttribute("style"));
			contentIframe.setFrameBorder("0");
			contentIframe.setScrolling("yes");
			contentIframe.setAttribute("allowtransparency", "true");
			contentInput.getParentNode().replaceChild(contentIframe, contentInput);
		}
		//处理设置字体操作按钮
		HTMLElement fontSettingAction = (HTMLElement)template.getElementById("fontSettingAction");
		if(fontSettingAction!=null) {
			fontSettingAction.setAttribute("onclick", "window.chat.showBox('fontSettingBox')");
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
			insertExpressionAction.setAttribute("onclick", "window.chat.showBox('expressionBox')");
		}
		//处理表情框
		HTMLElement expressionBox = (HTMLElement)template.getElementById("expressionBox");
		if(expressionBox!=null) {
			hideHtmlElement(expressionBox); //设为隐藏
			if(expressionBox.getTagName().equalsIgnoreCase("TD")) {
				hideHtmlElement((HTMLElement)expressionBox.getParentNode());
			}
			expressionBox.setAttribute("onclick", "window.chat.insertExpressionImage(event);");
		}
		//处理发送文件操作按钮
		HTMLElement sendFileAction = (HTMLElement)template.getElementById("sendFileAction");
		if(sendFileAction!=null) {
			sendFileAction.setAttribute("onclick", "doHtmlDialogAction('SENDFILE')");
		}
		//处理发送图片操作按钮
		HTMLElement sendImageAction = (HTMLElement)template.getElementById("sendImageAction");
		if(sendImageAction!=null) {
			sendImageAction.setAttribute("onclick", "doHtmlDialogAction('SENDIMAGE')");
		}
		//处理添加用户按钮
		HTMLElement addPersonAction = (HTMLElement)template.getElementById("addPersonAction");
		if(addPersonAction!=null) {
			addPersonAction.setAttribute("onclick", "doHtmlDialogAction('ADDCHATPERSON');");
		}
		
		//添加脚本引用
		getHtmlParser().appendScriptFile(template, Environment.getContextPath() + "/jeaf/filetransfer/js/fileTransfer.js");
		getHtmlParser().appendScriptFile(template, Environment.getContextPath() + "/im/js/chat.js");
		HTMLScriptElement scriptElement = (HTMLScriptElement)template.createElement("script");
		scriptElement.setLang("JavaScript");
		scriptElement.appendChild(template.createTextNode("new Chat('" + RequestUtils.getParameterLongValue(request, "chatId") + "');"));
		template.getBody().appendChild(scriptElement);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
		long chatId = RequestUtils.getParameterLongValue(request, "chatId");
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		ChatDetail chat = imService.loadChat(sessionInfo.getUserId(), chatId);
		IMChat imChat = new IMChat();
		long personId = Long.parseLong(chat.getChatPersonIds().split(",")[0]);
		Member member = memberServiceList.getMember(personId); //获取对方信息
		try {
			PropertyUtils.copyProperties(imChat, chat);
			PropertyUtils.copyProperties(imChat, member);
		}
		catch(Exception e) {
			
		}
		if(member.getOriginalRecord()!=null && (member.getOriginalRecord() instanceof Person)) { //系统用户
			//获取部门
			List orgs = orgService.listOrgsOfPerson(personId + "", false);
			imChat.setDepartment(((Org)orgs.get(0)).getDirectoryName());
		}
		//设置用户状态
		imChat.setStatus(imService.getPersonStatus(personId));
		if(imChat.getStatus()>IMService.IM_PERSON_STATUS_HIDE) {
			imChat.setWebIM("WEBIM".equals(imService.getClientType(personId)) ? "WEBIM" : "");
		}
		sitePage.setAttribute("record", imChat);
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
	 * @return the memberServiceList
	 */
	public MemberServiceList getMemberServiceList() {
		return memberServiceList;
	}

	/**
	 * @param memberServiceList the memberServiceList to set
	 */
	public void setMemberServiceList(MemberServiceList memberServiceList) {
		this.memberServiceList = memberServiceList;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}
}
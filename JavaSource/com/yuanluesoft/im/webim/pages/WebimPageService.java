package com.yuanluesoft.im.webim.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.im.pojo.IMPersonalSetting;
import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.im.webim.model.Webim;
import com.yuanluesoft.im.webim.model.WebimAction;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.util.PersonUtils;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * WEBIM页面服务
 * @author linchuan
 *
 */
public class WebimPageService extends PageService {
	private HTMLParser htmlParser; //HTML解析器
	private IMService imService; //IM服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//创建DIV,把BODY中的全部元素都粘帖到其中,用来判断窗口的大小
		HTMLElement span = (HTMLElement)template.createElement("span");
		span.setId("divWebim");
		span.setAttribute("style", "display: inline-block;");
		htmlParser.importNodes(template.getBody().getChildNodes(), span, false);
		htmlParser.setTextContent(template.getBody(), null);
		template.getBody().appendChild(span);
		
		//添加脚本
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/im/webim/js/webim.js");
		
		//给body添加窗口右对齐的样式
		String style = template.getBody().getAttribute("style");
		template.getBody().setAttribute("style", (style==null ? "" : style + ";") + "width: 3000px; background-color: transparent; margin: 0px;  padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px; overflow: hidden");
		
		String script;
		if("webim".equals(pageName)) { 
			//在线用户数字段外添加SPAN,用来更新用户数
			NodeList elements = template.getElementsByTagName("a");
			for(int i=(elements==null ? -1 : elements.getLength()-1); i>=0; i--) {
				HTMLAnchorElement a = (HTMLAnchorElement)elements.item(i);
				if("field".equals(a.getId())) {
					String fieldName = StringUtils.getPropertyValue(a.getAttribute("urn"), "name");
					if("onlinePersonCount".equals(fieldName) || "unreadSystemMessageCount".equals(fieldName)) {
						a.setName("recordCount");
						if("unreadSystemMessageCount".equals(fieldName)) {
							a.setAttribute("style", "display:none");
						}
					}
				}
			}
			//输出WEBIM脚本
			SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
			IMPersonalSetting personalSetting = imService.loadPersonalSetting(sessionInfo.getUserId()); //加载个人设置
			//WebimSetting = function(fontName, fontSize, fontColor, loginStatus, ctrlSend, playSoundOnReceived, setFocusOnReceived, openChatDialogOnReceived)
			script = "var webimSetting = new WebimSetting('" + personalSetting.getFontName() + "'," +
														  "'" + personalSetting.getFontSize() + "'," +
														  "'" + personalSetting.getFontColor() + "'," +
														  "'" + personalSetting.getStatus() + "'," +
														  (personalSetting.getCtrlSend()==1) + "," +
														  (personalSetting.getPlaySoundOnReceived()==1) + "," +
														  (personalSetting.getSetFocusOnReceived()==1) + "," +
														  (personalSetting.getOpenChatDialogOnReceived()==1) +
														  ");\r\n";
			script += "var webim = new Webim('" + sessionInfo.getUserId() + "', webimSetting);\r\n";
			//处理WEBIM按钮
			NodeList webimActions = template.getElementsByName("webimAction");
			for(int i=webimActions.getLength()-1; i>=0; i--) {
				HTMLElement element = (HTMLElement)webimActions.item(i);
				//解析WEBIM按钮
				WebimAction webimAction = (WebimAction)BeanUtils.generateBeanByProperties(WebimAction.class, element.getAttribute("urn"), null);
				element.removeAttribute("name");
				element.removeAttribute("urn");
				element.setId("webimAction_" + i);
				String dialogUrl = "/im/webim/";
				if("在线用户".equals(webimAction.getAction())) {
					dialogUrl += "onlinePersons.shtml";
				}
				else if("用户目录树".equals(webimAction.getAction())) {
					dialogUrl += "personTree.shtml";
				}
				else if("系统消息".equals(webimAction.getAction())) {
					dialogUrl += "systemMessage.shtml";
				}
				else if("个人设置".equals(webimAction.getAction())) {
					dialogUrl += "personalSetting.shtml";
				}
				else if("对话".equals(webimAction.getAction())) {
					dialogUrl += "chat.shtml";
				}
				script += "webim.addAction('webimAction_" + i + "', '" + webimAction.getAction() + "', '" + dialogUrl + "', '" + webimAction.getDialogWidth() + "', '" + webimAction.getDialogHeight() + "', '" + webimAction.getDialogAlign() + "', '" + webimAction.getSelectedStyle() + "', '" + webimAction.getMouseoverStyle() + "');\r\n";
			}
		}
		else { //客服
			//WebimSetting = function(fontName, fontSize, fontColor, loginStatus, ctrlSend, playSoundOnReceived, setFocusOnReceived, openChatDialogOnReceived)
			script = "var webimSetting = new WebimSetting('宋体', '12', '#000000', '" + IMService.IM_PERSON_STATUS_ONLINE + "', false, true, true, true);\r\n";
			script += "var webim = new Webim('0', webimSetting, true);\r\n"; //创建WEB客服
			//检查是否有客服在线,如果没有显示留言链接
			int online = -1;
			NodeList links = template.getElementsByTagName("a");
			for(int i=links.getLength()-1; i>=0; i--) {
				HTMLAnchorElement a = (HTMLAnchorElement)links.item(i);
				if(!"pageLink".equals(a.getId())) {
					continue;
				}
				String urn  = a.getAttribute("urn");
				String linkName = StringUtils.getPropertyValue(urn, "linkTitle");
				if(linkName==null || linkName.isEmpty()) {
					linkName = urn;
				}
				if("在线交谈".equals(linkName) || "留言".equals(linkName)) {
					if(online==-1) {
						online = imService.getFreeSpecialist(siteId)==null ? 0 : 1;
					}
					if((online==1 && "留言".equals(linkName)) || (online==0 && "在线交谈".equals(linkName))) {
						a.getParentNode().removeChild(a);
					}
				}
			}
		}
		script += "webim.show();";
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)template.createElement("script");
		scriptElement.setLang("JavaScript");
		scriptElement.appendChild(template.createTextNode(script));
		template.getBody().appendChild(scriptElement);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
		Webim webim = new Webim();
		if("webim".equals(pageName)) { 
			SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
			webim.setOnlinePersonCount(1); //在线用户数,实际值由客户端用ajax获取
			webim.setUserName(sessionInfo.getUserName()); //用户名
			webim.setStatus("在线"); //用户状态
			webim.setPortraitURL(PersonUtils.getPortraitURL(sessionInfo.getUserId())); //用户头像
		}
		sitePage.setAttribute("record", webim);
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
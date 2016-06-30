package com.yuanluesoft.jeaf.dialog.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.service.UserPageTemplateService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class DialogPageService extends PageService {
	private HTMLParser htmlParser;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getSitePage(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	protected SitePage getSitePage(String applicationName, String pageName, HttpServletRequest request) throws ServiceException {
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		if(sessionInfo!=null && sessionInfo.isInternalUser() && !SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			return super.getSitePage("jeaf/usermanage", "internalDialog", request);
		}
		else {
			return super.getSitePage("cms/sitemanage", "externalDialog", request);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		if(sitePage.getName().equals("internalDialog")) { //内部用户,调用用户页面模板服务获取模板
			return ((UserPageTemplateService)getTemplateService()).getTemplateHTMLDocument(sitePage.getApplicationName(), sitePage.getName(), RequestUtils.getSessionInfo(request), request);
		}
		else {
			return super.getTemplate(sitePage.getApplicationName(), sitePage.getName(), sitePage, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request, editMode);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//添加js引用
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/jeaf/common/js/common.js");
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/jeaf/dialog/js/dialog.js");
		
		//添加对话框关闭脚本
		HTMLElement element = (HTMLElement)template.getElementById("dialogCloseButton");
		if(element!=null) {
			element.removeAttribute("id");
			element.setAttribute("onclick", "DialogUtils.closeDialog()");
		}
		
		//设置窗口标题区域
		element = (HTMLElement)template.getElementById("dialogTitle");
		if(element!=null) {
			htmlParser.setTextContent(element, null); //清空
		}
		
		//添加窗口移动脚本
		String style = template.getBody().getAttribute("style");
		template.getBody().setAttribute("style", "background-color: transparent;" + (style==null || style.isEmpty() ? "" : style)); //隐藏窗口,尺寸调整完成后显示
		template.getBody().setAttribute("onload", "onDialogLoad();");
		
		//添加IFRAME,用来显示对话框页面
		element = (HTMLElement)template.getElementById("dialogBody");
		if(element==null) {
			return;
		}
		HTMLIFrameElement iframe = (HTMLIFrameElement)template.createElement("iframe");
		iframe.setId("dialogBody");
		iframe.setName("dialogBody_" + UUIDLongGenerator.generateId());
		iframe.setAttribute("onload", "try{onDialogBodyLoad();}catch(e){}");
		iframe.setAttribute("width", "100%");
		iframe.setAttribute("height", "100%");
		iframe.setSrc("about:blank");
		iframe.setFrameBorder("0");
		element.getParentNode().replaceChild(iframe, element);
	}
	
	/**
	 * 获取对话框样式节点列表,包括CSS和STYLE
	 * @param request
	 * @return
	 */
	public NodeList getDialogStyleNoeds(HttpServletRequest request) throws ServiceException {
		SitePage sitePage = getSitePage(null, null, request);
		HTMLDocument template = getTemplate(null, null, sitePage, RequestUtils.getParameterLongValue(request, "siteId"), 0, TemplateThemeService.THEME_TYPE_COMPUTER, 0, true, true, request, false);
		final NodeList styles = template.getElementsByTagName("style");
		final NodeList links = template.getElementsByTagName("link");
		NodeList nodes = new NodeList() {
			public int getLength() {
				return (styles==null ? 0 : styles.getLength()) + (links==null ? 0 : links.getLength());
			}
			public Node item(int index) {
				int styleLength = styles==null ? 0 : styles.getLength();
				if(index < styleLength) {
					return styles.item(index);
				}
				int linkLength = links==null ? 0 : links.getLength();
				return index < styleLength + linkLength ? links.item(index - styleLength) : null;
			}
		};
		return nodes;
	}
	
	/**
	 * 获取对话框样式HTML
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public String getDialogStyleHTML(HttpServletRequest request) throws ServiceException {
		NodeList nodes = getDialogStyleNoeds(request);
		String html = null;
		for(int i = 0; i < (nodes==null ? 0 : nodes.getLength()); i++) {
			html = (html==null ? "" : html + "\n") + htmlParser.getElementHTML((HTMLElement)nodes.item(i), "utf-8");
		}
		return html;
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
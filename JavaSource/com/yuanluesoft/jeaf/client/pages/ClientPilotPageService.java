package com.yuanluesoft.jeaf.client.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback;

/**
 * 
 * @author linchuan
 *
 */
public class ClientPilotPageService extends ClientPageService {
	private HTMLParser htmlParser; //HTML解析器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, final SitePage sitePage, final long siteId, final HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//重设BODY
		HTMLBodyElement body = (HTMLBodyElement)template.getBody();
		String style = body.getAttribute("style");
		body.setAttribute("style", (style==null || style.isEmpty() ? "" : style + ";") + "margin:0px; border-style:none; overflow:hidden");
		
		//插入JS调用
		String[] js = {"/jeaf/common/js/scroller.js", "pilot.js"};
		for(int i=0; i<js.length; i++) {
			HTMLScriptElement script = (HTMLScriptElement)template.createElement("script");
			script.setLang("javascript");
			script.setSrc(request.getContextPath() + (js[i].startsWith("/") ? "" : "/jeaf/client/js/") + js[i]);
			template.getBody().appendChild(script);
		}
		final StringBuffer script = new StringBuffer();
		
		//创建div,用来放置引导页面
		HTMLDivElement pilotPageContainer = (HTMLDivElement)template.createElement("div");
		pilotPageContainer.setId("pilotPageContainer");
		template.getBody().appendChild(pilotPageContainer);
		
		//处理引导页面
		final NodeList pilotPages = template.getElementsByName("pilotPage");
		final int pageCount = pilotPages==null ? 0 : pilotPages.getLength();
		HTMLElement nextPage = null;
		for(int i=pageCount-1; i>=0; i--) {
			HTMLElement pilotPage = (HTMLElement)pilotPages.item(i);
			pilotPage.setAttribute("style", "float:left");
			pilotPage.removeAttribute("id");
			pilotPage.removeAttribute("name");
			pilotPage.getParentNode().removeChild(pilotPage);
			if(nextPage==null) {
				pilotPageContainer.appendChild(pilotPage);
			}
			else {
				pilotPageContainer.insertBefore(pilotPage, nextPage);
			}
			nextPage = pilotPage;
			
			//处理“前一页”按钮、“下一页”按钮、“开始使用”按钮, <img id="previousPageButton" title="上一页"/>
			final int pageIndex = i;
			htmlParser.traversalChildNodes(pilotPage.getChildNodes(), true, new HTMLTraversalCallback() {
				public boolean processNode(Node node) {
					HTMLElement element = (HTMLElement)node;
					if(",previousPageButton,nextPageButton,startToUseButton,".indexOf("," + element.getId() + ",")==-1) {
						return false;
					}
					String onclick = null;
					if("previousPageButton".equals(element.getId())) {
						onclick = "window.pilot.gotoPage(" + Math.max(0, pageIndex - 1) + ")";
					}
					else if("nextPageButton".equals(element.getId())) {
						onclick = "window.pilot.gotoPage(" + Math.min(pageCount-1, pageIndex + 1) + ")";
					}
					else if("startToUseButton".equals(element.getId())) {
						onclick = "window.pilot.showMainPage()";
					}
					element.setAttribute("onclick", onclick);
					element.removeAttribute("id");
					element.removeAttribute("title");
					return true;
				}
			});
		}
		
		//检查是否垂直滚动
		boolean verticalScroll = "true".equals(htmlParser.getMeta(template, "pilotVerticalScroll"));
		htmlParser.removeMeta(template, "pilotVerticalScroll");
		
		//生成加载引导页面端脚本
		script.append("window.pilot = new Pilot(document.getElementById('pilotPageContainer'), " + verticalScroll + ");");
		
		//创建script元素
		HTMLScriptElement scriptElement = (HTMLScriptElement)template.createElement("script");
		scriptElement.setLang("javascript");
		htmlParser.setTextContent(scriptElement, script.toString());
		template.getBody().appendChild(scriptElement);
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
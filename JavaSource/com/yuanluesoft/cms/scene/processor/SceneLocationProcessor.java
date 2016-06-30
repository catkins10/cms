package com.yuanluesoft.cms.scene.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SceneLocationProcessor implements PageElementProcessor {
	private SceneService sceneService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//解析场景位置
		String urn = pageElement.getAttribute("urn");
		//解析分隔符
		String separator = StringUtils.getPropertyValue(urn, "separator");

		HTMLDocument document = htmlParser.parseHTMLString("<div id=\"separator\">" + separator + "</div>", "utf-8");
		NodeList separatorNodes = document.getElementById("separator").getChildNodes();
		pageElement.removeAttribute("href");
		htmlParser.setTextContent(pageElement, null);
		
		//获取目录
		SceneDirectory sceneDirectory = sceneService.getSceneDirectory(RequestUtils.getParameterLongValue(request, "id"));
		//获取父目录列表
		List parentDirectories = sceneService.listParentSceneDirectories(sceneDirectory.getId());
		
		for(int i=parentDirectories.size() - 1; i>=0; i--) {
			HTMLAnchorElement siteAnchor = (HTMLAnchorElement)pageElement.getOwnerDocument().createElement("A");
			if(i==parentDirectories.size() - 1) { //场景服务
				com.yuanluesoft.cms.scene.pojo.SceneService service = (com.yuanluesoft.cms.scene.pojo.SceneService)parentDirectories.get(i);
				//设置名称
				htmlParser.setTextContent(siteAnchor, service.getName());
				//设置链接
				siteAnchor.setHref(Environment.getContextPath() + "/cms/scene/sceneService.shtml?id=" + service.getId() + (parentSite.getId()>0 ? "&siteId=" + parentSite.getId() : ""));
			}
			else { //场景目录
				SceneDirectory parentDirectory = (SceneDirectory)parentDirectories.get(i);
				//设置名称
				htmlParser.setTextContent(siteAnchor, parentDirectory.getDirectoryName());
				//设置链接
				siteAnchor.setHref(Environment.getContextPath() + "/cms/scene/scene.shtml?id=" + parentDirectory.getId() + (parentSite.getId()>0 ? "&siteId=" + parentSite.getId() : ""));
			}
			pageElement.getParentNode().insertBefore(siteAnchor, pageElement);
			//插入分隔符
			if(separatorNodes!=null) {
				for(int j=0; j<separatorNodes.getLength(); j++) {
					pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().importNode(separatorNodes.item(j), true), pageElement);
				}
			}
		}
		//输出当前目录
		pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().createTextNode(sceneDirectory.getDirectoryName()), pageElement);
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
	 * @return the sceneService
	 */
	public SceneService getSceneService() {
		return sceneService;
	}

	/**
	 * @param sceneService the sceneService to set
	 */
	public void setSceneService(SceneService sceneService) {
		this.sceneService = sceneService;
	}
}

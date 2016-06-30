package com.yuanluesoft.cms.sitemanage.processor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageElement;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.StaticPageColumns;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 页面元素处理器：站点/栏目链接
 * @author linchuan
 *
 */
public class SiteLinkProcessor implements PageElementProcessor {
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement;
		//解析站点链接
		String urn = pageElement.getAttribute("urn");
		//解析站点ID
		long siteId =  StringUtils.getPropertyLongValue(urn, "siteId", 0);
		//解析打开方式
		String openMode = StringUtils.getPropertyValue(urn, "openMode");
		
		//删除配置属性
		pageElement.removeAttribute("id");
		pageElement.removeAttribute("urn");
		String columnName = null;
		if(webDirectory!=null) {
			if(siteId==-2) { //站点
				siteId = parentSite.getId();
				if("<站点>".equals(htmlParser.getTextContent(pageElement))) {
					htmlParser.setTextContent(pageElement, parentSite.getDirectoryName());
				}
			}
			else if(siteId==-1) { //父栏目
				String column = RequestUtils.getParameterStringValue(request, "columnName");
				siteId = (column==null || column.isEmpty() ? webDirectory.getParentDirectoryId() : webDirectory.getId());
				if("<父栏目>".equals(htmlParser.getTextContent(pageElement))) {
					htmlParser.setTextContent(pageElement, siteId==webDirectory.getId() ? webDirectory.getDirectoryName() : siteService.getDirectory(siteId).getDirectoryName());
				}
			}
			else if("true".equals(StringUtils.getPropertyValue(urn, "linkByName"))) { //按栏目名称
				siteId = webDirectory.getId();
				columnName = StringUtils.getPropertyValue(urn, "siteName"); //解析站点名称
				List columnIds = siteService.listDirectoryIdsByName(webDirectory.getId(), columnName, null); //按名称获取下级栏目
				if(columnIds==null || columnIds.isEmpty()) { //没有下级栏目
					columnIds = siteService.listDirectoryIdsByName(webDirectory.getParentDirectoryId(), columnName, null); //获取兄弟栏目
				}
				if(columnIds!=null && columnIds.size()==1) { //只有一个栏目
					siteId = ((Number)columnIds.get(0)).longValue();
					columnName = null;
				}
				else if(columnIds==null || columnIds.isEmpty()) { //下级和兄弟都没有指定的栏目
					columnIds = siteService.listDirectoryIdsByName(parentSite.getId(), columnName, null); //获取站点的下级栏目
					if(columnIds!=null && columnIds.size()==1) { //只有一个栏目
						siteId = ((Number)columnIds.get(0)).longValue();
						columnName = null;
					}
					else if(columnIds!=null && columnIds.size()>1) { //超过一个栏目
						siteId = parentSite.getId();
					}
				}
			}
		}
		if(columnName==null && webDirectory.getId()==siteId) { //链接到当前站点或栏目
			pageElement.setId("currentSite");
			pageElement.setClassName((pageElement.getClassName()==null ? "" : pageElement.getClassName() + " ") + "currentSite");
		}
		WebDirectory directory = (WebDirectory)siteService.getDirectory(siteId);
		String url = null;
		if(directory!=null) {
			if(columnName==null && directory.getRedirectUrl()!=null && !directory.getRedirectUrl().isEmpty()) { //重定向
				url = "{FINAL}" + directory.getRedirectUrl();
			}
			else {
				try {
					url = "/cms/sitemanage/index.shtml?siteId=" + siteId + (columnName==null ? "" : "&columnName=" + URLEncoder.encode(columnName, "utf-8"));
				}
				catch (UnsupportedEncodingException e) {
					
				}
			}
		}
		//输出链接
		LinkUtils.writeLink(anchorElement, url, openMode, webDirectory.getId(), siteId, null, false, true, sitePage, request);
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
		if(webDirectory==null) {
			return;
		}
		String urn = pageElement.getAttribute("urn");
		//解析站点ID
		long siteId =  StringUtils.getPropertyLongValue(urn, "siteId", 0);
		if(siteId==-1) { //父栏目
			//检查是否已经创建过
			String hql = "select StaticPageColumns.id" +
						 " from StaticPageColumns StaticPageColumns" +
						 " where StaticPageColumns.pageId=" + staticPageId +
						 " and StaticPageColumns.siteId=" + webDirectory.getId()  +
						 " and StaticPageColumns.columnType='3'"; //0/子栏目,1/父栏目及其兄弟栏目,2/兄弟栏目,3/父栏目
			if(databaseService.findRecordByHql(hql)==null) {
				//保存引用的记录列表
				StaticPageColumns staticPageColumns = new StaticPageColumns();
				staticPageColumns.setId(UUIDLongGenerator.generateId()); //页面ID
				staticPageColumns.setPageId(staticPageId); //页面ID
				staticPageColumns.setProcessorClassName(this.getClass().getName()); //处理器类名称
				staticPageColumns.setSiteId(webDirectory.getId()); //隶属的站点/栏目ID
				staticPageColumns.setColumnType('3');
				databaseService.saveRecord(staticPageColumns);
			}
			return;
		}
		String elementName;
		if(siteId==-2) { //不按栏目名称
			elementName = "siteLink_" + parentSite.getId();
			if("<站点>".equals(htmlParser.getTextContent(pageElement))) {
				siteId = 1; //设置为1,当站点名称有变化时,页面会更新
			}
			else {
				siteId = (parentSite.getRedirectUrl()==null ? "" : parentSite.getRedirectUrl()).hashCode(); //用重定向地址的HASH值做站点ID
			}
		}
		else if("true".equals(StringUtils.getPropertyValue(urn, "linkByName"))) { //按栏目名称
			String columnName = StringUtils.getPropertyValue(urn, "siteName"); //解析站点名称
			elementName = "siteLink_" + columnName;
			List columnIds = siteService.listDirectoryIdsByName(webDirectory.getId(), columnName, null); //按名称获取下级栏目
			if(columnIds!=null && !columnIds.isEmpty()) { //有下级栏目
				siteId = webDirectory.getId();
			}
			else {
				columnIds = siteService.listDirectoryIdsByName(webDirectory.getParentDirectoryId(), columnName, null); //获取兄弟栏目
				if(columnIds!=null && !columnIds.isEmpty()) { //有兄弟栏目
					siteId = webDirectory.getParentDirectoryId();
				}
				else { //下级和兄弟都没有指定的栏目
					siteId = parentSite.getId();
				}
			}
		}
		else { //不按栏目名称
			elementName = "siteLink_" + siteId;
			WebDirectory directory = (WebDirectory)siteService.getDirectory(siteId);
			if(directory==null) {
				return;
			}
			siteId = (directory.getRedirectUrl()==null ? "" : directory.getRedirectUrl()).hashCode(); //用重定向地址的HASH值做站点ID
		}
		//检查是否已经创建过
		String hql = "select StaticPageElement.id" +
					 " from StaticPageElement StaticPageElement" +
					 " where StaticPageElement.pageId=" + staticPageId +
					 " and StaticPageElement.siteId=" + siteId +
					 " and StaticPageElement.elementName='" + JdbcUtils.resetQuot(elementName) + "'";
		if(databaseService.findRecordByHql(hql)==null) {
			//保存引用的页面元素
			StaticPageElement staticPageElement  = new StaticPageElement();
			staticPageElement.setId(UUIDLongGenerator.generateId()); //页面ID
			staticPageElement.setPageId(staticPageId); //页面ID
			staticPageElement.setElementName(elementName); //页面元素名称
			staticPageElement.setSiteId(siteId); //站点ID
			databaseService.saveRecord(staticPageElement);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof WebDirectory)) { //不是网站栏目
			return null;
		}
		WebDirectory directory = (WebDirectory)object;
		//获取配置了目录位置的页面列表
		String hql = "select distinct StaticPage" +
			  		 " from StaticPage StaticPage, StaticPageElement StaticPageElement" +
			  		 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
			  		 " and StaticPageElement.pageId=StaticPage.id" +
			  		 " and (" +
			  		 "  (StaticPageElement.elementName='siteLink_" + directory.getId() + "'" + //按站点ID
			  		 "   and StaticPageElement.siteId!=" + (directory.getRedirectUrl()==null ? "" : directory.getRedirectUrl()).hashCode() + ")" +
			  		 "  or" +
			  		 "  (StaticPageElement.elementName='siteLink_" + directory.getDirectoryName() + "'" + //按站点名称
			  		 "   and (" +
			  		 "    StaticPageElement.siteId=" + directory.getId() + //栏目本身
			  		 "	  or StaticPageElement.siteId in (" +
			  		 "	   select WebDirectorySubjection.parentDirectoryId from WebDirectorySubjection WebDirectorySubjection where WebDirectorySubjection.directoryId=" + directory.getParentDirectoryId() + ")" + //父栏目
			  		 "   )" +
			  		 "  )" +
			  		 " )";
		return databaseService.findRecordsByHql(hql, 0, max);
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}
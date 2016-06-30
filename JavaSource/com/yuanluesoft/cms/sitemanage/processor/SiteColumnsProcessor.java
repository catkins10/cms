package com.yuanluesoft.cms.sitemanage.processor;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.StaticPageColumns;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 子栏目列表处理器
 * @author linchuan
 *
 */
public class SiteColumnsProcessor extends RecordListProcessor {
	private SiteService siteService; //站点服务
	private DatabaseService databaseService; //数据库服务
	private ImageService imageService; //图片服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
		if("sites".equals(view.getName())) { //站点列表
			view.addWhere("WebSite.id!=0 and WebSite.parentDirectoryId=" + StringUtils.getPropertyLongValue(recordListModel.getExtendProperties(), "siteId", 0));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		if("siteLogos".equals(view.getName())) { //站点LOGO
			return new RecordListData(imageService.list("cms/sitemanage", "logo", parentSite.getId(), false, 0, request));
		}
		else if("sites".equals(view.getName())) { //站点列表
			return super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		}
		if("columnsByResourceCount".equals(view.getName())) { //栏目排行(按文章数),js方式获取
			String sql = "select siteId" +
						 " from (select SiteResourceSubjection.siteId siteId, count(SiteResourceSubjection.id) resourceCount" +
						 " from cms_resource_subjection SiteResourceSubjection left join cms_resource SiteResource on SiteResourceSubjection.resourceId=SiteResource.id" +
						 " where SiteResource.status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'" +
						 " group by SiteResourceSubjection.siteId) SiteResourceSubjection" +
						 " order by resourceCount DESC";
			List siteIdList = getDatabaseService().executeQueryBySql(sql, beginRow, recordListModel.getRecordCount());
			String siteIds = null;
			for(Iterator iterator = siteIdList.iterator(); iterator.hasNext();) {
				SqlResult sqlResult = (SqlResult)iterator.next();
				siteIds = (siteIds==null ? "" : siteIds + ",") + sqlResult.get("siteId");
			}
			List sites = getDatabaseService().findRecordsByHql("from WebDirectory WebDirectory where WebDirectory.id in (" + JdbcUtils.validateInClauseNumbers(siteIds) + ")");
			sites = ListUtils.sortByProperty(sites, "id", siteIds);
			for(Iterator iterator = sites.iterator(); iterator.hasNext();) {
				WebDirectory site = (WebDirectory)iterator.next();
				Number count = (Number)getDatabaseService().findRecordByHql("select count(SiteResourceSubjection.id) from SiteResourceSubjection SiteResourceSubjection where SiteResourceSubjection.siteId=" + site.getId());
				site.setResourceCount(count==null ? 0 : count.intValue());
			}
			return new RecordListData(sites);
		}
		
		long siteId;
		Object parentRecord = sitePage.getAttribute("parentRecord");
		if(parentRecord!=null) { //有父记录,内嵌的记录列表
			siteId = ((WebDirectory)parentRecord).getId();
		}
		else {
			siteId = StringUtils.getPropertyLongValue(recordListModel.getExtendProperties(), "siteId", -1);
		}
		String columnType = retrieveColumnType(recordListModel.getExtendProperties(), request);
		List columns;
		if("parent".equals(columnType)) { //父栏目及其兄弟栏目列表
			columns = siteService.listChildDirectories(siteService.getDirectory(siteId==-1 ? webDirectory.getParentDirectoryId() : siteService.getDirectory(siteId).getParentDirectoryId()).getParentDirectoryId(), "column,viewReference,link", null, "WebDirectory.halt!='1'", false, false, null, beginRow, recordListModel.getRecordCount());
		}
		else if("parentOnly".equals(columnType)) { //父栏目
			columns = ListUtils.generateList(siteService.getDirectory(siteId==-1 ? webDirectory.getParentDirectoryId() : siteService.getDirectory(siteId).getParentDirectoryId()));
		}
		else if("sibling".equals(columnType)) { //兄弟栏目列表
			columns = siteService.listChildDirectories((siteId==-1 ? webDirectory.getParentDirectoryId() : siteService.getDirectory(siteId).getParentDirectoryId()), "column,viewReference,link", null, "WebDirectory.halt!='1'", false, false, null, beginRow, recordListModel.getRecordCount());
		}
		else if("current".equals(columnType)) { //当前栏目
			columns = ListUtils.generateList(siteId==-1 ? webDirectory : siteService.getDirectory(siteId));
		}
		else { //子栏目列表
			columns = siteService.listChildDirectories((siteId==-1 ? webDirectory.getId() : siteId), "column,viewReference,link", null, "WebDirectory.halt!='1'", false, false, null, beginRow, recordListModel.getRecordCount());
		}
		String linkType = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "linkType");
		if(columns!=null && "rssSubscribe".equals(linkType)) { //RSS订阅
			//过滤掉没有子栏目的栏目
			for(Iterator iterator = columns.iterator(); iterator.hasNext();) {
				WebDirectory column = (WebDirectory)iterator.next();
				column = (WebDirectory)siteService.getDirectory(column.getId());
				if(!siteService.hasChildDirectories(column.getId())) {
					iterator.remove();
				}
			}
		}
		return new RecordListData(columns);
	}
	
	/**
	 * 获取栏目类型
	 * @param extendProperties
	 * @param request
	 * @return
	 */
	private String retrieveColumnType(String extendProperties, HttpServletRequest request) {
		String columnType = StringUtils.getPropertyValue(extendProperties, "type");
		String columnName = RequestUtils.getParameterStringValue(request, "columnName");
		if(columnName!=null && !columnName.isEmpty()) { //有指定栏目名称,栏目级别降低一级
			if("parent".equals(columnType)) { //父栏目及其兄弟栏目列表
				columnType = "sibling";
			}
			else if("parentOnly".equals(columnType)) { //父栏目
				columnType = "current";
			}
			else { //兄弟栏目列表、子栏目列表
				columnType = "child";
			}
		}
		return columnType;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		if("siteLogos".equals(view.getName())) { //站点LOGO
			return;
		}
		long siteId;
		Object parentRecord = sitePage.getAttribute("parentRecord");
		if(parentRecord!=null) { //有父记录,内嵌的记录列表
			siteId = ((WebDirectory)parentRecord).getId();
		}
		else {
			siteId = StringUtils.getPropertyLongValue(recordListModel.getExtendProperties(), "siteId", -1);
			if(siteId==-1) {
				siteId = webDirectory.getId();
			}
		}
		String type = retrieveColumnType(recordListModel.getExtendProperties(), request);
		char columnType = '0'; //子栏目列表
		if("parent".equals(type)) { //父栏目列表
			columnType = '1';
		}
		else if("sibling".equals(type)) { //兄弟栏目列表
			columnType = '2';
		}
		else if("parentOnly".equals(type)) { //仅自己的父栏目
			columnType = '3';
		}
		else if("current".equals(type)) { //栏目本身
			columnType = '4';
		}
		//检查是否已经创建过
		String hql = "select StaticPageColumns.id" +
					 " from StaticPageColumns StaticPageColumns" +
					 " where StaticPageColumns.pageId=" + staticPageId +
					 " and StaticPageColumns.siteId=" + siteId  +
					 " and StaticPageColumns.columnType='" + columnType + "'"; //0/子栏目,1/父栏目及其兄弟栏目,2/兄弟栏目,3/父栏目
		if(databaseService.findRecordByHql(hql)==null) {
			//保存引用的记录列表
			StaticPageColumns staticPageColumns = new StaticPageColumns();
			staticPageColumns.setId(UUIDLongGenerator.generateId()); //页面ID
			staticPageColumns.setPageId(staticPageId); //页面ID
			staticPageColumns.setProcessorClassName(this.getClass().getName()); //处理器类名称
			staticPageColumns.setSiteId(siteId); //隶属的站点/栏目ID
			staticPageColumns.setColumnType(columnType);
			databaseService.saveRecord(staticPageColumns);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof WebDirectory)) { //不是网站目录、或者是站点
			return null;
		}
		WebDirectory webDirectory = (WebDirectory)object;
		if(webDirectory.getDirectoryName()==null) {
			return null;
		}
		//获取父栏目ID
		long parentDirectoryId = (DirectoryService.DIRECTORY_ACTION_ADD_CHILD.equals(modifyAction) || DirectoryService.DIRECTORY_ACTION_MOVE_CHILD.equals(modifyAction) || DirectoryService.DIRECTORY_ACTION_REMOVE_CHILD.equals(modifyAction) ? webDirectory.getId() : webDirectory.getParentDirectoryId());
		//获取兄弟栏目ID
		String hql = "select WebDirectory.id" +
					 " from WebDirectory WebDirectory" +
					 " where WebDirectory.parentDirectoryId=" + parentDirectoryId;
		//Logger.debug("SiteColumnsProcessor hql1: " + hql);
		String siblingSiteIds = ListUtils.join(databaseService.findRecordsByHql(hql), ",", false);
		String childSiteIds = null; //子栏目和侄子栏目ID
		String myChildSiteIds = null; //自己的子栏目ID
		if(StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE.equals(modifyAction)) { //更新,获取第一级子栏目ID
			hql = "select WebDirectory.id" +
					 " from WebDirectory WebDirectory" +
					 " where WebDirectory.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(siblingSiteIds) + ")";
			childSiteIds = ListUtils.join(databaseService.findRecordsByHql(hql), ",", false);
			//Logger.debug("SiteColumnsProcessor hql2: " + hql);
			
			hql = "select WebDirectory.id" +
				  " from WebDirectory WebDirectory" +
				  " where WebDirectory.parentDirectoryId=" + webDirectory.getId();
			myChildSiteIds = ListUtils.join(databaseService.findRecordsByHql(hql), ",", false);
			//Logger.debug("SiteColumnsProcessor hql3: " + hql);
		}
		hql = "select distinct StaticPage" +
			  " from StaticPage StaticPage, StaticPageColumns StaticPageColumns" +
			  " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
			  " and StaticPageColumns.pageId=StaticPage.id" +
			  " and ((StaticPageColumns.columnType='0' and StaticPageColumns.siteId=" + parentDirectoryId + ")" + //子栏目
			  (childSiteIds==null ? "" : " or (StaticPageColumns.columnType='1' and StaticPageColumns.siteId in (" + JdbcUtils.validateInClauseNumbers(childSiteIds) + "))") + //父栏目及其兄弟栏目
			  (siblingSiteIds==null ? "" : " or (StaticPageColumns.columnType='2' and StaticPageColumns.siteId in (" + JdbcUtils.validateInClauseNumbers(siblingSiteIds) + "))") + //兄弟栏目
			  (myChildSiteIds==null ? "" : " or (StaticPageColumns.columnType='3' and StaticPageColumns.siteId in (" + JdbcUtils.validateInClauseNumbers(myChildSiteIds) + "))") + //父栏目
			  " or (StaticPageColumns.columnType='4' and StaticPageColumns.siteId=" + webDirectory.getId() + ")" + //栏目自己
			  ")";
		//Logger.debug("SiteColumnsProcessor hql4: " + hql);
		return databaseService.findRecordsByHql(hql, 0, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#isCurrentRecord(java.lang.Object, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.RecordList, com.yuanluesoft.cms.sitemanage.model.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected boolean isCurrentRecord(Object record, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		if(!(record instanceof WebDirectory)) {
			return false;
		}
		WebDirectory column = (WebDirectory)record;
		return (column.getId()==webDirectory.getId() || column.getId()==webDirectory.getParentDirectoryId());
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

	/**
	 * @return the imageService
	 */
	public ImageService getImageService() {
		return imageService;
	}

	/**
	 * @param imageService the imageService to set
	 */
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}
}
package com.yuanluesoft.cms.siteresource.processor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.sitemanage.model.RebuildPageByResourceSiteIds;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.pojo.WebViewReference;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceTop;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;

/**
 * 站点资源列表处理器
 * @author linchuan
 *
 */
public class SiteResourcesProcessor extends RecordListProcessor {
	private SiteService siteService; //站点管理
	private ViewDefineService viewDefineService; //视图定义服务
	private DatabaseService databaseService; //数据库服务

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, boolean, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//解析记录列表模型
		RecordList recordListModel = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, pageElement.getAttribute("urn"), null);
		//检查是否引用了其他视图
		String referenceRecordListName = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "referenceRecordListName"); //引用的视图名称
		if(referenceRecordListName!=null) {
			String siteIds = getRecordListSiteIds(recordListModel, null, pageElement, sitePage, webDirectory, parentSite, request);
			List webDirectories = (siteIds==null || siteIds.isEmpty() || siteIds.equals("-1") ? null : siteService.listDirectories(siteIds));
			if(webDirectories!=null && !webDirectories.isEmpty() && (webDirectories.get(0) instanceof WebViewReference)) {
				WebViewReference reference = (WebViewReference)webDirectories.get(0);
				//设置引用参数
				String referenceParameter = null;
				for(Iterator iterator = webDirectories.iterator(); iterator.hasNext();) {
					WebDirectory directory = (WebDirectory)iterator.next();
					if(directory instanceof WebViewReference) {
						WebViewReference viewReference = (WebViewReference)directory;
						if(viewReference.getReferenceParameter()!=null && !viewReference.getReferenceParameter().isEmpty()) {
							referenceParameter = (referenceParameter==null ? "" : referenceParameter + ",") + viewReference.getReferenceParameter(); //合并引用参数
						}
					}
				}
				request.setAttribute("referenceParameter", referenceParameter);
				
				//获取引用的视图
				SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
				View view;
				try {
					view = viewDefineService.getView(reference.getApplicationName(), reference.getViewName(), sessionInfo);
				} 
				catch(PrivilegeException e) {
					Logger.exception(e);
					throw new ServiceException();
				}
				//获取对应的记录列表
				View referenceView = getPageDefineService().getRecordList(reference.getApplicationName(), referenceRecordListName, false, null, sessionInfo);
				request.setAttribute("viewDefine", referenceView);
				
				//设置记录列表需要扩展的join子句
				String siteReferenceRecordListJoin = (String)view.getExtendParameter("siteReferenceRecordListJoin");
				if(siteReferenceRecordListJoin==null) {
					siteReferenceRecordListJoin = (String)view.getExtendParameter("siteReferenceJoin");
				}
				if(siteReferenceRecordListJoin!=null && !siteReferenceRecordListJoin.isEmpty()) {
					referenceView.addJoin(siteReferenceRecordListJoin);
				}
				//设置记录列表需要扩展的where子句
				String siteReferenceRecordListWhere = (String)view.getExtendParameter("siteReferenceRecordListWhere");
				if(siteReferenceRecordListWhere==null) {
					siteReferenceRecordListWhere = (String)view.getExtendParameter("siteReferenceWhere");
				}
				if(siteReferenceRecordListWhere!=null && !siteReferenceRecordListWhere.isEmpty()) {
					referenceView.addWhere(siteReferenceRecordListWhere);
				}
				//使用引用的记录列表的列表处理器处理记录列表
				recordListModel.setApplicationName(referenceView.getApplicationName());
				recordListModel.setRecordListName(referenceView.getName());
				RecordListUtils.setRecordListProperties(pageElement, recordListModel);
				getPageBuilder().processPageElement(pageElement, webDirectory, parentSite, sitePage, requestInfo, request);
				
				//清除属性
				request.removeAttribute("viewDefine");
				request.removeAttribute("referenceParameter");
				return;
			}
		}
		super.writePageElement(pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String siteIds = getRecordListSiteIds(recordListModel, view, pageElement, sitePage, webDirectory, parentSite, request);
		String resourceTypes = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "resourceTypes"); //解析资源类型
		boolean imageResourceOnly = "true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "imageResourceOnly")); //解析是否只显示图片文章
		boolean videoResourceOnly = "true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "videoResourceOnly")); //解析是否只显示视频文章
		boolean containChildren = "true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "containChildren")); //解析是否包含子站/栏目资源

		sitePage.setAttribute("siteIds", siteIds);
		//增加置顶判断
		String where = null;
		if(!"hotResources".equals(recordListModel.getRecordListName())) {
			String join = "left join SiteResource.resourceTops resourceTops" +
						  " on resourceTops.resourceId=SiteResource.id" +
						  " and resourceTops.siteId in (" + JdbcUtils.validateInClauseNumbers(siteIds) + ")" +
						  " and (" +
						  "  resourceTops.expire is null" +
						  "  or resourceTops.expire>=DATE(" + DateTimeUtils.formatDate(DateTimeUtils.date(), null) + "))";
			view.addJoin(join);
		}
		if(!containChildren) { //不显示子站、子栏目
			where = (where==null ? "" : where + " and ") + "subjections.siteId in (" + JdbcUtils.validateInClauseNumbers(siteIds) + ")";
		}
		else if(("," + siteIds + ",").indexOf(",0,")==-1) { //不包括主站
			view.addJoin(", WebDirectorySubjection WebDirectorySubjection");
			where = (where==null ? "" : where + " and ") + "subjections.siteId=WebDirectorySubjection.directoryId" +
					 " and WebDirectorySubjection.parentDirectoryId in (" +  JdbcUtils.validateInClauseNumbers(siteIds) + ")";
	    }
		//解析资源类型
		if(resourceTypes!=null && !resourceTypes.equals("all")) { //不是显示全部资源
			for(int i=0; i<SiteResourceService.RESOURCE_TYPE_NAMES.length; i++) {
				resourceTypes = resourceTypes.replaceFirst(SiteResourceService.RESOURCE_TYPE_NAMES[i], "" + i);
			}
			where = (where==null ? "" : where + " and ") + "SiteResource.type in (" + resourceTypes + ")";
		}
		if(imageResourceOnly) {
			where = (where==null ? "" : where + " and ") + "SiteResource.imageCount>0 and not SiteResource.firstImageName is null";
		}
		else if(videoResourceOnly) {
			where = (where==null ? "" : where + " and ") + "SiteResource.videoCount>0";
		}
		//匿名用户权限控制
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) {
			where = (where==null ? "" : where + " and ") + "SiteResource.anonymousLevel>='" + SiteResourceService.ANONYMOUS_LEVEL_SUBJECT + "'";
		}
		if(recordListModel.isSearchResults()) {
			if("1".equals(request.getParameter("hasImage"))) {
				where = (where==null ? "" : where + " and ") + "SiteResource.imageCount>0";
			}
			if("1".equals(request.getParameter("hasVideo"))) {
				where = (where==null ? "" : where + " and ") + "SiteResource.videoCount>0";
			}
		}
		view.addWhere(where);
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		RecordListData recordListData = super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		if(recordListData==null || recordListData.getRecords()==null || recordListData.getRecords().isEmpty()) {
			return recordListData;
		}
		String siteIds = (String)sitePage.getAttribute("siteIds");
		if(ListUtils.findObjectByProperty(view.getColumns(), "name", "top")!=null) { //需要显示是否置顶,则清理掉不在栏目列表中的置顶记录
			for(Iterator iterator = recordListData.getRecords().iterator(); iterator.hasNext();) {
				SiteResource resource = (SiteResource)iterator.next();
				for(Iterator iteratorTop = resource.getResourceTops()==null ? null : resource.getResourceTops().iterator(); iteratorTop!=null && iteratorTop.hasNext();) {
					SiteResourceTop top = (SiteResourceTop)iteratorTop.next();
					if(("," + siteIds + ",").indexOf("," + top.getSiteId() + ",")==-1) {
						iteratorTop.remove();
					}
				}
			}
		}
		//获取最小置顶截止时间
		String hql = "select min(SiteResourceTop.expire)" +
					 " from SiteResourceTop SiteResourceTop" +
					 " where SiteResourceTop.resourceId in (" + ListUtils.join(recordListData.getRecords(), "id", ",", false) + ")" +
					 " and SiteResourceTop.siteId in (" + JdbcUtils.validateInClauseNumbers(siteIds) + ")" +
					 " and SiteResourceTop.expire is not null";
		Date minExpire = (Date)databaseService.findRecordByHql(hql);
		if(minExpire!=null) {
			PageUtils.setPageExpiresTime(DateTimeUtils.add(minExpire, Calendar.DAY_OF_MONTH, 1), request); //设为页面有效时间
		}
		return recordListData;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordSiteIds(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected String getRecordListSiteIds(RecordList recordListModel, View view, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		if(recordListModel.isSearchResults()) { //搜索结果
			String siteIds = ListUtils.join(request.getParameterValues("searchSiteIds"), ",", true);
			if(siteIds==null || siteIds.isEmpty()) {
				siteIds = RequestUtils.getParameterStringValue(request, "searchSiteId");
			}
			if(siteIds==null || siteIds.isEmpty()) {
				siteIds = "" + parentSite.getId();
			}
			else if(siteIds.equals("-1")) {
				siteIds = "" + webDirectory.getId();
			}
			return siteIds;
		}
		//解析配置参数
		String siteIds = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "siteIds"); //解析站点ID列表
		String columnNames =  null;
		if("true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "siteSelectByName"))) { //按栏目名称
			columnNames = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "siteNames"); //解析站点名称列表
		}
		else if(siteIds==null || siteIds.equals("-1")) {
			Object record = sitePage.getAttribute("record");
			if(record!=null && record instanceof SiteResource) { //文章页面
				SiteResource siteResource = (SiteResource)record;
				for(Iterator iterator = siteResource.getSubjections().iterator(); iterator.hasNext();) {
					SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
					if(siteService.getParentSite(subjection.getSiteId()).getId()==parentSite.getId()) { //检查文章所在栏目是否属于当前站点
						siteIds = "" + subjection.getSiteId();
						break;
					}
				}
			}
			if(siteIds==null || siteIds.equals("-1")) {
				Object parentRecord = sitePage.getAttribute("parentRecord");
				if(parentRecord!=null) { //有父记录
					webDirectory = (WebDirectory)parentRecord;
					siteIds = "" + webDirectory.getId();
				}
				else if((columnNames=request.getParameter("columnName"))==null) { //没有指定栏目名称
					siteIds = "" + webDirectory.getId();
				}
			}
		}
		if(columnNames!=null && (siteIds=ListUtils.join(siteService.listDirectoryIdsByName(webDirectory.getId(), columnNames, null), ",", false))==null) {	
			//没有指定的栏目, 获取父目录的栏目
			siteIds = ListUtils.join(siteService.listDirectoryIdsByName(webDirectory.getParentDirectoryId(), columnNames, null), ",", false);
			if(siteIds==null) {
				siteIds = "-1";
			}
		}
		return siteIds;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		//检查是否引用了其他视图
		String referenceRecordListName = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "referenceRecordListName"); //引用的视图名称
		if(referenceRecordListName!=null) {
			return;
		}
		super.createStaticPageRebuildBasisByRecordList(recordListModel, view, staticPageId, pageElement, sitePage, webDirectory, parentSite, htmlParser, databaseService, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(object instanceof WebDirectory) { //目录
			if(DirectoryService.DIRECTORY_ACTION_REMOVE_CHILD.equals(modifyAction) || DirectoryService.DIRECTORY_ACTION_MOVE_CHILD.equals(modifyAction)) { //子目录移动
				WebDirectory webDirectory = (WebDirectory)object;
				String hql = "select distinct StaticPage" +
							 " from StaticPage StaticPage left join StaticPage.recordLists StaticPageRecordList, WebDirectorySubjection WebDirectorySubjection" +
							 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
							 " and StaticPageRecordList.recordClassName='" + SiteResource.class.getName() + "'" +
							 " and StaticPageRecordList.siteId=WebDirectorySubjection.parentDirectoryId" +
							 " and WebDirectorySubjection.directoryId=" + webDirectory.getId();
				return databaseService.findRecordsByHql(hql, 0, max);
			}
		}
		if(!(object instanceof SiteResource) && !(object instanceof SiteResourceSubjection) && !(object instanceof RebuildPageByResourceSiteIds)) {
			return null;
		}
		return super.listStaticPageForModifiedObject(object, modifyAction, baseTime, databaseService, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordClassNameForStaticPage(java.lang.Object)
	 */
	protected String getRecordClassNameForStaticPage(Object object) throws ServiceException {
		if((object instanceof SiteResourceSubjection) || (object instanceof RebuildPageByResourceSiteIds)) {
			return SiteResource.class.getName();
		}
		return super.getRecordClassNameForStaticPage(object);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordSiteIds(java.lang.Object, com.yuanluesoft.jeaf.database.DatabaseService)
	 */
	protected String getRecordSiteIds(Object object, DatabaseService databaseService) throws ServiceException {
		if(object instanceof SiteResourceSubjection) {
			return ((SiteResourceSubjection)object).getSiteId() + "";
		}
		else if(object instanceof SiteResource) { //站点资源
			SiteResource siteResource = (SiteResource)object;
			return ListUtils.join(siteResource.getSubjections(), "siteId", ",", false);
		}
		else if(object instanceof RebuildPageByResourceSiteIds) {
			return ((RebuildPageByResourceSiteIds)object).getSiteIds();
		}
		return null;
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
package com.yuanluesoft.cms.onlineservice.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.onlineservice.model.OnlineServiceItemType;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemSubjection;
import com.yuanluesoft.cms.onlineservice.pojo.StaticPageOnlineServiceItems;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 网上办事办理事项列表处理器
 * @author linchuan
 *
 */
public class OnlineServiceItemsProcessor extends RecordListProcessor {
	private OnlineServiceDirectoryService onlineServiceDirectoryService;
	private DatabaseService databaseService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String directoryIds = getOnlineServiceDirectoryIds(recordListModel, recordListModel.getExtendProperties(), sitePage, parentSite, request);
		directoryIds = onlineServiceDirectoryService.getChildDirectoryIds(directoryIds, "directory");
		
		//修改join
		view.addJoin(", OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection");
		//修改where
		String where = "OnlineServiceDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		if(!"onlineServiceDownloads".equals(view.getName()) && !"onlineServiceExampleDownloads".equals(view.getName())) { //办理事项
			where += " and subjections.directoryId=OnlineServiceDirectorySubjection.directoryId";
		}
		else { //表格下载
			view.setJoin(view.getJoin() + ", OnlineServiceItemSubjection OnlineServiceItemSubjection");
			where += " and item.id=OnlineServiceItemSubjection.itemId" +
					 " and OnlineServiceItemSubjection.directoryId=OnlineServiceDirectorySubjection.directoryId";
		}
		view.addWhere(where);
		
		//为行政职权目录添加事项类型过滤
		String itemTypes = getItemTypes(recordListModel, sitePage, request);
		if(itemTypes!=null) {
			view.addWhere("OnlineServiceItem.itemType in ('" + JdbcUtils.resetQuot(itemTypes).replaceAll(",", "','") + "')");
		}
		
		//为行政职权目录添加公共服务类型过滤
		String publicServiceType = request.getParameter("publicServiceType");
		if(publicServiceType!=null && !publicServiceType.isEmpty() && !"全部".equals(publicServiceType)) {
			view.addWhere("OnlineServiceItem.publicServiceType='" + JdbcUtils.resetQuot(publicServiceType) + "'");
		}
		
		
		if(recordListModel.isSearchResults()) { //搜索结果
			String searchContent = request.getParameter("searchContent"); //办事事项\0表格下载\0样表下载\0办事指南\0常见问题
			if("办事事项".equals(searchContent)) {
				view.setQuickFilter("OnlineServiceItem.name like '%{KEY}%'");
			}
			else if("办事指南".equals(searchContent)) {
				view.setQuickFilter("guide.condition like '%{KEY}%'" + //申办条件
									" or guide.according like '%{KEY}%'" + //办理依据
									" or guide.program like '%{KEY}%'" + //办理程序
									" or guide.timeLimit like '%{KEY}%'" + //承诺时限
									" or guide.chargeAccording like '%{KEY}%'" + //收费依据
									" or guide.chargeStandard like '%{KEY}%'" + //收费标准
									" or guide.legalRight like '%{KEY}%'" + //法律权利,申请人法律权利及申诉途径
									" or guide.address like '%{KEY}%'" + //办理地点
									" or guide.traffic like '%{KEY}%'" + //交通线路
									" or materials.name like '%{KEY}%'" +  //材料
									" or units.unitName like '%{KEY}%'"); //办理机构
			}
		}
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#isAlwaysWriteSearchResults(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected boolean isAlwaysWriteSearchResults(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) {
		return request.getParameter("key")!=null && request.getParameter("directoryId")!=null;
	}

	/**
	 * 获取目录ID
	 * @param extendProperties
	 * @param sitePage
	 * @param parentSite
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private String getOnlineServiceDirectoryIds(RecordList recordListModel, String extendProperties, SitePage sitePage, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		//引用参数
		String referenceParameter = (String)request.getAttribute("referenceParameter");
		if(referenceParameter!=null) {
			return referenceParameter;
		}
		//获取职权目录ID
		long authorityDirectoryId = RequestUtils.getParameterLongValue(request, "authorityDirectoryId");
		if(authorityDirectoryId>0) {
			return "" + authorityDirectoryId;
		}
		//获取当前需要输出的目录
		long directoryId = StringUtils.getPropertyLongValue(extendProperties, "directoryId", -1);
		if(directoryId==-1) { //没有指定目录
			Object parentRecord = sitePage.getAttribute("parentRecord"); //嵌套时有效
			if(parentRecord!=null && (parentRecord instanceof OnlineServiceDirectory)) {
				directoryId = ((OnlineServiceDirectory)parentRecord).getId();
			}
			else {
				String directoryIds = ListUtils.join(request.getParameterValues("directoryIds"), ",", true); //搜索目录列表
				if(directoryIds!=null && !directoryIds.isEmpty()) {
					return directoryIds;
				}
				directoryId = RequestUtils.getParameterLongValue(request, "id");
				if(directoryId==0) {
					directoryId = RequestUtils.getParameterLongValue(request, "directoryId");
				}
			}
			if(directoryId==0) {
				//按站点获取根目录
				OnlineServiceDirectory directory = onlineServiceDirectoryService.getDirectoryBySiteId(parentSite.getId());
				directoryId = (directory==null ? 0 : directory.getId());
			}
		}
		return "" + directoryId;
	}
	
	/**
	 * 获取事项类型
	 * @param recordListModel
	 * @param sitePage
	 * @param request
	 * @return
	 */
	private String getItemTypes(RecordList recordListModel, SitePage sitePage, HttpServletRequest request) {
		String[] itemTypes = request.getParameterValues("itemTypes");
		if(itemTypes!=null) {
			String types = ListUtils.join(itemTypes, ",", false);
			return types==null || types.isEmpty() || types.indexOf("全部")!=-1 ? null : types;
		}
		if(!"authorityItems".equals(recordListModel.getRecordListName())) {
			return null;
		}
		String itemType;
		Object parentRecord = sitePage.getAttribute("parentRecord"); //嵌套时有效
		if(parentRecord!=null && (parentRecord instanceof OnlineServiceItemType)) {
			itemType = ((OnlineServiceItemType)parentRecord).getItemType();
		}
		else {
			itemType = request.getParameter("authorityItemType");
		}
		if(itemType!=null && !itemType.isEmpty() && !itemType.equals("全部")) {
			return itemType;
		}
		else {
			return "行政许可,非行政许可,行政处罚,行政确认,行政征收,行政强制,行政裁决,行政监督检查,行政给付,其他行政行为";
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		Object record = sitePage.getAttribute("record");
		if(record!=null && (record instanceof OnlineServiceItem) && "recordList".equals(pageElement.getId())) { //办理指南页面，且是内嵌的记录列表（如：受理单位、表格下载），不更新
			if(recordListModel.isPrivateRecordList()) { //私有记录列表
				return;
			}
			else if(("onlineServiceMaterials".equals(recordListModel.getRecordListName()) ||
					 "onlineServiceDownloads".equals(recordListModel.getRecordListName()) ||
					 "onlineServiceExampleDownloads".equals(recordListModel.getRecordListName())) &&
				    StringUtils.getPropertyLongValue(recordListModel.getExtendProperties(), "directoryId", -1)==-1) { //办事指南页面的资料下载
				return;
			}
		}
		String extendProperties = StringUtils.getPropertyValue(pageElement.getAttribute("urn"), "extendProperties");
		String[] directoryIds = getOnlineServiceDirectoryIds(recordListModel, extendProperties, sitePage, parentSite, request).split(",");
		String itemTypes = getItemTypes(recordListModel, sitePage, request);
		for(int i=0; i<directoryIds.length; i++) {
			//检查是否已经创建过
			String hql = "select StaticPageOnlineServiceItems.id" +
						 " from StaticPageOnlineServiceItems StaticPageOnlineServiceItems" +
						 " where StaticPageOnlineServiceItems.pageId=" + staticPageId +
						 " and StaticPageOnlineServiceItems.directoryId=" + directoryIds[i] +
						 (itemTypes==null ? " and StaticPageOnlineServiceItems.itemTypes is null" : " and StaticPageOnlineServiceItems.itemTypes='," + itemTypes + ",'");
			if(databaseService.findRecordByHql(hql)==null) {
				//保存引用的记录列表
				StaticPageOnlineServiceItems staticOnlineServiceItems = new StaticPageOnlineServiceItems();
				staticOnlineServiceItems.setId(UUIDLongGenerator.generateId()); //页面ID
				staticOnlineServiceItems.setPageId(staticPageId); //页面ID
				staticOnlineServiceItems.setProcessorClassName(this.getClass().getName()); //处理器类名称
				staticOnlineServiceItems.setDirectoryId(Long.parseLong(directoryIds[i])); //目录ID
				staticOnlineServiceItems.setItemTypes(itemTypes==null ? null : "," + itemTypes + ","); //事项类型
				databaseService.saveRecord(staticOnlineServiceItems);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		String directoryIds = null;
		String itemType = null;
		if(object instanceof OnlineServiceDirectory) { //目录变更
			if(DirectoryService.DIRECTORY_ACTION_MOVE_CHILD.equals(modifyAction) || DirectoryService.DIRECTORY_ACTION_REMOVE_CHILD.equals(modifyAction)) {
				directoryIds = ((OnlineServiceDirectory)object).getId() + "";
			}
		}
		else if(object instanceof OnlineServiceItem) {
			OnlineServiceItem item = (OnlineServiceItem)object;
			directoryIds = ListUtils.join(item.getSubjections(), "directoryId", ",", false); //所在目录
			itemType = item.getItemType();
		}
		else if(object instanceof OnlineServiceItemSubjection) {
			directoryIds = "" + ((OnlineServiceItemSubjection)object).getDirectoryId();
		}
		if(directoryIds==null) {
			return null;
		}
		//获取引用了项目列表的页面
		String hql = "select distinct StaticPage" +
					 " from StaticPage StaticPage, StaticPageOnlineServiceItems StaticPageOnlineServiceItems, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
					 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
					 " and StaticPageOnlineServiceItems.pageId=StaticPage.id" +
					 " and StaticPageOnlineServiceItems.directoryId=OnlineServiceDirectorySubjection.parentDirectoryId" +
					 " and OnlineServiceDirectorySubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		if(itemType!=null && !itemType.isEmpty()) {
			hql += " and (StaticPageOnlineServiceItems.itemTypes is null" +
				   "  or StaticPageOnlineServiceItems.itemTypes like '%," + itemType + ",%')";
		}
		return databaseService.findRecordsByHql(hql, 0, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		Object record = sitePage.getAttribute("record");
		if(record==null || !(record instanceof OnlineServiceItem)) { //不是办理事项
			return super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		}
		OnlineServiceItem item = (OnlineServiceItem)record;
		Set recordList = null;
		if("onlineServiceDownloads".equals(view.getName())) { //表格下载
			recordList = item.getDownloads();
		}
		else if("onlineServiceExampleDownloads".equals(view.getName())) { //样表下载
			recordList = item.getExampleDownloads();
		}
		else if("onlineServiceMaterials".equals(view.getName()) && item.getMaterials()!=null && !item.getMaterials().isEmpty()) { //申报材料
			recordList = new LinkedHashSet();
			for(Iterator iterator = item.getMaterials().iterator(); iterator.hasNext();) {
				OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)iterator.next();
				if(material.getName()!=null && !material.getName().trim().isEmpty()) {
					recordList.add(material);
				}
			}
		}
		return recordList==null ? null : new RecordListData(new ArrayList(recordList), recordList.size());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordUrl(com.yuanluesoft.jeaf.view.model.View, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, com.yuanluesoft.cms.pagebuilder.model.RecordList, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, boolean, javax.servlet.http.HttpServletRequest)
	 */
	protected String getRecordUrl(View view, Object record, String linkTitle, SitePage sitePage, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String url = super.getRecordUrl(view, record, linkTitle, sitePage, recordListModel, webDirectory, parentSite, requestInfo, request);
		if(record instanceof OnlineServiceItemMaterial) { //申报材料
			OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)record;
			if("表格下载".equals(linkTitle)) {
				if(material.getTableURL()==null || material.getTableURL().equals("")) {
					return null;
				}
				else {
					return "{FINAL}" + material.getTableURL();
				}
			}
			else if("样表下载".equals(linkTitle)) {
				if(material.getExampleURL()==null || material.getExampleURL().equals("")) {
					return null;
				}
				else {
					return "{FINAL}" + material.getExampleURL();
				}
			}
		}
		else if((record instanceof OnlineServiceItem)) { //办理事项
			OnlineServiceItem onlineServiceItem = (OnlineServiceItem)record;
			if("表格下载".equals(linkTitle)) {
				//获取表格下载列表
				String hql = "from OnlineServiceItemMaterial OnlineServiceItemMaterial" +
							 " where OnlineServiceItemMaterial.itemId=" + onlineServiceItem.getId() +
							 " and not OnlineServiceItemMaterial.tableURL is null";
				Collection downloads = databaseService.findRecordsByHql(hql, 0, 2);
				if(downloads==null || downloads.isEmpty()) {
					return null;
				}
				if(downloads.size()==1) { //只有一个下载
					OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)downloads.iterator().next();
					if(material.getExampleURL()==null || material.getExampleURL().equals("")) { //没有样表下载
						return "{FINAL}" + material.getTableURL();
					}
				}
			}
			else if("常见问题解答".equals(linkTitle)) {
				//检查有没有常见问题
				String hql = "select OnlineServiceItemFaq.id" +
							 " from OnlineServiceItemFaq OnlineServiceItemFaq" +
							 " where OnlineServiceItemFaq.itemId=" + onlineServiceItem.getId();
				if(getDatabaseService().findRecordByHql(hql)==null) {
					return null;
				}
			}
		}
		return url;
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
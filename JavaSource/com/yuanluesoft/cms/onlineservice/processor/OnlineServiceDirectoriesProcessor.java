package com.yuanluesoft.cms.onlineservice.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.onlineservice.model.OnlineServiceItemType;
import com.yuanluesoft.cms.onlineservice.model.PublicServiceType;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.StaticPageOnlineServiceDirectories;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
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
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 网上办事目录列表处理器
 * @author linchuan
 *
 */
public class OnlineServiceDirectoriesProcessor extends RecordListProcessor {
	private OnlineServiceDirectoryService onlineServiceDirectoryService; //网上办事目录服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		boolean isAuthorityTypes = "authorityTypes".equals(view.getName());
		if(isAuthorityTypes || "publicServiceTypes".equals(view.getName())) { //行政职权类别,公共服务类别
			List items = FieldUtils.listSelectItems(FieldUtils.getRecordField(OnlineServiceItem.class.getName(), isAuthorityTypes ? "itemType" : "publicServiceType", request), null, request);
			//转换为OnlineServiceItemType/PublicServiceType列表
			List itemTypes = new ArrayList();
			if("true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "showAll"))) {
				itemTypes.add(isAuthorityTypes ? (Object)new OnlineServiceItemType("全部") : new PublicServiceType("全部"));
			}
			for(Iterator iterator = items.iterator(); iterator.hasNext();) {
				String[] values = (String[])iterator.next();
				itemTypes.add(isAuthorityTypes ? (Object)new OnlineServiceItemType(values[0]) : new PublicServiceType(values[0]));
			}
			return new RecordListData(itemTypes);
		}
		long directoryId = getOnlineServiceDirectoryId(recordListModel.getExtendProperties(), sitePage, parentSite, request);
		String directoryType = "authorityDirectories".equals(view.getName()) ? "child" : StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "type");
		if("child".equals(directoryType)) { //子目录列表
			List records = onlineServiceDirectoryService.listChildDirectories(directoryId, "directory", null, "OnlineServiceDirectory.halt!='1'", false, false, null, beginRow, recordListModel.getRecordCount());
			if("authorityDirectories".equals(view.getName()) && "true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "showAll"))) {
				OnlineServiceDirectory directory = new OnlineServiceDirectory();
				directory.setId(0);
				directory.setDirectoryName("全部");
				if(records==null) {
					records = new ArrayList();
				}
				records.add(0, directory);
			}
			return new RecordListData(records);
		}
		else if("sibling".equals(directoryType)) { //兄弟目录列表
			return new RecordListData(onlineServiceDirectoryService.listChildDirectories(onlineServiceDirectoryService.getDirectory(directoryId).getParentDirectoryId(), "directory", null, "OnlineServiceDirectory.halt!='1'", false, false, null, beginRow, recordListModel.getRecordCount()));
		}
		else { //父目录列表
			return new RecordListData(onlineServiceDirectoryService.listChildDirectories(onlineServiceDirectoryService.getDirectory(onlineServiceDirectoryService.getDirectory(directoryId).getParentDirectoryId()).getParentDirectoryId(), "directory", null, "OnlineServiceDirectory.halt!='1'", false, false, null, beginRow, recordListModel.getRecordCount()));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#isCurrentRecord(java.lang.Object, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected boolean isCurrentRecord(Object record, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		if("authorityTypes".equals(recordListModel.getRecordListName())) { //行政职权类别
			OnlineServiceItemType itemType = (OnlineServiceItemType)record;
			String authorityItemType = request.getParameter("authorityItemType");
			return (itemType.getItemType().equals(authorityItemType==null || authorityItemType.isEmpty() ? "全部" : authorityItemType));
		}
		else if("publicServiceTypes".equals(recordListModel.getRecordListName())) { //公共服务类别
			PublicServiceType type = (PublicServiceType)record;
			String publicServiceType = request.getParameter("publicServiceType");
			return (type.getType().equals(publicServiceType==null || publicServiceType.isEmpty() ? "全部" : publicServiceType));
		}
		else if("authorityDirectories".equals(recordListModel.getRecordListName())) { //行政职权部门目录
			OnlineServiceDirectory directory = (OnlineServiceDirectory)record;
			return directory.getId()==RequestUtils.getParameterLongValue(request, "authorityDirectoryId");
		}
		else if(record instanceof OnlineServiceDirectory) {
			OnlineServiceDirectory directory = (OnlineServiceDirectory)record;
			return directory.getId()==RequestUtils.getParameterLongValue(request, "directoryId") ||  directory.getId()==RequestUtils.getParameterLongValue(request, "id");
		}
		return super.isCurrentRecord(record, pageElement, recordListModel, sitePage, webDirectory, parentSite, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		if("authorityTypes".equals(view.getName()) || "publicServiceTypes".equals(view.getName())) { //行政职权类别,公共服务类别
			return;
		}
		long directoryId = getOnlineServiceDirectoryId(recordListModel.getExtendProperties(), sitePage, parentSite, request);
		String directoryType = "authorityDirectories".equals(view.getName()) ? "child" : StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "type");
		char type = "parent".equals(directoryType) ? '1' : ("sibling".equals(directoryType) ? '2' : '0');
		//检查是否已经创建过
		String hql = "select StaticPageOnlineServiceDirectories.id" +
					 " from StaticPageOnlineServiceDirectories StaticPageOnlineServiceDirectories" +
					 " where StaticPageOnlineServiceDirectories.pageId=" + staticPageId +
					 " and StaticPageOnlineServiceDirectories.directoryId=" + directoryId  +
					 " and StaticPageOnlineServiceDirectories.directoryType='" + type + "'"; //0/子目录,1/父目录,2/兄弟目录
		if(databaseService.findRecordByHql(hql)==null) {
			//保存引用的记录列表
			StaticPageOnlineServiceDirectories staticOnlineServiceDirectories = new StaticPageOnlineServiceDirectories();
			staticOnlineServiceDirectories.setId(UUIDLongGenerator.generateId()); //页面ID
			staticOnlineServiceDirectories.setPageId(staticPageId); //页面ID
			staticOnlineServiceDirectories.setProcessorClassName(this.getClass().getName()); //处理器类名称
			staticOnlineServiceDirectories.setDirectoryId(directoryId); //目录ID
			staticOnlineServiceDirectories.setDirectoryType(type);
			databaseService.saveRecord(staticOnlineServiceDirectories);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof OnlineServiceDirectory)) { //不是网上办事目录
			return null;
		}
		OnlineServiceDirectory directory = (OnlineServiceDirectory)object;
		//获取父目录ID
		long parentDirectoryId = (DirectoryService.DIRECTORY_ACTION_ADD_CHILD.equals(modifyAction) || DirectoryService.DIRECTORY_ACTION_MOVE_CHILD.equals(modifyAction) || DirectoryService.DIRECTORY_ACTION_REMOVE_CHILD.equals(modifyAction) ? directory.getId() : directory.getParentDirectoryId());
		//获取兄弟目录ID
		String hql = "select OnlineServiceDirectory.id" +
					 " from OnlineServiceDirectory OnlineServiceDirectory" +
					 " where OnlineServiceDirectory.parentDirectoryId=" + parentDirectoryId;
		String siblingDirectoryIds = ListUtils.join(databaseService.findRecordsByHql(hql), ",", false);
		String childDirectoryIds = null;
		if(StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE.equals(modifyAction)) { //更新,获取第一级子目录ID
			hql = "select OnlineServiceDirectory.id" +
				  " from OnlineServiceDirectory OnlineServiceDirectory" +
				  " where OnlineServiceDirectory.parentDirectoryId=" + directory.getId();
			childDirectoryIds = ListUtils.join(databaseService.findRecordsByHql(hql), ",", false);
		}
		hql = "select distinct StaticPage" +
			  " from StaticPage StaticPage, StaticPageOnlineServiceDirectories StaticPageOnlineServiceDirectories" +
			  " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
			  " and StaticPageOnlineServiceDirectories.pageId=StaticPage.id" +
			  " and ((StaticPageOnlineServiceDirectories.directoryType='0' and StaticPageOnlineServiceDirectories.directoryId=" + parentDirectoryId + ")" + //子目录
			  (childDirectoryIds==null ? "" : " or (StaticPageOnlineServiceDirectories.directoryType='1' and StaticPageOnlineServiceDirectories.directoryId in (" + JdbcUtils.validateInClauseNumbers(childDirectoryIds) + "))") + //父目录
			  (siblingDirectoryIds==null ? "" : " or (StaticPageOnlineServiceDirectories.directoryType='2' and StaticPageOnlineServiceDirectories.directoryId in (" + JdbcUtils.validateInClauseNumbers(siblingDirectoryIds) + "))") + //兄弟目录
			  ")";
		return databaseService.findRecordsByHql(hql, 0, max);
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
	private long getOnlineServiceDirectoryId(String extendProperties, SitePage sitePage, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		long directoryId = StringUtils.getPropertyLongValue(extendProperties, "directoryId", -1);
		if(directoryId!=-1) {
			return directoryId;
		}
		Object parentRecord = sitePage.getAttribute("parentRecord"); //嵌套时有效
		if(parentRecord!=null && (directoryId=((OnlineServiceDirectory)parentRecord).getId())!=-1) {
			return directoryId;
		}
		if(request.getParameter("id")!=null && (directoryId=RequestUtils.getParameterLongValue(request, "id"))!=-1) { //URL中指定的目录
			return directoryId;
		}
		if(request.getParameter("directoryId")!=null && (directoryId=RequestUtils.getParameterLongValue(request, "directoryId"))!=-1) { //URL中指定的目录
			return directoryId;
		}
		//按站点获取根目录
		OnlineServiceDirectory directory = onlineServiceDirectoryService.getDirectoryBySiteId(parentSite.getId());
		return (directory==null ? 0 : directory.getId());
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
}
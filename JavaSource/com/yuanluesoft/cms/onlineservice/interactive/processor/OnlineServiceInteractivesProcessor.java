package com.yuanluesoft.cms.onlineservice.interactive.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.publicservice.processor.PublicServicesProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceInteractivesProcessor extends PublicServicesProcessor {
	private OnlineServiceDirectoryService onlineServiceDirectoryService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String pojoClassName = view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf('.') + 1);
		long itemId = RequestUtils.getParameterLongValue(request, "itemId");
		String unitName;
		if(itemId>0) {
			view.addWhere(pojoClassName + ".itemId=" + itemId);
		}
		else if((unitName = RequestUtils.getParameterStringValue(request, "unitName"))!=null && !unitName.isEmpty()) { //检查URL中是否有单位名称
			//修改join
			view.addJoin(",OnlineServiceItemUnit OnlineServiceItemUnit");
			//修改where
			String where = "OnlineServiceItemUnit.itemId=" + pojoClassName + ".itemId" +
						   " and OnlineServiceItemUnit.unitName='" + JdbcUtils.resetQuot(unitName) + "'";
			view.addWhere(where);
		}
		else {
			String directoryIds = getOnlineServiceDirectoryIds(recordListModel.getExtendProperties(), sitePage, parentSite, request);
			directoryIds = onlineServiceDirectoryService.getChildDirectoryIds(directoryIds, "directory");
			//修改join
			view.addJoin(",OnlineServiceItemSubjection OnlineServiceItemSubjection, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection");
			//修改where
			String where = "OnlineServiceItemSubjection.itemId=" + pojoClassName + ".itemId" +
			 			   " and OnlineServiceItemSubjection.directoryId=OnlineServiceDirectorySubjection.directoryId" +
			 			   " and OnlineServiceDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
			view.addWhere(where);
		}
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		long itemId = RequestUtils.getParameterLongValue(request, "itemId");
		if(itemId>0) { //和办理事项关联
			//检查是否已经创建过
			String hql = "select StaticPageOnlineServiceInteractives.id" +
						 " from StaticPageOnlineServiceInteractives StaticPageOnlineServiceInteractives" +
						 " where StaticPageOnlineServiceInteractives.pageId=" + staticPageId +
						 " and StaticPageOnlineServiceInteractives.recordClassName='" + view.getPojoClassName() + "'" +
						 " and StaticPageOnlineServiceInteractives.siteId=" + parentSite.getId() +
						 " and StaticPageOnlineServiceInteractives.itemId=" + itemId;
			if(databaseService.findRecordByHql(hql)==null) {
				//保存引用的记录列表
				StaticPageOnlineServiceInteractives staticOnlineServiceInteractives = new StaticPageOnlineServiceInteractives();
				staticOnlineServiceInteractives.setId(UUIDLongGenerator.generateId()); //页面ID
				staticOnlineServiceInteractives.setPageId(staticPageId); //页面ID
				staticOnlineServiceInteractives.setRecordClassName(view.getPojoClassName()); //记录类名称
				staticOnlineServiceInteractives.setProcessorClassName(this.getClass().getName()); //处理器类名称
				staticOnlineServiceInteractives.setSiteId(parentSite.getId()); //站点ID
				staticOnlineServiceInteractives.setItemId(itemId); //办理事项ID
				databaseService.saveRecord(staticOnlineServiceInteractives);
			}
		}
		else {
			//获取扩展属性
			String extendProperties = StringUtils.getPropertyValue(pageElement.getAttribute("urn"), "extendProperties");
			//获取隶属目录ID
			String[] directoryIds = getOnlineServiceDirectoryIds(extendProperties, sitePage, parentSite, request).split(",");
			for(int i=0; i<directoryIds.length; i++) {
				//检查是否已经创建过
				String hql = "select StaticPageOnlineServiceInteractives.id" +
							 " from StaticPageOnlineServiceInteractives StaticPageOnlineServiceInteractives" +
							 " where StaticPageOnlineServiceInteractives.pageId=" + staticPageId +
							 " and StaticPageOnlineServiceInteractives.recordClassName='" + view.getPojoClassName() + "'" +
							 " and StaticPageOnlineServiceInteractives.siteId=" + parentSite.getId() +
							 " and StaticPageOnlineServiceInteractives.directoryId=" + directoryIds[i];
				if(databaseService.findRecordByHql(hql)==null) {
					//保存引用的记录列表
					StaticPageOnlineServiceInteractives staticOnlineServiceInteractives = new StaticPageOnlineServiceInteractives();
					staticOnlineServiceInteractives.setId(UUIDLongGenerator.generateId()); //页面ID
					staticOnlineServiceInteractives.setPageId(staticPageId); //页面ID
					staticOnlineServiceInteractives.setRecordClassName(view.getPojoClassName()); //记录类名称
					staticOnlineServiceInteractives.setProcessorClassName(this.getClass().getName()); //处理器类名称
					staticOnlineServiceInteractives.setSiteId(parentSite.getId()); //站点ID
					staticOnlineServiceInteractives.setDirectoryId(Long.parseLong(directoryIds[i])); //目录ID
					databaseService.saveRecord(staticOnlineServiceInteractives);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof OnlineServiceInteractive)) { //不是网上办事互动
			return null;
		}
		OnlineServiceInteractive interactive = (OnlineServiceInteractive)object;
		//获取引用了项目列表的页面
		String hql = "select distinct StaticPage" +
					 " from StaticPage StaticPage," +
					 "  StaticPageOnlineServiceInteractives StaticPageOnlineServiceInteractives," +
					 "  OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection," +
					 "  OnlineServiceItemSubjection OnlineServiceItemSubjection" +
					 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
					 (interactive instanceof OnlineServiceAccept ? "" :  " and StaticPageOnlineServiceInteractives.siteId=" + interactive.getSiteId()) + //在线申报不按站点
					 " and StaticPageOnlineServiceInteractives.recordClassName='" + interactive.getClass().getName() + "'" +
					 " and StaticPageOnlineServiceInteractives.pageId=StaticPage.id" +
					 " and OnlineServiceDirectorySubjection.directoryId=OnlineServiceItemSubjection.directoryId" +
					 " and OnlineServiceItemSubjection.itemId=" + interactive.getItemId() +
				     " and StaticPageOnlineServiceInteractives.directoryId=OnlineServiceDirectorySubjection.parentDirectoryId" +
					 " and StaticPageOnlineServiceInteractives.itemId in (" + interactive.getItemId() + ",0)";
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
	private String getOnlineServiceDirectoryIds(String extendProperties, SitePage sitePage, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		//引用参数
		String referenceParameter = (String)request.getAttribute("referenceParameter");
		if(referenceParameter!=null) {
			return referenceParameter;
		}
		//获取当前需要输出的目录
		long directoryId = StringUtils.getPropertyLongValue(extendProperties, "directoryId", -1);
		if(directoryId==-1) { //没有指定目录
			Object parentRecord = sitePage.getAttribute("parentRecord"); //嵌套时有效
			directoryId = parentRecord==null ? RequestUtils.getParameterLongValue(request, "id") : ((OnlineServiceDirectory)parentRecord).getId();
			if(directoryId==0) {
				//按站点获取根目录
				OnlineServiceDirectory directory = onlineServiceDirectoryService.getDirectoryBySiteId(parentSite.getId());
				directoryId = (directory==null ? 0 : directory.getId());
			}
		}
		return "" + directoryId;
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
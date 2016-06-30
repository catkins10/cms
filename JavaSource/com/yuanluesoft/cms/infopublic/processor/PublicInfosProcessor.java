package com.yuanluesoft.cms.infopublic.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.infopublic.model.InfoDirectoryDownload;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoSubjection;
import com.yuanluesoft.cms.infopublic.pojo.StaticPageInfos;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
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
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;

/**
 * 信息公开列表处理器
 * @author linchuan
 *
 */
public class PublicInfosProcessor extends RecordListProcessor {
	private PublicDirectoryService publicDirectoryService; //信息公开目录服务
	private DatabaseService databaseService; //数据库服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		if("infoDirectoryDownloads".equals(view.getName())) { //信息公开目录下载
			//解析目录ID列表
			String directoryIds = getDirectoryIds(recordListModel.getExtendProperties(), parentSite, request);
			String hql = "select year(PublicInfo.generateDate), PublicInfo.infoFrom" +
						 " from PublicInfo PublicInfo left join PublicInfo.subjections subjections, PublicDirectorySubjection PublicDirectorySubjection" +
						 " where subjections.directoryId=PublicDirectorySubjection.directoryId" +
						 " and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(publicDirectoryService.getChildDirectoryIds(directoryIds, "category,info")) + ")" +
						 " and PublicInfo.type=0" +
						 " and PublicInfo.status='3'" +
						 " group by year(PublicInfo.generateDate), PublicInfo.infoFrom" +
						 " order by year(PublicInfo.generateDate), PublicInfo.infoFrom";
			List records = databaseService.findRecordsByHql(hql);
			if(records==null || records.isEmpty()) {
				return null;
			}
			for(int i=0; i<records.size(); i++) {
				Object[] values = (Object[])records.get(i);
				if(values[0]==null || values[1]==null) {
					records.remove(i);
					i--;
					continue;
				}
				InfoDirectoryDownload infoDirectoryDownload = new InfoDirectoryDownload();
				infoDirectoryDownload.setUnitName((String)values[1]); //单位名称
				infoDirectoryDownload.setYear(((Number)values[0]).intValue()); //年度
				infoDirectoryDownload.setName(infoDirectoryDownload.getUnitName() + infoDirectoryDownload.getYear() + "年政府信息公开目录"); //名称
				records.set(i, infoDirectoryDownload);
			}
			new RecordListData(records, records.size());
	    }
		return super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#generateSearchConditions(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected List generateSearchConditions(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		List searchConditions = super.generateSearchConditions(view, recordListModel, webDirectory, parentSite, sitePage, request);
		if(searchConditions==null) {
			searchConditions = new ArrayList();
		}
		String searchKey = request.getParameter("searchKey");
		if(searchKey!=null && !searchKey.isEmpty()) {
			String searchMode = request.getParameter("searchMode"); //标题\0正文\0文号\0发布机构\0索引号
			String fieldName = "subject";
			if("正文".equals(searchMode)) {
				fieldName = "lazyBody.body";
			}
			else if("文号".equals(searchMode)) {
				fieldName = "mark";
			}
			else if("发布机构".equals(searchMode)) {
				fieldName = "infoFrom";
			}
			else if("索引号".equals(searchMode)) {
				fieldName = "infoIndex";
			}
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, fieldName, "string", Condition.CONDITION_EXPRESSION_CONTAIN, searchKey, null));
		}
		int year = RequestUtils.getParameterIntValue(request, "year");
		if(year>0) {
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL, "year(PublicInfo.generateDate)=" + year, null));
		}
		return searchConditions;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		//解析目录ID列表
		String directoryIds = getDirectoryIds(recordListModel.getExtendProperties(), parentSite, request);
		//解析是否包含子目录资源
		boolean containChildren  = directoryIds==null ? true : !"publicArticles".equals(view.getName()) && "true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "containChildren"));
		String where;
		if(!containChildren) { //不显示子目录的信息
			where = "subjections.directoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		}
		else { //显示子目录的信息
			view.addJoin(", PublicDirectorySubjection PublicDirectorySubjection");
			where = "subjections.directoryId=PublicDirectorySubjection.directoryId" +
					" and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(publicDirectoryService.getChildDirectoryIds(directoryIds, "category,info")) + ")";
	    }
		view.addWhere(where);
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		//解析目录ID列表
		String directoryIds = getDirectoryIds(recordListModel.getExtendProperties(), parentSite, request);
		String[] ids = (directoryIds==null ? "0" : directoryIds).split(",");
		for(int i=0; i<ids.length; i++) {
			//检查是否已经创建过
			String hql = "select StaticPageInfos.id" +
						 " from StaticPageInfos StaticPageInfos" +
						 " where StaticPageInfos.pageId=" + staticPageId +
						 " and StaticPageInfos.directoryId=" + ids[i];
			if(databaseService.findRecordByHql(hql)==null) {
				//保存引用的记录列表
				StaticPageInfos staticPageInfos = new StaticPageInfos();
				staticPageInfos.setId(UUIDLongGenerator.generateId()); //页面ID
				staticPageInfos.setPageId(staticPageId); //页面ID
				staticPageInfos.setProcessorClassName(this.getClass().getName()); //处理器类名称
				staticPageInfos.setSiteId(0); //隶属的站点/栏目ID
				staticPageInfos.setDirectoryId(Long.parseLong(ids[i])); //隶属的论坛目录ID
				databaseService.saveRecord(staticPageInfos);
			}
		}
	}
	
	/**
	 * 获取目录ID列表
	 * @param extendProperties
	 * @param parentSite
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private String getDirectoryIds(String extendProperties, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		String directoryIds = StringUtils.getPropertyValue(extendProperties, "directoryIds");
		if(directoryIds!=null && !"-1".equals(directoryIds)) {
			return directoryIds;
		}
		if((directoryIds = request.getParameter("directoryId"))!=null && !directoryIds.isEmpty()) { //有指定目录
			return directoryIds;
		}
		//按站点获取主目录
		return publicDirectoryService.getMainDirectoryBySite(parentSite.getId()).getId() + "";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		String directoryIds = null;
		if(object instanceof PublicDirectory) { //目录变更
			if(DirectoryService.DIRECTORY_ACTION_MOVE_CHILD.equals(modifyAction) || DirectoryService.DIRECTORY_ACTION_REMOVE_CHILD.equals(modifyAction)) {
				directoryIds = ((PublicDirectory)object).getId() + "";
			}
		}
		else if(object instanceof PublicInfo) {
			PublicInfo info = (PublicInfo)object;
			directoryIds = ListUtils.join(info.getSubjections(), "directoryId", ",", false); //信息所在目录
		}
		else if(object instanceof PublicInfoSubjection) {
			directoryIds = "" + ((PublicInfoSubjection)object).getDirectoryId();
		}
		if(directoryIds==null) {
			return null;
		}
		//获取引用了信息列表的页面
		String hql = "select distinct StaticPage" +
					 " from StaticPage StaticPage, StaticPageInfos StaticPageInfos, PublicDirectorySubjection PublicDirectorySubjection" +
					 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
					 " and StaticPageInfos.pageId=StaticPage.id" +
					 " and StaticPageInfos.directoryId=PublicDirectorySubjection.parentDirectoryId" +
					 " and PublicDirectorySubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		return databaseService.findRecordsByHql(hql, 0, max);
	}

	/**
	 * @return the publicDirectoryService
	 */
	public PublicDirectoryService getPublicDirectoryService() {
		return publicDirectoryService;
	}

	/**
	 * @param publicDirectoryService the publicDirectoryService to set
	 */
	public void setPublicDirectoryService(
			PublicDirectoryService publicDirectoryService) {
		this.publicDirectoryService = publicDirectoryService;
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
package com.yuanluesoft.cms.scene.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.cms.scene.pojo.SceneLink;
import com.yuanluesoft.cms.scene.pojo.SceneService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class SceneDirectoriesProcessor extends RecordListProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		Object record = sitePage.getAttribute("record");
		if(record instanceof com.yuanluesoft.cms.scene.pojo.SceneService) {
			Set children = ((com.yuanluesoft.cms.scene.pojo.SceneService)record).getSceneDirectories();
			return children==null ? null : new RecordListData(new ArrayList(children));
		}
		else if(record instanceof SceneDirectory) {
			Set children = ((SceneDirectory)record).getChildDirectories();
			return children==null ? null : new RecordListData(new ArrayList(children));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasis(long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		//不创建重建依据,按记录ID来判断更新范围
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof SceneService) && !(object instanceof SceneDirectory)) { //不是场景服务
			return null;
		}
		List recordIds = new ArrayList();
		long directoryId = -1;
		if(object instanceof SceneService) { //场景服务
			SceneService sceneService = (SceneService)object;
			directoryId = sceneService.getId();
		}
		else if(object instanceof SceneDirectory) { //场景目录
			SceneDirectory sceneDirectory = (SceneDirectory)object;
			recordIds.add(new Long(sceneDirectory.getParentDirectoryId())); //添加上级目录
			directoryId = sceneDirectory.getId();
		}
		recordIds.add(new Long(directoryId)); //添加自己
		appendChildDirectories(recordIds, directoryId, databaseService); //添加子目录
		//按记录ID更新页面
		String hql = "select distinct StaticPage" +
					 " from StaticPage StaticPage" +
					 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
					 " and StaticPage.recordId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(recordIds, ",", false)) + ")";
		return databaseService.findRecordsByHql(hql, 0, max);
	}
	
	/**
	 * 添加子目录ID列表
	 * @param recordIds
	 * @param parentDirectoryId
	 * @param databaseService
	 * @throws ServiceException
	 */
	private void appendChildDirectories(List recordIds, long parentDirectoryId, DatabaseService databaseService) throws ServiceException {
		List ids = databaseService.findRecordsByHql("select SceneDirectory.id from SceneDirectory SceneDirectory where SceneDirectory.parentDirectoryId=" + parentDirectoryId);
		if(ids==null || ids.isEmpty()) {
			return;
		}
		recordIds.addAll(ids);
		//递归添加下级
		for(Iterator iterator = ids.iterator(); iterator.hasNext();) {
			Number id = (Number)iterator.next();
			appendChildDirectories(recordIds, id.longValue(), databaseService);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordUrl(java.lang.Object, com.yuanluesoft.cms.sitemanage.model.SitePage, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, boolean, javax.servlet.http.HttpServletRequest)
	 */
	protected String getRecordUrl(View view, Object record, String linkTitle, SitePage sitePage, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if(record instanceof SceneLink) {
			return "{FINAL}" + ((SceneLink)record).getUrl();
		}
		return super.getRecordUrl(view, record, linkTitle, sitePage, recordListModel, webDirectory, parentSite, requestInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writeRecordElement(com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor, java.lang.Object, org.w3c.dom.html.HTMLAnchorElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void writeRecordElement(View view, Object record, HTMLAnchorElement fieldElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RecordList recordListModel, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if(record instanceof SceneLink) {
			fieldElement.setTarget("_blank");
		}
		super.writeRecordElement(view, record, fieldElement, webDirectory, parentSite, htmlParser, sitePage, recordListModel, requestInfo, request);
	}
}
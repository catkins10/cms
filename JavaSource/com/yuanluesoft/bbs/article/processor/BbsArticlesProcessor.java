package com.yuanluesoft.bbs.article.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsArticleSubjection;
import com.yuanluesoft.bbs.article.pojo.StaticPageBbsArticles;
import com.yuanluesoft.bbs.forum.pojo.BbsDirectory;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
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
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class BbsArticlesProcessor extends RecordListProcessor {
	private DatabaseService databaseService; //数据库服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		//解析目录ID列表
		String directoryIds = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "directoryIds");
		//解析是否包含子目录资源
		boolean containChildren = directoryIds==null ? true : "true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "containChildren"));
		String where = null;
		if(!containChildren) { //不显示子论坛
			where = "subjections.forumId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		}
		else if(directoryIds!=null && ("," + directoryIds + ",").indexOf(",0,")==-1) { //不包括根论坛
			view.addJoin(", BbsDirectorySubjection BbsDirectorySubjection");
			where = "subjections.forumId=BbsDirectorySubjection.directoryId" +
					" and BbsDirectorySubjection.parentDirectoryId in (" +  JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		}
		view.addWhere(where);
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		//解析目录ID列表
		String directoryIds = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "directoryIds");
		String[] ids = (directoryIds==null ? "0" : directoryIds).split(",");
		for(int i=0; i<ids.length; i++) {
			//检查是否已经创建过
			String hql = "select StaticPageBbsArticles.id" +
						 " from StaticPageBbsArticles StaticPageBbsArticles" +
						 " where StaticPageBbsArticles.pageId=" + staticPageId +
						 " and StaticPageBbsArticles.directoryId=" + ids[i];
			if(databaseService.findRecordByHql(hql)==null) {
				//保存引用的记录列表
				StaticPageBbsArticles staticPageBbsArticles = new StaticPageBbsArticles();
				staticPageBbsArticles.setId(UUIDLongGenerator.generateId()); //页面ID
				staticPageBbsArticles.setPageId(staticPageId); //页面ID
				staticPageBbsArticles.setProcessorClassName(this.getClass().getName()); //处理器类名称
				staticPageBbsArticles.setSiteId(0); //隶属的站点/栏目ID
				staticPageBbsArticles.setDirectoryId(Long.parseLong(ids[i])); //隶属的论坛目录ID
				databaseService.saveRecord(staticPageBbsArticles);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		String forumIds = null;
		if(object instanceof BbsDirectory) { //论坛目录变更
			if(DirectoryService.DIRECTORY_ACTION_MOVE_CHILD.equals(modifyAction) || DirectoryService.DIRECTORY_ACTION_REMOVE_CHILD.equals(modifyAction)) {
				forumIds = ((BbsDirectory)object).getId() + "";
			}
		}
		else if(object instanceof BbsArticle) {
			BbsArticle bbsArticle = (BbsArticle)object;
			forumIds = ListUtils.join(bbsArticle.getSubjections(), "forumId", ",", false); //文章所在版块
		}
		else if(object instanceof BbsArticleSubjection) {
			forumIds = "" + ((BbsArticleSubjection)object).getForumId();
		}
		if(forumIds==null) {
			return null;
		}
		//获取引用了文章所在版块及其上级站点记录列表的页面
		String hql = "select distinct StaticPage" +
					 " from StaticPage StaticPage, StaticPageBbsArticles StaticPageBbsArticles, BbsDirectorySubjection BbsDirectorySubjection" +
					 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
					 " and StaticPageBbsArticles.pageId=StaticPage.id" +
					 " and StaticPageBbsArticles.directoryId=BbsDirectorySubjection.parentDirectoryId" +
					 " and BbsDirectorySubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(forumIds) + ")";
		return databaseService.findRecordsByHql(hql, 0, max);
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
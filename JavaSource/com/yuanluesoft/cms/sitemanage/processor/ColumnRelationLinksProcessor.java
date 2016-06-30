package com.yuanluesoft.cms.sitemanage.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebColumnRelationLink;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 栏目关联链接列表处理器
 * @author linchuan
 *
 */
public class ColumnRelationLinksProcessor extends RecordListProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, java.lang.String, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof WebColumnRelationLink)) {
			return null;
		}
		String hql = "select distinct StaticPage" +
					 " from StaticPage StaticPage left join StaticPage.recordLists StaticPageRecordList" +
					 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
					 " and StaticPageRecordList.siteId=" + ((WebColumnRelationLink)object).getColumnId() +
					 " and StaticPageRecordList.recordClassName='" + getRecordClassNameForStaticPage(object) + "'" +
					 " and StaticPageRecordList.processorClassName='" + this.getClass().getName() + "'";
		return databaseService.findRecordsByHql(hql, 0, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordListSiteIds(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected String getRecordListSiteIds(RecordList recordListModel, View view, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		return "" + webDirectory.getId();
	}
}
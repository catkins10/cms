package com.yuanluesoft.cms.comment.processor;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 评论列表处理器
 * @author linchuan
 *
 */
public class CommentsProcessor extends RecordListProcessor {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		long recordId = -1;
		String applicationName = sitePage.getApplicationName();
		String pageName = sitePage.getName();
		if(applicationName.equals("cms/comment") && pageName.equals("comment")) { //当前页面是评论页面
			recordId = RequestUtils.getParameterLongValue(request, "recordId");
			applicationName = RequestUtils.getParameterStringValue(request, "applicationName");
			pageName = RequestUtils.getParameterStringValue(request, "pageName");
		}
		else { //当前页面不是评论页面
			//获取记录ID
			Object record = sitePage.getAttribute("record");
			if(record!=null) {
				try {
					recordId = ((Number)PropertyUtils.getProperty(record, "id")).longValue();
				}
				catch (Exception e) {
					
				}
			}
			if(recordId==-1) {
				recordId = webDirectory.getId();
			}
		}
		String where = "CmsComment.recordId=" + recordId + " and CmsComment.applicationName='" + JdbcUtils.resetQuot(applicationName) + "' and CmsComment.pageName='" + JdbcUtils.resetQuot(pageName) + "'";
		view.addWhere(where);
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}
}
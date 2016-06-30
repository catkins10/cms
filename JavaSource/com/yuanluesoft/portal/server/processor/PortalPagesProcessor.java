package com.yuanluesoft.portal.server.processor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.portal.server.model.Portal;
import com.yuanluesoft.portal.server.model.PortalPage;

/**
 * 
 * @author linchuan
 *
 */
public class PortalPagesProcessor extends RecordListProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		Portal portal = (Portal)sitePage.getAttribute("portal");
		return portal==null ? null : new RecordListData(portal.getPortalPages());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#isCurrentRecord(java.lang.Object, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected boolean isCurrentRecord(Object record, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		return ((PortalPage)record).isSelected();
	}
}
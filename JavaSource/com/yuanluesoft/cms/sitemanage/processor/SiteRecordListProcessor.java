package com.yuanluesoft.cms.sitemanage.processor;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 站点相关记录列表处理器
 * @author linchuan
 *
 */
public class SiteRecordListProcessor extends RecordListProcessor {
	private SiteService siteService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
		String siteIds = getRelatedSiteIds(view, recordListModel, parentSite);
		if(siteIds!=null) {
			view.addWhere(view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf('.') + 1) + ".siteId in (" + siteIds + ")");
		}
	}
	
	/**
	 * 获取站点ID
	 * @param view
	 * @param recordListModel
	 * @param parentSite
	 * @return
	 */
	protected String getRelatedSiteIds(View view, RecordList recordListModel, WebSite parentSite) {
		if(isUnrelatedToSites(recordListModel, view)) { //和站点无关
			return null;
		}
		String siteIds = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "siteIds");
		if(siteIds==null || siteIds.isEmpty()) { //本站点
			return "" + parentSite.getId();
		}
		else if(!"true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "containChildren"))) { //不包含子站点
			return siteIds;
		}
		else if(("," + siteIds + ",").indexOf(",0,")!=-1) { //有根站点
			return null;
		}
		else {
			return siteService.getAllChildDirectoryIds(siteIds, "site");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordListSiteIds(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected String getRecordListSiteIds(RecordList recordListModel, View view, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		if(isUnrelatedToSites(recordListModel, view)) { //和站点无关
			return null;
		}
		String siteIds = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "siteIds");
		return siteIds==null || siteIds.isEmpty() ? "" + parentSite.getId() : siteIds;
	}
	
	/**
	 * 当前记录列表是否与和站点无关
	 * @param recordListModel
	 * @param view
	 * @return
	 */
	protected boolean isUnrelatedToSites(RecordList recordListModel, View view) {
		return "true".equals(view.getExtendParameter("unrelatedToSites"));
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
}
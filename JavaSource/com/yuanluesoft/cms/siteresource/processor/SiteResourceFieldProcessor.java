package com.yuanluesoft.cms.siteresource.processor;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 站点资源处理
 * @author linchuan
 *
 */
public class SiteResourceFieldProcessor extends RecordFieldProcessor {
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor#getFieldValue(java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected Object getFieldValue(Object record, Field field, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		if(!"columnName".equals(field.getName())) {
			return super.getFieldValue(record, field, pageElement, webDirectory, parentSite, sitePage, request);
		}
		//栏目名称
		SiteResource siteResource = (SiteResource)record;
		if(siteResource.getSubjections().size()==1) {
			return siteResource.getColumnName();
		}
		String columnIds = ListUtils.join(siteResource.getSubjections(), "siteId", ",", false);
		long parentIds[] = webDirectory.getId()==parentSite.getId() ? new long[]{webDirectory.getId()} : new long[]{webDirectory.getId(), parentSite.getId()};
		for(int i=0; i<parentIds.length; i++) {
			long childColumnId;
			SiteResourceSubjection subjection = (SiteResourceSubjection)ListUtils.findObjectByProperty(siteResource.getSubjections(), "siteId", new Long(parentIds[i]));
			if(subjection!=null) {
				childColumnId = subjection.getSiteId();
			}
			else {
				String childColumnIds = siteService.filterChildDirectoryIds(columnIds, parentIds[i]);
				if(childColumnIds==null || childColumnIds.isEmpty()) {
					continue;
				}
				childColumnId = Long.parseLong(childColumnIds.split(",")[0]);
			}
			if(childColumnId==((SiteResourceSubjection)siteResource.getSubjections().iterator().next()).getSiteId()) {
				break;
			}
			return siteService.getDirectoryName(childColumnId);
		}
		return siteResource.getColumnName();
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
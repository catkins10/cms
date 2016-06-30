package com.yuanluesoft.cms.onlineservice.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemSubjection;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceTemplateService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 网上办事办理事项页面服务
 * @author linchuan
 *
 */
public class OnlineServiceItemPageService extends com.yuanluesoft.cms.pagebuilder.PageService {
	private OnlineServiceItemService onlineServiceItemService; //办理事项服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		OnlineServiceItem onlineServiceItem = onlineServiceItemService.getOnlineServiceItem(RequestUtils.getParameterLongValue(request, "itemId"));
		if(onlineServiceItem==null) {
			throw new PageNotFoundException();
		}
		//设置record属性
		sitePage.setAttribute("record", onlineServiceItem);
		//获取所在目录
		long directoryId = RequestUtils.getParameterLongValue(request, "directoryId");
		if(directoryId==0) { //没有指定
			directoryId = ((OnlineServiceItemSubjection)onlineServiceItem.getSubjections().iterator().next()).getDirectoryId();
		}
		sitePage.setAttribute("directoryId", new Long(directoryId)); //为目录位置服务设置
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		OnlineServiceTemplateService onlineServiceTemplateService = (OnlineServiceTemplateService)getTemplateService();
		OnlineServiceItem onlineServiceItem = (OnlineServiceItem)sitePage.getAttribute("record");
		//获取所在目录
		long directoryId = ((Long)sitePage.getAttribute("directoryId")).longValue();
		return onlineServiceTemplateService.getTemplateHTMLDocument(pageName, directoryId, onlineServiceItem.getItemType(), siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}

	/**
	 * @return the onlineServiceItemService
	 */
	public OnlineServiceItemService getOnlineServiceItemService() {
		return onlineServiceItemService;
	}

	/**
	 * @param onlineServiceItemService the onlineServiceItemService to set
	 */
	public void setOnlineServiceItemService(
			OnlineServiceItemService onlineServiceItemService) {
		this.onlineServiceItemService = onlineServiceItemService;
	}
}
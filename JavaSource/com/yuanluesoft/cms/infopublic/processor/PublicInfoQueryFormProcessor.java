package com.yuanluesoft.cms.infopublic.processor;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLFormElement;

import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class PublicInfoQueryFormProcessor extends FormProcessor {
	private PublicDirectoryService publicDirectoryService; //信息公开目录服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#getFieldValue(org.w3c.dom.html.HTMLFormElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.jeaf.form.model.Form, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected Object getFieldValue(HTMLFormElement formElement, SitePage sitePage, Form form, Object bean, Field field, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws Exception {
		Object fieldValue = super.getFieldValue(formElement, sitePage, form, bean, field, webDirectory, parentSite, request);
		if(fieldValue!=null) {
			return fieldValue;
		}
		if("directoryId".equals(field.getName()) || "rootDirectoryId".equals(field.getName())) {
			fieldValue = new Long(getPublicMainDirectory(parentSite, request).getId());
		}
		else if("directoryName".equals(field.getName())) {
			fieldValue = getPublicMainDirectory(parentSite, request).getDirectoryName();
		}
		return fieldValue;
	}
	
	/**
	 * 获取主目录
	 * @param parentSite
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private PublicMainDirectory getPublicMainDirectory(WebSite parentSite, HttpServletRequest request) {
		PublicMainDirectory mainDirectory = (PublicMainDirectory)request.getAttribute("mainDirectory");
		if(mainDirectory==null) {
			try {
				mainDirectory = publicDirectoryService.getMainDirectoryBySite(parentSite.getId());
			} 
			catch(ServiceException e) {
				Logger.exception(e);
			}
			request.setAttribute("mainDirectory", mainDirectory);
		}
		return mainDirectory;
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
}
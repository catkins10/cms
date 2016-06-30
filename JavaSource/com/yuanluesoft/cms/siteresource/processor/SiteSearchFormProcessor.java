package com.yuanluesoft.cms.siteresource.processor;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SiteSearchFormProcessor extends FormProcessor {
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLFormElement formElement = (HTMLFormElement)pageElement;
		//获取需要搜索的站点ID和名称
		String searchSiteId = request.getParameter("searchSiteId");
		String searchSiteName = request.getParameter("searchSiteName");
		if(searchSiteId!=null && !searchSiteId.isEmpty() && !"-1".equals(searchSiteId)) {
			if(searchSiteName==null || searchSiteName.isEmpty()) {
				searchSiteName = siteService.getDirectoryName(Long.parseLong(searchSiteId));
			}
		}
		else if(formElement.getTarget()!=null && !formElement.getTarget().equals("")) { //保持和旧系统兼容
			String[] values = formElement.getTarget().split("\\x7C");
			searchSiteId = values[1];
			searchSiteName = values[0];
		}
		else {
			String properties = formElement.getAction();
			searchSiteId = StringUtils.getPropertyValue(properties, "siteId");
			searchSiteName = StringUtils.getPropertyValue(properties, "siteName");
		}
		if(!"quickSiteSearch".equals(formElement.getId())) { //不是快速搜索
			if(searchSiteId==null || searchSiteId.equals("-1")) {
				searchSiteName = parentSite.getDirectoryName();
				searchSiteId = parentSite.getId() + "";
			}
		}
		else { //快速搜索
			if(searchSiteId==null || searchSiteId.equals("-1")) {
				searchSiteName = webDirectory.getDirectoryName();
				searchSiteId = webDirectory.getId() + "";
			}
			//检查表单是否包含搜索栏目字段
			NodeList inputs = formElement.getElementsByTagName("input");
			int i = inputs.getLength() - 1;
			for(; i>=0 && !"searchSite".equals(((HTMLInputElement)inputs.item(i)).getName()); i--);
			if(i<0) { //没有搜索栏目字段
				//添加隐藏字段:搜索站点ID
				htmlParser.appendHiddenField("searchSiteId", searchSiteId, formElement);
			}
		}
		request.setAttribute("searchSiteId", searchSiteId); //设置属性,获取下拉列表时、获取字段值时使用
		request.setAttribute("searchSiteName", searchSiteName); //设置属性,获取下拉列表时、获取字段值时使用
		super.writePageElement(pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.FormProcessor#getFieldValue(org.w3c.dom.html.HTMLFormElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.jeaf.form.model.Form, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest)
	 */
	protected Object getFieldValue(HTMLFormElement formElement, SitePage sitePage, Form form, Object bean, Field field, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws Exception {
		Object fieldValue = super.getFieldValue(formElement, sitePage, form, bean, field, webDirectory, parentSite, request);
		if(fieldValue!=null) {
			return fieldValue;
		}
		if("searchSiteId".equals(field.getName())) {
			fieldValue = request.getAttribute("searchSiteId");
		}
		else if("searchSite".equals(field.getName())) {
			fieldValue = request.getAttribute("searchSiteName");
		}
		return fieldValue;
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
package com.yuanluesoft.portal.portlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.portal.container.internal.PortletResponseImpl;
import com.yuanluesoft.portal.container.internal.PortletStoredResponseImpl;
import com.yuanluesoft.portal.container.pojo.PortletEntity;

/**
 * 
 * @author linchuan
 *
 */
public class PageBasedPortlet extends BasePortlet {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.portal.portlet.BasePortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			String pageURL = request.getParameter("originalURL");
			if(pageURL==null) {
				pageURL = StringUtils.fillParameters(getInitParameter("pageURL"), true, false, false, "utf-8", null, (HttpServletRequest)request, null);
			}
			else {
				String queryString = ((HttpServletRequest)request).getQueryString();
				int index = pageURL.indexOf('?');
				pageURL = (index==-1 ? pageURL : pageURL.substring(0, index)).substring(Environment.getContextPath().length()) +
						  (queryString==null || queryString.isEmpty() ? "" : "?" + queryString);
			}
			request.setAttribute(PageService.PAGE_LOGGER_DISABLED, "true"); //禁止访问统计
			request.setAttribute(PageService.PAGE_ADVERT_DISABLED, "true"); //禁止加载广告
			request.setAttribute(PageService.PAGE_CORRECTION_DISABLED, "true"); //禁止加入纠错脚本
			//加载模板
			Map templates = loadPageTemplates(request);
			if(templates!=null) {
				request.setAttribute(PageService.PAGE_TEMPLATE_MAP, templates);
			}
			getPortletContext().getRequestDispatcher(pageURL).include(request, response);
			//解析页面
			PortletResponseImpl portletResponse = (PortletResponseImpl)response;
			PortletStoredResponseImpl storedResponse = (PortletStoredResponseImpl)portletResponse.getResponse();
			HTMLDocument htmlDocument = htmlParser.parseHTMLString(storedResponse.getOutputBufferAsString(), "utf-8");
			//重设HTML文档
			resetHTMLDocument(htmlDocument, request, response);
			//重设输出内容
			storedResponse.setOutputBufferAsString(htmlParser.getBodyHTML(htmlDocument, "utf-8", false));
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 加载portlet实体模板
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected Map loadPageTemplates(RenderRequest request) throws Exception {
		String ralationPages = getInitParameter("ralationPages"); //关联页面,如：搜索条件|cms/sitemanage_index\0搜索结果|cms/sitemanage_quickSiteSearch
		if(ralationPages==null) {
			return null;
		}
		//获取实体
		PortletEntity portletEntity = getPortletEntity();
		//获取站点ID
		String parameter = request.getParameter("siteId");
		long siteId = parameter==null ? 0 : Long.parseLong(parameter);
		//获取模板
		Map templates = new HashMap();
		String[] pages = ralationPages.split("\\\\0");
		for(int i=0; i<pages.length; i++) {
			String[] values = pages[i].split("\\|")[1].split("_");
			templates.put(values[0] + "_" + values[1], getTemplateHTMLDocument(portletEntity, siteId, request, values[0], values[1]));
		}
		return templates.isEmpty() ? null : templates;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.portal.portlet.BasePortlet#getTemplateHTMLDocument(com.yuanluesoft.portal.container.pojo.PortletEntity, long, javax.portlet.RenderRequest, java.lang.String, java.lang.String)
	 */
	protected HTMLDocument getTemplateHTMLDocument(PortletEntity portletEntity, long siteId, RenderRequest request, String pageApplication, String pageName) throws Exception {
		HTMLDocument templateDocument = super.getTemplateHTMLDocument(portletEntity, siteId, request, pageApplication, pageName);
		if(templateDocument==null) {
			return null;
		}
		//获取记录列表
		HTMLElement recordListElement = (HTMLElement)templateDocument.getElementById("recordList");
		if(recordListElement!=null) {
			RecordList recordList = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, recordListElement.getAttribute("urn"), null);
			recordList.setRecordCount(Integer.parseInt(request.getPreferences().getValue("recordCount", "8"))); //记录数
			RecordListUtils.setRecordListProperties(recordListElement, recordList); //重写URN
		}
		return templateDocument;
	}
}
package com.yuanluesoft.cms.siteresource.portlet;

import java.util.List;

import javax.portlet.RenderRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.portal.container.pojo.PortletEntity;
import com.yuanluesoft.portal.portlet.TemplateBasedPortlet;

/**
 * 
 * @author linchuan
 *
 */
public class SlideShowPortlet extends TemplateBasedPortlet {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.portal.portlet.BasePortlet#getTemplateHTMLDocument(com.yuanluesoft.portal.container.pojo.PortletEntity, long, javax.portlet.RenderRequest, java.lang.String, java.lang.String)
	 */
	protected HTMLDocument getTemplateHTMLDocument(final PortletEntity portletEntity, final long siteId, final RenderRequest request, String pageApplication, String pageName) throws Exception {
		HTMLDocument templateDocument = super.getTemplateHTMLDocument(portletEntity, siteId, request, pageApplication, pageName);
		if(templateDocument==null) {
			return null;
		}
		if(templateDocument!=null && portletEntity.getTemplates()!=null && !portletEntity.getTemplates().isEmpty()) { //有私有模板, 且portlet实体自定义模板不为空
			return templateDocument;
		}
		//获取记录列表
		HTMLElement recordListElement = (HTMLElement)templateDocument.getElementById("recordList");
		if(recordListElement==null) {
			return templateDocument;
		}
		//解析记录列表
		final RecordList recordList = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, recordListElement.getAttribute("urn"), null);
		//重设记录数
		try {
			int recordCount = Integer.parseInt(request.getPreferences().getValue("recordCount", "5"));
			recordList.setRecordCount(Math.max(3, Math.min(recordCount, 10))); //记录数
		}
		catch(Exception e) {
			
		}
		//重设切换时间
		try {
			recordList.setScrollSpeed(Integer.parseInt(request.getPreferences().getValue("scrollSpeed", "5")) * 1000);
		}
		catch(Exception e) {
			
		}
		//如果是文章列表,则重设栏目
		if("cms/sitemanage".equals(recordList.getApplicationName()) && "resources".equals(recordList.getRecordListName())) {
			String columnId = request.getPreferences().getValue("columnId", ""); //从个性化设置中获取栏目ID
			if(columnId==null || columnId.isEmpty()) {
				SiteService siteService = (SiteService)Environment.getService("siteService");
				long imageSiteId = siteId<0 ? 0 : siteId;
				SessionInfo sessionInfo = getSessionInfo(request);
				if(sessionInfo.isInternalUser()) { //内部用户
					WebSite webSite = siteService.getSiteByOwnerUnitId(sessionInfo.getUnitId());
					if(webSite!=null) {
						imageSiteId = webSite.getId();
					}
				}
				//获取“图片新闻”栏目
				List columns = siteService.listChildDirectoriesByName(imageSiteId, "图片新闻");
				columnId = "" + (columns==null || columns.isEmpty() ? imageSiteId : ((WebDirectory)columns.get(0)).getId());
			}
			//重设扩展属性
			String extendProperties = recordList.getExtendProperties();
			extendProperties = StringUtils.removeQueryParameters(extendProperties, "siteIds") + "&siteIds=" + columnId;
			recordList.setExtendProperties(extendProperties);
		}
		//重写URN
		RecordListUtils.setRecordListProperties(recordListElement, recordList);
		return templateDocument;
	}
}
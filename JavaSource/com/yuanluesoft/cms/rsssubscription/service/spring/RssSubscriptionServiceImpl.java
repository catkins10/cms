package com.yuanluesoft.cms.rsssubscription.service.spring;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.rsssubscription.service.RssSubscriptionService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.rss.model.RSSChannel;
import com.yuanluesoft.jeaf.rss.model.RSSItem;
import com.yuanluesoft.jeaf.rss.writer.RSSWriter;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;
import com.yuanluesoft.jeaf.view.model.Link;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;

/**
 * RSS订阅服务
 * @author linchuan
 *
 */
public class RssSubscriptionServiceImpl implements RssSubscriptionService {
	private SiteService siteService; //站点服务
	private RSSWriter rssWriter; //RSS输出服务
	private ViewDefineService viewDefineService; //视图定义服务
	private BusinessDefineService businessDefineService; //业务逻辑定义服务
	private HTMLParser htmlParser; //HTML解析器
	private PageBuilder pageBuilder; //页面生成器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.rsssubscription.service.RssSubscriptionService#writeRssChannel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void writeRssChannel(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String applicationName = request.getParameter("applicationName");
		String channel = request.getParameter("channel");
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		
		//获取站点
		final WebDirectory webDirectory = (WebDirectory)siteService.getDirectory(siteId);
		if(webDirectory==null) {
			return;
		}
		final WebSite parentSite = siteService.getParentSite(webDirectory.getId());
		
		//获取视图定义
		View view = null;
		try {
			view = viewDefineService.getView(applicationName, channel, SessionService.ANONYMOUS_SESSION);
		}
		catch(Exception e) {
			return;
		}
		//获取业务对象定义
		BusinessObject businessObject = businessDefineService.getBusinessObject(view.getPojoClassName());
		
		//获取字段列表中的RSS字段
		String rssTitleFieldName = null; //RSS标题
		String rssDescriptionFieldName = null; //RSS内容描述
		String rssPubDateFieldName = null; //RSS发布时间
		for(Iterator iterator = businessObject.getFields().iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if("true".equals(field.getParameter("rssTitleField"))) { //RSS标题
				rssTitleFieldName = field.getName();
			}
			else if("true".equals(field.getParameter("rssDescriptionField"))) { //RSS内容描述
				rssDescriptionFieldName = field.getName();
			}
			else if("true".equals(field.getParameter("rssPubDateField"))) { //RSS发布时间
				rssPubDateFieldName = field.getName();
			}
		}
		
		//构建记录列表模型
		final RecordList recordListModel = new RecordList();
		recordListModel.setApplicationName(applicationName); //应用名称
		recordListModel.setRecordListName(channel); //记录列表名称
		recordListModel.setExtendProperties(request.getParameter("extendProperties")); //拓展属性
		recordListModel.setRecordCount(50); //记录数
		//设置显示格式
		String recordFormat = "<a id=\"field\" urn=\"name=" + rssTitleFieldName + "\"></a>" +
							  (rssDescriptionFieldName==null ? "" : "<a id=\"field\" urn=\"name=" + rssDescriptionFieldName + "\"></a>") +
							  (rssPubDateFieldName==null ? "" : "<a id=\"field\" urn=\"name=" + rssPubDateFieldName + "\"></a>");
		recordListModel.setRecordFormat(recordFormat);
		
		SitePage sitePage = new SitePage();
		//获取记录列表
		HTMLDocument recordListDocument = htmlParser.parseHTMLString(RecordListUtils.gernerateRecordListElement(recordListModel, false), "utf-8");
		//处理记录列表,获取记录
		getPageBuilder().processPageElement(recordListDocument.getBody(), webDirectory, parentSite, sitePage, RequestUtils.getRequestInfo(request), request);
		List items = (List)sitePage.getAttribute(RecordListProcessor.RECORDLIST_ATTRIBUTE_RECORDS);
		//转换为rss条目列表
		for(int i=items==null ? -1 : items.size()-1; i>=0; i--) {
			try {
				Object record = items.get(i);
				//获取视图的第一个链接作为记录的链接
				Link link = view.getLinks()==null || view.getLinks().isEmpty() ? null : (Link)view.getLinks().get(0);
				String recordLink = link==null || link.getUrl()==null ? null : LinkUtils.regenerateURL(link.getUrl(), webDirectory.getId(), parentSite.getId(), record, true, null, request);
				//获取RSS标题(rssTitleField)
				String rssTitle = (String)getRssFieldValue(businessObject, record, rssTitleFieldName, request, true);
				//获取RSS内容描述(rssDescriptionField)
				String rssDescription = (String)getRssFieldValue(businessObject, record, rssDescriptionFieldName, request, true);
				//获取RSS发布时间(rssPubDateField)
				Date rssPubDate = (Date)getRssFieldValue(businessObject, record, rssPubDateFieldName, request, false);
				//添加RSS条目
				items.set(i, new RSSItem(rssTitle, recordLink, rssDescription, (rssPubDate==null ? null : new Timestamp(rssPubDate.getTime())), "" + PropertyUtils.getProperty(record, "id")));
			}
			catch(Exception e) {
				items.remove(i);
				Logger.exception(e);
			}
		}
		//获取宿主页面链接作为频道链接
		String channelLink = null;
		Link link = (Link)ListUtils.findObjectByProperty(view.getLinks(), "type", "hostLink");
		if(link!=null && link.getUrl()!=null) {
			channelLink = StringUtils.fillParameters(link.getUrl(), true, false, false, "utf-8", null, request, new FillParametersCallback() {
				public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
					if("columnId".equals(parameterName)) { //栏目ID
						return "" + webDirectory.getId();
					}
					else if("siteId".equals(parameterName)) { //父站点ID
						return "" + parentSite.getId();
					}
					return StringUtils.getPropertyValue(recordListModel.getExtendProperties(), parameterName); //从记录列表的扩展属性中获取属性值
				}
			});
		}
		channelLink =  LinkUtils.regenerateURL(channelLink, webDirectory.getId(), parentSite.getId(), null, false, null, request);
	 	//输出RSS频道
        rssWriter.writeRSSChannel(new RSSChannel(view.getTitle(), view.getTitle(), channelLink, (items!=null && !items.isEmpty() ? ((RSSItem)items.get(0)).getPubDate() : null), RequestUtils.getParameterIntValue(request, "ttl") , items), response);
	}
	
	/**
	 * 获取RSS字段值
	 * @param view
	 * @param businessObject
	 * @param record
	 * @param rssFieldName
	 * @param request
	 * @param format 是否需要格式化为文本
	 * @return
	 */
	private Object getRssFieldValue(BusinessObject businessObject, Object record, String rssFieldName, HttpServletRequest request, boolean formatValue) throws Exception {
		if(rssFieldName==null) {
			return null;
		}
		//查找对应的字段
		Field field = (Field)ListUtils.findObjectByProperty(businessObject.getFields(), "name", rssFieldName);
		//获取字段值
		Object fieldValue = FieldUtils.getFieldValue(record, rssFieldName, request);
		if(!formatValue) {
			return fieldValue;
		}
		return StringUtils.slice(FieldUtils.formatFieldValue(fieldValue, field, record, true, null, false, true, true, 0, null, null, request), 300, "...");
	}

	/**
	 * @return the rssWriter
	 */
	public RSSWriter getRssWriter() {
		return rssWriter;
	}

	/**
	 * @param rssWriter the rssWriter to set
	 */
	public void setRssWriter(RSSWriter rssWriter) {
		this.rssWriter = rssWriter;
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

	/**
	 * @return the businessDefineService
	 */
	public BusinessDefineService getBusinessDefineService() {
		return businessDefineService;
	}

	/**
	 * @param businessDefineService the businessDefineService to set
	 */
	public void setBusinessDefineService(BusinessDefineService businessDefineService) {
		this.businessDefineService = businessDefineService;
	}

	/**
	 * @return the viewDefineService
	 */
	public ViewDefineService getViewDefineService() {
		return viewDefineService;
	}

	/**
	 * @param viewDefineService the viewDefineService to set
	 */
	public void setViewDefineService(ViewDefineService viewDefineService) {
		this.viewDefineService = viewDefineService;
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}

	/**
	 * @return the pageBuilder
	 */
	public PageBuilder getPageBuilder() {
		return pageBuilder;
	}

	/**
	 * @param pageBuilder the pageBuilder to set
	 */
	public void setPageBuilder(PageBuilder pageBuilder) {
		this.pageBuilder = pageBuilder;
	}
}
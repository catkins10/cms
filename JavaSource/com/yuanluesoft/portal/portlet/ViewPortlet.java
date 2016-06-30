package com.yuanluesoft.portal.portlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;
import com.yuanluesoft.jeaf.view.model.Link;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.portal.container.model.PortletDefinition;
import com.yuanluesoft.portal.container.model.PortletPreference;
import com.yuanluesoft.portal.container.pojo.PortletEntity;


/**
 * 
 * @author linchuan
 *
 */
public class ViewPortlet extends TemplateBasedPortlet {
	protected ViewDefineService viewDefineService;
	
	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#init(javax.portlet.PortletConfig)
	 */
	public void init(PortletConfig portletConfig) throws PortletException {
		super.init(portletConfig);
		try {
			viewDefineService = (ViewDefineService)Environment.getService("viewDefineService");
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

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
		//解析记录列表
		final RecordList recordList = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, recordListElement.getAttribute("urn"), null);
		final View view = viewDefineService.getView(portletEntity.getPortletApplication(), getInitParameter("viewName"), getSessionInfo(request));
		resetRecordList(recordList, portletEntity, view, siteId, request);
		//重写URN
		RecordListUtils.setRecordListProperties(recordListElement, recordList);
		//处理“查看全部”,“新建...”链接
		htmlParser.traversalChildNodes(templateDocument.getBody().getChildNodes(), true, new HTMLTraversalCallback() {
			public boolean processNode(Node node) {
				HTMLElement element = (HTMLElement)node;
				if("viewAction".equals(element.getId())) {
					try {
						resetViewLink((HTMLAnchorElement)element, recordList, portletEntity, view, siteId, request);
						return true;
					}
					catch (Exception e) {
						Logger.exception(e);
					}
				}
				return false;
			}
		});
		return templateDocument;
	}
	
	/**
	 * 重置记录列表
	 * @param recordList
	 * @param portletEntity
	 * @param view
	 * @param siteId
	 * @param request
	 */
	protected void resetRecordList(RecordList recordList, PortletEntity portletEntity, View view, long siteId, final RenderRequest request) {
		//重置记录列表
		recordList.setApplicationName(view.getApplicationName()); //应用名称
		recordList.setRecordListName(view.getName()); //记录列表名称
		//重置链接标题
		for(Iterator iterator = view.getLinks()==null ? null : view.getLinks().iterator(); iterator!=null && iterator.hasNext();) {
			Link link = (Link)iterator.next();
			if(!"hostLink".equals(link.getType())) {
				recordList.setLinkTitle(link.getTitle());
				break;
			}
		}
		try {
			recordList.setRecordCount(Integer.parseInt(request.getPreferences().getValue("recordCount", "8"))); //记录数
		}
		catch(Exception e) {
			
		}
		//重置记录格式
		String titleFieldName = getInitParameter("titleFieldName"); //标题字段名称
		if(titleFieldName!=null && !"subject".equals(titleFieldName)) {
			recordList.setRecordFormat(recordList.getRecordFormat().replace("subject", titleFieldName));
		}
		String timeFieldName = getInitParameter("timeFieldName"); //时间字段名称
		if(timeFieldName!=null && !"created".equals(timeFieldName)) {
			recordList.setRecordFormat(recordList.getRecordFormat().replace("created", timeFieldName));
		}
		//设置扩展属性
		String extendProperties = getInitParameter("extendProperties");
		extendProperties = StringUtils.fillParameters(extendProperties, false, false, false, "utf-8", null, null, new FillParametersCallback() {
			public Object getParameterValue(String parameterName, Object bean, HttpServletRequest servletRequest) {
				return request.getPreferences().getValue(parameterName, null);
			}
		});
		//设置没有记录时的提示信息
		String emptyPrompt = (String)view.getExtendParameter("emptyPrompt");
		if(emptyPrompt!=null && !emptyPrompt.isEmpty() && recordList.getEmptyPrompt()!=null) {
			recordList.setEmptyPrompt(recordList.getEmptyPrompt().replace("暂无记录", emptyPrompt));
		}
		recordList.setExtendProperties(extendProperties);
	}
	
	/**
	 * 重置查看全部链接
	 * @param linkElement
	 * @param recordList
	 * @param portletEntity
	 * @param view
	 * @param siteId
	 * @param request
	 */
	protected void resetViewLink(HTMLAnchorElement linkElement, final RecordList recordList, PortletEntity portletEntity, View view, final long siteId, RenderRequest request) throws Exception {
		//<a id="viewAction" urn="applicationName=cms/sitemanage&amp;viewName=resources&amp;title=查看全部" target="_blank">查看全部</a>
		String linkTitle = StringUtils.getPropertyValue( linkElement.getAttribute("urn"), "title");
		if("查看全部".equals(linkTitle)) {
			linkElement.removeAttribute("id");
			linkElement.removeAttribute("urn");
			Link link = (Link)ListUtils.findObjectByProperty(view.getLinks(), "type", "hostLink");
			if(link!=null) { //有宿主页面
				String href = StringUtils.fillParameters(link.getUrl(), true, false, false, "utf-8", null, null, new FillParametersCallback() {
					public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
						if("columnId".equals(parameterName) || "siteId".equals(parameterName)) { //栏目ID, 父站点ID
							return "" + siteId;
						}
						return StringUtils.getPropertyValue(recordList.getExtendProperties(), parameterName); //从记录列表的扩展属性中获取属性值
					}
				});
				linkElement.setHref(LinkUtils.regenerateURL(href, siteId, siteId, null, false, null, null));
			}
			else if(view.getColumns()!=null && !view.getColumns().isEmpty()) { //有定义视图列
				linkElement.setHref(Environment.getContextPath() + "/jeaf/application/application.shtml?applicationName=" + view.getApplicationName() + "&viewName=" + view.getName());
			}
			else  {
				linkElement.getParentNode().removeChild(linkElement);
			}
		}
		else if(linkTitle.indexOf("新建")!=-1 || linkTitle.indexOf("添加")!=-1) {
			ViewAction createAction = null;
			for(Iterator iterator = view.getActions()==null ? null : view.getActions().iterator(); createAction==null && iterator!=null && iterator.hasNext();) {
				Object action = iterator.next();
				if(!(action instanceof ViewAction)) {
					continue;
				}
				ViewAction viewAction = (ViewAction)action;
				if(viewAction.getTitle().indexOf("新建")!=-1 || viewAction.getTitle().indexOf("添加")!=-1) {
					createAction = viewAction;
				}
			}
			if(createAction==null) {
				linkElement.getParentNode().removeChild(linkElement);
			}
			else {
				linkElement.setAttribute("urn", "applicationName=" + view.getApplicationName() + "&viewName=" + view.getName() + "&title=" + createAction.getTitle());
				//重设标题
				final String actionTitle = createAction.getTitle();
				htmlParser.traversalChildNodes(linkElement.getChildNodes(), false, new HTMLTraversalCallback() {
					public boolean processNode(Node node) {
						if(!(node instanceof Text)) {
							return false;
						}
						Text textNode = (Text)node;
						String content = textNode.getTextContent();
						if(content!=null && !content.trim().isEmpty()) {
							textNode.setTextContent(actionTitle);
						}
						return true;
					}
				});
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.portal.portlet.BasePortlet#listPortletPreferenceDefines(com.yuanluesoft.portal.container.model.PortletDefinition)
	 */
	protected List listPortletPreferenceDefines(PortletDefinition portletDefinition) {
		List portletPreferences = super.listPortletPreferenceDefines(portletDefinition);
		if(portletPreferences==null) {
			portletPreferences = new ArrayList();
		}
		portletPreferences.add(new PortletPreference("recordCount", "defaultValue=8&label=记录数&inputMode=text", false));
		return portletPreferences;
	}
}
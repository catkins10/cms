package com.yuanluesoft.portal.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLStyleElement;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.DynamicForm;
import com.yuanluesoft.jeaf.form.model.DynamicFormField;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.portal.container.internal.PortletConfigImpl;
import com.yuanluesoft.portal.container.model.PortletDefinition;
import com.yuanluesoft.portal.container.model.PortletPreference;
import com.yuanluesoft.portal.container.pojo.PortletEntity;
import com.yuanluesoft.portal.container.pojo.PortletEntityTemplate;
import com.yuanluesoft.portal.container.service.PortletTemplateService;

/**
 * 
 * @author linchuan
 *
 */
public class BasePortlet extends GenericPortlet {
	protected PortletTemplateService portletTemplateService;
	protected PageBuilder pageBuilder;
	protected HTMLParser htmlParser;

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#init(javax.portlet.PortletConfig)
	 */
	public void init(PortletConfig portletConfig) throws PortletException {
		super.init(portletConfig);
		try {
			portletTemplateService = (PortletTemplateService)Environment.getService("portletTemplateService");
			pageBuilder = (PageBuilder)Environment.getService("pageBuilder");
			htmlParser = (HTMLParser)Environment.getService("htmlParser");
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/**
	 * 获取portlet实体
	 * @return
	 */
	protected PortletEntity getPortletEntity() {
		return ((PortletConfigImpl)getPortletConfig()).getPortletEntity();
	}
	
	/**
	 * 获取portlet定义
	 * @return
	 */
	protected PortletDefinition getPortletDefinition() {
		return ((PortletConfigImpl)getPortletConfig()).getPortletDefinition();
	}
	
	/**
	 * 获取会话
	 * @param request
	 * @return
	 */
	protected SessionInfo getSessionInfo(RenderRequest request) {
		return (SessionInfo)request.getAttribute(PortletRequest.USER_INFO);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#doEdit(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		//创建动态表单
		DynamicForm dynamicForm = new DynamicForm();
		request.setAttribute("portletPreferences", dynamicForm);
		//添加字段
		List dynamicFormFields = generatePreferenceFields(getPortletDefinition(), request, true);
		if(dynamicFormFields!=null && !dynamicFormFields.isEmpty()) {
			dynamicForm.getFields().addAll(dynamicFormFields);
		}
		getPortletContext().getRequestDispatcher("/portal/portletPreferences.jsp").include(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#processAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		//保存个性化设置参数
		List preferences = listPortletPreferenceDefines(getPortletDefinition());
		for(Iterator iterator = preferences==null ? null : preferences.iterator(); iterator!=null && iterator.hasNext();) {
			PortletPreference preference = (PortletPreference)iterator.next();
			if(preference.isReadOnly()) {
				continue;
			}
			String value = request.getParameter(preference.getName());
			if(value!=null) {
				request.getPreferences().setValue(preference.getName(), value);
			}
		}
		request.getPreferences().store(); //保存个性化设置
	}
	
	/**
	 * 获取个性化设置参数(com.yuanluesoft.portal.container.model.PortletPreference)列表,默认从portlet-config.xml中获取
	 * @param portletDefinition
	 * @return
	 */
	protected List listPortletPreferenceDefines(PortletDefinition portletDefinition) {
		return portletDefinition.getPreferences()==null || portletDefinition.getPreferences().isEmpty() ? null : new ArrayList(portletDefinition.getPreferences());
	}
	
	/**
	 * 生成个性化设置字段列表
	 * @param portletDefinition
	 * @param request
	 * @param exceptReadOnly
	 * @return
	 */
	public List generatePreferenceFields(PortletDefinition portletDefinition, RenderRequest request, boolean exceptReadOnly) {
		List preferences = listPortletPreferenceDefines(portletDefinition);
		String fieldPropertyNames = ",label,name,title,length,required,persistence,";
		List dynamicFormFields = new ArrayList();
		for(Iterator iterator = preferences==null ? null : preferences.iterator(); iterator!=null && iterator.hasNext();) {
			PortletPreference preference = (PortletPreference)iterator.next();
			if(exceptReadOnly && preference.isReadOnly()) { //排除只读设置
				continue;
			}
			if(preference.getValue().indexOf("label=")==-1) {
				continue;
			}
			Field field = new Field();
			field.setTitle(StringUtils.getPropertyValue(preference.getValue(), "label"));
			field.setName(preference.getName());
			field.setType(StringUtils.getPropertyValue(preference.getValue(), "type"));
			field.setInputMode(StringUtils.getPropertyValue(preference.getValue(), "inputMode"));
			field.setLength(StringUtils.getPropertyValue(preference.getValue(), "length"));
			field.setRequired("true".equals(StringUtils.getPropertyValue(preference.getValue(), "required")));
			List proprtties = StringUtils.getProperties(preference.getValue());
			for(Iterator iteratorProperty = proprtties.iterator(); iteratorProperty.hasNext();) {
				Attribute attribute = (Attribute)iteratorProperty.next();
				if(fieldPropertyNames.indexOf("," + attribute.getName() + ",")==-1) {
					field.setParameter(attribute.getName().startsWith("parameter_") ? attribute.getName().substring("parameter_".length()) : attribute.getName(), attribute.getValue());
				}
			}
			String value = request==null ? null : request.getPreferences().getValue(field.getName(), null);
			if(value==null) {
				value = StringUtils.getPropertyValue(preference.getValue(), "defaultValue");
			}
			dynamicFormFields.add(new DynamicFormField(field, value));
		}
		return dynamicFormFields;
	}
	
	/**
	 * 生成portlet URL
	 * @param htmlElement
	 * @param targetAttribute
	 * @param urlAttribute
	 * @param response
	 * @param isAction
	 */
	protected PortletURL generatePortletURL(HTMLElement htmlElement, String target, String url, RenderResponse response, boolean isAction) {
		if(target!=null && !target.isEmpty() && !target.equalsIgnoreCase("_self")) {
			return null;
		}
		if(url==null || url.isEmpty() || url.equals("#")) {
			return null;
		}
		if(url.startsWith("javascript:")) {
			return null;
		}
		PortletURL portletURL = isAction ? response.createActionURL() : response.createRenderURL();
		portletURL.setParameter("originalURL", url); //原始URL
		return portletURL;
	}
	
	/**
	 * 获取预置模板
	 * @param portletTemplateService
	 * @param portletEntity
	 * @param siteId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected HTMLDocument getTemplateHTMLDocument(PortletEntity portletEntity, long siteId, RenderRequest request, String pageApplication, String pageName) throws Exception {
		//获取portlet实体自定义模板
		for(Iterator iterator = portletEntity.getTemplates()==null ? null : portletEntity.getTemplates().iterator(); iterator!=null && iterator.hasNext();) {
			PortletEntityTemplate portletEntityTemplate = (PortletEntityTemplate)iterator.next();
			if(pageApplication==null || (pageApplication.equals(portletEntityTemplate.getApplicationName()) && pageName.equals(portletEntityTemplate.getPageName()))) {
				HTMLDocument template = portletTemplateService.getTemplateHTMLDocument(portletEntityTemplate.getId(), siteId, false, (HttpServletRequest)request);
				if(template!=null) {
					return template;
				}
				break;
			}
		}
		//获取预置模板
		return portletTemplateService.getNormalTemplateDocument(portletEntity.getPortletApplication(), portletEntity.getPortletName(), pageApplication, pageName);
	}
	
	/**
	 * 重设HTML文档
	 * @param htmlDocument
	 * @param request
	 * @param response
	 */
	protected void resetHTMLDocument(HTMLDocument htmlDocument, RenderRequest request, RenderResponse response) throws Exception {
		//重置链接URL
		NodeList links = htmlDocument.getElementsByTagName("a");
		for(int i=0; i<(links==null ? 0 : links.getLength()); i++) {
			HTMLAnchorElement link = (HTMLAnchorElement)links.item(i);
			PortletURL portletURL = generatePortletURL(link, link.getTarget(), link.getHref(), response, false);
			if(portletURL!=null) {
				link.setHref(portletURL.toString());
			}
		}
		//重置表单操作
		NodeList forms = htmlDocument.getElementsByTagName("form");
		for(int i=0; i<(forms==null ? 0 : forms.getLength()); i++) {
			HTMLFormElement form = (HTMLFormElement)forms.item(i);
			PortletURL portletURL = generatePortletURL(form, form.getTarget(), form.getAction(), response, false);
			if(portletURL==null) {
				continue;
			}
			String action = portletURL.toString();
			if(form.getMethod()==null || !form.getMethod().equalsIgnoreCase("get")) { //不是GET
				form.setAction(action);
				continue;
			}
			//将url参数转换为hidden字段
			Map queryParameters = StringUtils.getQueryParameters(action); //解析参数列表
			for(Iterator iterator = queryParameters==null ? null : queryParameters.keySet().iterator(); iterator!=null && iterator.hasNext();) {
				String name = (String)iterator.next();
				String[] values = (String[])queryParameters.get(name);
				for(int j=0; j<(values==null ? 0 : values.length); j++) {
					try {
						htmlParser.appendHiddenField(name, values[j], form);
					}
					catch (ServiceException e) {
						Logger.exception(e);
					}
				}
			}
			action = action.substring(0, action.indexOf('?'));
			form.setAction(action);
		}
		//将css、js引入body
		HTMLHeadElement header = htmlParser.getHTMLHeader(htmlDocument, false);
		NodeList nodes = header==null ? null : header.getChildNodes();
		for(int i=(nodes==null ? -1 : nodes.getLength()-1); i>=0; i--) {
			Node node = nodes.item(i);
			if((node instanceof HTMLScriptElement) || (node instanceof HTMLLinkElement) || (node instanceof HTMLStyleElement)) {
				NodeList bodyNodes = htmlDocument.getBody().getChildNodes();
				if(bodyNodes==null || bodyNodes.getLength()==0) {
					htmlDocument.getBody().appendChild(node);
				}
				else {
					htmlDocument.getBody().insertBefore(node, bodyNodes.item(0));
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#getTitle(javax.portlet.RenderRequest)
	 */
	protected String getTitle(RenderRequest request) {
		try {
			return super.getTitle(request);
		}
		catch(Exception e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#doDispatch(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doDispatch(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		super.doDispatch(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#doHelp(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doHelp(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		super.doHelp(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		
	}

	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#render(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		super.render(request, response);
	}
}
package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.config.ActionConfig;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.form.service.FormFieldService;
import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 页面元素处理器：表单处理器
 * @author linchuan
 *
 */
public class FormProcessor implements PageElementProcessor {
	public final static String FORM_NORMAL_CSS_COMPUTER = "/cms/css/form.css";
	
	private FormDefineService formDefineService; //表单定义服务
	private FormFieldService formFieldService; //表单字段服务
	private PageBuilder pageBuilder;
	private FormSecurityService formSecurityService; //表单安全服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//获取表单定义
		Form form = loadFormDefine(pageElement, request);
		if(form==null) {
			return;
		}
		HTMLDocument htmlDocument = (HTMLDocument)pageElement.getOwnerDocument();
		//添加样式表和脚本
		String css;
		if(requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_DATA && requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_PAGE && requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_POST) {
			css = Environment.getContextPath() + FORM_NORMAL_CSS_COMPUTER;
			if(htmlDocument.getElementById(css)==null) {
				htmlParser.appendCssFile(htmlDocument, css, true);
			}
		}
		htmlParser.appendScriptFile(htmlDocument, Environment.getContextPath() + "/jeaf/common/js/common.js");
		
		//引用脚本文件
		if(form.getJs()!=null) {
			String[] js = form.getJs().split(",");
			for(int i=0; i<js.length; i++) {
				if(js[i].startsWith("/")) {
					js[i] = Environment.getContextPath() + js[i];
				}
				else {
					js[i] = LinkUtils.fillParameters(js[i], true, false, false, "utf-8", webDirectory.getId(), parentSite.getId(), null, sitePage, request);
				}
				htmlParser.appendScriptFile(htmlDocument, js[i]);
			}
		}
		
		String action = LinkUtils.fillParameters(form.getAction(), true, false, false, "utf-8", webDirectory.getId(), parentSite.getId(), null, sitePage, request) ; //获取action
		if(form.getAction().startsWith("/")) {
			action = Environment.getContextPath() + action; //获取action
		}
		//插入siteId隐藏字段
		htmlParser.appendHiddenField("siteId", RequestUtils.getParameterLongValue(request, "siteId") + "", pageElement);
		//插入表单所在应用藏字段
		htmlParser.appendHiddenField("currentFormApplicationName", form.getApplicationName(), pageElement);
		//插入表单名称藏字段
		htmlParser.appendHiddenField("currentFormName", form.getName(), pageElement);
		
		///判断当前页面是否有jsp子页面,如果有,则存在表单嵌套
		boolean isNested = ListUtils.findObjectByProperty(sitePage.getSubPages(), "type", "jsp")!=null;

		//重设表单名称
		pageElement.setAttribute("name", form.getApplicationName() + "/" + form.getName());
		//重设表单ID
		if(requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_DATA) {
			pageElement.setAttribute("id", "form_" + UUIDLongGenerator.generateId());
		}
		else {
			Integer formIndex = (Integer)request.getAttribute("formIndex");
			if(formIndex==null) {
				formIndex = new Integer(0);
			}
			request.setAttribute("formIndex", new Integer(formIndex.intValue() + 1));
			pageElement.setAttribute("id", "form_" + formIndex);
		}

		//处理字段和按钮
		writeFormElements((HTMLFormElement)pageElement, pageElement.getChildNodes(), sitePage, form, webDirectory, parentSite, request, htmlParser, action, isNested, requestInfo);
		
		//处理非表单字段
		NodeList nodes = pageElement.getChildNodes();
		for(int i=nodes==null ? -1 : nodes.getLength()-1; i>=0; i--) {
			Node node = nodes.item(i);
			if(node instanceof HTMLElement) {
				pageBuilder.processPageElement((HTMLElement)node, webDirectory, parentSite, sitePage, requestInfo, request);
			}
		}
		
		if(!"true".equals(sitePage.getAttribute("writeSystemPrompt"))) { //没有输出过系统错误
			String error = getFormError(form, request);
			if(error!=null) {
				HTMLScriptElement script = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
				htmlParser.setTextContent(script, "var systemPrompt = '" + error.replaceAll("'", "") + "';window.setTimeout('window.top.alert(systemPrompt)', 500);");
				htmlDocument.getBody().appendChild(script);
				sitePage.setAttribute("writeSystemPrompt", "true");
			}
		}
		
		//将action中的参数,转换成hidden字段
		int index = action.lastIndexOf('?');
		if(index!=-1) {
			Map queryParameters = StringUtils.getQueryParameters(action); //解析参数列表
			for(Iterator iterator = queryParameters==null ? null : queryParameters.keySet().iterator(); iterator!=null && iterator.hasNext();) {
				String name = (String)iterator.next();
				String[] values = (String[])queryParameters.get(name);
				for(int i=0; i<(values==null ? 0 : values.length); i++) {
					htmlParser.appendHiddenField(name, values[i], pageElement);
				}
			}
			action = action.substring(0, index);
		}
		
		List hiddenFields = FieldUtils.listFormFields(form, null, null, "hidden", null, false, false, false, false, 0);
		//自动插入隐藏字段
		for(Iterator iterator = hiddenFields==null ? null : hiddenFields.iterator(); iterator!=null && iterator.hasNext();) {
			Field field = (Field)iterator.next();
			Object fieldValue = getFieldValue((HTMLFormElement)pageElement, sitePage, form, field, webDirectory, parentSite, request, true);
			String fieldStringValue = (fieldValue==null ? null : fieldValue.toString());
			if("id".equals(field.getName()) && (requestInfo.getPageType()==RequestInfo.PAGE_TYPE_CLIENT_DATA || fieldStringValue==null || fieldStringValue.isEmpty())) {
				fieldStringValue = "" + getId((HTMLFormElement)pageElement, sitePage, form, webDirectory, parentSite, request, requestInfo);
			}
			if(requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_DATA || !"requestCode".equals(field.getName())) {
				htmlParser.appendHiddenField(field.getName(), fieldStringValue, pageElement);
			}
		}
		//插入queryString隐藏字段
		String queryString = request.getParameter("queryString");
		if(queryString==null) { //提交的信息里没有queryString
			//添加隐藏字段,存放queryString
			htmlParser.appendHiddenField("queryString", StringUtils.removeQueryParameters(request.getQueryString(), "staticPageId,client.system,client.model,client.systemVersion,client.deviceId,client.retrieveClientPage,client.pageWidth"), pageElement);
		}

		//检查页面是否有验证码输入框
		boolean validateCodeImageRequired = false;
		NodeList inputs = pageElement.getElementsByTagName("input");
		for(int i=0; i<inputs.getLength(); i++) {
			HTMLInputElement input = (HTMLInputElement)inputs.item(i);
			if("validateCode".equals(input.getName())) {
				validateCodeImageRequired = true;
				break;
			}
		}
		//插入"requestCode"隐藏字段
		if(requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_DATA) { //不是客户端数据填充模式
			HTMLInputElement requestCodeField = (HTMLInputElement)htmlParser.getElementByName(pageElement, "input", "requestCode");
			if(requestCodeField==null) {
				//生成请求编码
				String requestCode = formSecurityService.registRequest(validateCodeImageRequired);
				htmlParser.appendHiddenField("requestCode", requestCode, pageElement);
			}
			else {
				String requestCode = requestCodeField.getValue();
				if(requestCode==null || requestCode.isEmpty()) {
					requestCode = formSecurityService.registRequest(validateCodeImageRequired);
					requestCodeField.setValue(requestCode);
				}
				else {
					formSecurityService.setValidateCodeImageRequired(requestCode, validateCodeImageRequired);
				}
			}
		}
		if(isNested) { //表单嵌套
			//把所有表单元素都拷贝到新建的span中
			HTMLElement span = (HTMLElement)pageElement.getOwnerDocument().createElement("span"); //把form改为span
			span.setId(pageElement.getId()); //ID
			pageElement.getParentNode().insertBefore(span, pageElement);
			//引入form中的全部元素
			NodeList childElements = pageElement.getChildNodes();
			if(childElements!=null) {
				for(int i=childElements.getLength()-1; i>=0; i--) {
					Node node = childElements.item(i);
					pageElement.removeChild(node);
					if(span.getFirstChild()==null) {
						span.appendChild(node);
					}
					else {
						span.insertBefore(node, span.getFirstChild());
					}
				}
			}
			pageElement.getParentNode().removeChild(pageElement); //移除表单
			pageElement = span;
		}
		else { //不是内嵌表单,设置表单属性
			String style = StringUtils.removeStyles(pageElement.getAttribute("style"), "margin,border,border-left,border-right,border-top,border-bottom");
			pageElement.setAttribute("style", "margin:0px" + (style==null ? "" : ";" + style));
			pageElement.removeAttribute("title");
			pageElement.removeAttribute("target");
			pageElement.setAttribute("action", action);
			pageElement.setAttribute("method", form.getMethod());
		}
		//设置样式
		pageElement.setAttribute("class", "form");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if(pageElement.getId()==null || pageElement.getId().equals("")) {
			return null;
		}
		htmlParser.appendCssFile((HTMLDocument)pageElement.getOwnerDocument(), Environment.getContextPath() + "/cms/css/form.css", true);
		//克隆表单
		HTMLDocument temporaryDocument = htmlParser.parseHTMLString("<html><head/><body/></html>", "utf-8");
		HTMLElement formElement = (HTMLElement)temporaryDocument.getBody().appendChild(temporaryDocument.importNode(pageElement, true));
		writePageElement(formElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
		//复制脚本、样式表到当前文档
		NodeList nodes = ((HTMLHeadElement)temporaryDocument.getElementsByTagName("head").item(0)).getChildNodes();
		if(nodes==null) {
			return null;
		}
		for(int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node instanceof HTMLScriptElement) { //脚本
				HTMLScriptElement script = (HTMLScriptElement)node;
				if(script.getSrc()==null) {
					htmlParser.appendScript((HTMLDocument)pageElement.getOwnerDocument(), htmlParser.getTextContent(script));
				}
				else {
					htmlParser.appendScriptFile((HTMLDocument)pageElement.getOwnerDocument(), script.getSrc());
				}
			}
			else if((node instanceof HTMLLinkElement) && "stylesheet".equalsIgnoreCase(((HTMLLinkElement)node).getRel())) { //样式表
				htmlParser.appendCssFile((HTMLDocument)pageElement.getOwnerDocument(), ((HTMLLinkElement)node).getHref(), false);
			}
		}
		return null;
	}
	
	/**
	 * 获取表单配置
	 * @param pageElement
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public Form loadFormDefine(HTMLElement pageElement, HttpServletRequest request) throws ServiceException {
		String formName = pageElement.getId();
		if(formName==null || formName.isEmpty()) {
			return null;
		}
		String applicationName = StringUtils.getPropertyValue(pageElement.getAttribute("action"), "applicationName") ;
		if(applicationName==null || applicationName.equals("")) {
			applicationName = pageElement.getAttribute("name");
		}
		Form form = null;
		ActionForm actionForm = getActionForm(request); //获取struts表单
		if(actionForm!=null &&
		   applicationName.equals(actionForm.getFormDefine().getApplicationName()) &&
		   formName.equals(actionForm.getFormDefine().getName())) { //是当前表单
			form = actionForm.getFormDefine();
			form.setActions(actionForm.getFormActions()); //替换为struts表单操作列表
		}
		if(form==null) {
			form = formDefineService.loadFormDefine(applicationName, formName);
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageGenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {

	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}
	
	/**
	 * 获取当前BEAN
	 * @param sitePage
	 * @param form
	 * @param request
	 * @return
	 */
	protected Object getBean(SitePage sitePage, Form form, HttpServletRequest request) {
		Object bean = request==null ? null : getActionForm(request);
		if(bean==null) {
			bean = sitePage.getAttribute("record");
		}
		if(bean!=null && (!(bean instanceof ActionForm) || !isCurrentForm(form, request))) {
			//检查bean和表单是否匹配
			String beanClassName = bean.getClass().getName();
			beanClassName = beanClassName.substring(beanClassName.lastIndexOf(".") + 1);
			if(!beanClassName.equalsIgnoreCase(form.getName().replaceAll("Form", ""))) {
				bean = null;
			}
		}
		return bean;
	}
	
	/**
	 * 获取字段值
	 * @param formElement
	 * @param sitePage
	 * @param form
	 * @param webDirectory
	 * @param parentSite
	 * @param request
	 * @param formatValue 是否格式化为文本
	 * @return
	 */
	private Object getFieldValue(HTMLFormElement formElement, SitePage sitePage, Form form, Field field, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request, boolean formatValue) {
		Object bean = getBean(sitePage, form, request);
		try {
			Object fieldValue = getFieldValue(formElement, sitePage, form, bean, field, webDirectory, parentSite, request);
			if(!formatValue) {
				return fieldValue;
			}
			return FieldUtils.formatFieldValue(fieldValue, field, bean, false, null, false, false, false, 0, null, null, request);
		}
		catch (Exception e) {
			return request==null ? (formatValue ? "" : null) : request.getParameter(field.getName());
		}
	}
	
	/**
	 * 获取字段值
	 * @param formElement
	 * @param sitePage
	 * @param form
	 * @param bean
	 * @param field
	 * @param webDirectory
	 * @param parentSite
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected Object getFieldValue(HTMLFormElement formElement, SitePage sitePage, Form form, Object bean, Field field, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws Exception {
		if(bean!=null) {
			Object fieldValue = FieldUtils.getFieldValue(bean, field.getName(), request);
			if(fieldValue==null) {
				fieldValue = FieldUtils.getFieldDefaultValue(field, (bean instanceof ActionForm) && "create".equals(((ActionForm)bean).getAct()), form.getApplicationName(), bean, request);
			}
			return fieldValue;
		}
		else {
			Object fieldValue = request==null ? null : request.getParameter(field.getName());
			if(fieldValue==null) {
				fieldValue = FieldUtils.getFieldDefaultValue(field, false, form.getApplicationName(), bean, request);
			}
			return fieldValue;
		}
	}
	
	/**
	 * 获取当前表单
	 * @param request
	 * @return
	 */
	protected ActionForm getActionForm(HttpServletRequest request) {
		ActionConfig actionConfig = (ActionConfig)request.getAttribute("org.apache.struts.action.mapping.instance");
		if(actionConfig!=null && actionConfig.getName()!=null) {
			return (ActionForm)request.getAttribute(actionConfig.getName());
		}
		return null;
	}
	
	/**
	 * 获取ID
	 * @param formElement
	 * @param sitePage
	 * @param form
	 * @param webDirectory
	 * @param parentSite
	 * @param request
	 * @param pageMode
	 * @return
	 */
	private long getId(HTMLFormElement formElement, SitePage sitePage, Form form, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request, RequestInfo requestInfo) {
		if(requestInfo.getPageType()==RequestInfo.PAGE_TYPE_CLIENT_DATA) { //客户端数据填充模式
			return 0;
		}
		if(form.getField("id")==null) {
			return 0;
		}
		Long attribute = (Long)sitePage.getAttribute("id");
		if(attribute!=null) {
			return attribute.longValue();
		}
		long id = 0;
		try {
			id = Long.parseLong((String)getFieldValue(formElement, sitePage, form, form.getField("id"), webDirectory, parentSite, request, true));
		}
		catch(Exception e) {

		}
		if(id==0) {
			id = UUIDLongGenerator.generateId();
		}
		sitePage.setAttribute("id", new Long(id));
		return id;
	}
	
	/**
	 * 处理字段和按钮,递归函数
	 * @param formElements
	 * @param sitePage
	 * @param form
	 * @param request
	 * @param isNested
	 * @param requestInfo
	 * @throws ServiceException
	 */
	private void writeFormElements(HTMLFormElement formElement, NodeList formElements, SitePage sitePage, Form form, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request, HTMLParser htmlParser, String formActionURL, boolean isNested, RequestInfo requestInfo) throws ServiceException {
		if(formElements==null || formElements.getLength()==0) {
			return;
		}
		for(int i=formElements.getLength()-1; i>=0; i--) {
			if(!(formElements.item(i) instanceof HTMLElement)) {
				continue;
			}
			HTMLElement htmlElement = (HTMLElement)formElements.item(i);
			String id = htmlElement.getId();
			if("field".equals(id) || "validateCode".equals(htmlElement.getAttribute("name"))) {
				if(!(htmlElement instanceof HTMLAnchorElement)) { //不是记录字段
					Field formField = FieldUtils.getFormField(form, htmlElement.getAttribute("name"), request);
					if(formField!=null) {
						writeFormField(formElement, htmlElement, sitePage, form, formField, webDirectory, parentSite, request, htmlParser, requestInfo);
					}
				}
			}
			else if("hiddenField".equals(id)) { //隐藏字段
				Field formField = FieldUtils.getFormField(form, htmlElement.getAttribute("title"), request);
				if(formField!=null) {
					writeFormField(formElement, htmlElement, sitePage, form, formField, webDirectory, parentSite, request, htmlParser, requestInfo);
				}
			}
			else if("readonlyField".equals(id)) { //只读字段
				Field formField = FieldUtils.getFormField(form, htmlElement.getAttribute("title"), request);
				if(formField!=null) {
					writeFormField(formElement, htmlElement, sitePage, form, formField, webDirectory, parentSite, request, htmlParser, requestInfo);
				}
			}
			else if("fieldSpan".equals(id)) { //单选/复选框
				Field formField = FieldUtils.getFormField(form, ((HTMLElement)htmlElement.getElementsByTagName("input").item(0)).getAttribute("name"), request);
				if(formField!=null) {
					writeFormField(formElement, htmlElement, sitePage, form, formField, webDirectory, parentSite, request, htmlParser, requestInfo);
				}
			}
			else if("button".equals(id)) {
				FormAction formAction = (FormAction)ListUtils.findObjectByProperty(form.getActions(), "title", htmlElement.getTitle());
				if(formAction!=null) { //按钮存在
					writeFormAction(formElement, htmlElement, sitePage, form, formAction, webDirectory, parentSite, request, htmlParser, formActionURL, isNested, requestInfo);
				}
				else { //按钮不存在
					htmlElement.getParentNode().removeChild(htmlElement);
				}
			}
			else if("systemPrompt".equals(id)) { //系统消息
				writeSystemPrompt(formElement, htmlElement, sitePage, form, webDirectory, parentSite, request, htmlParser, formActionURL, isNested);
			}
			else if("smsValidateCodeLink".equals(id)) { //短信验证码链接
				int sendLimit = StringUtils.getPropertyIntValue(htmlElement.getAttribute("urn"), "sendLimit", 3); //发送次数
				int timeInterval = StringUtils.getPropertyIntValue(htmlElement.getAttribute("urn"), "timeInterval", 100); //时间间隔(秒)
				htmlElement.removeAttribute("urn");
				htmlElement.removeAttribute("id");
				htmlElement.setAttribute("href", "#");
				//获取字段列表
				List fields = FieldUtils.listFormFields(form, null, null, null, "html,hidden,none", false, false, false, false, 0);
				//查找扩展属性validateCodeReceiveNumber值为true的字段
				for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
					Field field = (Field)iterator.next();
					if("true".equals(field.getParameter("validateCodeReceiveNumber"))) {
						htmlElement.setAttribute("onclick", "FormUtils.sendValidateCodeSms(document.getElementsByName('" + field.getName() + "')[0].value, " + sendLimit + ", " + timeInterval + ", '" + parentSite.getId() + "');return false;");
						break;
					}
				}
			}
			else if(htmlElement instanceof HTMLInputElement) {
				String type = ((HTMLInputElement)htmlElement).getType();
				if("text".equals(type) || type==null || type.isEmpty()) {
					formFieldService.writeFormField(htmlElement, null, null, null, 0, 0, sitePage.getApplicationName(), requestInfo, request);
				}
			}
			else {
				writeFormElements(formElement, htmlElement.getChildNodes(), sitePage, form, webDirectory, parentSite, request, htmlParser, formActionURL, isNested, requestInfo);
			}
		}
	}
	
	/**
	 * 输出表单字段
	 * @param formElement
	 * @param fieldElement
	 * @param sitePage
	 * @param form
	 * @param request
	 * @param requestInfo
	 * @throws ServiceException
	 */
	protected void writeFormField(HTMLFormElement formElement, HTMLElement fieldElement, SitePage sitePage, Form form, Field formField, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request, HTMLParser htmlParser, RequestInfo requestInfo) throws ServiceException {
		Object fieldValue = getFieldValue(formElement, sitePage, form, formField, webDirectory, parentSite, request, false);
		Object bean = getBean(sitePage, form, request);
		long recordId = getId(formElement, sitePage, form, webDirectory, parentSite, request, requestInfo);
		long componentRecordId = -1;
		try {
			String componentId = formField.getName().substring(0, formField.getName().lastIndexOf(".") + 1) + "id";
			componentRecordId = Long.parseLong((String)getFieldValue(formElement, sitePage, form, form.getField(componentId), webDirectory, parentSite, request, true));
		}
		catch(Exception e) {
			
		}
		formFieldService.writeFormField(fieldElement, formField, fieldValue, bean, recordId, componentRecordId, sitePage.getApplicationName(), requestInfo, request);
	}
	
	/**
	 * 输出表单按钮
	 * @param buttonElement
	 * @param sitePage
	 * @param form
	 * @param request
	 * @param isNested
	 * @param requestInfo
	 * @throws ServiceException
	 */
	protected void writeFormAction(HTMLFormElement formElement, HTMLElement buttonElement, SitePage sitePage, Form form, FormAction formAction, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request, HTMLParser htmlParser, String formActionURL, boolean isNested, RequestInfo requestInfo) throws ServiceException {
		buttonElement.removeAttribute("title");
		buttonElement.setAttribute("id", "button_" + formAction.getTitle());
		boolean newWindow = "1".equals(buttonElement.getAttribute("tabIndex"));
		buttonElement.removeAttribute("tabIndex");
		//替换onClick中的属性值
		String onclick;
		if(!"submit".equals(formAction.getType())){
			onclick = LinkUtils.fillParameters(formAction.getExecute().replaceAll("\\{FORMID\\}", formElement.getId()), false, true, false, "utf-8", webDirectory.getId(), parentSite.getId(), null, sitePage, request);
		}
		else {
			//检查字段中是否有htmleditor
			List htmlFields = FieldUtils.listFormFields(form, null, null, "htmleditor", null, false, false, false, false, 0);
			boolean htmlEditor = htmlFields!=null && !htmlFields.isEmpty();
			if(!isNested) { //表单没有嵌套
				onclick = (htmlEditor ? "try{HtmlEditor.saveHtmlContent();}catch(e){}" : "") +
					      "var form=document.getElementById('" + formElement.getId() + "');" +
						  "if(!form.target)form.target='" + (newWindow ? "_blank" : "_self") + "';" +
						  "FormUtils.resetBoxElements(form);" +
						  "if(!form.onsubmit || form.onsubmit())form.submit();";
			}
			else { //表单嵌套
				//创建新的form来提交
				onclick = (htmlEditor ? "try{HtmlEditor.saveHtmlContent();}catch(e){}" : "") +
						  "var form=document.getElementById('tempForm');" +
						  "if(!form) {" +
						  " form=document.createElement('form');" +
						  " form.id='tempForm';" +
						  " form.style.display='none';" +
						  " document.body.appendChild(form);" +
						  "}" +
						  "form.action='" + formActionURL + "';" +
						  "form.method='" + form.getMethod() + "';" +
						  "form.appendChild(document.getElementById('" + formElement.getId() + "').cloneNode(true));" +
						  "form.target='" + (newWindow ? "_blank" : "_self") + "';" +
						  "FormUtils.resetBoxElements(form);" +
						  "if(!form.onsubmit || form.onsubmit())form.submit();" +
						  (newWindow ? "form.innerHTML='';" : "");
			}
		}
		if("button".equalsIgnoreCase(buttonElement.getTagName())) {
			buttonElement.setAttribute("type", "button");
		}
		buttonElement.removeAttribute("href");
		buttonElement.setAttribute("onclick", onclick);
		if(formAction.isDefault()) {
			buttonElement.setAttribute("default", "true");
		}
	}
	
	/**
	 * 输出系统提示
	 * @param formElement
	 * @param systemPromptElement
	 * @param sitePage
	 * @param form
	 * @param webDirectory
	 * @param parentSite
	 * @param request
	 * @param htmlParser
	 * @param formActionURL
	 * @param isNested
	 * @throws ServiceException
	 */
	protected void writeSystemPrompt(HTMLFormElement formElement, HTMLElement systemPromptElement, SitePage sitePage, Form form, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request, HTMLParser htmlParser, String formActionURL, boolean isNested) throws ServiceException {
		String error = getFormError(form, request);
		if(error==null) {
			htmlParser.setTextContent(systemPromptElement, error);
			return;
		}
		String format = systemPromptElement.getAttribute("urn");
		if(format==null || format.isEmpty()) { //格式为空
			htmlParser.setTextContent(systemPromptElement, "  " + error);
			HTMLImageElement img = (HTMLImageElement)formElement.getOwnerDocument().createElement("img");
			img.setSrc(Environment.getContextPath() + "/jeaf/form/img/warn.gif");
			img.setAlign("absmiddle");
			systemPromptElement.insertBefore(img, systemPromptElement.getFirstChild());
		}
		else {
			HTMLDocument formatDocument = htmlParser.parseHTMLString(format.replaceAll("&lt;提示信息&gt;", StringUtils.escape(error)), "utf-8");
			NodeList formatNodes = formatDocument.getBody().getChildNodes();
			for(int i=0; i<formatNodes.getLength(); i++) {
				systemPromptElement.getParentNode().insertBefore(formElement.getOwnerDocument().importNode(formatNodes.item(i), true), systemPromptElement);
			}
			systemPromptElement.getParentNode().removeChild(systemPromptElement);
		}
		sitePage.setAttribute("writeSystemPrompt", "true");
	}
	
	/**
	 * 获取表单错误
	 * @param form
	 * @param request
	 * @return
	 */
	protected String getFormError(Form form, HttpServletRequest request) {
		//检查表单是否匹配
		if(!isCurrentForm(form, request)) {
			return null;
		}
		//获取表单错误
		ActionForm actionForm = getActionForm(request);
		if(actionForm==null || actionForm.getErrors()==null || actionForm.getErrors().isEmpty()) {
			return null;
		}
		return ListUtils.join(actionForm.getErrors(), "、", false);
	}

	/**
	 * 检查表单是否匹配
	 * @param form
	 * @param request
	 * @return
	 */
	protected boolean isCurrentForm(Form form, HttpServletRequest request) {
		String action = form.getAction();
		if(action==null) {
			return false;
		}
		ActionForm actionForm = getActionForm(request);
		if(actionForm!=null && actionForm.getFormDefine()==form) {
			return true;
		}
		int index = action.indexOf('?');
		if(index!=-1) {
			action = action.substring(0, index);
		}
		if(RequestUtils.getRequestURL(request, true).indexOf(action)!=-1) {
			return true;
		}
		String url = (String)request.getAttribute("javax.servlet.forward.request_uri");
		if(url!=null && url.indexOf(action)!=-1) {
			return true;
		}
		url = request.getHeader("Referer"); //如果当前请求是从别的页面链接过来的，那个属性就是那个页面的url，如果请求的url是直接从浏览器地址栏输入的就没有这个值
		return (url!=null && url.indexOf(action)!=-1);
	}
	
	/**
	 * @return the formDefineService
	 */
	public FormDefineService getFormDefineService() {
		return formDefineService;
	}

	/**
	 * @param formDefineService the formDefineService to set
	 */
	public void setFormDefineService(FormDefineService formDefineService) {
		this.formDefineService = formDefineService;
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

	/**
	 * @return the formSecurityService
	 */
	public FormSecurityService getFormSecurityService() {
		return formSecurityService;
	}

	/**
	 * @param formSecurityService the formSecurityService to set
	 */
	public void setFormSecurityService(FormSecurityService formSecurityService) {
		this.formSecurityService = formSecurityService;
	}

	/**
	 * @return the formFieldService
	 */
	public FormFieldService getFormFieldService() {
		return formFieldService;
	}

	/**
	 * @param formFieldService the formFieldService to set
	 */
	public void setFormFieldService(FormFieldService formFieldService) {
		this.formFieldService = formFieldService;
	}
}
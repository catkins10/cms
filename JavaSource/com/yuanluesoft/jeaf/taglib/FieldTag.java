package com.yuanluesoft.jeaf.taglib;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseFieldTag;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.DynamicForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.DynamicFormField;
import com.yuanluesoft.jeaf.form.service.FormFieldService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.cyberneko.CyberNekoHTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class FieldTag extends BaseFieldTag {
	private boolean writeonly = false;
	private BusinessDefineService businessDefineService; //业务逻辑定义服务
	private FormFieldService formFieldService; //表单字段服务
	private HTMLParser htmlParser; //HTML解析器
	private HTMLDocument htmlDocument = null; //HTML文档
	
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.BaseFieldTag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		try {
			if(businessDefineService==null) {
				businessDefineService = (BusinessDefineService)Environment.getService("businessDefineService");
			}
			if(formFieldService==null) {
				formFieldService = (FormFieldService)Environment.getService("formFieldService");
			}
			if(htmlParser==null) {
				htmlParser = (HTMLParser)Environment.getService("htmlParser");
				htmlDocument = new CyberNekoHTMLParser().parseHTMLString("<html><body/></html>", "utf-8");
			}
		}
		catch(Exception e) {
			
		}
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		Object bean = TagUtils.getInstance().lookup(pageContext, name, null, null);
		Field fieldDefine = null;
		DynamicFormField dynamicFormField = null;
		String applicationName = null;
		if(bean instanceof ActionForm) { //表单
			ActionForm form = (ActionForm)bean;
			applicationName = form.getFormDefine().getApplicationName(); //应用名称
			if(bean instanceof DynamicForm) { //动态表单
				DynamicForm dynamicForm = (DynamicForm)bean;
				dynamicFormField = (DynamicFormField)ListUtils.findObjectByProperty(dynamicForm.getFields(), "fieldDefine.name", property);
				if(dynamicFormField!=null) {
					fieldDefine = dynamicFormField.getFieldDefine(); //获取字段配置
				}
			}
			fieldDefine = fieldDefine!=null ? fieldDefine : FieldUtils.getFormField(form.getFormDefine(), property, request); //获取字段配置
			if(fieldDefine==null) {
				throw new JspException("Field " + property + " is not found in class " + form.getClass().getName() + ".");
			}
		}
		else {
			fieldDefine = FieldUtils.getRecordField(bean.getClass().getName(), property, request); //获取字段配置
			if(fieldDefine==null) {
				throw new JspException("Field " + property + " is not found in class " + bean.getClass().getName() + ".");
			}
			try {
				applicationName = businessDefineService.getBusinessObject(bean.getClass()).getApplicationName();
			}
			catch(Exception e) {
				
			}
		}
        //是否只读
		writeonly = writeonly ? writeonly : "readonly".equals(fieldDefine.getInputMode()) || "hidden".equals(fieldDefine.getInputMode());
		//获取字段值
		Object fieldValue;
		try {
			fieldValue = getFieldValue(dynamicFormField, fieldDefine, bean, applicationName, request);
		}
		catch(Exception e) {
			throw new JspException(e);
		}
		//生成页面元素HTML
		if(writeonly) { //只读
			try {
				TagUtils.getInstance().write(this.pageContext, FieldUtils.formatFieldValue(fieldValue, fieldDefine, bean, writeonly, null, true, false, false, 0, null, null, (HttpServletRequest)pageContext.getRequest()));
			} 
			catch (Exception e) {
				throw new JspException(e);
			}
			return SKIP_BODY;
		}
		RequestInfo requestInfo = (RequestInfo)request.getAttribute("RequestInfo");
		if(requestInfo==null) {
			requestInfo = RequestUtils.getRequestInfo(request);
			request.setAttribute("RequestInfo", requestInfo);
		}
		try {
			writeField(fieldDefine, fieldValue, bean, applicationName, requestInfo, request);
		}
		catch(Exception e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.BaseInputTag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		release();
		return super.doEndTag();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.BaseFieldTag#release()
	 */
	public void release() {
		super.release();
		writeonly = false;
	}
	
	/**
	 * 获取字段值
	 * @param dynamicFormField
	 * @param fieldDefine
	 * @param bean
	 * @param applicationName
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Object getFieldValue(DynamicFormField dynamicFormField, Field fieldDefine, Object bean, String applicationName, HttpServletRequest request) throws Exception {
		if(dynamicFormField!=null) {
			return dynamicFormField.getValue();
		}
		Object fieldValue = FieldUtils.getFieldValue(bean, property, request);
		if(fieldValue!=null) {
			return fieldValue;
		}
		//检查是否旧记录
		ActionForm form;
		if(bean instanceof ActionForm &&
		   (FormAction.OPEN_MODE_EDIT.equals((form = (ActionForm)bean).getAct()) || FormAction.OPEN_MODE_OPEN.equals(form.getAct()) || FormAction.OPEN_MODE_EDIT_COMPONENT.equals(form.getAct()))) {
			return null;
		}
		//获取默认值
		return FieldUtils.getFieldDefaultValue(fieldDefine, false, applicationName, bean, request);
	}
	
	/**
	 * 输出字段
	 * @param fieldDefine
	 * @param fieldValue
	 * @param bean
	 * @param applicationName
	 * @param requestInfo
	 * @param request
	 * @throws Exception
	 */
	private void writeField(Field fieldDefine, Object fieldValue, Object bean, String applicationName, RequestInfo requestInfo, HttpServletRequest request) throws Exception {
		HTMLDocument htmlDocument = (HTMLDocument)this.htmlDocument.cloneNode(false);
		HTMLElement htmlElement;
		if("textarea".equals(fieldDefine.getInputMode())) {
			htmlElement = (HTMLElement)htmlDocument.createElement("textarea");
		}
		else if("radio".equals(fieldDefine.getInputMode()) || "multibox".equals(fieldDefine.getInputMode())) {
			htmlElement = (HTMLElement)htmlDocument.createElement("span");
		}
		else {
			htmlElement = (HTMLElement)htmlDocument.createElement("input");
			htmlElement.setAttribute("type", "text");
		}
		if(getTitle()!=null) {
			fieldDefine.setTitle(getTitle());
		}
		String[] attributeNames = {"accesskey", "accept", "alt", "disabled", "readonly", "style", "styleClass", "styleId", "tabindex",
							   	   "onblur", "onchange", "onclick", "ondblclick", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup"};
		for(int i = 0; i < attributeNames.length; i++) {
			Object attributeValue = null;
			try {
				attributeValue = PropertyUtils.getProperty(this, attributeNames[i]);
			}
			catch(Exception e) {
				
			}
			if(attributeValue!=null && !Boolean.FALSE.equals(attributeValue)) {
				htmlElement.setAttribute("styleClass".equals(attributeNames[i]) ? "class" : attributeNames[i], "" + attributeValue);
			}
		}
		htmlElement.setAttribute("name", property);
		htmlDocument.getBody().appendChild(htmlElement);
		//单选框
		if("checkbox".equals(fieldDefine.getInputMode())) {
			htmlElement.setAttribute("type", "checkbox");
			//查找label
			HTMLLabelElement label = (HTMLLabelElement)htmlDocument.createElement("label");
			label.setAttribute("style", "padding-left: 3px");
			label.setId("label_" + htmlElement.getAttribute("name"));
			htmlParser.setTextContent(label, (String)fieldDefine.getParameter("label"));
			htmlDocument.getBody().appendChild(label);
		}
		//获取记录ID
		Number recordId = null;
		try {
			recordId = (Number)PropertyUtils.getProperty(bean, "id");
		} 
		catch(Exception e) {
			
		}
		//获取组成部分记录ID
		Number componentRecordId = null;
		try {
			componentRecordId = (Number)PropertyUtils.getProperty(bean, property.substring(0, property.lastIndexOf(".")) + ".id");
		}
		catch(Exception e) {
			
		}
		//调用表单字段服务输出字段
		formFieldService.writeFormField(htmlElement, fieldDefine, fieldValue, bean, recordId==null ? 0 : recordId.longValue(), componentRecordId==null ? -1 : componentRecordId.longValue(), applicationName, requestInfo, request);
		//输出脚本
		HTMLHeadElement headElement = htmlParser.getHTMLHeader(htmlDocument, false);
		NodeList scripts = headElement==null ? null : headElement.getElementsByTagName("script");
		for(int i = 0; i < (scripts==null ? 0 : scripts.getLength()); i++) {
			HTMLScriptElement scriptElement = (HTMLScriptElement)scripts.item(i);
			if(scriptElement.getSrc()!=null && !scriptElement.getSrc().isEmpty()) {
				writeJsFile(scriptElement.getSrc(), request);
			}
		}
		//输出字段HTML
		TagUtils.getInstance().write(this.pageContext, htmlParser.getBodyHTML(htmlDocument, "utf-8", true));
	}
	
	/**
	 * 引用脚本,如果已经引用过,则不输出
	 * @param jsFile
	 * @return
	 */
	private void writeJsFile(String jsFile, HttpServletRequest request) throws Exception {
		List importScripts = (List)request.getAttribute("importScripts");
		if(importScripts==null) {
			importScripts = new ArrayList();
			request.setAttribute("importScripts", importScripts);
		}
		//检查是否已经引用过
		if(importScripts.indexOf(jsFile)!=-1) {
			return;
		}
		importScripts.add(jsFile);
		TagUtils.getInstance().write(this.pageContext, "<script language=\"JavaScript\" src=\"" + jsFile + "\"></script>");
	}

	/**
	 * @return the writeonly
	 */
	public boolean isWriteonly() {
		return writeonly;
	}

	/**
	 * @param writeonly the writeonly to set
	 */
	public void setWriteonly(boolean writeonly) {
		this.writeonly = writeonly;
	}
}
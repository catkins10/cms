package com.yuanluesoft.jeaf.form.service.spring;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.service.FormFieldService;
import com.yuanluesoft.jeaf.htmleditor.model.HtmlEditorCommandSet;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 表单字段服务
 * @author linchuan
 *
 */
public class FormFieldServiceImpl implements FormFieldService {
	private HTMLParser htmlParser; //HTML解析器
	
	/**
	 * 输出表单字段
	 * @param formElement
	 * @param fieldElement
	 * @param sitePage
	 * @param form
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	public void writeFormField(HTMLElement fieldElement, Field formField, Object fieldValue, Object bean, long recordId, long componentRecordId, String applicationName, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if(formField==null) {
			writeTextField(fieldElement, formField, null, request);
			return;
		}
		fieldElement.removeAttribute("title");
		fieldElement.removeAttribute("id");
		
		//设置提示信息
		String remark = (String)formField.getParameter("remark");
		fieldElement.setTitle(remark==null ? formField.getTitle() : remark);
		
		//处理扩展属性
		request.setAttribute("fieldExtendProperties", fieldElement.getAttribute("urn"));
		fieldElement.removeAttribute("urn");
		//添加事件处理
		appendEventAttributes(fieldElement, formField, request);
		//字段值
		String fieldTextValue = null;
		try {
			fieldTextValue = FieldUtils.formatFieldValue(fieldValue, formField, bean, false, null, false, false, false, 0, null, null, request);
		}
		catch (Exception e) {
			
		}
		if((requestInfo.getPageType()==RequestInfo.PAGE_TYPE_CLIENT_DATA || requestInfo.getPageType()==RequestInfo.PAGE_TYPE_CLIENT_POST) && "htmleditor".equals(formField.getInputMode())) { //客户端
			formField.setInputMode("textarea");
			formField.setParameter("rows", "6");
		}
		//设置输入提醒
		String alt;
		if(requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_DATA && requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_POST &&
		   ",text,textarea,dropdown,select,date,datetime,".indexOf("," + formField.getInputMode() + ",")!=-1 &&
		   (alt = fieldElement.getAttribute("alt"))!=null & !alt.isEmpty()) {
			//fieldElement.setAttribute("placeholder", alt); //HTML5
			if(fieldTextValue==null || fieldTextValue.isEmpty() ) {
				fieldTextValue = alt;
			}
			String onfocus = fieldElement.getAttribute("onfocus");
			String onblur = fieldElement.getAttribute("onblur");
			fieldElement.setAttribute("onfocus", "if(value==alt)value='';" + (onfocus==null ? "" :  onfocus));
			fieldElement.setAttribute("onblur", "if(value=='')value=alt;" + (onblur==null ? "" :  onblur));
		}
		if("date".equals(formField.getInputMode())) { //日期/时间
			writeDateField(fieldElement, formField, fieldTextValue, requestInfo, request);
		}
		else if("time".equals(formField.getInputMode())) { //时间
			writeTimeField(fieldElement, formField, fieldTextValue, requestInfo, request);
		}
		else if("day".equals(formField.getInputMode())) { //月日
			writeDayField(fieldElement, formField, fieldTextValue, requestInfo, request);
		}
		else if("datetime".equals(formField.getInputMode())) { //日期/时间
			writeDateTimeField(fieldElement, formField, fieldTextValue, requestInfo, request);
		}
		else if("checkbox".equals(formField.getInputMode())) { //复选框
			writeCheckboxField(fieldElement, formField, fieldTextValue, request);
		}
		else if("multibox".equals(formField.getInputMode())) { //多选框
			writeMultiboxOrRadioField(fieldElement, formField, fieldValue, fieldTextValue, bean, true, request);
		}
		else if("radio".equals(formField.getInputMode())) { //单选框
			writeMultiboxOrRadioField(fieldElement, formField, fieldValue, fieldTextValue, bean, false, request);
		}
		else if("dropdown".equals(formField.getInputMode())) { //下拉列表
			writeDropDownField(fieldElement, formField, fieldTextValue, bean, requestInfo, request);
		}
		else if("select".equals(formField.getInputMode())) { //选择对话框
			writeSelectField(fieldElement, formField, fieldTextValue, request);
		}
		else if("textarea".equals(formField.getInputMode())) { //多行文本框
			writeTextArea(fieldElement, formField, fieldTextValue, request);
		}
		else if("htmleditor".equals(formField.getInputMode())) { //HTML编辑器
			writeHtmlEditor(fieldElement, formField, fieldTextValue, bean, recordId, applicationName, request);
		}
		else if("hidden".equals(formField.getInputMode())) { //隐藏字段
			writeHiddenField(fieldElement, formField, fieldTextValue);
		}
		else if("readonly".equals(formField.getInputMode())) { //只读字段
			writeReadonlyField(fieldElement, formField, fieldTextValue);
		}
		else if("attachment".equals(formField.getInputMode())) { //附件
			writeAttachmentEditor(fieldElement, formField, bean, recordId, componentRecordId, request);
		}
		else if(fieldElement instanceof HTMLInputElement) {
			writeTextField(fieldElement, formField, fieldTextValue, request);
		}
	}
	
	/**
	 * 输出文本框
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeTextField(HTMLElement fieldElement, Field formField, String fieldValue, HttpServletRequest request) throws ServiceException {
		String fieldName = fieldElement.getAttribute("name"); //字段名称
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("SCRIPT");
		String styleClass = getStyleClass(fieldElement, formField);
		String style = getStyle(fieldElement, styleClass);
		//TextField = function(inputElementHTML, fieldName, styleClass, style, parentElement)
		String html = formField==null ? htmlParser.getElementHTML(fieldElement, "utf-8").replaceAll("'", "\\\\'").replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n") : renderInputElement(fieldElement, formField, fieldName, "text", fieldValue, FieldUtils.getFieldInputLength(formField), "true".equals(formField.getParameter("readonly")), request);
		htmlParser.setTextContent(scriptElement, "new TextField('" + html + "', '" + styleClass + "', '" + style + "');");
		fieldElement.getParentNode().replaceChild(scriptElement, fieldElement);
	}
	
	/**
	 * 输出多行文本框
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeTextArea(HTMLElement fieldElement, Field formField, String fieldValue, HttpServletRequest request) throws ServiceException {
		if(request!=null) {
			htmlParser.setTextContent(fieldElement, fieldValue); //设置字段值
		}
		int length = FieldUtils.getFieldInputLength(formField);
		if(length>0) {
			fieldElement.setAttribute("maxlength", length + "");
			//添加onpropertychange(ie),oninput(firefox)事件
			String script = "if(value.length>" + length + ")value=value.substring(0,value.charAt(" + length + ")=='\\\\n'?" + (length-1) + ":" + length + ");try{onchange();}catch(e){}";
			fieldElement.setAttribute("onpropertychange", "if(event.propertyName=='value'){" + script + "}");
			fieldElement.setAttribute("oninput", script);
		}
		//设置行数
		String rows = (String)formField.getParameter("rows");
		if(rows!=null && !rows.isEmpty()) {
			fieldElement.setAttribute("rows", rows);
		}
		String style = fieldElement.getAttribute("style");
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("SCRIPT");
		String styleClass = getStyleClass(fieldElement, formField);
		//TextAreaField = function(inputElementHTML, fieldName, styleClass, style, parentElement)
		htmlParser.setTextContent(scriptElement, "new TextAreaField('" + htmlParser.getElementHTML(fieldElement, "utf-8").replaceAll("'", "\\\\'").replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n") + "', '" + styleClass + "', '" + style + "');");
		fieldElement.getParentNode().replaceChild(scriptElement, fieldElement);
	}
	
	/**
	 * 输出日期类型字段
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeDateField(HTMLElement fieldElement, Field formField, String fieldValue, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String fieldName = fieldElement.getAttribute("name"); //字段名称
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("SCRIPT");
		String styleClass = getStyleClass(fieldElement, formField);
		String style = getStyle(fieldElement, styleClass);
		//DateField(inputElementHTML, styleClass, style, selectButtonStyleClass, selectButtonStyle, alignLeft, parentElement, terminalType)
		htmlParser.setTextContent(scriptElement, "new DateField('" + renderInputElement(fieldElement, formField, fieldName, "text", fieldValue, 0, false, request) + "', '" + styleClass + "', '" + style + "', '', '', false, null, '" + requestInfo.getTerminalType() + "');");
		fieldElement.getParentNode().replaceChild(scriptElement, fieldElement);
	}
	
	/**
	 * 输出时间类型字段
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeTimeField(HTMLElement fieldElement, Field formField, String fieldValue, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String fieldName = fieldElement.getAttribute("name"); //字段名称
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("SCRIPT");
		String styleClass = getStyleClass(fieldElement, formField);
		String style = getStyle(fieldElement, styleClass);
		//TimeField(inputElementHTML, secondEnabled, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, terminalType)
		htmlParser.setTextContent(scriptElement, "new TimeField('" + renderInputElement(fieldElement, formField, fieldName, "hidden", fieldValue, 0, false, request) + "', " + ("true".equals(formField.getParameter("secondEnabled"))) + ", '" + styleClass + "', '" + style + "', '', '', null, '" + requestInfo.getTerminalType() + "');");
		fieldElement.getParentNode().replaceChild(scriptElement, fieldElement);
	}
	
	/**
	 * 输出月日类型字段
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeDayField(HTMLElement fieldElement, Field formField, String fieldValue, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String fieldName = fieldElement.getAttribute("name"); //字段名称
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("SCRIPT");
		String styleClass = getStyleClass(fieldElement, formField);
		String style = getStyle(fieldElement, styleClass);
		//DayField(inputElementHTML, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, terminalType)
		htmlParser.setTextContent(scriptElement, "new DayField('" + renderInputElement(fieldElement, formField, fieldName, "hidden", fieldValue, 0, false, request) + "', '" + styleClass + "', '" + style + "', '', '', null, '" + requestInfo.getTerminalType() + "');");
		fieldElement.getParentNode().replaceChild(scriptElement, fieldElement);
	}
	
	/**
	 * 输出日期时间类型字段
	 * @param fieldElement
	 * @param fieldValue
	 * @param formField
	 * @param pageMode
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeDateTimeField(HTMLElement fieldElement, Field formField, String fieldValue, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String fieldName = fieldElement.getAttribute("name"); //字段名称
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("SCRIPT");
		String styleClass = getStyleClass(fieldElement, formField);
		String style = getStyle(fieldElement, styleClass);
		//DateTimeField = function(fieldHTML, fieldName, fieldValue, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, clickShowPicker, terminalType)
		htmlParser.setTextContent(scriptElement, "new DateTimeField('" + renderInputElement(fieldElement, formField, fieldName, "hidden", fieldValue, 0, false, request) + "', '" + styleClass + "', '" + style + "', '', '', null, false, '" + requestInfo.getTerminalType() + "');");
		fieldElement.getParentNode().replaceChild(scriptElement, fieldElement);
	}
	
	/**
	 * 输出下拉类型字段
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @param bean
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeDropDownField(HTMLElement fieldElement, Field formField, String fieldValue, Object bean, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//设置下拉列表
		String listValues = FieldUtils.getSelectItemsText(formField, bean, request);
		if(listValues==null) { //没有下拉选项
			fieldElement.setAttribute("value", fieldValue); //设置字段值
			return;
		}
		String fieldName = fieldElement.getAttribute("name");
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("SCRIPT");
		String valueField = (String)formField.getParameter("valueField");
		String titleField = (String)formField.getParameter("titleField");
		boolean selectOnly = "true".equals(formField.getParameter("selectOnly"));
		if(valueField==null && titleField==null && selectOnly) { // && listValues.indexOf('|')!=-1
			//插入隐藏字段
			HTMLInputElement hidden = (HTMLInputElement)fieldElement.cloneNode(false);
			hidden.setAttribute("type", "hidden");
			hidden.setName(fieldName);
			String alt = null;
			if(requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_DATA && requestInfo.getPageType()!=RequestInfo.PAGE_TYPE_CLIENT_POST &&
			   (alt = fieldElement.getAttribute("alt"))!=null && alt.equals(fieldValue)) {
				fieldValue = null;
			}
			hidden.setAttribute("value", fieldValue);
			fieldElement.getParentNode().insertBefore(hidden, fieldElement);
			valueField = fieldName + "_" + UUIDLongGenerator.generateId();
			hidden.setId(valueField);
			
			fieldName += "_title";
			titleField = fieldName;
			fieldValue = FieldUtils.getItemTitle(listValues, fieldValue);
			if(fieldValue==null || fieldValue.isEmpty()) {
				fieldValue = alt;
			}
		}
		else {
			if(valueField==null) {
				valueField = fieldName;
			}
			if(titleField==null) {
				titleField = fieldName;
			}
		}
		int length = FieldUtils.getFieldInputLength(formField); //字段长度
		String listPickerWidth = (String)formField.getParameter("listPickerWidth");
		String styleClass = getStyleClass(fieldElement, formField);
		String style = getStyle(fieldElement, styleClass);
		//DropdownField(inputElementHTML, listValues, valueField, titleField, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, listPickerWidth, terminalType)
		htmlParser.setTextContent(scriptElement, "new DropdownField('" + renderInputElement(fieldElement, formField, fieldName, "text", fieldValue, length, selectOnly, request) + "', '" + listValues.replaceAll("'", "\\\\'").replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n") + "', '" + valueField+ "', '" + titleField + "', '" + styleClass + "', '" + style + "', '', '', null, " + (listPickerWidth==null ? "null" : "'" + listPickerWidth + "'") + ", '" + requestInfo.getTerminalType() + "');");
		fieldElement.getParentNode().replaceChild(scriptElement, fieldElement);
	}
	
	/**
	 * 输出选择类型的字段
	 * @param sitePage
	 * @param form
	 * @param formField
	 * @param fieldElement
	 * @param request
	 * @param htmlParser
	 * @throws ServiceException
	 */
	protected void writeSelectField(HTMLElement fieldElement, Field formField, String fieldValue, HttpServletRequest request) throws ServiceException {
		String js = (String)formField.getParameter("js");
		if(js!=null) {
			String[] scripts = js.split(",");
			for(int i=0; i<scripts.length; i++) {
				HTMLScriptElement scriptElement =  (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("script");
				scriptElement.setCharset("utf-8");
				scriptElement.setSrc(Environment.getContextPath() + scripts[i]);
				fieldElement.getParentNode().insertBefore(scriptElement, fieldElement);
			}
		}
		String fieldName = fieldElement.getAttribute("name");
		String execute = FieldUtils.getSelectDialogScript(formField, null, request);
		//添加脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("SCRIPT");
		int length = FieldUtils.getFieldInputLength(formField); //字段长度
		String styleClass = getStyleClass(fieldElement, formField);
		String style = getStyle(fieldElement, styleClass);
		String selectButtonStyleClass = (String)formField.getParameter("selectButtonStyleClass");
		htmlParser.setTextContent(scriptElement, "new SelectField('" + renderInputElement(fieldElement, formField, fieldName, "text", fieldValue, length, "true".equals(formField.getParameter("selectOnly")), request) + "', '" + execute.replaceAll("\\x5c", "\\\\\\\\").replaceAll("\\x27", "\\\\'") + "', '" + styleClass + "', '" + style + "', '" + selectButtonStyleClass + "', '');");
		fieldElement.getParentNode().replaceChild(scriptElement, fieldElement);
	}
	
	/**
	 * 输出复选框或单选框
	 * @param fieldSpanElement
	 * @param formField
	 * @param fieldValue
	 * @param fieldTextValue
	 * @param bean
	 * @param multibox
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeMultiboxOrRadioField(HTMLElement fieldElement, Field formField, Object fieldValue, String fieldTextValue, Object bean, boolean multibox, HttpServletRequest request) throws ServiceException {
		//获取值列表
		List selectItems = FieldUtils.listSelectItems(formField, bean, request);
		if(selectItems==null || selectItems.isEmpty()) {
			fieldElement.getParentNode().removeChild(fieldElement); //删除span
			return;
		}
		List multiboxValues  = multibox ? ListUtils.generateListFromArray(fieldValue) : null;
		for(int i=0; i<selectItems.size(); i++) {
			String[] options = (String[])selectItems.get(i);
			HTMLInputElement inputElement = (HTMLInputElement)fieldElement.getOwnerDocument().createElement("INPUT");
			inputElement.setAttribute("type", multibox ? "checkbox" : "radio"); //类型
			inputElement.setName(formField.getName()); //名称
			String value = options[options.length>1 ? 1 : 0];
			inputElement.setValue(value); //值
			inputElement.setId(formField.getName() + "_" + (value==null ? "" : value).hashCode() + "_" + i); //ID
			if(!multibox) { //单选
				inputElement.setChecked(StringUtils.isEquals(fieldTextValue, value));
			}
			for(Iterator iterator = multiboxValues==null ? null : multiboxValues.iterator(); iterator!=null && iterator.hasNext();) {
				Object itemValue = iterator.next();
				if(StringUtils.isEquals(value, itemValue==null ? null : itemValue.toString())) {
					inputElement.setChecked(true);
					break;
				}
			}
			fieldElement.getParentNode().insertBefore(inputElement, fieldElement); //插入复选框或单选框
			inputElement.setClassName(multibox ? "checkbox" : "radio");
			//复制事件属性
			NamedNodeMap attributes = fieldElement.getAttributes();
			for(int j = 0; j < (attributes==null ? 0 : attributes.getLength()); j++) {
				Node attribute = attributes.item(j);
				if(attribute.getNodeName()!=null && attribute.getNodeName().startsWith("on")) {
					inputElement.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
				}
			}
			
			HTMLLabelElement labelElement = (HTMLLabelElement)fieldElement.getOwnerDocument().createElement("LABEL");
			labelElement.setAttribute("for", inputElement.getId());
			labelElement.appendChild(fieldElement.getOwnerDocument().createTextNode(options[0]));
			fieldElement.getParentNode().insertBefore(labelElement, fieldElement);
			if(i<selectItems.size()-1) {
				fieldElement.getParentNode().insertBefore(fieldElement.getOwnerDocument().createTextNode(" "), fieldElement); //插入一个空格
			}
		}
		fieldElement.getParentNode().removeChild(fieldElement); //删除span
	}
	
	/**
	 * 输出checkbox
	 * @param fieldElement
	 * @param fieldValue
	 * @param formField
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeCheckboxField(HTMLElement fieldElement, Field formField, String fieldValue, HttpServletRequest request) throws ServiceException {
		HTMLInputElement inputElement = (HTMLInputElement)fieldElement;
		inputElement.setValue((String)formField.getParameter("value"));
		inputElement.setChecked(fieldValue!=null && fieldValue.equals(inputElement.getValue()));
		inputElement.setId("field_" + formField.getName());
		//查找label
		HTMLLabelElement label = (HTMLLabelElement)htmlParser.getElementById(((HTMLDocument)fieldElement.getOwnerDocument()).getBody(), "label", "label_" + inputElement.getName());
		if(label!=null) {
			label.removeAttribute("id");
			label.setAttribute("for", inputElement.getId());
		}
	}
	
	/**
	 * 输出HTML编辑器字段
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @param bean
	 * @param id
	 * @param sitePage
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeHtmlEditor(HTMLElement fieldElement, Field formField, String fieldValue, Object bean, long id, String applicationName, HttpServletRequest request) throws ServiceException {
		//添加样式表和脚本
		htmlParser.appendScriptFile((HTMLDocument)fieldElement.getOwnerDocument(), Environment.getContextPath() + "/jeaf/htmleditor/js/htmleditor.js");
		
		//创建html编辑器,创建div,作为加载编辑器的容器
		String cssText = fieldElement.getAttribute("style");
		String width = StringUtils.getStyle(cssText, "width");
		if(width==null || width.equals("")) {
			width = "100%";
		}
		String height = StringUtils.getStyle(cssText, "height");
		if(height==null || height.isEmpty()) {
			height = (String)formField.getParameter("height");
		}
		if(height==null || height.equals("")) {
			height = "160px";
		}
		else {
			height = height.trim();
			if(!height.endsWith("%") && (height.endsWith("px") || height.endsWith("pt")) && Integer.parseInt(height.replaceAll("px", "").replaceAll("pt", ""))<50) {
				height = "160px";
			}
		}
		if(!height.endsWith("%") && !height.endsWith("px")) {
			height += "px";
		}
		HTMLDivElement div = (HTMLDivElement)fieldElement.getOwnerDocument().createElement("div");
		div.setAttribute("style", "width:" + width + "; height:" + height);
		fieldElement.getParentNode().replaceChild(div, fieldElement);
		//插入隐藏字段,存放字段值
		HTMLInputElement hiddenField = (HTMLInputElement)fieldElement.getOwnerDocument().createElement("input");
		hiddenField.setAttribute("type", "hidden");
		hiddenField.setAttribute("name", formField.getName());
		hiddenField.setAttribute("value", fieldValue); //设置字段值
		String inputId = "html_" + UUIDLongGenerator.generateId();
		hiddenField.setId(inputId);
		div.appendChild(hiddenField);
		//插入创建编辑器的脚本
		String attachmentSelector = (String)formField.getParameter("attachmentSelector");
		if(attachmentSelector==null) {
			attachmentSelector = "selectAttachment.shtml";
		}
		else {
			attachmentSelector = StringUtils.fillParameters(attachmentSelector, true, false, false, "utf-8", bean, request, null);
		}
		if(attachmentSelector.indexOf("id=")==-1) {
			attachmentSelector += (attachmentSelector.indexOf('?')==-1 ? '?' : '&') + "id=" + id;
		}
		String plugins = (String)formField.getParameter("plugins");
		String commands = (String)formField.getParameter("commands");
		if(commands==null || commands.isEmpty()) {
			String commandSet = (String)formField.getParameter("commandSet");
			List htmlEditorCommandSets = (List)Environment.getService("htmlEditorCommandSets");
			HtmlEditorCommandSet htmlEditorCommandSet = (HtmlEditorCommandSet)ListUtils.findObjectByProperty(htmlEditorCommandSets, "name", (commandSet==null ? ("false".equals(request.getAttribute("externalAction")) ? "standard" : "outer") : commandSet));
			plugins = htmlEditorCommandSet.getPlugins();
			commands = htmlEditorCommandSet.getCommands();
		}
		boolean fullPage = "true".equals(formField.getParameter("fullPage")); //是否完整HTML页面
		boolean autoIndentation = "true".equals(formField.getParameter("autoIndentation")); //是否自动缩进
		boolean centerImage = !"false".equals(formField.getParameter("centerImage")); //图片是否自动居中,默认居中
		HTMLScriptElement scriptElement = (HTMLScriptElement)fieldElement.getOwnerDocument().createElement("script");
		//HtmlEditor = function(fieldName, height, fullPage, autoIndentation, centerImage, readonly, plugins, commands, attachmentSelectorURL, parentElement)
		htmlParser.setTextContent(scriptElement, "new HtmlEditor('" + inputId + "', '" + height + "', " + fullPage + ", " + autoIndentation + ", " + centerImage + ", " + "true".equals(fieldElement.getAttribute("readonly")) + ", '" + plugins + "', '" + StringUtils.trim(commands) + "', '" + attachmentSelector + "');");
		div.appendChild(scriptElement);
	}
	
	/**
	 * 输出隐藏字段
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @throws ServiceException
	 */
	protected void writeHiddenField(HTMLElement fieldElement, Field formField, String fieldValue) throws ServiceException {
		fieldElement.setId(formField.getName());
		htmlParser.setTextContent(fieldElement, fieldValue);
	}
	
	/**
	 * 输出只读字段
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @throws ServiceException
	 */
	protected void writeReadonlyField(HTMLElement fieldElement, Field formField, String fieldValue) throws ServiceException {
		fieldElement.setId(formField.getName());
		htmlParser.setTextContent(fieldElement, fieldValue);
	}
	
	/**
	 * 输出附件编辑器
	 * @param fieldElement
	 * @param formField
	 * @param bean
	 * @param recordId
	 * @param componentRecordId
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeAttachmentEditor(HTMLElement fieldElement, Field formField, Object bean, long recordId, long componentRecordId, HttpServletRequest request) throws ServiceException {
		//attachmentType 附件类型,默认为attachment
		//attachmentEditorAction 附件编辑action,默认为attachmentEditor
		//attachmentEditorURL 附件编辑的URL,默认[attachmentEditorAction].shtml?[queryString]
		String attachmentEditorURL = (String)formField.getParameter("attachmentEditor");
		if(attachmentEditorURL!=null) {
			attachmentEditorURL = StringUtils.fillParameters(attachmentEditorURL, true, false, false, "utf-8", bean, request, null);
		}
		else {
			attachmentEditorURL = "attachmentEditor.shtml";
		}
		if(attachmentEditorURL.startsWith("/")) {
			attachmentEditorURL = Environment.getContextPath() + attachmentEditorURL;
		}
		else {
			String url = RequestUtils.getRequestURL(request, false);
			attachmentEditorURL = url.substring(0, url.lastIndexOf('/') + 1) + attachmentEditorURL;
		}
		String queryString = request.getParameter("queryString");
		if(queryString==null) { //提交的信息里没有queryString
			queryString = request.getQueryString();
		}
		if(queryString!=null && !queryString.equals("")) {
			attachmentEditorURL += (attachmentEditorURL.lastIndexOf('?')==-1 ? "?" : "&") + StringUtils.removeQueryParameters(queryString, "displayMode");
		}
		if(attachmentEditorURL.indexOf("id=")==-1) {
			attachmentEditorURL += (attachmentEditorURL.lastIndexOf('?')==-1 ? "?" : "&") + "id=" + recordId;
		}
		int index = formField.getName().lastIndexOf(".");
		if(index!=-1) {
			String componentId = formField.getName().substring(0, index + 1) + "id";
			if(attachmentEditorURL.indexOf(componentId)==-1) {
				if(componentRecordId>=0) {
					attachmentEditorURL += "&" + componentId + "=" + componentRecordId;
				}
			}
		}
		String attachmentType = (String)formField.getParameter("type");
		if(attachmentType==null) {
			attachmentType = formField.getName();
			if(index!=-1) {
				attachmentType = attachmentType.substring(index + 1);
			}
		}
		else {
			attachmentType = StringUtils.fillParameters(attachmentType, false, false, false, "utf-8", bean, request, null);
		}
		try {
			attachmentEditorURL += "&attachmentSelector.field=" + formField.getName() + "&attachmentSelector.type=" + URLEncoder.encode(attachmentType, "utf-8");
		}
		catch (UnsupportedEncodingException e) {
			
		}
		if("true".equals(formField.getParameter("simpleMode"))) {
			attachmentEditorURL += "&attachmentSelector.simpleMode=true";
		}
		//创建IFRAME
		HTMLIFrameElement iframe = (HTMLIFrameElement)fieldElement.getOwnerDocument().createElement("iframe");
		iframe.setId("attachmentFrame_" + attachmentType);
		iframe.setSrc(attachmentEditorURL);
		iframe.setFrameBorder("0");
		iframe.setClassName(getStyleClass(fieldElement, formField));
		iframe.setHeight("18px");
		String style = fieldElement.getAttribute("style");
		style = (style==null ? "" : style + ";") + "background-color:transparent !important; padding: 0px !important; border-style:none !important; display:block !important; border-style: none !important;";
		iframe.setAttribute("style", style);
		fieldElement.getParentNode().replaceChild(iframe, fieldElement);
	}
	
	/**
	 * 生成输入框
	 * @param fieldElement
	 * @param formField
	 * @param fieldName
	 * @param inputType
	 * @param value
	 * @param maxLength
	 * @param readonly
	 * @param request
	 * @return
	 */
	private String renderInputElement(HTMLElement fieldElement, Field formField, String fieldName, String inputType, String value, int maxLength, boolean readonly, HttpServletRequest request) throws ServiceException {
		if("password".equals(formField.getInputMode())) { //密码
			fieldElement.setAttribute("type", "password");
			fieldElement.setAttribute("autocomplete", "off"); //禁止自动完成
		}
		else {
			fieldElement.setAttribute("dataType", formField.getType()==null ? "string" : formField.getType().replaceAll("\\[\\]", ""));
			fieldElement.setAttribute("type", inputType);
		}
		if(maxLength>0) {
			fieldElement.setAttribute("maxlength", "" + maxLength);
		}
		else {
			fieldElement.removeAttribute("maxlength");
		}
		fieldElement.setAttribute("name", fieldName);
		if(readonly) {
			fieldElement.setAttribute("readonly", "true");
		}
		fieldElement.setAttribute("value", value);
		return htmlParser.getElementHTML(fieldElement, "utf-8").replaceAll("'", "\\\\'").replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
	}
	
	/**
	 * 添加事件处理
	 * @param element
	 * @param formField
	 * @param request
	 */
	private void appendEventAttributes(HTMLElement element, Field formField, HttpServletRequest request) {
		for(Iterator iterator = formField.getParameters()==null ? null : formField.getParameters().keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String parameterName = (String)iterator.next();
			if(parameterName.startsWith("on")) {
				element.setAttribute(parameterName, StringUtils.fillParameters((String)formField.getParameter(parameterName), false, true, false, "utf-8", null, request, null).replaceAll("\"", "\\\\\""));
			}
		}
	}
	
	/**
	 * 获取样式表
	 * @param fieldElement
	 * @param styleClass
	 * @return
	 */
	private String getStyle(HTMLElement fieldElement, String styleClass) {
		String style = fieldElement.getAttribute("style");
		if(styleClass!=null && !styleClass.isEmpty() && !"required".equals(styleClass)) {
			return style;
		}
		if(style==null || style.isEmpty()) {
			return "background-color: #ffffff; border: #a0a0a0 1px solid; padding: 2px 0px 0px 2px;";
		}
		if(style.toLowerCase().indexOf("background")==-1) {
			style += "; background-color: #ffffff";
		}
		if(style.toLowerCase().indexOf("border")==-1) {
			style += "; border: #a0a0a0 1px solid";
		}
		if(style.toLowerCase().indexOf("padding")==-1) {
			style += "; padding: 2px 0px 0px 2px";
		}
		if(style.toLowerCase().indexOf("width:")==-1) {
			style += "; width:100%";
		}
		return style;
	}
	
	/**
	 * 获取字段样式
	 * @param fieldElement
	 * @param formField
	 * @return
	 */
	private String getStyleClass(HTMLElement fieldElement, Field formField) {
		String styleClass = fieldElement.getAttribute("class");
		String style = fieldElement.getAttribute("style");
		if((style==null || style.isEmpty()) && (styleClass==null || styleClass.isEmpty() || "required".equals(styleClass))) {
			styleClass = "field";
		}
		if(formField==null || !formField.isRequired()) {
			return styleClass;
		}
		if(styleClass==null || styleClass.equals("")) {
			return null;
		}
		else if(styleClass.indexOf("equired")!=-1) {
			return styleClass;
		}
		else if("field".equals(styleClass)) {
			return styleClass + " required"; 
		}
		else {
			return styleClass + " " + styleClass + "Required"; 
		}
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
}
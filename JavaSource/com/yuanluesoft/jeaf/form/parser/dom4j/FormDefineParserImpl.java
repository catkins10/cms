/*
 * Created on 2004-12-20
 *
 */
package com.yuanluesoft.jeaf.form.parser.dom4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.parser.BusinessDefineParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.form.parser.FormDefineParser;

/**
 * 
 * @author linchuan
 *
 */
public class FormDefineParserImpl extends XmlParser implements FormDefineParser {
	private BusinessDefineParser businessDefineParser; //业务逻辑定义解析器,用来解析Field

	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.parser.FormDefineParser#parse(java.lang.String)
	 */
	public List parse(final String applicationName, final String defineFileName) throws ParseException {
		return parse(applicationName, parseXmlFile(defineFileName));
	}
	
	/**
	 * 根据XML配置解析表单定义
	 * @param applicationName
	 * @param xmlDefineRoot
	 * @return
	 * @throws FormDefineParseException
	 */
	private List parse(String applicationName, Element xmlDefineRoot) throws ParseException {
		List forms = new ArrayList();
		for(Iterator iterator = xmlDefineRoot.elementIterator(); iterator.hasNext();) {
			forms.add(parseForm(applicationName, (Element)iterator.next()));
		}
		return forms;
	}
	
	/**
	 * 解析表单
	 * @param applicationName
	 * @param xmlForm
	 * @return
	 * @throws FormDefineParseException
	 */
	private Form parseForm(String applicationName, Element xmlForm) throws ParseException {
		Form form = new Form();
		//设置应用名称
		form.setApplicationName(applicationName);
		//类名称
		form.setClassName(xmlForm.attributeValue("class"));
		//表单名称
		String name = xmlForm.attributeValue("name");
		if(name==null || name.equals("")) {
			name = form.getClassName();
			name = name.substring(name.lastIndexOf('.') + 1);
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			if(name.endsWith("Form")) {
			    name = name.substring(0, name.length() - 4);
			}
		}
		form.setName(name); //名称
		form.setTitle(xmlForm.attributeValue("title")); //标题
		form.setRecordClassName(xmlForm.attributeValue("pojo")); //pojo类名称
		form.setMethod(xmlForm.attributeValue("method")); //表单方法,默认为post
		if(form.getMethod()==null || form.getMethod().isEmpty()) {
			form.setMethod("post");
		}
		form.setAction(xmlForm.attributeValue("action")); //表单操作
		form.setJs(xmlForm.attributeValue("js")); //需要引入的脚本,多个时用逗号分隔
		
		//解析拓展属性
		Element xmlElement = xmlForm.element("extendedParameters");
		if(xmlElement!=null) {
			Map extendedParameters = new HashMap();
			for(Iterator iterator = xmlElement.elementIterator("parameter"); iterator.hasNext();) {
				Element xmlParameter = (Element)iterator.next();
				extendedParameters.put(xmlParameter.attributeValue("name"), xmlParameter.getText());
			}
			form.setExtendedParameters(extendedParameters.isEmpty() ? null : extendedParameters); //参数列表,为字段类型、输入方式配置所需要的参数
		}
		
		//设置操作列表
		xmlElement = xmlForm.element("actions");
		if(xmlElement!=null) {
			for(Iterator iterator = xmlElement.elementIterator(); iterator.hasNext();) {
				Element xmlAction = (Element)iterator.next();
				form.addAction(new FormAction(xmlAction.attributeValue("title"), xmlAction.attributeValue("type"), xmlAction.attributeValue("image"), xmlAction.attributeValue("hide"), xmlAction.attributeValue("execute"), "true".equals(xmlAction.attributeValue("default"))));
			}
		}
		//解析字段列表
		xmlElement = xmlForm.element("formFields");
		if(xmlElement!=null) {
			form.setFields(new ArrayList());
			for(Iterator iterator = xmlElement.elementIterator(); iterator.hasNext();) {
				Element xmlField = (Element)iterator.next();
				Field field = businessDefineParser.parseFeild(xmlField);
				if(field!=null) {
					form.getFields().add(field);
				}
			}
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.parser.FormDefineParser#saveFormDefine(java.util.List, java.lang.String)
	 */
	public void saveFormDefine(List forms, String defineFileName) throws ParseException {
		Document xmlDocument = DocumentHelper.createDocument();
		Element xmlForms = xmlDocument.addElement("forms");
		for(Iterator iterator = forms==null ? null : forms.iterator(); iterator!=null && iterator.hasNext();) {
			Form form = (Form)iterator.next();
			generateFormXML(form, xmlForms.addElement("form"));
		}
		//保存XML文件
		saveXmlFile(xmlDocument, defineFileName);
	}
	
	/**
	 * 生成表单XML
	 * @param form
	 * @param xmlForm
	 * @throws ParseException
	 */
	private void generateFormXML(Form form, Element xmlForm) throws ParseException {
		xmlForm.addAttribute("class", form.getClassName()); //类名称
		xmlForm.addAttribute("name", form.getName()); //表单名称
		xmlForm.addAttribute("title", form.getTitle()); //标题
		xmlForm.addAttribute("pojo", form.getRecordClassName()); //pojo类名称
		xmlForm.addAttribute("method", form.getMethod()); //表单方法,默认为post
		xmlForm.addAttribute("action", form.getAction()); //表单操作
		xmlForm.addAttribute("js", form.getJs()); //需要引入的脚本,多个时用逗号分隔
		
		//拓展属性
		if(form.getExtendedParameters()!=null && !form.getExtendedParameters().isEmpty()) {
			Element xmlExtendParameters = xmlForm.addElement("extendedParameters");
			for(Iterator iterator = form.getExtendedParameters().keySet().iterator(); iterator.hasNext();) {
				String name = (String)iterator.next();
				Element xmlParameter = xmlExtendParameters.addElement("parameter");
				xmlParameter.addAttribute("name", name);
				xmlParameter.setText((String)form.getExtendedParameters().get(name));
			}
		}
		
		//设置操作列表
		if(form.getActions()!=null && !form.getActions().isEmpty()) {
			Element xmlActions = xmlForm.addElement("actions");
			for(Iterator iterator = form.getActions().iterator(); iterator.hasNext();) {
				FormAction formAction = (FormAction)iterator.next();
				Element xmlAction = xmlActions.addElement("action");
				xmlAction.addAttribute("title", formAction.getTitle()); //标题
				xmlAction.addAttribute("type", formAction.getType()); //类型
				if(formAction.isDefault()) {
					xmlAction.addAttribute("default", "true"); //是否默认按钮
				}
				xmlAction.addAttribute("hide", formAction.getHideCondition()); //隐藏条件
				xmlAction.addAttribute("execute", formAction.getExecute()); //执行的操作
			}
		}

		//字段列表
		if(form.getFields()!=null && !form.getFields().isEmpty()) {
			Element xmlFields = xmlForm.addElement("formFields");
			for(Iterator iterator = form.getFields().iterator(); iterator.hasNext();) {
				Field field = (Field)iterator.next();
				businessDefineParser.generateFeildXML(field, xmlFields);
			}
		}
	}

	/**
	 * @return the businessDefineParser
	 */
	public BusinessDefineParser getBusinessDefineParser() {
		return businessDefineParser;
	}

	/**
	 * @param businessDefineParser the businessDefineParser to set
	 */
	public void setBusinessDefineParser(BusinessDefineParser businessDefineParser) {
		this.businessDefineParser = businessDefineParser;
	}
}
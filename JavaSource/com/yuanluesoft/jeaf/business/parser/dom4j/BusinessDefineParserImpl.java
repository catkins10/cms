package com.yuanluesoft.jeaf.business.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.parser.BusinessDefineParser;
import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 
 * @author linchuan
 *
 */
public class BusinessDefineParserImpl extends XmlParser implements BusinessDefineParser {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.parser.BusinessDefineParser#parse(java.lang.String, java.lang.String)
	 */
	public List parse(String applicationName, String defineFileName) throws ParseException {
		List businessObjects = new ArrayList();
		Element xmlDefine;
		try {
			xmlDefine = parseXmlFile(defineFileName);
		}
		catch(Exception e) {
			return null;
		}
		for(Iterator iterator = xmlDefine.elementIterator(); iterator.hasNext();) {
			Element xmlObject = (Element)iterator.next();
			businessObjects.add(parseBusinessObject(applicationName, xmlObject));
		}
		return businessObjects;
	}
	
	/**
	 * 解析业务对象
	 * @param applicationName
	 * @param xmlObject
	 * @return
	 * @throws ParseException
	 */
	private BusinessObject parseBusinessObject(String applicationName, Element xmlObject) throws ParseException {
		BusinessObject businessObject = new BusinessObject();
		businessObject.setApplicationName(applicationName); //应用名称
		businessObject.setTitle(xmlObject.attributeValue("title")); //标题
		businessObject.setClassName(xmlObject.attributeValue("class")); //类名称,pojo类名称
		businessObject.setBusinessServiceName(xmlObject.attributeValue("businessServiceName")); //业务逻辑服务名称,默认"businessService"
		businessObject.setFields(new ArrayList()); //字段列表
		for(Iterator iterator = xmlObject.elementIterator("field"); iterator.hasNext();) {
			Element xmlField = (Element)iterator.next();
			Field field = parseFeild(xmlField);
			if(field!=null) {
				businessObject.getFields().add(field);
			}
			
		}
		//扩展参数列表,用来增加应用程序需要的其他参数
		for(Iterator iterator = xmlObject.elementIterator("parameter"); iterator.hasNext();) {
			Element xmlParameter = (Element)iterator.next();
			businessObject.setExtendParameter(xmlParameter.attributeValue("name"), xmlParameter.getText());
		}
		return businessObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.parser.BusinessDefineParser#parseFeild(org.dom4j.Element)
	 */
	public Field parseFeild(Element xmlField) throws ParseException {
		Field field = new Field();
		field.setName(xmlField.attributeValue("name")); //名称
		if(field.getName()==null || field.getName().isEmpty()) {
			return null;
		}
		field.setTitle(xmlField.attributeValue("title")); //标题
		if(field.getTitle()==null || field.getTitle().isEmpty()) {
			return null;
		}
		field.setName(field.getName().trim());
		field.setTitle(field.getTitle().trim());
		field.setRequired("true".equals(xmlField.attributeValue("required"))); //是否必填项
		field.setLength(xmlField.attributeValue("length")); //字段长度
		field.setType(xmlField.attributeValue("type")); //字段类型
		if(field.getType()!=null) {
			field.setType(field.getType().trim());
		}
		else {
			field.setType("string");
		}
		field.setPersistence(!"false".equals(xmlField.attributeValue("persistence"))); //是否数据库字段
		field.setInputMode(xmlField.attributeValue("inputMode")); //字段输入方式
		if(field.getInputMode()!=null) {
			field.setInputMode(field.getInputMode().trim());
		}
		else if("date".equals(field.getType())) {
			field.setInputMode("date");
		}
		else if("timestamp".equals(field.getType())) {
			field.setInputMode("datetime");
		}
		else if("html".equals(field.getType())) {
			field.setInputMode("htmleditor");
		}
		else if("attachment".equals(field.getType()) || "image".equals(field.getType()) || "video".equals(field.getType())) {
			field.setInputMode("attachment");
		}
		else {
			field.setInputMode("text");
		}
		//参数列表
		for(Iterator iterator = xmlField.elementIterator("parameter"); iterator.hasNext();) {
			Element xmlParameter = (Element)iterator.next();
			field.setParameter(xmlParameter.attributeValue("name"), xmlParameter.getText());
		}
		return field;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.parser.BusinessDefineParser#saveBusinessDefine(java.util.List, java.lang.String)
	 */
	public void saveBusinessDefine(List businessObjects, String defineFileName) throws ParseException {
		Document xmlDocument = DocumentHelper.createDocument();
		Element xmlBusinessObjects = xmlDocument.addElement("businessObjects");
		for(Iterator iterator = businessObjects==null ? null : businessObjects.iterator(); iterator!=null && iterator.hasNext();) {
			BusinessObject businessObject = (BusinessObject)iterator.next();
			generateBusinessObjectXML(businessObject, xmlBusinessObjects);
		}
		//保存XML文件
		saveXmlFile(xmlDocument, defineFileName);
	}
	
	/**
	 * 生成业务对象XML
	 * @param businessObject
	 * @param parentElement
	 * @return
	 * @throws ParseException
	 */
	public Element generateBusinessObjectXML(BusinessObject businessObject, Element parentElement) throws ParseException {
		Element xmlBusinessObject = parentElement.addElement("businessObject");
		xmlBusinessObject.addAttribute("title", businessObject.getTitle()); //标题
		xmlBusinessObject.addAttribute("class", businessObject.getClassName()); //类名称,pojo类名称
		xmlBusinessObject.addAttribute("businessServiceName", businessObject.getBusinessServiceName()); //业务逻辑服务名称,默认"businessService"
		for(Iterator iterator = businessObject.getFields()==null ? null : businessObject.getFields().iterator(); iterator!=null && iterator.hasNext();) { //字段列表
			Field field = (Field)iterator.next();
			generateFeildXML(field, xmlBusinessObject);
		}
		return xmlBusinessObject;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.parser.BusinessDefineParser#generateFeildXML(com.yuanluesoft.jeaf.business.model.Field, org.dom4j.Element)
	 */
	public Element generateFeildXML(Field field, Element parentElement) throws ParseException {
		Element xmlField = parentElement.addElement("field");
		xmlField.addAttribute("name", field.getName()); //名称
		xmlField.addAttribute("title", field.getTitle()); //标题
		xmlField.addAttribute("length", field.getLength()); //字段长度,字符类型时是整数,按汉字计算,数字类型时可以是"6,3"
		if(field.isRequired()) {
			xmlField.addAttribute("required", "true"); //是否必填项
		}
		if(!field.isPersistence()) {
			xmlField.addAttribute("persistence", "false"); //是否持久字段,也就是数据库字段,默认true,参数 referenceFields: 引用的字段名称列表
		}
		xmlField.addAttribute("type", field.getType()); //类型
		xmlField.addAttribute("inputMode", field.getInputMode()); //输入方式
		//参数列表
		if(field.getParameters()!=null && !field.getParameters().isEmpty()) {
			for(Iterator iterator = field.getParameters().keySet().iterator(); iterator.hasNext();) {
				String name = (String)iterator.next();
				if("singleByteCharacters".equals(name) && "false".equals(field.getParameters().get(name))) {
					continue;
				}
				Element xmlParameter = xmlField.addElement("parameter");
				xmlParameter.addAttribute("name", name);
				xmlParameter.setText((String)field.getParameters().get(name));
			}
		}
		return xmlField;
	}
}
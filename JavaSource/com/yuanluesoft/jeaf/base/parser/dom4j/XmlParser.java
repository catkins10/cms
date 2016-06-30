/*
 * Created on 2005-1-4
 *
 */
package com.yuanluesoft.jeaf.base.parser.dom4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class XmlParser {
	
	/**
	 * 解析xml文件
	 * @param fileName
	 * @return
	 * @throws ParseException
	 */
	public Element parseXmlFile(String fileName) throws ParseException  {
		SAXReader reader = new SAXReader();
		try {
			return reader.read(new File(fileName)).getRootElement();
		}
		catch (Exception e) {
			throw new ParseException(e.getMessage());
		}
	}
	
	/**
	 * 解析xml字符串
	 * @param content
	 * @return
	 * @throws ParseException
	 */
	public Element parseXmlString(String content) throws ParseException {
		SAXReader reader = new SAXReader();
		//加载配置
		StringReader strReader = new StringReader(content.startsWith("<?xml") ? content : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + content);
		try {
			return reader.read(strReader).getRootElement();
		} 
		catch (DocumentException e) {
			throw(new ParseException("SAXReader Exception"));
		}
		finally {
			strReader.close();
		}
	}
	
	/**
	 * 解析字节类型xml配置
	 * @param bytes
	 * @return
	 * @throws ParseException
	 */
	public Element parseXmlBytes(byte[] bytes) throws ParseException {
		SAXReader reader = new SAXReader();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			return reader.read(in).getRootElement();
		}
		catch (DocumentException e) {
			throw(new ParseException("SAXReader Exception"));
		}
		finally {
			try {
				in.close();
			} 
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 查找扩展属性元素
	 * @param xmlElement
	 * @param name
	 * @return
	 */
	public Element findExtendedAttributeByName(Element xmlElement, String name) {
		Element xmlExtendedAttributes = xmlElement.element("ExtendedAttributes");
		if(xmlExtendedAttributes==null) {
			return null;
		}
		for(Iterator iterator = xmlExtendedAttributes.elementIterator(); iterator.hasNext();) {
			Element xmlExtended = (Element)iterator.next();
			if(name.equals(xmlExtended.attributeValue("Name"))) {
				return xmlExtended;
			}
		}
		return null;
	}
	
	/**
	 * 解析元素
	 * @param element
	 * @param xmlElement
	 * @return
	 */
	public com.yuanluesoft.jeaf.base.model.Element parseElement(com.yuanluesoft.jeaf.base.model.Element element, Element xmlElement) throws ParseException {
		element.setId(xmlElement.attributeValue("Id"));
		element.setName(xmlElement.attributeValue("Name"));
		return element;
	}
	
	/**
	 * 解析扩展属性
	 * @param xmlExtend
	 * @return
	 * @throws ApplicationConfigureParseException
	 */
	public ElementExtend parseElementExtend(Element xmlExtend) throws ParseException {
		return parseElementExtend(xmlExtend,new  ElementExtend());
	}
	
	/**
	 * 解析扩展属性
	 * @param xmlExtend
	 * @param extend
	 * @return
	 * @throws ParseException
	 */
	public ElementExtend parseElementExtend(Element xmlExtend, ElementExtend extend) throws ParseException {
		extend.setId(xmlExtend.attributeValue("Id"));
		String attribute = xmlExtend.attributeValue("StartX");
		if(attribute!=null) {
			extend.setStartX(new Integer(attribute).intValue());
		}
		attribute = xmlExtend.attributeValue("StartY");
		if(attribute!=null) {
			extend.setStartY(new Integer(attribute).intValue());
		}
		attribute = xmlExtend.attributeValue("EndX");
		if(attribute!=null) {
			extend.setEndX(new Integer(attribute).intValue());
		}
		attribute = xmlExtend.attributeValue("EndY");
		if(attribute!=null) {
			extend.setEndY(new Integer(attribute).intValue());
		}
		extend.setReadOnly("true".equals(xmlExtend.attributeValue("ReadOnly")));
		extend.setConfigOnly("true".equals(xmlExtend.attributeValue("ConfigOnly")));
		extend.setMoveDisable("true".equals(xmlExtend.attributeValue("MoveDisable")));
		extend.setDeleteDisable("true".equals(xmlExtend.attributeValue("DeleteDisable")));
		extend.setDisplayOnly("true".equals(xmlExtend.attributeValue("DisplayOnly")));
		return extend;
	}
	
	/**
	 * 生成元素xml
	 * @param xmlElement
	 * @param element
	 * @throws ParseException
	 */
	public void generateElementXML(Element xmlElement, com.yuanluesoft.jeaf.base.model.Element element) throws ParseException {
		if(element.getId()!=null) {
			xmlElement.addAttribute("Id", element.getId());
		}
		if(element.getName()!=null) {
			xmlElement.addAttribute("Name", element.getName());
		}
	}
	
	/**
	 * 生成坐标对象XML
	 * @param xmlParent
	 * @param coordinate
	 * @throws ParseException
	 */
	public Element generateExtendXML(Element xmlParent, final ElementExtend extend) throws ParseException {
		Element xmlExtend = xmlParent.addElement("Extend");
		generateElementXML(xmlExtend, extend);
		if(extend.getStartX()!=-1) {
			xmlExtend.addAttribute("StartX", "" + extend.getStartX());
			xmlExtend.addAttribute("StartY", "" + extend.getStartY());
			xmlExtend.addAttribute("EndX", "" + extend.getEndX());
			xmlExtend.addAttribute("EndY", "" + extend.getEndY());
		}
		if(extend.isReadOnly()) {
			xmlExtend.addAttribute("ReadOnly", "true");
		}
		if(extend.isConfigOnly()) {
			xmlExtend.addAttribute("ConfigOnly", "true");
		}
		if(extend.isMoveDisable()) {
			xmlExtend.addAttribute("MoveDisable", "true");
		}
		if(extend.isDeleteDisable()) {
			xmlExtend.addAttribute("DeleteDisable", "true");
		}
		if(extend.isDisplayOnly()) {
			xmlExtend.addAttribute("DisplayOnly", "true");
		}
		return xmlExtend;
	}
	
	/**
	 * 输出XML文本
	 * @param xmlDoc
	 * @return
	 * @throws ParseException
	 */
	public String asXML(Document xmlDoc) throws ParseException {
		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = null;
		try {
			OutputFormat outputFormat =  OutputFormat.createPrettyPrint();
			outputFormat.setIndent("\t");
			writer = new XMLWriter(stringWriter, outputFormat); 
			writer.write(xmlDoc);
			return stringWriter.toString().trim();
		}
		catch(Exception e) {
			throw(new ParseException(e));
		}
		finally {
			try {
				writer.close();
			}
			catch (Exception e) {
			
			}
			try {
				stringWriter.close();
			}
			catch (Exception e) {
			
			}
		}
	}
	
	/**
	 * 输出XML文本
	 * @param xmlElement
	 * @return
	 * @throws ParseException
	 */
	public String asXML(Element xmlElement) throws ParseException {
		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = null;
		try {
			OutputFormat outputFormat =  OutputFormat.createPrettyPrint();
			outputFormat.setIndent("\t");
			writer = new XMLWriter(stringWriter, outputFormat); 
			writer.write(xmlElement);
			return stringWriter.toString().trim();
		}
		catch(Exception e) {
			throw(new ParseException(e));
		}
		finally {
			try {
				writer.close();
			}
			catch (Exception e) {
			
			}
			try {
				stringWriter.close();
			}
			catch (Exception e) {
			
			}
		}
	}
	
	/**
	 * 保存XML
	 * @param xmlDoc
	 * @param filePath
	 * @throws ParseException
	 */
	public void saveXmlFile(Document xmlDoc, String filePath) throws ParseException {
		FileOutputStream out = null;
		XMLWriter writer;
		try {
			out = new FileOutputStream(filePath);
			OutputFormat outputFormat =  OutputFormat.createPrettyPrint();
			outputFormat.setIndent("\t");
			writer = new XMLWriter(out, outputFormat); 
			writer.write(xmlDoc);
		}
		catch(Exception e) {
			throw(new ParseException(e));
		}
		try {
			writer.close();
		}
		catch (Exception e) {
		
		}
		try {
			out.close();
		}
		catch (Exception e) {
		
		}
	}
	
	/**
	 * 获取子列表
	 * @param sourceList
	 * @param xmlList
	 * @return
	 */
	public List subList(List sourceList, Element xmlList, String propertyName) {
		if(xmlList==null) {
			return null;
		}
		List list = new ArrayList();
		for(Iterator iterator=xmlList.elementIterator(); iterator.hasNext();) {
			Element element = (Element)iterator.next();
			Object obj = ListUtils.findObjectByProperty(sourceList, propertyName, element.attributeValue(propertyName));
			if(obj!=null) {
				list.add(obj);
			}
		}
		return list.isEmpty() ? null:list;
	}
}
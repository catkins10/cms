/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.parser.dom4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.graphicseditor.model.extend.BrokenLine;
import com.yuanluesoft.jeaf.graphicseditor.model.extend.ConfigureExtend;
import com.yuanluesoft.jeaf.graphicseditor.model.extend.ElementExtend;
import com.yuanluesoft.jeaf.graphicseditor.model.extend.Line;

/**
 * 
 * @author linchuan
 *
 */
public class ConfigureExtendParser extends XmlParser {
	private LineParser lineParser;
	private ModifyHistoryParser modifyHistoryParser;
	
	/**
	 * 解析扩展配置
	 * @param xmlExtendRoot
	 * @return
	 * @throws BaseParseException
	 */
	public ConfigureExtend parseConfigureExtend(ConfigureExtend configureExtend, Element xmlExtendRoot) throws ParseException {
		configureExtend.setCreator(xmlExtendRoot.attributeValue("Creator"));
		String attribute = xmlExtendRoot.attributeValue("NextElementId");
		if(attribute!=null) {
			configureExtend.setNextElementId(new Integer(attribute).intValue());
		}
		attribute = xmlExtendRoot.attributeValue("CreateDate");
		if(attribute!=null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				configureExtend.setCreateDate(formatter.parse(attribute));
			}
			catch (java.text.ParseException e) {
				
			}
		}
		ArrayList list = new ArrayList();
		for(Iterator iterator=xmlExtendRoot.elementIterator(); iterator.hasNext();) {
			Element xmlElement = (Element)iterator.next();
			String tagName = xmlElement.getName();
			if(tagName.equals("ModifyHistory")) {
				//修改历史
				configureExtend.setModifyHistory(getModifyHistoryParser().parseModifyHistory(xmlExtendRoot.element("ModifyHistory")));
			}
			else if(tagName.equals("Line")) { //连接线
				list.add(getLineParser().parseLine(xmlElement));
			}
			else if(tagName.equals("BrokenLine")) { //转折线
				list.add(getLineParser().parseBrokenLine(xmlElement));
			}
			else if(tagName.equals("Extend")) { //坐标
				list.add(parseElementExtend(xmlElement));
			}
		}
		configureExtend.setElements(list);
		//校验NextElementId
		for(Iterator iterator = list.iterator(); iterator.hasNext();) {
			ElementExtend extend = (ElementExtend)iterator.next();
			String id = extend.getId();
			int num = 0;
			try {
				num = Integer.parseInt(id.substring(id.lastIndexOf('-') + 1));
			}
			catch(Exception e) {
				
			}
			if(configureExtend.getNextElementId() <= num) {
				configureExtend.setNextElementId(num + 1);
			}
		}
		return configureExtend;
	}

	/**
	 * 生成扩展配置XML文档
	 * @param configureExtend
	 * @return
	 * @throws ParseException
	 */
	public Document generateConfigureExtendXMLDocument(ConfigureExtend configureExtend) throws ParseException {
		Document xmlDoc = DocumentHelper.createDocument();
		//根元素
		Element xmlExtendRoot = xmlDoc.addElement("Extend");
		if(configureExtend.getCreator()==null) {
			return xmlDoc;
		}
		xmlExtendRoot.addAttribute("Creator", configureExtend.getCreator());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		xmlExtendRoot.addAttribute("CreateDate", formatter.format(configureExtend.getCreateDate()));
		xmlExtendRoot.addAttribute("NextElementId", "" + configureExtend.getNextElementId());
		//修改历史
		getModifyHistoryParser().generateModifyHistory(xmlExtendRoot, configureExtend.getModifyHistory());
		//元素列表
		if(configureExtend.getElements()!=null) {
			for(Iterator iterator=configureExtend.getElements().iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				if(object instanceof BrokenLine) {
					getLineParser().generateBrokenLineXML(xmlExtendRoot, (BrokenLine)object);
				}
				else if(object instanceof Line) {
					getLineParser().generateLineXML(xmlExtendRoot, (Line)object);
				}
				else if(object instanceof ElementExtend) {
					generateExtendXML(xmlExtendRoot, (ElementExtend)object);
				}
			}
		}
		return xmlDoc;
	}
	
	
	/**
	 * @return Returns the lineParser.
	 */
	public LineParser getLineParser() {
		if(lineParser==null) {
			lineParser = new LineParser();
		}
		return lineParser;
	}
	/**
	 * @param lineParser The lineParser to set.
	 */
	public void setLineParser(LineParser lineParser) {
		this.lineParser = lineParser;
	}
	/**
	 * @return Returns the modifyHistoryParser.
	 */
	public ModifyHistoryParser getModifyHistoryParser() {
		if(modifyHistoryParser==null) {
			modifyHistoryParser = new ModifyHistoryParser();
		}
		return modifyHistoryParser;
	}
	/**
	 * @param modifyHistoryParser The modifyHistoryParser to set.
	 */
	public void setModifyHistoryParser(ModifyHistoryParser modifyHistoryParser) {
		this.modifyHistoryParser = modifyHistoryParser;
	}
}

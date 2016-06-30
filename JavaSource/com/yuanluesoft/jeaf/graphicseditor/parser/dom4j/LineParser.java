/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.graphicseditor.model.extend.BrokenLine;
import com.yuanluesoft.jeaf.graphicseditor.model.extend.Line;

/**
 * 
 * @author linchuan
 *
 */
public class LineParser extends XmlParser {
	/**
	 * 解析连接线
	 * @param xmlLine
	 * @return
	 * @throws ApplicationConfigureParseException
	 */
	public Line parseLine(Element xmlLine) throws ParseException {
		return parseLine(xmlLine, new Line());
	}
	/**
	 * 解析连接线
	 * @param xmlLine
	 * @param line
	 * @return
	 * @throws ParseException
	 */
	public Line parseLine(Element xmlLine, Line line) throws ParseException {
		parseElementExtend(xmlLine, line);
		String attribute = xmlLine.attributeValue("Enter");
		if(attribute!=null) {
			line.setEnterIndex(new Integer(attribute).intValue());
		}
		attribute = xmlLine.attributeValue("Exit");
		if(attribute!=null) {
			line.setExitIndex(new Integer(attribute).intValue());
		}
		return line;
	}
	/**
	 * 解析转折线
	 * @param xmlBrokenLine
	 * @return
	 * @throws ApplicationConfigureParseException
	 */
	public BrokenLine parseBrokenLine(Element xmlBrokenLine) throws ParseException {
		BrokenLine line = new BrokenLine(); 
		parseLine(xmlBrokenLine, line);
		//解析转折点
		ArrayList xPoints = new ArrayList();
		ArrayList yPoints = new ArrayList();
		for(Iterator iterator=xmlBrokenLine.elementIterator(); iterator.hasNext();) {
			Element xmlElement = (Element)iterator.next();
			xPoints.add(new Integer(xmlElement.attributeValue("X")));
			yPoints.add(new Integer(xmlElement.attributeValue("Y")));
		}
		if(xPoints.size()>0) {
			line.setTurningXPoints(xPoints);
			line.setTurningYPoints(yPoints);
		}
		return line;
	}
	/**
	 * 生成线XML
	 * @param xmlParent
	 * @param line
	 * @throws ParseException
	 */
	public Element generateLineXML(Element xmlParent, final Line line) throws ParseException {
		Element xmlLine = generateExtendXML(xmlParent, line);
		xmlLine.setName("Line");
		xmlLine.addAttribute("Enter", "" + line.getEnterIndex());
		xmlLine.addAttribute("Exit", "" + line.getExitIndex());
		return xmlLine;
	}
	/**
	 * 生成转折线XML
	 * @param xmlParent
	 * @param line
	 * @throws ParseException
	 */
	public Element generateBrokenLineXML(Element xmlParent, final BrokenLine line) throws ParseException {
		Element xmlLine = generateLineXML(xmlParent, line);
		xmlLine.setName("BrokenLine");
		//转折点
		for(int i=0; i<(line.getTurningXPoints()==null ? 0 : line.getTurningXPoints().size()); i++) {
			Element xmlTurningPoint = xmlLine.addElement("Turning");
			xmlTurningPoint.addAttribute("X", "" + line.getTurningXPoints().get(i));
			xmlTurningPoint.addAttribute("Y", "" + line.getTurningYPoints().get(i));
		}
		return xmlLine;
	}
}

/*
 * Created on 2005-10-23
 *
 */
package com.yuanluesoft.jeaf.view.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.view.calendarview.model.CalendarView;
import com.yuanluesoft.jeaf.view.calendarview.parser.dom4j.CalendarViewParser;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.parser.ViewDefineParser;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;
import com.yuanluesoft.jeaf.view.statisticview.parser.dom4j.StatisticViewParser;

/**
 * 
 * @author linchuan
 *
 */
public class ViewDefineParserImpl extends XmlParser implements ViewDefineParser {
	private ViewParser viewParser;
	private StatisticViewParser statisticViewParser;
	private CalendarViewParser calendarViewParser;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.parser.ViewDefineParser#parse(java.lang.String, java.lang.String)
	 */
	public List parse(String applicationName, String defineFileName) throws ParseException {
		return parse(applicationName, parseXmlFile(defineFileName));
	}
	
	/**
	 * 根据XML配置解析表单定义
	 * @param application
	 * @param xmlDefineRoot
	 * @return
	 * @throws FormDefineParseException
	 */
	private List parse(String applicationName, Element xmlDefineRoot) throws ParseException {
		List views = new ArrayList();
		for(Iterator iterator = xmlDefineRoot.elementIterator(); iterator.hasNext();) {
			Element xmlView = (Element)iterator.next();
			String type = xmlView.getName();
			if("statisticView".equals(type)) {
				views.add(statisticViewParser.parseView(applicationName, xmlView));
			}
			else if("calendarView".equals(type)) {
				views.add(calendarViewParser.parseView(applicationName, xmlView));
			}
			else {
				views.add(viewParser.parseView(applicationName, xmlView));
			}
		}
		return views;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.parser.ViewDefineParser#saveViewDefine(java.util.List, java.lang.String)
	 */
	public void saveViewDefine(List views, String defineFileName) throws ParseException {
		Document xmlDocument = DocumentHelper.createDocument();
		Element xmlViews = xmlDocument.addElement("views");
		for(Iterator iterator = views==null ? null : views.iterator(); iterator!=null && iterator.hasNext();) {
			View view = (View)iterator.next();
			if(view instanceof StatisticView) { //统计视图
				statisticViewParser.generateViewXML(view, xmlViews.addElement("statisticView"));
			}
			else if(view instanceof CalendarView) { //日历视图
				calendarViewParser.generateViewXML(view, xmlViews.addElement("calendarView"));
			}
			else { //普通视图
				viewParser.generateViewXML(view, xmlViews.addElement("view"));
			}
		}
		//保存XML文件
		saveXmlFile(xmlDocument, defineFileName);
	}

	/**
	 * @return Returns the statisticViewParser.
	 */
	public StatisticViewParser getStatisticViewParser() {
		return statisticViewParser;
	}
	/**
	 * @param statisticViewParser The statisticViewParser to set.
	 */
	public void setStatisticViewParser(StatisticViewParser statisticViewParser) {
		this.statisticViewParser = statisticViewParser;
	}
	/**
	 * @return Returns the viewParser.
	 */
	public ViewParser getViewParser() {
		return viewParser;
	}
	/**
	 * @param viewParser The viewParser to set.
	 */
	public void setViewParser(ViewParser viewParser) {
		this.viewParser = viewParser;
	}
	/**
	 * @return Returns the calendarViewParser.
	 */
	public CalendarViewParser getCalendarViewParser() {
		return calendarViewParser;
	}
	/**
	 * @param calendarViewParser The calendarViewParser to set.
	 */
	public void setCalendarViewParser(CalendarViewParser calendarViewParser) {
		this.calendarViewParser = calendarViewParser;
	}
}
/*
 * Created on 2006-3-24
 *
 */
package com.yuanluesoft.jeaf.view.calendarview.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.view.calendarview.model.CalendarView;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.parser.dom4j.ViewParser;

/**
 * 
 * @author linchuan
 *
 */
public class CalendarViewParser extends ViewParser {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.parser.dom4j.ViewParser#parseView(java.lang.String, org.dom4j.Element)
	 */
	public View parseView(String applicationName, Element xmlView) throws ParseException {
		CalendarView view = new CalendarView();
		parseView(applicationName, xmlView, view);
		view.setDefaultMode(xmlView.attributeValue("defaultMode"));
		view.setCalendarColumn(xmlView.element("data").element("calendarColumn").getText());
		Element xmlElement = xmlView.element("calendarAction");
		if(xmlElement!=null) {
		    view.setCalendarAction(xmlElement.attributeValue("execute")); //设置日历操作
		    view.setCalendarActionHideCondition(xmlElement.attributeValue("hide")); //设置日历操作隐藏条件
		}
		//视图列列表
		xmlElement = xmlView.element("columns");
		if(xmlElement!=null) {
			ArrayList columns = new ArrayList();
			for(Iterator iterator = xmlElement.elementIterator(); iterator.hasNext();) {
				columns.add(parseColumn(view, (Element)iterator.next()));
			}
			view.setColumns(columns);
		}
		return view;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.parser.dom4j.ViewParser#generateViewXML(com.yuanluesoft.jeaf.view.model.View, org.dom4j.Element)
	 */
	public void generateViewXML(View view, Element xmlView) throws ParseException {
		super.generateViewXML(view, xmlView);
		CalendarView calendarView = (CalendarView)view;
		xmlView.element("data").addElement("calendarColumn").setText(calendarView.getCalendarColumn());
		xmlView.addAttribute("defaultMode", calendarView.getDefaultMode()); //默认的日历模式,month/week/day
		if(calendarView.getCalendarAction()!=null) {
			Element xmlCalendarAction = xmlView.addElement("calendarAction");
			xmlCalendarAction.addAttribute("execute", calendarView.getCalendarAction()); //日历操作
			xmlCalendarAction.addAttribute("hide", calendarView.getCalendarActionHideCondition()); //日历操作隐藏条件
		}
	}
}
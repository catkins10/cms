/*
 * Created on 2006-3-24
 *
 */
package com.yuanluesoft.jeaf.view.statisticview.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.parser.dom4j.ViewParser;
import com.yuanluesoft.jeaf.view.statisticview.model.GroupField;
import com.yuanluesoft.jeaf.view.statisticview.model.Statistic;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticColumn;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;

/**
 * 
 * @author linchuan
 *
 */
public class StatisticViewParser extends ViewParser {

	/**
	 * 解析统计视图
	 * @param xmlView
	 * @param defaultOpenFeatures
	 * @return
	 * @throws ParseException
	 */
	public View parseView(String applicationName, Element xmlView) throws ParseException {
		StatisticView view = new StatisticView();
		super.parseView(applicationName, xmlView, view);
		view.setStatistics(parseStatisticList(view, xmlView.element("statistics")));
		//视图列列表
		Element xmlElement = xmlView.element("columns");
		if(xmlElement!=null) {
			ArrayList columns = new ArrayList();
			for(Iterator iterator = xmlElement.elementIterator(); iterator.hasNext();) {
				columns.add(parseStatisticColumn(view, (Element)iterator.next()));
			}
			view.setColumns(columns);
		}
		return view;
	}
	
	/**
	 * 解析统计列表
	 * @param xmlStatisticList
	 * @return
	 */
	protected List parseStatisticList(StatisticView view, Element xmlStatisticList) throws ParseException {
		if(xmlStatisticList==null) {
			return null;
		}
		view.setHideDetail("true".equals(xmlStatisticList.attributeValue("hideDetail"))); //是否隐藏明细
		view.setDetailTitle(xmlStatisticList.attributeValue("detailTitle")); //明细项目标题
		List list = new ArrayList();
		for(Iterator iterator = xmlStatisticList.elementIterator(); iterator.hasNext();) {
			list.add(parseStatistic((Element)iterator.next()));
		}
		return list;
	}
	
	/**
	 * 解析统计
	 * @param xmlStatistic
	 * @return
	 * @throws ParseException
	 */
	protected Statistic parseStatistic(Element xmlStatistic) throws ParseException {
		Statistic statistic = new Statistic();
		statistic.setTitle(xmlStatistic.attributeValue("title")); //标题
		statistic.setStatisticAll("true".equals(xmlStatistic.attributeValue("statisticAll"))); //总计
		//解析分组字段
		Element xmlGroupFields = xmlStatistic.element("groupFields");
		if(xmlGroupFields!=null) {
			List groupFields = new ArrayList();
			for(Iterator iterator = xmlGroupFields.elementIterator(); iterator.hasNext();) {
				Element xmlGroupField = (Element)iterator.next();
				GroupField groupField = new GroupField();
				groupField.setName(xmlGroupField.attributeValue("name")); //属性名
				groupField.setField(xmlGroupField.attributeValue("field")); //字段名
				groupFields.add(groupField);
			}
			statistic.setGroupFields(groupFields);
		}
		//解析统计列
		List statisticColumns = new ArrayList();
		for(Iterator iterator = xmlStatistic.element("statisticColumns").elementIterator(); iterator.hasNext();) {
			Element xmlStatisticColumn = (Element)iterator.next();
			StatisticColumn statisticColumn = new StatisticColumn();
			statisticColumn.setName(xmlStatisticColumn.attributeValue("name"));
			statisticColumn.setFunction(xmlStatisticColumn.attributeValue("function"));
			statisticColumn.setWhereExtend(xmlStatisticColumn.attributeValue("whereExtend"));
			statisticColumns.add(statisticColumn);
		}
		statistic.setStatisticColumns(statisticColumns);
		return statistic;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.parser.dom4j.ViewParser#parseColumn(com.yuanluesoft.erp.core.model.base.view.View, org.dom4j.Element)
	 */
	protected Column parseStatisticColumn(StatisticView view, Element xmlColumn) throws ParseException {
		com.yuanluesoft.jeaf.view.statisticview.model.Column column = new com.yuanluesoft.jeaf.view.statisticview.model.Column();
		parseColumn(view, column, xmlColumn);
		column.setGroupBy(xmlColumn.attributeValue("groupBy")); //分组
		column.setSourceField(xmlColumn.attributeValue("sourceField")); //数据源
		column.setEmptyWhenStatistic("true".equals(xmlColumn.attributeValue("emptyWhenStatistic"))); //统计时是否清空
		return column;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.parser.dom4j.ViewParser#generateViewXML(com.yuanluesoft.jeaf.view.model.View, org.dom4j.Element)
	 */
	public void generateViewXML(View view, Element xmlView) throws ParseException {
		super.generateViewXML(view, xmlView);
		StatisticView statisticView = (StatisticView)view;
		Element xmlStatistics = xmlView.addElement("statistics");
		if(statisticView.isHideDetail()) {
			xmlStatistics.addAttribute("hideDetail", "true"); //是否隐藏明细
		}
		for(Iterator iterator = statisticView.getStatistics()==null ? null : statisticView.getStatistics().iterator(); iterator!=null && iterator.hasNext();) {
			Statistic statistic = (Statistic)iterator.next();
			generateStatisticXML(statistic, xmlStatistics);
		}
	}
	
	/**
	 * 生成统计XML
	 * @param statistic
	 * @param parentElement
	 * @return
	 * @throws ParseException
	 */
	protected Element generateStatisticXML(Statistic statistic, Element parentElement) throws ParseException {
		Element xmlStatistic = parentElement.addElement("statistic");
		statistic.setTitle(xmlStatistic.attributeValue("title")); //标题
		if(statistic.isStatisticAll()) {
			xmlStatistic.addAttribute("statisticAll", "true"); //总计
		}
		if(statistic.getGroupFields()!=null && !statistic.getGroupFields().isEmpty()) { 
			//解析分组字段
			Element xmlGroupFields = xmlStatistic.addElement("groupFields");
			for(Iterator iterator = statistic.getGroupFields().iterator(); iterator.hasNext();) {
				GroupField groupField = (GroupField)iterator.next();
				Element xmlGroupField = xmlGroupFields.addElement("groupField");
				xmlGroupField.addAttribute("name", groupField.getName()); //属性名
				xmlGroupField.addAttribute("field", groupField.getField()); //字段名
			}
		}
		//解析统计列
		Element xmlStatisticColumns = xmlStatistic.addElement("statisticColumns");
		for(Iterator iterator = statistic.getStatisticColumns().iterator(); iterator.hasNext();) {
			StatisticColumn statisticColumn = (StatisticColumn)iterator.next();
			Element xmlStatisticColumn = xmlStatisticColumns.addElement("statisticColumn");
			xmlStatisticColumn.addAttribute("name", statisticColumn.getName());
			xmlStatisticColumn.addAttribute("function", statisticColumn.getFunction());
			xmlStatisticColumn.addAttribute("whereExtend", statisticColumn.getWhereExtend());
		}
		return xmlStatistic;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.parser.dom4j.ViewParser#generateViewColumnXML(com.yuanluesoft.jeaf.view.model.Column, org.dom4j.Element)
	 */
	protected Element generateViewColumnXML(Column column, Element parentElement) throws ParseException {
		Element xmlColumn = super.generateViewColumnXML(column, parentElement);
		com.yuanluesoft.jeaf.view.statisticview.model.Column statisticViewColumn = (com.yuanluesoft.jeaf.view.statisticview.model.Column)column;
		xmlColumn.addAttribute("groupBy", statisticViewColumn.getGroupBy()); //分组
		xmlColumn.addAttribute("sourceField", statisticViewColumn.getSourceField()); //数据源
		if(statisticViewColumn.isEmptyWhenStatistic()) {
			xmlColumn.addAttribute("emptyWhenStatistic", "true"); //统计时是否清空
		}
		return xmlColumn;
	}
}
/*
 * Created on 2005-4-7
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.view.viewcustomize.model.ColumnCustom;
import com.yuanluesoft.jeaf.view.viewcustomize.model.SortColumnCustom;
import com.yuanluesoft.jeaf.view.viewcustomize.model.ViewCustom;
import com.yuanluesoft.jeaf.view.viewcustomize.parser.ViewCustomParser;

/**
 * 
 * @author linchuan
 *
 */
public class ViewCustomParserImpl extends XmlParser  implements ViewCustomParser {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.viewcustomize.parser.ViewCustomParser#parseViewCustom(java.lang.String)
	 */
	public ViewCustom parseViewCustom(String viewCustomText) throws ParseException {
		return parseViewCustom(parseXmlString(viewCustomText));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.viewcustomize.parser.ViewCustomParser#generateViewCustomXmlText(com.yuanluesoft.jeaf.view.viewcustomize.model.ViewCustom)
	 */
	public String generateViewCustomXmlText(ViewCustom viewCustom) throws ParseException {
		return asXML(generateViewCustomXMLDocument(viewCustom).getRootElement());
	}
	
	/**
	 * 解析视图定制信息
	 * @param xmlViewCustom
	 * @param view
	 * @return
	 * @throws ParseException
	 */
	private ViewCustom parseViewCustom(Element xmlViewCustom) throws ParseException {
		ViewCustom viewCustom = new ViewCustom();
		//解析每页记录数
		viewCustom.setPageRows(Integer.parseInt(xmlViewCustom.attributeValue("pageRows")));
		//解析列定义
		viewCustom.setColumns(new ArrayList());
		Element xmlCustoms = xmlViewCustom.element("columns");
		for(Iterator iterator=(xmlCustoms==null ? null : xmlCustoms.elementIterator()); iterator!=null && iterator.hasNext();) {
			Element xmlColumn = (Element)iterator.next();
			ColumnCustom columnCustom = new ColumnCustom();
			columnCustom.setColumnName(xmlColumn.attributeValue("name")); //列名称
			columnCustom.setColumnTitle(xmlColumn.attributeValue("title")); //列标题
			columnCustom.setColumnWidth(xmlColumn.attributeValue("width")); //列宽度
			columnCustom.setColumnAlign(xmlColumn.attributeValue("align")); //列对齐方式
			viewCustom.getColumns().add(columnCustom);
		}
		//解析排序列
		viewCustom.setSortColumns(new ArrayList());
		Element xmlSortCustoms = xmlViewCustom.element("sortColumns");
		for(Iterator iterator=(xmlSortCustoms==null ? null : xmlSortCustoms.elementIterator()); iterator!=null && iterator.hasNext();) {
			Element xmlColumn = (Element)iterator.next();
			SortColumnCustom sortColumnCustom = new SortColumnCustom();
			sortColumnCustom.setColumnName(xmlColumn.attributeValue("name")); //列名称
			sortColumnCustom.setColumnTitle(xmlColumn.attributeValue("title")); //列标题
			sortColumnCustom.setDesc("true".equals(xmlColumn.attributeValue("desc"))); //是否降序排列
			viewCustom.getSortColumns().add(sortColumnCustom);
		}
		return viewCustom;
	}
	
	/**
	 * 生成配置扩展XML
	 * @param workflowExtend
	 * @return
	 * @throws ParseException 
	 */
	private Document generateViewCustomXMLDocument(ViewCustom viewCustom) throws ParseException {
		Document xmlDoc = DocumentHelper.createDocument();
		Element xmlViewCustom = xmlDoc.addElement("viewCustom");
		xmlViewCustom.addAttribute("pageRows", "" + viewCustom.getPageRows());
		if(viewCustom.getColumns()!=null && !viewCustom.getColumns().isEmpty()) {
			Element xmlColumns = xmlViewCustom.addElement("columns");
			for(Iterator iterator = viewCustom.getColumns().iterator(); iterator.hasNext();) {
				ColumnCustom columnCustom = (ColumnCustom)iterator.next();
				Element xmlColumn = xmlColumns.addElement("column");
				if(columnCustom.getColumnName()!=null && !columnCustom.getColumnName().isEmpty()) {
					xmlColumn.addAttribute("name", columnCustom.getColumnName());
				}
				xmlColumn.addAttribute("title",columnCustom.getColumnTitle());
				if(columnCustom.getColumnWidth()!=null && !columnCustom.getColumnWidth().isEmpty()) {
					xmlColumn.addAttribute("width", columnCustom.getColumnWidth());
				}
				xmlColumn.addAttribute("align", columnCustom.getColumnAlign());
			}
		}
		if(viewCustom.getSortColumns()!=null && !viewCustom.getSortColumns().isEmpty()) {
			Element xmlSortColumns = xmlViewCustom.addElement("sortColumns");
			for(Iterator iterator = viewCustom.getSortColumns().iterator(); iterator.hasNext();) {
				SortColumnCustom sortColumnCustom = (SortColumnCustom)iterator.next();
				Element xmlColumn = xmlSortColumns.addElement("column");
				xmlColumn.addAttribute("name", sortColumnCustom.getColumnName());
				xmlColumn.addAttribute("title",sortColumnCustom.getColumnTitle());
				if(sortColumnCustom.isDesc()) {
					xmlColumn.addAttribute("desc", "true");
				}
			}
		}
		return xmlDoc;
	}
}
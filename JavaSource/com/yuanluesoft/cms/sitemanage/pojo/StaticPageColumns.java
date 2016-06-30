package com.yuanluesoft.cms.sitemanage.pojo;

import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageRecordList;

/**
 * 静态页面:使用到的栏目列表(cms_static_page_columns)
 * @author linchuan
 *
 */
public class StaticPageColumns extends StaticPageRecordList {
	private char columnType = '0'; //栏目类型,0/子栏目,1/父栏目,2/兄弟栏目

	/**
	 * @return the columnType
	 */
	public char getColumnType() {
		return columnType;
	}

	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(char columnType) {
		this.columnType = columnType;
	}
}
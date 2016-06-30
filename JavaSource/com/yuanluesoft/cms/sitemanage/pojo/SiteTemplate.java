package com.yuanluesoft.cms.sitemanage.pojo;

import com.yuanluesoft.cms.templatemanage.pojo.Template;

/**
 * 站点模板
 * @author linchuan
 *
 */
public class SiteTemplate extends Template {
	private String columnNames; //适用的栏目,配置子栏目模板时有效
	private String columnIds; //适用的栏目ID
	private int matchByName; //按栏目名称匹配栏目
	
	/**
	 * 获取格式过的栏目名称列表
	 * @return
	 */
	public String getFormattedColumnNames() {
		if(columnNames==null || !columnNames.startsWith(",") || !columnNames.endsWith(",")) {
			return null;
		}
		return columnNames.substring(1, columnNames.length()-1);
	}
	
	/**
	 * @return the columnIds
	 */
	public String getColumnIds() {
		return columnIds;
	}
	/**
	 * @param columnIds the columnIds to set
	 */
	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
	}
	/**
	 * @return the columnNames
	 */
	public String getColumnNames() {
		return columnNames;
	}
	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	/**
	 * @return the matchByName
	 */
	public int getMatchByName() {
		return matchByName;
	}
	/**
	 * @param matchByName the matchByName to set
	 */
	public void setMatchByName(int matchByName) {
		this.matchByName = matchByName;
	}
}
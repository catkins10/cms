package com.yuanluesoft.cms.siteresource.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 抓取规则配置
 * @author linchuan
 *
 */
public class CaptureColumn extends ActionForm {
	private String key; //关键字
	private String columnIds; //栏目ID列表
	private String columnNames; //栏目名称列表
	private int issue = 1; //是否发布
	
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
	 * @return the issue
	 */
	public int getIssue() {
		return issue;
	}
	/**
	 * @param issue the issue to set
	 */
	public void setIssue(int issue) {
		this.issue = issue;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
package com.yuanluesoft.jeaf.database.model;

import java.io.Serializable;

/**
 * 索引
 * @author linchuan
 *
 */
public class TableIndex implements Serializable {
	private String indexName; //名称
	private String indexColumns; //列
	
	/**
	 * @return the indexColumns
	 */
	public String getIndexColumns() {
		return indexColumns;
	}
	/**
	 * @param indexColumns the indexColumns to set
	 */
	public void setIndexColumns(String indexColumns) {
		this.indexColumns = indexColumns;
	}
	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}
	/**
	 * @param indexName the indexName to set
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
}
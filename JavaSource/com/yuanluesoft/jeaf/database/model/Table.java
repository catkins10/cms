package com.yuanluesoft.jeaf.database.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表
 * @author linchuan
 *
 */
public class Table implements Serializable {
	private String tableName; //表名称
	private String description; //描述
	private String pojoClassName; //POJO类名称
	private String primaryKey; //主键
	private List columns; //列列表
	private List indexes; //索引列表
	
	/**
	 * 添加列
	 * @param name
	 * @param pojoPropertyName
	 * @param description
	 * @param type
	 * @param length
	 */
	public TableColumn addTableColumn(String name, String pojoPropertyName, String description, String type, String length) {
		if(columns==null) {
			columns = new ArrayList();
		}
		TableColumn column = new TableColumn(name, pojoPropertyName, description, type, length);
		columns.add(column);
		return column;
	}
	
	/**
	 * 添加一对多列
	 * @param name
	 * @param description
	 * @param lazy
	 * @param tableName
	 * @param orderBy
	 * @param foreignKey
	 * @param pojoClassName
	 */
	public TableOneToManyColumn addTableOneToManyColumn(String name, String description, boolean lazy, String tableName, String orderBy, String foreignKey, String pojoClassName) {
		if(columns==null) {
			columns = new ArrayList();
		}
		TableOneToManyColumn column = new TableOneToManyColumn(name, description, lazy, tableName, orderBy, foreignKey, pojoClassName);
		columns.add(column);
		return column;
	}
	
	/**
	 * 添加多对一列
	 * @param name
	 * @param description
	 * @param pojoClassName
	 * @param columnName
	 * @return
	 */
	public TableManyToOneColumn addTableManyToOneColumn(String name, String description, String pojoClassName, String columnName) {
		if(columns==null) {
			columns = new ArrayList();
		}
		TableManyToOneColumn column = new TableManyToOneColumn(name, description, pojoClassName, columnName);
		columns.add(column);
		return column;
	}
	
	/**
	 * 添加索引
	 * @param indexName
	 * @param indexColumns
	 * @return
	 */
	public TableIndex addTableIndex(String indexName, String indexColumns) {
		if(indexes==null) {
			indexes = new ArrayList();
		}
		TableIndex index = new TableIndex();
		index.setIndexName(indexName);
		index.setIndexColumns(indexColumns);
		indexes.add(index);
		return index;
	}
	
	/**
	 * @return the pojoClassName
	 */
	public String getPojoClassName() {
		return pojoClassName;
	}
	/**
	 * @param pojoClassName the pojoClassName to set
	 */
	public void setPojoClassName(String pojoClassName) {
		this.pojoClassName = pojoClassName;
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the columns
	 */
	public List getColumns() {
		return columns;
	}
	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List columns) {
		this.columns = columns;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the indexes
	 */
	public List getIndexes() {
		return indexes;
	}

	/**
	 * @param indexes the indexes to set
	 */
	public void setIndexes(List indexes) {
		this.indexes = indexes;
	}

	/**
	 * @return the primaryKey
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
}
package com.yuanluesoft.jeaf.database.model;

/**
 * 多对一列
 * @author linchuan
 *
 */
public class TableManyToOneColumn extends TableColumn {
	private String pojoClassName; //POJO类型名称
	private String columnName; //列名称
	
	public TableManyToOneColumn(String name, String description, String pojoClassName, String columnName) {
		super(name, null, description, COLUMN_TYPE_MANY_TO_ONE, null);
		this.pojoClassName = pojoClassName;
		this.columnName = columnName;
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
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
package com.yuanluesoft.jeaf.database.model;

/**
 * 一对多列
 * @author linchuan
 *
 */
public class TableOneToManyColumn extends TableColumn {
	private boolean lazy = false; //是否延迟加载
	private String tableName; //关联的表
	private String orderBy; //关联的表排序字段
	private String foreignKey; //外键名称
	private String pojoClassName; //POJO类名称
	
	public TableOneToManyColumn(String name, String description, boolean lazy, String tableName, String orderBy, String foreignKey, String pojoClassName) {
		super(name, null, description, TableColumn.COLUMN_TYPE_ONE_TO_MANY, null);
		this.lazy = lazy;
		this.tableName = tableName;
		this.orderBy = orderBy;
		this.foreignKey = foreignKey;
		this.pojoClassName = pojoClassName;
	}

	/**
	 * @return the className
	 */
	public String getPojoClassName() {
		return pojoClassName;
	}

	/**
	 * @param className the className to set
	 */
	public void setPojoClassName(String className) {
		this.pojoClassName = className;
	}

	/**
	 * @return the foreignKey
	 */
	public String getForeignKey() {
		return foreignKey;
	}

	/**
	 * @param foreignKey the foreignKey to set
	 */
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	/**
	 * @return the lazy
	 */
	public boolean isLazy() {
		return lazy;
	}

	/**
	 * @param lazy the lazy to set
	 */
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
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
	
}
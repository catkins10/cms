package com.yuanluesoft.jeaf.dataimport.model;

/**
 * 目录和表的映射
 * @author linchuan
 *
 */
public class DirectoryTableMapping {
	private String directoryType;
	private String baseTableName; //目标基本表
	private String tableName;
	private String pojoClassName; //POJO类名称
	
	public DirectoryTableMapping(String directoryType, String baseTableName, String tableName, String pojoClassName) {
		super();
		this.directoryType = directoryType;
		this.baseTableName = baseTableName;
		this.tableName = tableName;
		this.pojoClassName = pojoClassName;
	}
	/**
	 * @return the baseTableName
	 */
	public String getBaseTableName() {
		return baseTableName;
	}
	/**
	 * @param baseTableName the baseTableName to set
	 */
	public void setBaseTableName(String baseTableName) {
		this.baseTableName = baseTableName;
	}
	/**
	 * @return the directoryType
	 */
	public String getDirectoryType() {
		return directoryType;
	}
	/**
	 * @param directoryType the directoryType to set
	 */
	public void setDirectoryType(String directoryType) {
		this.directoryType = directoryType;
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
}
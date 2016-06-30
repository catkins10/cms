package com.yuanluesoft.jeaf.database.model;

import java.io.Serializable;

/**
 * 表格列
 * @author linchuan
 *
 */
public class TableColumn implements Serializable {
	public final static String COLUMN_TYPE_VARCHAR = "varchar";
	public final static String COLUMN_TYPE_TEXT = "text";
	public final static String COLUMN_TYPE_CHAR = "char";
	public final static String COLUMN_TYPE_NUMBER = "number";
	public final static String COLUMN_TYPE_DATE = "date";
	public final static String COLUMN_TYPE_TIMESTAMP = "timestamp";
	public final static String COLUMN_TYPE_ONE_TO_MANY = "oneToMany";
	public final static String COLUMN_TYPE_MANY_TO_ONE = "manyToOne";
	
	private String name; //名称
	private String pojoPropertyName; //对应的POJO属性名称,为空时和名称相同
	private String description; //描述
	private String type; //类型,包括:varchar/text/char/number/date/timestamp
	private String length; //长度
	private String referenceTable; //关联表
	
	public TableColumn() {
		super();
	}

	public TableColumn(String name, String pojoPropertyName, String description, String type, String length) {
		super();
		this.name = name;
		this.pojoPropertyName = pojoPropertyName;
		this.description = description;
		this.type = type;
		this.length = length;
	}
	
	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the pojoPropertyName
	 */
	public String getPojoPropertyName() {
		return pojoPropertyName;
	}

	/**
	 * @param pojoPropertyName the pojoPropertyName to set
	 */
	public void setPojoPropertyName(String pojoPropertyName) {
		this.pojoPropertyName = pojoPropertyName;
	}

	/**
	 * @return the referenceTable
	 */
	public String getReferenceTable() {
		return referenceTable;
	}

	/**
	 * @param referenceTable the referenceTable to set
	 */
	public void setReferenceTable(String referenceTable) {
		this.referenceTable = referenceTable;
	}
}
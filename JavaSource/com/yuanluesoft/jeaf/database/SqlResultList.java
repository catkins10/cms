package com.yuanluesoft.jeaf.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * SQL语句执行结果列表
 * @author yuanluesoft
 *
 */
public class SqlResultList extends ArrayList implements Serializable {
	private List fieldNames; //字段名称列表

	/**
	 * @return the fieldNames
	 */
	public List getFieldNames() {
		return fieldNames;
	}

	/**
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(List fieldNames) {
		this.fieldNames = fieldNames;
	}
}
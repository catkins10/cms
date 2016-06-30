package com.yuanluesoft.jeaf.database;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * SQL语句执行结果
 * @author yuanluesoft
 *
 */
public class SqlResult implements Serializable {
	private Map values; //值列表
	
	/**
	 * 获取字段值
	 * @param columnName
	 * @return
	 */
	public Object get(String columnName) {
		return (values==null ? null : values.get(columnName.toLowerCase()));
	}
	
	/**
	 * 添加列
	 * @param columnName
	 * @param columnValue
	 */
	public void put(String columnName, Object columnValue) {
		if(values==null) {
			values = new HashMap();
		}
		values.put(columnName.toLowerCase(), columnValue);
	}
	
	/**
	 * 检查是否包含指定字段
	 * @param columnName
	 * @return
	 */
	public boolean containsColumn(String columnName) {
		return (values==null ? false : values.containsKey(columnName.toLowerCase()));
	}
	
	/**
	 * 获取浮点型字段值
	 * @param columnName
	 * @return
	 */
	public float getFloat(String columnName) {
		Object value = get(columnName);
		return value==null ? 0 : ((Number)value).floatValue();
	}
	
	/**
	 * 获取整型字段值
	 * @param columnName
	 * @return
	 */
	public int getInt(String columnName) {
		Object value = get(columnName);
		return value==null ? 0 : ((Number)value).intValue();
	}
	
	/**
	 * 获取长整型字段值
	 * @param columnName
	 * @return
	 */
	public long getLong(String columnName) {
		Object value = get(columnName);
		return value==null ? 0 : ((Number)value).longValue();
	}
}
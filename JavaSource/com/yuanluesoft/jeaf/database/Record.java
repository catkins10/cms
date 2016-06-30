package com.yuanluesoft.jeaf.database;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 记录
 * @author linchuan
 *
 */
public class Record implements Serializable, Cloneable {
	private long id;
	private Map extendProperties; //扩展属性列表,用于统计等场合

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		Record newRecord = (Record)super.clone();
		if(extendProperties!=null) {
			newRecord.extendProperties = new HashMap(extendProperties);
		}
		return newRecord;
	}
	
	/**
	 * 获取扩展属性值
	 * @param propertyName
	 * @return
	 */
	public Object getExtendPropertyValue(String propertyName) {
		if(extendProperties==null) {
			return null;
		}
		return extendProperties.get(propertyName); 
	}
	
	/**
	 * 设置扩展属性值
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setExtendPropertyValue(String propertyName, Object propertyValue) {
		if(extendProperties==null) {
			extendProperties = new HashMap();
		}
		if(propertyValue==null) {
			extendProperties.remove(propertyName);
		}
		else {
			extendProperties.put(propertyName, propertyValue);
		}
	}
	
	/**
	 * 获取扩展属性名称集合
	 * @return
	 */
	public Set getExtendPropertyNames() {
		return extendProperties==null ? null : extendProperties.keySet();
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}
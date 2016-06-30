package com.yuanluesoft.jeaf.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.database.LazyBody;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author yuanluesoft
 *
 */
public class LazyBodyUtils {

	/**
	 * 获取属性
	 * @param lazyBody
	 * @param propertyName
	 * @return
	 */
	public static String getBody(Record record) {
		try {
			Set lazyBody = (Set)PropertyUtils.getProperty(record, "lazyBody");
			if(lazyBody==null || lazyBody.isEmpty()) {
				return null;
			}
			return ((LazyBody)lazyBody.iterator().next()).getBody();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 设置属性
	 * @param lazyBody
	 * @param lazyClass
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public static void setBody(Record record, String body) {
		try {
			LazyBody lazyBodyRecord;
			Set lazyBody = (Set)PropertyUtils.getProperty(record, "lazyBody");
			if(lazyBody!=null && !lazyBody.isEmpty()) {
				lazyBodyRecord = (LazyBody)lazyBody.iterator().next();
			}
			else {
				lazyBodyRecord = (LazyBody)getLazyBodyPojoClass(record.getClass().getName()).newInstance();
				lazyBody = new HashSet();
				lazyBody.add(lazyBodyRecord);
				PropertyUtils.setProperty(record, "lazyBody", lazyBody);
			}
			lazyBodyRecord.setId(record.getId());
			lazyBodyRecord.setBody(body);
		}
		catch(Exception e) {
			
		}
	}
	
	/**
	 * 获取正文类
	 * @param pojoClassName
	 * @return
	 */
	private static Class getLazyBodyPojoClass(String pojoClassName) {
		for(int i=0; i<5; i++) {
			try {
				String className = pojoClassName + "Body";
				return Class.forName(className); //检查类是否存在
			}
			catch (ClassNotFoundException e) {
				
			}
			try {
				pojoClassName = Class.forName(pojoClassName).getSuperclass().getName(); //查找父类对应的正文
			}
			catch (ClassNotFoundException e) {
				
			}
		}
		return null;
	}
}
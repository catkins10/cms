package com.yuanluesoft.jeaf.util;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.callback.GenerateBeanCallback;

/**
 * 
 * @author linchuan
 *
 */
public class BeanUtils {

	/**
	 * 判断两个BEAN是否相同
	 * @param bean0
	 * @param bean1
	 * @return
	 */
	public static boolean equals(Object bean0, Object bean1) {
		if(bean0==null && bean1==null) {
			return true;
		}
		if(bean0==null || bean1==null) {
			return false;
		}
		if(!bean0.getClass().getName().equals(bean1.getClass().getName())) {
			return false;
		}
		if((bean0 instanceof String) || //字符串
		   (bean0 instanceof Number) || //数字
		   (bean0 instanceof Character) || //字符
		   (bean0 instanceof Date) || //日期
		   (bean0 instanceof Boolean)) { //布尔
			return bean0.equals(bean1);
		}
		else if(bean0 instanceof Object[]) { //对象数组
			Object[] objects0 = (Object[])bean0;
			Object[] objects1 = (Object[])bean1;
			if(objects0.length!=objects1.length) {
				return false;
			}
			int i=0;
			for(; i<objects0.length && equals(objects0[i], objects1[i]); i++);
			return i==objects0.length;
		}
		else if(bean0 instanceof int[]) { //整数数组
			int[] numbers0 = (int[])bean0;
			int[] numbers1 = (int[])bean1;
			if(numbers0.length!=numbers1.length) {
				return false;
			}
			int i=0;
			for(; i<numbers0.length && numbers0[i]==numbers1[i]; i++);
			return i==numbers0.length;
		}
		else if(bean0 instanceof double[]) { //双精度数数组
			double[] numbers0 = (double[])bean0;
			double[] numbers1 = (double[])bean1;
			if(numbers0.length!=numbers1.length) {
				return false;
			}
			int i=0;
			for(; i<numbers0.length && numbers0[i]==numbers1[i]; i++);
			return i==numbers0.length;
		}
		else if(bean0 instanceof float[]) { //浮点数数组
			float[] numbers0 = (float[])bean0;
			float[] numbers1 = (float[])bean1;
			if(numbers0.length!=numbers1.length) {
				return false;
			}
			int i=0;
			for(; i<numbers0.length && numbers0[i]==numbers1[i]; i++);
			return i==numbers0.length;
		}
		else if(bean0 instanceof Collection) { //集合、列表
			Collection collection0 = (Collection)bean0;
			Collection collection1 = (Collection)bean1;
			if(collection0.size()!=collection1.size()) {
				return false;
			}
			Iterator iterator0 = collection0.iterator();
			Iterator iterator1 = collection1.iterator();
			for(; iterator0.hasNext(); ) {
				Object object0 = iterator0.next();
				Object object1 = iterator1.next();
				if(!equals(object0, object1)) {
					return false;
				}
			}
			return true;
		}
		else {
			PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(bean0);
			if(properties==null || properties.length==0) {
				return bean0.equals(bean1);
			}
			for(int i=0; i<properties.length; i++) {
				String propertyName = properties[i].getName();
				if(!PropertyUtils.isWriteable(bean0, propertyName)) { //可写
					continue;
				}
				try {
					Object object0 = PropertyUtils.getProperty(bean0, propertyName);
					Object object1 = PropertyUtils.getProperty(bean1, propertyName);
					if(!equals(object0, object1)) {
						return false;
					}
				}
				catch(Exception e) {
					
				}
			}
			return true;
		}
	}
	
	/**
	 * 生成属性MAP
	 * @param bean
	 * @return
	 */
	public static Map generatePropertyMap(Object bean) {
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(bean);
		if(properties==null || properties.length==0) {
			return null;
		}
		Map propertyMap = new HashMap();
		for(int i=0; i<properties.length; i++) {
			try {
				String propertyName = properties[i].getName();
				propertyMap.put(propertyName, PropertyUtils.getProperty(bean, propertyName));
			}
			catch(Exception e) {
				
			}
		}
		return propertyMap;
	}
	
	/**
	 * 拷贝属性
	 * @param src
	 * @param dest
	 */
	public static void copyProperties(Object src, Object dest) {
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(dest);
		for(int i=0; i<properties.length; i++) {
			try {
				String propertyName = properties[i].getName();
				PropertyUtils.setProperty(dest, propertyName, PropertyUtils.getProperty(src, propertyName));
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 设置字段值
	 * @param bean
	 * @param propertyName
	 * @param propertyValue
	 * @param dateTimeFormat
	 */
	public static void setPropertyValue(Object bean, String propertyName, Object propertyValue, String dateTimeFormat) {
		if(bean==null) {
			return;
		}
		try {
			Class propertyType = PropertyUtils.getPropertyType(bean, propertyName);
			String propertyTypeName =  propertyType.getCanonicalName();
			if(String.class.isAssignableFrom(propertyType)) { //字符串
				PropertyUtils.setProperty(bean, propertyName, propertyValue==null || propertyValue.toString().isEmpty() ? null : propertyValue.toString());
			}
			else if(Integer.class.isAssignableFrom(propertyType) || "int".equals(propertyTypeName)) { //整数
				PropertyUtils.setProperty(bean, propertyName, propertyValue instanceof Number ? new Integer(((Number)propertyValue).intValue()) : new Integer(propertyValue.toString().replaceAll("℃", "")));
			}
			else if(Long.class.isAssignableFrom(propertyType) || "long".equals(propertyTypeName)) { //长整数
				PropertyUtils.setProperty(bean, propertyName, propertyValue instanceof Number ? new Long(((Number)propertyValue).longValue()) : new Long(propertyValue.toString().replaceAll("℃", "")));
			}
			else if(Float.class.isAssignableFrom(propertyType) || "float".equals(propertyTypeName)) { //浮点数
				PropertyUtils.setProperty(bean, propertyName, propertyValue instanceof Number ? new Float(((Number)propertyValue).floatValue()) : new Float(propertyValue.toString().replaceAll("℃", "")));
			}
			else if(Double.class.isAssignableFrom(propertyType) || "double".equals(propertyTypeName)) { //双精度浮点数
				PropertyUtils.setProperty(bean, propertyName, propertyValue instanceof Number ? new Double(((Number)propertyValue).doubleValue()) : new Double(propertyValue.toString().replaceAll("℃", "")));
			}
			else if(Boolean.class.isAssignableFrom(propertyType) || "boolean".equals(propertyTypeName)) { //布尔
				PropertyUtils.setProperty(bean, propertyName, propertyValue instanceof Boolean ? propertyValue : new Boolean(propertyValue.toString()));
			}
			else if(Character.class.isAssignableFrom(propertyType) || "char".equals(propertyTypeName)) { //字符
				PropertyUtils.setProperty(bean, propertyName, new Character(propertyValue.toString().charAt(0)));
			}
			else if(Timestamp.class.isAssignableFrom(propertyType)) { //时间
				PropertyUtils.setProperty(bean, propertyName, propertyValue instanceof Timestamp ? propertyValue : DateTimeUtils.parseTimestamp(propertyValue.toString(), dateTimeFormat));
			}
			else if(Date.class.isAssignableFrom(propertyType)) { //日期
				PropertyUtils.setProperty(bean, propertyName, propertyValue instanceof Date ? propertyValue :  DateTimeUtils.parseDate(propertyValue.toString(), dateTimeFormat));
			}
			else if(propertyType.isArray()) { //数组
				List list = ListUtils.generateListFromArray(propertyValue);
				PropertyUtils.setProperty(bean, propertyName, ListUtils.generateArrayFromList(list, propertyType, dateTimeFormat));
			}
		}
		catch(Exception e) {
			//Logger.exception(e);
		}
	}
	
	/**
	 * 根据属性列表生成对象
	 * @param objectClass
	 * @param properties
	 * @param callback
	 * @return
	 */
	public static Object generateBeanByProperties(Class objectClass, String properties, GenerateBeanCallback callback) {
		Object bean;
		try {
			bean = objectClass.newInstance();
		} 
		catch (Exception e) {
			Logger.exception(e);
			throw new Error(e.getMessage());
		}
		List attributes = StringUtils.getProperties(properties);
		for(Iterator iterator = attributes==null ? null : attributes.iterator(); iterator!=null && iterator.hasNext();) {
			Attribute attribute = (Attribute)iterator.next();
			if(attribute.getValue()==null || attribute.getValue().equals("")) {
				continue;
			}
			try {
				if(PropertyUtils.isWriteable(bean, attribute.getName())) {
					setPropertyValue(bean, attribute.getName(), attribute.getValue(), null);
				}
				else if(callback!=null) {
					callback.setPropertyValue(bean, attribute.getName(), attribute.getValue());
				}
			}
			catch(Exception e) {
				
			}
		}
		return bean;
	}
	
	/**
	 * 获取组成部分
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static Object getComponent(Object bean, String propertyName) {
		int index = propertyName.lastIndexOf('.');
		if(index!=-1) {
			try {
				bean = PropertyUtils.getProperty(bean, propertyName.substring(0, index));
			}
			catch(Exception e) {
			
			}
		}
		return bean;
	}
}
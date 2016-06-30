package com.yuanluesoft.jeaf.util;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author chuan
 *
 */
public class JsonUtils {
	
	/**
	 * 生成JSON数组
	 * @param obj
	 * @return
	 */
	public static JSONObject generateJSONObject(Object obj) {
		JSONArray jsons = new JSONArray();
		JSONObject json = generateJSONObject(obj, jsons);
		json.put("objects", jsons);
		return json;
	}

	/**
	 * 生成JSON对象
	 * @param obj
	 * @param jsons
	 * @return
	 */
	private static JSONObject generateJSONObject(final Object obj, JSONArray jsons) {
		//检查是否在列表中
		String uuid = obj.getClass().getName() + "@" + obj.hashCode();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("uuid", uuid);
		if(obj instanceof Collection) { //对象本身是列表
			jsonObject.put("collection", convertJSONValue(obj, jsons));
			return jsonObject;
		}
		Iterator iterator = jsons.iterator();
		for(; iterator.hasNext();) {
			JSONObject json = (JSONObject)iterator.next();
			if(uuid.equals(json.get("uuid"))) {
				return jsonObject;
			}
		}
		JSONObject json = new JSONObject();
		json.put("uuid", uuid);
		jsons.add(json);
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(obj);
		for(int i=0; i<(properties==null ? 0 : properties.length); i++) {
			String propertyName = properties[i].getName();
			if("class".equals(propertyName)) {
				continue;
			}
			Object propertyValue = null;
			try {
				propertyValue = PropertyUtils.getProperty(obj, propertyName);
			}
			catch(Exception e) {
				
			}
			json.put(propertyName, convertJSONValue(propertyValue, jsons));
		}
		return jsonObject;
	}
	
	/**
	 * 转换为JSON值
	 * @param propertyValue
	 * @return
	 */
	private static Object convertJSONValue(Object propertyValue, JSONArray jsons) {
		if(propertyValue==null) {
			return null;
		}
		if(propertyValue.getClass().isArray()) { //数组
			propertyValue = ListUtils.generateListFromArray(propertyValue); //转换为列表
			if(propertyValue==null) {
				propertyValue = new ArrayList();
			}
		}
		if((propertyValue instanceof Long) || (propertyValue instanceof Character)) { //长整型、字符
			return "" + propertyValue;
		}
		if(propertyValue instanceof Timestamp) { //时间
			return DateTimeUtils.formatTimestamp((Timestamp)propertyValue, null);
		}
		if(propertyValue instanceof Date) { //日期
			return DateTimeUtils.formatDate(new java.sql.Date(((Date)propertyValue).getTime()), null);
		}
		if((propertyValue instanceof String) || (propertyValue instanceof Number) || (propertyValue instanceof Boolean)) { //字符串、数字、布尔
			return propertyValue;
		}
		if(propertyValue instanceof Class) { //类
			return "" + ((Class)propertyValue).getName();
		}
		if(propertyValue instanceof Collection) { //列表
			JSONArray array = new JSONArray();
			for(Iterator iterator = ((Collection)propertyValue).iterator(); iterator.hasNext();) {
				Object item = iterator.next();
				array.add(convertJSONValue(item, jsons));
			}
			return array;
		}
		//其它对象
		return generateJSONObject(propertyValue, jsons);
	}
	
	/**
	 * 生成Java对象
	 * @param jsonText
	 * @return
	 */
	public static Object generateJavaObject(String jsonText) {
		try {
			if(jsonText==null || jsonText.isEmpty()) {
				return null;
			}
			if(Logger.isTraceEnabled()) {
				Logger.trace("JsonUtils: parse json text " + (jsonText.length() < 300 ? jsonText : jsonText.substring(0, 300) + "..."));
			}
			JSONObject jsonObject = (JSONObject)new JSONParser().parse(jsonText);
			return generateJavaObject(jsonObject, (JSONArray)jsonObject.get("objects"), new HashMap());
		}
		catch (Exception e) {
			throw new Error(e);
		}
	}
	
	/**
	 * 递归函数:生成Java对象
	 * @param uuid
	 * @param objects
	 * @return
	 */
	private static Object generateJavaObject(JSONObject jsonObject, JSONArray jsonObjects, Map javaObjects) throws Exception {
		String uuid = (String)jsonObject.get("uuid");
		Object object = javaObjects.get(uuid);
		if(object!=null) {
			return object;
		}
		int index = uuid.indexOf('@');
		object = Class.forName(uuid.substring(0, index)).newInstance();
		if(object instanceof Collection) { //本身是列表
			Object propertyValue = jsonObject.get("collection");
			Object value = retrievePropertyValue(propertyValue, List.class, jsonObjects, javaObjects);
			((Collection)object).addAll((List)value);
			return object;
		}
		jsonObject = null;
		for(Iterator iterator = jsonObjects.iterator(); jsonObject==null && iterator.hasNext();) {
			JSONObject obj = (JSONObject)iterator.next();
			if(uuid.equals(obj.get("uuid"))) {
				jsonObject = obj;
			}
		}
		if(jsonObject==null) {
			return null;
		}
		javaObjects.put(uuid, object);
		for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
			String propertyName = (String)iterator.next();
			if("uuid".equals(propertyName)) {
				continue;
			}
			if(!PropertyUtils.isWriteable(object, propertyName)) {
				continue;
			}
			Object propertyValue = jsonObject.get(propertyName);
			Class propertyType = PropertyUtils.getPropertyType(object, propertyName);
			Object value = retrievePropertyValue(propertyValue, propertyType, jsonObjects, javaObjects);
			try {
				PropertyUtils.setProperty(object, propertyName, value);
			}
			catch(Exception e) {
				if(Logger.isInfoEnabled()) {
					Logger.info("JsonUtils: " + propertyName + " of " + object.getClass().getName() + " to set as " + value);
				}
				throw e;
			}
		}
		return object;
	}
	
	/**
	 * 重置属性值
	 * @param propertyValue
	 * @param propertyType
	 * @param jsonObjects
	 * @param javaObjects
	 * @return
	 * @throws Exception
	 */
	private static Object retrievePropertyValue(Object propertyValue, Class propertyType, JSONArray jsonObjects, Map javaObjects) throws Exception {
		if(propertyValue==null) {
			return null;
		}
		if(propertyValue instanceof JSONArray) {
			JSONArray array = (JSONArray)propertyValue;
			for(int i = 0; i<array.size(); i++) {
				Object item = array.get(i);
				if(item instanceof JSONObject) {
					array.set(i, generateJavaObject((JSONObject)item, jsonObjects, javaObjects));
				}
			}
		}
		if(propertyValue instanceof JSONObject) {
			propertyValue = generateJavaObject((JSONObject)propertyValue, jsonObjects, javaObjects);
		}
		else if(propertyType.equals(Long.class) || propertyType.equals(long.class)) { //长整型
			propertyValue = propertyValue instanceof Number ? new Long(((Number)propertyValue).longValue()) : new Long("" + propertyValue);
		}
		else if(propertyType.equals(Integer.class) || propertyType.equals(int.class)) { //整型
			propertyValue = propertyValue instanceof Number ? new Integer(((Number)propertyValue).intValue()) : new Integer("" + propertyValue);
		}
		else if(propertyType.equals(Short.class) || propertyType.equals(short.class)) { //短整型
			propertyValue = propertyValue instanceof Number ? new Short(((Number)propertyValue).shortValue()) : new Short("" + propertyValue);
		}
		else if(propertyType.equals(Float.class) || propertyType.equals(float.class)) { //浮点数
			propertyValue = propertyValue instanceof Number ? new Float(((Number)propertyValue).floatValue()) : new Float("" + propertyValue);
		}
		else if(propertyType.equals(Double.class) || propertyType.equals(double.class)) { //双精度数
			propertyValue = propertyValue instanceof Number ? new Double(((Number)propertyValue).doubleValue()) : new Double("" + propertyValue);
		}
		else if(propertyType.equals(Character.class) || propertyType.equals(char.class)) { //字符
			propertyValue = new Character((propertyValue + " ").charAt(0));
		}
		else if(propertyType.equals(Boolean.class) || propertyType.equals(boolean.class)) { //布尔型
			propertyValue = new Boolean("" + propertyValue);
		}
		else if(Timestamp.class.isAssignableFrom(propertyType)) { //时间         
			propertyValue = DateTimeUtils.parseTimestamp((String)propertyValue, null);
		}
		else if(Date.class.isAssignableFrom(propertyType)) { //日期
			propertyValue = DateTimeUtils.parseDate((String)propertyValue, null);
		}
		else if(propertyType.isArray()) { //数组
			JSONArray array = (JSONArray)propertyValue;
			propertyValue = ListUtils.generateArrayFromList(array, propertyType, null);
		}
		else if(Collection.class.isAssignableFrom(propertyType)) { //列表
			JSONArray array = (JSONArray)propertyValue;
			if(List.class.isAssignableFrom(propertyType)) {
				propertyValue = new ArrayList(array);
			}
			else if(Set.class.isAssignableFrom(propertyType)) {
				propertyValue = new LinkedHashSet(array);
			}
		}
		return propertyValue;
	}
}
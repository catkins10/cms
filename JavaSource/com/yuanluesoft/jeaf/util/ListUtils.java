/*
 * Created on 2005-1-19
 *
 */
package com.yuanluesoft.jeaf.util;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ListUtils {
	
	/**
	 * 通过数组构造列表
	 * @param array
	 * @return
	 */
	public static List generateListFromArray(Object array) {
		if(array==null) {
			return null;
		}
		List list = new ArrayList();
		if(array instanceof Object[]) {
			for(int i=0; i<((Object[])array).length; i++) {
				list.add(((Object[])array)[i]);
			}
		}
		else if(array instanceof int[]) {
			for(int i=0; i<((int[])array).length; i++) {
				list.add(new Integer(((int[])array)[i]));
			}
		}
		else if(array instanceof long[]) {
			for(int i=0; i<((long[])array).length; i++) {
				list.add(new Long(((long[])array)[i]));
			}
		}
		else if(array instanceof float[]) {
			for(int i=0; i<((float[])array).length; i++) {
				list.add(new Float(((float[])array)[i]));
			}
		}
		else if(array instanceof double[]) {
			for(int i=0; i<((double[])array).length; i++) {
				list.add(new Double(((double[])array)[i]));
			}
		}
		else if(array instanceof short[]) {
			for(int i=0; i<((short[])array).length; i++) {
				list.add(new Short(((short[])array)[i]));
			}
		}
		else if(array instanceof char[]) {
			for(int i=0; i<((char[])array).length; i++) {
				list.add(new Character(((char[])array)[i]));
			}
		}
		else if(array instanceof boolean[]) {
			for(int i=0; i<((boolean[])array).length; i++) {
				list.add(new Boolean(((boolean[])array)[i]));
			}
		}
		return (list.isEmpty() ? null : list);
	}
	
	/**
	 * 列表转换为数组
	 * @param list
	 * @param arrayType
	 * @return
	 */
	public static Object generateArrayFromList(List list, Class arrayType, String dateTimeFormat) {
		Object array = Array.newInstance(arrayType.getComponentType(), list.size());
		for(int i=0; i<list.size(); i++) {
			Object item = list.get(i);
			if(array instanceof int[]) {
				item = item instanceof Number ? new Integer(((Number)item).intValue()) : new Integer("" + item);
			}
			else if(array instanceof long[]) {
				item = item instanceof Number ? new Long(((Number)item).longValue()) : new Long("" + item);
			}
			else if(array instanceof float[]) {
				item = item instanceof Number ? new Float(((Number)item).floatValue()) : new Float("" + item);
			}
			else if(array instanceof double[]) {
				item = item instanceof Number ? new Double(((Number)item).doubleValue()) : new Double("" + item);
			}
			else if(array instanceof short[]) {
				item = item instanceof Number ? new Short(((Number)item).shortValue()) : new Short("" + item);
			}
			else if(array instanceof char[]) {
				item = new Character((item + " ").charAt(0));
			}
			else if(array instanceof boolean[]) {
				item = new Boolean("" + item);
			}
			else if(array instanceof Timestamp[]) { //时间         
				try {
					item = item instanceof Timestamp ? item : DateTimeUtils.parseTimestamp(((String)item).trim(), dateTimeFormat);
				}
				catch(ParseException e) {
					item = null;
				}
			}
			else if(array instanceof Date[]) { //日期
				try {
					item = item instanceof Date ? item : DateTimeUtils.parseDate(((String)item).trim(), dateTimeFormat);
				}
				catch (ParseException e) {
					item = null;
				}
			}
			Array.set(array, i, item);
		}
		return array;
	}
	
	/**
	 * 生成列表
	 * @param object
	 * @return
	 */
	public static List generateList(Object object) {
		List list = new ArrayList();
		list.add(object);
		return list;
	}
	
	/**
	 * 从字符串构造列表
	 * @param text
	 * @param separator
	 * @return
	 */
	public static List generateList(String text, String separator) {
		if(text==null || text.equals("")) {
			return null;
		}
		String[] array = text.split(separator);
		List list = new ArrayList();
		for(int i=0; i<array.length; i++) {
			list.add(array[i]);
		}
		return (list.isEmpty() ? null : list);
	}
	
	/**
	 * 生成属性值列表
	 * @param collection
	 * @param propertyName
	 * @return
	 */
	public static List generatePropertyList(Collection collection, String propertyName) {
		if(collection==null || collection.isEmpty()) {
			return null;
		}
		List propertyList = new ArrayList();
		for(Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				propertyList.add(getProperty(object, propertyName));
			}
			catch(Exception e) {
			    
			}
		}
		return propertyList;
	}
	
	/**
	 * 生成属性值列表
	 * @param array
	 * @param propertyName
	 * @return
	 */
	public static List generatePropertyList(Object[] array, String propertyName) {
		if(array==null || array.length==0) {
			return null;
		}
		List propertyList = new ArrayList();
		for(int i=0; i<array.length; i++) {
			try {
				propertyList.add(getProperty(array[i], propertyName));
			}
			catch(Exception e) {
			    
			}
		}
		return propertyList;
	}
	
	/**
	 * 将列表串联成字符串
	 * @param collection
	 * @param separator
	 * @param unique
	 * @return
	 */
	public static String join(Collection collection, String separator, boolean unique) {
		if(collection==null || collection.isEmpty()) {
			return null;
		}
		String ret = separator;
		for(Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object value = iterator.next();
			if(!unique || (value!=null && ret.indexOf(separator + value + separator)==-1)) {
				ret += value + separator;
			}
		}
		return separator.equals(ret) ? null : ret.substring(separator.length(), ret.length()-separator.length());
	}
	
	/**
	 * 将数组串联成字符串
	 * @param array
	 * @param separator
	 * @param unique
	 * @return
	 */
	public static String join(Object[] array, String separator, boolean unique) {
		if(array==null || array.length==0) {
			return null;
		}
		List values = new ArrayList();
		for(int i=0; i<array.length; i++) {
			if(!unique || array[i]!=null) {
				values.add(array[i]);
			}
		}
		return join(values, separator, unique);
	}
	
	/**
	 * 将整数数组串联成字符串
	 * @param array
	 * @param separator
	 * @param unique
	 * @return
	 */
	public static String join(int[] array, String separator, boolean unique) {
		if(array==null || array.length==0) {
			return null;
		}
		List values = new ArrayList();
		for(int i=0; i<array.length; i++) {
			values.add("" + array[i]);
		}
		return join(values, separator, unique);
	}
	
	/**
	 * 将长整数数组串联成字符串
	 * @param array
	 * @param separator
	 * @param unique
	 * @return
	 */
	public static String join(long[] array, String separator, boolean unique) {
		if(array==null || array.length==0) {
			return null;
		}
		List values = new ArrayList();
		for(int i=0; i<array.length; i++) {
			values.add("" + array[i]);
		}
		return join(values, separator, unique);
	}
	
	/**
	 * 将列表中的属性值串联成字符串
	 * @param collection
	 * @param separator
	 * @param unique
	 * @param property
	 * @return
	 */
	public static String join(Collection collection, String propertyName, String separator, boolean unique) {
		if(collection==null || collection.isEmpty()) {
			return null;
		}
		List values = new ArrayList();
		for(Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object value;
			try {
				value = getProperty(iterator.next(), propertyName);
			}
			catch(Exception e) {
				throw new Error(e);
			}
			if(!unique || value!=null) {
				values.add(value);
			}
		}
		return join(values, separator, unique);
	}
	
	/**
	 * 按属性值查找对象
	 * @param listToFind
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Object findObjectByProperty(Collection listToFind, String propertyName, Object propertyValue) {
		if(listToFind==null) {
			return null;
		}
		for(Iterator iterator=listToFind.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				if((propertyName==null ? object : getProperty(object, propertyName)).equals(propertyValue)) {
					return object;
				}
			}
			catch(Exception e) {
			    
			}
		}
		return null;
	}
	
	/**
	 * 按属性值在数组中查找对象
	 * @param objects
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public static Object findObjectByProperty(Object[] objects, String propertyName, Object propertyValue) {
		if(objects==null) {
			return null;
		}
		for(int i=0; i<objects.length; i++) {
			try {
				if(getProperty(objects[i], propertyName).equals(propertyValue)) {
					return objects[i];
				}
			} catch(Exception e) {
			    
			}
		}
		return null;
	}
	
	/**
	 * 按属性值删除对象,返回被删除的对象
	 * @param listToFind
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Object removeObjectByProperty(Collection listToFind, String propertyName, Object propertyValue) {
		if(listToFind==null) {
			return null;
		}
		for(Iterator iterator=listToFind.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				if(getProperty(object, propertyName).equals(propertyValue)) {
				    iterator.remove();
					return object;
				}
			} 
			catch(Exception e) {
					
			}
		}
		return null;
	}
	
	/**
	 * 按属性值删除对象列表
	 * @param listToFind
	 * @param propertyName
	 * @param propertyValue
	 */
	public static List removeObjectsByProperty(Collection listToFind, String propertyName, Object propertyValue) {
		if(listToFind==null) {
			return null;
		}
		List removed = new ArrayList();
		for(Iterator iterator=listToFind.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				if(getProperty(object, propertyName).equals(propertyValue)) {
					removed.add(object);
				    iterator.remove();
				}
			} 
			catch(Exception e) {
					
			}
		}
		return removed.isEmpty() ? null : removed;
	}
	
	/**
	 * 按类型删除对象列表
	 * @param listToFind
	 * @param type
	 * @param checkInstanceOf
	 * @return
	 */
	public static List removeObjectsByType(Collection listToFind, Class type, boolean checkInstanceOf) {
		if(listToFind==null) {
			return null;
		}
		List removed = new ArrayList();
		for(Iterator iterator=listToFind.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				if((checkInstanceOf && type.isAssignableFrom(object.getClass())) ||
				   (!checkInstanceOf && object.getClass()==type)) {
					removed.add(object);
				    iterator.remove();
				}
			} 
			catch(Exception e) {
					
			}
		}
		return removed.isEmpty() ? null : removed;
	}
	
	/**
	 * 查找并替换
	 * @param listToFind
	 * @param propertyName
	 * @param propertyValue
	 * @param replacement
	 */
	public static void replaceObjectByProperty(List listToFind, String propertyName, Object propertyValue, Object replacement) {
		if(listToFind==null) {
			return;
		}
		int size = listToFind.size();
		for(int i=0; i<size; i++) {
			try {
				if(getProperty(listToFind.get(i), propertyName).equals(propertyValue)) {
					listToFind.set(i, replacement);
					break;
				}
			} catch(Exception e) {
			}
		}
	}
	
	/**
	 * 按属性值获取子列表
	 * @param listToFind
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public static List getSubListByProperty(Collection listToFind, String propertyName, Object propertyValue) {
		if(listToFind==null) {
			return null;
		}
		List list = new ArrayList();
		for(Iterator iterator=listToFind.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				if(getProperty(object, propertyName).equals(propertyValue)) {
					list.add(object);
				}
			} catch(Exception e) {
			}
		}
		return list.isEmpty() ? null : list;
	}
	
	/**
	 * 按类型获取对象
	 * @param list
	 * @param type
	 * @return
	 */
	public static Object findObjectByType(Collection list, Class type) {
		if(list==null) {
			return null;
		}
		for(Iterator iterator=list.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if(object.getClass()==type) {
				return object;
			}
		}
		return null;
	}
	
	/**
	 * 按类型获取子列表
	 * @param list
	 * @param type
	 * @param checkInstanceOf
	 * @return
	 */
	public static List getSubListByType(Collection list, Class type, boolean checkInstanceOf) {
		if(list==null) {
			return null;
		}
		List subList = new ArrayList();
		for(Iterator iterator=list.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if((checkInstanceOf && type.isAssignableFrom(object.getClass())) ||
			   (!checkInstanceOf && object.getClass()==type)) {
				subList.add(object);
			}
		}
		return subList.isEmpty() ? null : subList;
	}
	
	/**
	 * 检查一个列表中的元素是否都在另一个列表中
	 * @param toCheck
	 * @param list
	 * @param checkPropertyName
	 * @return
	 */
	public static boolean insideOf(Collection toCheck, Collection collection, String checkPropertyName) {
		if(toCheck==null || toCheck.isEmpty()) {
			return true;
		}
		if(collection==null || collection.isEmpty()) {
			return false;
		}
		for(Iterator iterator = toCheck.iterator(); iterator.hasNext();) {
			try {
				if(findObjectByProperty(collection, checkPropertyName, getProperty(iterator.next(), checkPropertyName))==null) {
					return false;
				}
			}
			catch (Exception e) {
				throw new Error(e);
			}
		}
		return true;
	}
	
	/**
	 * 检查对象是否都在另一个列表中
	 * @param object
	 * @param collection
	 * @param checkPropertyName
	 * @return
	 */
	public static boolean insideOf(Object object, Collection collection, String checkPropertyName) {
		if(object==null) {
			return true;
		}
		if(collection==null || collection.isEmpty()) {
			return false;
		}
		try {
			return (findObjectByProperty(collection, checkPropertyName, getProperty(object, checkPropertyName))!=null);
		}
		catch (Exception e) {
			throw new Error(e);
		}
	}
	
	/**
	 * 获取collection中和toCheck列表重叠的记录
	 * @param toCheck
	 * @param checkPropertyName
	 * @param collection
	 * @param propertyName
	 * @return
	 */
	public static List getInsideSubList(Collection toCheck, String checkPropertyName, Collection collection, String propertyName) {
		if(toCheck==null || collection==null) {
			return null;
		}
		List subList = new ArrayList();
		for(Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				if((object=findObjectByProperty(toCheck, checkPropertyName, (propertyName==null ? object : getProperty(object, propertyName))))!=null) {
					subList.add(object);
				}
			}
			catch (Exception e) {
				throw new Error(e);
			}
		}
		return subList.isEmpty() ? null : subList;
	}
	
	/**
	 * 获取在collection中有、toCheck中没有的元素
	 * @param toCheck
	 * @param checkPropertyName
	 * @param collection
	 * @param propertyName
	 * @return
	 */
	public static List getNotInsideSubList(Collection toCheck, String checkPropertyName, Collection collection, String propertyName) {
		if(collection==null || collection.isEmpty()) {
			return null;
		}
		if(toCheck==null || toCheck.isEmpty()) {
			return new ArrayList(collection);
		}
		List subList = new ArrayList();
		for(Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				if(findObjectByProperty(toCheck, checkPropertyName, (propertyName==null ? object : getProperty(object, propertyName)))==null) {
					subList.add(object);
				}
			}
			catch (Exception e) {
				throw new Error(e);
			}
		}
		return subList.isEmpty() ? null : subList;
	}
	
	/**
	 * 按属性调整列表
	 * @param list
	 * @param propertyName
	 * @param propertyValues 用逗号分隔
	 * @return
	 */
	public static List sortByProperty(Collection collection, String propertyName, String propertyValues) {
		if(collection==null || collection.isEmpty()) {
			return null;
		}
		List list = new ArrayList();
		String[] values = propertyValues.split(",");
		for(int i=0; i<values.length; i++) {
			try {
				for(Iterator iterator = collection.iterator(); iterator.hasNext();) {
					Object obj = iterator.next();
					if(values[i].equals("" + (propertyName==null ? obj : getProperty(obj, propertyName)))) {
						list.add(obj);
						break;
					}
				}
			}
			catch (Exception e) {
				return null;
			}
		}
		return list;
	}
	
	/**
	 * 添加全部
	 * @param list
	 * @param toAdd
	 */
	public static void addAll(Collection list, Collection toAdd) {
		if(list!=null && toAdd!=null && !toAdd.isEmpty()) {
			list.addAll(toAdd);
		}
	}
	
	/**
	 * 获取属性值
	 * @param obj
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	private static Object getProperty(Object obj, String propertyName) throws Exception {
		try {
			return PropertyUtils.getProperty(obj, propertyName);
		}
		catch(NoSuchMethodException e) {
			return PropertyUtils.getProperty(obj, propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1));
		}
	}
}
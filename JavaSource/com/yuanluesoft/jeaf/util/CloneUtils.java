package com.yuanluesoft.jeaf.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author linchuan
 *
 */
public class CloneUtils {
	private static final ThreadLocal threadLocal = new ThreadLocal();
	
	/**
	 * 克隆
	 * @param object
	 * @return
	 */
	public static void cloneProperties(Object object, Object clonedObject) {
		List fields = listFields(object.getClass());
		if(fields==null) {
			return;
		}
		//获取或者创建已克隆对象列表,避免循环克隆
		ArrayList clonedObjects = (ArrayList)threadLocal.get();
		if(clonedObjects==null) {
			clonedObjects = new ArrayList();
			threadLocal.set(clonedObjects);
		}
		boolean firstClonedObject = clonedObjects.isEmpty();
		//加入到已克隆对象列表
		clonedObjects.add(object);
		clonedObjects.add(clonedObject);
		//克隆属性
		for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			try {
				field.setAccessible(true);
				Object propertyValue = field.get(clonedObject);
				if(propertyValue!=null && !isBasicType(propertyValue)) {
					field.set(clonedObject, doClone(field.get(clonedObject)));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			catch(Error e) {
				e.printStackTrace();
			}
		}
		if(firstClonedObject) {
			if(clonedObjects.size()>500) {
				System.out.println("CloneUtils: total " + (clonedObjects.size()/2) + " objects cloned, class is " + object.getClass().getName() + ".");
			}
			clonedObjects.clear();
			threadLocal.remove();
		}
	}
	
	/**
	 * 获取字段列表
	 * @param clazz
	 * @return
	 */
	private static List listFields(Class clazz) {
		List fieldList = null;
		Field[] fields = clazz.getDeclaredFields();
		if(fields!=null && fields.length>=0) {
			fieldList = ListUtils.generateListFromArray(fields);
		}
		clazz = clazz.getSuperclass();
		if(clazz.equals(Object.class)) {
			return fieldList;
		}
		List superclassFields = listFields(clazz);
		if(superclassFields!=null) {
			if(fieldList==null) {
				fieldList = superclassFields;
			}
			else {
				fieldList.addAll(superclassFields);
			}
		}
		return fieldList;
	}
	
	/**
	 * 调用clone方法
	 * @param object
	 * @return
	 */
	private static Object doClone(Object object) {
		try {
			if(object==null) {
				return null;
			}
			else if(object instanceof Collection) { //集合
				return cloneCollection((Collection)object);
			}
			else if(object instanceof Map) { //map
				return cloneMap((Map)object);
			}
			else if(!isBasicType(object)) {
				//检查是否已经在已克隆对象列表中
				ArrayList clonedObjects = (ArrayList)threadLocal.get();
				for(int i=0; i<clonedObjects.size(); i+=2) {
					if(clonedObjects.get(i)==object || clonedObjects.get(i+1)==object) {
						return clonedObjects.get(i+1);
					}
				}
				return object.getClass().getMethod("clone", null).invoke(object, null);
			}
		}
		catch(Exception e) {
			
		}
		catch(Error e) {
			
		}
		return object;
	}
	
	/**
	 * 判断是否基础数据类型
	 * @param object
	 * @return
	 */
	public static boolean isBasicType(Object object) {
		return (object instanceof String) || 
			   (object instanceof Number) || 
			   (object instanceof Boolean) ||
			   (object instanceof Character);
	}
	
	/**
	 * 克隆集合
	 * @param list
	 * @return
	 */
	private static Collection cloneCollection(Collection collection) {
		Collection newCollection;
		try {
			newCollection = (Collection)collection.getClass().newInstance();
		}
		catch(Exception e) {
			return collection;
		}
		for(Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
			newCollection.add(obj==null || isBasicType(obj) ? obj : doClone(obj));
		}
		return newCollection;
	}
	
	/**
	 * 克隆MAP
	 * @param map
	 * @return
	 */
	private static Map cloneMap(Map map) {
		Map newMap;
		try {
			newMap = (Map)map.getClass().newInstance();
		}
		catch(Exception e) {
			return map;
		}
		if(map.isEmpty()) {
			return newMap;
		}
		for(Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			Object key = iterator.next();
			Object obj = map.get(key);
			newMap.put(key, obj==null || isBasicType(obj) ? obj : doClone(obj));
		}
		return newMap;
	}
}
package com.yuanluesoft.jeaf.util.callback;

/**
 * 
 * @author linchuan
 *
 */
public interface GenerateBeanCallback {
	
	/**
	 * 设置属性值,如果propertyName是bean的属性,不会调用本方法
	 * @param bean
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setPropertyValue(Object bean, String propertyName, String propertyValue);
}
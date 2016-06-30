package com.yuanluesoft.jeaf.business.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.base.model.CloneableObject;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 业务对象配置
 * @author linchuan
 *
 */
public class BusinessObject extends CloneableObject {
	private String title; //标题
	private String className; //类名称,pojo类名称
	private String applicationName; //应用名称
	private String businessServiceName; //业务逻辑服务名称,默认"businessService"
	private List fields; //字段列表
	private Map extendParameters; //参数列表,为字段类型、输入方式配置所需要的参数
	
	/**
	 * 获取参数值
	 * @param parameterName
	 * @return
	 */
	public Object getExtendParameter(String parameterName) {
		if(extendParameters==null) {
			return null;
		}
		return extendParameters.get(parameterName); 
	}
	
	/**
	 * 设置参数值
	 * @param parameterName
	 * @param parameterValue
	 */
	public void setExtendParameter(String parameterName, Object parameterValue) {
		if(extendParameters==null) {
			extendParameters = new HashMap();
		}
		if(parameterValue==null) {
			extendParameters.remove(parameterName);
		}
		else {
			extendParameters.put(parameterName, parameterValue);
		}
	}
	
	/**
	 * 获取字段定义
	 * @param fieldName
	 * @return
	 */
	public Field getField(String fieldName) {
		return (Field)ListUtils.findObjectByProperty(fields, "name", fieldName);
	}
	
	/**
	 * 按参数值查找字段
	 * @param parameterName
	 * @param parameterValue
	 * @return
	 */
	public Field getFieldByParameter(String parameterName, Object parameterValue) {
		if(fields==null || fields.isEmpty()) {
			return null;
		}
		for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(parameterValue.equals(field.getParameter(parameterName))) {
				return field;
			}
		}
		return null;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the fields
	 */
	public List getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List fields) {
		this.fields = fields;
	}

	/**
	 * @return the businessServiceName
	 */
	public String getBusinessServiceName() {
		return businessServiceName;
	}

	/**
	 * @param businessServiceName the businessServiceName to set
	 */
	public void setBusinessServiceName(String businessServiceName) {
		this.businessServiceName = businessServiceName;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the extendParameters
	 */
	public Map getExtendParameters() {
		return extendParameters;
	}

	/**
	 * @param extendParameters the extendParameters to set
	 */
	public void setExtendParameters(Map extendParameters) {
		this.extendParameters = extendParameters;
	}
}
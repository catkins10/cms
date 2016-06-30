/*
 * Created on 2004-12-19
 *
 */
package com.yuanluesoft.jeaf.form.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.base.model.CloneableObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 *
 * @author LinChuan
 * 
*/
public class Form extends CloneableObject {
	private String applicationName; //系统名称
	private String name; //表单名称
	private String title; //标题
	private String className; //表单类名称
	private String recordClassName; //数据库映射类名称
	private String method; //表单方法,默认为get
	private String action; //表单操作
	private String js; //需要引入的脚本,多个时用逗号分隔
	private Map extendedParameters; //拓展属性
	private List fields; //表单自有的字段列表,pojo的字段不需要重复配置
	private List actions; //表单操作列表
	
	/**
	 * 获取字段定义
	 * @param fieldName
	 * @return
	 */
	public Field getField(String fieldName) {
		return (Field)ListUtils.findObjectByProperty(fields, "name", fieldName);
	}
	
	/**
	 * 获取扩展参数值
	 * @param parameterName
	 * @return
	 */
	public String getExtendedParameter(String parameterName) {
		if(extendedParameters==null) {
			return null;
		}
		return (String)extendedParameters.get(parameterName); 
	}
	
	/**
	 * 设置扩展参数
	 * @param parameterName
	 * @param parameterValue
	 * @return
	 */
	public void setExtendedParameter(String parameterName, String parameterValue) {
		if(extendedParameters==null) {
			extendedParameters = new HashMap();
		}
		extendedParameters.put(parameterName, parameterValue);
	}
	
	/**
	 * 增加操作
	 * @param action
	 */
	public void addAction(FormAction action) {
		if(actions==null) {
			actions = new ArrayList();
		}
		actions.add(action);
	}
	/**
	 * 删除操作
	 * @param action
	 */
	public void removeAction(FormAction action) {
		actions.remove(action);
	}
	/**
	 * @return Returns the actions.
	 */
	public List getActions() {
		return actions;
	}
	/**
	 * @param actions The actions to set.
	 */
	public void setActions(List actions) {
		this.actions = actions;
	}
	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return Returns the pojoClassName.
	 */
	public String getRecordClassName() {
		return recordClassName;
	}
	/**
	 * @param pojoClassName The pojoClassName to set.
	 */
	public void setRecordClassName(String pojoClassName) {
		this.recordClassName = pojoClassName;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return Returns the application.
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param application The application to set.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the extendedParameters
	 */
	public Map getExtendedParameters() {
		return extendedParameters;
	}

	/**
	 * @param extendedParameters the extendedParameters to set
	 */
	public void setExtendedParameters(Map extendedParameters) {
		this.extendedParameters = extendedParameters;
	}

	/**
	 * @return the js
	 */
	public String getJs() {
		return js;
	}

	/**
	 * @param js the js to set
	 */
	public void setJs(String js) {
		this.js = js;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
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
}
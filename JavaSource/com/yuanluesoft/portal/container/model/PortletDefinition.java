package com.yuanluesoft.portal.container.model;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PortletDefinition extends CloneableObject {
	private String description;  //描述
	private String portletName; //名称
	private String displayName; //显示名称
	private String portletClass; //类名称
	private String resourceBundle; //国际化资源文件
	private List initParameters; //初始化参数列表
	private List supports; //支持的MIME类型以及对应的模式
	private List supportedLocales; //支持的区域
	private List preferences; //个性化设置参数
	
	/**
	 * 添加参数
	 * @param parameterName
	 * @param parameterValue
	 */
	public void addInitParameter(String parameterName, String parameterValue) {
		PortletParameter parameter = new PortletParameter();
		parameter.setName(parameterName); //参数名称
		parameter.setValue(parameterValue); //参数值
		if(initParameters==null) {
			initParameters = new ArrayList();
		}
		initParameters.add(parameter);
	}
	
	/**
	 * 获取参数值
	 * @param parameterName
	 * @return
	 */
	public String getInitParameterValue(String parameterName) {
		if(initParameters==null) {
			return null;
		}
		PortletParameter parameter = (PortletParameter)ListUtils.findObjectByProperty(initParameters, "name", parameterName);
		return parameter==null ? null : parameter.getValue();
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the initParameters
	 */
	public List getInitParameters() {
		return initParameters;
	}
	/**
	 * @param initParameters the initParameters to set
	 */
	public void setInitParameters(List initParameters) {
		this.initParameters = initParameters;
	}
	/**
	 * @return the portletClass
	 */
	public String getPortletClass() {
		return portletClass;
	}
	/**
	 * @param portletClass the portletClass to set
	 */
	public void setPortletClass(String portletClass) {
		this.portletClass = portletClass;
	}
	/**
	 * @return the portletName
	 */
	public String getPortletName() {
		return portletName;
	}
	/**
	 * @param portletName the portletName to set
	 */
	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	/**
	 * @return the resourceBundle
	 */
	public String getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * @param resourceBundle the resourceBundle to set
	 */
	public void setResourceBundle(String resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	/**
	 * @return the supports
	 */
	public List getSupports() {
		return supports;
	}

	/**
	 * @param supports the supports to set
	 */
	public void setSupports(List supports) {
		this.supports = supports;
	}

	/**
	 * @return the supportedLocales
	 */
	public List getSupportedLocales() {
		return supportedLocales;
	}

	/**
	 * @param supportedLocales the supportedLocales to set
	 */
	public void setSupportedLocales(List supportedLocales) {
		this.supportedLocales = supportedLocales;
	}

	/**
	 * @return the preferences
	 */
	public List getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences the preferences to set
	 */
	public void setPreferences(List preferences) {
		this.preferences = preferences;
	}
}
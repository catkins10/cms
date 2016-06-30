/*
 * Created on 2006-5-26
 *
 */
package com.yuanluesoft.eai.client.model;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class EAI extends Element {
	private String description; //描述
	private List children; //子元素列表, Group/Application/Link
	
	/**
	 * 获取应用
	 * @param applicationName
	 * @return
	 */
	public Application getApplication(String applicationName) {
		return getApplication(getChildren(), applicationName);
	}
	
	/**
	 * 获取EAI应用配置
	 * @param eaiChildren
	 * @param applicationName
	 * @return
	 */
	private Application getApplication(List eaiChildren, String applicationName) {
		for(Iterator iterator = eaiChildren==null ? null : eaiChildren.iterator(); iterator!=null && iterator.hasNext();) {
			Object element = iterator.next();
			if((element instanceof Application) && applicationName.equals(((Application)element).getName())) {
				return (Application)element;
			}
			else if(element instanceof Group) { //分组
				Application application = getApplication(((Group)element).getChildren(), applicationName);
				if(application!=null) {
					return application;
				}
			}
		}
		return null;
	}
	/**
	 * @return the children
	 */
	public List getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List children) {
		this.children = children;
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
}

package com.yuanluesoft.jeaf.form.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author linchuan
 *
 */
public class Tab {
	private String id; //ID
	private String name; //标签名称,如：流程记录、办理意见
	private String jspFile; //对应的jsp文件
	private boolean selected;
	private Map attributes; //属性列表

	public Tab(String id, String name, String jspFile, boolean selected) {
		super();
		this.id = id;
		this.name = name;
		this.jspFile = jspFile;
		this.selected = selected;
	}
	
	/**
	 * 获取属性值
	 * @param attributeName
	 * @return
	 */
	public Object getAttribute(String attributeName) {
		if(attributes==null) {
			return null;
		}
		return attributes.get(attributeName); 
	}
	
	/**
	 * 设置属性值
	 * @param attributeName
	 * @param attributeValue
	 */
	public void setAttribute(String attributeName, Object attributeValue) {
		if(attributes==null) {
			attributes = new HashMap();
		}
		if(attributeValue==null) {
			attributes.remove(attributeName);
		}
		else {
			attributes.put(attributeName, attributeValue);
		}
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	/**
	 * @return the jspFile
	 */
	public String getJspFile() {
		return jspFile;
	}
	/**
	 * @param jspFile the jspFile to set
	 */
	public void setJspFile(String jspFile) {
		this.jspFile = jspFile;
	}

	/**
	 * @return the attributes
	 */
	public Map getAttributes() {
		return attributes;
	}
}
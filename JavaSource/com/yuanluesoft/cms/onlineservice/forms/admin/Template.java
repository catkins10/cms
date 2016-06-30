package com.yuanluesoft.cms.onlineservice.forms.admin;


/**
 * 
 * @author linchuan
 *
 */
public class Template extends com.yuanluesoft.cms.templatemanage.forms.Template {
	private long directoryId; //目录ID
	private String directoryName; //目录名称
	private String itemTypes; //事项类型
	
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	/**
	 * @return the itemTypes
	 */
	public String getItemTypes() {
		return itemTypes;
	}
	/**
	 * @param itemTypes the itemTypes to set
	 */
	public void setItemTypes(String itemTypes) {
		this.itemTypes = itemTypes;
	}
}
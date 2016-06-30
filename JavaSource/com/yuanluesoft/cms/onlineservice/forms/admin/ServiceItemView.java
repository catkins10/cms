package com.yuanluesoft.cms.onlineservice.forms.admin;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemView extends ViewForm {
	private long directoryId; //目录ID
	private String importServiceItemIds; //需要引入的办理事项ID列表
	private String importServiceItemNames; //需要引入的办理事项名称列表

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
	 * @return the importServiceItemIds
	 */
	public String getImportServiceItemIds() {
		return importServiceItemIds;
	}

	/**
	 * @param importServiceItemIds the importServiceItemIds to set
	 */
	public void setImportServiceItemIds(String importServiceItemIds) {
		this.importServiceItemIds = importServiceItemIds;
	}

	/**
	 * @return the importServiceItemNames
	 */
	public String getImportServiceItemNames() {
		return importServiceItemNames;
	}

	/**
	 * @param importServiceItemNames the importServiceItemNames to set
	 */
	public void setImportServiceItemNames(String importServiceItemNames) {
		this.importServiceItemNames = importServiceItemNames;
	}
	
}
package com.yuanluesoft.cms.onlineservice.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ImportAuthority extends ActionForm {
	private long directoryId; //目录ID
	private boolean sheetAsDirectory; //把sheet作为目录
	private String otherDirectoryIds;//需同步导入的目录id
	private String otherDirectoryNames;//需同步导入的目录名称

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
	 * @return the sheetAsDirectory
	 */
	public boolean isSheetAsDirectory() {
		return sheetAsDirectory;
	}

	/**
	 * @param sheetAsDirectory the sheetAsDirectory to set
	 */
	public void setSheetAsDirectory(boolean sheetAsDirectory) {
		this.sheetAsDirectory = sheetAsDirectory;
	}

	public String getOtherDirectoryIds() {
		return otherDirectoryIds;
	}

	public void setOtherDirectoryIds(String otherDirectoryIds) {
		this.otherDirectoryIds = otherDirectoryIds;
	}

	public String getOtherDirectoryNames() {
		return otherDirectoryNames;
	}

	public void setOtherDirectoryNames(String otherDirectoryNames) {
		this.otherDirectoryNames = otherDirectoryNames;
	}
	
	
}
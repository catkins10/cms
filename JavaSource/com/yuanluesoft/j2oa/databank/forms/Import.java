/*
 * Created on 2006-10-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.j2oa.databank.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 *
 * @author linchuan
 *
 */
public class Import extends ActionForm {
	private String systemDirectory; //操作系统目录
	private String databankDirectoryName; //资料库目录
	private long databankDirectoryId; //资料库目录ID
	
	/**
	 * @return Returns the databankDirectoryId.
	 */
	public long getDatabankDirectoryId() {
		return databankDirectoryId;
	}
	/**
	 * @param databankDirectoryId The databankDirectoryId to set.
	 */
	public void setDatabankDirectoryId(long databankDirectoryId) {
		this.databankDirectoryId = databankDirectoryId;
	}
	/**
	 * @return Returns the databankDirectoryName.
	 */
	public String getDatabankDirectoryName() {
		return databankDirectoryName;
	}
	/**
	 * @param databankDirectoryName The databankDirectoryName to set.
	 */
	public void setDatabankDirectoryName(String databankDirectoryName) {
		this.databankDirectoryName = databankDirectoryName;
	}
	/**
	 * @return the systemDirectory
	 */
	public String getSystemDirectory() {
		return systemDirectory;
	}
	/**
	 * @param systemDirectory the systemDirectory to set
	 */
	public void setSystemDirectory(String systemDirectory) {
		this.systemDirectory = systemDirectory;
	}
}

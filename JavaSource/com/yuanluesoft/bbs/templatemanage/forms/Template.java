package com.yuanluesoft.bbs.templatemanage.forms;


/**
 * 
 * @author linchuan
 *
 */
public class Template extends com.yuanluesoft.cms.templatemanage.forms.Template {
	private long directoryId; //论坛目录ID
	private String directoryName; //论坛目录名称
	
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
}
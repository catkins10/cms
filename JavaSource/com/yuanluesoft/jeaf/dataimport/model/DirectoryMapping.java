package com.yuanluesoft.jeaf.dataimport.model;

/**
 * 
 * @author linchuan
 *
 */
public class DirectoryMapping {
	private String directoryId;
	private String directoryFullName;
	
	public DirectoryMapping(String directoryId, String directoryFullName) {
		super();
		this.directoryId = directoryId;
		this.directoryFullName = directoryFullName;
	}
	/**
	 * @return the directoryFullName
	 */
	public String getDirectoryFullName() {
		return directoryFullName;
	}
	/**
	 * @param directoryFullName the directoryFullName to set
	 */
	public void setDirectoryFullName(String directoryFullName) {
		this.directoryFullName = directoryFullName;
	}
	/**
	 * @return the directoryId
	 */
	public String getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(String directoryId) {
		this.directoryId = directoryId;
	}
}

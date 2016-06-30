package com.yuanluesoft.j2oa.dispatch.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class FilingConfig extends ActionForm {
	private char toArchives = '0'; //是否归档到文书档案
	private char toDatabank = '0'; //是否归档到资料库
	private long directoryId; //资料库归档目录ID
	private char createDirectoryByYear = '0'; //是否自动按年创建目录
	
	private String directoryName; //目录名称

	/**
	 * @return Returns the createDirectoryByYear.
	 */
	public char getCreateDirectoryByYear() {
		return createDirectoryByYear;
	}
	/**
	 * @param createDirectoryByYear The createDirectoryByYear to set.
	 */
	public void setCreateDirectoryByYear(char createDirectoryByYear) {
		this.createDirectoryByYear = createDirectoryByYear;
	}
	/**
	 * @return Returns the directoryId.
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId The directoryId to set.
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return Returns the toArchives.
	 */
	public char getToArchives() {
		return toArchives;
	}
	/**
	 * @param toArchives The toArchives to set.
	 */
	public void setToArchives(char toArchives) {
		this.toArchives = toArchives;
	}
	/**
	 * @return Returns the toDatabank.
	 */
	public char getToDatabank() {
		return toDatabank;
	}
	/**
	 * @param toDatabank The toDatabank to set.
	 */
	public void setToDatabank(char toDatabank) {
		this.toDatabank = toDatabank;
	}
	/**
	 * @return Returns the directoryName.
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName The directoryName to set.
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
}
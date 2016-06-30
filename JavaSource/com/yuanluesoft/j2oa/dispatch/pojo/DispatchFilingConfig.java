/*
 * Created on 2007-1-4
 *
 */
package com.yuanluesoft.j2oa.dispatch.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 发文归档配置(dispatch_config_filing)
 * @author linchuan
 *
 */
public class DispatchFilingConfig extends Record {
	private char toArchives = '0'; //是否归档到文书档案
	private char toDatabank = '0'; //是否归档到资料库
	private long directoryId; //资料库归档目录ID
	private char createDirectoryByYear = '0'; //是否自动按年创建目录
	/**
	 * @return the createDirectoryByYear
	 */
	public char getCreateDirectoryByYear() {
		return createDirectoryByYear;
	}
	/**
	 * @param createDirectoryByYear the createDirectoryByYear to set
	 */
	public void setCreateDirectoryByYear(char createDirectoryByYear) {
		this.createDirectoryByYear = createDirectoryByYear;
	}
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
	 * @return the toArchives
	 */
	public char getToArchives() {
		return toArchives;
	}
	/**
	 * @param toArchives the toArchives to set
	 */
	public void setToArchives(char toArchives) {
		this.toArchives = toArchives;
	}
	/**
	 * @return the toDatabank
	 */
	public char getToDatabank() {
		return toDatabank;
	}
	/**
	 * @param toDatabank the toDatabank to set
	 */
	public void setToDatabank(char toDatabank) {
		this.toDatabank = toDatabank;
	}
}

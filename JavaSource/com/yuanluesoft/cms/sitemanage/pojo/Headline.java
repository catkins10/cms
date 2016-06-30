package com.yuanluesoft.cms.sitemanage.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 站点管理:头版头条(cms_headline)
 * @author linchuan
 *
 */
public class Headline extends Record {
	private long directoryId; //目录ID
	private String headlineName; //名称
	private String headlineURL; //链接地址
	private String summarize; //概述
	private Timestamp lastModified; //最后修改时间
	
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
	 * @return the headlineName
	 */
	public String getHeadlineName() {
		return headlineName;
	}
	/**
	 * @param headlineName the headlineName to set
	 */
	public void setHeadlineName(String headlineName) {
		this.headlineName = headlineName;
	}
	/**
	 * @return the headlineURL
	 */
	public String getHeadlineURL() {
		return headlineURL;
	}
	/**
	 * @param headlineURL the headlineURL to set
	 */
	public void setHeadlineURL(String headlineURL) {
		this.headlineURL = headlineURL;
	}
	/**
	 * @return the summarize
	 */
	public String getSummarize() {
		return summarize;
	}
	/**
	 * @param summarize the summarize to set
	 */
	public void setSummarize(String summarize) {
		this.summarize = summarize;
	}
	/**
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
}

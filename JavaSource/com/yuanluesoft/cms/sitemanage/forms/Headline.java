package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Headline extends ActionForm {
	private String directoryIds; //参数:目录ID列表
	private String headlineName; //参数:名称
	private String headlineURL; //参数:链接地址
	private String summarize; //参数:概述
	
	private String[] selectedDirectoryIds; //选中的目录ID
	
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
	 * @return the directoryIds
	 */
	public String getDirectoryIds() {
		return directoryIds;
	}
	/**
	 * @param directoryIds the directoryIds to set
	 */
	public void setDirectoryIds(String directoryIds) {
		this.directoryIds = directoryIds;
	}
	/**
	 * @return the selectedDirectoryIds
	 */
	public String[] getSelectedDirectoryIds() {
		return selectedDirectoryIds;
	}
	/**
	 * @param selectedDirectoryIds the selectedDirectoryIds to set
	 */
	public void setSelectedDirectoryIds(String[] selectedDirectoryIds) {
		this.selectedDirectoryIds = selectedDirectoryIds;
	}
}
package com.yuanluesoft.cms.sitemanage.model;

/**
 * 
 * @author linchuan
 *
 */
public class Headline {
	private String headlineName; //名称
	private String headlineURL; //链接地址
	private String summarize; //概述
	private String imageURL; //图片URL
	
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
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}
	/**
	 * @param imageURL the imageURL to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
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
}

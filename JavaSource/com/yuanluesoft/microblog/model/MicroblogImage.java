package com.yuanluesoft.microblog.model;

import java.io.Serializable;

/**
 * 微博配图
 * @author linchuan
 *
 */
public class MicroblogImage implements Serializable, Cloneable {
	private String thumbnailUrl; //缩略图片地址 
	private String middleUrl; //中等尺寸图片地址 
	private String originalUrl; //原始图片地址 
	
	/**
	 * @return the middleUrl
	 */
	public String getMiddleUrl() {
		return middleUrl;
	}
	/**
	 * @param middleUrl the middleUrl to set
	 */
	public void setMiddleUrl(String middleUrl) {
		this.middleUrl = middleUrl;
	}
	/**
	 * @return the originalUrl
	 */
	public String getOriginalUrl() {
		return originalUrl;
	}
	/**
	 * @param originalUrl the originalUrl to set
	 */
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	/**
	 * @return the thumbnailUrl
	 */
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	/**
	 * @param thumbnailUrl the thumbnailUrl to set
	 */
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
}
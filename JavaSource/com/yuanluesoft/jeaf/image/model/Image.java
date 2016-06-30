package com.yuanluesoft.jeaf.image.model;

import java.io.Serializable;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;

/**
 * 
 * @author linchuan
 *
 */
public class Image extends Attachment implements Serializable {
	private int width; //图片宽度
	private int height; //图片高度
	private String url; //图像URL
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#getDescription()
	 */
	public String getDescription() {
		return getName() + "\r\n" +
			   "尺寸：" + width +"×" + height + "\r\n" +
			   "大小：" + getFileSize();
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#getUrlAttachment()
	 */
	public String getUrlAttachment() {
		return url;
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#getUrlInline()
	 */
	public String getUrlInline() {
		return url;
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#setUrlAttachment(java.lang.String)
	 */
	public void setUrlAttachment(String urlAttachment) {
		this.url = urlAttachment;
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#setUrlInline(java.lang.String)
	 */
	public void setUrlInline(String urlInline) {
		this.url = urlInline;
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}

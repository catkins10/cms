package com.yuanluesoft.jeaf.htmlparser.model;

/**
 * 
 * @author linchuan
 *
 */
public class HTMLBodyInfo {
	private int imageCount; //图片数量
	private String firstImageName; //第一个图片的名称
	private int videoCount; //视频数量
	private String firstVideoName; //第一个视频名称
	private int attachmentCount; //附件数量
	private boolean bodyChanged; //分析过程中是否对正文做了调整
	private String newBody; //调整后新的正文
	
	/**
	 * @return the attachmentCount
	 */
	public int getAttachmentCount() {
		return attachmentCount;
	}
	/**
	 * @param attachmentCount the attachmentCount to set
	 */
	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}
	/**
	 * @return the bodyChanged
	 */
	public boolean isBodyChanged() {
		return bodyChanged;
	}
	/**
	 * @param bodyChanged the bodyChanged to set
	 */
	public void setBodyChanged(boolean bodyChanged) {
		this.bodyChanged = bodyChanged;
	}
	/**
	 * @return the firstImageName
	 */
	public String getFirstImageName() {
		return firstImageName;
	}
	/**
	 * @param firstImageName the firstImageName to set
	 */
	public void setFirstImageName(String firstImageName) {
		this.firstImageName = firstImageName;
	}
	/**
	 * @return the firstVideoName
	 */
	public String getFirstVideoName() {
		return firstVideoName;
	}
	/**
	 * @param firstVideoName the firstVideoName to set
	 */
	public void setFirstVideoName(String firstVideoName) {
		this.firstVideoName = firstVideoName;
	}
	/**
	 * @return the imageCount
	 */
	public int getImageCount() {
		return imageCount;
	}
	/**
	 * @param imageCount the imageCount to set
	 */
	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}
	/**
	 * @return the newBody
	 */
	public String getNewBody() {
		return newBody;
	}
	/**
	 * @param newBody the newBody to set
	 */
	public void setNewBody(String newBody) {
		this.newBody = newBody;
	}
	/**
	 * @return the videoCount
	 */
	public int getVideoCount() {
		return videoCount;
	}
	/**
	 * @param videoCount the videoCount to set
	 */
	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}
}
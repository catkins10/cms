package com.yuanluesoft.cms.templatemanage.model;

/**
 * 链接打开方式
 * @author linchuan
 *
 */
public class LinkOpenMode {
	private String openMode; //链接方式
	private String linkDialogWidth; //对话框宽度
	private String linkDialogHeight; //对话框高度
	
	/**
	 * @return the linkDialogHeight
	 */
	public String getLinkDialogHeight() {
		return linkDialogHeight;
	}
	/**
	 * @param linkDialogHeight the linkDialogHeight to set
	 */
	public void setLinkDialogHeight(String linkDialogHeight) {
		this.linkDialogHeight = linkDialogHeight;
	}
	/**
	 * @return the linkDialogWidth
	 */
	public String getLinkDialogWidth() {
		return linkDialogWidth;
	}
	/**
	 * @param linkDialogWidth the linkDialogWidth to set
	 */
	public void setLinkDialogWidth(String linkDialogWidth) {
		this.linkDialogWidth = linkDialogWidth;
	}
	/**
	 * @return the openMode
	 */
	public String getOpenMode() {
		return openMode;
	}
	/**
	 * @param openMode the openMode to set
	 */
	public void setOpenMode(String openMode) {
		this.openMode = openMode;
	}
}
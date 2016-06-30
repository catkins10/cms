package com.yuanluesoft.im.webim.model;

/**
 * 
 * @author linchuan
 *
 */
public class WebimAction {
	private String action; //按钮名称
	private String dialogWidth; //对话框宽度
	private String dialogHeight; //对话框高度
	private String dialogAlign; //对话框对齐方式
	private String selectedStyle; //样式:选中
	private String mouseoverStyle; //样式:鼠标经过
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the dialogAlign
	 */
	public String getDialogAlign() {
		return dialogAlign;
	}
	/**
	 * @param dialogAlign the dialogAlign to set
	 */
	public void setDialogAlign(String dialogAlign) {
		this.dialogAlign = dialogAlign;
	}
	/**
	 * @return the dialogHeight
	 */
	public String getDialogHeight() {
		return dialogHeight;
	}
	/**
	 * @param dialogHeight the dialogHeight to set
	 */
	public void setDialogHeight(String dialogHeight) {
		this.dialogHeight = dialogHeight;
	}
	/**
	 * @return the dialogWidth
	 */
	public String getDialogWidth() {
		return dialogWidth;
	}
	/**
	 * @param dialogWidth the dialogWidth to set
	 */
	public void setDialogWidth(String dialogWidth) {
		this.dialogWidth = dialogWidth;
	}
	/**
	 * @return the mouseoverStyle
	 */
	public String getMouseoverStyle() {
		return mouseoverStyle;
	}
	/**
	 * @param mouseoverStyle the mouseoverStyle to set
	 */
	public void setMouseoverStyle(String mouseoverStyle) {
		this.mouseoverStyle = mouseoverStyle;
	}
	/**
	 * @return the selectedStyle
	 */
	public String getSelectedStyle() {
		return selectedStyle;
	}
	/**
	 * @param selectedStyle the selectedStyle to set
	 */
	public void setSelectedStyle(String selectedStyle) {
		this.selectedStyle = selectedStyle;
	}
}
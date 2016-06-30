package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:HTML编辑器
 * @author linchuan
 *
 */
public class HtmlEditorParameter {
	private String attachmentSelector; //附件选择对话框URL, 默认为selectAttachment.shtml
	private String height; //高度,默认100%
	private String commandSet; //命令集合名称,后台默认为standard,前台默认为outer
	
	/**
	 * @return the attachmentSelector
	 */
	public String getAttachmentSelector() {
		return attachmentSelector;
	}
	/**
	 * @param attachmentSelector the attachmentSelector to set
	 */
	public void setAttachmentSelector(String attachmentSelector) {
		this.attachmentSelector = attachmentSelector;
	}
	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}
	/**
	 * @return the commandSet
	 */
	public String getCommandSet() {
		return commandSet;
	}
	/**
	 * @param commandSet the commandSet to set
	 */
	public void setCommandSet(String commandSet) {
		this.commandSet = commandSet;
	}
}
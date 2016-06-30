package com.yuanluesoft.cms.pagebuilder.model.tabstrip;

/**
 * TAB选项列表
 * @author linchuan
 *
 */
public class TabstripButtonList extends TabstripButton {
	private String applicationName; //应用名称
	private String recordListName; //记录列表名称
	private int buttonCount; //选项数量
	private int buttonSpacing; //选项间隔
	private String tabstripButton;
	private String extendProperties; //拓展属性
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the buttonCount
	 */
	public int getButtonCount() {
		return buttonCount;
	}
	/**
	 * @param buttonCount the buttonCount to set
	 */
	public void setButtonCount(int buttonCount) {
		this.buttonCount = buttonCount;
	}
	/**
	 * @return the buttonFormat
	 */
	public String getTabstripButton() {
		return tabstripButton;
	}
	/**
	 * @param buttonFormat the buttonFormat to set
	 */
	public void setTabstripButton(String buttonFormat) {
		this.tabstripButton = buttonFormat;
	}
	/**
	 * @return the buttonSpacing
	 */
	public int getButtonSpacing() {
		return buttonSpacing;
	}
	/**
	 * @param buttonSpacing the buttonSpacing to set
	 */
	public void setButtonSpacing(int buttonSpacing) {
		this.buttonSpacing = buttonSpacing;
	}
	/**
	 * @return the recordListName
	 */
	public String getRecordListName() {
		return recordListName;
	}
	/**
	 * @param recordListName the recordListName to set
	 */
	public void setRecordListName(String recordListName) {
		this.recordListName = recordListName;
	}
	/**
	 * @return the extendProperties
	 */
	public String getExtendProperties() {
		return extendProperties;
	}
	/**
	 * @param extendProperties the extendProperties to set
	 */
	public void setExtendProperties(String extendProperties) {
		this.extendProperties = extendProperties;
	}
}
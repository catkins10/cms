package com.yuanluesoft.cms.pagebuilder.model.navigator;

/**
 * 导航栏项目列表
 * @author linchuan
 *
 */
public class NavigatorItemList {
	private String extendProperties; //记录列表扩展属性
	private String applicationName; //应用名称
	private String recordListName; //记录列表名称
	private String linkOpenMode; //链接打开方式
	private String linkTitle; //链接的标题
	private int itemCount; //显示的项目数
	private int itemSpacing; //项目间隔
	private String itemFormat; //显示的条目格式
	private String linkItemStyle; //导航栏链接项目：未选中
    private String linkItemSelectedStyle; //导航栏链接项目：选中
	private String menuItemStyle; //导航栏菜单项目：未选中
	private String menuItemSelectedStyle; //导航栏菜单项目：选中
	private String menuItemDropdownStyle; //导航栏菜单项目：未选中时的下拉按钮
	private String menuItemSelectedDropdownStyle; //导航栏菜单项目：选中时的下拉按钮
	
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
	/**
	 * @return the itemCount
	 */
	public int getItemCount() {
		return itemCount;
	}
	/**
	 * @param itemCount the itemCount to set
	 */
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	/**
	 * @return the itemFormat
	 */
	public String getItemFormat() {
		return itemFormat;
	}
	/**
	 * @param itemFormat the itemFormat to set
	 */
	public void setItemFormat(String itemFormat) {
		this.itemFormat = itemFormat;
	}
	/**
	 * @return the itemSpacing
	 */
	public int getItemSpacing() {
		return itemSpacing;
	}
	/**
	 * @param itemSpacing the itemSpacing to set
	 */
	public void setItemSpacing(int itemSpacing) {
		this.itemSpacing = itemSpacing;
	}
	/**
	 * @return the linkItemSelectedStyle
	 */
	public String getLinkItemSelectedStyle() {
		return linkItemSelectedStyle;
	}
	/**
	 * @param linkItemSelectedStyle the linkItemSelectedStyle to set
	 */
	public void setLinkItemSelectedStyle(String linkItemSelectedStyle) {
		this.linkItemSelectedStyle = linkItemSelectedStyle;
	}
	/**
	 * @return the linkItemStyle
	 */
	public String getLinkItemStyle() {
		return linkItemStyle;
	}
	/**
	 * @param linkItemStyle the linkItemStyle to set
	 */
	public void setLinkItemStyle(String linkItemStyle) {
		this.linkItemStyle = linkItemStyle;
	}
	/**
	 * @return the linkOpenMode
	 */
	public String getLinkOpenMode() {
		return linkOpenMode;
	}
	/**
	 * @param linkOpenMode the linkOpenMode to set
	 */
	public void setLinkOpenMode(String linkOpenMode) {
		this.linkOpenMode = linkOpenMode;
	}
	/**
	 * @return the linkTitle
	 */
	public String getLinkTitle() {
		return linkTitle;
	}
	/**
	 * @param linkTitle the linkTitle to set
	 */
	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}
	/**
	 * @return the menuItemDropdownStyle
	 */
	public String getMenuItemDropdownStyle() {
		return menuItemDropdownStyle;
	}
	/**
	 * @param menuItemDropdownStyle the menuItemDropdownStyle to set
	 */
	public void setMenuItemDropdownStyle(String menuItemDropdownStyle) {
		this.menuItemDropdownStyle = menuItemDropdownStyle;
	}
	/**
	 * @return the menuItemSelectedDropdownStyle
	 */
	public String getMenuItemSelectedDropdownStyle() {
		return menuItemSelectedDropdownStyle;
	}
	/**
	 * @param menuItemSelectedDropdownStyle the menuItemSelectedDropdownStyle to set
	 */
	public void setMenuItemSelectedDropdownStyle(
			String menuItemSelectedDropdownStyle) {
		this.menuItemSelectedDropdownStyle = menuItemSelectedDropdownStyle;
	}
	/**
	 * @return the menuItemSelectedStyle
	 */
	public String getMenuItemSelectedStyle() {
		return menuItemSelectedStyle;
	}
	/**
	 * @param menuItemSelectedStyle the menuItemSelectedStyle to set
	 */
	public void setMenuItemSelectedStyle(String menuItemSelectedStyle) {
		this.menuItemSelectedStyle = menuItemSelectedStyle;
	}
	/**
	 * @return the menuItemStyle
	 */
	public String getMenuItemStyle() {
		return menuItemStyle;
	}
	/**
	 * @param menuItemStyle the menuItemStyle to set
	 */
	public void setMenuItemStyle(String menuItemStyle) {
		this.menuItemStyle = menuItemStyle;
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
}
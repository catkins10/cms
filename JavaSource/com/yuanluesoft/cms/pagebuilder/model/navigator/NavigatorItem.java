package com.yuanluesoft.cms.pagebuilder.model.navigator;

import java.util.List;

/**
 * 导航栏项目
 * @author linchuan
 *
 */
public class NavigatorItem {
	private String linkItemStyle; //导航栏链接项目：未选中
    private String linkItemSelectedStyle; //导航栏链接项目：选中
	private String menuItemStyle; //导航栏菜单项目：未选中
	private String menuItemSelectedStyle; //导航栏菜单项目：选中
	private String menuItemDropdownStyle; //导航栏菜单项目：未选中时的下拉按钮
	private String menuItemSelectedDropdownStyle; //导航栏菜单项目：选中时的下拉按钮
	private List menuItems; //菜单列表

	public NavigatorItem() {
		super();
	}

	public NavigatorItem(String linkItemStyle, String linkItemSelectedStyle, String menuItemStyle, String menuItemSelectedStyle, String menuItemDropdownStyle, String menuItemSelectedDropdownStyle, List menuItems) {
		super();
		this.linkItemStyle = linkItemStyle;
		this.linkItemSelectedStyle = linkItemSelectedStyle;
		this.menuItemStyle = menuItemStyle;
		this.menuItemSelectedStyle = menuItemSelectedStyle;
		this.menuItemDropdownStyle = menuItemDropdownStyle;
		this.menuItemSelectedDropdownStyle = menuItemSelectedDropdownStyle;
		this.menuItems = menuItems;
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
	 * @return the menuItems
	 */
	public List getMenuItems() {
		return menuItems;
	}

	/**
	 * @param menuItems the menuItems to set
	 */
	public void setMenuItems(List menuItems) {
		this.menuItems = menuItems;
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
}
package com.yuanluesoft.cms.pagebuilder.model.navigator;

/**
 * 导航栏
 * @author linchuan
 *
 */
public class Navigator {
	private String name; //名称
	private String type; //类型,horizontalMenuTop/horizontalMenuBottom/horizontalLineTop/horizontalLineBottom/verticalPopupLeft/verticalPopupRight//verticalLineLeft/verticalLineRight/verticalDropdown
	private String linkItemStyle; //导航栏链接项目：未选中
    private String linkItemSelectedStyle; //导航栏链接项目：选中
	private String menuItemStyle; //导航栏菜单项目：未选中
	private String menuItemSelectedStyle; //导航栏菜单项目：选中
	private String menuItemDropdownStyle; //导航栏菜单项目：未选中时的下拉按钮
	private String menuItemSelectedDropdownStyle; //导航栏菜单项目：选中时的下拉按钮
	private String popupMenuStyle; //菜单
	private String popupMenuItemStyle; //菜单条目：未选中
	private String popupMenuItemSelectedStyle; //菜单条目：选中private String 
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the popupMenuItemSelectedStyle
	 */
	public String getPopupMenuItemSelectedStyle() {
		return popupMenuItemSelectedStyle;
	}
	/**
	 * @param popupMenuItemSelectedStyle the popupMenuItemSelectedStyle to set
	 */
	public void setPopupMenuItemSelectedStyle(String popupMenuItemSelectedStyle) {
		this.popupMenuItemSelectedStyle = popupMenuItemSelectedStyle;
	}
	/**
	 * @return the popupMenuItemStyle
	 */
	public String getPopupMenuItemStyle() {
		return popupMenuItemStyle;
	}
	/**
	 * @param popupMenuItemStyle the popupMenuItemStyle to set
	 */
	public void setPopupMenuItemStyle(String popupMenuItemStyle) {
		this.popupMenuItemStyle = popupMenuItemStyle;
	}
	/**
	 * @return the popupMenuStyle
	 */
	public String getPopupMenuStyle() {
		return popupMenuStyle;
	}
	/**
	 * @param popupMenuStyle the popupMenuStyle to set
	 */
	public void setPopupMenuStyle(String popupMenuStyle) {
		this.popupMenuStyle = popupMenuStyle;
	}
}
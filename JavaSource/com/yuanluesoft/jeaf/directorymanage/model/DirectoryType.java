package com.yuanluesoft.jeaf.directorymanage.model;

/**
 * 目录类型
 * @author linchuan
 *
 */
public class DirectoryType {
	private String type; //类型
	private String parentTypes; //父目录类型列表,用逗号分隔
	private String title; //标题
	private Class directoryClass; //对应的类
	private String icon; //图标,如: /jeaf/usermanage/icons/department.gif
	private String expandIcon; //展开时的图标, 允许为null
	private boolean navigatorIndent; //应用导航:如果用户没有上级目录权限,是否缩进
	
	/**
	 * 目录类型
	 * @param type 类型
	 * @param parentTypes 父目录类型列表,用逗号分隔
	 * @param title 标题
	 * @param directoryClass 对应的类
	 * @param icon 图标,如: /jeaf/usermanage/icons/department.gif
	 * @param expandIcon 展开时的图标, 允许为null
	 * @param navigatorIndent 应用导航:如果用户没有上级目录权限,是否缩进
	 */
	public DirectoryType(String type, String parentTypes, String title, Class directoryClass, String icon, String expandIcon, boolean navigatorIndent) {
		super();
		this.type = type;
		this.parentTypes = parentTypes;
		this.title = title;
		this.directoryClass = directoryClass;
		this.icon = icon;
		this.expandIcon = expandIcon;
		this.navigatorIndent = navigatorIndent;
	}
	
	/**
	 * @return the expandIcon
	 */
	public String getExpandIcon() {
		return expandIcon;
	}
	/**
	 * @param expandIcon the expandIcon to set
	 */
	public void setExpandIcon(String expandIcon) {
		this.expandIcon = expandIcon;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the directoryClass
	 */
	public Class getDirectoryClass() {
		return directoryClass;
	}

	/**
	 * @param directoryClass the directoryClass to set
	 */
	public void setDirectoryClass(Class directoryClass) {
		this.directoryClass = directoryClass;
	}

	/**
	 * @return the navigatorIndent
	 */
	public boolean isNavigatorIndent() {
		return navigatorIndent;
	}

	/**
	 * @param navigatorIndent the navigatorIndent to set
	 */
	public void setNavigatorIndent(boolean navigatorIndent) {
		this.navigatorIndent = navigatorIndent;
	}

	/**
	 * @return the parentTypes
	 */
	public String getParentTypes() {
		return parentTypes;
	}

	/**
	 * @param parentTypes the parentTypes to set
	 */
	public void setParentTypes(String parentTypes) {
		this.parentTypes = parentTypes;
	}
}

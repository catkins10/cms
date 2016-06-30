package com.yuanluesoft.jeaf.directorymanage.model;

/**
 * 目录权限
 * @author linchuan
 *
 */
public class DirectoryPopedomType {
	public final static int INHERIT_FROM_PARENT_NO = 0; //不继承
	public final static int INHERIT_FROM_PARENT_ALWAYS = 1; //总是继承
	public final static int INHERIT_FROM_PARENT_WHEN_EMPTY = 2; //当自己没有配置权限时从上级目录继承
	
	private String name; //类型
	private String title; //标题
	private String directoryTypes; //对应的目录类型
	private int inheritFromParent; //是否允许从上级目录继承, 0/不继承,1/继承,2/当自己没有配置权限时从上级目录继承
	private boolean keepMyselfPopedom; //当用户把自己的权限删除时,是否需要自动恢复
	private boolean navigatorFilter; //导航是否需要过滤
	
	/**
	 * 目前权限类型
	 * @param name 类型
	 * @param title 标题
	 * @param directoryTypes 对应的目录类型,all表示全部
	 * @param inheritFromParent 是否允许从上级目录继承,INHERIT_FROM_PARENT_NO/INHERIT_FROM_PARENT_ALWAYS/INHERIT_FROM_PARENT_WHEN_EMPTY
	 * @param keepMyselfPopedom 当用户把自己的权限删除时,是否需要自动恢复
	 * @param navigatorFilter 导航是否需要过滤
	 */
	public DirectoryPopedomType(String name, String title, String directoryTypes, int inheritFromParent, boolean keepMyselfPopedom, boolean navigatorFilter) {
		super();
		this.name = name;
		this.title = title;
		this.directoryTypes = directoryTypes;
		this.inheritFromParent = inheritFromParent;
		this.keepMyselfPopedom = keepMyselfPopedom;
		this.navigatorFilter = navigatorFilter;
	}
	/**
	 * @return the inheritFromParent
	 */
	public int isInheritFromParent() {
		return inheritFromParent;
	}
	/**
	 * @param inheritFromParent the inheritFromParent to set
	 */
	public void setInheritFromParent(int inheritFromParent) {
		this.inheritFromParent = inheritFromParent;
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
	 * @return the keepMyselfPopedom
	 */
	public boolean isKeepMyselfPopedom() {
		return keepMyselfPopedom;
	}
	/**
	 * @param keepMyselfPopedom the keepMyselfPopedom to set
	 */
	public void setKeepMyselfPopedom(boolean keepMyselfPopedom) {
		this.keepMyselfPopedom = keepMyselfPopedom;
	}
	/**
	 * @return the navigatorFilter
	 */
	public boolean isNavigatorFilter() {
		return navigatorFilter;
	}
	/**
	 * @param navigatorFilter the navigatorFilter to set
	 */
	public void setNavigatorFilter(boolean navigatorFilter) {
		this.navigatorFilter = navigatorFilter;
	}
	/**
	 * @return the directoryTypes
	 */
	public String getDirectoryTypes() {
		return directoryTypes;
	}
	/**
	 * @param directoryTypes the directoryTypes to set
	 */
	public void setDirectoryTypes(String directoryTypes) {
		this.directoryTypes = directoryTypes;
	}
}
package com.yuanluesoft.cms.pagebuilder.model.tabstrip;

/**
 * TAB标签页
 * @author linchuan
 *
 */
public class TabstripBody {
	private String name; //名称
	private String width; //宽度
	private String height; //高度
	private String switchMode; //切换方式
	private boolean timeSwitch; //是否定时切换
	private int timeInterval; //时间间隔
	private boolean clickOpenMore; //单击时是否打开“更多”
	
	/**
	 * @return the clickOpenMore
	 */
	public boolean isClickOpenMore() {
		return clickOpenMore;
	}
	/**
	 * @param clickOpenMore the clickOpenMore to set
	 */
	public void setClickOpenMore(boolean clickOpenMore) {
		this.clickOpenMore = clickOpenMore;
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
	 * @return the switchMode
	 */
	public String getSwitchMode() {
		return switchMode;
	}
	/**
	 * @param switchMode the switchMode to set
	 */
	public void setSwitchMode(String switchMode) {
		this.switchMode = switchMode;
	}
	/**
	 * @return the timeInterval
	 */
	public int getTimeInterval() {
		return timeInterval;
	}
	/**
	 * @param timeInterval the timeInterval to set
	 */
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return the timeSwitch
	 */
	public boolean isTimeSwitch() {
		return timeSwitch;
	}
	/**
	 * @param timeSwitch the timeSwitch to set
	 */
	public void setTimeSwitch(boolean timeSwitch) {
		this.timeSwitch = timeSwitch;
	}
}
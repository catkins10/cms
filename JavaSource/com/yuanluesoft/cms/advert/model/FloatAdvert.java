package com.yuanluesoft.cms.advert.model;

import java.io.Serializable;

/**
 * 浮动广告
 * @author linchuan
 *
 */
public class FloatAdvert implements Serializable {
	private long advertPutId; //投放ID
	private String mode; //投放方式,static/绝对位置,popup/弹出窗口,固定在窗口指定位置(absoluteLeft/左,absoluteRight/右,absoluteTop/上,absoluteBottom/下,absoluteLeftTop/左上,absoluteRightTop/右上,absoluteLeftBottom/左下,absoluteRightBottom/右下),fly/全屏滚动
	private int x; //水平边距,可以是绝对坐标和相对坐标(按窗口大小)
	private int y; //垂直边距,可以是绝对坐标和相对坐标(按窗口大小)
	private double speed; //移动速度,像数/秒
	private int displaySeconds; //显示时长,以秒为单位
	private int loadMode; //加载方式
	private int hideMode; //隐藏方式
	private String content; //广告内容HTML
	private String minimizeContent; //最小化时HTML
	private String width; //宽度
	private String height; //高度
	private String minimizeWidth; //最小化时宽度
	private String minimizeHeight; //最小化时高度
	private String href; //链接地址
	
	/**
	 * @return the advertPutId
	 */
	public long getAdvertPutId() {
		return advertPutId;
	}
	/**
	 * @param advertPutId the advertPutId to set
	 */
	public void setAdvertPutId(long advertPutId) {
		this.advertPutId = advertPutId;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the displaySeconds
	 */
	public int getDisplaySeconds() {
		return displaySeconds;
	}
	/**
	 * @param displaySeconds the displaySeconds to set
	 */
	public void setDisplaySeconds(int displaySeconds) {
		this.displaySeconds = displaySeconds;
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
	 * @return the hideMode
	 */
	public int getHideMode() {
		return hideMode;
	}
	/**
	 * @param hideMode the hideMode to set
	 */
	public void setHideMode(int hideMode) {
		this.hideMode = hideMode;
	}
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}
	/**
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}
	/**
	 * @return the loadMode
	 */
	public int getLoadMode() {
		return loadMode;
	}
	/**
	 * @param loadMode the loadMode to set
	 */
	public void setLoadMode(int loadMode) {
		this.loadMode = loadMode;
	}
	/**
	 * @return the minimizeContent
	 */
	public String getMinimizeContent() {
		return minimizeContent;
	}
	/**
	 * @param minimizeContent the minimizeContent to set
	 */
	public void setMinimizeContent(String minimizeContent) {
		this.minimizeContent = minimizeContent;
	}
	/**
	 * @return the minimizeHeight
	 */
	public String getMinimizeHeight() {
		return minimizeHeight;
	}
	/**
	 * @param minimizeHeight the minimizeHeight to set
	 */
	public void setMinimizeHeight(String minimizeHeight) {
		this.minimizeHeight = minimizeHeight;
	}
	/**
	 * @return the minimizeWidth
	 */
	public String getMinimizeWidth() {
		return minimizeWidth;
	}
	/**
	 * @param minimizeWidth the minimizeWidth to set
	 */
	public void setMinimizeWidth(String minimizeWidth) {
		this.minimizeWidth = minimizeWidth;
	}
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
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
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
}
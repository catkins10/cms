package com.yuanluesoft.cms.advert.pojo;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 广告管理:浮动广告投放页面(advert_put_page)
 * @author linchuan
 *
 */
public class AdvertPutPage extends Record {
	private long spaceId; //广告位ID
	private String siteIds; //站点/栏目ID
	private String siteNames; //站点/栏目名称
	private int containChildSite; //是否包含子站/子栏目
	private String pageNames; //页面名称
	private String pageTitles; //页面标题
	private String clientTypes; //客户端类型
	private String mode; //投放方式,static/绝对位置,popup/弹出窗口,固定在窗口指定位置(absoluteLeft/左,absoluteRight/右,absoluteTop/上,absoluteBottom/下,absoluteLeftTop/左上,absoluteRightTop/右上,absoluteLeftBottom/左下,absoluteRightBottom/右下),fly/全屏滚动
	private int x; //水平边距,可以是绝对坐标和相对坐标(按窗口大小)
	private int y; //垂直边距,可以是绝对坐标和相对坐标(按窗口大小)
	private double speed; //移动速度,像数/毫秒
	private int displaySeconds; //显示时长,以秒为单位
	private int loadMode; //加载方式
	private int hideMode; //隐藏方式

	/**
	 * 获取客户端类型,数组形式
	 * @return
	 */
	public String[] getClientTypeArray() {
		return clientTypes==null || clientTypes.isEmpty() ? null : clientTypes.split(",");
	}
	
	/**
	 * 设置客户端类型,数组形式
	 * @param clientTypeArray
	 */
	public void setClientTypeArray(String[] clientTypeArray) {
		if(clientTypeArray==null || clientTypeArray.length==0) {
			clientTypes = null;
		}
		else {
			clientTypes = ListUtils.join(clientTypeArray, ",", false);
		}
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
	 * @return the siteIds
	 */
	public String getSiteIds() {
		return siteIds;
	}
	/**
	 * @param siteIds the siteIds to set
	 */
	public void setSiteIds(String siteIds) {
		this.siteIds = siteIds;
	}
	/**
	 * @return the siteNames
	 */
	public String getSiteNames() {
		return siteNames;
	}
	/**
	 * @param siteNames the siteNames to set
	 */
	public void setSiteNames(String siteNames) {
		this.siteNames = siteNames;
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
	/**
	 * @return the spaceId
	 */
	public long getSpaceId() {
		return spaceId;
	}
	/**
	 * @param spaceId the spaceId to set
	 */
	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * @return the containChildSite
	 */
	public int getContainChildSite() {
		return containChildSite;
	}
	/**
	 * @param containChildSite the containChildSite to set
	 */
	public void setContainChildSite(int containChildSite) {
		this.containChildSite = containChildSite;
	}
	/**
	 * @return the pageNames
	 */
	public String getPageNames() {
		return pageNames;
	}
	/**
	 * @param pageNames the pageNames to set
	 */
	public void setPageNames(String pageNames) {
		this.pageNames = pageNames;
	}
	/**
	 * @return the pageTitles
	 */
	public String getPageTitles() {
		return pageTitles;
	}
	/**
	 * @param pageTitles the pageTitles to set
	 */
	public void setPageTitles(String pageTitles) {
		this.pageTitles = pageTitles;
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
	 * @return the clientTypes
	 */
	public String getClientTypes() {
		return clientTypes;
	}
	/**
	 * @param clientTypes the clientTypes to set
	 */
	public void setClientTypes(String clientTypes) {
		this.clientTypes = clientTypes;
	}
}
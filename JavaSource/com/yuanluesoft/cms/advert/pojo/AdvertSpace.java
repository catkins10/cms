package com.yuanluesoft.cms.advert.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 广告管理:广告位(advert_space)
 * @author linchuan
 *
 */
public class AdvertSpace extends Record {
	private long siteId; //站点ID
	private String name; //名称
	private int isFloat; //是否浮动广告,浮动/固定位置(在页面模板中插入)
	private String freeContent; //无广告时HTML,固定位置时有效，如：虚位以待
	private String width; //宽度,px/pt/%
	private String height; //高度,px/pt/%
	private String price; //参考单价
	private Timestamp created; //创建时间
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Set adverts; //广告列表
	private Set putPages; //投放页面列表
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the freeContent
	 */
	public String getFreeContent() {
		return freeContent;
	}
	/**
	 * @param freeContent the freeContent to set
	 */
	public void setFreeContent(String freeContent) {
		this.freeContent = freeContent;
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
	 * @return the isFloat
	 */
	public int getIsFloat() {
		return isFloat;
	}
	/**
	 * @param isFloat the isFloat to set
	 */
	public void setIsFloat(int isFloat) {
		this.isFloat = isFloat;
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
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
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
	 * @return the adverts
	 */
	public Set getAdverts() {
		return adverts;
	}
	/**
	 * @param adverts the adverts to set
	 */
	public void setAdverts(Set adverts) {
		this.adverts = adverts;
	}
	/**
	 * @return the putPages
	 */
	public Set getPutPages() {
		return putPages;
	}
	/**
	 * @param putPages the putPages to set
	 */
	public void setPutPages(Set putPages) {
		this.putPages = putPages;
	}
}
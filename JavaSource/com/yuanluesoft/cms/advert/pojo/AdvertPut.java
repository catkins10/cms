package com.yuanluesoft.cms.advert.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 广告管理:广告投放(advert_put)
 * @author linchuan
 *
 */
public class AdvertPut extends Record {
	private long advertId; //广告ID
	private long siteId; //站点ID
	private String advertName; //广告名称
	private String spaceName; //广告位名称
	private String customerName; //客户名称
	private Timestamp beginTime; //投放开始时间
	private Timestamp endTime; //投放结束时间
	private Timestamp factEndTime; //实际结束时间
	private double price; //价格
	private int accessTimes; //点击次数
	private Timestamp created; //登记时间
	private String creator; //登记人
	private long creatorId; //创建人ID
	
	/**
	 * 获取结束时间
	 * @return
	 */
	public Timestamp getEnd() {
		return factEndTime==null ? endTime : factEndTime;
	}
	
	/**
	 * @return the accessTimes
	 */
	public int getAccessTimes() {
		return accessTimes;
	}
	/**
	 * @param accessTimes the accessTimes to set
	 */
	public void setAccessTimes(int accessTimes) {
		this.accessTimes = accessTimes;
	}
	/**
	 * @return the advertId
	 */
	public long getAdvertId() {
		return advertId;
	}
	/**
	 * @param advertId the advertId to set
	 */
	public void setAdvertId(long advertId) {
		this.advertId = advertId;
	}
	/**
	 * @return the advertName
	 */
	public String getAdvertName() {
		return advertName;
	}
	/**
	 * @param advertName the advertName to set
	 */
	public void setAdvertName(String advertName) {
		this.advertName = advertName;
	}
	/**
	 * @return the beginTime
	 */
	public Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
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
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
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
	 * @return the spaceName
	 */
	public String getSpaceName() {
		return spaceName;
	}
	/**
	 * @param spaceName the spaceName to set
	 */
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	/**
	 * @return the factEndTime
	 */
	public Timestamp getFactEndTime() {
		return factEndTime;
	}
	/**
	 * @param factEndTime the factEndTime to set
	 */
	public void setFactEndTime(Timestamp factEndTime) {
		this.factEndTime = factEndTime;
	}
}
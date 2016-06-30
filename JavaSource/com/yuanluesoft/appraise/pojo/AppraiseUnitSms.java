package com.yuanluesoft.appraise.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评议管理:短信通知(appraise_unit_sms)
 * @author linchuan
 *
 */
public class AppraiseUnitSms extends Record {
	private long areaId; //地区ID
	private String area; //地区
	private String unitIds; //接收单位ID列表
	private String unitNames; //接收单位列表
	private String smsContent; //短信内容
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private Timestamp sendTime; //发送时间
	
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
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
	 * @return the sendTime
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the smsContent
	 */
	public String getSmsContent() {
		return smsContent;
	}
	/**
	 * @param smsContent the smsContent to set
	 */
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	/**
	 * @return the unitIds
	 */
	public String getUnitIds() {
		return unitIds;
	}
	/**
	 * @param unitIds the unitIds to set
	 */
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	/**
	 * @return the unitNames
	 */
	public String getUnitNames() {
		return unitNames;
	}
	/**
	 * @param unitNames the unitNames to set
	 */
	public void setUnitNames(String unitNames) {
		this.unitNames = unitNames;
	}
}
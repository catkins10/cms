package com.yuanluesoft.traffic.busline.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公交线路:变更通知(traffic_busline_change)
 * @author linchuan
 *
 */
public class BusLineChange extends Record {
	private long busLineId; //公交线路ID
	private String content; //变更说明
	private char interim = '0'; //是否临时变更
	private Date beginDate; //开始时间
	private Date endDate; //结束时间
	private long creatorId; //发布人ID
	private String creator; //发布人姓名
	private Timestamp created; //发布时间
	
	/**
	 * 是否临时变更
	 * @return
	 */
	public String getIsTnterim() {
		return interim=='1' ? "√" : "";
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
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
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the interim
	 */
	public char getInterim() {
		return interim;
	}

	/**
	 * @param interim the interim to set
	 */
	public void setInterim(char interim) {
		this.interim = interim;
	}

	/**
	 * @return the busLineId
	 */
	public long getBusLineId() {
		return busLineId;
	}

	/**
	 * @param busLineId the busLineId to set
	 */
	public void setBusLineId(long busLineId) {
		this.busLineId = busLineId;
	}
}
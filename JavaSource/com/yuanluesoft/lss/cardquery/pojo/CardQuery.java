/**
 * 
 */
package com.yuanluesoft.lss.cardquery.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * @author kangshiwei
 *社保制卡查询
 */
public class CardQuery extends Record {

	private String name; //姓名
	private int sex; //性别
	private String securityNumber; //社会保障号
	private String batchNumber; //批号
	private String jurisdiction; //参保辖区
	private String cardType; //制卡类型
	private Date receiveDate; //接收数据日期
	private Date makeCardDate; //制卡日期
	private Date removedCardDate; //移出卡片日期
	private String mark;//备注
	private String creator; //登记人
	private long creatorId; //登记人ID
	private Timestamp created; //登记时间
	
	
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public String getJurisdiction() {
		return jurisdiction;
	}
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}
	public Date getMakeCardDate() {
		return makeCardDate;
	}
	public void setMakeCardDate(Date makeCardDate) {
		this.makeCardDate = makeCardDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public Date getRemovedCardDate() {
		return removedCardDate;
	}
	public void setRemovedCardDate(Date removedCardDate) {
		this.removedCardDate = removedCardDate;
	}
	public String getSecurityNumber() {
		return securityNumber;
	}
	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}

	
	
}

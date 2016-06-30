package com.yuanluesoft.credit.enterprisecredit.municipal.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author zyh
 *
 */
public class Municipal extends ActionForm {
	private String name;// 企业名称 -->
	private String filingTime;// 立案时间 -->
	private String breakLaw;// 违法行为 -->
	private String basics;// 处罚法律依据 -->
	private String bookNum;// 决定书文号 -->
	private String bookTime;// 下达决定书时间 -->
	private String money;//处罚金额 -->
	private String deparment;// 办理机关 -->
	private String endTime;// 结案时间 -->
	private String remark;// 备注 -->
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getBasics() {
		return basics;
	}
	public void setBasics(String basics) {
		this.basics = basics;
	}
	public String getBookNum() {
		return bookNum;
	}
	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}
	public String getBookTime() {
		return bookTime;
	}
	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
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
	public String getDeparment() {
		return deparment;
	}
	public void setDeparment(String deparment) {
		this.deparment = deparment;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getFilingTime() {
		return filingTime;
	}
	public void setFilingTime(String filingTime) {
		this.filingTime = filingTime;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBreakLaw() {
		return breakLaw;
	}
	public void setBreakLaw(String breakLaw) {
		this.breakLaw = breakLaw;
	}
	
	
	
}
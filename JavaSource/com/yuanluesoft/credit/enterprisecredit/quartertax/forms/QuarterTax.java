package com.yuanluesoft.credit.enterprisecredit.quartertax.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author zyh
 *
 */
public class QuarterTax extends ActionForm {
	private String nuit;//单位
	private String oweTime;//欠税时间
	private String department ; //基层管征单位
	private String enterprise; //企业或单位名称
	private String number;//纳税人识别号
	private String person;//法定代表人或负责人姓名
	private String cardNum;//居民身份证或其他有效身份证件号码
	private String addr;//经营地点
	private String taxType;//欠税税种
	private String total;//累计欠税余额
	private String thisYear;//本年欠税余额
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public String getThisYear() {
		return thisYear;
	}
	public void setThisYear(String thisYear) {
		this.thisYear = thisYear;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getNuit() {
		return nuit;
	}
	public void setNuit(String nuit) {
		this.nuit = nuit;
	}
	public String getOweTime() {
		return oweTime;
	}
	public void setOweTime(String oweTime) {
		this.oweTime = oweTime;
	}

	
	
	
	
}
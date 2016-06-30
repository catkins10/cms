package com.yuanluesoft.credit.bank.searchloan.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 贷款信息(credit_bank_loan)
 * @author zyh
 *
 */
public class BankLoan extends Record {
	private String enterpriseName;//企业名称
	private long productId;//项目ID
	private String productName; //贷款项目
	private double mony;//贷款金额（万元）
	private Timestamp time;//贷款发放时间
	private Timestamp issueTime;//发布时间
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
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
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public double getMony() {
		return mony;
	}
	public void setMony(double mony) {
		this.mony = mony;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Timestamp getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
	}
	
	
	
	
	
}
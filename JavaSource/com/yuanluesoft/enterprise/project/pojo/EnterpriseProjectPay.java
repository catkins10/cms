package com.yuanluesoft.enterprise.project.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:付款(enterprise_project_pay)
 * @author linchuan
 *
 */
public class EnterpriseProjectPay extends Record {
	private long projectId; //项目ID
	private String clauseName; //款项名称
	private double accountReceivable; //按合同进度应到款数
	private Date payDate; //付款时间
	private double invoiceAmount; //票额
	private Date billingReceiveDate; //收票时间
	private String receiver; //接收人
	private char isPaid = '0'; //是否已付款
	
	/**
	 * 获取付款说明
	 * @return
	 */
	public String getPaidStatus() {
		return isPaid=='1' ? "√" : "";
	}
	
	/**
	 * @return the accountReceivable
	 */
	public double getAccountReceivable() {
		return accountReceivable;
	}
	/**
	 * @param accountReceivable the accountReceivable to set
	 */
	public void setAccountReceivable(double accountReceivable) {
		this.accountReceivable = accountReceivable;
	}
	/**
	 * @return the billingReceiveDate
	 */
	public Date getBillingReceiveDate() {
		return billingReceiveDate;
	}
	/**
	 * @param billingReceiveDate the billingReceiveDate to set
	 */
	public void setBillingReceiveDate(Date billingReceiveDate) {
		this.billingReceiveDate = billingReceiveDate;
	}
	/**
	 * @return the clauseName
	 */
	public String getClauseName() {
		return clauseName;
	}
	/**
	 * @param clauseName the clauseName to set
	 */
	public void setClauseName(String clauseName) {
		this.clauseName = clauseName;
	}
	/**
	 * @return the invoiceAmount
	 */
	public double getInvoiceAmount() {
		return invoiceAmount;
	}
	/**
	 * @param invoiceAmount the invoiceAmount to set
	 */
	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	/**
	 * @return the isPaid
	 */
	public char getIsPaid() {
		return isPaid;
	}
	/**
	 * @param isPaid the isPaid to set
	 */
	public void setIsPaid(char isPaid) {
		this.isPaid = isPaid;
	}
	/**
	 * @return the payDate
	 */
	public Date getPayDate() {
		return payDate;
	}
	/**
	 * @param payDate the payDate to set
	 */
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
}

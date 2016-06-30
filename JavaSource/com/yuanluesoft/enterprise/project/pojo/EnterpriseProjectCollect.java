package com.yuanluesoft.enterprise.project.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 项目管理:收款(enterprise_project_collect)
 * @author linchuan
 *
 */
public class EnterpriseProjectCollect extends Record {
	private long projectId; //项目ID
	private long contractId; //合同ID
	private String clauseName; //款项名称
	private double accountReceivable; //按合同进度应到款数
	private double invoiceAmount; //开票金额（元）
	private Date billingDate; //开票时间
	private double receiveAmount; //到款金额（元）
	private Date receiveDate; //到款时间
	private String remark; //备注
	
	private EnterpriseProject project; //项目
	private EnterpriseProjectContract contract; //合同
	
	/**
	 * 获取开票月份
	 * @return
	 */
	public String getBillingMonth() {
		return StringUtils.getChineseNumber(DateTimeUtils.getMonth(billingDate) + 1, false) + "月";
	}
	
	/**
	 * 获取开票年度
	 * @return
	 */
	public String getBillingYear() {
		return DateTimeUtils.getYear(billingDate) + "年";
	}
	

	/**
	 * 获取开票月份
	 * @return
	 */
	public int getBillingMonthIntValue() {
		return DateTimeUtils.getMonth(billingDate) + 1;
	}
	
	/**
	 * 获取开票年度
	 * @return
	 */
	public int getBillingYearIntValue() {
		return DateTimeUtils.getYear(billingDate);
	}
	
	/**
	 * 获取未开盘金额
	 * @return
	 */
	public double getUninvoiceAmount() {
		return contract.getContractValue() - invoiceAmount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		EnterpriseProjectCollect collect = (EnterpriseProjectCollect)super.clone();
		if(project!=null) {
			collect.setProject((EnterpriseProject)project.clone());
		}
		if(contract!=null) {
			collect.setContract((EnterpriseProjectContract)contract.clone());
		}
		return collect;
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
	 * @return the billingDate
	 */
	public Date getBillingDate() {
		return billingDate;
	}
	/**
	 * @param billingDate the billingDate to set
	 */
	public void setBillingDate(Date billingDate) {
		this.billingDate = billingDate;
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
	 * @return the contractId
	 */
	public long getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(long contractId) {
		this.contractId = contractId;
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
	 * @return the receiveAmount
	 */
	public double getReceiveAmount() {
		return receiveAmount;
	}
	/**
	 * @param receiveAmount the receiveAmount to set
	 */
	public void setReceiveAmount(double receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
	/**
	 * @return the receiveDate
	 */
	public Date getReceiveDate() {
		return receiveDate;
	}
	/**
	 * @param receiveDate the receiveDate to set
	 */
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the contract
	 */
	public EnterpriseProjectContract getContract() {
		return contract;
	}
	/**
	 * @param contract the contract to set
	 */
	public void setContract(EnterpriseProjectContract contract) {
		this.contract = contract;
	}
	/**
	 * @return the project
	 */
	public EnterpriseProject getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(EnterpriseProject project) {
		this.project = project;
	}
}
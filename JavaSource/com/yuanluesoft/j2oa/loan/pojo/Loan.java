/*
 * Created on 2006-6-8
 *
 */
package com.yuanluesoft.j2oa.loan.pojo;

import java.sql.Date;
import java.util.Set;

import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 借款单(loan_loan)
 * @author linchuan
 *
 */
public class Loan extends WorkflowData {
	private long id; //ID
	private String loanPersonName; //借款人
	private long loanPersonId; //借款人ID
	private String loanDepartmentName; //借款人所在部门
	private long loanDepartmentId; //借款人所在部门ID
	private long projectId; //项目ID
	private String projectName; //项目名称
	private String type; //费用类别
	private String reason; //借款事由
	private double money; //借款金额
	private Date loanDate; //借款时间
	private Date intendingRepayDate; //预计还款时间
	private Date repayDate; //实际还款时间
	private String workflowInstanceId; //工作流实例ID
	private char prepaid = '0'; //是否支付
	private Date payDate; //支付时间
	private char repaid = '0'; //是否已还款
	private double reimburseMoney; //已核销金额
	private String remark; //备注
	
	private Set reimburseLoans;

 	/**
	 * 获取大写的金额
	 * @return
	 */
    public String getMoneyCapital() {
    	return StringUtils.getMoneyCapital(money, true);
    }
    
    /***
     * 获取大写的已核销金额
     * @return
     */
    public String getReimburseMoneyCapital() {
    	return StringUtils.getMoneyCapital(reimburseMoney, true);
    }
	
    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * @return Returns the projectId.
     */
    public long getProjectId() {
        return projectId;
    }
    /**
     * @param projectId The projectId to set.
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    /**
     * @return Returns the reason.
     */
    public java.lang.String getReason() {
        return reason;
    }
    /**
     * @param reason The reason to set.
     */
    public void setReason(java.lang.String reason) {
        this.reason = reason;
    }
    /**
     * @return Returns the workflowInstanceId.
     */
    public java.lang.String getWorkflowInstanceId() {
        return workflowInstanceId;
    }
    /**
     * @param workflowInstanceId The workflowInstanceId to set.
     */
    public void setWorkflowInstanceId(java.lang.String workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }
    /**
     * @return Returns the loanDepartmentId.
     */
    public long getLoanDepartmentId() {
        return loanDepartmentId;
    }
    /**
     * @param loanDepartmentId The loanDepartmentId to set.
     */
    public void setLoanDepartmentId(long loanDepartmentId) {
        this.loanDepartmentId = loanDepartmentId;
    }
    /**
     * @return Returns the loanPersonId.
     */
    public long getLoanPersonId() {
        return loanPersonId;
    }
    /**
     * @param loanPersonId The loanPersonId to set.
     */
    public void setLoanPersonId(long loanPersonId) {
        this.loanPersonId = loanPersonId;
    }
    /**
     * @return Returns the prepaid.
     */
    public char getPrepaid() {
        return prepaid;
    }
    /**
     * @param prepaid The prepaid to set.
     */
    public void setPrepaid(char prepaid) {
        this.prepaid = prepaid;
    }
    /**
     * @return Returns the repayDate.
     */
    public java.sql.Date getRepayDate() {
        return repayDate;
    }
    /**
     * @param repayDate The repayDate to set.
     */
    public void setRepayDate(java.sql.Date repayDate) {
        this.repayDate = repayDate;
    }
    /**
     * @return Returns the intendingRepayDate.
     */
    public java.sql.Date getIntendingRepayDate() {
        return intendingRepayDate;
    }
    /**
     * @param intendingRepayDate The intendingRepayDate to set.
     */
    public void setIntendingRepayDate(java.sql.Date intendingRepayDate) {
        this.intendingRepayDate = intendingRepayDate;
    }
    /**
     * @return Returns the loanDate.
     */
    public java.sql.Date getLoanDate() {
        return loanDate;
    }
    /**
     * @param loanDate The loanDate to set.
     */
    public void setLoanDate(java.sql.Date loanDate) {
        this.loanDate = loanDate;
    }
    /**
     * @return Returns the payDate.
     */
    public java.sql.Date getPayDate() {
        return payDate;
    }
    /**
     * @param payDate The payDate to set.
     */
    public void setPayDate(java.sql.Date payDate) {
        this.payDate = payDate;
    }
    /**
     * @return Returns the repaid.
     */
    public char getRepaid() {
        return repaid;
    }
    /**
     * @param repaid The repaid to set.
     */
    public void setRepaid(char repaid) {
        this.repaid = repaid;
    }
    /**
     * @return Returns the remark.
     */
    public java.lang.String getRemark() {
        return remark;
    }
    /**
     * @param remark The remark to set.
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }
    /**
     * @return Returns the type.
     */
    public java.lang.String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }
    /**
     * @return Returns the loanDepartmentName.
     */
    public java.lang.String getLoanDepartmentName() {
        return loanDepartmentName;
    }
    /**
     * @param loanDepartmentName The loanDepartmentName to set.
     */
    public void setLoanDepartmentName(java.lang.String loanDepartmentName) {
        this.loanDepartmentName = loanDepartmentName;
    }
    /**
     * @return Returns the loanPersonName.
     */
    public java.lang.String getLoanPersonName() {
        return loanPersonName;
    }
    /**
     * @param loanPersonName The loanPersonName to set.
     */
    public void setLoanPersonName(java.lang.String loanPersonName) {
        this.loanPersonName = loanPersonName;
    }
    /**
     * @return Returns the projectName.
     */
    public java.lang.String getProjectName() {
        return projectName;
    }
    /**
     * @param projectName The projectName to set.
     */
    public void setProjectName(java.lang.String projectName) {
        this.projectName = projectName;
    }
    /**
     * @return Returns the reimburseMoney.
     */
    public double getReimburseMoney() {
        return reimburseMoney;
    }
    /**
     * @param reimburseMoney The reimburseMoney to set.
     */
    public void setReimburseMoney(double reimburseMoney) {
        this.reimburseMoney = reimburseMoney;
    }
    /**
     * @return Returns the reimburseLoans.
     */
    public java.util.Set getReimburseLoans() {
        return reimburseLoans;
    }
    /**
     * @param reimburseLoans The reimburseLoans to set.
     */
    public void setReimburseLoans(java.util.Set reimburseLoans) {
        this.reimburseLoans = reimburseLoans;
    }
    /**
     * @return Returns the money.
     */
    public double getMoney() {
        return money;
    }
    /**
     * @param money The money to set.
     */
    public void setMoney(double money) {
        this.money = money;
    }
}

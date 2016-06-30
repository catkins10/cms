package com.yuanluesoft.j2oa.loan.forms;

import java.util.List;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class LoanForm extends WorkflowForm {
    private java.util.Set opinions;
	private long projectId;
	private double money;
	private double reimburseMoney;
	private java.lang.String reason;
	private java.lang.String workflowInstanceId;
	private long loanDepartmentId;
	private String loanPersonName;
	private String loanDepartmentName;
	private long loanPersonId;
	private char prepaid = '0';
	private java.sql.Date repayDate;
	private java.sql.Date intendingRepayDate;
	private java.sql.Date loanDate;
	private java.sql.Date payDate;
	private char repaid = '0';
	private String remark;
	private String type;
	private java.util.Set reimburseLoans;
	
	
	private String moneyCapital; //金额大写
	private String reimburseMoneyCapital; //已核销金额大写
	private List types;
	
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
     * @return Returns the loanDepartmentName.
     */
    public String getLoanDepartmentName() {
        return loanDepartmentName;
    }
    /**
     * @param loanDepartmentName The loanDepartmentName to set.
     */
    public void setLoanDepartmentName(String loanDepartmentName) {
        this.loanDepartmentName = loanDepartmentName;
    }
    /**
     * @return Returns the loanPersonName.
     */
    public String getLoanPersonName() {
        return loanPersonName;
    }
    /**
     * @param loanPersonName The loanPersonName to set.
     */
    public void setLoanPersonName(String loanPersonName) {
        this.loanPersonName = loanPersonName;
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
    /**
     * @return Returns the opinions.
     */
    public java.util.Set getOpinions() {
        return opinions;
    }
    /**
     * @param opinions The opinions to set.
     */
    public void setOpinions(java.util.Set opinions) {
        this.opinions = opinions;
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
    public String getRemark() {
        return remark;
    }
    /**
     * @param remark The remark to set.
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return Returns the types.
     */
    public List getTypes() {
        return types;
    }
    /**
     * @param types The types to set.
     */
    public void setTypes(List types) {
        this.types = types;
    }
    /**
     * @return Returns the moneyCapital.
     */
    public String getMoneyCapital() {
        return moneyCapital;
    }
    /**
     * @param moneyCapital The moneyCapital to set.
     */
    public void setMoneyCapital(String moneyCapital) {
        this.moneyCapital = moneyCapital;
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
     * @return Returns the reimburseMoneyCapital.
     */
    public String getReimburseMoneyCapital() {
        return reimburseMoneyCapital;
    }
    /**
     * @param reimburseMoneyCapital The reimburseMoneyCapital to set.
     */
    public void setReimburseMoneyCapital(String reimburseMoneyCapital) {
        this.reimburseMoneyCapital = reimburseMoneyCapital;
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
}
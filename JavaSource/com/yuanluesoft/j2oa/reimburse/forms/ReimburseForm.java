package com.yuanluesoft.j2oa.reimburse.forms;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class ReimburseForm extends WorkflowForm {
	private long projectId; //项目ID
	private String projectName; //项目名称
	private String personName; //报销人姓名
	private long personId; //报销人ID
	private String departmentName; //部门名称
	private long departmentId; //部门ID
	private String type; //报销类别
	private Date reimburseDate; //报销时间
	private String journey; //出差路线
	private int dayCount; //总天数
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //结束时间
	private String description; //事件描述
	private int billCount; //票据张数
	private double money; //总金额
	private double payMoney; //实际支付金额
	private char prepaid = '0'; //是否已支付
	private Date payDate; //支付时间
	private String remark; //备注
	private String workflowInstanceId; //工作流实例ID
	private java.util.Set charges;
	private java.util.Set reimburseLoans;
	
	private String moneyCapital; //金额大写
	private String payMoneyCapital; //实际支付金额大写
	private List types; //报销类别列表
	private String reimburseLoanMoney; //核销金额列表,格式：借款单ID1,核销金额1,...,借款单IDn,核销金额n
	
    /**
     * @return Returns the beginTime.
     */
    public java.sql.Timestamp getBeginTime() {
        return beginTime;
    }
    /**
     * @param beginTime The beginTime to set.
     */
    public void setBeginTime(java.sql.Timestamp beginTime) {
        this.beginTime = beginTime;
    }
    /**
     * @return Returns the billCount.
     */
    public int getBillCount() {
        return billCount;
    }
    /**
     * @param billCount The billCount to set.
     */
    public void setBillCount(int billCount) {
        this.billCount = billCount;
    }
    /**
     * @return Returns the departmentId.
     */
    public long getDepartmentId() {
        return departmentId;
    }
    /**
     * @param departmentId The departmentId to set.
     */
    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }
    /**
     * @return Returns the departmentName.
     */
    public java.lang.String getDepartmentName() {
        return departmentName;
    }
    /**
     * @param departmentName The departmentName to set.
     */
    public void setDepartmentName(java.lang.String departmentName) {
        this.departmentName = departmentName;
    }
    /**
     * @return Returns the description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    /**
     * @return Returns the endTime.
     */
    public java.sql.Timestamp getEndTime() {
        return endTime;
    }
    /**
     * @param endTime The endTime to set.
     */
    public void setEndTime(java.sql.Timestamp endTime) {
        this.endTime = endTime;
    }
    /**
     * @return Returns the journey.
     */
    public java.lang.String getJourney() {
        return journey;
    }
    /**
     * @param journey The journey to set.
     */
    public void setJourney(java.lang.String journey) {
        this.journey = journey;
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
     * @return Returns the personId.
     */
    public long getPersonId() {
        return personId;
    }
    /**
     * @param personId The personId to set.
     */
    public void setPersonId(long personId) {
        this.personId = personId;
    }
    /**
     * @return Returns the personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    /**
     * @param personName The personName to set.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
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
     * @return Returns the reimburseDate.
     */
    public java.sql.Date getReimburseDate() {
        return reimburseDate;
    }
    /**
     * @param reimburseDate The reimburseDate to set.
     */
    public void setReimburseDate(java.sql.Date reimburseDate) {
        this.reimburseDate = reimburseDate;
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
     * @return Returns the dayCount.
     */
    public int getDayCount() {
        return dayCount;
    }
    /**
     * @param dayCount The dayCount to set.
     */
    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }
    /**
     * @return Returns the reimburseLoans.
     */
    public Set getReimburseLoans() {
        return reimburseLoans;
    }
    /**
     * @param reimburseLoans The reimburseLoans to set.
     */
    public void setReimburseLoans(Set reimburseLoans) {
        this.reimburseLoans = reimburseLoans;
    }
    /**
     * @return Returns the payMoney.
     */
    public double getPayMoney() {
        return payMoney;
    }
    /**
     * @param payMoney The payMoney to set.
     */
    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }
    /**
     * @return Returns the payMoneyCapital.
     */
    public String getPayMoneyCapital() {
        return payMoneyCapital;
    }
    /**
     * @param payMoneyCapital The payMoneyCapital to set.
     */
    public void setPayMoneyCapital(String payMoneyCapital) {
        this.payMoneyCapital = payMoneyCapital;
    }
    /**
     * @return Returns the reimburseLoanMoney.
     */
    public String getReimburseLoanMoney() {
        return reimburseLoanMoney;
    }
    /**
     * @param reimburseLoanMoney The reimburseLoanMoney to set.
     */
    public void setReimburseLoanMoney(String reimburseLoanMoney) {
        this.reimburseLoanMoney = reimburseLoanMoney;
    }
	/**
	 * @return the charges
	 */
	public java.util.Set getCharges() {
		return charges;
	}
	/**
	 * @param charges the charges to set
	 */
	public void setCharges(java.util.Set charges) {
		this.charges = charges;
	}
}
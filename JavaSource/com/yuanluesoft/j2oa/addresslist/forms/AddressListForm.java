/*
 * Created on 2006-5-25
 *
 */
package com.yuanluesoft.j2oa.addresslist.forms;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 *
 * @author linchuan
 *
 */
public class AddressListForm extends ActionForm {
	private String name;
	private String category;
	private long creatorId;
	private String email;
	private char isPersonal = '0';
	private String mobile;
	private String birthday;
	private String companyAddress;
	private String companyName;
	private String companyPostalcode;
	private String companyTel;
	private String department;
	private String fax;
	private String homeAddress;
	private String homepage;
	private String homePostalcode;
	private String homeTel;
	private String job;
	private String msn;
	private String qq;
	private String remark;
	
	//来往记录
	private java.util.Set logs;
	private long logId; //来往记录ID
	private Date logTime; //来往记录内容
	private String logContent; //来往记录内容
	
	private boolean common; //是否公共通讯录
	
    /**
     * @return Returns the birthday.
     */
    public String getBirthday() {
        return birthday;
    }
    /**
     * @param birthday The birthday to set.
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    /**
     * @return Returns the category.
     */
    public String getCategory() {
        return category;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }
    /**
     * @return Returns the companyAddress.
     */
    public String getCompanyAddress() {
        return companyAddress;
    }
    /**
     * @param companyAddress The companyAddress to set.
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
    /**
     * @return Returns the companyName.
     */
    public String getCompanyName() {
        return companyName;
    }
    /**
     * @param companyName The companyName to set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    /**
     * @return Returns the companyPostalcode.
     */
    public String getCompanyPostalcode() {
        return companyPostalcode;
    }
    /**
     * @param companyPostalcode The companyPostalcode to set.
     */
    public void setCompanyPostalcode(String companyPostalcode) {
        this.companyPostalcode = companyPostalcode;
    }
    /**
     * @return Returns the companyTel.
     */
    public String getCompanyTel() {
        return companyTel;
    }
    /**
     * @param companyTel The companyTel to set.
     */
    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }
    /**
     * @return Returns the creatorId.
     */
    public long getCreatorId() {
        return creatorId;
    }
    /**
     * @param creatorId The creatorId to set.
     */
    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
    /**
     * @return Returns the department.
     */
    public String getDepartment() {
        return department;
    }
    /**
     * @param department The department to set.
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return Returns the fax.
     */
    public String getFax() {
        return fax;
    }
    /**
     * @param fax The fax to set.
     */
    public void setFax(String fax) {
        this.fax = fax;
    }
    /**
     * @return Returns the homeAddress.
     */
    public String getHomeAddress() {
        return homeAddress;
    }
    /**
     * @param homeAddress The homeAddress to set.
     */
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
    /**
     * @return Returns the homepage.
     */
    public String getHomepage() {
        return homepage;
    }
    /**
     * @param homepage The homepage to set.
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    /**
     * @return Returns the homePostalcode.
     */
    public String getHomePostalcode() {
        return homePostalcode;
    }
    /**
     * @param homePostalcode The homePostalcode to set.
     */
    public void setHomePostalcode(String homePostalcode) {
        this.homePostalcode = homePostalcode;
    }
    /**
     * @return Returns the homeTel.
     */
    public String getHomeTel() {
        return homeTel;
    }
    /**
     * @param homeTel The homeTel to set.
     */
    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
    }
    /**
     * @return Returns the isPersonal.
     */
    public char getIsPersonal() {
        return isPersonal;
    }
    /**
     * @param isPersonal The isPersonal to set.
     */
    public void setIsPersonal(char isPersonal) {
        this.isPersonal = isPersonal;
    }
    /**
     * @return Returns the job.
     */
    public String getJob() {
        return job;
    }
    /**
     * @param job The job to set.
     */
    public void setJob(String job) {
        this.job = job;
    }
    /**
     * @return Returns the logs.
     */
    public java.util.Set getLogs() {
        return logs;
    }
    /**
     * @param logs The logs to set.
     */
    public void setLogs(java.util.Set logs) {
        this.logs = logs;
    }
    /**
     * @return Returns the mobile.
     */
    public String getMobile() {
        return mobile;
    }
    /**
     * @param mobile The mobile to set.
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    /**
     * @return Returns the msn.
     */
    public String getMsn() {
        return msn;
    }
    /**
     * @param msn The msn to set.
     */
    public void setMsn(String msn) {
        this.msn = msn;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the qq.
     */
    public String getQq() {
        return qq;
    }
    /**
     * @param qq The qq to set.
     */
    public void setQq(String qq) {
        this.qq = qq;
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
     * @return Returns the common.
     */
    public boolean isCommon() {
        return common;
    }
    /**
     * @param common The common to set.
     */
    public void setCommon(boolean common) {
        this.common = common;
    }
    /**
     * @return Returns the logContent.
     */
    public String getLogContent() {
        return logContent;
    }
    /**
     * @param logContent The logContent to set.
     */
    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
    /**
     * @return Returns the logId.
     */
    public long getLogId() {
        return logId;
    }
    /**
     * @param logId The logId to set.
     */
    public void setLogId(long logId) {
        this.logId = logId;
    }
    /**
     * @return Returns the logTime.
     */
    public Date getLogTime() {
        return logTime;
    }
    /**
     * @param logTime The logTime to set.
     */
    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }
}

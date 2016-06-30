/*
 * Created on 2006-5-25
 *
 */
package com.yuanluesoft.j2oa.addresslist.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 通讯录(address_list)
 * @author linchuan
 *
 */
public class AddressList extends Record {
	private String category; //分类,通讯录类目包括：国际组织、常用网址、常用电话号码、单位内部各部门、下级单位、兄弟单位及其他有关单位的通讯信息等
	private String name; //名称(姓名)
	private String birthday; //生日
	private String mobile; //手机
	private String email; //E-Mail
	private String homeAddress; //住宅地址
	private String homePostalcode; //邮编
	private String homeTel; //住宅电话
	private String qq; //QQ
	private String msn; //MSN
	private String fax; //传真
	private String homepage; //网址
	private String companyName; //单位名称
	private String companyAddress; //单位地址
	private String companyPostalcode; //单位邮编
	private String department; //所在部门
	private String companyTel; //办公室电话
	private String job; //职务
	private String remark; //备注
	private char isPersonal = '0'; //是否个人通讯录
	private long creatorId; //登记人ID
	private java.util.Set logs;

    /**
     * @return Returns the name.
     */
    public java.lang.String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(java.lang.String name) {
        this.name = name;
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
     * @return Returns the email.
     */
    public java.lang.String getEmail() {
        return email;
    }
    /**
     * @param email The email to set.
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
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
     * @return Returns the mobile.
     */
    public java.lang.String getMobile() {
        return mobile;
    }
    /**
     * @param mobile The mobile to set.
     */
    public void setMobile(java.lang.String mobile) {
        this.mobile = mobile;
    }
    /**
     * @return Returns the birthday.
     */
    public java.lang.String getBirthday() {
        return birthday;
    }
    /**
     * @param birthday The birthday to set.
     */
    public void setBirthday(java.lang.String birthday) {
        this.birthday = birthday;
    }
    /**
     * @return Returns the companyAddress.
     */
    public java.lang.String getCompanyAddress() {
        return companyAddress;
    }
    /**
     * @param companyAddress The companyAddress to set.
     */
    public void setCompanyAddress(java.lang.String companyAddress) {
        this.companyAddress = companyAddress;
    }
    /**
     * @return Returns the companyName.
     */
    public java.lang.String getCompanyName() {
        return companyName;
    }
    /**
     * @param companyName The companyName to set.
     */
    public void setCompanyName(java.lang.String companyName) {
        this.companyName = companyName;
    }
    /**
     * @return Returns the companyPostalcode.
     */
    public java.lang.String getCompanyPostalcode() {
        return companyPostalcode;
    }
    /**
     * @param companyPostalcode The companyPostalcode to set.
     */
    public void setCompanyPostalcode(java.lang.String companyPostalcode) {
        this.companyPostalcode = companyPostalcode;
    }
    /**
     * @return Returns the companyTel.
     */
    public java.lang.String getCompanyTel() {
        return companyTel;
    }
    /**
     * @param companyTel The companyTel to set.
     */
    public void setCompanyTel(java.lang.String companyTel) {
        this.companyTel = companyTel;
    }
    /**
     * @return Returns the department.
     */
    public java.lang.String getDepartment() {
        return department;
    }
    /**
     * @param department The department to set.
     */
    public void setDepartment(java.lang.String department) {
        this.department = department;
    }
    /**
     * @return Returns the fax.
     */
    public java.lang.String getFax() {
        return fax;
    }
    /**
     * @param fax The fax to set.
     */
    public void setFax(java.lang.String fax) {
        this.fax = fax;
    }
    /**
     * @return Returns the homeAddress.
     */
    public java.lang.String getHomeAddress() {
        return homeAddress;
    }
    /**
     * @param homeAddress The homeAddress to set.
     */
    public void setHomeAddress(java.lang.String homeAddress) {
        this.homeAddress = homeAddress;
    }
    /**
     * @return Returns the homepage.
     */
    public java.lang.String getHomepage() {
        return homepage;
    }
    /**
     * @param homepage The homepage to set.
     */
    public void setHomepage(java.lang.String homepage) {
        this.homepage = homepage;
    }
    /**
     * @return Returns the homePostalcode.
     */
    public java.lang.String getHomePostalcode() {
        return homePostalcode;
    }
    /**
     * @param homePostalcode The homePostalcode to set.
     */
    public void setHomePostalcode(java.lang.String homePostalcode) {
        this.homePostalcode = homePostalcode;
    }
    /**
     * @return Returns the homeTel.
     */
    public java.lang.String getHomeTel() {
        return homeTel;
    }
    /**
     * @param homeTel The homeTel to set.
     */
    public void setHomeTel(java.lang.String homeTel) {
        this.homeTel = homeTel;
    }
    /**
     * @return Returns the job.
     */
    public java.lang.String getJob() {
        return job;
    }
    /**
     * @param job The job to set.
     */
    public void setJob(java.lang.String job) {
        this.job = job;
    }
    /**
     * @return Returns the msn.
     */
    public java.lang.String getMsn() {
        return msn;
    }
    /**
     * @param msn The msn to set.
     */
    public void setMsn(java.lang.String msn) {
        this.msn = msn;
    }
    /**
     * @return Returns the qq.
     */
    public java.lang.String getQq() {
        return qq;
    }
    /**
     * @param qq The qq to set.
     */
    public void setQq(java.lang.String qq) {
        this.qq = qq;
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
     * @return Returns the category.
     */
    public java.lang.String getCategory() {
        return category;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(java.lang.String category) {
        this.category = category;
    }
}

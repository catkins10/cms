package com.yuanluesoft.charge.servicemanage.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 计费系统:服务策略(charge_service_policy)
 * @author linchuan
 *
 */
public class ServicePolicy extends Record {
	private String serviceItem; //服务项目名称,如：学习评测、英语学习器
	private int policy; //策略,1/免费,2/在指定时间段内免费,3/试用指定天数
	private char studentEnabled = '0'; //是否面向学生
	private char teacherEnabled = '0'; //是否面向教师
	private char genearchEnabled = '0'; //是否面向家长
	private char employeeEnabled; //是否面向普通用户
	private char anonymousEnabled = '0'; //是否面向匿名用户
	private Date beginDate; //使用开始时间
	private Date endDate; //使用截止时间
	private int days; //试用天数
	private long creatorId; //创建人
	private String creator; //创建人
	private Timestamp created; //创建时间
	private String remark; //备注
	
	/**
	 * 获取策略标题
	 * @return
	 */
	public String getPloicyTitle() {
		String personTypes = null;
		if(studentEnabled=='1') {
			personTypes = "学生";
		}
		if(teacherEnabled=='1') {
			personTypes = (personTypes==null ? "" : personTypes + "/") + "教师";
		}
		if(genearchEnabled=='1') {
			personTypes = (personTypes==null ? "" : personTypes + "/") + "家长";
		}
		if(employeeEnabled=='1') {
			personTypes = (personTypes==null ? "" : personTypes + "/") + "工作人员";
		}
		if(anonymousEnabled=='1') {
			personTypes = (personTypes==null ? "" : personTypes + "/") + "匿名用户";
		}
		String title = null;
		switch(policy) {
		case 1:
			title = "免费";
			break;
		
		case 2:
			title = DateTimeUtils.formatDate(beginDate, null) + "~" + DateTimeUtils.formatDate(endDate, null) + "免费试用";
			break;
			
		case 3:
			title = "从第一次使用开始的" + days + "天内免费试用";
			break;
		}
		return title + "(" + personTypes + ")";
	}
	
	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the days
	 */
	public int getDays() {
		return days;
	}
	/**
	 * @param days the days to set
	 */
	public void setDays(int days) {
		this.days = days;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the policy
	 */
	public int getPolicy() {
		return policy;
	}
	/**
	 * @param policy the policy to set
	 */
	public void setPolicy(int policy) {
		this.policy = policy;
	}
	/**
	 * @return the serviceItem
	 */
	public String getServiceItem() {
		return serviceItem;
	}
	/**
	 * @param serviceItem the serviceItem to set
	 */
	public void setServiceItem(String serviceItem) {
		this.serviceItem = serviceItem;
	}

	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
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
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}

	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * @return the anonymousEnabled
	 */
	public char getAnonymousEnabled() {
		return anonymousEnabled;
	}

	/**
	 * @param anonymousEnabled the anonymousEnabled to set
	 */
	public void setAnonymousEnabled(char anonymousEnabled) {
		this.anonymousEnabled = anonymousEnabled;
	}

	/**
	 * @return the employeeEnabled
	 */
	public char getEmployeeEnabled() {
		return employeeEnabled;
	}

	/**
	 * @param employeeEnabled the employeeEnabled to set
	 */
	public void setEmployeeEnabled(char employeeEnabled) {
		this.employeeEnabled = employeeEnabled;
	}

	/**
	 * @return the genearchEnabled
	 */
	public char getGenearchEnabled() {
		return genearchEnabled;
	}

	/**
	 * @param genearchEnabled the genearchEnabled to set
	 */
	public void setGenearchEnabled(char genearchEnabled) {
		this.genearchEnabled = genearchEnabled;
	}

	/**
	 * @return the studentEnabled
	 */
	public char getStudentEnabled() {
		return studentEnabled;
	}

	/**
	 * @param studentEnabled the studentEnabled to set
	 */
	public void setStudentEnabled(char studentEnabled) {
		this.studentEnabled = studentEnabled;
	}

	/**
	 * @return the teacherEnabled
	 */
	public char getTeacherEnabled() {
		return teacherEnabled;
	}

	/**
	 * @param teacherEnabled the teacherEnabled to set
	 */
	public void setTeacherEnabled(char teacherEnabled) {
		this.teacherEnabled = teacherEnabled;
	}
}

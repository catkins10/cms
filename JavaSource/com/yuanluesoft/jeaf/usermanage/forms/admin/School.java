/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.forms.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;


/**
 * @author Administrator
 *
 *
 */
public class School extends Org {
	private String category; //学校类别
	private String managerName; //管理员姓名
	private List departmentNames;
	private char managerSex = 'M'; //管理员性别
	private String managerDepartment; //管理员所在部门
	private String managerMobile; //管理员手机
	private String managerTel; //管理员手机
	private String managerMail; //管理员邮件
	//注册部门
	private String[] registDepartmentNames;
	//注册班级
	private String[] registGradeNames;
	private String[] registGradeClassCounts;;
	private String[] registGradeEnrollYears;
	private String[] registLengthOfSchoolings;
	
	/* (non-Javadoc)
	 * @see org.apache.struts.validator.ValidatorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if(request.getParameterValues("department")!=null) {
			registDepartmentNames = request.getParameterValues("department");
		}
		if(request.getParameterValues("gradeName")!=null) {
			registGradeNames = request.getParameterValues("gradeName");
			registGradeClassCounts = request.getParameterValues("gradeClassCount");
			registGradeEnrollYears = request.getParameterValues("gradeEnrollYear");
			registLengthOfSchoolings = request.getParameterValues("lengthOfSchooling");
		}
		super.reset(mapping, request);
	}
	/**
	 * @return Returns the categoty.
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param categoty The categoty to set.
	 */
	public void setCategory(String categoty) {
		this.category = categoty;
	}
	/**
	 * @return the managerDepartment
	 */
	public String getManagerDepartment() {
		return managerDepartment;
	}
	/**
	 * @param managerDepartment the managerDepartment to set
	 */
	public void setManagerDepartment(String managerDepartment) {
		this.managerDepartment = managerDepartment;
	}
	/**
	 * @return the managerMail
	 */
	public String getManagerMail() {
		return managerMail;
	}
	/**
	 * @param managerMail the managerMail to set
	 */
	public void setManagerMail(String managerMail) {
		this.managerMail = managerMail;
	}
	/**
	 * @return the managerMobile
	 */
	public String getManagerMobile() {
		return managerMobile;
	}
	/**
	 * @param managerMobile the managerMobile to set
	 */
	public void setManagerMobile(String managerMobile) {
		this.managerMobile = managerMobile;
	}
	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}
	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	/**
	 * @return the managerSex
	 */
	public char getManagerSex() {
		return managerSex;
	}
	/**
	 * @param managerSex the managerSex to set
	 */
	public void setManagerSex(char managerSex) {
		this.managerSex = managerSex;
	}
	/**
	 * @return the managerTel
	 */
	public String getManagerTel() {
		return managerTel;
	}
	/**
	 * @param managerTel the managerTel to set
	 */
	public void setManagerTel(String managerTel) {
		this.managerTel = managerTel;
	}
	/**
	 * @return the departmentNames
	 */
	public List getDepartmentNames() {
		return departmentNames;
	}
	/**
	 * @param departmentNames the departmentNames to set
	 */
	public void setDepartmentNames(List departmentNames) {
		this.departmentNames = departmentNames;
	}
	/**
	 * @return the registDepartmentNames
	 */
	public String[] getRegistDepartmentNames() {
		return registDepartmentNames;
	}
	/**
	 * @param registDepartmentNames the registDepartmentNames to set
	 */
	public void setRegistDepartmentNames(String[] registDepartmentNames) {
		this.registDepartmentNames = registDepartmentNames;
	}
	/**
	 * @return the registGradeClassCounts
	 */
	public String[] getRegistGradeClassCounts() {
		return registGradeClassCounts;
	}
	/**
	 * @param registGradeClassCounts the registGradeClassCounts to set
	 */
	public void setRegistGradeClassCounts(String[] registGradeClassCounts) {
		this.registGradeClassCounts = registGradeClassCounts;
	}
	/**
	 * @return the registGradeEnrollYears
	 */
	public String[] getRegistGradeEnrollYears() {
		return registGradeEnrollYears;
	}
	/**
	 * @param registGradeEnrollYears the registGradeEnrollYears to set
	 */
	public void setRegistGradeEnrollYears(String[] registGradeEnrollYears) {
		this.registGradeEnrollYears = registGradeEnrollYears;
	}
	/**
	 * @return the registGradeNames
	 */
	public String[] getRegistGradeNames() {
		return registGradeNames;
	}
	/**
	 * @param registGradeNames the registGradeNames to set
	 */
	public void setRegistGradeNames(String[] registGradeNames) {
		this.registGradeNames = registGradeNames;
	}
	/**
	 * @return the registLengthOfSchoolings
	 */
	public String[] getRegistLengthOfSchoolings() {
		return registLengthOfSchoolings;
	}
	/**
	 * @param registLengthOfSchoolings the registLengthOfSchoolings to set
	 */
	public void setRegistLengthOfSchoolings(String[] registLengthOfSchoolings) {
		this.registLengthOfSchoolings = registLengthOfSchoolings;
	}
}

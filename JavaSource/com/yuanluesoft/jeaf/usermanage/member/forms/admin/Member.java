package com.yuanluesoft.jeaf.usermanage.member.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Member extends ActionForm {
	private String loginName; //用户名
	private String password; //密码
	private String email; //邮箱
	private String question; //密码问题
	private String answer; //问题答案
	private Timestamp registTime; //注册时间
	private String name; //真实姓名
	private char sex = 'M'; //性别
	private String area; //所属省份
	private String company; //工作单位/所在院校
	private String organization; //单位所属行业
	private String address; //地址
	private String postalcode; //邮编
	private String fax; //传真
	private String telephone; //联系电话
	private String cell; //手机
	private String department; //部门
	private String duty; //职务
	private String identityCardName; //证件名称
	private String identityCard; //证件号码
	private char hideDetail = '0'; //公开用户资料
	private char halt = '0'; //是否禁用
	private String portraitName; //使用的系统头像名称,如果有自定义,则以自定义的头像为准
	
	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the cell
	 */
	public String getCell() {
		return cell;
	}
	/**
	 * @param cell the cell to set
	 */
	public void setCell(String cell) {
		this.cell = cell;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * @return the duty
	 */
	public String getDuty() {
		return duty;
	}
	/**
	 * @param duty the duty to set
	 */
	public void setDuty(String duty) {
		this.duty = duty;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the halt
	 */
	public char getHalt() {
		return halt;
	}
	/**
	 * @param halt the halt to set
	 */
	public void setHalt(char halt) {
		this.halt = halt;
	}
	/**
	 * @return the hideDetail
	 */
	public char getHideDetail() {
		return hideDetail;
	}
	/**
	 * @param hideDetail the hideDetail to set
	 */
	public void setHideDetail(char hideDetail) {
		this.hideDetail = hideDetail;
	}
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}
	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the portraitName
	 */
	public String getPortraitName() {
		return portraitName;
	}
	/**
	 * @param portraitName the portraitName to set
	 */
	public void setPortraitName(String portraitName) {
		this.portraitName = portraitName;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the registTime
	 */
	public Timestamp getRegistTime() {
		return registTime;
	}
	/**
	 * @param registTime the registTime to set
	 */
	public void setRegistTime(Timestamp registTime) {
		this.registTime = registTime;
	}
	/**
	 * @return the sex
	 */
	public char getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(char sex) {
		this.sex = sex;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * @return the identityCard
	 */
	public String getIdentityCard() {
		return identityCard;
	}
	/**
	 * @param identityCard the identityCard to set
	 */
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	/**
	 * @return the identityCardName
	 */
	public String getIdentityCardName() {
		return identityCardName;
	}
	/**
	 * @param identityCardName the identityCardName to set
	 */
	public void setIdentityCardName(String identityCardName) {
		this.identityCardName = identityCardName;
	}
	/**
	 * @return the postalcode
	 */
	public String getPostalcode() {
		return postalcode;
	}
	/**
	 * @param postalcode the postalcode to set
	 */
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
}
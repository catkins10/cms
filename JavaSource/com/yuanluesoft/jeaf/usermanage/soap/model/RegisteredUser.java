package com.yuanluesoft.jeaf.usermanage.soap.model;

import java.io.Serializable;

/**
 * 网上注册用户
 * @author linchuan
 *
 */
public class RegisteredUser implements Serializable {
	private long id; //ID
	private String loginName; //用户名
	private String email; //邮箱
	private String name; //真实姓名
	private String area; //所属省份
	private String company; //工作单位/所在院校
	private String organization; //单位所属行业
	private String department; //部门
	private String duty; //职务
	private String telephone; //联系电话
	private String cell; //手机
	private int portraitWidth; //头像宽度
	private int portraitHeight; //头像高度
	private String portraitUrl; //头像URL
	
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
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the portraitHeight
	 */
	public int getPortraitHeight() {
		return portraitHeight;
	}
	/**
	 * @param portraitHeight the portraitHeight to set
	 */
	public void setPortraitHeight(int portraitHeight) {
		this.portraitHeight = portraitHeight;
	}
	/**
	 * @return the portraitUrl
	 */
	public String getPortraitUrl() {
		return portraitUrl;
	}
	/**
	 * @param portraitUrl the portraitUrl to set
	 */
	public void setPortraitUrl(String portraitUrl) {
		this.portraitUrl = portraitUrl;
	}
	/**
	 * @return the portraitWidth
	 */
	public int getPortraitWidth() {
		return portraitWidth;
	}
	/**
	 * @param portraitWidth the portraitWidth to set
	 */
	public void setPortraitWidth(int portraitWidth) {
		this.portraitWidth = portraitWidth;
	}
}
package com.yuanluesoft.jeaf.usermanage.soap.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class Person implements Serializable {
	private long id; //ID
	private String name; //用户名
	private String sex; //性别,男/女
	private String loginName; //登录用户名
	private String password; //密码,MD5加密
	private String mailAddress; //邮箱地址
	private String familyAddress; //家庭地址
	private String cell; //手机
	private String telOffice; //办公室电话
	private String telFamily; //家庭电话
	private String type; //用户类型,普通用户/教师/学生/学生家长
	private float priority; //优先级,用来做用户排序
	private String orgName; //所在组织机构
	private int portraitWidth; //头像宽度
	private int portraitHeight; //头像高度
	private String portraitUrl; //头像URL
	private String subjectionOrgIds; //用户隶属的组织机构ID列表
	private String subjectionRoleIds; //用户隶属的角色/岗位ID列表
	private String supervisorIds; //分管领导ID列表
	private String supervisorNames; //分管领导姓名列表
	
	/**
	 * @return the familyAddress
	 */
	public String getFamilyAddress() {
		return familyAddress;
	}
	/**
	 * @param familyAddress the familyAddress to set
	 */
	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
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
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
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
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the telFamily
	 */
	public String getTelFamily() {
		return telFamily;
	}
	/**
	 * @param telFamily the telFamily to set
	 */
	public void setTelFamily(String telFamily) {
		this.telFamily = telFamily;
	}
	/**
	 * @return the telOffice
	 */
	public String getTelOffice() {
		return telOffice;
	}
	/**
	 * @param telOffice the telOffice to set
	 */
	public void setTelOffice(String telOffice) {
		this.telOffice = telOffice;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the subjectionOrgIds
	 */
	public String getSubjectionOrgIds() {
		return subjectionOrgIds;
	}
	/**
	 * @param subjectionOrgIds the subjectionOrgIds to set
	 */
	public void setSubjectionOrgIds(String subjectionOrgIds) {
		this.subjectionOrgIds = subjectionOrgIds;
	}
	/**
	 * @return the subjectionRoleIds
	 */
	public String getSubjectionRoleIds() {
		return subjectionRoleIds;
	}
	/**
	 * @param subjectionRoleIds the subjectionRoleIds to set
	 */
	public void setSubjectionRoleIds(String subjectionRoleIds) {
		this.subjectionRoleIds = subjectionRoleIds;
	}
	/**
	 * @return the supervisorIds
	 */
	public String getSupervisorIds() {
		return supervisorIds;
	}
	/**
	 * @param supervisorIds the supervisorIds to set
	 */
	public void setSupervisorIds(String supervisorIds) {
		this.supervisorIds = supervisorIds;
	}
	/**
	 * @return the supervisorNames
	 */
	public String getSupervisorNames() {
		return supervisorNames;
	}
	/**
	 * @param supervisorNames the supervisorNames to set
	 */
	public void setSupervisorNames(String supervisorNames) {
		this.supervisorNames = supervisorNames;
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
	
}

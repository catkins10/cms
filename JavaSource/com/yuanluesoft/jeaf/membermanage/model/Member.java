package com.yuanluesoft.jeaf.membermanage.model;

import java.io.Serializable;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class Member implements Serializable {
	private long id; //ID
	private int memberType; //用户类型,PERSON_TYPE_EMPLOYEE/普通用户, PERSON_TYPE_TEACHER/教师, PERSON_TYPE_STUDENT/学生, PERSON_TYPE_GENEARCH/家长, PERSON_TYPE_OTHER/其他用户自定义类型
	private String name; //用户名
	private String loginName; //登录用户名
	private char sex; //性别,M/F
	private char halt = '0'; //是否停用
	private String mailAddress; //邮件地址
	private String tel; //电话
	private String fax; //传真
	private String mobile; //手机
	private String identityCardName; //证件名称
	private String identityCard; //证件号码
	private String company; //单位名称
	private String address; //证件号码
	private String postalcode; //邮编
	private String profileURL; //修改个人资料的链接地址
	private Record originalRecord; //原始记录
	
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
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	 * @return the originalRecord
	 */
	public Record getOriginalRecord() {
		return originalRecord;
	}
	/**
	 * @param originalRecord the originalRecord to set
	 */
	public void setOriginalRecord(Record originalRecord) {
		this.originalRecord = originalRecord;
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
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
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
	/**
	 * @return the profileURL
	 */
	public String getProfileURL() {
		return profileURL;
	}
	/**
	 * @param profileURL the profileURL to set
	 */
	public void setProfileURL(String profileURL) {
		this.profileURL = profileURL;
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
	 * @return the memberType
	 */
	public int getMemberType() {
		return memberType;
	}
	/**
	 * @param memberType the memberType to set
	 */
	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}
}
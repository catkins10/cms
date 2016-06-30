package com.yuanluesoft.fdi.customer.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 客户联系人(fdi_customer_contact)
 * @author linchuan
 *
 */
public class FdiCustomerContact extends Record {
	private long companyId; //客户单位ID
	private String companyName; //客户单位名称
	private String name; //中文姓名
	private String englishName; //英文姓名
	private char sex = 'M'; //性别
	private String address; //个人地址
	private String tel; //电话,含国别、地区号
	private String mobile; //手机
	private String fax; //传真,含国别、地区号
	private String email; //E-mail
	private String im; //QQ\MSN\微博
	private String post; //所在部门及职务
	private String remark; //个人备注,特点、兴趣、任期等
	private String discuss; //商谈事项
	private String source; //信息来源
	private String chinaContact; //最初中方联系人
	private Timestamp created; //收录时间
	private long creatorId; //录入者ID
	private String creator; //录入者
	private Set discusses; //往来情况列表
	
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
	 * @return the chinaContact
	 */
	public String getChinaContact() {
		return chinaContact;
	}
	/**
	 * @param chinaContact the chinaContact to set
	 */
	public void setChinaContact(String chinaContact) {
		this.chinaContact = chinaContact;
	}
	/**
	 * @return the companyId
	 */
	public long getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	 * @return the discuss
	 */
	public String getDiscuss() {
		return discuss;
	}
	/**
	 * @param discuss the discuss to set
	 */
	public void setDiscuss(String discuss) {
		this.discuss = discuss;
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
	 * @return the englishName
	 */
	public String getEnglishName() {
		return englishName;
	}
	/**
	 * @param englishName the englishName to set
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
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
	 * @return the im
	 */
	public String getIm() {
		return im;
	}
	/**
	 * @param im the im to set
	 */
	public void setIm(String im) {
		this.im = im;
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
	 * @return the post
	 */
	public String getPost() {
		return post;
	}
	/**
	 * @param post the post to set
	 */
	public void setPost(String post) {
		this.post = post;
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
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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
	 * @return the discusses
	 */
	public Set getDiscusses() {
		return discusses;
	}
	/**
	 * @param discusses the discusses to set
	 */
	public void setDiscusses(Set discusses) {
		this.discusses = discusses;
	}
}
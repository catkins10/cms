package com.yuanluesoft.credit.bank.bank.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author zyh
 *
 */
public class Bank extends ActionForm {
	private String loginName;//登录用户名
	private String password;//密码
	private String name; //名称
	private String introduction; //简介
	private String addr; //地址
	private String postcode; //邮编
	private String linkMan; //联系人
	private String email; //邮箱
	private String faxes; //传真
	private String tel; //电话
	private String logoImage;
	
	//授信（保险）部门
	private String person;//负责人
	private String operator;//经办人
	private String orgUrl;//网址
	private String orgEmail;//邮箱
	private String orgFaxes;//传真
	private String orgTel;//电话
	//举报投诉渠道
	private String complainUrl;//网址
	private String complainEmail;//邮箱
	private String complainFaxes;//传真
	private String complainTel;//电话
	private Set products;//金融产品
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getComplainEmail() {
		return complainEmail;
	}
	public void setComplainEmail(String complainEmail) {
		this.complainEmail = complainEmail;
	}
	public String getComplainFaxes() {
		return complainFaxes;
	}
	public void setComplainFaxes(String complainFaxes) {
		this.complainFaxes = complainFaxes;
	}
	public String getComplainTel() {
		return complainTel;
	}
	public void setComplainTel(String complainTel) {
		this.complainTel = complainTel;
	}
	public String getComplainUrl() {
		return complainUrl;
	}
	public void setComplainUrl(String complainUrl) {
		this.complainUrl = complainUrl;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFaxes() {
		return faxes;
	}
	public void setFaxes(String faxes) {
		this.faxes = faxes;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOrgEmail() {
		return orgEmail;
	}
	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}
	public String getOrgFaxes() {
		return orgFaxes;
	}
	public void setOrgFaxes(String orgFaxes) {
		this.orgFaxes = orgFaxes;
	}
	public String getOrgTel() {
		return orgTel;
	}
	public void setOrgTel(String orgTel) {
		this.orgTel = orgTel;
	}
	public String getOrgUrl() {
		return orgUrl;
	}
	public void setOrgUrl(String orgUrl) {
		this.orgUrl = orgUrl;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Set getProducts() {
		return products;
	}
	public void setProducts(Set products) {
		this.products = products;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}
	
	
}
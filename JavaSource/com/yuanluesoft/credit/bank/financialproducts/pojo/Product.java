package com.yuanluesoft.credit.bank.financialproducts.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 金融产品(credit_bank_product)
 * @author zyh
 *
 */
public class Product extends Record {
	private String name; //名称
	private long typeId;//
	private String type; //种类
	private String introduction; //简介
	private String forWho; //适用对象
	private String condition; //申请条件
	private String material;//申请材料
	private String maxMoney; //最高额度
	private String how; //申请流程
	private String linkMan; //联系人
	private String email;//邮箱
	private String faxes;//传真
	private String tel;//电话
	private Timestamp issueTime;//发布时间
	private String logoImage;
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
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
	public String getForWho() {
		return forWho;
	}
	public void setForWho(String forWho) {
		this.forWho = forWho;
	}
	public String getHow() {
		return how;
	}
	public void setHow(String how) {
		this.how = how;
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
	public String getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(String maxMoney) {
		this.maxMoney = maxMoney;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
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
	public Timestamp getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
	}
	public String getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	
	
	
	
}
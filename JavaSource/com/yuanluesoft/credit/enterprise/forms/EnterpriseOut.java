package com.yuanluesoft.credit.enterprise.forms;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 企业信息（内资）(credit_enterprise_out)
 * @author zyh
 *
 */
public class EnterpriseOut extends ActionForm {
	private String loginName;//登录用户名
	private String password;//密码
	
	private String registCode;//注册号
	private String creditCode;//统一社会信用代码
	private String name;//企业名称
	private String registType;//登记类型
	private String type;//企业类型
	private String country;//外企国别
	private String person;//法定代表人\负责人
	private double invest;//投资总额(万美元)
	private double worth;//注册资本(万美元)
	private double realWorth;//实收资本(万美元)
	
	private double outWorth;//外方认缴资本(万美元)
	private String tel;//联系电话
	private String linkMan;//公示联络员
	private String linkTel;//联络员电话
	private String doorType;//行业门类
	private String industry;//行业类别
	private String code;//行业代码
	private String businessScope;//经营范围
	private Date startDate;//成立日期
	private String limitDate;//营业期限
	
	private Date approvalDate;//核准日期
	private String addr;//住所
	private String ascription;//管片工商所
	private String area;//片区
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAscription() {
		return ascription;
	}
	public void setAscription(String ascription) {
		this.ascription = ascription;
	}
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getCreditCode() {
		return creditCode;
	}
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	public String getDoorType() {
		return doorType;
	}
	public void setDoorType(String doorType) {
		this.doorType = doorType;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkTel() {
		return linkTel;
	}
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getRegistCode() {
		return registCode;
	}
	public void setRegistCode(String registCode) {
		this.registCode = registCode;
	}
	public String getRegistType() {
		return registType;
	}
	public void setRegistType(String registType) {
		this.registType = registType;
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
	public double getInvest() {
		return invest;
	}
	public void setInvest(double invest) {
		this.invest = invest;
	}
	public double getOutWorth() {
		return outWorth;
	}
	public void setOutWorth(double outWorth) {
		this.outWorth = outWorth;
	}
	public double getRealWorth() {
		return realWorth;
	}
	public void setRealWorth(double realWorth) {
		this.realWorth = realWorth;
	}
	public double getWorth() {
		return worth;
	}
	public void setWorth(double worth) {
		this.worth = worth;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getLimitDate() {
		return limitDate;
	}
	public void setLimitDate(String limitDate) {
		this.limitDate = limitDate;
	}
	
	
	
	
}
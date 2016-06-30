package com.yuanluesoft.credit.enterprisecredit.market.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 龙海市场监督管理局企业信用(credit_market_enterprise)
 * @author zyh
 *
 */
public class MarketEnterprise extends Record {
	private String registCode ; //注册号
	private String name; //企业名称
	private String type;//企业类型
	private String addr;//住所
	private Date buildDate;//成立日期
	
	private String registrar;//登记机关
	private String aic;//工商所
	private String area;//片区
	private String creditType;//信用类别
	private double creditScore;//信用分值
	private String person;//负责人
	private String tel;//电话
	private String businessScope;//经营范围
	private Date startDate;//经营期限起
	private Date endDate;//经营期限止
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getAic() {
		return aic;
	}
	public void setAic(String aic) {
		this.aic = aic;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Date getBuildDate() {
		return buildDate;
	}
	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
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
	public double getCreditScore() {
		return creditScore;
	}
	public void setCreditScore(double creditScore) {
		this.creditScore = creditScore;
	}
	public String getCreditType() {
		return creditType;
	}
	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getRegistrar() {
		return registrar;
	}
	public void setRegistrar(String registrar) {
		this.registrar = registrar;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
	
	
	
	
	
}
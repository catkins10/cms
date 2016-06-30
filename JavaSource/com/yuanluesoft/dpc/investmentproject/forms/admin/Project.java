package com.yuanluesoft.dpc.investmentproject.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Project extends ActionForm {
	private String name; //项目全称
	private String industry; //所属行业,包括交通、能源、农林业、工业园区、工业、社会事业、城建环保、旅游业、商贸服务业
	private String childIndustry; //所属子行业,高速公路、铁路、热电、水利、石油化工、高新技术产业、电子信息技术、机械、传统产业等
	private String area; //所在地区(开发区)
	private String address; //项目地址
	private String reason; //项目建设理由及条件
	private String scale; //项目建设规模和内容
	private String benefit; //项目经济效益分析
	private double investment; //总投资(万元)
	private double foreignInvestment; //利用外资(万元)
	private String currency; //币种,默认人民币
	private String investMode; //利用外资方式,独资、合资等
	private String investmentDetail; //投资情况详情
	private String cycle; //建设周期
	private String progress; //前期工作进展情况
	private String preferential; //配套的优惠措施
	private String managingUnit; //承办单位
	private String leader; //项目负责人
	private String linkman; //联系人及电话
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private String remark; //备注
	
	//扩展属性
	private String investModes; //投资方式
	
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
	 * @return the benefit
	 */
	public String getBenefit() {
		return benefit;
	}
	/**
	 * @param benefit the benefit to set
	 */
	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}
	/**
	 * @return the childIndustry
	 */
	public String getChildIndustry() {
		return childIndustry;
	}
	/**
	 * @param childIndustry the childIndustry to set
	 */
	public void setChildIndustry(String childIndustry) {
		this.childIndustry = childIndustry;
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
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the cycle
	 */
	public String getCycle() {
		return cycle;
	}
	/**
	 * @param cycle the cycle to set
	 */
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	/**
	 * @return the foreignInvestment
	 */
	public double getForeignInvestment() {
		return foreignInvestment;
	}
	/**
	 * @param foreignInvestment the foreignInvestment to set
	 */
	public void setForeignInvestment(double foreignInvestment) {
		this.foreignInvestment = foreignInvestment;
	}
	/**
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}
	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	/**
	 * @return the investment
	 */
	public double getInvestment() {
		return investment;
	}
	/**
	 * @param investment the investment to set
	 */
	public void setInvestment(double investment) {
		this.investment = investment;
	}
	/**
	 * @return the investmentDetail
	 */
	public String getInvestmentDetail() {
		return investmentDetail;
	}
	/**
	 * @param investmentDetail the investmentDetail to set
	 */
	public void setInvestmentDetail(String investmentDetail) {
		this.investmentDetail = investmentDetail;
	}
	/**
	 * @return the investMode
	 */
	public String getInvestMode() {
		return investMode;
	}
	/**
	 * @param investMode the investMode to set
	 */
	public void setInvestMode(String investMode) {
		this.investMode = investMode;
	}
	/**
	 * @return the leader
	 */
	public String getLeader() {
		return leader;
	}
	/**
	 * @param leader the leader to set
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}
	/**
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * @param linkman the linkman to set
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * @return the managingUnit
	 */
	public String getManagingUnit() {
		return managingUnit;
	}
	/**
	 * @param managingUnit the managingUnit to set
	 */
	public void setManagingUnit(String managingUnit) {
		this.managingUnit = managingUnit;
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
	 * @return the preferential
	 */
	public String getPreferential() {
		return preferential;
	}
	/**
	 * @param preferential the preferential to set
	 */
	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}
	/**
	 * @return the progress
	 */
	public String getProgress() {
		return progress;
	}
	/**
	 * @param progress the progress to set
	 */
	public void setProgress(String progress) {
		this.progress = progress;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
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
	 * @return the scale
	 */
	public String getScale() {
		return scale;
	}
	/**
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}
	/**
	 * @return the investModes
	 */
	public String getInvestModes() {
		return investModes;
	}
	/**
	 * @param investModes the investModes to set
	 */
	public void setInvestModes(String investModes) {
		this.investModes = investModes;
	}
}
package com.yuanluesoft.fet.project.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class FetMerchant extends Record {
	private String name; //客商姓名
	private String company; //公司名称
	private String duty; //公司职务
	private String otherDuties; //其他职衔
	private String country; //所在国别或地区
	private String industry; //行业类别
	private String businessScope; //经营范围
	private String address; //公司地址
	private String tel; //电话
	private String fax; //传真
	private String mail; //电子邮件
	private String mobile; //手机
	private String webSite; //网址
	private String investmentAreas; //已投资区域,三明市内、三明市外两个选项
	private String investmentPorjects; //已投资项目
	private String investmentOrder; //投资意向
	private String background; //公司背景资料
	private String enterpriseScale; //规模
	private Date companyCreated; //公司成立时间
	private Date created; //录入时间
	private String creator; //录入人
	private long creatorId; //录入人ID
	private String linkman; //联络人姓名
	private String linkmanDuty; //联络人公司职务
	private String linkmanTel; //联络人电话
	private String linkmanFax; //联络人传真
	private String linkmanMobile; //联络人手机
	private String remark; //备注
	/**
	 * @return the businessScope
	 */
	public String getBusinessScope() {
		return businessScope;
	}
	/**
	 * @param businessScope the businessScope to set
	 */
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
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
	 * @return the companyCreated
	 */
	public Date getCompanyCreated() {
		return companyCreated;
	}
	/**
	 * @param companyCreated the companyCreated to set
	 */
	public void setCompanyCreated(Date companyCreated) {
		this.companyCreated = companyCreated;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
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
	 * @return the enterpriseScale
	 */
	public String getEnterpriseScale() {
		return enterpriseScale;
	}
	/**
	 * @param enterpriseScale the enterpriseScale to set
	 */
	public void setEnterpriseScale(String enterpriseScale) {
		this.enterpriseScale = enterpriseScale;
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
	 * @return the background
	 */
	public String getBackground() {
		return background;
	}
	/**
	 * @param background the background to set
	 */
	public void setBackground(String background) {
		this.background = background;
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
	 * @return the investmentAreas
	 */
	public String getInvestmentAreas() {
		return investmentAreas;
	}
	/**
	 * @param investmentAreas the investmentAreas to set
	 */
	public void setInvestmentAreas(String investmentAreas) {
		this.investmentAreas = investmentAreas;
	}
	/**
	 * @return the investmentOrder
	 */
	public String getInvestmentOrder() {
		return investmentOrder;
	}
	/**
	 * @param investmentOrder the investmentOrder to set
	 */
	public void setInvestmentOrder(String investmentOrder) {
		this.investmentOrder = investmentOrder;
	}
	/**
	 * @return the investmentPorjects
	 */
	public String getInvestmentPorjects() {
		return investmentPorjects;
	}
	/**
	 * @param investmentPorjects the investmentPorjects to set
	 */
	public void setInvestmentPorjects(String investmentPorjects) {
		this.investmentPorjects = investmentPorjects;
	}
	/**
	 * @return the linkmanDuty
	 */
	public String getLinkmanDuty() {
		return linkmanDuty;
	}
	/**
	 * @param linkmanDuty the linkmanDuty to set
	 */
	public void setLinkmanDuty(String linkmanDuty) {
		this.linkmanDuty = linkmanDuty;
	}
	/**
	 * @return the linkmanFax
	 */
	public String getLinkmanFax() {
		return linkmanFax;
	}
	/**
	 * @param linkmanFax the linkmanFax to set
	 */
	public void setLinkmanFax(String linkmanFax) {
		this.linkmanFax = linkmanFax;
	}
	/**
	 * @return the linkmanMobile
	 */
	public String getLinkmanMobile() {
		return linkmanMobile;
	}
	/**
	 * @param linkmanMobile the linkmanMobile to set
	 */
	public void setLinkmanMobile(String linkmanMobile) {
		this.linkmanMobile = linkmanMobile;
	}
	/**
	 * @return the linkmanTel
	 */
	public String getLinkmanTel() {
		return linkmanTel;
	}
	/**
	 * @param linkmanTel the linkmanTel to set
	 */
	public void setLinkmanTel(String linkmanTel) {
		this.linkmanTel = linkmanTel;
	}
	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
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
	 * @return the otherDuties
	 */
	public String getOtherDuties() {
		return otherDuties;
	}
	/**
	 * @param otherDuties the otherDuties to set
	 */
	public void setOtherDuties(String otherDuties) {
		this.otherDuties = otherDuties;
	}
	/**
	 * @return the webSite
	 */
	public String getWebSite() {
		return webSite;
	}
	/**
	 * @param webSite the webSite to set
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

}

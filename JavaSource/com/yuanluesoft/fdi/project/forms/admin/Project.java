package com.yuanluesoft.fdi.project.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Project extends ActionForm {
	private String chinaCompanyName; //中方单位名称
	private String chinaCompanyAddress; //中方单位地址
	private String chinaCompanyPostal; //中方单位邮编
	private String chinaCompanySite; //中方单位网址
	private String chinaContactName; //中方联系人姓名
	private char chinaContactSex = 'M'; //中方联系人性别
	private String chinaContactTel; //中方联系人电话,含地区号
	private String chinaContactMobile; //中方联系人手机
	private String chinaContactFax; //中方联系人传真,含地区号
	private String chinaContactEmail; //中方联系人E-mail
	private String chinaContactIm; //中方联系人QQ\MSN
	private String chinaContactPost; //中方联系人所在部门及职务
	private String fdiContact1; //招商办联系人1
	private String fdiContact2; //招商办联系人2
	private String foreignCompanyName; //外方单位中文名称
	private String foreignCompanyEnglishName; //外方单位英文名称
	private String foreignCompanyAddress; //外方单位地址,含国别
	private String foreignCompanyPostal; //外方单位邮编
	private String foreignCompanyIntroduction; //外方单位简介,包括战略布局、核心行业与产品优势、竞争力分析等
	private String foreignCompanySite; //外方单位网址
	private String foreignContactName; //外方洽谈人姓名,中英文
	private String foreignContactPost; //外方洽谈人职务
	private char foreignContactSex = 'M'; //外方洽谈人性别
	private String foreignContactTel; //外方洽谈人电话,含地区号
	private String foreignContactMobile; //外方洽谈人手机
	private String foreignContactFax; //外方洽谈人传真,含地区号
	private String foreignContactEmail; //外方洽谈人E-mail
	private String foreignContactIm; //外方洽谈人QQ\MSN
	private String projectName; //项目名称
	private String projectCategory; //项目归类,电子信息,机械装备,石油化工,生物医药,闽台合作,侨商项目, 基础设施、服务业、农林牧渔、纺织轻工、其他
	private long industryId; //行业分类ID
	private String industry; //行业分类名称
	private String cooperateMode; //拟合作方式,合资,独资,合作,技术引进,设备引进,补偿贸易,其他
	private String summary; //项目概述,包括项目建设理由和条件、市场情况、建设内容、规模与目标等
	private double totalInvestment; //项目总投资,万元
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private Set pushs; //推进情况列表
	
	/**
	 * @return the chinaCompanyAddress
	 */
	public String getChinaCompanyAddress() {
		return chinaCompanyAddress;
	}
	/**
	 * @param chinaCompanyAddress the chinaCompanyAddress to set
	 */
	public void setChinaCompanyAddress(String chinaCompanyAddress) {
		this.chinaCompanyAddress = chinaCompanyAddress;
	}
	/**
	 * @return the chinaCompanyName
	 */
	public String getChinaCompanyName() {
		return chinaCompanyName;
	}
	/**
	 * @param chinaCompanyName the chinaCompanyName to set
	 */
	public void setChinaCompanyName(String chinaCompanyName) {
		this.chinaCompanyName = chinaCompanyName;
	}
	/**
	 * @return the chinaCompanyPostal
	 */
	public String getChinaCompanyPostal() {
		return chinaCompanyPostal;
	}
	/**
	 * @param chinaCompanyPostal the chinaCompanyPostal to set
	 */
	public void setChinaCompanyPostal(String chinaCompanyPostal) {
		this.chinaCompanyPostal = chinaCompanyPostal;
	}
	/**
	 * @return the chinaCompanySite
	 */
	public String getChinaCompanySite() {
		return chinaCompanySite;
	}
	/**
	 * @param chinaCompanySite the chinaCompanySite to set
	 */
	public void setChinaCompanySite(String chinaCompanySite) {
		this.chinaCompanySite = chinaCompanySite;
	}
	/**
	 * @return the chinaContactEmail
	 */
	public String getChinaContactEmail() {
		return chinaContactEmail;
	}
	/**
	 * @param chinaContactEmail the chinaContactEmail to set
	 */
	public void setChinaContactEmail(String chinaContactEmail) {
		this.chinaContactEmail = chinaContactEmail;
	}
	/**
	 * @return the chinaContactFax
	 */
	public String getChinaContactFax() {
		return chinaContactFax;
	}
	/**
	 * @param chinaContactFax the chinaContactFax to set
	 */
	public void setChinaContactFax(String chinaContactFax) {
		this.chinaContactFax = chinaContactFax;
	}
	/**
	 * @return the chinaContactIm
	 */
	public String getChinaContactIm() {
		return chinaContactIm;
	}
	/**
	 * @param chinaContactIm the chinaContactIm to set
	 */
	public void setChinaContactIm(String chinaContactIm) {
		this.chinaContactIm = chinaContactIm;
	}
	/**
	 * @return the chinaContactMobile
	 */
	public String getChinaContactMobile() {
		return chinaContactMobile;
	}
	/**
	 * @param chinaContactMobile the chinaContactMobile to set
	 */
	public void setChinaContactMobile(String chinaContactMobile) {
		this.chinaContactMobile = chinaContactMobile;
	}
	/**
	 * @return the chinaContactName
	 */
	public String getChinaContactName() {
		return chinaContactName;
	}
	/**
	 * @param chinaContactName the chinaContactName to set
	 */
	public void setChinaContactName(String chinaContactName) {
		this.chinaContactName = chinaContactName;
	}
	/**
	 * @return the chinaContactPost
	 */
	public String getChinaContactPost() {
		return chinaContactPost;
	}
	/**
	 * @param chinaContactPost the chinaContactPost to set
	 */
	public void setChinaContactPost(String chinaContactPost) {
		this.chinaContactPost = chinaContactPost;
	}
	/**
	 * @return the chinaContactSex
	 */
	public char getChinaContactSex() {
		return chinaContactSex;
	}
	/**
	 * @param chinaContactSex the chinaContactSex to set
	 */
	public void setChinaContactSex(char chinaContactSex) {
		this.chinaContactSex = chinaContactSex;
	}
	/**
	 * @return the chinaContactTel
	 */
	public String getChinaContactTel() {
		return chinaContactTel;
	}
	/**
	 * @param chinaContactTel the chinaContactTel to set
	 */
	public void setChinaContactTel(String chinaContactTel) {
		this.chinaContactTel = chinaContactTel;
	}
	/**
	 * @return the cooperateMode
	 */
	public String getCooperateMode() {
		return cooperateMode;
	}
	/**
	 * @param cooperateMode the cooperateMode to set
	 */
	public void setCooperateMode(String cooperateMode) {
		this.cooperateMode = cooperateMode;
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
	 * @return the fdiContact1
	 */
	public String getFdiContact1() {
		return fdiContact1;
	}
	/**
	 * @param fdiContact1 the fdiContact1 to set
	 */
	public void setFdiContact1(String fdiContact1) {
		this.fdiContact1 = fdiContact1;
	}
	/**
	 * @return the fdiContact2
	 */
	public String getFdiContact2() {
		return fdiContact2;
	}
	/**
	 * @param fdiContact2 the fdiContact2 to set
	 */
	public void setFdiContact2(String fdiContact2) {
		this.fdiContact2 = fdiContact2;
	}
	/**
	 * @return the foreignCompanyAddress
	 */
	public String getForeignCompanyAddress() {
		return foreignCompanyAddress;
	}
	/**
	 * @param foreignCompanyAddress the foreignCompanyAddress to set
	 */
	public void setForeignCompanyAddress(String foreignCompanyAddress) {
		this.foreignCompanyAddress = foreignCompanyAddress;
	}
	/**
	 * @return the foreignCompanyEnglishName
	 */
	public String getForeignCompanyEnglishName() {
		return foreignCompanyEnglishName;
	}
	/**
	 * @param foreignCompanyEnglishName the foreignCompanyEnglishName to set
	 */
	public void setForeignCompanyEnglishName(String foreignCompanyEnglishName) {
		this.foreignCompanyEnglishName = foreignCompanyEnglishName;
	}
	/**
	 * @return the foreignCompanyIntroduction
	 */
	public String getForeignCompanyIntroduction() {
		return foreignCompanyIntroduction;
	}
	/**
	 * @param foreignCompanyIntroduction the foreignCompanyIntroduction to set
	 */
	public void setForeignCompanyIntroduction(String foreignCompanyIntroduction) {
		this.foreignCompanyIntroduction = foreignCompanyIntroduction;
	}
	/**
	 * @return the foreignCompanyName
	 */
	public String getForeignCompanyName() {
		return foreignCompanyName;
	}
	/**
	 * @param foreignCompanyName the foreignCompanyName to set
	 */
	public void setForeignCompanyName(String foreignCompanyName) {
		this.foreignCompanyName = foreignCompanyName;
	}
	/**
	 * @return the foreignCompanyPostal
	 */
	public String getForeignCompanyPostal() {
		return foreignCompanyPostal;
	}
	/**
	 * @param foreignCompanyPostal the foreignCompanyPostal to set
	 */
	public void setForeignCompanyPostal(String foreignCompanyPostal) {
		this.foreignCompanyPostal = foreignCompanyPostal;
	}
	/**
	 * @return the foreignCompanySite
	 */
	public String getForeignCompanySite() {
		return foreignCompanySite;
	}
	/**
	 * @param foreignCompanySite the foreignCompanySite to set
	 */
	public void setForeignCompanySite(String foreignCompanySite) {
		this.foreignCompanySite = foreignCompanySite;
	}
	/**
	 * @return the foreignContactEmail
	 */
	public String getForeignContactEmail() {
		return foreignContactEmail;
	}
	/**
	 * @param foreignContactEmail the foreignContactEmail to set
	 */
	public void setForeignContactEmail(String foreignContactEmail) {
		this.foreignContactEmail = foreignContactEmail;
	}
	/**
	 * @return the foreignContactFax
	 */
	public String getForeignContactFax() {
		return foreignContactFax;
	}
	/**
	 * @param foreignContactFax the foreignContactFax to set
	 */
	public void setForeignContactFax(String foreignContactFax) {
		this.foreignContactFax = foreignContactFax;
	}
	/**
	 * @return the foreignContactIm
	 */
	public String getForeignContactIm() {
		return foreignContactIm;
	}
	/**
	 * @param foreignContactIm the foreignContactIm to set
	 */
	public void setForeignContactIm(String foreignContactIm) {
		this.foreignContactIm = foreignContactIm;
	}
	/**
	 * @return the foreignContactMobile
	 */
	public String getForeignContactMobile() {
		return foreignContactMobile;
	}
	/**
	 * @param foreignContactMobile the foreignContactMobile to set
	 */
	public void setForeignContactMobile(String foreignContactMobile) {
		this.foreignContactMobile = foreignContactMobile;
	}
	/**
	 * @return the foreignContactName
	 */
	public String getForeignContactName() {
		return foreignContactName;
	}
	/**
	 * @param foreignContactName the foreignContactName to set
	 */
	public void setForeignContactName(String foreignContactName) {
		this.foreignContactName = foreignContactName;
	}
	/**
	 * @return the foreignContactPost
	 */
	public String getForeignContactPost() {
		return foreignContactPost;
	}
	/**
	 * @param foreignContactPost the foreignContactPost to set
	 */
	public void setForeignContactPost(String foreignContactPost) {
		this.foreignContactPost = foreignContactPost;
	}
	/**
	 * @return the foreignContactSex
	 */
	public char getForeignContactSex() {
		return foreignContactSex;
	}
	/**
	 * @param foreignContactSex the foreignContactSex to set
	 */
	public void setForeignContactSex(char foreignContactSex) {
		this.foreignContactSex = foreignContactSex;
	}
	/**
	 * @return the foreignContactTel
	 */
	public String getForeignContactTel() {
		return foreignContactTel;
	}
	/**
	 * @param foreignContactTel the foreignContactTel to set
	 */
	public void setForeignContactTel(String foreignContactTel) {
		this.foreignContactTel = foreignContactTel;
	}
	/**
	 * @return the projectCategory
	 */
	public String getProjectCategory() {
		return projectCategory;
	}
	/**
	 * @param projectCategory the projectCategory to set
	 */
	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the totalInvestment
	 */
	public double getTotalInvestment() {
		return totalInvestment;
	}
	/**
	 * @param totalInvestment the totalInvestment to set
	 */
	public void setTotalInvestment(double totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
	/**
	 * @return the pushs
	 */
	public Set getPushs() {
		return pushs;
	}
	/**
	 * @param pushs the pushs to set
	 */
	public void setPushs(Set pushs) {
		this.pushs = pushs;
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
	 * @return the industryId
	 */
	public long getIndustryId() {
		return industryId;
	}
	/**
	 * @param industryId the industryId to set
	 */
	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}
}
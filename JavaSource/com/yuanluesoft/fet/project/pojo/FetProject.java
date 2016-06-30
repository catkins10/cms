package com.yuanluesoft.fet.project.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:项目(fet_project)
 * @author linchuan
 *
 */
public class FetProject extends Record {
	private String name; //项目名称
	private String status; //项目进展情况,意向/可望签约/已签约,拟报批/已报批,筹建/开工/竣工/转办内资企业
	private String manageUnit; //项目主管单位
	private Timestamp created; //填报日期
	private long fairId; //活动ID
	private String fairName; //活动名称
	private String fairNumber; //届别
	private String address; //项目地址
	private String linkman; //联系人
	private String tel; //联系电话
	private String fax; //传真
	private String country; //外方国别地区
	private String investmentType; //投资方式
	private String industry; //行业
	private String foreignCompany; //外方单位
	private String chineseCompany; //中方单位
	private String businessScope; //经营范围
	private String enterpriseScale; //生产建设规模
	private String sign; //签约情况,意向项目/可望签约项目/合同项目
	private double totalInvestment; //总投资
	private double bargainInvestment; //合同外资
	private double registInvestment; //注册外资
	private Date toSubmitTime; //拟报批时间
	private String toSubmitType; //拟报批类型
	private String toSubmitEvolve; //未报批项目进展说明
	private Date approvalTime; //批准时间
	private double approvalTotalInvestment; //批准总投资
	private double approvalBargainInvestment; //批准合同外资
	private double approvalRegistInvestment; //批准注册外资
	private String organizationCode; //企业代码
	private String companyName; //企业名称
	private Date toBuildingDate; //拟开工时间
	private Date getLicenseTime; //领照时间
	private double receivedInvestment; //已到资金,从资金到位明细中累计
	private Date buildingDate; //开工时间
	private Date compeletTime; //竣工时间
	private String remark; //备注
	private String county; //县别
	private String approvalUnit; //审批单位,各县外经贸局
	private String signCategory; //签约种类
	private String consult; //对接洽谈情况
	private Date upgradeDate; //升格时间
	private double receivedChecked; //已验资金	
	
	
	private Set problems;
	private Set investments;
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
	 * @return the approvalBargainInvestment
	 */
	public double getApprovalBargainInvestment() {
		return approvalBargainInvestment;
	}
	/**
	 * @param approvalBargainInvestment the approvalBargainInvestment to set
	 */
	public void setApprovalBargainInvestment(double approvalBargainInvestment) {
		this.approvalBargainInvestment = approvalBargainInvestment;
	}
	/**
	 * @return the approvalRegistInvestment
	 */
	public double getApprovalRegistInvestment() {
		return approvalRegistInvestment;
	}
	/**
	 * @param approvalRegistInvestment the approvalRegistInvestment to set
	 */
	public void setApprovalRegistInvestment(double approvalRegistInvestment) {
		this.approvalRegistInvestment = approvalRegistInvestment;
	}
	/**
	 * @return the approvalTime
	 */
	public Date getApprovalTime() {
		return approvalTime;
	}
	/**
	 * @param approvalTime the approvalTime to set
	 */
	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}
	/**
	 * @return the approvalTotalInvestment
	 */
	public double getApprovalTotalInvestment() {
		return approvalTotalInvestment;
	}
	/**
	 * @param approvalTotalInvestment the approvalTotalInvestment to set
	 */
	public void setApprovalTotalInvestment(double approvalTotalInvestment) {
		this.approvalTotalInvestment = approvalTotalInvestment;
	}
	/**
	 * @return the bargainInvestment
	 */
	public double getBargainInvestment() {
		return bargainInvestment;
	}
	/**
	 * @param bargainInvestment the bargainInvestment to set
	 */
	public void setBargainInvestment(double bargainInvestment) {
		this.bargainInvestment = bargainInvestment;
	}
	/**
	 * @return the buildingDate
	 */
	public Date getBuildingDate() {
		return buildingDate;
	}
	/**
	 * @param buildingDate the buildingDate to set
	 */
	public void setBuildingDate(Date buildingDate) {
		this.buildingDate = buildingDate;
	}
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
	 * @return the chineseCompany
	 */
	public String getChineseCompany() {
		return chineseCompany;
	}
	/**
	 * @param chineseCompany the chineseCompany to set
	 */
	public void setChineseCompany(String chineseCompany) {
		this.chineseCompany = chineseCompany;
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
	 * @return the compeletTime
	 */
	public Date getCompeletTime() {
		return compeletTime;
	}
	/**
	 * @param compeletTime the compeletTime to set
	 */
	public void setCompeletTime(Date compeletTime) {
		this.compeletTime = compeletTime;
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
	 * @return the fairId
	 */
	public long getFairId() {
		return fairId;
	}
	/**
	 * @param fairId the fairId to set
	 */
	public void setFairId(long fairId) {
		this.fairId = fairId;
	}
	/**
	 * @return the fairName
	 */
	public String getFairName() {
		return fairName;
	}
	/**
	 * @param fairName the fairName to set
	 */
	public void setFairName(String fairName) {
		this.fairName = fairName;
	}
	/**
	 * @return the fairNumber
	 */
	public String getFairNumber() {
		return fairNumber;
	}
	/**
	 * @param fairNumber the fairNumber to set
	 */
	public void setFairNumber(String fairNumber) {
		this.fairNumber = fairNumber;
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
	 * @return the foreignCompany
	 */
	public String getForeignCompany() {
		return foreignCompany;
	}
	/**
	 * @param foreignCompany the foreignCompany to set
	 */
	public void setForeignCompany(String foreignCompany) {
		this.foreignCompany = foreignCompany;
	}
	/**
	 * @return the getLicenseTime
	 */
	public Date getGetLicenseTime() {
		return getLicenseTime;
	}
	/**
	 * @param getLicenseTime the getLicenseTime to set
	 */
	public void setGetLicenseTime(Date getLicenseTime) {
		this.getLicenseTime = getLicenseTime;
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
	 * @return the investmentType
	 */
	public String getInvestmentType() {
		return investmentType;
	}
	/**
	 * @param investmentType the investmentType to set
	 */
	public void setInvestmentType(String investmentType) {
		this.investmentType = investmentType;
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
	 * @return the manageUnit
	 */
	public String getManageUnit() {
		return manageUnit;
	}
	/**
	 * @param manageUnit the manageUnit to set
	 */
	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
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
	 * @return the organizationCode
	 */
	public String getOrganizationCode() {
		return organizationCode;
	}
	/**
	 * @param organizationCode the organizationCode to set
	 */
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	/**
	 * @return the receivedInvestment
	 */
	public double getReceivedInvestment() {
		return receivedInvestment;
	}
	/**
	 * @param receivedInvestment the receivedInvestment to set
	 */
	public void setReceivedInvestment(double receivedInvestment) {
		this.receivedInvestment = receivedInvestment;
	}
	/**
	 * @return the registInvestment
	 */
	public double getRegistInvestment() {
		return registInvestment;
	}
	/**
	 * @param registInvestment the registInvestment to set
	 */
	public void setRegistInvestment(double registInvestment) {
		this.registInvestment = registInvestment;
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
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the toBuildingDate
	 */
	public Date getToBuildingDate() {
		return toBuildingDate;
	}
	/**
	 * @param toBuildingDate the toBuildingDate to set
	 */
	public void setToBuildingDate(Date toBuildingDate) {
		this.toBuildingDate = toBuildingDate;
	}
	/**
	 * @return the toSubmitEvolve
	 */
	public String getToSubmitEvolve() {
		return toSubmitEvolve;
	}
	/**
	 * @param toSubmitEvolve the toSubmitEvolve to set
	 */
	public void setToSubmitEvolve(String toSubmitEvolve) {
		this.toSubmitEvolve = toSubmitEvolve;
	}
	/**
	 * @return the toSubmitType
	 */
	public String getToSubmitType() {
		return toSubmitType;
	}
	/**
	 * @param toSubmitType the toSubmitType to set
	 */
	public void setToSubmitType(String toSubmitType) {
		this.toSubmitType = toSubmitType;
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
	 * @return the problems
	 */
	public Set getProblems() {
		return problems;
	}
	/**
	 * @param problems the problems to set
	 */
	public void setProblems(Set problems) {
		this.problems = problems;
	}
	/**
	 * @return the toSubmitTime
	 */
	public Date getToSubmitTime() {
		return toSubmitTime;
	}
	/**
	 * @param toSubmitTime the toSubmitTime to set
	 */
	public void setToSubmitTime(Date toSubmitTime) {
		this.toSubmitTime = toSubmitTime;
	}
	/**
	 * @return the investments
	 */
	public Set getInvestments() {
		return investments;
	}
	/**
	 * @param investments the investments to set
	 */
	public void setInvestments(Set investments) {
		this.investments = investments;
	}
	/**
	 * @return the approvalUnit
	 */
	public String getApprovalUnit() {
		return approvalUnit;
	}
	/**
	 * @param approvalUnit the approvalUnit to set
	 */
	public void setApprovalUnit(String approvalUnit) {
		this.approvalUnit = approvalUnit;
	}
	/**
	 * @return the consult
	 */
	public String getConsult() {
		return consult;
	}
	/**
	 * @param consult the consult to set
	 */
	public void setConsult(String consult) {
		this.consult = consult;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	/**
	 * @return the signCategory
	 */
	public String getSignCategory() {
		return signCategory;
	}
	/**
	 * @param signCategory the signCategory to set
	 */
	public void setSignCategory(String signCategory) {
		this.signCategory = signCategory;
	}
	/**
	 * @return the receivedChecked
	 */
	public double getReceivedChecked() {
		return receivedChecked;
	}
	/**
	 * @param receivedChecked the receivedChecked to set
	 */
	public void setReceivedChecked(double receivedChecked) {
		this.receivedChecked = receivedChecked;
	}
	/**
	 * @return the upgradeDate
	 */
	public Date getUpgradeDate() {
		return upgradeDate;
	}
	/**
	 * @param upgradeDate the upgradeDate to set
	 */
	public void setUpgradeDate(Date upgradeDate) {
		this.upgradeDate = upgradeDate;
	}
}

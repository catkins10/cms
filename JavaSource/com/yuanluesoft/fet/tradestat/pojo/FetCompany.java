package com.yuanluesoft.fet.tradestat.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 企业信息(fet_company)
 * @author linchuan
 *
 */
public class FetCompany extends Record {
	private String code; //组织海关代码
	private String loginName; //企业帐号
	private String password; //密码
	private String name; //中文名称
	private String englishName; //英文名称
	private Date created; //企业成立时间
	private String type; //经济类型,国有及民企、三资
	private String address; //企业通讯地址(中文)
	private String englishAddress; //企业通讯地址(英文)
	private long countyId; //所属县、市ID
	private String county; //所属县、市
	private long developmentAreaId; //所属开发区ID
	private String developmentArea; //所属开发区
	private String businessType; //企业经营性质,制造商、经销(贸易)商
	private char hasPrivilege = '0'; //有无进出口经营权
	private String principalName; //业务负责人中文名
	private String principalEnglishName; //业务负责人英文名
	private String job; //职务(中文)
	private String englishJob; //职务(英文)
	private int totalLastYear; //上年进出口额,万美元
	private int registeredCapital; //注册资金,万元
	private String tel; //联系电话
	private String fax; //传真
	private String webSite; //企业网址
	private String email; //电子邮箱
	private String postalCode; //邮政编码
	private int employeeCount; //员工人数
	private String foreignOfficeName; //境外办事机构(中文)
	private String foreignOfficeEnglishName; //境外办事机构(英文)
	private String foreignOfficeCityName; //所在城市(中文)
	private String foreignOfficeCityEnglishName; //所在城市(英文)
	private String motherCompanyName; //母公司名称(中文)
	private String motherCompanyEnglishName; //母公司名称(英文)
	private String remark; //备注
	private char approvalPass = '0'; //审核通过否
	
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
	 * @return the businessType
	 */
	public String getBusinessType() {
		return businessType;
	}
	/**
	 * @param businessType the businessType to set
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the countyId
	 */
	public long getCountyId() {
		return countyId;
	}
	/**
	 * @param countyId the countyId to set
	 */
	public void setCountyId(long countyId) {
		this.countyId = countyId;
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
	 * @return the developmentArea
	 */
	public String getDevelopmentArea() {
		return developmentArea;
	}
	/**
	 * @param developmentArea the developmentArea to set
	 */
	public void setDevelopmentArea(String developmentArea) {
		this.developmentArea = developmentArea;
	}
	/**
	 * @return the developmentAreaId
	 */
	public long getDevelopmentAreaId() {
		return developmentAreaId;
	}
	/**
	 * @param developmentAreaId the developmentAreaId to set
	 */
	public void setDevelopmentAreaId(long developmentAreaId) {
		this.developmentAreaId = developmentAreaId;
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
	 * @return the employeeCount
	 */
	public int getEmployeeCount() {
		return employeeCount;
	}
	/**
	 * @param employeeCount the employeeCount to set
	 */
	public void setEmployeeCount(int employeeCount) {
		this.employeeCount = employeeCount;
	}
	/**
	 * @return the englishAddress
	 */
	public String getEnglishAddress() {
		return englishAddress;
	}
	/**
	 * @param englishAddress the englishAddress to set
	 */
	public void setEnglishAddress(String englishAddress) {
		this.englishAddress = englishAddress;
	}
	/**
	 * @return the englishJob
	 */
	public String getEnglishJob() {
		return englishJob;
	}
	/**
	 * @param englishJob the englishJob to set
	 */
	public void setEnglishJob(String englishJob) {
		this.englishJob = englishJob;
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
	 * @return the foreignOfficeCityEnglishName
	 */
	public String getForeignOfficeCityEnglishName() {
		return foreignOfficeCityEnglishName;
	}
	/**
	 * @param foreignOfficeCityEnglishName the foreignOfficeCityEnglishName to set
	 */
	public void setForeignOfficeCityEnglishName(String foreignOfficeCityEnglishName) {
		this.foreignOfficeCityEnglishName = foreignOfficeCityEnglishName;
	}
	/**
	 * @return the foreignOfficeCityName
	 */
	public String getForeignOfficeCityName() {
		return foreignOfficeCityName;
	}
	/**
	 * @param foreignOfficeCityName the foreignOfficeCityName to set
	 */
	public void setForeignOfficeCityName(String foreignOfficeCityName) {
		this.foreignOfficeCityName = foreignOfficeCityName;
	}
	/**
	 * @return the foreignOfficeEnglishName
	 */
	public String getForeignOfficeEnglishName() {
		return foreignOfficeEnglishName;
	}
	/**
	 * @param foreignOfficeEnglishName the foreignOfficeEnglishName to set
	 */
	public void setForeignOfficeEnglishName(String foreignOfficeEnglishName) {
		this.foreignOfficeEnglishName = foreignOfficeEnglishName;
	}
	/**
	 * @return the foreignOfficeName
	 */
	public String getForeignOfficeName() {
		return foreignOfficeName;
	}
	/**
	 * @param foreignOfficeName the foreignOfficeName to set
	 */
	public void setForeignOfficeName(String foreignOfficeName) {
		this.foreignOfficeName = foreignOfficeName;
	}
	/**
	 * @return the hasPrivilege
	 */
	public char getHasPrivilege() {
		return hasPrivilege;
	}
	/**
	 * @param hasPrivilege the hasPrivilege to set
	 */
	public void setHasPrivilege(char hasPrivilege) {
		this.hasPrivilege = hasPrivilege;
	}
	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}
	/**
	 * @param job the job to set
	 */
	public void setJob(String job) {
		this.job = job;
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
	 * @return the motherCompanyEnglishName
	 */
	public String getMotherCompanyEnglishName() {
		return motherCompanyEnglishName;
	}
	/**
	 * @param motherCompanyEnglishName the motherCompanyEnglishName to set
	 */
	public void setMotherCompanyEnglishName(String motherCompanyEnglishName) {
		this.motherCompanyEnglishName = motherCompanyEnglishName;
	}
	/**
	 * @return the motherCompanyName
	 */
	public String getMotherCompanyName() {
		return motherCompanyName;
	}
	/**
	 * @param motherCompanyName the motherCompanyName to set
	 */
	public void setMotherCompanyName(String motherCompanyName) {
		this.motherCompanyName = motherCompanyName;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	/**
	 * @return the principalEnglishName
	 */
	public String getPrincipalEnglishName() {
		return principalEnglishName;
	}
	/**
	 * @param principalEnglishName the principalEnglishName to set
	 */
	public void setPrincipalEnglishName(String principalEnglishName) {
		this.principalEnglishName = principalEnglishName;
	}
	/**
	 * @return the principalName
	 */
	public String getPrincipalName() {
		return principalName;
	}
	/**
	 * @param principalName the principalName to set
	 */
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
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
	 * @return the registeredCapital
	 */
	public int getRegisteredCapital() {
		return registeredCapital;
	}
	/**
	 * @param registeredCapital the registeredCapital to set
	 */
	public void setRegisteredCapital(int registeredCapital) {
		this.registeredCapital = registeredCapital;
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
	 * @return the totalLastYear
	 */
	public int getTotalLastYear() {
		return totalLastYear;
	}
	/**
	 * @param totalLastYear the totalLastYear to set
	 */
	public void setTotalLastYear(int totalLastYear) {
		this.totalLastYear = totalLastYear;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	/**
	 * @return the approvalPass
	 */
	public char getApprovalPass() {
		return approvalPass;
	}
	/**
	 * @param approvalPass the approvalPass to set
	 */
	public void setApprovalPass(char approvalPass) {
		this.approvalPass = approvalPass;
	}

}

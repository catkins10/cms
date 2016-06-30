package com.yuanluesoft.bidding.enterprise.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 企业(bidding_enterprise)
 * @author linchuan
 *
 */
public class BiddingEnterprise extends WorkflowData {
	private String area; //所在地区
	private String name; //企业名称
	private String businessLicence; //营业执照号码
	private String statisticsLicence; //单位统计证
	private String safeLicence; //安全许可证
	private Date registDate; //注册时间
	private int registeredCapital; //注册资金（万元）
	private String kind; //企业性质,国有
	private String legalRepresentative; //法定代表人
	private String representativeIdNumber; //法定代表人身份证号码
	private String representativeTel; //法定代表人联系电话
	private String manager; //企业经理
	private String managerIdNumber; //企业经理身份证号码
	private String managerTel; //企业经理联系电话
	private String technicalLeader; //技术负责人
	private String technicalLeaderIdNumner; //技术负责人身份证号码
	private String technicalLeaderTel; //技术负责人电话
	private String address; //通讯地址
	private String postalcode; //邮政编码
	private String linkman; //联系人
	private String linkmanIdNumber; //联系人身份证号码
	private String tel; //联系电话
	private String mobile; //手机
	private String fax; //传真
	private String email; //电子邮件
	private String website; //企业主页
	private String bank; //开户银行
	private String account; //开户帐号
	private String introduction; //经营范围
	private Timestamp created; //登记时间
	private char isValid = '0'; //企业信息是否生效,完成注册完成后置1
	private char isNullify = '0'; //是否注销审批记录
	private char isAlter = '0'; //是否变更审批记录
	private long alterEnterpriseId; //变更企业ID
	private String alterDescription; //变更内容描述,根据用户操作,自动做记录
	private String workflowInstanceId; //工作流实例ID
	private String remark; //备注

	private Set certs; //资质列表
	private Set jobholders; //从业人员列表
	private Set employees; //企业用户列表

	/**
	 * 获取资质标题列表
	 * @return
	 */
	public String getCertTitles() {
		return ListUtils.join(certs, "type", ",", false);
	}
	
	/**
	 * 获取状态名称
	 * @return
	 */
	public String getStatusText() {
		return isAlter=='1' ? "企业变更" : (isNullify=='1' ? "企业注销" : (isValid=='1' ? "企业" : "企业注册"));
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
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
	 * @return the alterDescription
	 */
	public String getAlterDescription() {
		return alterDescription;
	}

	/**
	 * @param alterDescription the alterDescription to set
	 */
	public void setAlterDescription(String alterDescription) {
		this.alterDescription = alterDescription;
	}

	/**
	 * @return the alterEnterpriseId
	 */
	public long getAlterEnterpriseId() {
		return alterEnterpriseId;
	}

	/**
	 * @param alterEnterpriseId the alterEnterpriseId to set
	 */
	public void setAlterEnterpriseId(long alterEnterpriseId) {
		this.alterEnterpriseId = alterEnterpriseId;
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
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * @param bank the bank to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * @return the businessLicence
	 */
	public String getBusinessLicence() {
		return businessLicence;
	}

	/**
	 * @param businessLicence the businessLicence to set
	 */
	public void setBusinessLicence(String businessLicence) {
		this.businessLicence = businessLicence;
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
	 * @return the employees
	 */
	public Set getEmployees() {
		return employees;
	}

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(Set employees) {
		this.employees = employees;
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
	 * @return the introduction
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction the introduction to set
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * @return the isAlter
	 */
	public char getIsAlter() {
		return isAlter;
	}

	/**
	 * @param isAlter the isAlter to set
	 */
	public void setIsAlter(char isAlter) {
		this.isAlter = isAlter;
	}
	/**
	 * @return the isValid
	 */
	public char getIsValid() {
		return isValid;
	}

	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(char isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return the jobholders
	 */
	public Set getJobholders() {
		return jobholders;
	}

	/**
	 * @param jobholders the jobholders to set
	 */
	public void setJobholders(Set jobholders) {
		this.jobholders = jobholders;
	}

	/**
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @param kind the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * @return the legalRepresentative
	 */
	public String getLegalRepresentative() {
		return legalRepresentative;
	}

	/**
	 * @param legalRepresentative the legalRepresentative to set
	 */
	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
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
	 * @return the manager
	 */
	public String getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}

	/**
	 * @return the managerIdNumber
	 */
	public String getManagerIdNumber() {
		return managerIdNumber;
	}

	/**
	 * @param managerIdNumber the managerIdNumber to set
	 */
	public void setManagerIdNumber(String managerIdNumber) {
		this.managerIdNumber = managerIdNumber;
	}

	/**
	 * @return the managerTel
	 */
	public String getManagerTel() {
		return managerTel;
	}

	/**
	 * @param managerTel the managerTel to set
	 */
	public void setManagerTel(String managerTel) {
		this.managerTel = managerTel;
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
	 * @return the postalcode
	 */
	public String getPostalcode() {
		return postalcode;
	}

	/**
	 * @param postalcode the postalcode to set
	 */
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	/**
	 * @return the registDate
	 */
	public Date getRegistDate() {
		return registDate;
	}

	/**
	 * @param registDate the registDate to set
	 */
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
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
	 * @return the representativeIdNumber
	 */
	public String getRepresentativeIdNumber() {
		return representativeIdNumber;
	}

	/**
	 * @param representativeIdNumber the representativeIdNumber to set
	 */
	public void setRepresentativeIdNumber(String representativeIdNumber) {
		this.representativeIdNumber = representativeIdNumber;
	}

	/**
	 * @return the representativeTel
	 */
	public String getRepresentativeTel() {
		return representativeTel;
	}

	/**
	 * @param representativeTel the representativeTel to set
	 */
	public void setRepresentativeTel(String representativeTel) {
		this.representativeTel = representativeTel;
	}

	/**
	 * @return the safeLicence
	 */
	public String getSafeLicence() {
		return safeLicence;
	}

	/**
	 * @param safeLicence the safeLicence to set
	 */
	public void setSafeLicence(String safeLicence) {
		this.safeLicence = safeLicence;
	}

	/**
	 * @return the statisticsLicence
	 */
	public String getStatisticsLicence() {
		return statisticsLicence;
	}

	/**
	 * @param statisticsLicence the statisticsLicence to set
	 */
	public void setStatisticsLicence(String statisticsLicence) {
		this.statisticsLicence = statisticsLicence;
	}

	/**
	 * @return the technicalLeader
	 */
	public String getTechnicalLeader() {
		return technicalLeader;
	}

	/**
	 * @param technicalLeader the technicalLeader to set
	 */
	public void setTechnicalLeader(String technicalLeader) {
		this.technicalLeader = technicalLeader;
	}

	/**
	 * @return the technicalLeaderIdNumner
	 */
	public String getTechnicalLeaderIdNumner() {
		return technicalLeaderIdNumner;
	}

	/**
	 * @param technicalLeaderIdNumner the technicalLeaderIdNumner to set
	 */
	public void setTechnicalLeaderIdNumner(String technicalLeaderIdNumner) {
		this.technicalLeaderIdNumner = technicalLeaderIdNumner;
	}

	/**
	 * @return the technicalLeaderTel
	 */
	public String getTechnicalLeaderTel() {
		return technicalLeaderTel;
	}

	/**
	 * @param technicalLeaderTel the technicalLeaderTel to set
	 */
	public void setTechnicalLeaderTel(String technicalLeaderTel) {
		this.technicalLeaderTel = technicalLeaderTel;
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
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the workflowInstanceId
	 */
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}

	/**
	 * @param workflowInstanceId the workflowInstanceId to set
	 */
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}

	/**
	 * @return the certs
	 */
	public Set getCerts() {
		return certs;
	}

	/**
	 * @param certs the certs to set
	 */
	public void setCerts(Set certs) {
		this.certs = certs;
	}

	/**
	 * @return the isNullify
	 */
	public char getIsNullify() {
		return isNullify;
	}

	/**
	 * @param isNullify the isNullify to set
	 */
	public void setIsNullify(char isNullify) {
		this.isNullify = isNullify;
	}

	/**
	 * @return the linkmanIdNumber
	 */
	public String getLinkmanIdNumber() {
		return linkmanIdNumber;
	}

	/**
	 * @param linkmanIdNumber the linkmanIdNumber to set
	 */
	public void setLinkmanIdNumber(String linkmanIdNumber) {
		this.linkmanIdNumber = linkmanIdNumber;
	}
}
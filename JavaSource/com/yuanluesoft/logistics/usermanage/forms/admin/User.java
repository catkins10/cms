package com.yuanluesoft.logistics.usermanage.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class User extends ActionForm {
	private String loginName; //登录用户名
	private String password; //登录密码
	private char isCompany = '0'; //公司/个人
	private char isHalt = '0'; //是否停用
	private String name; //公司名称/个人姓名
	private long areaId; //所在地区ID
	private String area; //所在地区
	private String address; //地址
	private String fax; //传真
	private String tel; //电话
	private String businessLicence; //公司营业执照号码
	private String legalRepresentative; //法人代表
	private String representativeIdNumber; //法人代表身份证号码
	private String representativeTel; //法人代表联系电话
	private String webSite; //公司网址
	private String linkman; //联系人
	private String linkmanTel; //联系电话
	private String linkmanQQ; //联系人QQ
	private String linkmanMail; //联系人邮箱
	private char isDeleted = '0'; //是否删除
	private long creatorId; //注册人ID
	private String creator; //注册人姓名
	private Timestamp created; //注册时间
	private String creatorIP; //注册人IP
	private char isApproval = '0'; //待审核
	private long approverId; //审核人ID
	private String approver; //审核人姓名
	private String approverIP; //审核人IP
	private Timestamp approvalTime; //审核时间
	private String remark; //备注
	private Set blacklists; //黑名单列表
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
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return the blacklists
	 */
	public Set getBlacklists() {
		return blacklists;
	}
	/**
	 * @param blacklists the blacklists to set
	 */
	public void setBlacklists(Set blacklists) {
		this.blacklists = blacklists;
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
	 * @return the creatorIP
	 */
	public String getCreatorIP() {
		return creatorIP;
	}
	/**
	 * @param creatorIP the creatorIP to set
	 */
	public void setCreatorIP(String creatorIP) {
		this.creatorIP = creatorIP;
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
	 * @return the isCompany
	 */
	public char getIsCompany() {
		return isCompany;
	}
	/**
	 * @param isCompany the isCompany to set
	 */
	public void setIsCompany(char isCompany) {
		this.isCompany = isCompany;
	}
	/**
	 * @return the isDeleted
	 */
	public char getIsDeleted() {
		return isDeleted;
	}
	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(char isDeleted) {
		this.isDeleted = isDeleted;
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
	 * @return the linkmanMail
	 */
	public String getLinkmanMail() {
		return linkmanMail;
	}
	/**
	 * @param linkmanMail the linkmanMail to set
	 */
	public void setLinkmanMail(String linkmanMail) {
		this.linkmanMail = linkmanMail;
	}
	/**
	 * @return the linkmanQQ
	 */
	public String getLinkmanQQ() {
		return linkmanQQ;
	}
	/**
	 * @param linkmanQQ the linkmanQQ to set
	 */
	public void setLinkmanQQ(String linkmanQQ) {
		this.linkmanQQ = linkmanQQ;
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
	 * @return the isHalt
	 */
	public char getIsHalt() {
		return isHalt;
	}
	/**
	 * @param isHalt the isHalt to set
	 */
	public void setIsHalt(char isHalt) {
		this.isHalt = isHalt;
	}
	/**
	 * @return the approvalTime
	 */
	public Timestamp getApprovalTime() {
		return approvalTime;
	}
	/**
	 * @param approvalTime the approvalTime to set
	 */
	public void setApprovalTime(Timestamp approvalTime) {
		this.approvalTime = approvalTime;
	}
	/**
	 * @return the approver
	 */
	public String getApprover() {
		return approver;
	}
	/**
	 * @param approver the approver to set
	 */
	public void setApprover(String approver) {
		this.approver = approver;
	}
	/**
	 * @return the approverId
	 */
	public long getApproverId() {
		return approverId;
	}
	/**
	 * @param approverId the approverId to set
	 */
	public void setApproverId(long approverId) {
		this.approverId = approverId;
	}
	/**
	 * @return the approverIP
	 */
	public String getApproverIP() {
		return approverIP;
	}
	/**
	 * @param approverIP the approverIP to set
	 */
	public void setApproverIP(String approverIP) {
		this.approverIP = approverIP;
	}
	/**
	 * @return the isApproval
	 */
	public char getIsApproval() {
		return isApproval;
	}
	/**
	 * @param isApproval the isApproval to set
	 */
	public void setIsApproval(char isApproval) {
		this.isApproval = isApproval;
	}
}
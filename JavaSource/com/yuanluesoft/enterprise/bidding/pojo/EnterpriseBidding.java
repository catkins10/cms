package com.yuanluesoft.enterprise.bidding.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 招投标项目(enterprise_bidding)
 * @author linchuan
 *
 */
public class EnterpriseBidding extends Record {
	private String projectName; //项目名称
	private String biddingNumber; //招标编号
	private String content; //招标内容
	private String bidTarget; //标的
	private String technicalRequirement; //技术要求
	private String timeRequirement; //时间要求
	private Date tenderDate; //招标时间
	private String owner; //业主单位
	private String qualificationExamination; //资格审查,资格预审、资格后审
	private String tenderingPractice; //招标方式,公开招标、邀请招标
	private String agency; //招标代理单位名称
	private String agencyTel; //招标代理联系电话
	private String agencyFax; //招标代理传真号码
	private String agencyAddress; //招标代理联系地址
	private String agencyLinkman; //招标代理联系人
	private String depositBank; //保证金开户银行
	private String depositAccountName; //保证金帐户名称
	private String depositAccount; //保证金帐号
	private String tradingCenter; //交易中心名称
	private String tradingCenterAddress; //交易中心地址
	private double depositAmount; //投标保证金额
	private char depositPaid = '0'; //是否已缴纳
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private String remark; //备注
	
	/**
	 * 是否已缴纳保证金
	 * @return
	 */
	public String getDepositPaidStatus() {
		return depositPaid=='1' ? "√" : "";
	}
	
	/**
	 * @return the agency
	 */
	public String getAgency() {
		return agency;
	}
	/**
	 * @param agency the agency to set
	 */
	public void setAgency(String agency) {
		this.agency = agency;
	}
	/**
	 * @return the agencyAddress
	 */
	public String getAgencyAddress() {
		return agencyAddress;
	}
	/**
	 * @param agencyAddress the agencyAddress to set
	 */
	public void setAgencyAddress(String agencyAddress) {
		this.agencyAddress = agencyAddress;
	}
	/**
	 * @return the agencyFax
	 */
	public String getAgencyFax() {
		return agencyFax;
	}
	/**
	 * @param agencyFax the agencyFax to set
	 */
	public void setAgencyFax(String agencyFax) {
		this.agencyFax = agencyFax;
	}
	/**
	 * @return the agencyLinkman
	 */
	public String getAgencyLinkman() {
		return agencyLinkman;
	}
	/**
	 * @param agencyLinkman the agencyLinkman to set
	 */
	public void setAgencyLinkman(String agencyLinkman) {
		this.agencyLinkman = agencyLinkman;
	}
	/**
	 * @return the agencyTel
	 */
	public String getAgencyTel() {
		return agencyTel;
	}
	/**
	 * @param agencyTel the agencyTel to set
	 */
	public void setAgencyTel(String agencyTel) {
		this.agencyTel = agencyTel;
	}
	/**
	 * @return the biddingNumber
	 */
	public String getBiddingNumber() {
		return biddingNumber;
	}
	/**
	 * @param biddingNumber the biddingNumber to set
	 */
	public void setBiddingNumber(String biddingNumber) {
		this.biddingNumber = biddingNumber;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the depositAccount
	 */
	public String getDepositAccount() {
		return depositAccount;
	}
	/**
	 * @param depositAccount the depositAccount to set
	 */
	public void setDepositAccount(String depositAccount) {
		this.depositAccount = depositAccount;
	}
	/**
	 * @return the depositAccountName
	 */
	public String getDepositAccountName() {
		return depositAccountName;
	}
	/**
	 * @param depositAccountName the depositAccountName to set
	 */
	public void setDepositAccountName(String depositAccountName) {
		this.depositAccountName = depositAccountName;
	}
	/**
	 * @return the depositAmount
	 */
	public double getDepositAmount() {
		return depositAmount;
	}
	/**
	 * @param depositAmount the depositAmount to set
	 */
	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}
	/**
	 * @return the depositBank
	 */
	public String getDepositBank() {
		return depositBank;
	}
	/**
	 * @param depositBank the depositBank to set
	 */
	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}
	/**
	 * @return the depositPaid
	 */
	public char getDepositPaid() {
		return depositPaid;
	}
	/**
	 * @param depositPaid the depositPaid to set
	 */
	public void setDepositPaid(char depositPaid) {
		this.depositPaid = depositPaid;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
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
	 * @return the qualificationExamination
	 */
	public String getQualificationExamination() {
		return qualificationExamination;
	}
	/**
	 * @param qualificationExamination the qualificationExamination to set
	 */
	public void setQualificationExamination(String qualificationExamination) {
		this.qualificationExamination = qualificationExamination;
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
	 * @return the technicalRequirement
	 */
	public String getTechnicalRequirement() {
		return technicalRequirement;
	}
	/**
	 * @param technicalRequirement the technicalRequirement to set
	 */
	public void setTechnicalRequirement(String technicalRequirement) {
		this.technicalRequirement = technicalRequirement;
	}
	/**
	 * @return the tenderingPractice
	 */
	public String getTenderingPractice() {
		return tenderingPractice;
	}
	/**
	 * @param tenderingPractice the tenderingPractice to set
	 */
	public void setTenderingPractice(String tenderingPractice) {
		this.tenderingPractice = tenderingPractice;
	}
	/**
	 * @return the timeRequirement
	 */
	public String getTimeRequirement() {
		return timeRequirement;
	}
	/**
	 * @param timeRequirement the timeRequirement to set
	 */
	public void setTimeRequirement(String timeRequirement) {
		this.timeRequirement = timeRequirement;
	}
	/**
	 * @return the tradingCenter
	 */
	public String getTradingCenter() {
		return tradingCenter;
	}
	/**
	 * @param tradingCenter the tradingCenter to set
	 */
	public void setTradingCenter(String tradingCenter) {
		this.tradingCenter = tradingCenter;
	}
	/**
	 * @return the tradingCenterAddress
	 */
	public String getTradingCenterAddress() {
		return tradingCenterAddress;
	}
	/**
	 * @param tradingCenterAddress the tradingCenterAddress to set
	 */
	public void setTradingCenterAddress(String tradingCenterAddress) {
		this.tradingCenterAddress = tradingCenterAddress;
	}

	/**
	 * @return the tenderDate
	 */
	public Date getTenderDate() {
		return tenderDate;
	}

	/**
	 * @param tenderDate the tenderDate to set
	 */
	public void setTenderDate(Date tenderDate) {
		this.tenderDate = tenderDate;
	}

	/**
	 * @return the bidTarget
	 */
	public String getBidTarget() {
		return bidTarget;
	}

	/**
	 * @param bidTarget the bidTarget to set
	 */
	public void setBidTarget(String bidTarget) {
		this.bidTarget = bidTarget;
	}
}

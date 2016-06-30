package com.yuanluesoft.cms.infopublic.request.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;

/**
 * 
 * @author linchuan
 *
 */
public class Request extends PublicServiceAdminForm {
	private char proposerType = '0'; //申请人类型,公民,法人/其他组织
	private String code; //机构代码
	private String legalRepresentative; //法人代表
	private String unit; //涉及单位
	private long unitId; //涉及单位ID
	private String purpose; //用途
	private String medium; //提供方式,纸面/电子邮件/光盘/磁盘
	private String receiveMode; //获取信息的方式,邮寄/快递/电子邮件/传真/自行领取/当场阅读、抄录
	private String approvalResult; //审批结果,同意公开/同意部分公开/不公开
	private String notPublicType; //不公开类别,信息不存在/非本部门掌握/申请内容不明确/免予公开/其他原因未能提供信息
	private String notPublicReason; //其他未能提供信息的原因
	private String doneMedium; //实际提供方式
	private String doneReceiveMode; //实际获取信息的方式
	private Timestamp approvalTime; //审批时间
	private long registrantId; //登记人ID,非网络申请时有效
	private String registrant; //登记人姓名,非网络申请时有效
	private String applyMode; //申请方式,电话、传真等，非网络申请时有效
	private Set notify; //告知书
	
	private String siteName; //站点名称
	private String[] mediums;
	private String[] receiveModes;
	
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
	 * @return the medium
	 */
	public String getMedium() {
		return medium;
	}
	/**
	 * @param medium the medium to set
	 */
	public void setMedium(String medium) {
		this.medium = medium;
	}
	/**
	 * @return the proposerType
	 */
	public char getProposerType() {
		return proposerType;
	}
	/**
	 * @param proposerType the proposerType to set
	 */
	public void setProposerType(char proposerType) {
		this.proposerType = proposerType;
	}
	/**
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose;
	}
	/**
	 * @param purpose the purpose to set
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	/**
	 * @return the receiveMode
	 */
	public String getReceiveMode() {
		return receiveMode;
	}
	/**
	 * @param receiveMode the receiveMode to set
	 */
	public void setReceiveMode(String receiveMode) {
		this.receiveMode = receiveMode;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the doneMedium
	 */
	public String getDoneMedium() {
		return doneMedium;
	}
	/**
	 * @param doneMedium the doneMedium to set
	 */
	public void setDoneMedium(String doneMedium) {
		this.doneMedium = doneMedium;
	}
	/**
	 * @return the doneReceiveMode
	 */
	public String getDoneReceiveMode() {
		return doneReceiveMode;
	}
	/**
	 * @param doneReceiveMode the doneReceiveMode to set
	 */
	public void setDoneReceiveMode(String doneReceiveMode) {
		this.doneReceiveMode = doneReceiveMode;
	}
	/**
	 * @return the approvalResult
	 */
	public String getApprovalResult() {
		return approvalResult;
	}
	/**
	 * @param approvalResult the approvalResult to set
	 */
	public void setApprovalResult(String approvalResult) {
		this.approvalResult = approvalResult;
	}
	/**
	 * @return the notPublicReason
	 */
	public String getNotPublicReason() {
		return notPublicReason;
	}
	/**
	 * @param notPublicReason the notPublicReason to set
	 */
	public void setNotPublicReason(String notPublicReason) {
		this.notPublicReason = notPublicReason;
	}
	/**
	 * @return the notPublicType
	 */
	public String getNotPublicType() {
		return notPublicType;
	}
	/**
	 * @param notPublicType the notPublicType to set
	 */
	public void setNotPublicType(String notPublicType) {
		this.notPublicType = notPublicType;
	}
	/**
	 * @return the applyMode
	 */
	public String getApplyMode() {
		return applyMode;
	}
	/**
	 * @param applyMode the applyMode to set
	 */
	public void setApplyMode(String applyMode) {
		this.applyMode = applyMode;
	}
	/**
	 * @return the registrant
	 */
	public String getRegistrant() {
		return registrant;
	}
	/**
	 * @param registrant the registrant to set
	 */
	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}
	/**
	 * @return the registrantId
	 */
	public long getRegistrantId() {
		return registrantId;
	}
	/**
	 * @param registrantId the registrantId to set
	 */
	public void setRegistrantId(long registrantId) {
		this.registrantId = registrantId;
	}
	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}
	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * @return the mediums
	 */
	public String[] getMediums() {
		return mediums;
	}
	/**
	 * @param mediums the mediums to set
	 */
	public void setMediums(String[] mediums) {
		this.mediums = mediums;
	}
	/**
	 * @return the receiveModes
	 */
	public String[] getReceiveModes() {
		return receiveModes;
	}
	/**
	 * @param receiveModes the receiveModes to set
	 */
	public void setReceiveModes(String[] receiveModes) {
		this.receiveModes = receiveModes;
	}
	/**
	 * @return the notify
	 */
	public Set getNotify() {
		return notify;
	}
	/**
	 * @param notify the notify to set
	 */
	public void setNotify(Set notify) {
		this.notify = notify;
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

}
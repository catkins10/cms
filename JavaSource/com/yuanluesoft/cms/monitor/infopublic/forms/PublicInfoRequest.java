package com.yuanluesoft.cms.monitor.infopublic.forms;

import java.sql.Timestamp;

import com.yuanluesoft.cms.monitor.forms.MonitorRecord;

/**
 * 
 * @author linchuan
 *
 */
public class PublicInfoRequest extends MonitorRecord {
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String recordId; //记录ID
	private Timestamp captureTime; //采集时间
	private String proposerType; //申请人类型,公民,法人/其他组织
	private String applyMode; //申请方式,网络、电话、传真
	private String creator; //申请人
	private String creatorMobile; //申请人手机
	private String creatorMail; //申请人邮箱
	private String creatorTel; //联系电话
	private String code; //机构代码
	private String legalRepresentative; //法人代表
	private String creatorIP; //申请人IP
	private String content; //内容描述
	private String purpose; //用途
	private Timestamp created; //申请时间
	private String medium; //提供方式,纸面/电子邮件/光盘/磁盘
	private String receiveMode; //获取信息的方式,邮寄/快递/电子邮件/传真/自行领取/当场阅读、抄录
	private String approvalResult; //审批结果,同意公开/同意部分公开/不公开
	private String notPublicType; //不公开类别,信息不存在/非本部门掌握/申请内容不明确/免予公开/其他原因未能提供信息
	private String notPublicReason; //其他未能提供信息的原因
	private String doneMedium; //实际提供方式
	private String doneReceiveMode; //实际信息获取方式
	private Timestamp approvalTime; //审批时间
	
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
	 * @return the captureTime
	 */
	public Timestamp getCaptureTime() {
		return captureTime;
	}
	/**
	 * @param captureTime the captureTime to set
	 */
	public void setCaptureTime(Timestamp captureTime) {
		this.captureTime = captureTime;
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
	 * @return the creatorMail
	 */
	public String getCreatorMail() {
		return creatorMail;
	}
	/**
	 * @param creatorMail the creatorMail to set
	 */
	public void setCreatorMail(String creatorMail) {
		this.creatorMail = creatorMail;
	}
	/**
	 * @return the creatorMobile
	 */
	public String getCreatorMobile() {
		return creatorMobile;
	}
	/**
	 * @param creatorMobile the creatorMobile to set
	 */
	public void setCreatorMobile(String creatorMobile) {
		this.creatorMobile = creatorMobile;
	}
	/**
	 * @return the creatorTel
	 */
	public String getCreatorTel() {
		return creatorTel;
	}
	/**
	 * @param creatorTel the creatorTel to set
	 */
	public void setCreatorTel(String creatorTel) {
		this.creatorTel = creatorTel;
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
	 * @return the proposerType
	 */
	public String getProposerType() {
		return proposerType;
	}
	/**
	 * @param proposerType the proposerType to set
	 */
	public void setProposerType(String proposerType) {
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
	 * @return the recordId
	 */
	public String getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
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
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
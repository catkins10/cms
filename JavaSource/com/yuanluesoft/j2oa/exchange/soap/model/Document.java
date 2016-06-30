package com.yuanluesoft.j2oa.exchange.soap.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 公文
 * @author linchuan
 *
 */
public class Document implements Serializable {
	private long id; //ID
	private String subject; //标题
	private String documentUnit; //发文单位
	private String sign; //签发人
	private String docWord; //发文字号
	private Calendar generateDate; //成文日期
	private String docType; //发文种类
	private String secureLevel; //秘密等级
	private String secureTerm; //保密期限
	private String priority; //紧急程度
	private String keyword; //主题词
	private int printNumber; //印发份数
	private Calendar distributeDate; //印发日期
	private String mainSend; //主送单位
	private String copySend; //抄送单位
	private String otherSend; //其他接收单位
	private Calendar created; //创建时间
	private long creatorId; //创建人ID
	private String creator; //创建人
	private String creatorUnit; //创建人所在单位
	private long creatorUnitId; //创建人所在单位ID
	private int issue = 0; //是否发布
	private long issuePersonId; //最后发布人Id
	private String issuePerson; //最后发布人
	private Calendar issueTime; //最后发布时间
	private String remark; //备注
	private String[] bodyFileNames; //正文文件名称列表
	private String[] attachmentFileNames; //附件文件名称列表
	
	/**
	 * @return the copySend
	 */
	public String getCopySend() {
		return copySend;
	}
	/**
	 * @param copySend the copySend to set
	 */
	public void setCopySend(String copySend) {
		this.copySend = copySend;
	}
	/**
	 * @return the created
	 */
	public Calendar getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Calendar created) {
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
	 * @return the creatorUnit
	 */
	public String getCreatorUnit() {
		return creatorUnit;
	}
	/**
	 * @param creatorUnit the creatorUnit to set
	 */
	public void setCreatorUnit(String creatorUnit) {
		this.creatorUnit = creatorUnit;
	}
	/**
	 * @return the creatorUnitId
	 */
	public long getCreatorUnitId() {
		return creatorUnitId;
	}
	/**
	 * @param creatorUnitId the creatorUnitId to set
	 */
	public void setCreatorUnitId(long creatorUnitId) {
		this.creatorUnitId = creatorUnitId;
	}
	/**
	 * @return the distributeDate
	 */
	public Calendar getDistributeDate() {
		return distributeDate;
	}
	/**
	 * @param distributeDate the distributeDate to set
	 */
	public void setDistributeDate(Calendar distributeDate) {
		this.distributeDate = distributeDate;
	}
	/**
	 * @return the docType
	 */
	public String getDocType() {
		return docType;
	}
	/**
	 * @param docType the docType to set
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}
	/**
	 * @return the documentUnit
	 */
	public String getDocumentUnit() {
		return documentUnit;
	}
	/**
	 * @param documentUnit the documentUnit to set
	 */
	public void setDocumentUnit(String documentUnit) {
		this.documentUnit = documentUnit;
	}
	/**
	 * @return the docWord
	 */
	public String getDocWord() {
		return docWord;
	}
	/**
	 * @param docWord the docWord to set
	 */
	public void setDocWord(String docWord) {
		this.docWord = docWord;
	}
	/**
	 * @return the generateDate
	 */
	public Calendar getGenerateDate() {
		return generateDate;
	}
	/**
	 * @param generateDate the generateDate to set
	 */
	public void setGenerateDate(Calendar generateDate) {
		this.generateDate = generateDate;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the issue
	 */
	public int getIssue() {
		return issue;
	}
	/**
	 * @param issue the issue to set
	 */
	public void setIssue(int issue) {
		this.issue = issue;
	}
	/**
	 * @return the issuePerson
	 */
	public String getIssuePerson() {
		return issuePerson;
	}
	/**
	 * @param issuePerson the issuePerson to set
	 */
	public void setIssuePerson(String issuePerson) {
		this.issuePerson = issuePerson;
	}
	/**
	 * @return the issuePersonId
	 */
	public long getIssuePersonId() {
		return issuePersonId;
	}
	/**
	 * @param issuePersonId the issuePersonId to set
	 */
	public void setIssuePersonId(long issuePersonId) {
		this.issuePersonId = issuePersonId;
	}
	/**
	 * @return the issueTime
	 */
	public Calendar getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Calendar issueTime) {
		this.issueTime = issueTime;
	}
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return the mainSend
	 */
	public String getMainSend() {
		return mainSend;
	}
	/**
	 * @param mainSend the mainSend to set
	 */
	public void setMainSend(String mainSend) {
		this.mainSend = mainSend;
	}
	/**
	 * @return the otherSend
	 */
	public String getOtherSend() {
		return otherSend;
	}
	/**
	 * @param otherSend the otherSend to set
	 */
	public void setOtherSend(String otherSend) {
		this.otherSend = otherSend;
	}
	/**
	 * @return the printNumber
	 */
	public int getPrintNumber() {
		return printNumber;
	}
	/**
	 * @param printNumber the printNumber to set
	 */
	public void setPrintNumber(int printNumber) {
		this.printNumber = printNumber;
	}
	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
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
	 * @return the secureLevel
	 */
	public String getSecureLevel() {
		return secureLevel;
	}
	/**
	 * @param secureLevel the secureLevel to set
	 */
	public void setSecureLevel(String secureLevel) {
		this.secureLevel = secureLevel;
	}
	/**
	 * @return the secureTerm
	 */
	public String getSecureTerm() {
		return secureTerm;
	}
	/**
	 * @param secureTerm the secureTerm to set
	 */
	public void setSecureTerm(String secureTerm) {
		this.secureTerm = secureTerm;
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
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the attachmentFileNames
	 */
	public String[] getAttachmentFileNames() {
		return attachmentFileNames;
	}
	/**
	 * @param attachmentFileNames the attachmentFileNames to set
	 */
	public void setAttachmentFileNames(String[] attachmentFileNames) {
		this.attachmentFileNames = attachmentFileNames;
	}
	/**
	 * @return the bodyFileNames
	 */
	public String[] getBodyFileNames() {
		return bodyFileNames;
	}
	/**
	 * @param bodyFileNames the bodyFileNames to set
	 */
	public void setBodyFileNames(String[] bodyFileNames) {
		this.bodyFileNames = bodyFileNames;
	}
}
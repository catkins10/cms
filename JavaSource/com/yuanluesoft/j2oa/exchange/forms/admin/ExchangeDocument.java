package com.yuanluesoft.j2oa.exchange.forms.admin;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocumentUnit;
import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 公文
 * @author linchuan
 *
 */
public class ExchangeDocument extends ActionForm {
	private String sourceRecordId; //源记录ID
	private String subject; //标题
	private String documentUnit; //发文单位
	private String sign; //签发人
	private String docWord; //发文字号
	private Date generateDate; //成文日期
	private String docType; //发文种类
	private String secureLevel; //秘密等级
	private String secureTerm; //保密期限
	private String priority; //紧急程度
	private String keyword; //主题词
	private int printNumber; //印发份数
	private Date distributeDate; //印发日期
	private String mainSend; //主送单位
	private String copySend; //抄送单位
	private String otherSend; //其他接收单位
	private Timestamp created; //创建时间
	private long creatorId; //创建人ID
	private String creator; //创建人
	private String creatorUnit; //创建人所在单位
	private long creatorUnitId; //创建人所在单位ID
	private char issue = '0'; //是否发布
	private long issuePersonId; //最后发布人Id
	private String issuePerson; //最后发布人
	private Timestamp issueTime; //最后发布时间
	private String remark; //备注
	private Set exchangeUnits; //接收单位列表
	private Set exchangeUndos; //撤销记录
	private Set exchangeMessages; //反馈
	
	//扩展属性
	private ExchangeDocumentUnit currentUnit; //当前用户所在单位交换记录 
	//撤销选项
	private boolean undo; //是否撤销发布
	private String undoReason; //撤销发布的原因
	private char resign; //是否重新签收
	
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
	public Date getDistributeDate() {
		return distributeDate;
	}
	/**
	 * @param distributeDate the distributeDate to set
	 */
	public void setDistributeDate(Date distributeDate) {
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
	 * @return the exchangeUndos
	 */
	public Set getExchangeUndos() {
		return exchangeUndos;
	}
	/**
	 * @param exchangeUndos the exchangeUndos to set
	 */
	public void setExchangeUndos(Set exchangeUndos) {
		this.exchangeUndos = exchangeUndos;
	}
	/**
	 * @return the exchangeUnits
	 */
	public Set getExchangeUnits() {
		return exchangeUnits;
	}
	/**
	 * @param exchangeUnits the exchangeUnits to set
	 */
	public void setExchangeUnits(Set exchangeUnits) {
		this.exchangeUnits = exchangeUnits;
	}
	/**
	 * @return the generateDate
	 */
	public Date getGenerateDate() {
		return generateDate;
	}
	/**
	 * @param generateDate the generateDate to set
	 */
	public void setGenerateDate(Date generateDate) {
		this.generateDate = generateDate;
	}
	/**
	 * @return the issue
	 */
	public char getIssue() {
		return issue;
	}
	/**
	 * @param issue the issue to set
	 */
	public void setIssue(char issue) {
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
	public Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Timestamp issueTime) {
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
	 * @return the resign
	 */
	public char getResign() {
		return resign;
	}
	/**
	 * @param resign the resign to set
	 */
	public void setResign(char resign) {
		this.resign = resign;
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
	 * @return the undo
	 */
	public boolean isUndo() {
		return undo;
	}
	/**
	 * @param undo the undo to set
	 */
	public void setUndo(boolean undo) {
		this.undo = undo;
	}
	/**
	 * @return the undoReason
	 */
	public String getUndoReason() {
		return undoReason;
	}
	/**
	 * @param undoReason the undoReason to set
	 */
	public void setUndoReason(String undoReason) {
		this.undoReason = undoReason;
	}
	/**
	 * @return the currentUnit
	 */
	public ExchangeDocumentUnit getCurrentUnit() {
		return currentUnit;
	}
	/**
	 * @param currentUnit the currentUnit to set
	 */
	public void setCurrentUnit(ExchangeDocumentUnit currentUnit) {
		this.currentUnit = currentUnit;
	}
	/**
	 * @return the exchangeMessages
	 */
	public Set getExchangeMessages() {
		return exchangeMessages;
	}
	/**
	 * @param exchangeMessages the exchangeMessages to set
	 */
	public void setExchangeMessages(Set exchangeMessages) {
		this.exchangeMessages = exchangeMessages;
	}
	/**
	 * @return the sourceRecordId
	 */
	public String getSourceRecordId() {
		return sourceRecordId;
	}
	/**
	 * @param sourceRecordId the sourceRecordId to set
	 */
	public void setSourceRecordId(String sourceRecordId) {
		this.sourceRecordId = sourceRecordId;
	}	
}
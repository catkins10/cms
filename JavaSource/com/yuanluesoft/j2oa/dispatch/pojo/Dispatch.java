/*
 * Created on 2004-8-19
 *
 */
package com.yuanluesoft.j2oa.dispatch.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 发文单(dispatch_dispatch)
 * @author linchuan
 *
 */
public class Dispatch extends WorkflowData {
	private String workflowInstanceId; //工作流实例ID
	private String subject; //标题
	private String docType; //发文种类
	private String secureLevel; //秘密等级
	private String secureTerm; //保密期限
	private String priority; //紧急程度
	private String mainSend; //主送机关
	private String copySend; //抄送机关
	private String keyword; //主题词
	private String docMark; //机关代字
	private int markYear; //机关代字年份
	private int markSequence; //机关代字序号
	private String docWord; //发文字号
	private int printNumber; //打印份数
	private int pageCount; //页数
	private String queryLevel; //查询级别
	private String distributeRange; //分发范围
	private String draftDepartment; //起草部门
	private String draftPerson; //起草人
	private Timestamp draftDate; //起草时间
	private String signPerson; //签发人
	private Date signDate; //签发时间
	private Date generateDate; //生成时间
	private Date distributeDate; //印发日期
	private Timestamp filingTime; //归档时间
	private String remark; //附注
	private String publicType; //公开类型,主动公开/依申请公开/不公开
	private String publicReason; //不公开的理由
	private Set bodies; //正文
	
	/**
	 * @return the bodies
	 */
	public Set getBodies() {
		return bodies;
	}
	/**
	 * @param bodies the bodies to set
	 */
	public void setBodies(Set bodies) {
		this.bodies = bodies;
	}
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
	 * @return the distributeRange
	 */
	public String getDistributeRange() {
		return distributeRange;
	}
	/**
	 * @param distributeRange the distributeRange to set
	 */
	public void setDistributeRange(String distributeRange) {
		this.distributeRange = distributeRange;
	}
	/**
	 * @return the docMark
	 */
	public String getDocMark() {
		return docMark;
	}
	/**
	 * @param docMark the docMark to set
	 */
	public void setDocMark(String docMark) {
		this.docMark = docMark;
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
	 * @return the draftDate
	 */
	public Timestamp getDraftDate() {
		return draftDate;
	}
	/**
	 * @param draftDate the draftDate to set
	 */
	public void setDraftDate(Timestamp draftDate) {
		this.draftDate = draftDate;
	}
	/**
	 * @return the draftDepartment
	 */
	public String getDraftDepartment() {
		return draftDepartment;
	}
	/**
	 * @param draftDepartment the draftDepartment to set
	 */
	public void setDraftDepartment(String draftDepartment) {
		this.draftDepartment = draftDepartment;
	}
	/**
	 * @return the draftPerson
	 */
	public String getDraftPerson() {
		return draftPerson;
	}
	/**
	 * @param draftPerson the draftPerson to set
	 */
	public void setDraftPerson(String draftPerson) {
		this.draftPerson = draftPerson;
	}
	/**
	 * @return the filingTime
	 */
	public Timestamp getFilingTime() {
		return filingTime;
	}
	/**
	 * @param filingTime the filingTime to set
	 */
	public void setFilingTime(Timestamp filingTime) {
		this.filingTime = filingTime;
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
	 * @return the markSequence
	 */
	public int getMarkSequence() {
		return markSequence;
	}
	/**
	 * @param markSequence the markSequence to set
	 */
	public void setMarkSequence(int markSequence) {
		this.markSequence = markSequence;
	}
	/**
	 * @return the markYear
	 */
	public int getMarkYear() {
		return markYear;
	}
	/**
	 * @param markYear the markYear to set
	 */
	public void setMarkYear(int markYear) {
		this.markYear = markYear;
	}
	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}
	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
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
	 * @return the publicReason
	 */
	public String getPublicReason() {
		return publicReason;
	}
	/**
	 * @param publicReason the publicReason to set
	 */
	public void setPublicReason(String publicReason) {
		this.publicReason = publicReason;
	}
	/**
	 * @return the publicType
	 */
	public String getPublicType() {
		return publicType;
	}
	/**
	 * @param publicType the publicType to set
	 */
	public void setPublicType(String publicType) {
		this.publicType = publicType;
	}
	/**
	 * @return the queryLevel
	 */
	public String getQueryLevel() {
		return queryLevel;
	}
	/**
	 * @param queryLevel the queryLevel to set
	 */
	public void setQueryLevel(String queryLevel) {
		this.queryLevel = queryLevel;
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
	 * @return the signDate
	 */
	public Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the signPerson
	 */
	public String getSignPerson() {
		return signPerson;
	}
	/**
	 * @param signPerson the signPerson to set
	 */
	public void setSignPerson(String signPerson) {
		this.signPerson = signPerson;
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
}
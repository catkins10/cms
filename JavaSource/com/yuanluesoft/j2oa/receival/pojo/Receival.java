/*
 * Created on 2005-10-8
 *
 */
package com.yuanluesoft.j2oa.receival.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 收文单(receival_receival)
 * @author linchuan
 *
 */
public class Receival extends WorkflowData {
	private String docWord; //文件字号,文件字号
	private String fromUnit; //来文单位,来文单位
	private String registPerson; //登记人,登记人
	private String subject; //标题,标题
	private int sequence; //收文序号(TODO)
	private String docType; //文件分类
	private String secureLevel; //秘密等级,秘密等级
	private String secureTerm; //保密期限,保密期限
	private String priority; //紧急程度,紧急程度
	private int receivalCount; //份数,份数
	private int pageCount; //页数,页数
	private Date signDate; //成文日期,成文日期
	private String registDepartment; //登记部门,登记部门
	private Date receivalDate; //收文日期,收文日期
	private Date transactDate; //办理期限,办理期限
	private String keyword; //主题词,主题词
	private String content; //来文摘要,来文摘要
	private Timestamp filingTime; //归档时间
	private String mainDo; //主办部门
	private String remark; //附注,附注
	
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
	 * @return the fromUnit
	 */
	public String getFromUnit() {
		return fromUnit;
	}
	/**
	 * @param fromUnit the fromUnit to set
	 */
	public void setFromUnit(String fromUnit) {
		this.fromUnit = fromUnit;
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
	 * @return the mainDo
	 */
	public String getMainDo() {
		return mainDo;
	}
	/**
	 * @param mainDo the mainDo to set
	 */
	public void setMainDo(String mainDo) {
		this.mainDo = mainDo;
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
	 * @return the receivalCount
	 */
	public int getReceivalCount() {
		return receivalCount;
	}
	/**
	 * @param receivalCount the receivalCount to set
	 */
	public void setReceivalCount(int receivalCount) {
		this.receivalCount = receivalCount;
	}
	/**
	 * @return the receivalDate
	 */
	public Date getReceivalDate() {
		return receivalDate;
	}
	/**
	 * @param receivalDate the receivalDate to set
	 */
	public void setReceivalDate(Date receivalDate) {
		this.receivalDate = receivalDate;
	}
	/**
	 * @return the registDepartment
	 */
	public String getRegistDepartment() {
		return registDepartment;
	}
	/**
	 * @param registDepartment the registDepartment to set
	 */
	public void setRegistDepartment(String registDepartment) {
		this.registDepartment = registDepartment;
	}
	/**
	 * @return the registPerson
	 */
	public String getRegistPerson() {
		return registPerson;
	}
	/**
	 * @param registPerson the registPerson to set
	 */
	public void setRegistPerson(String registPerson) {
		this.registPerson = registPerson;
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
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
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
	 * @return the transactDate
	 */
	public Date getTransactDate() {
		return transactDate;
	}
	/**
	 * @param transactDate the transactDate to set
	 */
	public void setTransactDate(Date transactDate) {
		this.transactDate = transactDate;
	}
}
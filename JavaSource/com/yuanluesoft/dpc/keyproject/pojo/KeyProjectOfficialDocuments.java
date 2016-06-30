package com.yuanluesoft.dpc.keyproject.pojo;

import java.sql.Date;

/**
 * 项目审批文件(keyproject_official_documents)
 * @author linchuan
 *
 */
public class KeyProjectOfficialDocuments extends KeyProjectComponent {
	private String approvalDocuments; //报批文件,业主单位或者项目负责单位提交的报批文件标题
	private String documentNumber; //批准文号,批准该项目的公文文号
	private String subject; //标题,批转该项目的公文标题
	private String body; //文件正文,批转公文的正文内容附件
	private Date approvalDate; //批准日期,批准公文的成文日期
	
	/**
	 * @return the approvalDate
	 */
	public Date getApprovalDate() {
		return approvalDate;
	}
	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	/**
	 * @return the approvalDocuments
	 */
	public String getApprovalDocuments() {
		return approvalDocuments;
	}
	/**
	 * @param approvalDocuments the approvalDocuments to set
	 */
	public void setApprovalDocuments(String approvalDocuments) {
		this.approvalDocuments = approvalDocuments;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the documentNumber
	 */
	public String getDocumentNumber() {
		return documentNumber;
	}
	/**
	 * @param documentNumber the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
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
}
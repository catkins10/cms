package com.yuanluesoft.cms.inquiry.forms.admin;


/**
 * 
 * @author linchuan
 *
 */
public class Template  extends com.yuanluesoft.cms.templatemanage.forms.Template {
	private long subjectId; //隶属调查主题ID
	private String subject; //隶属调查主题
	
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
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}
	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
}
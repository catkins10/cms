package com.yuanluesoft.cms.inquiry.pojo;

import com.yuanluesoft.cms.templatemanage.pojo.Template;

/**
 * 在线调查:模板配置(cms_inquiry_template)
 * @author linchuan
 *
 */
public class InquiryTemplate extends Template {
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
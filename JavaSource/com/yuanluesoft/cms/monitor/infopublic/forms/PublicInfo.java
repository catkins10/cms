package com.yuanluesoft.cms.monitor.infopublic.forms;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.cms.monitor.forms.MonitorRecord;

/**
 * 
 * @author linchuan
 *
 */
public class PublicInfo extends MonitorRecord {
	private String subject; //标题
	private String body; //正文
	private String infoIndex; //索引号
	private String infoFrom; //发布机构
	private String mark; //文号
	private Date generateDate; //生成日期
	private String creator; //创建人
	private Timestamp created; //创建时间
	private Timestamp issueTime; //发布时间
	private String summarize; //内容概述
	private String category; //主题分类
	private String keywords; //主题词
	private String directoryName; //目录名称
	
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
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
	 * @return the infoFrom
	 */
	public String getInfoFrom() {
		return infoFrom;
	}
	/**
	 * @param infoFrom the infoFrom to set
	 */
	public void setInfoFrom(String infoFrom) {
		this.infoFrom = infoFrom;
	}
	/**
	 * @return the infoIndex
	 */
	public String getInfoIndex() {
		return infoIndex;
	}
	/**
	 * @param infoIndex the infoIndex to set
	 */
	public void setInfoIndex(String infoIndex) {
		this.infoIndex = infoIndex;
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
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the mark
	 */
	public String getMark() {
		return mark;
	}
	/**
	 * @param mark the mark to set
	 */
	public void setMark(String mark) {
		this.mark = mark;
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
	 * @return the summarize
	 */
	public String getSummarize() {
		return summarize;
	}
	/**
	 * @param summarize the summarize to set
	 */
	public void setSummarize(String summarize) {
		this.summarize = summarize;
	}
}
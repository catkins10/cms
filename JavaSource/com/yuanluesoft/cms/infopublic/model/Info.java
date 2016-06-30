package com.yuanluesoft.cms.infopublic.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * 信息(用于数据交换)
 * @author linchuan
 *
 */
public class Info implements Serializable {
	private String infoId; //信息在当前系统中的ID
	private String infoUrl; //信息在当前系统中的URL
	private String infoIndex; //索引号
	private String infoFrom; //发布机构
	private String mark; //文号
	private Date generateDate; //生成日期
	private String subject; //标题
	private String body; //HTML正文
	private String charset; //字符集
	private Timestamp created; //创建时间
	private Timestamp issueTime; //发布时间
	private String summarize; //内容概述
	private String category; //主题分类
	private String keywords; //主题词
	private boolean isDirectIssue; //是否直接发布
	private long directoryId; //信息隶属目录ID
	private String creatorName; //创建人
	private String creatorDepartmentName; //创建人所在部门
	private String creatorUnitName; //创建人所在单位
	private List attachmentFilePaths; //附件文件路径列表
	private List imageFilePaths; //图片文件路径列表
	private List videoFilePaths; //视频文件路径列表
	
	/**
	 * @return the attachmentFilePaths
	 */
	public List getAttachmentFilePaths() {
		return attachmentFilePaths;
	}
	/**
	 * @param attachmentFilePaths the attachmentFilePaths to set
	 */
	public void setAttachmentFilePaths(List attachmentFilePaths) {
		this.attachmentFilePaths = attachmentFilePaths;
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
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
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
	 * @return the creatorDepartmentName
	 */
	public String getCreatorDepartmentName() {
		return creatorDepartmentName;
	}
	/**
	 * @param creatorDepartmentName the creatorDepartmentName to set
	 */
	public void setCreatorDepartmentName(String creatorDepartmentName) {
		this.creatorDepartmentName = creatorDepartmentName;
	}
	/**
	 * @return the creatorName
	 */
	public String getCreatorName() {
		return creatorName;
	}
	/**
	 * @param creatorName the creatorName to set
	 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	/**
	 * @return the creatorUnitName
	 */
	public String getCreatorUnitName() {
		return creatorUnitName;
	}
	/**
	 * @param creatorUnitName the creatorUnitName to set
	 */
	public void setCreatorUnitName(String creatorUnitName) {
		this.creatorUnitName = creatorUnitName;
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
	 * @return the imageFilePaths
	 */
	public List getImageFilePaths() {
		return imageFilePaths;
	}
	/**
	 * @param imageFilePaths the imageFilePaths to set
	 */
	public void setImageFilePaths(List imageFilePaths) {
		this.imageFilePaths = imageFilePaths;
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
	 * @return the infoId
	 */
	public String getInfoId() {
		return infoId;
	}
	/**
	 * @param infoId the infoId to set
	 */
	public void setInfoId(String infoId) {
		this.infoId = infoId;
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
	 * @return the infoUrl
	 */
	public String getInfoUrl() {
		return infoUrl;
	}
	/**
	 * @param infoUrl the infoUrl to set
	 */
	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}
	/**
	 * @return the isDirectIssue
	 */
	public boolean isDirectIssue() {
		return isDirectIssue;
	}
	/**
	 * @param isDirectIssue the isDirectIssue to set
	 */
	public void setDirectIssue(boolean isDirectIssue) {
		this.isDirectIssue = isDirectIssue;
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
	/**
	 * @return the videoFilePaths
	 */
	public List getVideoFilePaths() {
		return videoFilePaths;
	}
	/**
	 * @param videoFilePaths the videoFilePaths to set
	 */
	public void setVideoFilePaths(List videoFilePaths) {
		this.videoFilePaths = videoFilePaths;
	}
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
}
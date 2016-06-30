package com.yuanluesoft.cms.siteresource.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class Article implements Serializable {
	private String articleId; //文章在当前系统中的ID
	private String articleUrl; //文章在当前系统中的URL
	private String subject; //标题
	private String subhead; //副标题
	private String body; //HTML正文
	private String charset; //字符集
	private String columnIds; //文章隶属栏目ID列表,用逗号分隔
	private String source; //来源
	private String author; //作者
	private String keyword; //主题词
	private String mark; //文号
	private char anonymousLevel; //匿名用户访问级别,1/不能访问,2/仅标题,3/完全访问
	private Timestamp created; //创建时间
	private Timestamp issueTime; //发布时间
	private boolean isDirectIssue; //是否直接发布
	private String creatorName; //创建人
	private String creatorDepartmentName; //创建人所在部门
	private String creatorUnitName; //创建人所在单位
	private List attachmentFilePaths; //附件文件路径列表
	private List imageFilePaths; //图片文件路径列表
	private List videoFilePaths; //视频文件路径列表
	
	/**
	 * @return the anonymousLevel
	 */
	public char getAnonymousLevel() {
		return anonymousLevel;
	}
	/**
	 * @param anonymousLevel the anonymousLevel to set
	 */
	public void setAnonymousLevel(char anonymousLevel) {
		this.anonymousLevel = anonymousLevel;
	}
	/**
	 * @return the articleId
	 */
	public String getArticleId() {
		return articleId;
	}
	/**
	 * @param articleId the articleId to set
	 */
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
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
	 * @return the columnIds
	 */
	public String getColumnIds() {
		return columnIds;
	}
	/**
	 * @param columnIds the columnIds to set
	 */
	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
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
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the subhead
	 */
	public String getSubhead() {
		return subhead;
	}
	/**
	 * @param subhead the subhead to set
	 */
	public void setSubhead(String subhead) {
		this.subhead = subhead;
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
	 * @return the articleUrl
	 */
	public String getArticleUrl() {
		return articleUrl;
	}
	/**
	 * @param articleUrl the articleUrl to set
	 */
	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}
}
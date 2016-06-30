package com.yuanluesoft.cms.infopublic.forms.admin;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class PublicInfo extends WorkflowForm {
	private int type; //类型,0/信息,1/年度报告、制度等
	private String infoIndex; //索引号
	private int infoYear; //年度
	private int infoSequence; //顺序号
	private String infoFrom; //发布机构
	private long infoFromUnitId; //发布机构ID
	private String mark; //文号
	private Date generateDate; //生成日期
	private String subject; //标题
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private Timestamp issueTime; //发布时间
	private String workflowInstanceId;
	private Set workItems;
	private Set opinions;
	private Set visitors;
	private String summarize; //内容概述
	private String category; //主题分类
	private String keywords; //主题词
	private String body; //正文
	private Set subjections; //信息所在目录
	private char issue = '0';
	private char issueSite = '0';
	private String issueSiteIds; //同步的网站ID列表
	private String sourceRecordId; //源记录ID
	private String sourceRecordUrl; //源记录URL
	private String directoryName; //所在目录名称
	private char status = PublicInfoService.INFO_STATUS_TODO; //状态,0/撤销发布,1/待处理,2/退回、取回修改,3/已发布,4/办结未发布,5/已删除
	
	//扩展属性
	private String issueSiteNames; //同步的网站名称列表
	
	private long directoryId; //所属目录ID
	private String directoryFullName; //所属目录名称
	private String otherDirectoryIds; //所属的其他目录ID
	private String otherDirectoryFullNames; //所属的其他目录名称
	
	private long siteId; //隶属站点ID,预览时、图片添加水印时使用
	private RecordVisitorList readers = new RecordVisitorList(); //访问者列表
	
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
	 * @return the subjections
	 */
	public Set getSubjections() {
		return subjections;
	}
	/**
	 * @param subjections the subjections to set
	 */
	public void setSubjections(Set subjections) {
		this.subjections = subjections;
	}
	/**
	 * @return the opinions
	 */
	public Set getOpinions() {
		return opinions;
	}
	/**
	 * @param opinions the opinions to set
	 */
	public void setOpinions(Set opinions) {
		this.opinions = opinions;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
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
	/**
	 * @return the workItems
	 */
	public Set getWorkItems() {
		return workItems;
	}
	/**
	 * @param workItems the workItems to set
	 */
	public void setWorkItems(Set workItems) {
		this.workItems = workItems;
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
	 * @return the issueSite
	 */
	public char getIssueSite() {
		return issueSite;
	}
	/**
	 * @param issueSite the issueSite to set
	 */
	public void setIssueSite(char issueSite) {
		this.issueSite = issueSite;
	}
	/**
	 * @return the issueSiteIds
	 */
	public String getIssueSiteIds() {
		return issueSiteIds;
	}
	/**
	 * @param issueSiteIds the issueSiteIds to set
	 */
	public void setIssueSiteIds(String issueSiteIds) {
		this.issueSiteIds = issueSiteIds;
	}
	/**
	 * @return the issueSiteNames
	 */
	public String getIssueSiteNames() {
		return issueSiteNames;
	}
	/**
	 * @param issueSiteNames the issueSiteNames to set
	 */
	public void setIssueSiteNames(String issueSiteNames) {
		this.issueSiteNames = issueSiteNames;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
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
	 * @return the otherDirectoryIds
	 */
	public String getOtherDirectoryIds() {
		return otherDirectoryIds;
	}
	/**
	 * @param otherDirectoryIds the otherDirectoryIds to set
	 */
	public void setOtherDirectoryIds(String otherDirectoryIds) {
		this.otherDirectoryIds = otherDirectoryIds;
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
	/**
	 * @return the sourceRecordUrl
	 */
	public String getSourceRecordUrl() {
		return sourceRecordUrl;
	}
	/**
	 * @param sourceRecordUrl the sourceRecordUrl to set
	 */
	public void setSourceRecordUrl(String sourceRecordUrl) {
		this.sourceRecordUrl = sourceRecordUrl;
	}
	/**
	 * @return the directoryFullName
	 */
	public String getDirectoryFullName() {
		return directoryFullName;
	}
	/**
	 * @param directoryFullName the directoryFullName to set
	 */
	public void setDirectoryFullName(String directoryFullName) {
		this.directoryFullName = directoryFullName;
	}
	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(char status) {
		this.status = status;
	}
	/**
	 * @return the otherDirectoryFullNames
	 */
	public String getOtherDirectoryFullNames() {
		return otherDirectoryFullNames;
	}
	/**
	 * @param otherDirectoryFullNames the otherDirectoryFullNames to set
	 */
	public void setOtherDirectoryFullNames(String otherDirectoryFullNames) {
		this.otherDirectoryFullNames = otherDirectoryFullNames;
	}
	/**
	 * @return the infoFromUnitId
	 */
	public long getInfoFromUnitId() {
		return infoFromUnitId;
	}
	/**
	 * @param infoFromUnitId the infoFromUnitId to set
	 */
	public void setInfoFromUnitId(long infoFromUnitId) {
		this.infoFromUnitId = infoFromUnitId;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the readers
	 */
	public RecordVisitorList getReaders() {
		return readers;
	}
	/**
	 * @param readers the readers to set
	 */
	public void setReaders(RecordVisitorList readers) {
		this.readers = readers;
	}
	/**
	 * @return the infoSequence
	 */
	public int getInfoSequence() {
		return infoSequence;
	}
	/**
	 * @param infoSequence the infoSequence to set
	 */
	public void setInfoSequence(int infoSequence) {
		this.infoSequence = infoSequence;
	}
	/**
	 * @return the infoYear
	 */
	public int getInfoYear() {
		return infoYear;
	}
	/**
	 * @param infoYear the infoYear to set
	 */
	public void setInfoYear(int infoYear) {
		this.infoYear = infoYear;
	}
}
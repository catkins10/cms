package com.yuanluesoft.cms.infopublic.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.util.LazyBodyUtils;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 
 * @author yuanluesoft
 *
 */
public class PublicInfo extends WorkflowData {
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
	private String summarize; //内容概述
	private String category; //主题分类
	private String keywords; //主题词
	private long orgId; //创建者所在部门ID
	private String orgName; //创建者所在部门名称
	private long unitId; //创建者所在单位ID
	private String unitName; //创建者所在单位名称
	private long issuePersonId; //发布人ID
	private char issueSite = '1';
	private String issueSiteIds; //同步的网站ID列表
	private String sourceRecordId; //源记录ID
	private String sourceRecordUrl; //源记录URL
	private String directoryName; //所在目录名称
	private char status = PublicInfoService.INFO_STATUS_TODO; //状态,0/撤销发布,1/待处理,2/退回、取回修改,3/已发布,4/办结未发布,5/已删除
	
	private Set subjections; //信息所在目录
	private Set lazyBody; //正文
	private Set accessStats; //访问统计
	
	/**
	 * 获取正文
	 */
	public String getBody() {
		return LazyBodyUtils.getBody(this);
	}
	
	/**
	 * 设置正文
	 * @param body
	 */
	public void setBody(String body) {
		LazyBodyUtils.setBody(this, body);
	}
	
	/**
	 * 获取状态描述
	 * @return
	 */
	public String getStatusDescription() {
		if(status==SiteResourceService.RESOURCE_STATUS_UNISSUE) {
			return "撤销发布";
		}
		else if(status==SiteResourceService.RESOURCE_STATUS_ISSUE) {
			return "已发布";
		}
		else if(status==SiteResourceService.RESOURCE_STATUS_NOPASS) {
			return "办结/未采用";
		}
		else if(status>=SiteResourceService.RESOURCE_STATUS_DELETED) {
			return "已删除";
		}
		else if(getWorkItems()==null || getWorkItems().isEmpty()) {
			return "办结";
		}
		return getWorkflowStatus();
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
	 * @return the lazyBody
	 */
	public Set getLazyBody() {
		return lazyBody;
	}
	/**
	 * @param lazyBody the lazyBody to set
	 */
	public void setLazyBody(Set lazyBody) {
		this.lazyBody = lazyBody;
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
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the accessStats
	 */
	public Set getAccessStats() {
		return accessStats;
	}

	/**
	 * @param accessStats the accessStats to set
	 */
	public void setAccessStats(Set accessStats) {
		this.accessStats = accessStats;
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

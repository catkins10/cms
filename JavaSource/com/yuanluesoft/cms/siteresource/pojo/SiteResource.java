/*
 * Created on 2007-2-3
 *
 */
package com.yuanluesoft.cms.siteresource.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.stat.pojo.AccessStat;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.LazyBodyUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 *
 * @author linchuan
 *
 */
public class SiteResource extends WorkflowData {
	private int type; //类型,0/文章,1/连载,2/链接
	private String subject; //标题
	private String subhead; //副标题
	private String source; //来源
	private String author; //作者
	private String translator; //译者
	private String review; //校对
	private String otherColumnIds; //所属的其他栏目ID
	private String otherColumnNames; //所属的其他栏目名称
	private String keyword; //关键字
	private String link; //链接,类型为链接时使用
	private Timestamp created; //创建时间
	private Timestamp issueTime; //发布时间
	private Timestamp issueEndTime; //发布截止时间
	private int imageCount; //图片数量
	private int uploadImageCount; //实际上传图片数量
	private String firstImageName; //第一个图片文件名称
	private int videoCount; //视频数量
	private int uploadVideoCount; //实际上传视频数量
	private String firstVideoName; //第一个视频文件名称
	private int attachmentCount; //附件数量
	private int uploadAttachmentCount; //实际上传附件数量
	private String subjectColor; //标题颜色
	private float priority; //重要等级
	private Timestamp lastModified; //最后修改时间
	private long editorId; //创建者ID
	private String editor; //创建者
	private long orgId; //创建者所在部门ID
	private String orgName; //创建者所在部门名称
	private long unitId; //创建者所在单位ID
	private String unitName; //创建者所在单位名称
	private long issuePersonId; //发布人ID
	private String mark; //文号
	private char anonymousLevel = '3'; //匿名用户访问级别,1/不能访问,2/仅标题,3/完全访问
	private String sourceRecordId; //源记录ID
	private String sourceRecordClassName; //源记录类名称
	private String sourceRecordUrl; //源记录URL
	private String columnName; //所在栏目名称
	private char status = SiteResourceService.RESOURCE_STATUS_TODO; //状态,0/撤销发布,1/待处理,2/退回、取回修改,3/已发布,4/办结未发布,5/已删除
	
	private Set lazyBody;
	private Set subjections;
	private Set accessStats; //访问统计
	private Set relationLinks; //相关链接
	private Set relatedLinks; //被引用的记录
	private Set resourcePhotos; //图集
	private Set resourceVideos; //视频集
	private Set resourceTops; //置顶

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.Record#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		SiteResource resource = (SiteResource)super.clone();
		resource.setId(UUIDLongGenerator.generateId()); //ID
		try {
			if(resource.getLazyBody()!=null && !resource.getLazyBody().isEmpty()) {
				Set lazyBody = new HashSet();
				SiteResourceBody resourceBody = (SiteResourceBody)((SiteResourceBody)resource.getLazyBody().iterator().next()).clone();
				resourceBody.setId(resource.getId());
				lazyBody.add(resourceBody);
				resource.setLazyBody(lazyBody);
			}
			if(resource.getSubjections()!=null && !resource.getSubjections().isEmpty()) {
				Set subjections = new HashSet();
				for(Iterator iterator = resource.getSubjections().iterator(); iterator.hasNext();) {
					SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
					subjection = (SiteResourceSubjection)subjection.clone();
					subjection.setResourceId(resource.getId());
					subjections.add(subjection);
				}
				resource.setSubjections(subjections);
			}
		}
		catch(Exception e) {
			
		}
		return resource;
	}
	
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
	 * 获取资源类型
	 * @return
	 */
	public String getResourceType() {
		return SiteResourceService.RESOURCE_TYPE_NAMES[type];
	}
	
	/**
	 * 获取trim后的主题
	 * @return
	 */
	public String getSubjectTrim() {
		return subject==null ? "无标题" : subject.trim().replaceAll("\r", "").replaceAll("\n", "");
	}
	
	/**
	 * 获取概述
	 * @return
	 */
	public String getSummarize() {
		String summarize = StringUtils.filterHtmlElement(getBody(), false);
		if(summarize!=null) {
			summarize = summarize.replaceAll("[ /,\\*\\?\\\\:\\<\\>|&\r\n\"\']", "");
			if(summarize.length()>100) {
				summarize = summarize.substring(0, 100) + "...";
			}
		}
		return summarize;
	}
	
	/**
	 * 是否有图
	 * @return
	 */
	public String getHasImages() {
		return imageCount==0 ? "" : (imageCount==1 ? "图" : "多图");  
	}
	
	/**
	 * 是否有视频
	 * @return
	 */
	public String getHasVideos() {
		return videoCount==0 ? "" : "视频";  
	}
	
	/**
	 * 是否有附件
	 * @return
	 */
	public String getHasAttachments() {
		return attachmentCount==0 ? "" : "附件";  
	}
	
	/**
	 * 是否置顶
	 * @return
	 */
	public String getTop() {
		return resourceTops==null || resourceTops.isEmpty() ? "" : "置顶";  
	}
	
	/**
	 * 获取访问次数
	 * @return
	 */
	public long getAccessCount() {
		try {
			return ((AccessStat)accessStats.iterator().next()).getTimes();
		}
		catch(Exception e) {
			return 0;
		}
	}

	/**
	 * 获取记录URL
	 * @return
	 */
	public String getRecordURL() {
		if(type==SiteResourceService.RESOURCE_TYPE_ARTICLE) { //文章
			return Environment.getContextPath() + "/cms/siteresource/article.shtml";
		}
		else if(type==SiteResourceService.RESOURCE_TYPE_LINK) { //链接
			return "{FINAL}" + link;
		}
		return null;
	}
	
	/**
	 * 获取源记录类型
	 * @return
	 */
	public String getSourceRecordType() {
		if(sourceRecordUrl==null || sourceRecordUrl.isEmpty()) {
			return "无";
		}
		else if(sourceRecordClassName!=null && !sourceRecordClassName.isEmpty()) {
			try {
				BusinessObject businessObject = ((BusinessDefineService)Environment.getService("businessDefineService")).getBusinessObject(sourceRecordClassName);
				if(businessObject!=null) {
					return businessObject.getTitle();
				}
			}
			catch (Exception e) {
				
			}
		}
		return "网页抓取";
	}
	
	/**
	 * 获取有效的相关链接
	 * @return
	 */
	public List getValidRelationLinks() {
		return ListUtils.getSubListByProperty(relationLinks, "halt", new Integer(0)); //相关链接
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	/**
	 * @return Returns the author.
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author The author to set.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return Returns the created.
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return Returns the issueTime.
	 */
	public Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime The issueTime to set.
	 */
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
	}
	/**
	 * @return Returns the keyword.
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword The keyword to set.
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return Returns the source.
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source The source to set.
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return Returns the subhead.
	 */
	public String getSubhead() {
		return subhead;
	}
	/**
	 * @param subhead The subhead to set.
	 */
	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}
	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return Returns the subjections.
	 */
	public java.util.Set getSubjections() {
		return subjections;
	}
	/**
	 * @param subjections The subjections to set.
	 */
	public void setSubjections(java.util.Set subjections) {
		this.subjections = subjections;
	}
	/**
	 * @return Returns the editorId.
	 */
	public long getEditorId() {
		return editorId;
	}
	/**
	 * @param editorId The editorId to set.
	 */
	public void setEditorId(long editorId) {
		this.editorId = editorId;
	}
	/**
	 * @return Returns the imageCount.
	 */
	public int getImageCount() {
		return imageCount;
	}
	/**
	 * @param imageCount The imageCount to set.
	 */
	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}
	/**
	 * @return Returns the priority.
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return Returns the subjectColor.
	 */
	public String getSubjectColor() {
		return subjectColor;
	}
	/**
	 * @param subjectColor The subjectColor to set.
	 */
	public void setSubjectColor(String subjectColor) {
		this.subjectColor = subjectColor;
	}
	/**
	 * @return the firstImageName
	 */
	public String getFirstImageName() {
		return firstImageName;
	}
	/**
	 * @param firstImageName the firstImageName to set
	 */
	public void setFirstImageName(String firstImageName) {
		this.firstImageName = firstImageName;
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
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
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
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
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
	 * @return the firstVideoName
	 */
	public String getFirstVideoName() {
		return firstVideoName;
	}

	/**
	 * @param firstVideoName the firstVideoName to set
	 */
	public void setFirstVideoName(String firstVideoName) {
		this.firstVideoName = firstVideoName;
	}

	/**
	 * @return the videoCount
	 */
	public int getVideoCount() {
		return videoCount;
	}

	/**
	 * @param videoCount the videoCount to set
	 */
	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}

	/**
	 * @return the attachmentCount
	 */
	public int getAttachmentCount() {
		return attachmentCount;
	}

	/**
	 * @param attachmentCount the attachmentCount to set
	 */
	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
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
	 * @return the uploadAttachmentCount
	 */
	public int getUploadAttachmentCount() {
		return uploadAttachmentCount;
	}

	/**
	 * @param uploadAttachmentCount the uploadAttachmentCount to set
	 */
	public void setUploadAttachmentCount(int uploadAttachmentCount) {
		this.uploadAttachmentCount = uploadAttachmentCount;
	}

	/**
	 * @return the uploadImageCount
	 */
	public int getUploadImageCount() {
		return uploadImageCount;
	}

	/**
	 * @param uploadImageCount the uploadImageCount to set
	 */
	public void setUploadImageCount(int uploadImageCount) {
		this.uploadImageCount = uploadImageCount;
	}

	/**
	 * @return the uploadVideoCount
	 */
	public int getUploadVideoCount() {
		return uploadVideoCount;
	}

	/**
	 * @param uploadVideoCount the uploadVideoCount to set
	 */
	public void setUploadVideoCount(int uploadVideoCount) {
		this.uploadVideoCount = uploadVideoCount;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(char status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}

	/**
	 * @return the sourceRecordClassName
	 */
	public String getSourceRecordClassName() {
		return sourceRecordClassName;
	}

	/**
	 * @param sourceRecordClassName the sourceRecordClassName to set
	 */
	public void setSourceRecordClassName(String sourceRecordClassName) {
		this.sourceRecordClassName = sourceRecordClassName;
	}

	/**
	 * @return the otherColumnIds
	 */
	public String getOtherColumnIds() {
		return otherColumnIds;
	}

	/**
	 * @param otherColumnIds the otherColumnIds to set
	 */
	public void setOtherColumnIds(String otherColumnIds) {
		this.otherColumnIds = otherColumnIds;
	}

	/**
	 * @return the otherColumnNames
	 */
	public String getOtherColumnNames() {
		return otherColumnNames;
	}

	/**
	 * @param otherColumnNames the otherColumnNames to set
	 */
	public void setOtherColumnNames(String otherColumnNames) {
		this.otherColumnNames = otherColumnNames;
	}

	/**
	 * @return the review
	 */
	public String getReview() {
		return review;
	}

	/**
	 * @param review the review to set
	 */
	public void setReview(String review) {
		this.review = review;
	}

	/**
	 * @return the translator
	 */
	public String getTranslator() {
		return translator;
	}

	/**
	 * @param translator the translator to set
	 */
	public void setTranslator(String translator) {
		this.translator = translator;
	}

	/**
	 * @return the relatedLinks
	 */
	public Set getRelatedLinks() {
		return relatedLinks;
	}

	/**
	 * @param relatedLinks the relatedLinks to set
	 */
	public void setRelatedLinks(Set relatedLinks) {
		this.relatedLinks = relatedLinks;
	}

	/**
	 * @return the relationLinks
	 */
	public Set getRelationLinks() {
		return relationLinks;
	}

	/**
	 * @param relationLinks the relationLinks to set
	 */
	public void setRelationLinks(Set relationLinks) {
		this.relationLinks = relationLinks;
	}

	/**
	 * @return the issueEndTime
	 */
	public Timestamp getIssueEndTime() {
		return issueEndTime;
	}

	/**
	 * @param issueEndTime the issueEndTime to set
	 */
	public void setIssueEndTime(Timestamp issueEndTime) {
		this.issueEndTime = issueEndTime;
	}

	/**
	 * @return the resourcePhotos
	 */
	public Set getResourcePhotos() {
		return resourcePhotos;
	}

	/**
	 * @param resourcePhotos the resourcePhotos to set
	 */
	public void setResourcePhotos(Set resourcePhotos) {
		this.resourcePhotos = resourcePhotos;
	}

	/**
	 * @return the resourceVideos
	 */
	public Set getResourceVideos() {
		return resourceVideos;
	}

	/**
	 * @param resourceVideos the resourceVideos to set
	 */
	public void setResourceVideos(Set resourceVideos) {
		this.resourceVideos = resourceVideos;
	}

	/**
	 * @return the resourceTops
	 */
	public Set getResourceTops() {
		return resourceTops;
	}

	/**
	 * @param resourceTops the resourceTops to set
	 */
	public void setResourceTops(Set resourceTops) {
		this.resourceTops = resourceTops;
	}
}
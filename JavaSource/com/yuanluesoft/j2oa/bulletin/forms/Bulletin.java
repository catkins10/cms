package com.yuanluesoft.j2oa.bulletin.forms;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author LinChuan
*
 */
public class Bulletin extends WorkflowForm {
	private String subject; //主题
	private String category; //类别
	private String signPerson; //批准人
	private Date beginDate; //有效时间_开始时间
	private Date endDate; //有效时间_结束时间
	private String content; //内容
	private char issued = '0'; //是否发布
	private Timestamp issueTime; //发布时间
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	
	//发布范围
	private RecordVisitorList issueRange = new RecordVisitorList();
	
	private List accessVisitors; //已经看过的人员
	
	/**
	 * @return Returns the beginDate.
	 */
	public java.sql.Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate The beginDate to set.
	 */
	public void setBeginDate(java.sql.Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return Returns the category.
	 */
	public java.lang.String getCategory() {
		return category;
	}
	/**
	 * @param category The category to set.
	 */
	public void setCategory(java.lang.String category) {
		this.category = category;
	}
	/**
	 * @return Returns the content.
	 */
	public java.lang.String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	/**
	 * @return Returns the endDate.
	 */
	public java.sql.Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(java.sql.Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the signPerson.
	 */
	public java.lang.String getSignPerson() {
		return signPerson;
	}
	/**
	 * @param signPerson The signPerson to set.
	 */
	public void setSignPerson(java.lang.String signPerson) {
		this.signPerson = signPerson;
	}
	/**
	 * @return Returns the subject.
	 */
	public java.lang.String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(java.lang.String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the issued.
	 */
	public char getIssued() {
		return issued;
	}
	/**
	 * @param issued The issued to set.
	 */
	public void setIssued(char issued) {
		this.issued = issued;
	}
	/**
	 * @return Returns the issueTime.
	 */
	public java.sql.Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime The issueTime to set.
	 */
	public void setIssueTime(java.sql.Timestamp issueTime) {
		this.issueTime = issueTime;
	}
    /**
     * @return Returns the issueRange.
     */
    public RecordVisitorList getIssueRange() {
        return issueRange;
    }
    /**
     * @param issueRange The issueRange to set.
     */
    public void setIssueRange(RecordVisitorList issueRange) {
        this.issueRange = issueRange;
    }
	/**
	 * @return the accessVisitors
	 */
	public List getAccessVisitors() {
		return accessVisitors;
	}
	/**
	 * @param accessVisitors the accessVisitors to set
	 */
	public void setAccessVisitors(List accessVisitors) {
		this.accessVisitors = accessVisitors;
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
}
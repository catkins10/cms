package com.yuanluesoft.j2oa.meeting.forms;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author LinChuan
*
 */
public class Meeting extends WorkflowForm {
	private String subject; //名称
	private String content; //议题
	private String address; //会议地点
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //结束时间
	private char issued = '0'; //是否发布
	private Timestamp issueTime; //发布时间
	private long handlerId; //经办人ID
	private String handlerName; //经办人
	private Timestamp created; //创建时间
	private String reamrk; //备注
	
	//参加者
	private RecordVisitorList attendees = new RecordVisitorList();
	public boolean forceIssue; //是否在冲突时还要发布
	public String conflict; //冲突
	
	private List accessVisitors; //已经看过的人员
	
	/**
	 * @return Returns the handlerName.
	 */
	public String getHandlerName() {
		return handlerName;
	}
	/**
	 * @param handlerName The handlerName to set.
	 */
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	/**
	 * @return Returns the beginTime.
	 */
	public java.sql.Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime The beginTime to set.
	 */
	public void setBeginTime(java.sql.Timestamp beginTime) {
		this.beginTime = beginTime;
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
	 * @return Returns the endTime.
	 */
	public java.sql.Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime The endTime to set.
	 */
	public void setEndTime(java.sql.Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return Returns the handlerId.
	 */
	public long getHandlerId() {
		return handlerId;
	}
	/**
	 * @param handlerId The handlerId to set.
	 */
	public void setHandlerId(long handlerId) {
		this.handlerId = handlerId;
	}
	/**
	 * @return Returns the reamrk.
	 */
	public java.lang.String getReamrk() {
		return reamrk;
	}
	/**
	 * @param reamrk The reamrk to set.
	 */
	public void setReamrk(java.lang.String reamrk) {
		this.reamrk = reamrk;
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
	 * @return Returns the address.
	 */
	public java.lang.String getAddress() {
		return address;
	}
	/**
	 * @param address The address to set.
	 */
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
    /**
     * @return Returns the attendees.
     */
    public RecordVisitorList getAttendees() {
        return attendees;
    }
    /**
     * @param attendees The attendees to set.
     */
    public void setAttendees(RecordVisitorList attendees) {
        this.attendees = attendees;
    }
	/**
	 * @return Returns the forceIssue.
	 */
	public boolean isForceIssue() {
		return forceIssue;
	}
	/**
	 * @param forceIssue The forceIssue to set.
	 */
	public void setForceIssue(boolean forceIssue) {
		this.forceIssue = forceIssue;
	}
	/**
	 * @return Returns the conflict.
	 */
	public String getConflict() {
		return conflict;
	}
	/**
	 * @param conflict The conflict to set.
	 */
	public void setConflict(String conflict) {
		this.conflict = conflict;
	}
	/**
	 * @return the issued
	 */
	public char getIssued() {
		return issued;
	}
	/**
	 * @param issued the issued to set
	 */
	public void setIssued(char issued) {
		this.issued = issued;
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
}
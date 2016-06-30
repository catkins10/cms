package com.yuanluesoft.j2oa.memorabilia.forms;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class MemorabiliaForm extends ActionForm {
	private Date time; //发生时间
	private long creatorId; //录入人ID
	private String creatorName; //录入人姓名
	private Timestamp created; //录入时间
	private String subject; //主题
	private String content; //详细内容

    /**
     * @return Returns the content.
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
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
     * @return Returns the creatorId.
     */
    public long getCreatorId() {
        return creatorId;
    }
    /**
     * @param creatorId The creatorId to set.
     */
    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
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
     * @return Returns the time.
     */
    public Date getTime() {
        return time;
    }
    /**
     * @param time The time to set.
     */
    public void setTime(Date time) {
        this.time = time;
    }
    /**
     * @return Returns the creatorName.
     */
    public String getCreatorName() {
        return creatorName;
    }
    /**
     * @param creatorName The creatorName to set.
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
/*
 * Created on 2006-5-27
 *
 */
package com.yuanluesoft.j2oa.memorabilia.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 大事记(memorabilia_memorabilia)
 * @author linchuan
 *
 */
public class Memorabilia extends Record {
	private Date time; //发生时间
	private long creatorId; //录入人ID
	private String creatorName; //录入人姓名
	private Timestamp created; //录入时间
	private String subject; //主题
	private String content; //详细内容
	
    /**
     * @return Returns the time.
     */
    public java.sql.Date getTime() {
        return time;
    }
    /**
     * @param time The time to set.
     */
    public void setTime(java.sql.Date time) {
        this.time = time;
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
     * @return Returns the created.
     */
    public java.sql.Timestamp getCreated() {
        return created;
    }
    /**
     * @param created The created to set.
     */
    public void setCreated(java.sql.Timestamp created) {
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
}

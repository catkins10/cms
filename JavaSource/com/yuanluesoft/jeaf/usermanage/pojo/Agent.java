/*
 * Created on 2006-7-5
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class Agent extends Record {
	private long personId;
	private long agentId;
	private java.sql.Timestamp beginTime;
	private java.sql.Timestamp endTime;
	private java.lang.String source;
    
    /**
     * @return Returns the personId.
     */
    public long getPersonId() {
        return personId;
    }
    /**
     * @param personId The personId to set.
     */
    public void setPersonId(long personId) {
        this.personId = personId;
    }
    /**
     * @return Returns the agentId.
     */
    public long getAgentId() {
        return agentId;
    }
    /**
     * @param agentId The agentId to set.
     */
    public void setAgentId(long agentId) {
        this.agentId = agentId;
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
     * @return Returns the source.
     */
    public java.lang.String getSource() {
        return source;
    }
    /**
     * @param source The source to set.
     */
    public void setSource(java.lang.String source) {
        this.source = source;
    }
}

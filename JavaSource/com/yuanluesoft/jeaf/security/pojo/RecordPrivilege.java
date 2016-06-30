/*
 * Created on 2006-6-10
 *
 */
package com.yuanluesoft.jeaf.security.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 *
 * @author linchuan
 *
 */
public class RecordPrivilege extends Record {
	private char accessLevel = '0';
	private long recordId;
	private long visitorId;
	
    /**
     * @return Returns the accessLevel.
     */
    public char getAccessLevel() {
        return accessLevel;
    }
    /**
     * @param accessLevel The accessLevel to set.
     */
    public void setAccessLevel(char accessLevel) {
        this.accessLevel = accessLevel;
    }
    /**
     * @return Returns the recordId.
     */
    public long getRecordId() {
        return recordId;
    }
    /**
     * @param recordId The recordId to set.
     */
    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
    /**
     * @return Returns the visitorId.
     */
    public long getVisitorId() {
        return visitorId;
    }
    /**
     * @param visitorId The visitorId to set.
     */
    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }
}
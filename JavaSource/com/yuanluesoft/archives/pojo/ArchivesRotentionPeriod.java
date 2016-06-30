/*
 * Created on 2006-8-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 保管期限(archives_rotention_period)
 * @author linchuan
 *
 */
public class ArchivesRotentionPeriod extends Record {
	private String rotentionPeriodCode; //期限编号
	private String rotentionPeriod; //保管期限

    /**
     * @return Returns the rotentionPeriodCode.
     */
    public java.lang.String getRotentionPeriodCode() {
        return rotentionPeriodCode;
    }
    /**
     * @param rotentionPeriodCode The rotentionPeriodCode to set.
     */
    public void setRotentionPeriodCode(java.lang.String rotentionPeriodCode) {
        this.rotentionPeriodCode = rotentionPeriodCode;
    }
    /**
     * @return Returns the rotentionPeriod.
     */
    public java.lang.String getRotentionPeriod() {
        return rotentionPeriod;
    }
    /**
     * @param rotentionPeriod The rotentionPeriod to set.
     */
    public void setRotentionPeriod(java.lang.String rotentionPeriod) {
        this.rotentionPeriod = rotentionPeriod;
    }
}

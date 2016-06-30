/*
 * Created on 2006-8-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 全宗设置(archives_fonds)
 * @author linchuan
 *
 */
public class ArchivesFonds extends Record {
	private String fondsName; //全宗名称,单位名称
	private String fondsCode; //全宗号
	
    /**
     * @return Returns the fondsCode.
     */
    public java.lang.String getFondsCode() {
        return fondsCode;
    }
    /**
     * @param fondsCode The fondsCode to set.
     */
    public void setFondsCode(java.lang.String fondsCode) {
        this.fondsCode = fondsCode;
    }
    /**
     * @return Returns the fondsName.
     */
    public java.lang.String getFondsName() {
        return fondsName;
    }
    /**
     * @param fondsName The fondsName to set.
     */
    public void setFondsName(java.lang.String fondsName) {
        this.fondsName = fondsName;
    }
}

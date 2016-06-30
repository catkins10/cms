/*
 * Created on 2006-8-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 文件密级配置(archives_secure_level)
 * @author linchuan
 *
 */
public class ArchivesSecureLevel extends Record {
	private String secureLevelCode; //密级编号
	private String secureLevel; //密级
	
    /**
     * @return Returns the secureLevelCode.
     */
    public java.lang.String getSecureLevelCode() {
        return secureLevelCode;
    }
    /**
     * @param secureLevelCode The secureLevelCode to set.
     */
    public void setSecureLevelCode(java.lang.String secureLevelCode) {
        this.secureLevelCode = secureLevelCode;
    }
    /**
     * @return Returns the secureLevel.
     */
    public java.lang.String getSecureLevel() {
        return secureLevel;
    }
    /**
     * @param secureLevel The secureLevel to set.
     */
    public void setSecureLevel(java.lang.String secureLevel) {
        this.secureLevel = secureLevel;
    }
}

/*
 * Created on 2006-8-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 机构或问题配置(archive_unit)
 * @author linchuan
 *
 */
public class ArchivesUnit extends Record {
	private String unitCode; //编号
	private String unit; //机构或问题
	
    /**
     * @return Returns the unitCode.
     */
    public java.lang.String getUnitCode() {
        return unitCode;
    }
    /**
     * @param unitCode The unitCode to set.
     */
    public void setUnitCode(java.lang.String unitCode) {
        this.unitCode = unitCode;
    }
    /**
     * @return Returns the unit.
     */
    public java.lang.String getUnit() {
        return unit;
    }
    /**
     * @param unit The unit to set.
     */
    public void setUnit(java.lang.String unit) {
        this.unit = unit;
    }
}

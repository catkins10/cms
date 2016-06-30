package com.yuanluesoft.archives.forms;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 * 
 */
public class ArchivesUnit extends ActionForm {
	private String unitCode; //编号
	private String unit; //机构或问题
	
	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit The unit to set.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return Returns the unitCode.
	 */
	public String getUnitCode() {
		return unitCode;
	}
	/**
	 * @param unitCode The unitCode to set.
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
}
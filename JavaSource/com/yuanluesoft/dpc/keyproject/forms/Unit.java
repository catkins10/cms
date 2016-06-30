package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectUnit;

/**
 * 
 * @author linchuan
 *
 */
public class Unit extends Project {
	private KeyProjectUnit projectUnit = new KeyProjectUnit();

	/**
	 * @return the projectUnit
	 */
	public KeyProjectUnit getProjectUnit() {
		return projectUnit;
	}

	/**
	 * @param projectUnit the projectUnit to set
	 */
	public void setProjectUnit(KeyProjectUnit projectUnit) {
		this.projectUnit = projectUnit;
	}
}
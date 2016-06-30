package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectAnnualObjective;

/**
 * 
 * @author linchuan
 *
 */
public class AnnualObjective extends Project {
	private KeyProjectAnnualObjective annualObjective = new KeyProjectAnnualObjective();

	/**
	 * @return the annualObjective
	 */
	public KeyProjectAnnualObjective getAnnualObjective() {
		return annualObjective;
	}

	/**
	 * @param annualObjective the annualObjective to set
	 */
	public void setAnnualObjective(KeyProjectAnnualObjective annualObjective) {
		this.annualObjective = annualObjective;
	}
}
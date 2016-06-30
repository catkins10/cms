package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectIndustry;

/**
 * 行业配置
 * @author linchuan
 *
 */
public class Industry extends Parameter {
	private KeyProjectIndustry industry = new KeyProjectIndustry();

	/**
	 * @return the industry
	 */
	public KeyProjectIndustry getIndustry() {
		return industry;
	}

	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(KeyProjectIndustry industry) {
		this.industry = industry;
	}
}
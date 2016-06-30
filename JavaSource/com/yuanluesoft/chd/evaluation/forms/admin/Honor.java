package com.yuanluesoft.chd.evaluation.forms.admin;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantHonor;

/**
 * 
 * @author linchuan
 *
 */
public class Honor extends Plant {
	private ChdEvaluationPlantHonor honor = new ChdEvaluationPlantHonor(); //企业荣誉

	/**
	 * @return the honor
	 */
	public ChdEvaluationPlantHonor getHonor() {
		return honor;
	}

	/**
	 * @param honor the honor to set
	 */
	public void setHonor(ChdEvaluationPlantHonor honor) {
		this.honor = honor;
	}
}
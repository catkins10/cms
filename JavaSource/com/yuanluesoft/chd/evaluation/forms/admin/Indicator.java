package com.yuanluesoft.chd.evaluation.forms.admin;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationIndicator;

/**
 * 
 * @author linchuan
 *
 */
public class Indicator extends PlantType {
	private ChdEvaluationIndicator indicator = new ChdEvaluationIndicator(); //主要指标

	/**
	 * @return the indicator
	 */
	public ChdEvaluationIndicator getIndicator() {
		return indicator;
	}

	/**
	 * @param indicator the indicator to set
	 */
	public void setIndicator(ChdEvaluationIndicator indicator) {
		this.indicator = indicator;
	}
}
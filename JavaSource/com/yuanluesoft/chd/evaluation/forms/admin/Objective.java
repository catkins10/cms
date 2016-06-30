package com.yuanluesoft.chd.evaluation.forms.admin;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationObjective;

/**
 * 
 * @author linchuan
 *
 */
public class Objective extends Plant {
	private ChdEvaluationObjective objective = new ChdEvaluationObjective();

	/**
	 * @return the objective
	 */
	public ChdEvaluationObjective getObjective() {
		return objective;
	}

	/**
	 * @param objective the objective to set
	 */
	public void setObjective(ChdEvaluationObjective objective) {
		this.objective = objective;
	}
}
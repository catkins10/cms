package com.yuanluesoft.chd.evaluation.forms.admin;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPrerequisites;

/**
 * 
 * @author linchuan
 *
 */
public class Prerequisites extends PlantType {
	private ChdEvaluationPrerequisites evaluationPrerequisites = new ChdEvaluationPrerequisites(); //必备条件

	/**
	 * @return the evaluationPrerequisites
	 */
	public ChdEvaluationPrerequisites getEvaluationPrerequisites() {
		return evaluationPrerequisites;
	}

	/**
	 * @param evaluationPrerequisites the evaluationPrerequisites to set
	 */
	public void setEvaluationPrerequisites(
			ChdEvaluationPrerequisites evaluationPrerequisites) {
		this.evaluationPrerequisites = evaluationPrerequisites;
	}
}
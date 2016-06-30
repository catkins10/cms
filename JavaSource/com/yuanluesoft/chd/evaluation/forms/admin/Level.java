package com.yuanluesoft.chd.evaluation.forms.admin;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationLevel;

/**
 * 
 * @author linchuan
 *
 */
public class Level extends Company {
	private ChdEvaluationLevel level = new ChdEvaluationLevel(); //评价等级

	/**
	 * @return the level
	 */
	public ChdEvaluationLevel getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(ChdEvaluationLevel level) {
		this.level = level;
	}
}
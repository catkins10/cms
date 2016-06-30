package com.yuanluesoft.chd.evaluation.pojo;

import java.util.Set;

/**
 * 评价体系目录:公司(chd_evaluation_company)
 * @author linchuan
 *
 */
public class ChdEvaluationCompany extends ChdEvaluationDirectory {
	private Set levels; //评价等级列表

	/**
	 * @return the levels
	 */
	public Set getLevels() {
		return levels;
	}

	/**
	 * @param levels the levels to set
	 */
	public void setLevels(Set levels) {
		this.levels = levels;
	}
}
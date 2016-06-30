package com.yuanluesoft.chd.evaluation.pojo;


/**
 * 评价体系目录:发电企业评价项目(chd_evaluation_plant_rule)
 * @author linchuan
 *
 */
public class ChdEvaluationPlantRule extends ChdEvaluationDirectory {
	private char isIndicator = '0'; //是否指标评价

	/**
	 * @return the isIndicator
	 */
	public char getIsIndicator() {
		return isIndicator;
	}

	/**
	 * @param isIndicator the isIndicator to set
	 */
	public void setIsIndicator(char isIndicator) {
		this.isIndicator = isIndicator;
	}
}
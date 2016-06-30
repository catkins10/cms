package com.yuanluesoft.chd.evaluation.pojo;

import java.util.Set;

/**
 * 评价体系目录:发电企业类型(chd_evaluation_plant_type)
 * @author linchuan
 *
 */
public class ChdEvaluationPlantType extends ChdEvaluationDirectory {
	private Set indicators; //主要指标
	private Set prerequisites; //必备条件
	private Set generators; //机组综合数据表模板
	
	/**
	 * @return the generators
	 */
	public Set getGenerators() {
		return generators;
	}
	/**
	 * @param generators the generators to set
	 */
	public void setGenerators(Set generators) {
		this.generators = generators;
	}
	/**
	 * @return the indicators
	 */
	public Set getIndicators() {
		return indicators;
	}
	/**
	 * @param indicators the indicators to set
	 */
	public void setIndicators(Set indicators) {
		this.indicators = indicators;
	}
	/**
	 * @return the prerequisites
	 */
	public Set getPrerequisites() {
		return prerequisites;
	}
	/**
	 * @param prerequisites the prerequisites to set
	 */
	public void setPrerequisites(Set prerequisites) {
		this.prerequisites = prerequisites;
	}
}
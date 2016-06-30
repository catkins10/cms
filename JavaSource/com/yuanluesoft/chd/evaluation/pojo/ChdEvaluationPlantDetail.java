package com.yuanluesoft.chd.evaluation.pojo;

import java.util.Set;


/**
 * 评价体系目录:发电企业评价细则(chd_evaluation_plant_detail)
 * @author linchuan
 *
 */
public class ChdEvaluationPlantDetail extends ChdEvaluationDirectory {
	private Set points; //评价要点列表

	/**
	 * @return the points
	 */
	public Set getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Set points) {
		this.points = points;
	}
}
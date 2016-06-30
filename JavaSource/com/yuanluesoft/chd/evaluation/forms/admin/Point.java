package com.yuanluesoft.chd.evaluation.forms.admin;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPoint;

/**
 * 
 * @author linchuan
 *
 */
public class Point extends Detail {
	private ChdEvaluationPoint point = new ChdEvaluationPoint();

	/**
	 * @return the point
	 */
	public ChdEvaluationPoint getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(ChdEvaluationPoint point) {
		this.point = point;
	}
}
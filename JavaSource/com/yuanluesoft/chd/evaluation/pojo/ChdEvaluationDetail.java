package com.yuanluesoft.chd.evaluation.pojo;

import java.util.Set;


/**
 * 评价项目:评价细则(chd_evaluation_detail)
 * @author linchuan
 *
 */
public class ChdEvaluationDetail extends ChdEvaluationDirectory {
	private String objective; //目标要求
	private String method; //评价方法
	private String deduct; //扣分条款
	private Set points; //评价要点列表
	
	/**
	 * @return the deduct
	 */
	public String getDeduct() {
		return deduct;
	}
	/**
	 * @param deduct the deduct to set
	 */
	public void setDeduct(String deduct) {
		this.deduct = deduct;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the objective
	 */
	public String getObjective() {
		return objective;
	}
	/**
	 * @param objective the objective to set
	 */
	public void setObjective(String objective) {
		this.objective = objective;
	}
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
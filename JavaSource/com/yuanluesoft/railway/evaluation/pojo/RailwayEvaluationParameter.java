package com.yuanluesoft.railway.evaluation.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考核配置(railway_evaluation_parameter)
 * @author linchuan
 *
 */
public class RailwayEvaluationParameter extends Record {
	private double eventLevelADeduct; //铁路局问题A扣分
	private double eventLevelBDeduct; //铁路局问题B扣分
	private double eventLevelCDeduct; //铁路局问题C扣分
	private double eventLevelDDeduct; //铁路局问题D扣分
	private double eventDeductLimit; //铁路局问题扣分上限
	private double mutualEvaluationRaise; //互评靠前加分,在所有人的选择中都靠前时的加分额度
	private double mutualEvaluationDeduct; //互评靠后减分,在所有人的选择中都靠后时的扣分额度
	private double testLackDeduct; //考试题量未完成扣分,上限,1题未答时扣除全部
	private double testRaise; //考分靠前加分,同岗位中排名第一时的加分额度
	private double testDeduct; //考分靠后减分,同岗位中排名倒数第一时的扣分额度
	
	/**
	 * @return the eventLevelADeduct
	 */
	public double getEventLevelADeduct() {
		return eventLevelADeduct;
	}
	/**
	 * @param eventLevelADeduct the eventLevelADeduct to set
	 */
	public void setEventLevelADeduct(double eventLevelADeduct) {
		this.eventLevelADeduct = eventLevelADeduct;
	}
	/**
	 * @return the eventLevelBDeduct
	 */
	public double getEventLevelBDeduct() {
		return eventLevelBDeduct;
	}
	/**
	 * @param eventLevelBDeduct the eventLevelBDeduct to set
	 */
	public void setEventLevelBDeduct(double eventLevelBDeduct) {
		this.eventLevelBDeduct = eventLevelBDeduct;
	}
	/**
	 * @return the eventLevelCDeduct
	 */
	public double getEventLevelCDeduct() {
		return eventLevelCDeduct;
	}
	/**
	 * @param eventLevelCDeduct the eventLevelCDeduct to set
	 */
	public void setEventLevelCDeduct(double eventLevelCDeduct) {
		this.eventLevelCDeduct = eventLevelCDeduct;
	}
	/**
	 * @return the eventLevelDDeduct
	 */
	public double getEventLevelDDeduct() {
		return eventLevelDDeduct;
	}
	/**
	 * @param eventLevelDDeduct the eventLevelDDeduct to set
	 */
	public void setEventLevelDDeduct(double eventLevelDDeduct) {
		this.eventLevelDDeduct = eventLevelDDeduct;
	}
	/**
	 * @return the mutualEvaluationDeduct
	 */
	public double getMutualEvaluationDeduct() {
		return mutualEvaluationDeduct;
	}
	/**
	 * @param mutualEvaluationDeduct the mutualEvaluationDeduct to set
	 */
	public void setMutualEvaluationDeduct(double mutualEvaluationDeduct) {
		this.mutualEvaluationDeduct = mutualEvaluationDeduct;
	}
	/**
	 * @return the mutualEvaluationRaise
	 */
	public double getMutualEvaluationRaise() {
		return mutualEvaluationRaise;
	}
	/**
	 * @param mutualEvaluationRaise the mutualEvaluationRaise to set
	 */
	public void setMutualEvaluationRaise(double mutualEvaluationRaise) {
		this.mutualEvaluationRaise = mutualEvaluationRaise;
	}
	/**
	 * @return the testDeduct
	 */
	public double getTestDeduct() {
		return testDeduct;
	}
	/**
	 * @param testDeduct the testDeduct to set
	 */
	public void setTestDeduct(double testDeduct) {
		this.testDeduct = testDeduct;
	}
	/**
	 * @return the testLackDeduct
	 */
	public double getTestLackDeduct() {
		return testLackDeduct;
	}
	/**
	 * @param testLackDeduct the testLackDeduct to set
	 */
	public void setTestLackDeduct(double testLackDeduct) {
		this.testLackDeduct = testLackDeduct;
	}
	/**
	 * @return the testRaise
	 */
	public double getTestRaise() {
		return testRaise;
	}
	/**
	 * @param testRaise the testRaise to set
	 */
	public void setTestRaise(double testRaise) {
		this.testRaise = testRaise;
	}
	/**
	 * @return the eventDeductLimit
	 */
	public double getEventDeductLimit() {
		return eventDeductLimit;
	}
	/**
	 * @param eventDeductLimit the eventDeductLimit to set
	 */
	public void setEventDeductLimit(double eventDeductLimit) {
		this.eventDeductLimit = eventDeductLimit;
	}
}
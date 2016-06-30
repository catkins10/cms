package com.yuanluesoft.dpc.keyproject.pojo;

/**
 * 年度目标(keyproject_annual_objective)
 * @author linchuan
 *
 */
public class KeyProjectAnnualObjective  extends KeyProjectComponent {
	private int objectiveYear; //年度
	private double investPlan; //计划完成投资
	private double investCompleted; //已完成投资
	private String objective; //目标
	
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
	 * @return the objectiveYear
	 */
	public int getObjectiveYear() {
		return objectiveYear;
	}
	/**
	 * @param objectiveYear the objectiveYear to set
	 */
	public void setObjectiveYear(int objectiveYear) {
		this.objectiveYear = objectiveYear;
	}
	/**
	 * @return the investCompleted
	 */
	public double getInvestCompleted() {
		return investCompleted;
	}
	/**
	 * @param investCompleted the investCompleted to set
	 */
	public void setInvestCompleted(double investCompleted) {
		this.investCompleted = investCompleted;
	}
	/**
	 * @return the investPlan
	 */
	public double getInvestPlan() {
		return investPlan;
	}
	/**
	 * @param investPlan the investPlan to set
	 */
	public void setInvestPlan(double investPlan) {
		this.investPlan = investPlan;
	}
}
package com.yuanluesoft.dpc.keyproject.pojo;

/**
 * 项目:五年计划项目(keyproject_five_year_plan)
 * @author linchuan
 *
 */
public class KeyProjectFiveYearPlan extends KeyProjectComponent {
	private int fiveYearPlanNumber; //第几个五年计划项目
	private double previousFiveYearInvest; //上个五年计划完成投资
	private double currentFiveYearInvest; //当前五年计划规划投资
	private String currentFiveYearObjective; //当前五年计划工作目标
	
	/**
	 * @return the currentFiveYearInvest
	 */
	public double getCurrentFiveYearInvest() {
		return currentFiveYearInvest;
	}
	/**
	 * @param currentFiveYearInvest the currentFiveYearInvest to set
	 */
	public void setCurrentFiveYearInvest(double currentFiveYearInvest) {
		this.currentFiveYearInvest = currentFiveYearInvest;
	}
	/**
	 * @return the currentFiveYearObjective
	 */
	public String getCurrentFiveYearObjective() {
		return currentFiveYearObjective;
	}
	/**
	 * @param currentFiveYearObjective the currentFiveYearObjective to set
	 */
	public void setCurrentFiveYearObjective(String currentFiveYearObjective) {
		this.currentFiveYearObjective = currentFiveYearObjective;
	}
	/**
	 * @return the fiveYearPlanNumber
	 */
	public int getFiveYearPlanNumber() {
		return fiveYearPlanNumber;
	}
	/**
	 * @param fiveYearPlanNumber the fiveYearPlanNumber to set
	 */
	public void setFiveYearPlanNumber(int fiveYearPlanNumber) {
		this.fiveYearPlanNumber = fiveYearPlanNumber;
	}
	/**
	 * @return the previousFiveYearInvest
	 */
	public double getPreviousFiveYearInvest() {
		return previousFiveYearInvest;
	}
	/**
	 * @param previousFiveYearInvest the previousFiveYearInvest to set
	 */
	public void setPreviousFiveYearInvest(double previousFiveYearInvest) {
		this.previousFiveYearInvest = previousFiveYearInvest;
	}
}
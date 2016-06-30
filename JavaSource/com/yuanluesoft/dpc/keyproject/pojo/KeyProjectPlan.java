package com.yuanluesoft.dpc.keyproject.pojo;

/**
 * 工作安排(keyproject_plan)
 * @author linchuan
 *
 */
public class KeyProjectPlan extends KeyProjectComponent {
	private int planYear; //年份
	private int planMonth; //月份
	private String enterUnit; //项目参建单位新进场
	private String plan; //工作安排及需要调解决的意见
	/**
	 * @return the enterUnit
	 */
	public String getEnterUnit() {
		return enterUnit;
	}
	/**
	 * @param enterUnit the enterUnit to set
	 */
	public void setEnterUnit(String enterUnit) {
		this.enterUnit = enterUnit;
	}
	/**
	 * @return the plan
	 */
	public String getPlan() {
		return plan;
	}
	/**
	 * @param plan the plan to set
	 */
	public void setPlan(String plan) {
		this.plan = plan;
	}
	/**
	 * @return the planMonth
	 */
	public int getPlanMonth() {
		return planMonth;
	}
	/**
	 * @param planMonth the planMonth to set
	 */
	public void setPlanMonth(int planMonth) {
		this.planMonth = planMonth;
	}
	/**
	 * @return the planYear
	 */
	public int getPlanYear() {
		return planYear;
	}
	/**
	 * @param planYear the planYear to set
	 */
	public void setPlanYear(int planYear) {
		this.planYear = planYear;
	}
}
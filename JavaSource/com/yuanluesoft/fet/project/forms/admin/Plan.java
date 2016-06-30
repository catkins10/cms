package com.yuanluesoft.fet.project.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 项目管理:利用外资计划(fet_project_plan)
 * @author linchuan
 *
 */
public class Plan extends ActionForm {
	private String county; //区县
	private int planYear; //年度
	private int planMonth; //月份
	private int contractCheckPlan; //合同外资计划(验资口径)
	private int receiveCheckPlan; //实际到资计划(验资口径)
	private int contractPlan; //合同外资计划(可比口径)
	private int receivePlan; //实际到资计划(可比口径)
	
	/**
	 * @return the contractCheckPlan
	 */
	public int getContractCheckPlan() {
		return contractCheckPlan;
	}
	/**
	 * @param contractCheckPlan the contractCheckPlan to set
	 */
	public void setContractCheckPlan(int contractCheckPlan) {
		this.contractCheckPlan = contractCheckPlan;
	}
	/**
	 * @return the contractPlan
	 */
	public int getContractPlan() {
		return contractPlan;
	}
	/**
	 * @param contractPlan the contractPlan to set
	 */
	public void setContractPlan(int contractPlan) {
		this.contractPlan = contractPlan;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
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
	/**
	 * @return the receiveCheckPlan
	 */
	public int getReceiveCheckPlan() {
		return receiveCheckPlan;
	}
	/**
	 * @param receiveCheckPlan the receiveCheckPlan to set
	 */
	public void setReceiveCheckPlan(int receiveCheckPlan) {
		this.receiveCheckPlan = receiveCheckPlan;
	}
	/**
	 * @return the receivePlan
	 */
	public int getReceivePlan() {
		return receivePlan;
	}
	/**
	 * @param receivePlan the receivePlan to set
	 */
	public void setReceivePlan(int receivePlan) {
		this.receivePlan = receivePlan;
	}
}

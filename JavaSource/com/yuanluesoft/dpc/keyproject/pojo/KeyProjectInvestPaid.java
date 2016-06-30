package com.yuanluesoft.dpc.keyproject.pojo;

/**
 * 项目资金到位情况(keyproject_invest_paid)
 * @author linchuan
 *
 */
public class KeyProjectInvestPaid  extends KeyProjectComponent {
	private int paidYear; //年份
	private int paidMonth; //月份
	private double paidInvest; //当月到位资金金额
	private double yearInvest; //年初至报告期累计到位资金（万元）
	private double percentage; //占年计划（%）
	private String source; //资金来源
	private String childSource; //资金子来源
	private String remark; //来源说明
	
	/**
	 * 获取完整的资金来源
	 * @return
	 */
	public String getFullSource() {
		if(source==null || source.equals("")) {
			return null;
		}
		return source + (childSource==null || childSource.equals("") ? "" : "/" + childSource);
	}
	/**
	 * @return the paidInvest
	 */
	public double getPaidInvest() {
		return paidInvest;
	}
	/**
	 * @param paidInvest the paidInvest to set
	 */
	public void setPaidInvest(double paidInvest) {
		this.paidInvest = paidInvest;
	}
	/**
	 * @return the paidMonth
	 */
	public int getPaidMonth() {
		return paidMonth;
	}
	/**
	 * @param paidMonth the paidMonth to set
	 */
	public void setPaidMonth(int paidMonth) {
		this.paidMonth = paidMonth;
	}
	/**
	 * @return the paidYear
	 */
	public int getPaidYear() {
		return paidYear;
	}
	/**
	 * @param paidYear the paidYear to set
	 */
	public void setPaidYear(int paidYear) {
		this.paidYear = paidYear;
	}
	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the yearInvest
	 */
	public double getYearInvest() {
		return yearInvest;
	}
	/**
	 * @param yearInvest the yearInvest to set
	 */
	public void setYearInvest(double yearInvest) {
		this.yearInvest = yearInvest;
	}
	/**
	 * @return the childSource
	 */
	public String getChildSource() {
		return childSource;
	}
	/**
	 * @param childSource the childSource to set
	 */
	public void setChildSource(String childSource) {
		this.childSource = childSource;
	}
}
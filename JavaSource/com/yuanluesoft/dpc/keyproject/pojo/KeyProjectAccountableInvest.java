package com.yuanluesoft.dpc.keyproject.pojo;

/**
 * 项目总投资资金（责任制）(keyproject_accountable_invest)
 * @author linchuan
 *
 */
public class KeyProjectAccountableInvest extends KeyProjectComponent {
	private String source; //资金来源
	private String childSource; //资金子来源
	private double amount; //资金金额
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
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
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
package com.yuanluesoft.telex.base.model;

import java.util.List;

/**
 * 按密级统计
 * @author linchuan
 *
 */
public class TotalBySecurityLevel {
	private String securityLevel; //密级,含“合计”
	private List totals; //统计数据列表
	
	/**
	 * @return the securityLevel
	 */
	public String getSecurityLevel() {
		return securityLevel;
	}
	/**
	 * @param securityLevel the securityLevel to set
	 */
	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}
	/**
	 * @return the totals
	 */
	public List getTotals() {
		return totals;
	}
	/**
	 * @param totals the totals to set
	 */
	public void setTotals(List totals) {
		this.totals = totals;
	}
}
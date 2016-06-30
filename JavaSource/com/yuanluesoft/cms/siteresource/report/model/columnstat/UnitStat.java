package com.yuanluesoft.cms.siteresource.report.model.columnstat;

import java.util.List;

/**
 * 单位统计
 * @author linchuan
 *
 */
public class UnitStat {
	private long unitId; //单位ID
	private String unitName; //单位名称
	private int total; //发布统计
	private List columnStats; //栏目统计
	
	/**
	 * @return the columnStats
	 */
	public List getColumnStats() {
		return columnStats;
	}
	/**
	 * @param columnStats the columnStats to set
	 */
	public void setColumnStats(List columnStats) {
		this.columnStats = columnStats;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
}
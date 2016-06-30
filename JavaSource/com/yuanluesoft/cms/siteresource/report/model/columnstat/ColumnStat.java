package com.yuanluesoft.cms.siteresource.report.model.columnstat;

import java.util.List;

/**
 * 栏目统计
 * @author linchuan
 *
 */
public class ColumnStat {
	private long columnId; //栏目ID
	private String columnName; //栏目名称
	private int total; //发布统计
	private List columnStats; //子栏目统计
	private int rowspan; //占用的表格行数
	/**
	 * @return the columnId
	 */
	public long getColumnId() {
		return columnId;
	}
	/**
	 * @param columnId the columnId to set
	 */
	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
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
	 * @return the rowspan
	 */
	public int getRowspan() {
		return rowspan;
	}
	/**
	 * @param rowspan the rowspan to set
	 */
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
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
package com.yuanluesoft.cms.siteresource.report.model.ensurereport;

import java.util.List;

/**
 * 单位分类
 * @author linchuan
 *
 */
public class UnitCategory {
	private String category; //分类名称
	private String unitNames; //单位名称
	private String clusterUnitNames; //集群单位名称
	private String notClusterUnitNames; //非集群单位名称
	private List unitStats; //各单位统计列表

	/**
	 * 获取单位总数
	 * @return
	 */
	public int getUnitCount() {
		return unitStats==null ? 0 : unitStats.size();
	}
	
	/**
	 * @return the unitStats
	 */
	public List getUnitStats() {
		return unitStats;
	}

	/**
	 * @param unitStats the unitStats to set
	 */
	public void setUnitStats(List unitStats) {
		this.unitStats = unitStats;
	}

	/**
	 * @return the clusterUnitNames
	 */
	public String getClusterUnitNames() {
		return clusterUnitNames;
	}

	/**
	 * @param clusterUnitNames the clusterUnitNames to set
	 */
	public void setClusterUnitNames(String clusterUnitNames) {
		this.clusterUnitNames = clusterUnitNames;
	}

	/**
	 * @return the notClusterUnitNames
	 */
	public String getNotClusterUnitNames() {
		return notClusterUnitNames;
	}

	/**
	 * @param notClusterUnitNames the notClusterUnitNames to set
	 */
	public void setNotClusterUnitNames(String notClusterUnitNames) {
		this.notClusterUnitNames = notClusterUnitNames;
	}

	/**
	 * @return the unitNames
	 */
	public String getUnitNames() {
		return unitNames;
	}

	/**
	 * @param unitNames the unitNames to set
	 */
	public void setUnitNames(String unitNames) {
		this.unitNames = unitNames;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}
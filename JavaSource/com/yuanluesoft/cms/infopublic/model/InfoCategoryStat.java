package com.yuanluesoft.cms.infopublic.model;

/**
 * 
 * @author linchuan
 *
 */
public class InfoCategoryStat {
	private String name; //分类名称
	private int yearTotal; //年数量
	private int currentSeasonTotal; //本季度
	private int previousSeasonTotal; //上季度
	
	/**
	 * @return the currentSeasonTotal
	 */
	public int getCurrentSeasonTotal() {
		return currentSeasonTotal;
	}
	/**
	 * @param currentSeasonTotal the currentSeasonTotal to set
	 */
	public void setCurrentSeasonTotal(int currentSeasonTotal) {
		this.currentSeasonTotal = currentSeasonTotal;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the previousSeasonTotal
	 */
	public int getPreviousSeasonTotal() {
		return previousSeasonTotal;
	}
	/**
	 * @param previousSeasonTotal the previousSeasonTotal to set
	 */
	public void setPreviousSeasonTotal(int previousSeasonTotal) {
		this.previousSeasonTotal = previousSeasonTotal;
	}
	/**
	 * @return the yearTotal
	 */
	public int getYearTotal() {
		return yearTotal;
	}
	/**
	 * @param yearTotal the yearTotal to set
	 */
	public void setYearTotal(int yearTotal) {
		this.yearTotal = yearTotal;
	}
}
/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.view.statisticview.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class StatisticView extends com.yuanluesoft.jeaf.view.model.View implements Serializable {
	private List statistics; //统计列表
	private boolean hideDetail; //是否隐藏明细
	private String detailTitle; //明细项目标题
	
	//输出视图时使用
	private int headRowCount = 1; //表头行数
	private int headColCount = 1; //行头行数,统计列(type="statistic")之前的列数
	
	/**
	 * @return Returns the headRowCount.
	 */
	public int getHeadRowCount() {
		return headRowCount;
	}
	/**
	 * @param headRowCount The headRowCount to set.
	 */
	public void setHeadRowCount(int headRowCount) {
		this.headRowCount = headRowCount;
	}
	/**
	 * @return Returns the statistics.
	 */
	public List getStatistics() {
		return statistics;
	}
	/**
	 * @param statistics The statistics to set.
	 */
	public void setStatistics(List statistics) {
		this.statistics = statistics;
	}
	/**
	 * @return Returns the hideDetail.
	 */
	public boolean isHideDetail() {
		return hideDetail;
	}
	/**
	 * @param hideDetail The hideDetail to set.
	 */
	public void setHideDetail(boolean hideDetail) {
		this.hideDetail = hideDetail;
	}
	/**
	 * @return Returns the headColCount.
	 */
	public int getHeadColCount() {
		return headColCount;
	}
	/**
	 * @param headColCount The headColCount to set.
	 */
	public void setHeadColCount(int headColCount) {
		this.headColCount = headColCount;
	}
	/**
	 * @return the detailTitle
	 */
	public String getDetailTitle() {
		return detailTitle;
	}
	/**
	 * @param detailTitle the detailTitle to set
	 */
	public void setDetailTitle(String detailTitle) {
		this.detailTitle = detailTitle;
	}
}

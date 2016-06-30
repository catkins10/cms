package com.yuanluesoft.chd.evaluation.data.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class IndicatorData extends ActionForm {
	private long directoryId; //发电企业ID
	private int dataYear; //年度
	private int dataMonth; //月度
	private List indicatorDataList; //指标完成情况列表
	
	/**
	 * @return the dataMonth
	 */
	public int getDataMonth() {
		return dataMonth;
	}
	/**
	 * @param dataMonth the dataMonth to set
	 */
	public void setDataMonth(int dataMonth) {
		this.dataMonth = dataMonth;
	}
	/**
	 * @return the dataYear
	 */
	public int getDataYear() {
		return dataYear;
	}
	/**
	 * @param dataYear the dataYear to set
	 */
	public void setDataYear(int dataYear) {
		this.dataYear = dataYear;
	}
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the indicatorDataList
	 */
	public List getIndicatorDataList() {
		return indicatorDataList;
	}
	/**
	 * @param indicatorDataList the indicatorDataList to set
	 */
	public void setIndicatorDataList(List indicatorDataList) {
		this.indicatorDataList = indicatorDataList;
	}
}
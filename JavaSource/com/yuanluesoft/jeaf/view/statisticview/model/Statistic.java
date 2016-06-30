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
public class Statistic implements Serializable {
	private String title;
	private boolean statisticAll;
	private List groupFields;
	private List statisticColumns;
	
	/**
	 * @return Returns the groupFields.
	 */
	public List getGroupFields() {
		return groupFields;
	}
	/**
	 * @param groupFields The groupFields to set.
	 */
	public void setGroupFields(List groupFields) {
		this.groupFields = groupFields;
	}
	/**
	 * @return Returns the statisticAll.
	 */
	public boolean isStatisticAll() {
		return statisticAll;
	}
	/**
	 * @param statisticAll The statisticAll to set.
	 */
	public void setStatisticAll(boolean statisticAll) {
		this.statisticAll = statisticAll;
	}
	/**
	 * @return Returns the statisticColumns.
	 */
	public List getStatisticColumns() {
		return statisticColumns;
	}
	/**
	 * @param statisticColumns The statisticColumns to set.
	 */
	public void setStatisticColumns(List statisticColumns) {
		this.statisticColumns = statisticColumns;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}

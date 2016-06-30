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
public class Column extends com.yuanluesoft.jeaf.view.model.Column implements Serializable {
	private String groupBy; //type=="group",指定分类字段
	private List groupValues; //分组数据
	private String sourceField; //type=="group",指定源数据
	private boolean emptyWhenStatistic; //统计时是否清空,如:备注
	
	public Column() {
		super();
	}
	
	public Column(String name, String title, String type) {
		super(name, title, type);
	}
	
	public int getGroupValuesSize() {
		return groupValues==null ? 1 : groupValues.size();
	}
	/**
	 * @return Returns the groupBy.
	 */
	public String getGroupBy() {
		return groupBy;
	}
	/**
	 * @param groupBy The groupBy to set.
	 */
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	/**
	 * @return Returns the groupValues.
	 */
	public List getGroupValues() {
		return groupValues;
	}
	/**
	 * @param groupValues The groupValues to set.
	 */
	public void setGroupValues(List groupValues) {
		this.groupValues = groupValues;
	}
	/**
	 * @return Returns the sourceField.
	 */
	public String getSourceField() {
		return sourceField;
	}
	/**
	 * @param sourceField The sourceField to set.
	 */
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
	/**
	 * @return the emptyWhenStatistic
	 */
	public boolean isEmptyWhenStatistic() {
		return emptyWhenStatistic;
	}
	/**
	 * @param emptyWhenStatistic the emptyWhenStatistic to set
	 */
	public void setEmptyWhenStatistic(boolean emptyWhenStatistic) {
		this.emptyWhenStatistic = emptyWhenStatistic;
	}
}

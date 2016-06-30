package com.yuanluesoft.dpc.keyproject.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 重点项目:资金来源(keyproject_invest_source)
 * @author linchuan
 *
 */
public class KeyProjectInvestSource extends Record {
	private String source; //来源
	private String childSource; //子来源
	private float priority; //优先级
	
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
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
}
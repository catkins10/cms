package com.yuanluesoft.jeaf.numeration.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 编号服务:当前序号(numeration_sequence)
 * @author linchuan
 *
 */
public class NumerationSequence extends Record {
	private String application; //应用名称,如:项目管理
	private String name; //编号名称,如:项目编号
	private String numerationRange; //编号范围,如:榕市建安招2009
	private int sequence; //序号
	
	/**
	 * @return the application
	 */
	public String getApplication() {
		return application;
	}
	/**
	 * @param application the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
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
	 * @return the range
	 */
	public String getNumerationRange() {
		return numerationRange;
	}
	/**
	 * @param range the range to set
	 */
	public void setNumerationRange(String range) {
		this.numerationRange = range;
	}
	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}
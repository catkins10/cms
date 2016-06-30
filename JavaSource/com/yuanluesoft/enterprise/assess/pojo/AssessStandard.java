package com.yuanluesoft.enterprise.assess.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考核/汇报配置:考核内容和标准(assess_standard)
 * @author linchuan
 *
 */
public class AssessStandard extends Record {
	private long classifyId; //考核类型ID
	private String content; //考核内容
	private String standard; //说明及评分标准
	private double maxValue; //最大分值
	private double priority; //顺序号
	
	/**
	 * @return the classifyId
	 */
	public long getClassifyId() {
		return classifyId;
	}
	/**
	 * @param classifyId the classifyId to set
	 */
	public void setClassifyId(long classifyId) {
		this.classifyId = classifyId;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the maxValue
	 */
	public double getMaxValue() {
		return maxValue;
	}
	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	/**
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}
	/**
	 * @return the standard
	 */
	public String getStandard() {
		return standard;
	}
	/**
	 * @param standard the standard to set
	 */
	public void setStandard(String standard) {
		this.standard = standard;
	}
}
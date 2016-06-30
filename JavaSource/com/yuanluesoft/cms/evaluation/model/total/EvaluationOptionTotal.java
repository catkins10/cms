package com.yuanluesoft.cms.evaluation.model.total;


/**
 * 测评选项汇总
 * @author linchuan
 *
 */
public class EvaluationOptionTotal {
	private long optionId; //选项ID
	private int count; //选中次数
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the optionId
	 */
	public long getOptionId() {
		return optionId;
	}
	/**
	 * @param optionId the optionId to set
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}
}
package com.yuanluesoft.job.talent.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才:求职意向工作性质(job_talent_intention_type)
 * @author linchuan
 *
 */
public class JobTalentIntentionType extends Record {
	private long intentionId; //求职意向ID
	private int type; //工作性质,全职,兼职,实习,暑期工
	
	/**
	 * @return the intentionId
	 */
	public long getIntentionId() {
		return intentionId;
	}
	/**
	 * @param intentionId the intentionId to set
	 */
	public void setIntentionId(long intentionId) {
		this.intentionId = intentionId;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
}
package com.yuanluesoft.chd.evaluation.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 年度目标(chd_eval_objective)
 * @author linchuan
 *
 */
public class ChdEvaluationObjective extends Record {
	private long plantId; //发电企业ID
	private int objectiveYear; //年度
	private String objective; //年度目标
	private String result; //完成情况
	private Timestamp created; //提交时间
	private long creatorId; //提交人ID
	private String creator; //提交人姓名
	private String remark; //备注
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the objective
	 */
	public String getObjective() {
		return objective;
	}
	/**
	 * @param objective the objective to set
	 */
	public void setObjective(String objective) {
		this.objective = objective;
	}
	/**
	 * @return the objectiveYear
	 */
	public int getObjectiveYear() {
		return objectiveYear;
	}
	/**
	 * @param objectiveYear the objectiveYear to set
	 */
	public void setObjectiveYear(int objectiveYear) {
		this.objectiveYear = objectiveYear;
	}
	/**
	 * @return the plantId
	 */
	public long getPlantId() {
		return plantId;
	}
	/**
	 * @param plantId the plantId to set
	 */
	public void setPlantId(long plantId) {
		this.plantId = plantId;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
}

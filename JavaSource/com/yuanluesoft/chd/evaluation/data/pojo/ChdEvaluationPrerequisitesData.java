package com.yuanluesoft.chd.evaluation.data.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 必备条件完成情况(chd_eval_prerequisites_data)
 * @author linchuan
 *
 */
public class ChdEvaluationPrerequisitesData extends Record {
	private long plantId; //发电企业ID
	private String plant; //发电企业名称
	private int declareYear; //年度
	private long prerequisitesId; //必备条件ID
	private String prerequisites; //必备条件
	private String result; //完成情况说明
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
	 * @return the declareYear
	 */
	public int getDeclareYear() {
		return declareYear;
	}
	/**
	 * @param declareYear the declareYear to set
	 */
	public void setDeclareYear(int declareYear) {
		this.declareYear = declareYear;
	}
	/**
	 * @return the plant
	 */
	public String getPlant() {
		return plant;
	}
	/**
	 * @param plant the plant to set
	 */
	public void setPlant(String plant) {
		this.plant = plant;
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
	 * @return the prerequisites
	 */
	public String getPrerequisites() {
		return prerequisites;
	}
	/**
	 * @param prerequisites the prerequisites to set
	 */
	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}
	/**
	 * @return the prerequisitesId
	 */
	public long getPrerequisitesId() {
		return prerequisitesId;
	}
	/**
	 * @param prerequisitesId the prerequisitesId to set
	 */
	public void setPrerequisitesId(long prerequisitesId) {
		this.prerequisitesId = prerequisitesId;
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
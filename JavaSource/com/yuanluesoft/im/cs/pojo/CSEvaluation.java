package com.yuanluesoft.im.cs.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 客服:服务评价(im_cs_evaluation)
 * @author linchuan
 *
 */
public class CSEvaluation extends Record {
	private long siteId; //站点ID
	private long specialistId; //客服人员ID
	private String specialistName; //客服人员姓名
	private String level; //评价等级
	private String evaluateIP; //评价人IP
	private Timestamp evaluateTime; //评价时间
	private String remark; //补充说明
	
	/**
	 * @return the evaluateIP
	 */
	public String getEvaluateIP() {
		return evaluateIP;
	}
	/**
	 * @param evaluateIP the evaluateIP to set
	 */
	public void setEvaluateIP(String evaluateIP) {
		this.evaluateIP = evaluateIP;
	}
	/**
	 * @return the evaluateTime
	 */
	public Timestamp getEvaluateTime() {
		return evaluateTime;
	}
	/**
	 * @param evaluateTime the evaluateTime to set
	 */
	public void setEvaluateTime(Timestamp evaluateTime) {
		this.evaluateTime = evaluateTime;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
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
	 * @return the specialistId
	 */
	public long getSpecialistId() {
		return specialistId;
	}
	/**
	 * @param specialistId the specialistId to set
	 */
	public void setSpecialistId(long specialistId) {
		this.specialistId = specialistId;
	}
	/**
	 * @return the specialistName
	 */
	public String getSpecialistName() {
		return specialistName;
	}
	/**
	 * @param specialistName the specialistName to set
	 */
	public void setSpecialistName(String specialistName) {
		this.specialistName = specialistName;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}
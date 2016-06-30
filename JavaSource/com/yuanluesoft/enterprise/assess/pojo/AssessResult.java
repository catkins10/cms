package com.yuanluesoft.enterprise.assess.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 绩效考核:个人考核(assess_result)
 * @author linchuan
 *
 */
public class AssessResult extends Record {
	private long assessId; //考核ID
	private long personId; //被考核人ID
	private String personName; //被考核人姓名
	private long classifyId; //考核类型ID
	private float result; //考核成绩
	private String remark; //备注
	private Set individualResults; //各单项成绩列表

	/**
	 * @return the assessId
	 */
	public long getAssessId() {
		return assessId;
	}
	/**
	 * @param assessId the assessId to set
	 */
	public void setAssessId(long assessId) {
		this.assessId = assessId;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
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
	public float getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(float result) {
		this.result = result;
	}
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
	 * @return the individualResults
	 */
	public Set getIndividualResults() {
		return individualResults;
	}
	/**
	 * @param individualResults the individualResults to set
	 */
	public void setIndividualResults(Set individualResults) {
		this.individualResults = individualResults;
	}
}
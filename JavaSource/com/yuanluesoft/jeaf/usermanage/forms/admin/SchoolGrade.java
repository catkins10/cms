package com.yuanluesoft.jeaf.usermanage.forms.admin;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 *
 */
public class SchoolGrade extends ActionForm {
	private int enrollTime;
	private String gradeName;
	private int lengthOfSchooling;
	private long parentOrgId;
	private int classCount;
	
	private List gradeNames; //年级列表
	
	/**
	 * @return the classCount
	 */
	public int getClassCount() {
		return classCount;
	}
	/**
	 * @param classCount the classCount to set
	 */
	public void setClassCount(int classCount) {
		this.classCount = classCount;
	}
	/**
	 * @return the enrollTime
	 */
	public int getEnrollTime() {
		return enrollTime;
	}
	/**
	 * @param enrollTime the enrollTime to set
	 */
	public void setEnrollTime(int enrollTime) {
		this.enrollTime = enrollTime;
	}
	/**
	 * @return the gradeName
	 */
	public String getGradeName() {
		return gradeName;
	}
	/**
	 * @param gradeName the gradeName to set
	 */
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	/**
	 * @return the parentOrgId
	 */
	public long getParentOrgId() {
		return parentOrgId;
	}
	/**
	 * @param parentOrgId the parentOrgId to set
	 */
	public void setParentOrgId(long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}
	/**
	 * @return the lengthOfSchooling
	 */
	public int getLengthOfSchooling() {
		return lengthOfSchooling;
	}
	/**
	 * @param lengthOfSchooling the lengthOfSchooling to set
	 */
	public void setLengthOfSchooling(int lengthOfSchooling) {
		this.lengthOfSchooling = lengthOfSchooling;
	}
	/**
	 * @return the gradeNames
	 */
	public List getGradeNames() {
		return gradeNames;
	}
	/**
	 * @param gradeNames the gradeNames to set
	 */
	public void setGradeNames(List gradeNames) {
		this.gradeNames = gradeNames;
	}
}
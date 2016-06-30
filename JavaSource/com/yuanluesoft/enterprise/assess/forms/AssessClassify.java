package com.yuanluesoft.enterprise.assess.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * 
 * @author linchuan
 *
 */
public class AssessClassify extends ActionForm {
	private String classify; //类型,如项目组考核、业务员考核
	private float priority; //优先级
	private char selfAssess = '0'; //需要自评
	private char projectTeamAccess = '0'; //只适用于项目组

	private Set assessUsers; //适用的用户列表
	private Set standards; //考核标准列表
	private Set activities; //考核阶段列表
	
	//适用的用户列表
	private RecordVisitorList assessUser = new RecordVisitorList();
	
	/**
	 * @return the activities
	 */
	public Set getActivities() {
		return activities;
	}
	/**
	 * @param activities the activities to set
	 */
	public void setActivities(Set activities) {
		this.activities = activities;
	}
	/**
	 * @return the assessUsers
	 */
	public Set getAssessUsers() {
		return assessUsers;
	}
	/**
	 * @param assessUsers the assessUsers to set
	 */
	public void setAssessUsers(Set assessUsers) {
		this.assessUsers = assessUsers;
	}
	/**
	 * @return the classify
	 */
	public String getClassify() {
		return classify;
	}
	/**
	 * @param classify the classify to set
	 */
	public void setClassify(String classify) {
		this.classify = classify;
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
	/**
	 * @return the selfAssess
	 */
	public char getSelfAssess() {
		return selfAssess;
	}
	/**
	 * @param selfAssess the selfAssess to set
	 */
	public void setSelfAssess(char selfAssess) {
		this.selfAssess = selfAssess;
	}
	/**
	 * @return the standards
	 */
	public Set getStandards() {
		return standards;
	}
	/**
	 * @param standards the standards to set
	 */
	public void setStandards(Set standards) {
		this.standards = standards;
	}
	/**
	 * @return the assessUser
	 */
	public RecordVisitorList getAssessUser() {
		return assessUser;
	}
	/**
	 * @param assessUser the assessUser to set
	 */
	public void setAssessUser(RecordVisitorList assessUser) {
		this.assessUser = assessUser;
	}
	/**
	 * @return the projectTeamAccess
	 */
	public char getProjectTeamAccess() {
		return projectTeamAccess;
	}
	/**
	 * @param projectTeamAccess the projectTeamAccess to set
	 */
	public void setProjectTeamAccess(char projectTeamAccess) {
		this.projectTeamAccess = projectTeamAccess;
	}
}
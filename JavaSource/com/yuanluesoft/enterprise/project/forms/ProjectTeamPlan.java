package com.yuanluesoft.enterprise.project.forms;

import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeamPlan;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectTeamPlan extends Project {
	private EnterpriseProjectTeamPlan plan = new EnterpriseProjectTeamPlan();
	private String teamMembers; //项目组成员列表
	
	/**
	 * @return the plan
	 */
	public EnterpriseProjectTeamPlan getPlan() {
		return plan;
	}
	/**
	 * @param plan the plan to set
	 */
	public void setPlan(EnterpriseProjectTeamPlan plan) {
		this.plan = plan;
	}
	/**
	 * @return the teamMembers
	 */
	public String getTeamMembers() {
		return teamMembers;
	}
	/**
	 * @param teamMembers the teamMembers to set
	 */
	public void setTeamMembers(String teamMembers) {
		this.teamMembers = teamMembers;
	}
}
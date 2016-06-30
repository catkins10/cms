/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.directorymanage.forms.DirectoryForm;

/**
 * @author Administrator
 *
 *
 */
public class Org extends DirectoryForm {
	private Timestamp lastModified; //最后修改时间
	private Set leaders; //部门领导
	private Set supervisors; //主管领导
	
	private String leaderIds; //部门领导ID
	private String leaderNames; //部门领导姓名
	private String supervisorIds; //主管领导ID
	private String supervisorNames; //主管领导姓名
	
	/**
	 * @return the leaders
	 */
	public Set getLeaders() {
		return leaders;
	}
	/**
	 * @param leaders the leaders to set
	 */
	public void setLeaders(Set leaders) {
		this.leaders = leaders;
	}
	/**
	 * @return the supervisors
	 */
	public Set getSupervisors() {
		return supervisors;
	}
	/**
	 * @param supervisors the supervisors to set
	 */
	public void setSupervisors(Set supervisors) {
		this.supervisors = supervisors;
	}
	/**
	 * @return the leaderIds
	 */
	public String getLeaderIds() {
		return leaderIds;
	}
	/**
	 * @param leaderIds the leaderIds to set
	 */
	public void setLeaderIds(String leaderIds) {
		this.leaderIds = leaderIds;
	}
	/**
	 * @return the leaderNames
	 */
	public String getLeaderNames() {
		return leaderNames;
	}
	/**
	 * @param leaderNames the leaderNames to set
	 */
	public void setLeaderNames(String leaderNames) {
		this.leaderNames = leaderNames;
	}
	/**
	 * @return the supervisorIds
	 */
	public String getSupervisorIds() {
		return supervisorIds;
	}
	/**
	 * @param supervisorIds the supervisorIds to set
	 */
	public void setSupervisorIds(String supervisorIds) {
		this.supervisorIds = supervisorIds;
	}
	/**
	 * @return the supervisorNames
	 */
	public String getSupervisorNames() {
		return supervisorNames;
	}
	/**
	 * @param supervisorNames the supervisorNames to set
	 */
	public void setSupervisorNames(String supervisorNames) {
		this.supervisorNames = supervisorNames;
	}
	/**
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
}
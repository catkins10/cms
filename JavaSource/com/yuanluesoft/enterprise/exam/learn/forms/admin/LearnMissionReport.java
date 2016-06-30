package com.yuanluesoft.enterprise.exam.learn.forms.admin;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class LearnMissionReport extends ActionForm {
	private long missionId; //任务ID
	private List learned; //已学习人员(ExamTest)
	private List notLearnPersons; //未学习人员(Peron)
	
	/**
	 * @return the learned
	 */
	public List getLearned() {
		return learned;
	}
	/**
	 * @param learned the learned to set
	 */
	public void setLearned(List learned) {
		this.learned = learned;
	}
	/**
	 * @return the missionId
	 */
	public long getMissionId() {
		return missionId;
	}
	/**
	 * @param missionId the missionId to set
	 */
	public void setMissionId(long missionId) {
		this.missionId = missionId;
	}
	/**
	 * @return the notLearnPersons
	 */
	public List getNotLearnPersons() {
		return notLearnPersons;
	}
	/**
	 * @param notLearnPersons the notLearnPersons to set
	 */
	public void setNotLearnPersons(List notLearnPersons) {
		this.notLearnPersons = notLearnPersons;
	}
}
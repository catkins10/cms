package com.yuanluesoft.j2oa.personnel.forms;

import com.yuanluesoft.j2oa.personnel.pojo.PersonnelRewardsPunishment;


/**
 * 
 * @author linchuan
 *
 */
public class RewardsPunishment extends Employee {
	private PersonnelRewardsPunishment rewardsPunishment = new PersonnelRewardsPunishment();

	/**
	 * @return the rewardsPunishment
	 */
	public PersonnelRewardsPunishment getRewardsPunishment() {
		return rewardsPunishment;
	}

	/**
	 * @param rewardsPunishment the rewardsPunishment to set
	 */
	public void setRewardsPunishment(PersonnelRewardsPunishment rewardsPunishment) {
		this.rewardsPunishment = rewardsPunishment;
	}
}
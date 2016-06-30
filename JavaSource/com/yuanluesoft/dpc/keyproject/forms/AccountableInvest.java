package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectAccountableInvest;

/**
 * 
 * @author linchuan
 *
 */
public class AccountableInvest extends Project {
	private KeyProjectAccountableInvest projectAccountableInvest = new KeyProjectAccountableInvest();

	/**
	 * @return the projectAccountableInvest
	 */
	public KeyProjectAccountableInvest getProjectAccountableInvest() {
		return projectAccountableInvest;
	}

	/**
	 * @param projectAccountableInvest the projectAccountableInvest to set
	 */
	public void setProjectAccountableInvest(
			KeyProjectAccountableInvest projectAccountableInvest) {
		this.projectAccountableInvest = projectAccountableInvest;
	}
}
package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvest;

/**
 * 
 * @author linchuan
 *
 */
public class Invest extends Project {
	private KeyProjectInvest projectInvest = new KeyProjectInvest();

	/**
	 * @return the projectInvest
	 */
	public KeyProjectInvest getProjectInvest() {
		return projectInvest;
	}

	/**
	 * @param projectInvest the projectInvest to set
	 */
	public void setProjectInvest(KeyProjectInvest projectInvest) {
		this.projectInvest = projectInvest;
	}
}
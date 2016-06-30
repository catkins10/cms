package com.yuanluesoft.fdi.project.forms.admin;

import com.yuanluesoft.fdi.project.pojo.FdiProjectPush;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectPush extends Project {
	private FdiProjectPush push = new FdiProjectPush();

	/**
	 * @return the push
	 */
	public FdiProjectPush getPush() {
		return push;
	}

	/**
	 * @param push the push to set
	 */
	public void setPush(FdiProjectPush push) {
		this.push = push;
	}
}
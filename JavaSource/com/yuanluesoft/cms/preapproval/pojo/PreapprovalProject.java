/**
 * 
 */
package com.yuanluesoft.cms.preapproval.pojo;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 预审项目配置(cms_pre_approval_project)
 * @author linchuan
 *
 */
public class PreapprovalProject extends Record {
	private String name; //项目名称
	private String description; //项目介绍

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}

package com.yuanluesoft.chd.evaluation.forms.admin;

import java.util.Set;

/**
 * 
 * @author linchuan
 *
 */
public class Company extends DirectoryForm {
	private Set levels; //评价等级列表

	/**
	 * @return the levels
	 */
	public Set getLevels() {
		return levels;
	}

	/**
	 * @param levels the levels to set
	 */
	public void setLevels(Set levels) {
		this.levels = levels;
	}
}
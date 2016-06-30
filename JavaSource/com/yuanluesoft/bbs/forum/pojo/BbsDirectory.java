package com.yuanluesoft.bbs.forum.pojo;

import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;

/**
 * 论坛:论坛目录(bbs_directory)
 * @author linchuan
 *
 */
public class BbsDirectory extends Directory {
	private String description; //描述

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
}

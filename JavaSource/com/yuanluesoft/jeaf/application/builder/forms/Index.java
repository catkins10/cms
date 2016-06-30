package com.yuanluesoft.jeaf.application.builder.forms;

import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationIndex;

/**
 * 
 * @author linchuan
 *
 */
public class Index extends ApplicationForm {
	private ApplicationIndex index = new ApplicationIndex();

	/**
	 * @return the index
	 */
	public ApplicationIndex getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(ApplicationIndex index) {
		this.index = index;
	}
}
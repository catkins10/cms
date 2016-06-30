package com.yuanluesoft.appraise.forms.admin;

import com.yuanluesoft.appraise.pojo.AppraiseOption;

/**
 * 
 * @author linchuan
 *
 */
public class Option extends Task {
	private AppraiseOption option = new AppraiseOption();

	/**
	 * @return the option
	 */
	public AppraiseOption getOption() {
		return option;
	}

	/**
	 * @param option the option to set
	 */
	public void setOption(AppraiseOption option) {
		this.option = option;
	}
}
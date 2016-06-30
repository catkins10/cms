package com.yuanluesoft.cms.scene.forms;

import com.yuanluesoft.cms.sitemanage.forms.Select;

/**
 * 
 * @author linchuan
 *
 */
public class SelectScene extends Select {
	private boolean oneSiteOnly; //是否只显示一个站点

	/**
	 * @return the oneSiteOnly
	 */
	public boolean isOneSiteOnly() {
		return oneSiteOnly;
	}

	/**
	 * @param oneSiteOnly the oneSiteOnly to set
	 */
	public void setOneSiteOnly(boolean oneSiteOnly) {
		this.oneSiteOnly = oneSiteOnly;
	}
}
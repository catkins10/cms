package com.yuanluesoft.cms.advert.forms;

import com.yuanluesoft.cms.sitemanage.forms.Select;

/**
 * 
 * @author linchuan
 *
 */
public class SelectAdvert extends Select {
	private boolean floatOnly; //仅浮动广告
	private boolean fixedOnly; //仅固定位置广告
	
	/**
	 * @return the fixedOnly
	 */
	public boolean isFixedOnly() {
		return fixedOnly;
	}
	/**
	 * @param fixedOnly the fixedOnly to set
	 */
	public void setFixedOnly(boolean fixedOnly) {
		this.fixedOnly = fixedOnly;
	}
	/**
	 * @return the floatOnly
	 */
	public boolean isFloatOnly() {
		return floatOnly;
	}
	/**
	 * @param floatOnly the floatOnly to set
	 */
	public void setFloatOnly(boolean floatOnly) {
		this.floatOnly = floatOnly;
	}
}
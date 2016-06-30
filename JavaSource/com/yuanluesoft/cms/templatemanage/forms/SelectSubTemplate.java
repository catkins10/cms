package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.cms.sitemanage.forms.Select;

/**
 * 
 * @author linchuan
 *
 */
public class SelectSubTemplate extends Select {
	private long themeId; //主题ID

	/**
	 * @return the themeId
	 */
	public long getThemeId() {
		return themeId;
	}

	/**
	 * @param themeId the themeId to set
	 */
	public void setThemeId(long themeId) {
		this.themeId = themeId;
	}
}
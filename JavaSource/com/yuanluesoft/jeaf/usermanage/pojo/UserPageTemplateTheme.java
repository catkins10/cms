package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;

/**
 * 模板管理:用户模板主题(user_template_theme)
 * @author linchuan
 *
 */
public class UserPageTemplateTheme extends TemplateTheme {
	private long userId; //用户ID

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
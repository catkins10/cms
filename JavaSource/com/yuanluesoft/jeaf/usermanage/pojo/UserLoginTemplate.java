package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.cms.templatemanage.pojo.Template;

/**
 * 用户管理:登录模板(user_login_template)
 * @author linchuan
 *
 */
public class UserLoginTemplate extends Template {
	private String hostNames; //域名
	
	/**
	 * 获取对外显示的域名
	 * @return
	 */
	public String getHostNamesText() {
		return hostNames==null || !hostNames.startsWith(",") ? hostNames : hostNames.substring(1, hostNames.length()-1);
	}

	/**
	 * @return the hostNames
	 */
	public String getHostNames() {
		return hostNames;
	}

	/**
	 * @param hostNames the hostNames to set
	 */
	public void setHostNames(String hostNames) {
		this.hostNames = hostNames;
	}
}
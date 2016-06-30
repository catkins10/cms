package com.yuanluesoft.jeaf.sso.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.membermanage.exception.LoginException;

/**
 * 登录策略服务
 * @author linchuan
 *
 */
public interface LoginPolicyService {
	
	/**
	 * 检查登录策略
	 * @param userLoginName
	 * @param request
	 * @throws LoginException
	 */
	public void validateLoginPolicy(String userLoginName, HttpServletRequest request) throws LoginException;
}

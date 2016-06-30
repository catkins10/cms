/*
 * Created on 2006-3-9
 *
 */
package com.yuanluesoft.jeaf.sso.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.sso.service.exception.SsoSessionException;

/**
 * 
 * @author linchuan
 *
 */
public interface SsoSessionService {
	public final static String SSO_SESSION_COOKIE_NAME = "SsoSessionId";
	public final static String SSO_LOGIN_LAST_LOGIN_NAME = "lastLoginName";
	public final static String SSO_LOGIN_REMEMBER_LOGIN_NAME = "rememberLoginName";
	
	/**
	 * 创建sso会话
	 * @param personLoginName
	 * @param eternal 是否会话永久有效,一般在客户端登录时为true
	 * @return 会话ID
	 * @throws SsoSessionException
	 */
	public String createSsoSession(String personLoginName, boolean eternal) throws SsoSessionException;
	
	/**
	 * 复制永久有效的会话,以避免被注销
	 * @param ssoSessionId
	 * @return
	 * @throws SsoSessionException
	 */
	public String cloneSsoSession(String ssoSessionId) throws SsoSessionException;
	
	/**
	 * 创建TICKET, 由登录服务调用
	 * @param ssoSessionId
	 * @return
	 * @throws SsoSessionException
	 */
	public String createTicket(String ssoSessionId) throws SsoSessionException;

	/**
	 * 注销
	 * @param ssoSessionId
	 * @throws SsoSessionException
	 */
	public void removeSsoSession(String ssoSessionId) throws SsoSessionException;
	
	/**
	 * 根据TICKET获取登录用户名
	 * @param ssoSessionId
	 * @return 用户名 + "," + 登录顺序号
	 * @throws SsoSessionException
	 */
	public String getLoginNameByTicket(String ticket) throws SsoSessionException;
	
	/**
	 * 检查会话有效性
	 * @param personLoginName
	 * @param loginSequence
	 * @return
	 * @throws SsoSessionException
	 */
	public boolean verifySession(String personLoginName, long loginSequence) throws SsoSessionException;
	
	/**
	 * 按会话ID获取登录用户名
	 * @param ssoSessionId
	 * @return
	 * @throws SsoSessionException
	 */
	public String getLoginNameBySessionId(String ssoSessionId) throws SsoSessionException;
	
	/**
	 * 登录
	 * @param userLoginName
	 * @param password
	 * @param passwordMatcher 密码匹配器,用来检查密码是否一致
	 * @param request
	 * @return
	 * @throws LoginException
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException;
}
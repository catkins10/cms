/*
 * Created on 2006-3-10
 *
 */
package com.yuanluesoft.jeaf.sessionmanage.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface SessionService {
	public static final String ANONYMOUS = "anonymous"; //匿名用户的登录用户名
	public static final String ANONYMOUS_USERNAME = "游客"; //匿名用户的登录用户名
	public static final long ANONYMOUS_USERID = 10; //匿名用户的ID
	public static final long ANONYMOUS_DEPARTMENTID = 0; //匿名用户隶属的部门ID
	public static final SessionInfo ANONYMOUS_SESSION = new SessionInfo(ANONYMOUS, ANONYMOUS_USERNAME, ANONYMOUS_USERID, ANONYMOUS_DEPARTMENTID, "" + ANONYMOUS_DEPARTMENTID); //匿名用户会话
	
	/**
	 * 获取会话信息
	 * @param request
	 * @param sessionInfoClass 指定会话对象类
	 * @param anonymousEnable 是否允许匿名访问
	 * @return
	 * @throws SessionException
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request, Class sessionInfoClass, boolean anonymousEnable) throws SessionException;
	
	/**
	 * 根据用户名获取会话信息
	 * @param userLoginName
	 * @return
	 * @throws SessionException
	 */
	public SessionInfo getSessionInfo(String userLoginName) throws SessionException;
	
	/**
	 * 设置当前会话的用户,用于模拟新注册用户的登录
	 * @param userLoginName
	 * @param sessionInfoClass
	 * @param request
	 * @return
	 * @throws SessionException
	 */
	public SessionInfo setSessionInfo(String userLoginName, Class sessionInfoClass, HttpServletRequest request) throws SessionException;
	
	/**
	 * 清除会话信息,在用户信息更新后调用
	 * @throws SessionException
	 */
	public void removeAllSessionInfo() throws SessionException;
	
	/**
	 * 清除指定的会话信息,在用户信息更新后调用
	 * @param userLoginName
	 * @throws SessionException
	 */
	public void removeSessionInfo(String userLoginName) throws SessionException;
	
	/**
	 * 注销
	 * @param request
	 * @throws SessionException
	 */
	public void removeSessionInfo(HttpServletRequest request) throws SessionException;
}
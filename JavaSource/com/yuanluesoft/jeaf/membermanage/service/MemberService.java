package com.yuanluesoft.jeaf.membermanage.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;

/**
 * 成员服务
 * @author yuanlue
 *
 */
public interface MemberService {
	public final static String LOGIN_EMPTY_USERNAME = "用户名不能为空";
	public final static String LOGIN_EMPTY_PASSWORD = "密码不能为空";
	public final static String LOGIN_INVALID_USERNAME_OR_PASSWORD = "用户名或密码错误";
	public final static String LOGIN_ACCOUNT_IS_HALT = "帐户未生效或已停用，请联系管理员";
	public final static String LOGIN_INVALID_POLICY = "禁止登录";
	public final static String LOGIN_FAILED = "出错，登录不能完成";
	public final static String LOGIN_TIMEOUT = "超时，请重新登录";

	/**
	 * 获取当前登录的成员信息
	 * @param loginName
	 * @param password
	 * @param passwordMatcher
	 * @param request
	 * @return
	 * @throws LoginException
	 * @throws ServiceException
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException;
	
	/**
	 * 按ID获取用户
	 * @param memberId
	 * @return
	 * @throws LoginException
	 */
	public Member getMember(long memberId) throws ServiceException;
	
	/**
	 * 根据登录用户名创建会话
	 * @param loginName
	 * @return
	 * @throws SessionException
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException;
	
	/**
	 * 检查用户是否被使用
	 * @param loginName
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException;
	
	/**
	 * 修改密码
	 * @param loginName
	 * @param oldPassword
	 * @param newPassword
	 * @param validateOldPassword
	 * @return
	 * @throws ServiceException
	 * @throws WrongPasswordException
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException;
}
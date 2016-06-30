package com.yuanluesoft.jeaf.usermanage.security.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.security.exception.PasswordOverdueException;
import com.yuanluesoft.jeaf.usermanage.security.exception.PasswordStrengthException;
import com.yuanluesoft.jeaf.usermanage.security.exception.UserHaltException;
import com.yuanluesoft.jeaf.usermanage.security.pojo.UserPasswordPolicy;

/**
 * 
 * @author chuan
 *
 */
public interface UserSecurityService extends BusinessService {
	
	/**
	 * 按组织机构ID获取密码策略
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public UserPasswordPolicy getUserPasswordPolicy(long orgId) throws ServiceException;

	/**
	 * 获取用户密码强度要求
	 * @param loginName
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public int getPasswordStrength(String loginName, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 用户密码强度校验
	 * @param loginName
	 * @param password
	 * @param request
	 * @throws PasswordStrengthException
	 * @throws PasswordOverdueException
	 */
	public void validatePasswordStrength(String loginName, String password, HttpServletRequest request) throws PasswordStrengthException;

	/**
	 * 登录审计,触发密码强度异常、密码逾期未修改异常或者用户停用异常
	 * @param loginName
	 * @param request
	 * @throws PasswordStrengthException
	 * @throws PasswordOverdueException
	 * @throws UserHaltException
	 */
	public void loginAudit(String loginName, HttpServletRequest request) throws PasswordStrengthException, PasswordOverdueException, UserHaltException;
	
	/**
	 * 密码输入错误
	 * @param loginName
	 * @param request
	 * @throws UserHaltException
	 */
	public void passwordWrong(String loginName, HttpServletRequest request) throws UserHaltException;

	/**
	 * 密码修改完成
	 * @param userId
	 * @throws ServiceException
	 */
	public void passwordChanged(long userId) throws ServiceException;
	
	/**
	 * 用户是否被停用
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isHalt(long userId) throws ServiceException;
	
	/**
	 * 设置是否停用
	 * @param userId
	 * @param halt
	 * @throws ServiceException
	 */
	public void setHalt(long userId, boolean halt) throws ServiceException;
	
	/**
	 * 发送重置密码邮件
	 * @param loginName
	 * @param mailAddress
	 * @param request
	 * @throws ServiceException
	 */
	public void sendResetMail(String loginName, String mailAddress, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 校验重置密码验证码
	 * @param loginName
	 * @param code
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public boolean validateResetCode(String loginName, String code, HttpServletRequest request) throws ServiceException;
}
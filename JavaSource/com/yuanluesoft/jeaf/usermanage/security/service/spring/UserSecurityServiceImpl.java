package com.yuanluesoft.jeaf.usermanage.security.service.spring;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.mail.service.MailService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.security.exception.PasswordOverdueException;
import com.yuanluesoft.jeaf.usermanage.security.exception.PasswordStrengthException;
import com.yuanluesoft.jeaf.usermanage.security.exception.UserHaltException;
import com.yuanluesoft.jeaf.usermanage.security.pojo.UserPasswordPolicy;
import com.yuanluesoft.jeaf.usermanage.security.pojo.UserSecurity;
import com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.PasswordUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;

/**
 * 
 * @author chuan
 *
 */
public class UserSecurityServiceImpl extends BusinessServiceImpl implements UserSecurityService {
	private SiteService siteService; //站点服务
	private SessionService sessionService; //用户会话服务
	private MailService mailService; //邮件服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#getUserPasswordPolicy(long)
	 */
	public UserPasswordPolicy getUserPasswordPolicy(long orgId) throws ServiceException {
		String hql = "from UserPasswordPolicy UserPasswordPolicy where UserPasswordPolicy.orgId=" + orgId;
		return (UserPasswordPolicy)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#getPasswordStrength(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public int getPasswordStrength(String loginName, HttpServletRequest request) throws ServiceException {
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(loginName);
		} 
		catch(SessionException e) {
			throw new ServiceException(e);
		}
		UserPasswordPolicy userPasswordPolicy = getUserPasswordPolicy(sessionInfo, request);
		return userPasswordPolicy==null ? PasswordUtils.PASSWORD_STRENGTH_LOW : (sessionInfo.isInternalUser() ? userPasswordPolicy.getInternalPasswordStrength() : userPasswordPolicy.getExternalPasswordStrength());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#validateStrength(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public void validatePasswordStrength(String loginName, String password, HttpServletRequest request) throws PasswordStrengthException {
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(loginName);
		} 
		catch(SessionException e) {
			return;
		}
		UserPasswordPolicy userPasswordPolicy = getUserPasswordPolicy(sessionInfo, request);
		if(userPasswordPolicy==null) {
			return;
		}
		int passwordStrength = sessionInfo.isInternalUser() ? userPasswordPolicy.getInternalPasswordStrength() : userPasswordPolicy.getExternalPasswordStrength();
		if(passwordStrength>PasswordUtils.PASSWORD_STRENGTH_LOW && PasswordUtils.getPasswordStrength(password)<passwordStrength) { //密码强度不够
			throw new PasswordStrengthException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#loginAudit(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public void loginAudit(String loginName, HttpServletRequest request) throws PasswordStrengthException, PasswordOverdueException, UserHaltException {
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(loginName);
		} 
		catch(SessionException e) {
			return;
		}
		//获取用户密码安全记录
		UserSecurity userSecurity = getUserSecurity(sessionInfo.getUserId(), true);
		if(userSecurity.getHalt()=='1') {
			throw new UserHaltException();
		}
		if(userSecurity.getPasswordWrong()>0) {
			userSecurity.setPasswordWrong(0);
			getDatabaseService().updateRecord(userSecurity);
		}
		//获取密码策略
		UserPasswordPolicy userPasswordPolicy = getUserPasswordPolicy(sessionInfo, request);
		if(userPasswordPolicy==null) {
			return;
		}
		if(sessionInfo.getPassword()!=null && !sessionInfo.getPassword().isEmpty()) {
			int passwordStrength = sessionInfo.isInternalUser() ? userPasswordPolicy.getInternalPasswordStrength() : userPasswordPolicy.getExternalPasswordStrength();
			if(passwordStrength>PasswordUtils.PASSWORD_STRENGTH_LOW && PasswordUtils.getPasswordStrength(sessionInfo.getPassword())<passwordStrength) { //密码强度不够
				throw new PasswordStrengthException();
			}
		}
		//检查是否超期
		int passwordDays = sessionInfo.isInternalUser() ? userPasswordPolicy.getInternalPasswordDays() : userPasswordPolicy.getExternalPasswordDays();
		if(userSecurity.getLastSetPassword()!=null && passwordDays>0 && (System.currentTimeMillis()-userSecurity.getLastSetPassword().getTime())/(24*3600*1000)>=passwordDays) { //密码有效期已过
			throw new PasswordOverdueException();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#passwordWrong(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public void passwordWrong(String loginName, HttpServletRequest request) throws UserHaltException {
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(loginName);
		} 
		catch(SessionException e) {
			throw new UserHaltException();
		}
		UserSecurity userSecurity = getUserSecurity(sessionInfo.getUserId(), true);
	    if(userSecurity.getHalt()=='1') {
	    	throw new UserHaltException();
	    }
		//获取密码策略
		UserPasswordPolicy userPasswordPolicy = getUserPasswordPolicy(sessionInfo, request);
		if(userPasswordPolicy.getPasswordWrong()<=0) {
			return;
		}
		userSecurity.setPasswordWrong(userSecurity.getPasswordWrong() + 1);
	    if(userSecurity.getPasswordWrong()>=userPasswordPolicy.getPasswordWrong()) {
	    	userSecurity.setPasswordWrong(0);
	    	userSecurity.setHalt('1'); //停用
	    }
	    getDatabaseService().updateRecord(userSecurity);
	    if(userSecurity.getHalt()=='1') {
	    	throw new UserHaltException();
	    }
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#passwordChanged(long)
	 */
	public void passwordChanged(long userId) throws ServiceException {
		UserSecurity userSecurity = getUserSecurity(userId, true);
		userSecurity.setLastSetPassword(DateTimeUtils.now());
		userSecurity.setHalt('0');
		userSecurity.setResetCode(null);
		userSecurity.setResetCodeCreated(null);
		getDatabaseService().updateRecord(userSecurity);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#isHalt(long)
	 */
	public boolean isHalt(long userId) throws ServiceException {
		UserSecurity userSecurity = getUserSecurity(userId, false);
		return userSecurity!=null && userSecurity.getHalt()=='1';
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#setHalt(long, boolean)
	 */
	public void setHalt(long userId, boolean halt) throws ServiceException {
		UserSecurity userSecurity = getUserSecurity(userId, true);
		userSecurity.setHalt(halt ? '1' : '0');
		update(userSecurity);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#sendResetMail(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public void sendResetMail(String loginName, String mailAddress, HttpServletRequest request) throws ServiceException {
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(loginName);
		} 
		catch(SessionException e) {
			throw new ServiceException(e);
		}
		//生成重置密码验证码
		String code = generateResetCode(sessionInfo.getUserId());
		//发送邮件
		UserPasswordPolicy userPasswordPolicy = getUserPasswordPolicy(sessionInfo, request);
		String mailSubject, mailContent;
		if(userPasswordPolicy!=null && userPasswordPolicy.getResetPasswordMailSubject()!=null && !userPasswordPolicy.getResetPasswordMailSubject().isEmpty()) {
			mailSubject = userPasswordPolicy.getResetPasswordMailSubject();
		}
		else {
			mailSubject = "密码重置";
		}
		String time = userPasswordPolicy==null || userPasswordPolicy.getResetPasswordCodeExpire()<=0 ? "一直有效" : StringUtils.getTime(userPasswordPolicy.getResetPasswordCodeExpire() * 60 * 1000);
		if(userPasswordPolicy!=null && userPasswordPolicy.getResetPasswordMailContent()!=null && !userPasswordPolicy.getResetPasswordMailContent().isEmpty()) {
			mailContent = userPasswordPolicy.getResetPasswordMailContent().replaceAll("<用户名>", sessionInfo.getUserName())
																		  .replaceAll("<有效期>", time)
																		  .replaceAll("<验证码>", code);
		}
		else {
			mailContent = "尊敬的" + sessionInfo.getUserName() + "，您好！\r\r" +
						  "您的验证码为：" + code + "，有效期：" + time + "\r\n" +
						  "请拷贝上面的验证码，用来重新设置您的账户密码";
		}
		mailService.sendMail(0, null, getUnitId(sessionInfo, request), mailAddress, mailSubject, StringUtils.generateHtmlContent(mailContent), false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService#validateResetCode(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public boolean validateResetCode(String loginName, String code, HttpServletRequest request) throws ServiceException {
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(loginName);
		} 
		catch(SessionException e) {
			throw new ServiceException(e);
		}
		UserSecurity userSecurity = getUserSecurity(sessionInfo.getUserId(), false);
		System.out.println("*************userSecurity:" + userSecurity + ","  + userSecurity.getResetCode() + "," + code);
		if(userSecurity==null || !code.equalsIgnoreCase(userSecurity.getResetCode())) {
			return false;
		}
		//检查是否超时
		UserPasswordPolicy userPasswordPolicy = getUserPasswordPolicy(sessionInfo, request);
		System.out.println("*************userPasswordPolicy:" + userPasswordPolicy + ","  + userPasswordPolicy.getResetPasswordCodeExpire());
		if(userPasswordPolicy==null || userPasswordPolicy.getResetPasswordCodeExpire()<=0) {
			return true;
		}
		System.out.println("*********" + DateTimeUtils.add(userSecurity.getResetCodeCreated(), Calendar.MINUTE, userPasswordPolicy.getResetPasswordCodeExpire()));
		return DateTimeUtils.add(userSecurity.getResetCodeCreated(), Calendar.MINUTE, userPasswordPolicy.getResetPasswordCodeExpire()).after(DateTimeUtils.now());
	}
	
	/**
	 * 生成重置密码验证码
	 * @param userId
	 * @return
	 */
	private String generateResetCode(long userId) {
		UserSecurity userSecurity = getUserSecurity(userId, true);
		userSecurity.setResetCode(UUIDStringGenerator.generateId().substring(0, 6));
		userSecurity.setResetCodeCreated(DateTimeUtils.now());
		getDatabaseService().updateRecord(userSecurity);
		return userSecurity.getResetCode();
	}
	
	/**
	 * 获取单位ID
	 * @param sessionInfo
	 * @param request
	 * @return
	 */
	private long getUnitId(SessionInfo sessionInfo, HttpServletRequest request) {
		long unitId = sessionInfo.getUnitId();
		if(!sessionInfo.isInternalUser()) { //不是内部用户,获取站点隶属单位
			WebSite site = null;
			try {
				site = siteService.getParentSite(RequestUtils.getParameterLongValue(request, "siteId"));
			} 
			catch (ServiceException e) {
			
			}
			unitId = site==null ? 0 : site.getOwnerUnitId();
		}
		return unitId;
	}

	/**
	 * 获取用户密码策略
	 * @param loginName
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private UserPasswordPolicy getUserPasswordPolicy(SessionInfo sessionInfo, HttpServletRequest request) {
		String hql = "select UserPasswordPolicy" +
					 " from UserPasswordPolicy UserPasswordPolicy, OrgSubjection OrgSubjection" +
					 " where UserPasswordPolicy.orgId=OrgSubjection.parentDirectoryId" +
					 " and OrgSubjection.directoryId=" + getUnitId(sessionInfo, request) +
					 " order by OrgSubjection.id";
		return (UserPasswordPolicy)getDatabaseService().findRecordByHql(hql);
	}
	
	/**
	 * 获取用户密码安全记录
	 * @param userId
	 * @param createWhenNotExists
	 * @return
	 */
	private UserSecurity getUserSecurity(long userId, boolean createIfNotExists) {
		String hql = "from UserSecurity UserSecurity where UserSecurity.userId=" + userId;
		UserSecurity userSecurity = (UserSecurity)getDatabaseService().findRecordByHql(hql);
		if(userSecurity!=null || !createIfNotExists) {
			return userSecurity;
		}
		userSecurity = new UserSecurity();
		userSecurity.setId(UUIDLongGenerator.generateId()); //ID
		userSecurity.setUserId(userId); //用户ID
		userSecurity.setLastSetPassword(DateTimeUtils.now()); //密码修改时间,上次设置密码的时间
		getDatabaseService().saveRecord(userSecurity);
		return userSecurity;
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * @return the mailService
	 */
	public MailService getMailService() {
		return mailService;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
package com.yuanluesoft.logistics.usermanage.service.spring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.logistics.usermanage.model.LogisticsSessionInfo;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsBlacklist;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsUser;
import com.yuanluesoft.logistics.usermanage.service.LogisticsUserService;

/**
 * 
 * @author linchuan
 *
 */
public class LogisticsUserServiceImpl extends BusinessServiceImpl implements LogisticsUserService {
	private MemberServiceList memberServiceList;
	private CryptService cryptService; //加/解密服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		LogisticsUser user = (LogisticsUser)super.load(recordClass, id);
		if(user!=null) {
			if(user.getPassword()==null || user.getPassword().equals("")) {
				user.setPassword(encryptPassword(user.getId(), user.getLoginName()));
	    	}
			user.setPassword("{" + user.getPassword() + "}");
		}
		return user;
	}


	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		LogisticsUser user = (LogisticsUser)record;
		//用户名不区分大小写
		user.setLoginName(user.getLoginName().toLowerCase()); //转换为小写
		user.setPassword(encryptPassword(user.getId(), user.getPassword()));
		super.save(record);
		user.setPassword("{" + user.getPassword() + "}");
		return user;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		LogisticsUser user = (LogisticsUser)record;
		//用户名不区分大小写
		user.setLoginName(user.getLoginName().toLowerCase()); //转换为小写
		user.setPassword(encryptPassword(user.getId(), user.getPassword()));
		super.update(record);
		user.setPassword("{" + user.getPassword() + "}");
		return user;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(java.lang.Object)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		List errors = super.validateBusiness(record, isNew);
		LogisticsUser user = (LogisticsUser)record;
		if(memberServiceList.isLoginNameInUse(user.getLoginName(), user.getId())) {
			if(errors==null) {
				errors = new ArrayList();
			}
			errors.add("登录用户名已经被使用");
		}
		return errors;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		List errors = super.validateDataIntegrity(record, isNew);
		//检查名称是否重复
		LogisticsUser user = (LogisticsUser)record;
		String hql = "select LogisticsUser.id from LogisticsUser LogisticsUser where LogisticsUser.name='" + JdbcUtils.resetQuot(user.getName()) + "' and LogisticsUser.id!=" + user.getId();
		if(getDatabaseService().findRecordByHql(hql)!=null) {
			if(errors==null) {
				errors = new ArrayList();
			}
			errors.add("“" + user.getName() + "”已经被注册过");
		}
		return errors;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.usermanage.service.LogisticsUserService#addToBlacklist(long, java.lang.String)
	 */
	public void addToBlacklist(long userId, String reason) throws ServiceException {
		//获取用户记录
		LogisticsUser user = (LogisticsUser)load(LogisticsUser.class, userId);
		//检查是否已经在黑名单中
		if(inBlacklist(user)) {
			return;
		}
		LogisticsBlacklist blacklist = new LogisticsBlacklist();
		blacklist.setId(UUIDLongGenerator.generateId()); //ID
		blacklist.setUserId(userId); //用户ID
		blacklist.setUserName(user.getName()); //公司名称/个人姓名
		blacklist.setBlacklistBegin(DateTimeUtils.now()); //列入黑名单时间
		blacklist.setBlacklistEnd(null); //黑名单解禁时间
		blacklist.setReason(reason); //列入原因
		blacklist.setRemark(null); //备注
		getDatabaseService().saveRecord(blacklist);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.usermanage.service.LogisticsUserService#removeFromBlacklist(long)
	 */
	public void removeFromBlacklist(long userId) throws ServiceException {
		LogisticsUser user = (LogisticsUser)load(LogisticsUser.class, userId);
		if(inBlacklist(user)) {
			LogisticsBlacklist blacklist = (LogisticsBlacklist)user.getBlacklists().iterator().next();
			blacklist.setBlacklistEnd(DateTimeUtils.now()); //黑名单解禁时间
			getDatabaseService().updateRecord(blacklist);
		}
	}

	/**
	 * 口令加密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String encryptPassword(long userId, String password) throws ServiceException {
		if(password.startsWith("{") && password.endsWith("}")) { //口令解密
			return password.substring(1, password.length() - 1);
		}
	    return cryptService.encrypt(password, "" + userId, false);
	}

	/**
	 * 口令解密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String decryptPassword(long userId, String password) throws ServiceException {
		return cryptService.decrypt(password, "" + userId, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		LogisticsUser user = (LogisticsUser)getDatabaseService().findRecordByHql("from LogisticsUser LogisticsUser where LogisticsUser.loginName='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'"); //按登录用户名查找用户
		if(user==null) {
			return false;
		}
		if(validateOldPassword) {
			if(user.getPassword()==null || user.getPassword().equals("")) {
			    if(!oldPassword.equals(user.getLoginName())) {
			        throw(new WrongPasswordException()); //密码错误
			    }
			}
			else if(!encryptPassword(user.getId(), oldPassword).equals(user.getPassword())) {
				throw(new WrongPasswordException()); //密码错误
			}
		}
		//设置新密码
		user.setPassword(encryptPassword(user.getId(), newPassword));
		getDatabaseService().updateRecord(user);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		try {
			LogisticsUser user = (LogisticsUser)getDatabaseService().findRecordByHql("from LogisticsUser LogisticsUser where LogisticsUser.loginName='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'"); //按登录用户名查找用户
			if(user==null) { //不是企业用户
				return null;
			}
			LogisticsSessionInfo sessionInfo = new LogisticsSessionInfo();
			//设置用户信息
			sessionInfo.setLoginName(user.getLoginName());
			sessionInfo.setUserType(PersonService.PERSON_TYPE_OTHER);
			String password = user.getPassword();
			if(password==null || password.equals("")) {
				password = user.getLoginName();
			}
			else {//口令解密
				password = decryptPassword(user.getId(), password);
			}
			sessionInfo.setPassword(password);
			sessionInfo.setUserName(user.getName());
			sessionInfo.setUserId(user.getId());
			String orgIds = "0," + user.getId();
			sessionInfo.setDepartmentId(user.getId());
			sessionInfo.setDepartmentName(user.getName());
			//设置部门信息
			sessionInfo.setDepartmentIds(orgIds);
			return sessionInfo;
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new SessionException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		//检查用户表
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		Number id = (Number)getDatabaseService().findRecordByHql("select LogisticsUser.id from LogisticsUser LogisticsUser where LogisticsUser.loginName='" + JdbcUtils.resetQuot(loginName) + "'");
		if(id!=null) {
			return (id.longValue()!=personId);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		if(loginName==null || loginName.equals("") || password==null || password.equals("")) {
    		return null;
    	}
		loginName = loginName.toLowerCase();
    	//用户校验
		String hql = "from LogisticsUser LogisticsUser" +
					 " where LogisticsUser.loginName='" + JdbcUtils.resetQuot(loginName) + "'" +
					 " and LogisticsUser.isApproval='0'";
    	LogisticsUser user = (LogisticsUser)getDatabaseService().findRecordByHql(hql, ListUtils.generateList("blacklists", ",")); //按登录用户名查找用户
    	if(user==null) {
    		return null;
    	}
    	if(user.getIsDeleted()=='1' || user.getIsHalt()=='1') {
    		throw new LoginException(MemberService.LOGIN_ACCOUNT_IS_HALT); //帐号停用
    	}
    	//检查是否列入黑名单
    	if(inBlacklist(user)) {
    		throw new LoginException(MemberService.LOGIN_ACCOUNT_IS_HALT); //帐号停用
    	}
		//密码解密
    	String correctPassword = loginName; //正确的密码
	    if(user.getPassword()!=null && !user.getPassword().equals("")) { //注:密码为空时,要求用户输入的密码必须和用户名相同
	    	correctPassword =  cryptService.decrypt(user.getPassword(), "" + user.getId(), false);
	    }
    	return new MemberLogin(user.getLoginName(), user.getId(), PersonService.PERSON_TYPE_EMPLOYEE, correctPassword, !passwordMatcher.matching(correctPassword, password));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		LogisticsUser logisticsUser = (LogisticsUser)load(LogisticsUser.class, memberId);
		if(logisticsUser==null) {
			return null;
		}
		Member member = new Member();
		try {
			PropertyUtils.copyProperties(member, logisticsUser);
		}
		catch(Exception e) {
			
		}
		member.setMemberType(PersonService.PERSON_TYPE_EMPLOYEE);
		member.setOriginalRecord(logisticsUser);
		return member;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.logistics.usermanage.service.LogisticsUserService#inBlacklist(com.yuanluesoft.logistics.usermanage.pojo.LogisticsUser)
	 */
	public boolean inBlacklist(LogisticsUser user) {
		if(user.getBlacklists()!=null && !user.getBlacklists().isEmpty()) {
    		LogisticsBlacklist blacklist = (LogisticsBlacklist)user.getBlacklists().iterator().next();
    		return blacklist.getBlacklistEnd()==null || blacklist.getBlacklistEnd().after(DateTimeUtils.now());
    	}
		return false;
	}

	/**
	 * @return the memberServiceList
	 */
	public MemberServiceList getMemberServiceList() {
		return memberServiceList;
	}

	/**
	 * @param memberServiceList the memberServiceList to set
	 */
	public void setMemberServiceList(MemberServiceList memberServiceList) {
		this.memberServiceList = memberServiceList;
	}


	/**
	 * @return the cryptService
	 */
	public CryptService getCryptService() {
		return cryptService;
	}


	/**
	 * @param cryptService the cryptService to set
	 */
	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}
}
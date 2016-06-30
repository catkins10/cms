package com.yuanluesoft.jeaf.usermanage.member.service.spring;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.security.service.exception.SecurityException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.soap.SoapService;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.usermanage.member.pojo.Member;
import com.yuanluesoft.jeaf.usermanage.member.service.MemberService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class MemberServiceImpl extends BusinessServiceImpl implements MemberService, SoapService {
	private CryptService cryptService; //加密服务
	private SoapPassport serviceSoapPassport;
	private ExchangeClient exchangeClient; //数据交换服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Member member = (Member)super.load(recordClass, id);
		if(member==null) {
			return null;
		}
		String password = member.getPassword();
		if(password!=null && !password.equals("")) {
			member.setPassword("{" + password + "}");
		}
		return member;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		Member member = (Member)record;
		//用户名不区分大小写
		member.setLoginName(member.getLoginName().toLowerCase()); //转换为小写
		member.setRegistTime(DateTimeUtils.now());
		//加密口令
		member.setPassword(encryptPassword(member.getId(), member.getPassword()));
		if(exchangeClient!=null) {
			exchangeClient.synchUpdate(record, null, 2000);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		Member member = (Member)record;
		//用户名不区分大小写
		member.setLoginName(member.getLoginName().toLowerCase()); //转换为小写
		//加密口令
		member.setPassword(encryptPassword(member.getId(), member.getPassword()));
		if(exchangeClient!=null) {
			exchangeClient.synchUpdate(record, null, 2000);
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(exchangeClient!=null) {
			exchangeClient.synchDelete(record, null, 2000);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.usermanage.service.MemberService#getUserByLoginName(java.lang.String)
	 */
	public Member getMemberByLoginName(String loginName) throws ServiceException {
		String hql = "from Member Member where Member.loginName='" + JdbcUtils.resetQuot(loginName.toLowerCase()) + "'";
		return (Member)getDatabaseService().findRecordByHql(hql);
	}

	/**
	 * 口令加密
	 * @param userId
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	private String encryptPassword(long userId, String password) throws ServiceException {
		if(password.startsWith("{") && password.endsWith("}")) {
			return password.substring(1, password.length() - 1);
		}
		return cryptService.encrypt(password, "" + userId, false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.PersonService#decryptPassword(long, java.lang.String)
	 */
	public String decryptPassword(long userId, String password) throws ServiceException {
		return cryptService.decrypt(password, "" + userId, false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.member.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		//检查论坛用户表
		Number id = (Number)getDatabaseService().findRecordByHql("select Member.id from Member Member where Member.loginName='" + JdbcUtils.resetQuot(loginName) + "'");
		if(id!=null) {
			return (id.longValue()!=personId);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.usermanage.service.MemberService#getLastUser()
	 */
	public Member getLastMember() throws ServiceException {
		return (Member)getDatabaseService().findRecordByHql("from Member Member order by Member.registTime DESC");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.usermanage.service.MemberService#totalUser()
	 */
	public int totalMember() throws ServiceException {
		Object total = getDatabaseService().findRecordByHql("select count(Member) from Member Member");
		return (total==null ? 0 : ((Number)total).intValue());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.member.service.MemberService#getMember(long)
	 */
	public Member getMemberById(long memberId) throws ServiceException {
		return (Member)load(Member.class, memberId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		Member member = getMemberByLoginName(loginName);
		if(member==null) {
			return false;
		}
		if(validateOldPassword) {
			if(member.getPassword()==null || member.getPassword().equals("")) {
				if(!oldPassword.equals(member.getLoginName())) {
					throw(new WrongPasswordException()); //密码错误
				}
			}
			else if(!(encryptPassword(member.getId(), oldPassword)).equals(member.getPassword())) {
				throw(new WrongPasswordException()); //密码错误
			}
		}
		member.setPassword(newPassword);
		update(member);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		try {
			Member member = getMemberByLoginName(loginName);
    		if(member==null) {
   				return null;
    		}
			SessionInfo sessionInfo = new SessionInfo();
			//设置用户信息
			sessionInfo.setLoginName(member.getLoginName());
			sessionInfo.setUserType(PersonService.PERSON_TYPE_OTHER + 1);
			String password = member.getPassword();
			if(password==null || password.equals("")) {
				password = member.getLoginName();
			}
			else {
				//口令解密
				try {
					password = cryptService.decrypt(password, "" + member.getId(), false);
				}
				catch (SecurityException e) {
					Logger.exception(e);
					throw new SessionException(e.getMessage());
				}
			}
			sessionInfo.setPassword(password);
			sessionInfo.setUserName(member.getName());
			sessionInfo.setUserId(member.getId());
			sessionInfo.setDepartmentId(0);
			sessionInfo.setDepartmentIds("0");
			return sessionInfo;
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new SessionException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		if(loginName==null || loginName.equals("") || password==null || password.equals("")) {
    		return null;
    	}
		loginName = loginName.toLowerCase();
    	Member member = null;
    	//用户校验
    	try {
    		member = getMemberByLoginName(loginName);
    		if(member==null) {
   				return null;
    		}
    	}
    	catch(ServiceException e) {
    		throw new LoginException();
    	}
    	if(member.getHalt()=='1') {
    		throw(new LoginException(MemberService.LOGIN_ACCOUNT_IS_HALT)); //帐号停用
    	}
		String userPassword = member.getLoginName();
		if(member.getPassword()!=null && !member.getPassword().equals("")) {
			userPassword = decryptPassword(member.getId(), member.getPassword());
		}
		return new MemberLogin(member.getLoginName(), member.getId(), PersonService.PERSON_TYPE_OTHER, userPassword, !passwordMatcher.matching(userPassword, password));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public com.yuanluesoft.jeaf.membermanage.model.Member getMember(long memberId) throws ServiceException {
		Member member = getMemberById(memberId);
		if(member==null) {
			return null;
		}
		com.yuanluesoft.jeaf.membermanage.model.Member memberModel = new com.yuanluesoft.jeaf.membermanage.model.Member();
		try {
			PropertyUtils.copyProperties(memberModel, member);
		}
		catch(Exception e) {
			
		}
		memberModel.setMobile(member.getCell()); //手机
		memberModel.setTel(member.getTelephone()); //电话
		memberModel.setMailAddress(member.getEmail()); //邮箱
		memberModel.setProfileURL("/jeaf/usermanage/member/profile.shtml"); //修改个人资料的链接
		memberModel.setMemberType(PersonService.PERSON_TYPE_OTHER);
		memberModel.setOriginalRecord(member); //原始记录
		return memberModel;
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

	/**
	 * @return the serviceSoapPassport
	 */
	public SoapPassport getServiceSoapPassport() {
		return serviceSoapPassport;
	}

	/**
	 * @param serviceSoapPassport the serviceSoapPassport to set
	 */
	public void setServiceSoapPassport(SoapPassport serviceSoapPassport) {
		this.serviceSoapPassport = serviceSoapPassport;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}
}
package com.yuanluesoft.bbs.usermanage.service.spring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.bbs.usermanage.model.BbsSessionInfo;
import com.yuanluesoft.bbs.usermanage.pojo.BbsUser;
import com.yuanluesoft.bbs.usermanage.service.BbsUserService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.stat.pojo.LoginStat;
import com.yuanluesoft.jeaf.stat.service.StatService;
import com.yuanluesoft.jeaf.usermanage.member.pojo.Member;
import com.yuanluesoft.jeaf.usermanage.member.service.MemberService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class BbsUserServiceImpl extends BusinessServiceImpl implements BbsUserService, com.yuanluesoft.jeaf.membermanage.service.MemberService {
	private OrgService orgService; //组织机构服务
	private MemberService memberService; //网上注册用户服务
	private PersonService personService; //系统用户服务
	private StatService statService; //统计服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.usermanage.service.BbsUserService#getBbsUserModel(long)
	 */
	public com.yuanluesoft.bbs.usermanage.model.BbsUser getBbsUserModel(long personId) throws ServiceException {
		com.yuanluesoft.bbs.usermanage.model.BbsUser bbsUserModel = new com.yuanluesoft.bbs.usermanage.model.BbsUser();
		BbsUser bbsUser = getBbsUser(personId, true);
		try {
			PropertyUtils.copyProperties(bbsUserModel, bbsUser);
		} 
		catch (Exception e) {

		}
		//设置用户注册时间
		if(bbsUser.getType()==USER_TYPE_SYSTEM) { //系统用户
			Person person = personService.getPerson(personId);
			bbsUserModel.setCreated(person.getCreated());
			bbsUserModel.setEmail(person.getMailAddress()); //邮箱
			bbsUserModel.setSex(person.getSex());
			bbsUserModel.setHideDetail('1');
		}
		else if(bbsUser.getType()==USER_TYPE_MEMBER) { //网上注册用户
			Member member = memberService.getMemberById(personId);
			if(member!=null) {
				bbsUserModel.setCreated(member.getRegistTime());
				bbsUserModel.setArea(member.getArea()); //所属省份
				bbsUserModel.setCompany(member.getCompany()); //工作单位/所在院校
				bbsUserModel.setEmail(member.getEmail()); //邮箱
				bbsUserModel.setSex(member.getSex());
				bbsUserModel.setHideDetail(member.getHideDetail());
			}
		}
		//设置最后登录时间
		LoginStat loginStat = statService.getLoginStat(personId);
		if(loginStat!=null) {
			bbsUserModel.setLastLoginTime(loginStat.getLastLogin());
		}
		return bbsUserModel;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.usermanage.service.BbsUserService#getBbsUser(long)
	 */
	public BbsUser getBbsUser(long personId) throws ServiceException {
		return getBbsUser(personId, true);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		List errors = null;
		BbsUser bbsUser = (BbsUser)record;
		//检查昵称是否重复
		String hql = "select BbsUser.id from BbsUser BbsUser where BbsUser.nickname='" + JdbcUtils.resetQuot(bbsUser.getNickname()) + "' and BbsUser.id!=" + bbsUser.getId();
		if(getDatabaseService().findRecordByHql(hql)!=null) {
			errors = new ArrayList();
			errors.add("昵称“" + bbsUser.getNickname() + "”已经被占用");
		}
		return errors;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.usermanage.service.BbsUserService#increasePost(long, int)
	 */
	public void increasePost(long personId, int count) throws ServiceException {
		if(personId>0) {
			BbsUser bbsUser = getBbsUser(personId, true);
			bbsUser.setPostCount(bbsUser.getPostCount() + count);
			getDatabaseService().updateRecord(bbsUser);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.usermanage.service.BbsUserService#increaseReply(long, int)
	 */
	public synchronized void increaseReply(long personId, int count) throws ServiceException {
		if(personId>0) {
			BbsUser bbsUser = getBbsUser(personId, true);
			bbsUser.setReplyCount(bbsUser.getReplyCount() + count);
			getDatabaseService().updateRecord(bbsUser);
		}
	}
	
	/**
	 * 获取用户
	 * @param personId
	 * @param createIfNotExists 当用户不存在时创建用户
	 * @return
	 * @throws ServiceException
	 */
	private BbsUser getBbsUser(long personId, boolean createIfNotExists) throws ServiceException {
		BbsUser bbsUser = (BbsUser)getDatabaseService().findRecordById(BbsUser.class.getName(), personId);
		if(createIfNotExists && bbsUser==null) {
			bbsUser = new BbsUser();
			bbsUser.setId(personId);
			Person person = personService.getPerson(personId);
			if(person!=null) {
				bbsUser.setNickname(person.getLoginName());
				bbsUser.setType(USER_TYPE_SYSTEM);
			}
			else {
				Member member = memberService.getMemberById(personId);
				if(member!=null) {
					bbsUser.setNickname(member.getLoginName());
				}
				bbsUser.setType(USER_TYPE_MEMBER);
			}
			getDatabaseService().saveRecord(bbsUser);
		}
		return bbsUser;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		boolean systemUser = false; //是否系统用户
		//调用网上用户注册服务获取会话
		SessionInfo sessionInfo = memberService.createSessionInfo(loginName);
		if(sessionInfo==null) { //不是网上注册用户
			sessionInfo = personService.createSessionInfo(loginName); //调用系统用户服务获取会话
			if(sessionInfo==null) { //不是系统用户
				return null;
			}
			systemUser = true;
		}
		BbsSessionInfo bbsSessionInfo = new BbsSessionInfo();
		try {
			PropertyUtils.copyProperties(bbsSessionInfo, sessionInfo);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new SessionException(SessionException.SESSION_FAILED);
		}
		bbsSessionInfo.setSystemUser(systemUser); //是否系统用户
		//获取昵称
		if(sessionInfo.getLoginName().equals(SessionService.ANONYMOUS)) {
			bbsSessionInfo.setNickname("游客");
		}
		else {
			BbsUser bbsUser;
			try {
				bbsUser = getBbsUser(sessionInfo.getUserId());
			}
			catch (ServiceException e) {
				Logger.exception(e);
				throw new SessionException(SessionException.SESSION_FAILED);
			}
			//昵称
			bbsSessionInfo.setNickname(bbsUser==null || bbsUser.getNickname()==null ? sessionInfo.getLoginName() : bbsUser.getNickname());
			//是否VIP
			bbsSessionInfo.setVip(bbsUser==null ? '0' : bbsUser.getVip());
		}
		return bbsSessionInfo;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public com.yuanluesoft.jeaf.membermanage.model.Member getMember(long memberId) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLoginNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		return false;
	}
	/**
	 * @return the memberService
	 */
	public MemberService getMemberService() {
		return memberService;
	}

	/**
	 * @param memberService the memberService to set
	 */
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @return the statService
	 */
	public StatService getStatService() {
		return statService;
	}

	/**
	 * @param statService the statService to set
	 */
	public void setStatService(StatService statService) {
		this.statService = statService;
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}

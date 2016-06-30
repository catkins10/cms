package com.yuanluesoft.jeaf.membermanage.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author yuanlue
 *
 */
public class MemberServiceList implements MemberService {
	private String memberServiceNames; //成员服务名称列表
	private OrgService orgService; //组织机构服务
	
	//私有属性
	private List memberServices = null; //成员服务列表

	/**
	 * 创建会话
	 * @param loginName
	 * @param sessionInfoClass
	 * @return
	 * @throws SessionException
	 */
	public SessionInfo createSessionInfo(String loginName, Class sessionInfoClass) throws SessionException {
		for(Iterator iterator=getMemberServices().iterator(); iterator.hasNext();) {
			MemberService memberService = (MemberService)iterator.next();
			SessionInfo sessionInfo = memberService.createSessionInfo(loginName);
			if(sessionInfo!=null && (sessionInfoClass==null || sessionInfoClass.isInstance(sessionInfo))) {
				return sessionInfo;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#createSessionInfo(java.lang.String)
	 */
	public SessionInfo createSessionInfo(String loginName) throws SessionException {
		return createSessionInfo(loginName, null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#isLongNameInUse(java.lang.String, long)
	 */
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		for(Iterator iterator=getMemberServices().iterator(); iterator.hasNext();) {
			MemberService memberService = (MemberService)iterator.next();
			if(memberService.isLoginNameInUse(loginName, personId)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#login(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sso.service.passwordcomparator.PasswordMatcher, javax.servlet.http.HttpServletRequest)
	 */
	public MemberLogin login(String loginName, String password, Matcher passwordMatcher, HttpServletRequest request) throws LoginException, ServiceException {
		for(Iterator iterator=getMemberServices().iterator(); iterator.hasNext();) {
			MemberService memberService = (MemberService)iterator.next();
			MemberLogin memberLogin = memberService.login(loginName, password, passwordMatcher, request);
			if(memberLogin!=null) {
				return memberLogin;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#changePassword(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public boolean changePassword(String loginName, String oldPassword, String newPassword, boolean validateOldPassword) throws ServiceException, WrongPasswordException {
		for(Iterator iterator=getMemberServices().iterator(); iterator.hasNext();) {
			MemberService memberService = (MemberService)iterator.next();
			if(memberService.changePassword(loginName, oldPassword, newPassword, validateOldPassword)) { //修改密码
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.membermanage.service.MemberService#getMember(long)
	 */
	public Member getMember(long memberId) throws ServiceException {
		for(Iterator iterator=memberServices.iterator(); iterator.hasNext();) {
			MemberService memberService = (MemberService)iterator.next();
			Member member = memberService.getMember(memberId);
			if(member!=null) {
				return member;
			}
		}
		return null;
	}
	
	/**
	 * 获取成员服务列表
	 * @return
	 */
	private List getMemberServices() {
		if(memberServices!=null) {
			return memberServices;
		}
		memberServices = new ArrayList();
		String[] serviceNames = memberServiceNames.split(",");
		for(int i=0; i<serviceNames.length; i++) {
			try {
				memberServices.add(Environment.getService(serviceNames[i].trim()));
			}
			catch(Exception e) {
				
			}	
		}
		return memberServices;
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
	 * @return the memberServiceNames
	 */
	public String getMemberServiceNames() {
		return memberServiceNames;
	}

	/**
	 * @param memberServiceNames the memberServiceNames to set
	 */
	public void setMemberServiceNames(String memberServiceNames) {
		this.memberServiceNames = memberServiceNames;
	}
}
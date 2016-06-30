/*
 * Created on 2006-3-10
 *
 */
package com.yuanluesoft.jeaf.sessionmanage.service.spring;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.sso.service.exception.SsoSessionException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SessionServiceImpl implements SessionService {
	private MemberServiceList memberServiceList; //成员服务列表
	private Cache cache; //缓存
	
    private SsoSessionService ssoSessionService; //SSO会话服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sessionmanage.service.SessionService#getSessionInfo(javax.servlet.http.HttpServletRequest, java.lang.Class, boolean)
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request, Class sessionInfoClass, boolean anonymousEnable) throws SessionException {
		try {
			boolean anonymous = false;
			String ticket = request.getParameter("ticket");
			SessionInfo sessionInfo;
			//用ticket获取会话
			if(ticket!=null && !"".equals(ticket)) {
				if(ANONYMOUS.equals(ticket)) { //匿名用户
					anonymous = true;
				}
				else if((sessionInfo = getSessionInfoByTicket(ticket, sessionInfoClass, request))!=null) {
					return sessionInfo;
				}
			}
			sessionInfo = RequestUtils.getSessionInfo(request);
			if(sessionInfo==null) { //会话为空
				if(!anonymous || !anonymousEnable) { //不是匿名用户或者不允许匿名访问
					throw new SessionException(SessionException.SESSION_NONE);
				}
				//是匿名用户且允许匿名访问
				sessionInfo = loadSessionInfo(ANONYMOUS, sessionInfoClass);
				request.getSession().setAttribute("SessionInfo", sessionInfo);
				return sessionInfo;
			}
			else if(ANONYMOUS.equals(sessionInfo.getLoginName())) { //当前是匿名用户
				if(!anonymousEnable) { //不允许匿名访问
					throw new SessionException(SessionException.SESSION_NONE);
				}
				//允许匿名访问
				if(sessionInfoClass.isInstance(sessionInfo)) { //类型匹配
					return sessionInfo;
				}
				sessionInfo = loadSessionInfo(ANONYMOUS, sessionInfoClass);
				request.getSession().setAttribute("SessionInfo", sessionInfo);
				return sessionInfo;
			}
			else {
				//检查会话是否有效
				if(!ssoSessionService.verifySession(sessionInfo.getLoginName(), ((Long)request.getSession().getAttribute("LoginSequence")).longValue())) {
					//会话失效,删除属性
					request.getSession().removeAttribute("SessionInfo");
					throw new SessionException(SessionException.SESSION_TIMEOUT);
				}
				if(sessionInfoClass.isInstance(sessionInfo)) { //类型匹配
					return sessionInfo;
				}
				sessionInfo = loadSessionInfo(sessionInfo.getLoginName(), sessionInfoClass);
				request.getSession().setAttribute("SessionInfo", sessionInfo);
				return sessionInfo;
			}
		}
		catch (SessionException se) {
			throw se;
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new SessionException(SessionException.SESSION_FAILED);
		}
	}
	
	/**
	 * 按TICKET创建会话
	 * @param ticket
	 * @param sessionInfoClass
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @throws SsoSessionException
	 * @throws SessionException
	 */
	private SessionInfo getSessionInfoByTicket(String ticket, Class sessionInfoClass, HttpServletRequest request) throws ServiceException, SsoSessionException, SessionException {
		String loginResult = ssoSessionService.getLoginNameByTicket(ticket);
		if(loginResult==null || "".equals(loginResult)) {
			return null;
		}
		//创建新的用户会话信息
		String[] values = loginResult.split(",");
		SessionInfo sessionInfo = loadSessionInfo(values[0], sessionInfoClass);
		request.getSession().setAttribute("LoginSequence", new Long(values[1]));
		request.getSession().setAttribute("SessionInfo", sessionInfo);
		return sessionInfo;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sessionmanage.service.SessionService#setSessionInfo(java.lang.String, java.lang.Class, javax.servlet.http.HttpServletRequest)
	 */
	public SessionInfo setSessionInfo(String userLoginName, Class sessionInfoClass, HttpServletRequest request) throws SessionException {
		try {
			String ssoSessionId = ssoSessionService.createSsoSession(userLoginName, false);
			String ticket = ssoSessionService.createTicket(ssoSessionId);
			return getSessionInfoByTicket(ticket, sessionInfoClass, request);
		}
		catch(Exception e) {
			if(e instanceof SessionException) {
				throw (SessionException)e;
			}
			throw new SessionException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sessionmanage.service.SessionService#getSessionInfo(java.lang.String)
	 */
	public SessionInfo getSessionInfo(String userName) throws SessionException {
		return loadSessionInfo(userName, SessionInfo.class);
	}
	
	/**
	 * 创建会话信息
	 * @param loginName
	 * @param sessionInfoClass
	 * @return
	 * @throws SessionException
	 */
	private SessionInfo loadSessionInfo(String loginName, Class sessionInfoClass) throws SessionException {
		try {
			if(ANONYMOUS.equals(loginName)) {
				SessionInfo sessionInfo = (SessionInfo)sessionInfoClass.newInstance();
				sessionInfo.setLoginName(SessionService.ANONYMOUS);
				sessionInfo.setUserId(SessionService.ANONYMOUS_USERID); //匿名用户ID
				sessionInfo.setDepartmentId(SessionService.ANONYMOUS_DEPARTMENTID);
				sessionInfo.setDepartmentIds("" + SessionService.ANONYMOUS_DEPARTMENTID);
				sessionInfo.setUserName(SessionService.ANONYMOUS_USERNAME);
				return sessionInfo;
			}
			SessionInfo sessionInfo = (SessionInfo)cache.get(loginName);
			if(sessionInfo!=null && sessionInfoClass.isInstance(sessionInfo)) { //缓存中有会话,且类型匹配
				return sessionInfo;
			}
			//创建会话对象
			sessionInfo = memberServiceList.createSessionInfo(loginName, sessionInfoClass);
			if(sessionInfo==null) {
				throw new SessionException(SessionException.SESSION_NO_MATCH);
			}
			cache.put(loginName, sessionInfo);
			return sessionInfo;
		}
		catch(SessionException se) {
			throw se;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new SessionException(SessionException.SESSION_FAILED);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sessionmanage.service.SessionService#cleanAllSessionInfo()
	 */
	public void removeAllSessionInfo() throws SessionException {
		try {
			cache.clear();
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new SessionException(SessionException.SESSION_FAILED);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sessionmanage.service.SessionService#cleanSessionInfo(long)
	 */
	public void removeSessionInfo(String userLoginName) throws SessionException {
		try {
			cache.remove(userLoginName);
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new SessionException(SessionException.SESSION_FAILED);
		}
	}
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sessionmanage.service.SessionService#removeSessionInfo(javax.servlet.http.HttpServletRequest)
	 */
	public void removeSessionInfo(HttpServletRequest request) throws SessionException {
		request.getSession().removeAttribute("SessionInfo");
	}

	/**
	 * @return Returns the cache.
	 */
	public Cache getCache() {
		return cache;
	}
	/**
	 * @param cache The cache to set.
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
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
	 * @return the ssoSessionService
	 */
	public SsoSessionService getSsoSessionService() {
		return ssoSessionService;
	}

	/**
	 * @param ssoSessionService the ssoSessionService to set
	 */
	public void setSsoSessionService(SsoSessionService ssoSessionService) {
		this.ssoSessionService = ssoSessionService;
	}
}

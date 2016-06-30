package com.yuanluesoft.jeaf.sessionmanage.service.spring;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.sso.service.exception.SsoSessionException;

/**
 * 
 * @author linchuan
 *
 */
public class WapSessionServiceImpl extends SessionServiceImpl implements SessionService {
	private SsoSessionService ssoSessionService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sessionmanage.service.spring.SessionServiceImpl#getSessionInfo(javax.servlet.http.HttpServletRequest, java.lang.Class, boolean)
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request, Class sessionInfoClass, boolean anonymousEnable) throws SessionException {
		String loginName = null;
		//按URL参数wapSessionId检查会话
		String wapSessionId = request.getParameter("wapsessionid");
		if(wapSessionId!=null && !wapSessionId.equals("")) {
			try {
				loginName = ssoSessionService.getLoginNameBySessionId(wapSessionId);
			} 
			catch(SsoSessionException e) {
				
			}
		}
		if(!anonymousEnable && loginName==null) { //不允许匿名
			throw new SessionException(SessionException.SESSION_FAILED);
		}
		SessionInfo sessionInfo = getSessionInfo(loginName==null ? SessionService.ANONYMOUS : loginName);
		request.setAttribute("SessionInfo", sessionInfo); //设置为请求属性
		return sessionInfo;
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

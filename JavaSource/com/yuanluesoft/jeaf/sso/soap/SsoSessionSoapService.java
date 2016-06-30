/*
 * Created on 2006-3-10
 *
 */
package com.yuanluesoft.jeaf.sso.soap;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.soap.BaseSoapService;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.sso.service.exception.SsoSessionException;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class SsoSessionSoapService extends BaseSoapService {

	/**
	 * 根据TICKET获取登录用户名
	 * @param ticket
	 * @return
	 * @throws SoapException
	 */
	public String getLoginNameByTicket(String ticket) throws SsoSessionException, SoapException {
		return ((SsoSessionService)getSpringService("ssoSessionService")).getLoginNameByTicket(ticket);
	}
	
	/**
	 * 检查会话有效性
	 * @param personLoginName 登录用户名
	 * @param loginSequence
	 * @return
	 * @throws SoapException
	 */
	public boolean verifySession(String personLoginName, long loginSequence) throws SsoSessionException, SoapException {
		return ((SsoSessionService)getSpringService("ssoSessionService")).verifySession(personLoginName, loginSequence);
	}
	
	/**
	 * 获取登录令牌
	 * @return
	 * @throws SoapException
	 */
	public String getLoginToken() throws SoapException {
		Cache cache = (Cache)getSpringService("ticketCache"); //缓存
		String loginToken = UUIDStringGenerator.generateId();
		try {
			cache.put(loginToken, "1");
			return loginToken;
		} 
		catch(CacheException e) {
			Logger.exception(e);
			throw new SoapException();
		}
	}
	
	/**
	 * 登录服务
	 * @param userName
	 * @param password des+base64+md5加密,key是loginToken
	 * @param loginToken 登录令牌
	 * @return
	 * @throws SoapException
	 */
	public String login(String userName, String password, String loginToken) throws SsoSessionException, LoginException, SoapException {
		Cache cache = (Cache)getSpringService("ticketCache"); //缓存
		//检查令牌是否有效
		try {
			if(!"1".equals(cache.get(loginToken))) {
				return null;
			}
			cache.remove(loginToken);
		} 
		catch(CacheException e) {
			Logger.exception(e);
			return null;
		}
		//解密
		try {
			password = Encoder.getInstance().desBase64Decode(password, Encoder.getInstance().md5Base64Encode(loginToken), null);
		} 
		catch (Exception e) {
			Logger.exception(e);
			return null;
		}
		//登录
		Matcher matcher = new Matcher() {
			public boolean matching(String from, String toMatch) {
				try {
					return toMatch.equals(Encoder.getInstance().md5Encode(from));
				}
				catch (Exception e) {
					return false;
				}
			}
		};
		try {
			SsoSessionService ssoSessionService = (SsoSessionService)getSpringService("ssoSessionService");
			String loginName = ssoSessionService.login(userName, password, matcher, null).getMemberLoginName();
			return ssoSessionService.createSsoSession(loginName, false);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new SoapException();
		}
	}
	
	/**
	 * 注销
	 * @param ssoSessionId
	 * @throws SsoSessionException
	 * @throws SoapException
	 */
	public void logout(String ssoSessionId) throws SsoSessionException, SoapException {
		SsoSessionService ssoSessionService = (SsoSessionService)getSpringService("ssoSessionService");
		ssoSessionService.removeSsoSession(ssoSessionId);
	}
}
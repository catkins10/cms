package com.yuanluesoft.jeaf.sso.soap;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.util.Encoder;

/**
 * SSO会话服务客户端
 * @author linchuan
 *
 */
public class SsoSessionSoapClient {
	private SoapConnectionPool soapConnectionPool; //SOAP连接池
	private SoapPassport soapPassport; //SOAP许可证

	public SsoSessionSoapClient(SoapConnectionPool soapConnectionPool, SoapPassport soapPassport) {
		super();
		this.soapConnectionPool = soapConnectionPool;
		this.soapPassport = soapPassport;
	}

	/**
	 * 创建SSO会话
	 * @param userName
	 * @param password md5编码过的密码
	 * @return
	 * @throws ServiceException
	 */
	public String createSsoSession(String userName, String password) throws ServiceException {
		try {
			//获取登录令牌,public String getLoginToken()
			String loginToken = (String)soapConnectionPool.invokeRemoteMethod("SsoSessionService", "getLoginToken", soapPassport, null, null);
			if(Logger.isTraceEnabled()) {
				Logger.trace("UserReplicateService: get login token " + loginToken + ".");
			}
			//登录,public String login(String userName, String password, String loginToken)
			Object[] values = {userName, Encoder.getInstance().desBase64Encode(password, Encoder.getInstance().md5Base64Encode(loginToken), null), loginToken};
			return (String)soapConnectionPool.invokeRemoteMethod("SsoSessionService", "login", soapPassport, values, null);
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}
}
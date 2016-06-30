/*
 * Created on 2006-2-28
 *
 */
package com.yuanluesoft.jeaf.soap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class BaseSoapService {
	private MessageContext messageContext = null; //axis消息上下文
	
	/**
	 * 获取axis消息上下文
	 * @return
	 */
	protected MessageContext getMessageContext() {
		if(messageContext==null) {
			messageContext = MessageContext.getCurrentContext();
		}
		return messageContext;
	}
	
	/**
	 * 获取spring服务
	 * @param beanName
	 * @return
	 * @throws SoapException
	 */
	protected Object getSpringService(String serviceName) throws SoapException {
		Object service;
		try {
			service = Environment.getService(serviceName);
		}
		catch (ServiceException e) {
			throw new SoapException();
		}
		if(!(service instanceof SoapService)) {
			return service;
		}
		
    	SoapService soapService = (SoapService)service;
	    //身份验证
	    SoapPassport serviceSoapPassport = soapService.getServiceSoapPassport();
	    if(serviceSoapPassport!=null &&
	       (!serviceSoapPassport.getUserName().equals(getMessageContext().getUsername()) || !serviceSoapPassport.getPassword().equals(getMessageContext().getPassword()))) {
	     	throw new SoapException("username or password is invalid");
	    }
	    //IP地址检查
	    /*HttpServletRequest request = (HttpServletRequest)messageContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
	    String ip = request.getRemoteAddr();
	    if(!ip.equals("127.0.0.1")) { //非本机
	    	//检查IP是否在被允许的IP列表中
	    	List ips = serviceSoapPassport.getIps();
	    	if(ips==null || ips.indexOf(ip)==-1) {
	    		throw new SoapException("remote address is invalid");
	    	}
	    }*/
	    return soapService;
	}
	
	/**
	 * 获取用户会话
	 * @param ssoSessionId
	 * @return
	 */
	protected SessionInfo getSessionInfo(String ssoSessionId) {
		try {
			SsoSessionService ssoSessionService = (SsoSessionService)getSpringService("ssoSessionService");
			SessionService sessionService = (SessionService)getSpringService("sessionService");
			return sessionService.getSessionInfo(ssoSessionService.getLoginNameBySessionId(ssoSessionId));
		}
		catch(Exception e) {
			Logger.exception(e);
			return SessionService.ANONYMOUS_SESSION;
		}
	}
	
	/**
	 * 获取http请求
	 * @return
	 */
	protected HttpServletRequest getHttpServletRequest() {
		return (HttpServletRequest)getMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
	}
	
	/**
	 * 获取http应答
	 * @return
	 */
	protected HttpServletResponse getHttpServletResponse() {
		return (HttpServletResponse)getMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE);
	}
}
package com.yuanluesoft.jeaf.distribution.service.spring;

import java.io.Serializable;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.distribution.model.RemoteMethodInvoke;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class DistributionServiceImpl implements DistributionService {
	private final String MASTER_SERVER_KEY = "MasterServer";
	private final String SOAP_SERVICE_NAME = "DistributionService";

	private Cache cache; //分布式缓存,用来存放主服务器WEB服务URL
	private SoapPassport localSoapPassport; //服务端SOAP身份验证
	private SoapConnectionPool soapConnectionPool; //soap连接池
	
	private String webApplicationDistributeUrl; //分布式部署时的其他服务器访问时的URL

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.distribution.service.DistributionService#isMasterServer(boolean)
	 */
	public boolean isMasterServer(boolean verifyRemoteMasterServerAlive) throws ServiceException {
		try {
			String url = (String)cache.get(MASTER_SERVER_KEY);
			if(url==null) {
				setAsMasterServer();
				return true;
			}
			if(webApplicationDistributeUrl.equals(url)) { //当前服务器就是主服务器
				return true;
			}
			else if(!verifyRemoteMasterServerAlive) {
				return false;
			}
			else {
				try {
					if(((Boolean)invokeSoapMethodOnRemoteServer(url, SOAP_SERVICE_NAME, "keepAlive", null, null)).booleanValue()) { //检查远程主机是否处于活动状态
						return false; //远程的主服务器处于活动状态
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				//远程的主服务器已失效
				setAsMasterServer(); //把当前服务器设为主服务器
				return true;
			}
		}
		catch(Exception e) {
			
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.distribution.service.DistributionService#setAsMasterServer()
	 */
	public void setAsMasterServer() throws ServiceException {
		if(Logger.isDebugEnabled()) {
			try {
				Logger.debug("DistributionService: set local server as master server, previous master server web service url is " + cache.get(MASTER_SERVER_KEY) + ".");
			}
			catch (CacheException e) {
				
			}
		}
		try {
			cache.put(MASTER_SERVER_KEY, webApplicationDistributeUrl);
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}

	/**
	 * 执行主服务器上SOAP方法
	 * @param serviceName
	 * @param methodName
	 * @param args
	 * @param typeMappings
	 * @return
	 * @throws ServiceException
	 */
	protected Object invokeSoapMethodOnMasterServer(String serviceName, String methodName, Object[] args, Class[] typeMappings) throws ServiceException {
		try {
			return invokeSoapMethodOnRemoteServer((String)cache.get(MASTER_SERVER_KEY), serviceName, methodName, args, typeMappings);
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 运行远程服务器上的SOAP方法
	 * @param remoteServerDistributeUrl
	 * @param serviceName
	 * @param methodName
	 * @param args
	 * @param typeMappings
	 * @return
	 * @throws ServiceException
	 */
	private Object invokeSoapMethodOnRemoteServer(String remoteServerDistributeUrl, String serviceName, String methodName, Object[] args, Class[] typeMappings) throws ServiceException {
		if(Logger.isDebugEnabled()) {
			Logger.debug("DistributionService: invoke soap method on remote server, server path is " + remoteServerDistributeUrl + ", service name is " + serviceName + ", method name is " + methodName + ".");
		}
		localSoapPassport.setUrl(remoteServerDistributeUrl + "/services/");
		try {
			return soapConnectionPool.invokeRemoteMethod(serviceName, methodName, localSoapPassport, args, typeMappings);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.distribution.service.DistributionService#invokeMethodOnMasterServer(java.lang.String, java.lang.String, java.io.Serializable[])
	 */
	public Serializable invokeMethodOnMasterServer(String serviceName, String methodName, Serializable[] args) throws ServiceException {
		try {
			return invokeMethodOnRemoteServer((String)cache.get(MASTER_SERVER_KEY), serviceName, methodName, args);
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.distribution.service.DistributionService#invokeMethodOnRemoteServer(java.lang.String, java.lang.String, java.lang.String, java.io.Serializable[])
	 */
	public Serializable invokeMethodOnRemoteServer(String remoteServerDistributeUrl, String serviceName, String methodName, Serializable[] args) throws ServiceException {
		if(Logger.isDebugEnabled()) {
			Logger.debug("DistributionService: invoke method on remote server, server path is " + remoteServerDistributeUrl + ", service name is " + serviceName + ", method name is " + methodName + ".");
		}
		RemoteMethodInvoke methodInvoke = new RemoteMethodInvoke(serviceName, methodName, args);
		try {
			String returnValue = (String)invokeSoapMethodOnRemoteServer(remoteServerDistributeUrl, SOAP_SERVICE_NAME, "remoteMethodInvoke", new String[]{Encoder.getInstance().objectBase64Encode(methodInvoke)}, null);
			return returnValue==null ? null : (Serializable)Encoder.getInstance().objectBase64Decode(returnValue);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * @return the soapConnectionPool
	 */
	public SoapConnectionPool getSoapConnectionPool() {
		return soapConnectionPool;
	}

	/**
	 * @param soapConnectionPool the soapConnectionPool to set
	 */
	public void setSoapConnectionPool(SoapConnectionPool soapConnectionPool) {
		this.soapConnectionPool = soapConnectionPool;
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * @return the localSoapPassport
	 */
	public SoapPassport getLocalSoapPassport() {
		return localSoapPassport;
	}

	/**
	 * @param localSoapPassport the localSoapPassport to set
	 */
	public void setLocalSoapPassport(SoapPassport localSoapPassport) {
		try { //克隆,避免公共对象被修改
			this.localSoapPassport = (SoapPassport)localSoapPassport.clone();
		}
		catch (CloneNotSupportedException e) {
			
		}
	}

	/**
	 * @return the webApplicationDistributeUrl
	 */
	public String getWebApplicationDistributeUrl() {
		return webApplicationDistributeUrl;
	}

	/**
	 * @param webApplicationDistributeUrl the webApplicationDistributeUrl to set
	 */
	public void setWebApplicationDistributeUrl(String webApplicationDistributeUrl) {
		this.webApplicationDistributeUrl = StringUtils.fillLocalHostIP(webApplicationDistributeUrl);
	}
}
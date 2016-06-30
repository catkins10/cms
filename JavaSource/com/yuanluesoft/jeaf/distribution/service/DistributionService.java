package com.yuanluesoft.jeaf.distribution.service;

import java.io.Serializable;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface DistributionService {
	
	/**
	 * 获取分布式部署时的其他服务器访问时的URL
	 * @return
	 */
	public String getWebApplicationDistributeUrl();
	
	/**
	 * 检查当前服务器是否是分布式服务器中的主服务器
	 * @param verifyRemoteMasterServerAlive 检查远程的主服务器是否处于活动状态,如果不处于活动状态,则把当前服务器设为主服务器
	 * @return
	 * @throws ServiceException
	 */
	public boolean isMasterServer(boolean verifyRemoteMasterServerAlive) throws ServiceException;
	
	/**
	 * 把当前服务器设为主服务器
	 * @throws ServiceException
	 */
	public void setAsMasterServer() throws ServiceException;
	
	/**
	 * 运行主服务器上的方法
	 * @param serviceName
	 * @param methodName
	 * @param args
	 * @param typeMappings
	 * @return
	 * @throws ServiceException
	 */
	public Serializable invokeMethodOnMasterServer(String serviceName, String methodName, Serializable[] args) throws ServiceException;

	/**
	 * 运行远程服务器上的方法
	 * @param remoteServerDistributeUrl
	 * @param serviceName
	 * @param methodName
	 * @param args
	 * @param typeMappings
	 * @return
	 * @throws ServiceException
	 */
	public Serializable invokeMethodOnRemoteServer(String remoteServerDistributeUrl, String serviceName, String methodName, Serializable[] args) throws ServiceException;
}
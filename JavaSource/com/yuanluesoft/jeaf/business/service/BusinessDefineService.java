package com.yuanluesoft.jeaf.business.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 业务逻辑定义服务
 * @author linchuan
 *
 */
public interface BusinessDefineService {
	
	/**
	 * 获取业务对象的定义
	 * @param businessClass
	 * @return
	 * @throws ServiceException
	 */
	public BusinessObject getBusinessObject(Class businessClass) throws ServiceException;
	
	/**
	 * 按类名获取业务对象的定义
	 * @param businessClassName
	 * @return
	 * @throws ServiceException
	 */
	public BusinessObject getBusinessObject(String businessClassName) throws ServiceException;
	
	/**
	 * 获取应用的业务对象列表
	 * @param applicationName
	 * @return
	 * @throws ServiceException
	 */
	public List listBusinessObjects(String applicationName) throws ServiceException;
	
	/**
	 * 保存业务逻辑定义
	 * @param applicationName
	 * @param businessObjects
	 * @throws ServiceException
	 */
	public void saveBusinessDefine(String applicationName, List businessObjects) throws ServiceException;
}
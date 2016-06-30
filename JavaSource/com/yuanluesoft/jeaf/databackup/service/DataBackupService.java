package com.yuanluesoft.jeaf.databackup.service;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 平台自带的备份服务,是各种备份处理器的容器,如果系统有备份产品,可以不启用本服务 
 * @author linchuan
 *
 */
public interface DataBackupService {
	
	/**
	 * 备份,由定时器调用
	 * @throws ServiceException
	 */
	public void backup() throws ServiceException;
}

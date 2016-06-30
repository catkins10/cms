package com.yuanluesoft.traffic.expressway.service;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.traffic.expressway.pojo.ExpresswayPrice;

/**
 * 养路费服务
 * @author yuanluesoft
 *
 */
public interface ExpresswayService {
	
	/**
	 * 通信费查询
	 * @param entry
	 * @param exit
	 * @return
	 * @throws ServiceException
	 */
	public ExpresswayPrice getPrice(String entry, String exit) throws ServiceException;
}

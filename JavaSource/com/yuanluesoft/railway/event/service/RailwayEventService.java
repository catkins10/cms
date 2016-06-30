package com.yuanluesoft.railway.event.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.railway.event.pojo.RailwayEventImport;

/**
 * 
 * @author linchuan
 *
 */
public interface RailwayEventService extends BusinessService {

	/**
	 * 数据导入
	 * @param importId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public RailwayEventImport importData(long importId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 删除导入的数据
	 * @param importId
	 * @throws ServiceException
	 */
	public void deleteImportedData(long importId) throws ServiceException;
	
	/**
	 * 获取问题数量
	 * @param personId
	 * @param year
	 * @param month
	 * @return
	 * @throws ServiceException
	 */
	public int[] getEventCounts(long personId, int year, int month) throws ServiceException;
}
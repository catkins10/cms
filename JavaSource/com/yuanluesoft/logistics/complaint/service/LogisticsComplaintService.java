package com.yuanluesoft.logistics.complaint.service;

import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.logistics.complaint.pojo.LogisticsComplaint;

/**
 * 
 * @author linchuan
 *
 */
public interface LogisticsComplaintService extends PublicService {

	/**
	 * 统计货源被投诉次数
	 * @param complaint
	 * @return
	 * @throws ServiceException
	 */
	public int countSupplyComplaintTimes(LogisticsComplaint complaint) throws ServiceException;
	
	/**
	 * 统计公司/个人被投诉次数
	 * @param complaint
	 * @return
	 * @throws ServiceException
	 */
	public int countUserComplaintTimes(LogisticsComplaint complaint) throws ServiceException;
}
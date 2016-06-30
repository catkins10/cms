package com.yuanluesoft.j2oa.supervise.service;

import java.sql.Timestamp;

import com.yuanluesoft.j2oa.supervise.pojo.Supervise;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface SuperviseService extends BusinessService {
	public static final int SUPERVISE_STATUS_NEW = 0; //新建
	public static final int SUPERVISE_STATUS_PROCESSING = 1; //正在落实
	public static final int SUPERVISE_STATUS_COMPLETED = 2; //销号
	
	public static final String[] SUPERVISE_STATUS_TITLES = {"新建", "正在落实", "销号"};
	
	/**
	 * 更新完成时限
	 * @param supervise
	 * @param timeLimit
	 * @throws ServiceException
	 */
	public void updateTimeLimit(Supervise supervise, Timestamp timeLimit) throws ServiceException;
}
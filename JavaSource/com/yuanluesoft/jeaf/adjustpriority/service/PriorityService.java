package com.yuanluesoft.jeaf.adjustpriority.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public interface PriorityService {
	
	/**
	 * 获取高优先级的记录列表
	 * @param view
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listHighPriorityRecords(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 调整优先级
	 * @param highPriorityRecordIds
	 * @param view
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void adjustPriority(String highPriorityRecordIds, View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
}

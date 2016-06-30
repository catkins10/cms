package com.yuanluesoft.jeaf.application.builder.service;

import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 应用定义服务
 * @author linchuan
 *
 */
public interface ApplicationDefineService extends BusinessService {
	
	/**
	 * 创建导航栏
	 * @param applicationId
	 * @throws ServiceException
	 */
	public void createNavigators(long applicationId) throws ServiceException;
	
	/**
	 * 批量创建视图
	 * @param applicationForm
	 * @param viewFieldIds
	 * @param viewFieldNames
	 * @param sortFieldIds
	 * @param sortFieldNames
	 * @param sortFieldDirections
	 * @throws ServiceException
	 */
	public void createViews(ApplicationForm applicationForm, String viewFieldIds, String viewFieldNames, String sortFieldIds, String sortFieldNames, String sortFieldDirections) throws ServiceException;
}
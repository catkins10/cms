/*
 * Created on 2005-4-5
 *
 */
package com.yuanluesoft.j2oa.dispatch.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.archives.administrative.model.FilingOption;
import com.yuanluesoft.j2oa.dispatch.pojo.Dispatch;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchFilingConfig;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public interface DispatchService extends BusinessService {
	
	/**
	 * 获取发文
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public Dispatch getDispatch(long id) throws ServiceException;
	
	/**
	 * 保存正文
	 * @param dispatch
	 * @param request
	 * @throws ServiceException
	 */
	public void saveBody(Dispatch dispatch, HttpServletRequest request) throws ServiceException;

	/**
	 * 获取归档配置
	 * @return
	 * @throws ServiceException
	 */
	public DispatchFilingConfig getFilingConfig() throws ServiceException;
	
	/**
	 * 分发
	 * @param dispatch
	 * @param workItemId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void distribute(Dispatch dispatch, String workItemId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 归档
	 * @param dispatch
	 * @param filingConfig
	 * @param filingOption
	 * @param filingPersonId
	 * @param filingPersonName
	 * @throws ServiceException
	 */
	public void filing(Dispatch dispatch, DispatchFilingConfig filingConfig, FilingOption filingOption, long filingPersonId, String filingPersonName) throws ServiceException;
}
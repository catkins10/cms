package com.yuanluesoft.cms.monitor.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.monitor.pojo.MonitorParameter;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface MonitorService extends BusinessService {
	
	/**
	 * 获取参数配置
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public MonitorParameter loadMonitorParameter(long id) throws ServiceException;
	
	/**
	 * 保存参数配置
	 * @param orgId
	 * @param orgName
	 * @param request
	 * @throws ServiceException
	 */
	public MonitorParameter saveMonitorParameter(long orgId, String orgName, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 处理采集请求
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void processCaptureRequest(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IOException;
	
	/**
	 * 获取采集时间,用最后配置的单位的采集时间加上5分钟
	 * @return
	 * @throws ServiceException
	 */
	public String getCaptureTime() throws ServiceException;
	
	/**
	 * 获取SQL示例
	 * @param captureContentClass
	 * @return
	 * @throws ServiceException
	 */
	public String getSampleSql(String captureContentClass) throws ServiceException;
}
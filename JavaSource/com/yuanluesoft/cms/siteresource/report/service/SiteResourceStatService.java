package com.yuanluesoft.cms.siteresource.report.service;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.cms.siteresource.report.model.ensurereport.EnsureReport;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 站点资源统计服务
 * @author linchuan
 *
 */
public interface SiteResourceStatService {

	/**
	 * 创建栏目统计报表,返回UnitStat列表
	 * @param beginDate
	 * @param endDate
	 * @param siteId
	 * @param unitIds
	 * @return
	 * @throws ServiceException
	 */
	public List createColumnStatReport(Date beginDate, Date endDate, long siteId, String unitIds) throws ServiceException;
	
	/**
	 * 输出信息保障报表
	 * @param siteId
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	public EnsureReport writeEnsureReport(long siteId, Date beginDate, Date endDate) throws ServiceException;
	
	/**
	 * 获取单位分类
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listEnsureUnitCategories(long siteId) throws ServiceException;
	
	/**
	 * 获取栏目配置列表
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listEnsureColumnConfigs(long siteId) throws ServiceException;
}
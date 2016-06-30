/*
 * Created on 2006-9-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.services;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public interface ArchivesConfigService extends BusinessService {
	
	/**
	 * 获取全宗列表
	 * @return
	 * @throws ServiceException
	 */
	public List listFonds() throws ServiceException;
	
	/**
	 * 获取机构或问题列表
	 * @return
	 * @throws ServiceException
	 */
	public List listArchivesUnits() throws ServiceException;
	
	/**
	 * 获取密级列表
	 * @return
	 * @throws ServiceException
	 */
	public List listSecureLevels() throws ServiceException;
	
	/**
	 * 获取保管期限列表
	 * @return
	 * @throws ServiceException
	 */
	public List listRotentionPeriods() throws ServiceException;
	
	/**
	 * 获取指定密级的访问者列表
	 * @param secureLevel
	 * @return
	 * @throws ServiceException
	 */
	public List listSecureLevelVisitors(String secureLevel) throws ServiceException;
	
	/**
	 * 根据全宗名称获取全宗号
	 * @param fondsName
	 * @return
	 * @throws ServiceException
	 */
	public String getFondsCode(String fondsName) throws ServiceException;
	
	/**
	 * 根据机构或问题获取机构或问题编号
	 * @param archivesUnit
	 * @return
	 * @throws ServiceException
	 */
	public String getArchivesUnitCode(String archivesUnit) throws ServiceException;
	
	/**
	 * 根据密级获取密级编号
	 * @param secureLevel
	 * @return
	 * @throws ServiceException
	 */
	public String getSecureLevelCode(String secureLevel) throws ServiceException;
	
	/**
	 * 根据保管期限获取保管期限编号
	 * @param rotentionPeriod
	 * @return
	 * @throws ServiceException
	 */
	public String getRotentionPeriodCode(String rotentionPeriod) throws ServiceException;
}

package com.yuanluesoft.fet.tradestat.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author yuanluesoft
 *
 */
public interface TradeStatService extends BusinessService {
	
	/**
	 * 区县是否按编码查询
	 * @return
	 */
	public boolean isCountyQueryByCode();
	
	/**
	 * 初始化数据
	 * @throws ServiceException
	 */
	public void initData() throws ServiceException;
	
	/**
	 * 从excel文件导入数据
	 * @param dataFileName
	 * @param isExport
	 * @param year
	 * @param month
	 * @throws ServiceException
	 */
	public void importData(String dataFileName, boolean isExport, int year, int month) throws ServiceException;
	
	/**
	 * 输出出口数据
	 * @param countyCodes
	 * @param countyNames
	 * @param developmentAreaCodes
	 * @param developmentAreaNames
	 * @param companyCode
	 * @param companyName
	 * @param year
	 * @param month
	 * @param response
	 * @throws ServiceException
	 */
	public void exportExportData(List countyCodes, List countyNames, List developmentAreaCodes, List developmentAreaNames, String companyCode, String companyName, int year, int month, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出进口数据
	 * @param countyCodes
	 * @param countyNames
	 * @param developmentAreaCodes
	 * @param developmentAreaNames
	 * @param companyCode
	 * @param companyName
	 * @param year
	 * @param month
	 * @param response
	 * @throws ServiceException
	 */
	public void exportImportData(List countyCodes, List countyNames, List developmentAreaCodes, List developmentAreaNames, String companyCode, String companyName, int year, int month, HttpServletResponse response) throws ServiceException;
}

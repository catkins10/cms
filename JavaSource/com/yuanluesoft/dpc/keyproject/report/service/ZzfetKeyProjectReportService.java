package com.yuanluesoft.dpc.keyproject.report.service;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 漳州外经贸报表服务
 * @author linchuan
 *
 */
public interface ZzfetKeyProjectReportService {
	
	/**
	 * 输出战役项目报表
	 * @param year
	 * @param month
	 * @param keyMonth 大干150天的起始月份
	 * @param developmentArea 开发区或者开发区分类
	 * @param projectLevels
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void writeProgressReport(int year, int month, int keyMonth, String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 输出目标责任制表
	 * @param year
	 * @param month
	 * @param keyMonth 大干150天的起始月份
	 * @param developmentArea
	 * @param projectLevels
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void writeAccountableReport(int year, int month, int keyMonth, String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 输出重大支撑项目表
	 * @param year
	 * @param month
	 * @param keyMonth 大干150天的起始月份
	 * @param developmentArea 开发区或者开发区分类
	 * @param response
	 * @param sessionInfo
	 * @param projectLevels
	 * @throws ServiceException
	 */
	public void writePillarReport(int year, int month, int keyMonth, String developmentArea, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 输出经济开发区年度项目明细表
	 * @param year
	 * @param month
	 * @param keyMonth 大干150天的起始月份
	 * @param developmentArea 开发区或者开发区分类
	 * @param projectLevels
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void writeYearReport(int year, int month, int keyMonth, String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 输出旬报表
	 * @param year
	 * @param month
	 * @param tenDay 旬
	 * @param keyMonth 大干150天的起始月份
	 * @param developmentArea 开发区或者开发区分类
	 * @param projectLevels
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void writeTenDayReport(int year, int month, char tenDay, int keyMonth, String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 输出漳州市重点建设战役项目（前期工作）台账
	 * @param projectId
	 * @param year
	 * @param month
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void writeProphaseDetail(long projectId, int year, int month, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 输出漳州市重点建设战役项目（在建、新开工）台账
	 * @param projectId
	 * @param year
	 * @param month
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void writeBuildingDetail(long projectId, int year, int month, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
}
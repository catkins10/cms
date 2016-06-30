package com.yuanluesoft.dpc.keyproject.report.service;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 南平重点项目报表服务
 * @author linchuan
 *
 */
public interface NpdpcKeyProjectReportService {
	
	/**
	 * 输出月报表
	 * @param year
	 * @param month
	 * @param projectLevels
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void writeMonthReport(int year, int month, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 输出年报表
	 * @param year
	 * @param month
	 * @param projectLevels
	 * @param response
	 * @throws ServiceException
	 */
	public void writeYearReport(int year, int month, String[] projectLevels, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出项目参建单位汇总表
	 * @param year
	 * @param unitType 设计单位/监理单位/施工单位
	 * @param response
	 * @throws ServiceException
	 */
	public void writeUnitReport(int year, String unitType, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出资金到位汇总表
	 * @param year
	 * @param response
	 * @throws ServiceException
	 */
	public void writeInvestReport(int year, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 重点项目全年分布情况 
	 * @param year
	 * @param classify 拟建（预备）/ 在建
	 * @param graphBy industry/按行业、area/建设地、level/项目级别、investmentSubject/投资主体
	 * @param width
	 * @param height
	 * @param response
	 * @throws ServiceException
	 */
	public void writeDistributeGraph(int year, String classify, String graphBy, int width, int height, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 重点项目投资情况分析
	 * @param year
	 * @param graphBy industry/按行业、area/建设地、level/项目级别、investmentSubject/投资主体
	 * @param graphType 总体投资分析、年度计划投资分析以及投资完成分析
	 * @param width
	 * @param height
	 * @param response
	 * @throws ServiceException
	 */
	public void writeInvestGraph(int year, String graphBy, String graphType, int width, int height, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出五年计划报表
	 * @param year
	 * @param fiveYearPlanNumber
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void writeFiveYearReport(int year, int fiveYearPlanNumber, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
}
package com.yuanluesoft.jeaf.report.excel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 电子表格报表服务
 * @author linchuan
 *
 */
public interface ExcelReportService {

	/**
	 * 创建电子表格报表
	 * @param view
	 * @param currentCategories
	 * @param searchConditions
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void createExcelReport(View view, String currentCategories, String searchConditions, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 写报表,直接通过response返回给浏览器
	 * @param templateFile
	 * @param outputName
	 * @param response
	 * @param reportCallback
	 * @throws ServiceException
	 */
	public void writeExcelReport(String templateFile, String outputName, HttpServletResponse response, ExcelReportCallback reportCallback) throws ServiceException;
	
	/**
	 * 写报表,生成报表文件
	 * @param templateFile
	 * @param outputFilePath
	 * @param reportCallback
	 * @throws ServiceException
	 */
	public void writeExcelReport(String templateFile, String outputFilePath, ExcelReportCallback reportCallback) throws ServiceException;
}
/*
 * Created on 2005-3-5
 *
 */
package com.yuanluesoft.jeaf.report.pdf;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author LinChuan
 * 
 */
public interface PdfReportService {
	
	/**
	 * 创建PDF报表
	 * @param view
	 * @param currentCategories
	 * @param searchConditions
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public abstract void createPdfReport(View view, String currentCategories, String searchConditions, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * HTML转换为PDF输出
	 * @param htmlFileName
	 * @param outputStream
	 * @throws ServiceException
	 */
	public abstract void htmlToPdf(String htmlFileName, OutputStream outputStream) throws ServiceException;
}

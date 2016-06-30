package com.yuanluesoft.dpc.investmentproject.service;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.dpc.investmentproject.pojo.InvestmentProjectParameter;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface InvestmentProjectService extends BusinessService {

	/**
	 * 获取参数配置
	 * @return
	 * @throws ServiceException
	 */
	public InvestmentProjectParameter getParameter() throws ServiceException;
	
	/**
	 * 获取行业列表
	 * @return
	 * @throws ServiceException
	 */
	public List listIndustries() throws ServiceException;
	
	/**
	 * 输出报表
	 * @param beginDate
	 * @param endDate
	 * @param ares
	 * @param response
	 * @throws ServiceException
	 */
	public void writeProjectReport(Date beginDate, Date endDate, String area, HttpServletResponse response) throws ServiceException;
}
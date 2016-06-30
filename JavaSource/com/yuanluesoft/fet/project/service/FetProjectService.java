package com.yuanluesoft.fet.project.service;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface FetProjectService extends BusinessService {
	
	/**
	 * 输出参加活动可望签约项目情况表
	 * @param fairName
	 * @param fairNumber
	 * @param response
	 * @throws ServiceException
	 */
	public void exportFairPorjects(String fairName, int fairNumber, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出项目进展情况
	 * @param fairName
	 * @param fairNumber
	 * @param response
	 * @throws ServiceException
	 */
	public void exportPorjectEvolve(String fairName, int fairNumber, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出利用外资进度表
	 * @param year
	 * @param month
	 * @param response
	 * @throws ServiceException
	 */
	public void exportInvestment(int year, int month, HttpServletResponse response) throws ServiceException;
}

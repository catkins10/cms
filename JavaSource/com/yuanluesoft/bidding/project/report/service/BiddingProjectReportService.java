package com.yuanluesoft.bidding.project.report.service;

import java.sql.Date;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface BiddingProjectReportService {
	
	/**
	 * 输出招标项目统计报表(福州)
	 * @param beginDate
	 * @param endDate
	 * @param response
	 * @throws ServiceException
	 */
	public void writeProjectReport(Date beginDate, Date endDate, HttpServletResponse response) throws ServiceException;

	/**
	 * 输出代理标书销售报表
	 * @param beginDate
	 * @param endDate
	 * @param agentId
	 * @param agentName
	 * @param cities
	 * @param response
	 * @throws ServiceException
	 */
	public void writeAgentSalesReport(Date beginDate, Date endDate, long agentId, String agentName, String[] cities, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出项目投标报名汇总
	 * @param project
	 * @param response
	 * @throws ServiceException
	 */
	public void writeProjectSignUpReport(BiddingProject project, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出项目投标保证金汇总
	 * @param project
	 * @param status 0/全部, 1/待返还, 2/已返还
	 * @param response
	 * @throws ServiceException
	 */
	public void writeProjectPledgeReport(BiddingProject project, int status, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 输出标书销售费报表
	 * @param beginDate
	 * @param endDate
	 * @param paymentBanks 指定银行,空时则统计全部银行
	 * @param cities
	 * @param isReturn 是否返还明细表
	 * @param response
	 * @throws ServiceException
	 */
	public void writeDocumentSalesReport(Date beginDate, Date endDate, String paymentBanks, String[] cities, boolean isReturn, HttpServletResponse response) throws ServiceException;
}
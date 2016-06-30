package com.yuanluesoft.bidding.enterprise.services;

import java.sql.Date;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.membermanage.service.MemberService;

/**
 * 
 * @author yuanlue
 *
 */
public interface EmployeeService extends BusinessService, MemberService {
	
	/**
	 * 是否支持EKEY
	 */
	public boolean isEkeySupport();
	
	/**
	 * 获取试用天数
	 * @return
	 */
	public int getTryDays();
	
	/**
	 * 判断企业试用用户数是否超额
	 * @param enterpriseId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isTryOver(long enterpriseId) throws ServiceException;
	
	/**
	 * 导出EKEY报表
	 * @param beginDate
	 * @param endDate
	 * @param response
	 * @throws ServiceException
	 */
	public void exportEKeyReport(Date beginDate, Date endDate, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 添加员工
	 * @param enterprise
	 * @param loginName
	 * @param password
	 * @throws ServiceException
	 */
	public void addEmplyee(BiddingEnterprise enterprise, String loginName, String password) throws ServiceException;
}
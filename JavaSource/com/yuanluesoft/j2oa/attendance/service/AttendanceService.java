package com.yuanluesoft.j2oa.attendance.service;

import java.sql.Date;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.attendance.pojo.AttendanceMend;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 考勤服务
 * @author linchuan
 *
 */
public interface AttendanceService extends BusinessService {

	/**
	 * 输出报表（电子表格）
	 * @param orgId
	 * @param beginDate
	 * @param endDate
	 * @param response
	 * @throws ServiceException
	 */
	public void writeReport(long orgId, Date beginDate, Date endDate, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 打卡
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void punchCard(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 补卡审核通过
	 * @param mend
	 * @throws ServiceException
	 */
	public void approvalMend(AttendanceMend mend) throws ServiceException;
}
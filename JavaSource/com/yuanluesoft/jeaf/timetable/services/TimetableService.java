package com.yuanluesoft.jeaf.timetable.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.timetable.model.WorkAttendance;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;

/**
 * 
 * @author linchuan
 *
 */
public interface TimetableService extends BusinessService {

	/**
	 * 计算工作日
	 * @param beginTime
	 * @param endTime
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public double countWorkDays(Timestamp beginTime, Timestamp endTime, long orgId) throws ServiceException;
	
	/**
	 * 计算工作日
	 * @param beginTime
	 * @param endTime
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public double countWorkDays(Timestamp beginTime, Timestamp endTime, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 增加工作日
	 * @param time
	 * @param days
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public Timestamp addWorkDays(Timestamp time, double days, long orgId) throws ServiceException;
	
	/**
	 * 增加工作日
	 * @param time
	 * @param days
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Timestamp addWorkDays(Timestamp time, double days, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 单个用户考勤统计
	 * @param person
	 * @param beginDate
	 * @param endDate
	 * @param clockInTimes 打卡时间(Timestamp)列表,按从小到大顺序排列
	 * @param leavePosts 离岗(LeavePost)记录列表
	 * @return
	 * @throws ServiceException
	 */
	public WorkAttendance workAttendanceStat(Person person, Date beginDate, Date endDate, List clockInTimes, List leavePosts) throws ServiceException;
}
/*
 * Created on 2005-11-21
 *
 */
package com.yuanluesoft.j2oa.meeting.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.j2oa.meeting.pojo.Meeting;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.UserBusyException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public interface MeetingService extends BusinessService {
	
	/**
	 * 获得在指定时间范围内使用指定会议室的会议
	 * @param meetingRoomName
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ServiceException
	 */
	public List listMeetingsUseMeetingRoom(String meetingRoomName, Timestamp beginTime, Timestamp endTime) throws ServiceException;

	/**
	 * 发布公告
	 * @param meeting
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issueMeeting(Meeting meeting, boolean forceIssue, SessionInfo sessionInfo) throws ServiceException, UserBusyException;
	
	/**
	 * 撤销发布
	 * @param meeting
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void unissueMeeting(Meeting meeting, SessionInfo sessionInfo) throws ServiceException;
}
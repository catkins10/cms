/*
 * Created on 2005-11-21
 *
 */
package com.yuanluesoft.j2oa.meeting.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public interface MeetingRoomService extends BusinessService {
	/**
	 * 获取会议室列表
	 * @return
	 * @throws ServiceException
	 */
	public List listMeetingRoomNames() throws ServiceException;
}

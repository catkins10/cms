/*
 * Created on 2005-11-19
 *
 */
package com.yuanluesoft.j2oa.meeting.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.meeting.pojo.MeetingRoom;
import com.yuanluesoft.j2oa.meeting.service.MeetingRoomService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 *
 * @author linchuan
 *
 */
public class MeetingRoomServiceImpl extends BusinessServiceImpl implements MeetingRoomService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		MeetingRoom meetingRoom = (MeetingRoom)super.load(recordClass, id);
		String hql = "from Meeting Meeting";
		hql += " where Meeting.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ") and Meeting.address='" + JdbcUtils.resetQuot(meetingRoom.getName()) + "'";
		hql += " order by Meeting.beginTime";
		List todoMeetings = getDatabaseService().findRecordsByHql(hql, 0, 0);
		if(todoMeetings!=null && !todoMeetings.isEmpty()) {
			List meetings = new ArrayList();
			for(Iterator iterator = todoMeetings.iterator(); iterator.hasNext();) {
				meetings.add(iterator.next());
			}
			meetingRoom.setTodoMeetings(meetings);
		}
		return meetingRoom;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.meeting.service.MeetingRoomService#listMeetingRoomNames()
	 */
	public List listMeetingRoomNames() throws ServiceException {
		return getDatabaseService().findRecordsByHql("select MeetingRoom.name from MeetingRoom MeetingRoom", 0, 0);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return listMeetingRoomNames();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		MeetingRoom meetingRoom = (MeetingRoom)record;
		if(getDatabaseService().findRecordByHql("select MeetingRoom.name from MeetingRoom MeetingRoom where MeetingRoom.name='" + JdbcUtils.resetQuot(meetingRoom.getName()) + "'")!=null) {
			List errors = new ArrayList();
			errors.add("会议室名称重复");
			return errors;
		}
		return null;
	}
}
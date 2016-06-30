/*
 * Created on 2005-11-20
 *
 */
package com.yuanluesoft.j2oa.meeting.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.meeting.pojo.Meeting;
import com.yuanluesoft.j2oa.meeting.pojo.MeetingRoom;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 *
 * @author linchuan
 *
 */
public class MeetingRoomViewServiceImpl extends ViewServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.view.model.ViewPackage, java.lang.String, java.lang.String, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		if(records==null || records.isEmpty()) {
			return records;
		}
		String hql = "from Meeting Meeting";
		hql += " where Meeting.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")";
		hql += " order by Meeting.address, Meeting.beginTime";
		List todoMeetings = getDatabaseService().findRecordsByHql(hql, 0, 0);
		if(todoMeetings==null || todoMeetings.isEmpty()) {
			return records;
		}
		String prevAddress = null;
		MeetingRoom meetingRoom = null;
		for(Iterator iterator = todoMeetings.iterator(); iterator.hasNext();) {
			Meeting meeting = (Meeting)iterator.next();
			String address = meeting.getAddress();
			if(!address.equals(prevAddress)) {
				prevAddress = address;
				meetingRoom = (MeetingRoom)ListUtils.findObjectByProperty(records, "name", address);
				if(meetingRoom!=null) {
					meetingRoom.setTodoMeetings(new ArrayList());
				}
			}
			if(meetingRoom!=null) {
				meetingRoom.getTodoMeetings().add(meeting);
			}
		}
		return records;
	}
}
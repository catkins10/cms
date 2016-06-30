/*
 * Created on 2005-11-19
 *
 */
package com.yuanluesoft.j2oa.meeting.service.spring;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.j2oa.meeting.pojo.Meeting;
import com.yuanluesoft.j2oa.meeting.pojo.MeetingPrivilege;
import com.yuanluesoft.j2oa.meeting.service.MeetingService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.UserBusyException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.UserBusyCheckService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 *
 * @author linchuan
 *
 */
public class MeetingServiceImpl extends BusinessServiceImpl implements MeetingService {
	private RecordControlService recordControlService; //记录控制服务
	private MessageService messageService; //消息通知服务
	private UserBusyCheckService userBusyCheckService; //用户忙碌检测服务
	private PersonService personService; //用户服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.meeting.service.MeetingService#ListMeetingsUseMeetingRoom(java.lang.String, java.sql.Timestamp, java.sql.Timestamp)
	 */
	public List listMeetingsUseMeetingRoom(String meetingRoomName, Timestamp beginTime, Timestamp endTime) throws ServiceException {
		if(beginTime==null || endTime==null) {
			return null;
		}
		String hql = "from MeetingRoom MeetingRoom where MeetingRoom.name='" + JdbcUtils.resetQuot(meetingRoomName) + "'";
		if(getDatabaseService().findRecordByHql(hql)==null) { //会议地点不在会议室
			return null;
		}
		hql = "from Meeting Meeting";
		hql += " where Meeting.address='" + JdbcUtils.resetQuot(meetingRoomName) + "'"; //会议室检查
		hql += " and Meeting.issued='1'"; //已发布
		hql += " and Meeting.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTime, null) + ")";
		hql += " and Meeting.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(java.lang.Object)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		List errors = new ArrayList();
		if(record instanceof Meeting) {
			Meeting pojoMeeting = (Meeting)record;
			if(pojoMeeting.getBeginTime().equals(pojoMeeting.getEndTime()) || pojoMeeting.getBeginTime().after(pojoMeeting.getEndTime())) {
				errors.add("会议开始时间必须早于结束时间");
			}
		}
		return errors.isEmpty() ? null : errors;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.meeting.service.MeetingService#issueMeeting(com.yuanluesoft.j2oa.meeting.pojo.Meeting, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issueMeeting(Meeting meeting, boolean forceIssue, SessionInfo sessionInfo) throws ServiceException, UserBusyException {
		if(meeting.getBeginTime().before(DateTimeUtils.now())) {
			throw new ValidateException("会议开始时间必须在当前时间以后");
		}
		List meetings = listMeetingsUseMeetingRoom(meeting.getAddress(), meeting.getBeginTime(), meeting.getEndTime());
		if(meetings!=null && !meetings.isEmpty()) {
			String validateError = meeting.getAddress() + "已经被下列会议占用:";
			int i=1;
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(Iterator iterator = meetings.iterator(); iterator.hasNext();) {
				Meeting otherMeeting = (Meeting)iterator.next();
				validateError += "\r\n  (" + (i++) + ") " + otherMeeting.getSubject() + ",时间:" + formater.format(otherMeeting.getBeginTime()) + " 至 " + formater.format(otherMeeting.getEndTime());
			}
			throw new ValidateException(validateError);
		}
		List attendees = recordControlService.listVisitors(meeting.getId(), Meeting.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(!forceIssue && attendees!=null) { //不是强制发布,且指定了与会人员
		    //检查领导日程安排是否有冲突
			String conflict = null;
		    for(Iterator iterator = attendees.iterator(); iterator.hasNext();) {
		    	MeetingPrivilege attendee = (MeetingPrivilege)iterator.next();
		    	Person person = personService.getPerson(attendee.getVisitorId());
		    	if(person==null) { //不是个人
		    		continue;
		    	}
		        List checkResult = userBusyCheckService.userBusyCheck(attendee.getVisitorId(), meeting.getBeginTime(), meeting.getEndTime());
		        if(checkResult!=null && !checkResult.isEmpty()) {
		        	conflict = (conflict==null ? "" : conflict + "\r\n\r\n") + person.getName();
		            for(Iterator iteratorResult = checkResult.iterator(); iteratorResult.hasNext();) {
		            	conflict += "\r\n" + iteratorResult.next();
		            }
		        }
		    }
		    if(conflict!=null) {
		        throw new UserBusyException(conflict);
		    }
	    }
		meeting.setIssued('1');
		meeting.setIssueTime(DateTimeUtils.now());
		update(meeting);
		if(attendees==null) { //允许所有人访问
			recordControlService.appendVisitor(meeting.getId(), Meeting.class.getName(), 0, RecordControlService.ACCESS_LEVEL_READONLY);
		}
		else {
			List readers = recordControlService.copyVisitors(meeting.getId(), meeting.getId(), RecordControlService.ACCESS_LEVEL_PREREAD, RecordControlService.ACCESS_LEVEL_READONLY, Meeting.class.getName());
			//发送通知
			messageService.removeMessages(meeting.getId());
			String message = "会议：" + meeting.getSubject() + "\r\n地点：" + meeting.getAddress();
			SimpleDateFormat formater = new SimpleDateFormat("MM-dd HH:mm");
			message +=  "\r\n时间：" + formater.format(meeting.getBeginTime()) + " 到 " + formater.format(meeting.getEndTime());
			messageService.sendMessageToVisitors(readers, true, message, sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, meeting.getId(), null, null, null, null, 0, null);
			//会前30分钟再通知一次
			Timestamp messageTime = new Timestamp(meeting.getBeginTime().getTime() - 30 * 60000);
			messageService.sendMessageToVisitors(readers, true, message, sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, meeting.getId(), null, messageTime, null, null, 0, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.meeting.service.MeetingService#unissueMeeting(com.yuanluesoft.j2oa.meeting.pojo.Meeting, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void unissueMeeting(Meeting meeting, SessionInfo sessionInfo) throws ServiceException {
		meeting.setIssued('0');
		update(meeting);
		List preReaders = getRecordControlService().listVisitors(meeting.getId(), Meeting.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(preReaders!=null && !preReaders.isEmpty()) {
			messageService.removeMessages(meeting.getId());
			String message = "取消会议：" + meeting.getSubject() + "\r\n地点：" + meeting.getAddress();
			SimpleDateFormat formater = new SimpleDateFormat("MM-dd HH:mm");
			message +=  "\r\n时间：" + formater.format(meeting.getBeginTime()) + " 到 " + formater.format(meeting.getEndTime());
			//发送通知
			messageService.sendMessageToVisitors(preReaders, true, message, sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, meeting.getId(), null, null, null, null, 0, null);
		}
	}

	/**
	 * @return the messageService
	 */
	public MessageService getMessageService() {
		return messageService;
	}

	/**
	 * @param messageService the messageService to set
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * @return the recordControlService
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}

	/**
	 * @param recordControlService the recordControlService to set
	 */
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @return the userBusyCheckService
	 */
	public UserBusyCheckService getUserBusyCheckService() {
		return userBusyCheckService;
	}

	/**
	 * @param userBusyCheckService the userBusyCheckService to set
	 */
	public void setUserBusyCheckService(UserBusyCheckService userBusyCheckService) {
		this.userBusyCheckService = userBusyCheckService;
	}
}
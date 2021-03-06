/*
 * Created on 2006-6-5
 *
 */
package com.yuanluesoft.j2oa.meeting.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.j2oa.meeting.pojo.Meeting;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.usermanage.service.UserBusyCheckService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 *
 * @author linchuan
 *
 */
public class UserBusyCheckServiceImpl implements UserBusyCheckService {
    private DatabaseService databaseService;

    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.usermanage.service.UserBusyCheckService#userBusyCheck(long, java.sql.Timestamp, java.sql.Timestamp)
     */
    public List userBusyCheck(long personId, Timestamp beginTime, Timestamp endTime) throws ServiceException {
        if(beginTime==null || endTime==null) {
			return null;
		}
        String hql = "select Meeting from Meeting Meeting, MeetingPrivilege MeetingPrivilege" +
					 " where Meeting.id=MeetingPrivilege.recordId" +
					 " and Meeting.issued='1'" + 
					 " and Meeting.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTime, null) + ")" +
					 " and Meeting.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")" +
					 " and MeetingPrivilege.visitorId=" + personId +
					 " and MeetingPrivilege.accessLevel='" + RecordControlService.ACCESS_LEVEL_PREREAD + "'";
		List records = getDatabaseService().findRecordsByHql(hql);
		if(records==null || records.isEmpty()) { 
			return null;
		}
		List result = new ArrayList();
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
		    Meeting meeting = (Meeting)iterator.next();
		    result.add("会议:" + meeting.getSubject() + "(" + DateTimeUtils.formatTimestamp(meeting.getBeginTime(), "MM-dd HH:mm") + " 到 " + DateTimeUtils.formatTimestamp(meeting.getEndTime(), "MM-dd HH:mm") + ")");
		}
        return result;
    }

    /**
     * @return Returns the databaseService.
     */
    public DatabaseService getDatabaseService() {
        return databaseService;
    }
    /**
     * @param databaseService The databaseService to set.
     */
    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
}

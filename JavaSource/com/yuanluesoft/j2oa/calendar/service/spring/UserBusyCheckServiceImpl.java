/*
 * Created on 2006-6-5
 *
 */
package com.yuanluesoft.j2oa.calendar.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.j2oa.calendar.pojo.Calendar;
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
        String hql = "select Calendar from Calendar Calendar, CalendarPrivilege CalendarPrivilege" +
					 " where Calendar.id=CalendarPrivilege.recordId" +
					 " and Calendar.publish='1'" + 
					 " and Calendar.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTime, null) + ")" +
					 " and Calendar.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")" +
					 " and CalendarPrivilege.visitorId=" + personId +
					 " and CalendarPrivilege.accessLevel='" + RecordControlService.ACCESS_LEVEL_PREREAD + "'";
		List records = getDatabaseService().findRecordsByHql(hql);
		if(records==null || records.isEmpty()) { 
			return null;
		}
		List result = new ArrayList();
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
		    Calendar calendar = (Calendar)iterator.next();
		    result.add("日程安排:" + calendar.getSubject() + "(" + DateTimeUtils.formatTimestamp(calendar.getBeginTime(), "MM-dd HH:mm") + " 到 " + DateTimeUtils.formatTimestamp(calendar.getEndTime(), "MM-dd HH:mm") + ")");
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
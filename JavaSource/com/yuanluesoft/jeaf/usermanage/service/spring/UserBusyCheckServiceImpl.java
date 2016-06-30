/*
 * Created on 2006-6-5
 *
 */
package com.yuanluesoft.jeaf.usermanage.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.service.UserBusyCheckService;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class UserBusyCheckServiceImpl implements UserBusyCheckService {
    private String userBusyCheckServiceNames; //服务名称列表 

    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.usermanage.service.UserBusyCheckService#userBusyCheck(long, java.sql.Timestamp, java.sql.Timestamp)
     */
    public List userBusyCheck(long personId, Timestamp beginTime, Timestamp endTime) throws ServiceException {
        if(userBusyCheckServiceNames==null || userBusyCheckServiceNames.equals("")) {
            return null;
        }
        List result = new ArrayList();
        String[] serviceNames = userBusyCheckServiceNames.split(",");
        for(int i=0; i<serviceNames.length; i++) {
        	UserBusyCheckService userBusyCheckService = (UserBusyCheckService)Environment.getService(serviceNames[i]);
        	if(userBusyCheckService==null) {
        		continue;
        	}
            List checkResult = userBusyCheckService.userBusyCheck(personId, beginTime, endTime);
            if(checkResult!=null && !checkResult.isEmpty()) {
                result.addAll(checkResult);
            }
        }
        return result.isEmpty() ? null : result;
    }

	/**
	 * @return the userBusyCheckServiceNames
	 */
	public String getUserBusyCheckServiceNames() {
		return userBusyCheckServiceNames;
	}

	/**
	 * @param userBusyCheckServiceNames the userBusyCheckServiceNames to set
	 */
	public void setUserBusyCheckServiceNames(String userBusyCheckServiceNames) {
		this.userBusyCheckServiceNames = userBusyCheckServiceNames;
	}
}
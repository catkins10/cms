/*
 * Created on 2006-7-5
 *
 */
package com.yuanluesoft.j2oa.leave.service.spring;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.leave.pojo.Leave;
import com.yuanluesoft.j2oa.leave.pojo.LeaveConfig;
import com.yuanluesoft.j2oa.leave.service.LeaveService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.AgentService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 *
 * @author linchuan
 *
 */
public class LeaveServiceImpl extends BusinessServiceImpl implements LeaveService {
    private AgentService agentService;
    private RecordControlService recordControlService;

    /* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.leave.service.LeaveService#approvalLeave(com.yuanluesoft.j2oa.leave.pojo.Leave, boolean)
	 */
	public void approvalLeave(Leave leave, boolean pass) throws ServiceException {
		leave.setApprovalPass(pass ? '1' : '2');
		getDatabaseService().updateRecord(leave);
		//设置代理人
	    RecordVisitorList agents = recordControlService.getVisitors(leave.getId(), Leave.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
        agentService.setAgents(leave.getPersonId(), agents.getVisitorIds(), leave.getBeginTime(), leave.getEndTime(), "j2oa/leave");
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.leave.service.LeaveService#terminateLeave(com.yuanluesoft.j2oa.leave.pojo.Leave)
	 */
	public void terminateLeave(Leave leave) throws ServiceException {
	    //设置销假时间
        leave.setIsTerminated('1');
        leave.setTerminateTime(DateTimeUtils.now());
        getDatabaseService().updateRecord(leave);
        
        //删除代理人
        agentService.removeAgents(leave.getPersonId(), "j2oa/leave");
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.leave.service.LeaveService#getLeaveConfig()
	 */
	public LeaveConfig getLeaveConfig() throws ServiceException {
		return (LeaveConfig)getDatabaseService().findRecordByHql("from LeaveConfig LeaveConfig");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		LeaveConfig leaveConfig = getLeaveConfig();
		return ListUtils.generateList(leaveConfig==null ? "病假,事假,年休假" : leaveConfig.getTypes(), ",");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.businesstrip.service.BusinesstripService#listPersonalBusinesstrips(long, java.sql.Timestamp, java.sql.Timestamp)
	 */
	public List listPersonalLeaves(long personId, Timestamp beginTime, Timestamp endTime) throws ServiceException {
		String hql = "from Leave Leave" +
					 " where Leave.personId=" + personId +
					 " and Leave.approvalPass='1'" +
					 " and Leave.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTime, null) + ")" +
					 " and Leave.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")" +
					 " order by Leave.beginTime";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/**
	 * @return the agentService
	 */
	public AgentService getAgentService() {
		return agentService;
	}
	/**
	 * @param agentService the agentService to set
	 */
	public void setAgentService(AgentService agentService) {
		this.agentService = agentService;
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
}

/*
 * Created on 2006-6-30
 *
 */
package com.yuanluesoft.j2oa.businesstrip.service.spring;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.j2oa.businesstrip.pojo.Businesstrip;
import com.yuanluesoft.j2oa.businesstrip.service.BusinesstripService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 *
 * @author linchuan
 *
 */
public class BusinesstripServiceImpl extends BusinessServiceImpl implements BusinesstripService {
    private RecordControlService recordControlService;
    private WorkflowExploitService workflowExploitService;

    /* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.businesstrip.service.BusinesstripService#approvalBusinesstrip(com.yuanluesoft.j2oa.businesstrip.pojo.Businesstrip, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void approvalBusinesstrip(Businesstrip businessTrip, String workItemId, boolean pass, SessionInfo sessionInfo) throws ServiceException {
		businessTrip.setApprovalPass(pass ? '1' : '0');
		getDatabaseService().updateRecord(businessTrip);
		if(!pass) {
			return;
		}
		RecordVisitorList visitors = recordControlService.getVisitors(businessTrip.getId(), Businesstrip.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(visitors==null) {
			return;
		}
        WorkflowMessage workflowMessage = new WorkflowMessage("出差", "出差" + businessTrip.getAddress() + "," + businessTrip.getReason(), null);
	    String[] ids = visitors.getVisitorIds().split(",");
	    String[] names = visitors.getVisitorNames().split(",");
	    for(int i=0; i<ids.length; i++) {
	    	workflowExploitService.addVisitor(businessTrip.getWorkflowInstanceId(), workItemId, ids[i], names[i], workflowMessage, businessTrip, sessionInfo);
		}
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.businesstrip.service.BusinesstripService#listPersonalBusinesstrips(long, java.sql.Timestamp, java.sql.Timestamp)
	 */
	public List listPersonalBusinesstrips(long personId, Timestamp beginTime, Timestamp endTime) throws ServiceException {
		String hql = "select Businesstrip" +
					 " from Businesstrip Businesstrip, BusinesstripPrivilege BusinesstripPrivilege" +
					 " where BusinesstripPrivilege.recordId=Businesstrip.id" +
					 " and BusinesstripPrivilege.visitorId=" + personId +
					 " and BusinesstripPrivilege.accessLevel='" + RecordControlService.ACCESS_LEVEL_PREREAD + "'" +
					 " and Businesstrip.approvalPass='1'" +
					 " and Businesstrip.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTime, null) + ")" +
					 " and Businesstrip.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")" +
					 " order by Businesstrip.beginTime";
		return getDatabaseService().findRecordsByHql(hql);
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
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}
}
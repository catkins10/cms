package com.yuanluesoft.enterprise.workreport.service.spring;

import java.util.List;

import com.yuanluesoft.enterprise.workreport.pojo.WorkReport;
import com.yuanluesoft.enterprise.workreport.service.WorkReportService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class WorkReportServiceImpl extends BusinessServiceImpl implements WorkReportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.workreport.service.WorkReportService#listProjectTeamWorkReports(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, int, int)
	 */
	public List listProjectTeamWorkReports(long teamId, SessionInfo sessionInfo, int offset, int limit) throws ServiceException {
		//获取考核记录
		return getDatabaseService().findPrivilegedRecords(WorkReport.class.getName(), null, null, "WorkReport.teamId=" + teamId, "WorkReport.reportTime DESC", null, RecordControlService.ACCESS_LEVEL_READONLY, false, null, offset, limit, sessionInfo);
	}
}
package com.yuanluesoft.logistics.complaint.service.spring;

import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.logistics.complaint.pojo.LogisticsComplaint;
import com.yuanluesoft.logistics.complaint.service.LogisticsComplaintService;

/**
 * 
 * @author linchuan
 *
 */
public class LogisticsComplaintServiceImpl extends PublicServiceImpl implements LogisticsComplaintService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.complaint.service.LogisticsComplaintService#countSupplyComplaintTimes(com.yuanluesoft.logistics.complaint.pojo.LogisticsComplaint)
	 */
	public int countSupplyComplaintTimes(LogisticsComplaint complaint) throws ServiceException {
		String hql = "select count(LogisticsComplaint.id)" +
					 " from LogisticsComplaint LogisticsComplaint" +
					 " where LogisticsComplaint." + (complaint.getSupplyId()>0 ? "supplyId=" + complaint.getSupplyId() : "vehicleSupplyId=" + complaint.getVehicleSupplyId());
		return ((Number)getDatabaseService().findRecordByHql(hql)).intValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.complaint.service.LogisticsComplaintService#countUserComplaintTimes(com.yuanluesoft.logistics.complaint.pojo.LogisticsComplaint)
	 */
	public int countUserComplaintTimes(LogisticsComplaint complaint) throws ServiceException {
		String hql = "select count(LogisticsComplaint.id)" +
					 " from LogisticsComplaint LogisticsComplaint" +
					 " where LogisticsComplaint.userId=" + complaint.getUserId();
		return ((Number)getDatabaseService().findRecordByHql(hql)).intValue();
	}
}
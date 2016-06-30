package com.yuanluesoft.cms.infopublic.request.service.spring;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.yuanluesoft.cms.infopublic.model.InfoStat;
import com.yuanluesoft.cms.infopublic.request.service.PublicRequestService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PublicRequestServiceImpl implements PublicRequestService {
	private DatabaseService databaseService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.request.service.PublicRequestService#stat(java.sql.Date, java.sql.Date, long)
	 */
	public List stat(Date beginDate, Date endDate, long siteId) throws ServiceException {
		List stats = new ArrayList();
		//统计总数
		String hql = "select count(PublicRequest.id)" +
					 " from PublicRequest PublicRequest" +
					 " where PublicRequest.siteId=" + siteId +
					 " and PublicRequest.created>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 (endDate==null ? "" : " and PublicRequest.created<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")");
		Number count = (Number)getDatabaseService().findRecordByHql(hql);
		InfoStat stat = new InfoStat();
		stats.add(stat);
		stat.setName("申请总数");
		int total = count==null ? 0 : count.intValue();
		stat.setCount(total);
		stat.setPercent(-1);
		if(total==0) {
			return stats;
		}
		//获取各种申请形式的申请数量
		String[] receiveModes = {"邮寄", "快递", "电子邮件", "传真", "自行领取/当场阅读、抄录"};
		for(int i=0; i<receiveModes.length; i++) {
			hql = "select count(PublicRequest.id)" +
				  " from PublicRequest PublicRequest" +
				  " where PublicRequest.siteId=" + siteId +
				  " and PublicRequest.created>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
				  (endDate==null ? "" : " and PublicRequest.created<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")") +
				  " and PublicRequest.receiveMode like '" + JdbcUtils.resetQuot(receiveModes[i]) + "%'";
			count = (Number)getDatabaseService().findRecordByHql(hql);
			stat = new InfoStat();
			stats.add(stat);
			stat.setName(receiveModes[i]);
			stat.setCount(count==null ? 0 : count.intValue());
			stat.setPercent((stat.getCount() + 0.0f)/total);
		}
		return stats;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}

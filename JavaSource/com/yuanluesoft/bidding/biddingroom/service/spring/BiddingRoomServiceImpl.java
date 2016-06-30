package com.yuanluesoft.bidding.biddingroom.service.spring;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.bidding.biddingroom.service.BiddingRoomService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class BiddingRoomServiceImpl extends BusinessServiceImpl  implements BiddingRoomService {
	private PageService pageService; //页面服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		super.save(record);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		super.update(record);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.biddingroom.service.BiddingRoomService#listRooms(java.lang.String, java.lang.String)
	 */
	public List listRooms(String roomType, String city) throws ServiceException {
		String hql = "from BiddingRoom BiddingRoom" +
					 " where BiddingRoom.type='" + JdbcUtils.resetQuot(roomType) + "'" +
					 (city==null ? "" : " and BiddingRoom.city='" + JdbcUtils.resetQuot(city) + "'") +
					 " order by BiddingRoom.priority DESC, BiddingRoom.name";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.biddingroom.service.BiddingRoomService#listFreeRooms(java.lang.String, java.lang.String, long, java.sql.Timestamp, java.sql.Timestamp)
	 */
	public List listFreeRooms(String roomType, String city, long forProjectId, Timestamp beginTime, Timestamp endTimestamp) throws ServiceException {
		String hql = "from BiddingRoom BiddingRoom" +
				     " where BiddingRoom.type='" + JdbcUtils.resetQuot(roomType) + "'" +
				     " and BiddingRoom.city='" + JdbcUtils.resetQuot(city) + "'" +
				     " and not BiddingRoom.id in (" +
				     "  select BiddingRoomSchedule.roomId" +
				     "   from BiddingRoomSchedule BiddingRoomSchedule, BiddingProject BiddingProject" +
				     "   where BiddingRoomSchedule.projectId!=" + forProjectId +
				     "   and BiddingRoomSchedule.projectId=BiddingProject.id" +
				     "   and BiddingProject.halt!='1'" +
				     "   and BiddingRoomSchedule.publicPersonId>0" +
				     "   and BiddingRoomSchedule.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTimestamp, null) + ")" +
				     "   and BiddingRoomSchedule.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")" +
				     ")" +
				     " order by BiddingRoom.priority DESC, BiddingRoom.name";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.biddingroom.service.BiddingRoomService#isFreeRoom(long, long, java.sql.Timestamp, java.sql.Timestamp)
	 */
	public boolean isFreeRoom(long roomId, long forProjectId, Timestamp beginTime, Timestamp endTimestamp) throws ServiceException {
		String hql = "select BiddingRoomSchedule.id" +
					 " from BiddingRoomSchedule BiddingRoomSchedule, BiddingProject BiddingProject" +
				     " where BiddingRoomSchedule.roomId=" + roomId +
				     " and BiddingRoomSchedule.projectId!=" + forProjectId +
				     " and BiddingRoomSchedule.projectId=BiddingProject.id" +
				     " and BiddingProject.halt!='1'" +
				     " and BiddingRoomSchedule.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(endTimestamp, null) + ")" +
				     " and BiddingRoomSchedule.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(beginTime, null) + ")";
		return getDatabaseService().findRecordByHql(hql)==null;
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
}
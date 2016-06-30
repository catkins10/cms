package com.yuanluesoft.bidding.biddingroom.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;


/**
 * 
 * @author linchuan
 *
 */
public interface BiddingRoomService extends BusinessService {
	
	/**
	 * 获取开评标室列表
	 * @param roomType
	 * @param city
	 * @return
	 * @throws ServiceException
	 */
	public List listRooms(String roomType, String city) throws ServiceException;
	
	/**
	 * 获取空闲开评标室列表
	 * @param roomType
	 * @param city
	 * @param forProjectId
	 * @param beginTime
	 * @param endTimestamp
	 * @return
	 * @throws ServiceException
	 */
	public List listFreeRooms(String roomType, String city, long forProjectId, Timestamp beginTime, Timestamp endTimestamp) throws ServiceException;
	
	/**
	 * 检查开评标室是否空闲
	 * @param roomId
	 * @param forProjectId
	 * @param beginTime
	 * @param endTimestamp
	 * @return
	 * @throws ServiceException
	 */
	public boolean isFreeRoom(long roomId, long forProjectId, Timestamp beginTime, Timestamp endTimestamp) throws ServiceException;
}
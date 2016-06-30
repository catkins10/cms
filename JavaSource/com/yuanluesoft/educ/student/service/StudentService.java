package com.yuanluesoft.educ.student.service;

import java.util.List;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.educ.student.pojo.Stude;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

public interface StudentService extends PublicService {
	
	
	
	/**
	 * 完成注册
	 * @param stude
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeRegist(Stude stude, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建一个学生变更记录
	 * @param studeId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Stude createAlter(long studeId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 完成学生变更
	 * @param studeAlter
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeAlter(Stude studeAlter, SessionInfo sessionInfo) throws ServiceException;
	
	
	/**
	 * 检查学生信息是否有效
	 * @param studeId
	 * @return
	 */
	public boolean isStudentValid(long studeId);
}

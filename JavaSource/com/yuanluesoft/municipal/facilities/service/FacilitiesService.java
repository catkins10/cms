package com.yuanluesoft.municipal.facilities.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.municipal.facilities.pdaservice.model.SzcgEventReportParameter;
import com.yuanluesoft.municipal.facilities.pdaservice.model.ValiateProDepartHandleResultResult;
import com.yuanluesoft.municipal.facilities.pdaservice.model.ValidateEvent;
import com.yuanluesoft.municipal.facilities.pojo.FacilitiesEvent;
import com.yuanluesoft.municipal.facilities.pojo.FacilitiesEventParameter;

/**
 * 市政设施监控服务
 * @author linchuan
 *
 */
public interface FacilitiesService extends BusinessService {
	
	/**
	 * 获取事件
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public FacilitiesEvent getEvent(long id) throws ServiceException;

	/**
	 * 事件上报
	 * @param entity
	 * @throws ServiceException
	 */
	public void reportEvent(SzcgEventReportParameter entity) throws ServiceException;
	
	/**
	 * 获取PDA使用者列表
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listPdaUsers(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取PDA用户的任务数量
	 * @param pdaUserCode
	 * @return
	 * @throws ServiceException
	 */
	public int countPdaUserTasks(String pdaUserCode) throws ServiceException;
	
	/**
	 * 获取PDA用户的任务列表
	 * @param pdaUserCode
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	public List listPdaUserTasks(String pdaUserCode, int pageIndex, int pageSize) throws ServiceException;
	
	/**
	 * PDA完成事件真实性检查
	 * @param validateResult
	 * @throws ServiceException
	 */
	public void completePdaValidateTruth(ValidateEvent validateResult) throws ServiceException;
	
	/**
	 * PDA完成事件处理结果检查
	 * @param validateResult
	 * @throws ServiceException
	 */
	public void completePdaValidateResult(ValiateProDepartHandleResultResult validateResult) throws ServiceException;
	
	/**
	 * 检查PDA用户是否登录
	 * @param userCode
	 * @return
	 * @throws ServiceException
	 */
	public boolean isPadUserLogin(String userCode) throws ServiceException;
	
	/**
	 * 检查PDA用户是否登录GPS
	 * @param userCode
	 * @return
	 * @throws ServiceException
	 */
	public boolean isPadUserGPSLogin(String userCode) throws ServiceException;
	
	/**
	 * 加载参数配置
	 * @return
	 * @throws ServiceException
	 */
	public FacilitiesEventParameter loadEventParameter() throws ServiceException;
	
	/**
	 * 生成项目编号
	 * @param event
	 * @param preview
	 * @return
	 * @throws ServiceException
	 */
	public String generateEventNumber(FacilitiesEvent event, boolean preview) throws ServiceException;
}
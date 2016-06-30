package com.yuanluesoft.appraise.appraiser.service;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.appraise.appraiser.pojo.AppraiserImport;
import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface AppraiserService extends BusinessService, ApplicationNavigatorService {
	//评议员类型
	public final static int APPRAISER_TYPE_BASIC = 0; //基础库评议员
	public final static int APPRAISER_TYPE_RECIPIENT = 1; //管理服务对象
	public final static int APPRAISER_TYPE_DELEGATE = 2; //评议代表
	
	//评议员状态
	public final static int APPRAISER_STATUS_TOAPPROVAL = 0; //待审核
	public final static int APPRAISER_STATUS_ENABLED = 1; //已启用
	public final static int APPRAISER_STATUS_DISABLED = 2; //已过期
	
	/**
	 * 评议员导入
	 * @param importId
	 * @param orgId
	 * @param expire
	 * @param appraiserType
	 * @param appraiserStatus
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public List importAppraisers(long importId, long orgId, Date expire, int appraiserType, int appraiserStatus, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 将指定组织的评议员设为无效
	 * @param orgId
	 * @param appraiserType
	 * @throws ServiceException
	 */
	public void setAppraisersDisabled(long orgId, int appraiserType) throws ServiceException;
	
	/**
	 * 完成服务对象导入
	 * @param appraiserImport
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeImportRecipients(AppraiserImport appraiserImport, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取导入的评议员
	 * @param importId
	 * @throws ServiceException
	 */
	public List listImportedRecipients(long importId) throws ServiceException;
	
	/**
	 * 获取组织机构的评议员
	 * @param orgId
	 * @param year
	 * @param month
	 * @param appraiserType 评议员类型,基础库评议员/0,管理服务对象/1
	 * @param appraiserJobs 评议员身份,空不限制
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listOrgAppraisers(long orgId, int year, int month, int appraiserType, String appraiserJobs, int offset, int limit) throws ServiceException;
}
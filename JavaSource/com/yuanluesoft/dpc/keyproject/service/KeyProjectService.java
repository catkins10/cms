package com.yuanluesoft.dpc.keyproject.service;

import java.util.List;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProject;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectParameter;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 重点项目服务
 * @author linchuan
 *
 */
public interface KeyProjectService extends BusinessService {
	
	/**
	 * 更新项目所在地
	 * @param keyProject
	 * @param area
	 * @throws ServiceException
	 */
	public void updateProjectArea(KeyProject keyProject, String area) throws ServiceException;

	/**
	 * 把项目列入重点项目
	 * @param keyProject
	 * @param isKeyProject
	 * @throws ServiceException
	 */
	public void setAsKeyProject(KeyProject keyProject, boolean isKeyProject) throws ServiceException;
	
	/**
	 * 完成项目汇报的审批
	 * @param keyProject
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeApprovalDebrief(KeyProject keyProject, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 项目申报
	 * @param keyProject
	 * @param declareYear
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void declareProject(KeyProject keyProject, int declareYear, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取重点项目参数配置
	 * @return
	 * @throws ServiceException
	 */
	public KeyProjectParameter getKeyProjectParameter() throws ServiceException;
	
	/**
	 * 获取行业列表
	 * @return
	 * @throws ServiceException
	 */
	public List listKeyProjectIndustries() throws ServiceException;
	
	/**
	 * 获取资金来源列表
	 * @return
	 * @throws ServiceException
	 */
	public List listKeyProjectInvestSources() throws ServiceException;
	
	/**
	 * 获取开发区分类列表
	 * @return
	 * @throws ServiceException
	 */
	public List listDevelopmentAreaCategories() throws ServiceException;
	
	/**
	 * 汇报是否需要审核
	 * @return
	 */
	public boolean isApprovalDebrief();
	
	/**
	 * 
	 * @return
	 */
	public String getComponentNames();
}
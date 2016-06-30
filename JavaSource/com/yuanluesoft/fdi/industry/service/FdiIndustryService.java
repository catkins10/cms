package com.yuanluesoft.fdi.industry.service;

import java.util.List;

import com.yuanluesoft.fdi.industry.pojo.FdiIndustry;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;

/**
 * 
 * @author linchuan
 *
 */
public interface FdiIndustryService extends BusinessService {

	/**
	 * 获取行业分类
	 * @param industryId
	 * @return
	 * @throws ServiceException
	 */
	public FdiIndustry getIndustry(long industryId) throws ServiceException;
	
	/**
	 * 获取用户对行业的权限
	 * @param industryIds
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public char getIndustryAccessLevel(String industryIds, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建行业树
	 * @param editabled 输出有编辑权限的分类
	 * @param readabled 输出有查询权限的分类
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Tree createIndustryTree(boolean editabled, boolean readabled, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取用户有权限的行业ID列表
	 * @param editabled 是否有编辑权限
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listIndustryIds(boolean editabled, SessionInfo sessionInfo) throws ServiceException;
}
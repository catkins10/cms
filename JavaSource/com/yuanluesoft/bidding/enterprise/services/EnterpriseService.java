package com.yuanluesoft.bidding.enterprise.services;

import java.util.List;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanlue
 *
 */
public interface EnterpriseService extends BusinessService {
	
	/**
	 * 获取企业记录
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public BiddingEnterprise getEnterprise(long id) throws ServiceException;

	/**
	 * 获取分类列表,如:招标代理,施工企业
	 * @return
	 * @throws ServiceException
	 */
	public List listEnterpriseCategories() throws ServiceException;
	
	/**
	 * 获取代理名录库列表
	 * @return
	 * @throws ServiceException
	 */
	public List listAgentLibs() throws ServiceException;
	
	/**
	 * 获取企业所在区域列表
	 * @param enterpriseType 指定企业分类,不指定时获取全部
	 * @return
	 * @throws ServiceException
	 */
	public List listEnterpriseAreas(String enterpriseType) throws ServiceException;
	
	/**
	 * 获取企业列表
	 * @param enterpriseType
	 * @param area
	 * @return
	 * @throws ServiceException
	 */
	public List listEnterprises(String enterpriseType, String area) throws ServiceException;
	
	/**
	 * 完成登记
	 * @param enterprise
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeRegist(BiddingEnterprise enterprise, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建一个企业变更记录
	 * @param enterpriseId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public BiddingEnterprise createAlter(long enterpriseId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 完成企业变更
	 * @param enterpriseAlter
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeAlter(BiddingEnterprise enterpriseAlter, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建一个企业注销记录
	 * @param enterpriseId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public BiddingEnterprise createNullify(long enterpriseId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 完成企业注销
	 * @param enterpriseNullify
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void completeNullify(BiddingEnterprise enterpriseNullify, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 导入企业库
	 * @param importId
	 * @throws ServiceException
	 */
	public void importBidEnterprises(long importId) throws ServiceException;
	
	/**
	 * 增加投标企业
	 * @param account
	 * @param accountName
	 * @param bank
	 * @throws ServiceException
	 */
	public void addBidEnterprise(String account, String accountName, String bank) throws ServiceException;
	
	/**
	 * 查找投标企业
	 * @param account
	 * @param accountName
	 * @throws ServiceException
	 */
	public List findBidEnterprise(String account, String accountName) throws ServiceException;
	
	/**
	 * 检查企业是否有效
	 * @param enterpriseId
	 * @return
	 */
	public boolean isEnterpriseValid(long enterpriseId);
}
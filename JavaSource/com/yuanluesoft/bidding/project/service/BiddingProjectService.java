package com.yuanluesoft.bidding.project.service;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectBidopening;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectComponent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectNotice;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPitchon;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanlue
 *
 */
public interface BiddingProjectService extends BusinessService {
	
	/**
	 * 获取工程
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public BiddingProject getProject(long id) throws ServiceException;
	
	/**
	 * 保存工程组成部分
	 * @param projectId
	 * @param projectName
	 * @param biddingMode 招标方式
	 * @param component
	 * @throws ServiceException
	 */
	public void saveProjectComponent(long projectId, String projectName, String biddingMode, BiddingProjectComponent component) throws ServiceException;
	
	/**
	 * 删除工程组成部分
	 * @param component
	 * @throws ServiceException
	 */
	public void removeProjectComponent(BiddingProjectComponent component) throws ServiceException;
	
	/**
	 * 发布工程组成部分
	 * @param component
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void publicProjectComponent(BiddingProjectComponent component, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 上传招标文件
	 * @param project
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void uploadBiddingDocument(BiddingProject project, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 发送招标文件到评标系统
	 * @param project
	 * @return
	 * @throws ServiceException
	 */
	public void sendBiddingDocumentToEvaluateSystem(BiddingProject project) throws ServiceException;
	
	/**
	 * 同步更新开标结果
	 * @throws ServiceException
	 */
	public BiddingProjectBidopening synchBidopening(BiddingProject project) throws ServiceException;
	
	/**
	 * 同步更新中标结果
	 * @param project
	 * @throws ServiceException
	 */
	public BiddingProjectPitchon synchPitchon(BiddingProject project) throws ServiceException;
	
	/**
	 * 生成中标通知书
	 * @param project
	 * @return
	 * @throws ServiceException
	 */
	public BiddingProjectNotice generateNotice(BiddingProject project) throws ServiceException;
		
	/**
	 * 新增或更新建设单位
	 * @param owner
	 * @param ownerType
	 * @param ownerRepresentative
	 * @param ownerLinkman
	 * @param ownerLinkmanIdCard
	 * @param ownerTel
	 * @param ownerFax
	 * @param ownerMail
	 * @throws ServiceException
	 */
	public void saveOrUpdateOwner(String owner, String ownerType, String ownerRepresentative, String ownerLinkman, String ownerLinkmanIdCard, String ownerTel, String ownerFax, String ownerMail) throws ServiceException;
	
	/**
	 * 报报名列表传送到标书服务器
	 * @throws ServiceException
	 */
	public void exportSignUps() throws ServiceException;
	
	/**
	 * 自动发布开标公示
	 * @throws ServiceException
	 */
	public void publicBidopenings() throws ServiceException;
	
	/**
	 * 检查开标项目是否超出开标室的数量
	 * @param projectPlan
	 * @param projectCity
	 * @return
	 * @throws ServiceException
	 */
	public boolean isBidopeningProjectOverflow(BiddingProjectPlan projectPlan, String projectCity) throws ServiceException;
	
	/**
	 * 发送投标文件到开标系统
	 * @param signUp
	 * @throws ServiceException
	 */
	public void sendBidToBiddingSystem(BiddingSignUp signUp) throws ServiceException;
	
	/**
	 * 是否实名报名
	 * @param projectId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isRealNameSignUp(long projectId) throws ServiceException;
}
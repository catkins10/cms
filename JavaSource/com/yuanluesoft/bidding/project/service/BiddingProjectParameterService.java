package com.yuanluesoft.bidding.project.service;

import java.util.List;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectCity;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectParameter;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author yuanlue
 *
 */
public interface BiddingProjectParameterService extends BusinessService, WorkflowConfigureCallback {
	public static final char BIDDING_CITY_MANAGER = '4'; //管理员
	public static final char BIDDING_CITY_REPORT_VISITOR = '3'; //报表查看人员
	public static final char BIDDING_CITY_PROJECT_CREATOR = '2'; //项目登记人员
	public static final char BIDDING_CITY_PROJECT_APPROVER = '1'; //项目审核人员
	
	/**
	 * 生成项目招标编号
	 * @param project
	 * @param preview
	 * @return
	 * @throws ServiceException
	 */
	public String generateBiddingNumber(BiddingProject project, boolean preview) throws ServiceException;
	
	/**
	 * 生成报名号
	 * @param project
	 * @param preview
	 * @return
	 * @throws ServiceException
	 */
	public String generateSignUpNumber(BiddingProject project, boolean preview) throws ServiceException;
	
	/**
	 * 生成报建收件编号
	 * @param project
	 * @param preview
	 * @return
	 * @throws ServiceException
	 */
	public String generateDeclareReceiveNumber(BiddingProject project, boolean preview) throws ServiceException;
	
	/**
	 * 生成报建编号
	 * @param project
	 * @param preview
	 * @return
	 * @throws ServiceException
	 */
	public String generateDeclareNumber(BiddingProject project, boolean preview) throws ServiceException;
	
	/**
	 * 生成中标通知书编号
	 * @param project
	 * @param preview
	 * @return
	 * @throws ServiceException
	 */
	public String generateNoticeNumber(BiddingProject project, boolean preview) throws ServiceException;
	
	/**
	 * 获取工程类别对应的资料列表
	 * @param projectCategory
	 * @param projectProcedure
	 * @param projectCity
	 * @param fileType
	 * @return
	 * @throws ServiceException
	 */
	public List listProjectFileItems(String projectCategory, String projectProcedure, String projectCity, String fileType) throws ServiceException;
	
	/**
	 * 是否要求资料完整才能继续流转
	 * @param projectCategory
	 * @param projectProcedure
	 * @param projectCity
	 * @param fileType
	 * @return
	 * @throws ServiceException
	 */
	public boolean isNeedFullProjectFiles(String projectCategory, String projectProcedure, String projectCity, String fileType) throws ServiceException;
	
	/**
	 * 添加资料配置
	 * @param projectFileConfigId
	 * @param fileSn
	 * @param fileType
	 * @param fileName
	 * @param fileCategory
	 * @param remark
	 * @throws ServiceException
	 */
	public void addProjectFileItem(long projectFileConfigId, double fileSn, String fileType, String fileName, String fileCategory, String remark) throws ServiceException;
	
	/**
	 * 删除资料配置
	 * @param fileId
	 * @throws ServiceException
	 */
	public void deleteProjectFileItem(long fileItemId) throws ServiceException;
	
	/**
	 * 获取地区详细配置
	 * @param area
	 * @return
	 * @throws ServiceException
	 */
	public BiddingProjectCity getCityDetail(String city) throws ServiceException;
	
	/**
	 * 按项目ID获取地区详细配置
	 * @param projectId
	 * @return
	 * @throws ServiceException
	 */
	public BiddingProjectCity getCityDetail(long projectId) throws ServiceException;
	
	/**
	 * 获取参数配置
	 * @param projectCategory 项目分类
	 * @param projectProcedure 招标内容
	 * @param projectCity 项目所在地区
	 * @return
	 * @throws ServiceException
	 */
	public BiddingProjectParameter getParameter(String projectCategory, String projectProcedure, String projectCity) throws ServiceException;
	
	/**
	 * 获取流程名称
	 * @param projectCategory
	 * @param projectProcedure
	 * @param projectCity
	 * @return
	 * @throws ServiceException
	 */
	public long getWorkflowId(String projectCategory, String projectProcedure, String projectCity) throws ServiceException;
	
	/**
	 * 获取用户允许登记项目的地区
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listProjectCreatableCities(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取用户有报表查看权限的地区
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listReportVisitableCities(SessionInfo sessionInfo) throws ServiceException;
	
	
	/**
	 * 是否通过网络支付保证金
	 * @param projectId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isPledgeInternetPayment(long projectId) throws ServiceException;
	
	/**
	 * 获取区域英文名称
	 * @param cityName
	 * @return
	 * @throws ServiceException
	 */
	public String getCityEnglishName(String cityName) throws ServiceException;
	
	/**
	 * 获取工程类别英文名称
	 * @param projectCategory
	 * @return
	 * @throws ServiceException
	 */
	public String getProjectCategoryEnglishName(String projectCategory) throws ServiceException;
	
	/**
	 * 获取招标内容英文名称
	 * @param projectProcedure
	 * @return
	 * @throws ServiceException
	 */
	public String getProjectProcedureEnglishName(String projectProcedure) throws ServiceException;
	
	/**
	 * 是否实名报名
	 * @param city
	 * @param projectCategory
	 * @param projectProcedure
	 * @return
	 * @throws ServiceException
	 */
	public boolean isRealNameSignUp(String city, String projectCategory, String projectProcedure) throws ServiceException;
}
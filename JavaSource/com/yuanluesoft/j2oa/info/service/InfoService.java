package com.yuanluesoft.j2oa.info.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.info.pojo.InfoFilter;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazine;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine;
import com.yuanluesoft.j2oa.info.pojo.InfoReceive;
import com.yuanluesoft.j2oa.info.pojo.InfoWorkflow;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 信息采编服务
 * @author linchuan
 *
 */
public interface InfoService extends BusinessService, WorkflowConfigureCallback {
	//信息状态
	public static final int INFO_STATUS_TO_APPROVAL = 0; //审核中
	public static final int INFO_STATUS_NO_USE = 1; //未采用
	public static final int INFO_STATUS_TO_TYPESET = 2; //待排版
	public static final int INFO_STATUS_TYPESET = 3; //已排版
	public static final int INFO_STATUS_ISSUE = 4; //已定版
	
	//流程类型
	public static final int WORKFLOW_TYPE_INFO = 1; //稿件
	public static final int WORKFLOW_TYPE_MAGAZINE = 2; //刊物

	/**
	 * 同步投稿
	 * @throws ServiceException
	 */
	public void synchContributeInfos() throws ServiceException;
	
	/**
	 * 获取流程配置
	 * @param type 1/稿件,2/刊物
	 * @return
	 * @throws ServiceException
	 */
	public InfoWorkflow getInfoWorkflow(int type) throws ServiceException;
	
	/**
	 * 信息过滤
	 * @param infoReceive
	 * @param magazineIds
	 * @param level
	 * @param isBrief
	 * @param isVerified
	 * @param isCircular
	 * @param opinion
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void filterInfo(InfoReceive infoReceive, String[] magazineIds, String level, int isBrief, int isVerified, int isCircular, String opinion, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取上一个接收到的信息ID
	 * @param infoReceive
	 * @param toFilterOnly
	 * @return
	 * @throws ServiceException
	 */
	public long getPreviousReceiveInfoId(InfoReceive infoReceive, boolean toFilterOnly) throws ServiceException;
	
	/**
	 * 获取下一个接收到的信息ID
	 * @param infoReceive
	 * @param toFilterOnly
	 * @return
	 * @throws ServiceException
	 */
	public long getNextInfoId(InfoReceive infoReceive, boolean toFilterOnly) throws ServiceException;
	
	/**
	 * 获取来稿正文
	 * @param infoReceiveId
	 * @return
	 * @throws ServiceException
	 */
	public String getReceiveInfoBody(long infoReceiveId) throws ServiceException;
	
	/**
	 * 获取稿件正文
	 * @param infoFilterId
	 * @return
	 * @throws ServiceException
	 */
	public String getFilterInfoBody(long infoFilterId) throws ServiceException;
	
	/**
	 * 录用稿件
	 * @param info
	 * @throws ServiceException
	 */
	public void useInfo(InfoFilter info) throws ServiceException;
	
	/**
	 * 获取用户任编辑的刊物
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listEditableMagazineDefines(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 保存刊物使用的稿件
	 * @param magazine
	 * @param magazineColumn
	 * @param magazineUseInfoIds
	 * @throws ServiceException
	 */
	public void saveMagazineUseInfos(InfoMagazine magazine, String magazineColumn, String magazineUseInfoIds) throws ServiceException;
	
	/**
	 * 获取待排版稿件列表
	 * @param magazineDefineId
	 * @param isBrief
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listToTypesetInfos(long magazineDefineId, boolean isBrief, int limit) throws ServiceException;
	
	/**
	 * 保存刊物正文
	 * @param magazine
	 * @param request
	 * @throws ServiceException
	 */
	public void saveMagazineBody(InfoMagazine magazine, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 定版
	 * @param magazine
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issueMagazine(InfoMagazine magazine, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取退改稿人电话
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public String getRevisePersonTel(long userId) throws ServiceException;
	
	/**
	 * 合并信息
	 * @param infoIds
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public InfoFilter combineInfos(String infoIds, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 取消信息合并
	 * @param infoIds
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void uncombineInfos(String infoIds, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取信息列表
	 * @param infoIds
	 * @return
	 * @throws ServiceException
	 */
	public List listInfos(String infoIds) throws ServiceException;
	
	/**
	 * 检查用户是否某个刊物的编辑或者主编
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean isMagazineEditor(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 按刊物定义ID获取模板
	 * @param templateId
	 * @return
	 * @throws ServiceException
	 */
	public Attachment getWordTemplate(long magazineDefineId) throws ServiceException;
	
	/**
	 * 保存刊物模板
	 * @param magazineDefine
	 * @param request
	 * @throws ServiceException
	 */
	public void saveWordTemplate(InfoMagazineDefine magazineDefine, HttpServletRequest request) throws ServiceException;
}
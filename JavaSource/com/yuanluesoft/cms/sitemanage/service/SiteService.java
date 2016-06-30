/*
 * Created on 2007-7-1
 *
 */
package com.yuanluesoft.cms.sitemanage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.pojo.Headline;
import com.yuanluesoft.cms.sitemanage.pojo.WebColumn;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.formula.service.FormulaSupport;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.services.InitializeService;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author linchuan
 * 
 */
public interface SiteService extends DirectoryService, InitializeService, FormulaSupport, WorkflowConfigureCallback {

	/**
	 * 获取栏目所在站点,如果栏目本身就是站点,则返回栏目自己
	 * @param columnId
	 * @return
	 * @throws ServiceException
	 */
	public WebSite getParentSite(long columnId) throws ServiceException;

	/**
	 * 获取站点信息审批流程ID
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public long getApprovalWorkflowId(long siteId) throws ServiceException;
	
	/**
	 * 保存栏目同步设置
	 * @param directory
	 * @param isNewDirectory
	 * @param synchToDirectoryIds
	 * @param synchFromDirectoryIds
	 * @throws ServiceException
	 */
	public void saveSynchDirectories(WebDirectory directory, boolean isNewDirectory, String synchToDirectoryIds, String synchFromDirectoryIds) throws ServiceException;
	
	/**
	 * 获取栏目需要同步的其他栏目ID列表，包括栏目自己、以及需要同步的所有栏目
	 * @param columnIds
	 * @return
	 * @throws ServiceException
	 */
	public List listSynchColumnIds(String columnIds) throws ServiceException;
	
	/**
	 * 获取用户有管理权限的第一个站点
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public WebSite getFirstManagedSite(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取用户有编辑权限的第一个站点
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public WebSite getFirstEditabledSite(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取水印
	 * @param columnId
	 * @return
	 * @throws ServiceException
	 */
	public WaterMark getWaterMark(long columnId) throws ServiceException;
	
	/**
	 * 获取头版头条
	 * @param webDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public Headline getHeadline(long webDirectoryId) throws ServiceException;
	
	/**
	 * 设置头版头条
	 * @param webDirectoryId
	 * @param headlineName
	 * @param headlineURL
	 * @param summarize
	 * @throws ServiceException
	 */
	public void setHeadline(long webDirectoryId, String headlineName, String headlineURL, String summarize) throws ServiceException;
	
	/**
	 * 是否允许编辑删除文章
	 * @param webDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isEditorDeleteable(long webDirectoryId) throws ServiceException;
	
	/**
	 * 是否允许编辑撤销文章并重发布
	 * @param webDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isEditorReissueable(long webDirectoryId) throws ServiceException;
	
	/**
	 * 根据栏目ID获取用户有撤销发布权限的栏目或站点,含父栏目/站点
	 * @param columnIds
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listReissueableDirectories(String columnIds, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取匿名用户的站点资源访问级别
	 * @param webDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public char getAnonymousLevel(long webDirectoryId) throws ServiceException;
	
	/**
	 * 获取同步的文章发布选项
	 * @param webDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public char getSynchIssueOption(long webDirectoryId) throws ServiceException;
	
	/**
	 * 获取站点/栏目自己的管理员列表
	 * @param siteId
	 * @param myManagersOnly 仅自己的管理员列表
	 * @param asPerson true:转换为用户(Person)列表,包括部门、角色成员, false:返回目录权限(WebDirectoryPopedom)列表
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listSiteManagers(long siteId, boolean myManagersOnly, boolean asPerson, int max) throws ServiceException;

	/**
	 * 获取站点/栏目编辑列表
	 * @param siteId
	 * @param myEditorsOnly 仅自己的编辑列表
	 * @param asPerson true: 转换为用户(Person)列表,包括部门、角色成员, false:返回目录权限(WebDirectoryPopedom)列表
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listSiteEditors(long siteId, boolean myEditorsOnly, boolean asPerson, int max) throws ServiceException;
	
	/**
	 * 获取用户最近使用的栏目列表,并转换为树节点
	 * @param extendPropertyNames
	 * @param sessionInfo
	 * @param maxColumn
	 * @return
	 * @throws ServiceException
	 */
	public TreeNode createRecentUsenTreeNode(String extendPropertyNames, SessionInfo sessionInfo, int maxColumn) throws ServiceException;
	
	/**
	 * 更新主机名页面映射表
	 * @throws Exception
	 */
	public void updateHostPageMapping();
	
	/**
	 * 下载视频播放器LOGO
	 * @param siteId
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void downloadVideoPlayerLogo(long siteId, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 添加相关栏目
	 * @param webColumn
	 * @param relationColumnIds
	 * @throws ServiceException
	 */
	public void addRelationColumns(WebColumn webColumn, String relationColumnIds) throws ServiceException;
	
	/**
	 * 根据隶属单位获取站点
	 * @param ownerUnitId
	 * @return
	 * @throws ServiceException
	 */
	public WebSite getSiteByOwnerUnitId(long ownerUnitId) throws ServiceException;
	
	/**
	 * 主机名称校验,检查页面是否属于主机所指定的站点
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public boolean validateHostName(HttpServletRequest request) throws ServiceException;
}
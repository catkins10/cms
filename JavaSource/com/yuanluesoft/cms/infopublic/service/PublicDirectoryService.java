package com.yuanluesoft.cms.infopublic.service;

import java.util.List;

import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicGuide;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.services.InitializeService;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author yuanluesoft
 *
 */
public interface PublicDirectoryService extends DirectoryService, InitializeService, WorkflowConfigureCallback {

	/**
	 * 更新信息公开指南
	 * @param directoryId
	 * @param guide
	 * @throws ServiceException
	 */
	public void updateGuide(long directoryId, String guide) throws ServiceException;
	
	/**
	 * 获取目录需要同步的站点列表
	 * @param directoryId
	 * @param inherit 是否继承父目录设置的站点列表
	 * @return
	 * @throws ServiceException
	 */
	public String getDirectorySynchSiteIds(long directoryId, boolean inherit) throws ServiceException;
	
	/**
	 * 获取信息发布流程ID
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public long getApprovalWorkflowId(long directoryId) throws ServiceException;
	
	/**
	 * 获取目录隶属的主目录
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public PublicMainDirectory getMainDirectory(long directoryId) throws ServiceException;
	
	/**
	 * 获取站点关联的信息公开主目录
	 * @param siteId
	 * @return
	 */
	public PublicMainDirectory getMainDirectoryBySite(long siteId) throws ServiceException;
	
	/**
	 * 获取主目录自己的子目录,不允许是下级主目录的子目录
	 * @param mainDirectoryId
	 * @param directoryName
	 * @return
	 * @throws ServiceException
	 */
	public PublicDirectory getPublicDirectoryByName(long mainDirectoryId, String directoryName) throws ServiceException;
	
	/**
	 * 获取信息公开指南
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public PublicGuide getPublicGuide(long directoryId) throws ServiceException;
	
	/**
	 * 是否允许编辑撤销文章并重发布
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isEditorReissueable(long directoryId) throws ServiceException;
	
	/**
	 * 是否允许编辑删除文章
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isEditorDeleteable(long directoryId) throws ServiceException;

	/**
	 * 获取目录的管理员列表
	 * @param siteId
	 * @param myManagersOnly 仅自己的管理员列表
	 * @param asPerson true:转换为用户(Person)列表,包括部门、角色成员, false:返回目录权限(PublicDirectoryPopedom)列表
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listDirectoryManagers(long directoryId, boolean myManagersOnly, boolean asPerson, int max) throws ServiceException;

	/**
	 * 获取目录的编辑列表
	 * @param siteId
	 * @param myEditorsOnly 仅自己的编辑列表
	 * @param asPerson true:转换为用户(Person)列表,包括部门、角色成员, false:返回目录权限(PublicDirectoryPopedom)列表
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listDirectoryEditors(long directoryId, boolean myEditorsOnly, boolean asPerson, int max) throws ServiceException;
	
	/**
	 * 获取用户最近使用的栏目列表,并转换为树节点
	 * @param extendPropertyNames
	 * @param sessionInfo
	 * @param maxDirectory
	 * @return
	 * @throws ServiceException
	 */
	public TreeNode createRecentUsenTreeNode(String extendPropertyNames, SessionInfo sessionInfo, int maxDirectory) throws ServiceException;
}
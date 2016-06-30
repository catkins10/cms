package com.yuanluesoft.jeaf.directorymanage.service;

import java.util.List;

import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;

/**
 * 目录服务
 * @author linchuan
 *
 */
public interface DirectoryService extends BusinessService, ApplicationNavigatorService {
	//目录操作
	public static final String DIRECTORY_ACTION_REMOVE_CHILD = "removeChildDirectory"; //删除了一个子目录
	public static final String DIRECTORY_ACTION_MOVE_CHILD = "moveChildDirectory"; //移动一个子目录到当前目录
	public static final String DIRECTORY_ACTION_ADD_CHILD = "addChildDirectory"; //添加一个子目录到当前目录
	
	/**
	 * 获取目录名称
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public Directory getDirectory(long directoryId) throws ServiceException;
	
	/**
	 * 创建一个目录
	 * @param directory
	 * @return
	 * @throws ServiceException
	 */
	public Directory createDirectory(Directory directory) throws ServiceException;
	
	/**
	 * 按名称创建一个目录,如果已经存在则直接返回该目录
	 * @param directoryId 目录ID,等于-1时自动生成
	 * @param parentDirectoryId
	 * @param directoryName
	 * @param directoryType
	 * @param remark
	 * @param creatorId
	 * @param creatorName
	 * @return
	 * @throws ServiceException
	 */
	public Directory createDirectory(long directoryId, long parentDirectoryId, String directoryName, String directoryType, String remark, long creatorId, String creatorName) throws ServiceException;
	
	/**
	 * 获取目录类型名称
	 * @param directoryType
	 * @return
	 */
	public String getDirectoryTypeTitle(String directoryType) throws ServiceException;
	
	/**
	 * 目录拷贝
	 * @param directoryId
	 * @param toDirectoryId
	 * @param newDirectoryName
	 * @param userName
	 * @param userId
	 * @throws ServiceException
	 */
	public Directory copyDirectory(long directoryId, long toDirectoryId, String newDirectoryName, String userName, long userId) throws ServiceException;
	
	/**
	 * 获取权限配置列表
	 * @param directory 如果是新纪录,directory=null
	 * @param directoryClassName
	 * @return
	 * @throws ServiceException
	 */
	public List getDirectoryPopedomConfigs(Directory directory, String directoryClassName) throws ServiceException;
	
	/**
	 * 保存权限配置,如果权限有变化,返回true
	 * @param directory
	 * @param directoryPopedomConfigs
	 * @param isNewDirectory
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean saveDirectoryPopedomConfigs(Directory directory, List directoryPopedomConfigs, boolean isNewDirectory, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 授权,如果权限有变化,返回true
	 * @param directory
	 * @param isNewDirectory
	 * @param popedomName
	 * @param userIds
	 * @param userNames
	 * @param keepMyselfPopedom
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean authorize(Directory directory, boolean isNewDirectory, String popedomName, String userIds, String userNames, boolean keepMyselfPopedom, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 更新引用的目录
	 * @param directoryId
	 * @param linkedDirectoryIds
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void updateLinkedDirectories(long directoryId, String linkedDirectoryIds, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取目录列表
	 * @param directoryIds
	 * @return
	 * @throws ServiceException
	 */
	public List listDirectories(String directoryIds) throws ServiceException;
	
	/**
	 * 获取下级目录列表
	 * @param directoryId
	 * @param directoryTypeFilters 目录类型列表,用逗号分隔,null或者all表示全部
	 * @param popedomFilters 需要过滤的权限类型列表,用逗号分隔, null:不过滤, all:有任何一种权限都可以, 其他:需要有指定的权限
	 * @param otherFilter 其它过滤条件
	 * @param keepDirectoryLink 是否保留目录引用
	 * @param readLazyLoadProperties 是否读取延迟加载的属性
	 * @param sessionInfo
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listChildDirectories(long directoryId, String directoryTypeFilters, String popedomFilters, String otherFilter, boolean keepDirectoryLink, boolean readLazyLoadProperties, SessionInfo sessionInfo, int offset, int limit) throws ServiceException;
	
	/**
	 * 获取目录ID列表
	 * @param directoryTypeFilters
	 * @param popedomFilters
	 * @param popedomInheritDisabled 不输出继承的权限
	 * @param sessionInfo
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listDirectoryIds(String directoryTypeFilters, String popedomFilters, boolean popedomInheritDisabled, SessionInfo sessionInfo, int offset, int limit) throws ServiceException;
	
	/**
	 * 按名称获取子目录列表,如果当前目录名称也在名称列表中,也一并返回
	 * @param parentDirectoryId
	 * @param names
	 * @return
	 * @throws ServiceException
	 */
	public List listChildDirectoriesByName(long parentDirectoryId, String names) throws ServiceException;

	/**
	 * 是否有子目录
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public boolean hasChildDirectories(long directoryId) throws ServiceException;

	/**
	 * 获取上级目录列表,从高到低,不包含当前目录
	 * @param directoryId
	 * @param traceToDirectoryType 上溯到指定的目录类型
	 * @return
	 * @throws ServiceException
	 */
	public List listParentDirectories(long directoryId, String traceToDirectoryType) throws ServiceException;
	
	/**
	 * 获取指定类型的父目录
	 * @param directoryId
	 * @param directoryTypes
	 * @return
	 * @throws ServiceException
	 */
	public Directory getParentDirectory(long directoryId, String directoryTypes) throws ServiceException;
	
	/**
	 * 判断是否是子目录
	 * @param childDirectoryId
	 * @param parentDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isChildDirectory(long childDirectoryId, long parentDirectoryId) throws ServiceException;
	
	/**
	 * 筛选出隶属于父目录的子目录ID列表
	 * @param childDirectoryIds
	 * @param parentDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public String filterChildDirectoryIds(String childDirectoryIds, long parentDirectoryId) throws ServiceException;
	
	/**
	 * 获取目录名称
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public String getDirectoryName(long directoryId) throws ServiceException;
	
	/**
	 * 获取目录全名
	 * @param directoryId
	 * @param delimiter
	 * @param traceToDirectoryTypes 上溯到指定的目录类型列表
	 * @return
	 * @throws ServiceException
	 */
	public String getDirectoryFullName(long directoryId, String delimiter, String traceToDirectoryTypes) throws ServiceException;
	
	/**
	 * 获取目录名称列表
	 * @param directoryIds
	 * @param delimiter
	 * @param namesDelimiter
	 * @param traceToDirectoryTypes 上溯到指定的目录类型列表
	 * @return
	 * @throws ServiceException
	 */
	public String getDirectoryFullNames(String directoryIds, String delimiter, String namesDelimiter, String traceToDirectoryTypes) throws ServiceException;
	
	/**
	 * 获取用户对目录的权限,没有权限返回null
	 * @param directoryId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listDirectoryPopedoms(long directoryId, SessionInfo sessionInfo);
	
	/**
	 * 获取用户对下级目录的权限,用逗号分隔,没有权限返回null
	 * @param directoryId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listChildDirectoryPopedoms(long directoryId, SessionInfo sessionInfo);
	
	/**
	 * 获取目录访问者
	 * @param directoryId
	 * @param popedomName
	 * @param myVisitorsOnly 只获取自己的访问者,如果没有,从上级获取
	 * @param asPerson true:转换为用户(Person)列表,包括部门、角色成员, false:返回目录权限(DirectoryPopedom)列表
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listDirectoryVisitors(long directoryId, String popedomName, boolean myVisitorsOnly, boolean asPerson, int max) throws ServiceException;
	
	/**
	 * 检查用户是否有指定的权限
	 * @param directoryId
	 * @param popedomNames all表示任意一种权限
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean checkPopedom(long directoryId, String popedomNames, SessionInfo sessionInfo);
	
	/**
	 * 检查用户是否有当前目录或者上级目录的指定权限
	 * @param directoryId
	 * @param popedomNames
	 * @param sessionInfo
	 * @return
	 */
	public boolean checkParentDirectoryPopedom(long directoryId, String popedomNames, SessionInfo sessionInfo);
		
	/**
	 * 创建树
	 * @param rootDirectoryId 指定根目录ID
	 * @param directoryTypeFilters 目录类型列表,用逗号分隔,null或者all表示全部
	 * @param popedomFilters 需要过滤的权限类型列表,用逗号分隔, null:不过滤, all:有任何一种权限都可以, 其他:需要有指定的权限
	 * @param otherFilters 其它过滤条件HQL
	 * @param extendPropertyNames 需要在节点显示的扩展属性名称列表
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Tree createDirectoryTree(long rootDirectoryId, String directoryTypeFilters, String popedomFilters, String otherFilters, String extendPropertyNames, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取树的子节点
	 * @param parentDirectoryId
	 * @param directoryTypeFilters 目录类型列表,用逗号分隔,null或者all表示全部
	 * @param popedomFilters 需要过滤的权限类型列表,用逗号分隔, null:不过滤, all:有任何一种权限都可以, 其他:需要有指定的权限
	 * @param otherFilters 其它过滤条件HQL
	 * @param extendPropertyNames 需要在节点显示的扩展属性名称列表
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listChildTreeNodes(long parentDirectoryId, String directoryTypeFilters, String popedomFilters, String otherFilters, String extendPropertyNames, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 根据目录全称获取目录
	 * @param fullName
	 * @param delimiter
	 * @return
	 * @throws ServiceException
	 */
	public Directory getDirectoryByFullName(String fullName, String delimiter) throws ServiceException;
	
	/**
	 * 按目录名称获取目录
	 * @param directoryName
	 * @param sonDirectoryOnly
	 * @param parentId
	 * @return
	 * @throws ServiceException
	 */
	public Directory getDirectoryByName(long parentDirectoryId, String directoryName, boolean sonDirectoryOnly) throws ServiceException;
	
	/**
	 * 按目录名称获取目录列表
	 * @param parentDirectoryId
	 * @param directoryNames
	 * @param sonDirectoryOnly
	 * @return
	 * @throws ServiceException
	 */
	public List listDirectoriesByNames(long parentDirectoryId, String directoryNames, boolean sonDirectoryOnly) throws ServiceException;
	
	/**
	 * 获取所有的子目录列表,包括下级,以及下级的下级
	 * @param directoryId
	 * @param childDirectoryTypes
	 * @param keepDirectoryLink 是否保留目录引用
	 * @return
	 * @throws ServiceException
	 */
	public List listAllChildDirectories(long directoryId, String childDirectoryTypes, boolean keepDirectoryLink) throws ServiceException;
	
	/**
	 * 获取第一级子目录ID列表,过滤掉类型不是childDirectoryTypes的子目录,如果当前的目录类型属于childDirectoryTypes,直接返回当前目录
	 * @param directoryIds
	 * @param childDirectoryTypes
	 * @return
	 */
	public String getChildDirectoryIds(String directoryIds, String childDirectoryTypes);
	
	/**
	 * 获取所有的子目录ID列表,包扩directoryIds本身
	 * @param directoryIds
	 * @param childDirectoryTypes
	 * @return
	 */
	public String getAllChildDirectoryIds(String directoryIds, String childDirectoryTypes);

	/**
	 * 按栏目名称获取对应的目录ID,如果父目录本身名称匹配,也返回其ID
	 * @param parentDirectoryId
	 * @param directoryNames
	 * @param childDirectoryTypes
	 * @return
	 * @throws ServiceException
	 */
	public List listDirectoryIdsByName(long parentDirectoryId, String directoryNames, String childDirectoryTypes) throws ServiceException;

	/**
	 * 检查目录是否存在
	 * @param directoryId
	 * @return
	 */
	public boolean isDirectoryExists(long directoryId);
	
	/**
	 * 重置目录类型列表,追加父类型
	 * @param directoryTypeFilters
	 * @return
	 */
	public String appendParentDirectoryTypes(String directoryTypes);
}
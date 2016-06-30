package com.yuanluesoft.cms.scene.service;

import java.util.List;

import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface SceneService extends BusinessService {

	/**
	 * 获取场景服务，注：不获取下级目录
	 * @param sceneServiceId
	 * @return
	 * @throws ServiceException
	 */
	public com.yuanluesoft.cms.scene.pojo.SceneService getSceneService(long sceneServiceId) throws ServiceException;
	
	/**
	 * 根据场景目录ID获取场景服务
	 * @param sceneDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public com.yuanluesoft.cms.scene.pojo.SceneService getSceneServiceByDirectoryId(long sceneDirectoryId) throws ServiceException;
	
	/**
	 * 获取场景目录
	 * @param sceneDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public SceneDirectory getSceneDirectory(long sceneDirectoryId) throws ServiceException;
	
	/**
	 * 目录拷贝
	 * @param fromDirectoryId
	 * @param toDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public SceneDirectory copySceneDirectory(long fromDirectoryId, long toDirectoryId) throws ServiceException;
	
	/**
	 * 按站点获取场景服务列表
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listSceneServices(long siteId) throws ServiceException;
	
	/**
	 * 获取下级场景目录列表
	 * @param parentDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	public List listChildSceneDirectories(long parentDirectoryId) throws ServiceException;
	
	/**
	 * 获取上级场景目录列表,从低到高,最后一个是场景服务
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public List listParentSceneDirectories(long directoryId) throws ServiceException;
}
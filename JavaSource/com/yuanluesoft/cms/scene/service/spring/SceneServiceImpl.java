package com.yuanluesoft.cms.scene.service.spring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.scene.pojo.SceneContent;
import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.cms.scene.pojo.SceneLink;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class SceneServiceImpl extends BusinessServiceImpl implements SceneService {
	private ExchangeClient exchangeClient; //数据交换服务
	private PageService pageService; //页面服务
	private TemplateService templateService; //模板服务,用来清理缓存的模板
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof SceneDirectory) {
			SceneDirectory sceneDirectory = (SceneDirectory)record;
			//新记录, 根据类名称设置目录类型
			if(sceneDirectory instanceof SceneContent) {
				sceneDirectory.setDirectoryType("content");
			}
			else if(sceneDirectory instanceof SceneLink) {
				sceneDirectory.setDirectoryType("link");
			}
			else {
				sceneDirectory.setDirectoryType("scene");
			}
			sceneDirectory.setPriority(generateSceneDirectoryPriority(sceneDirectory.getParentDirectoryId()));
		}
		exchangeClient.synchUpdate(record, null, 2000);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		templateService.clearCachedTemplate();
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		exchangeClient.synchUpdate(record, null, 2000);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		templateService.clearCachedTemplate();
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		//删除子目录
		Set childDirectories = null;
		if(record instanceof com.yuanluesoft.cms.scene.pojo.SceneService) { //场景服务
			com.yuanluesoft.cms.scene.pojo.SceneService sceneService = (com.yuanluesoft.cms.scene.pojo.SceneService)record;
			childDirectories = sceneService.getSceneDirectories();
		}
		else {
			SceneDirectory sceneDirectory = (SceneDirectory)record;
			childDirectories = sceneDirectory.getChildDirectories();
		}
		for(Iterator iterator = childDirectories==null ? null : childDirectories.iterator(); iterator!=null && iterator.hasNext();) {
			SceneDirectory childDirectory = (SceneDirectory)iterator.next();
			List children = getDatabaseService().findRecordsByHql("from SceneDirectory SceneDirectory where SceneDirectory.parentDirectoryId=" + childDirectory.getId());
			childDirectory.setChildDirectories(children==null || children.isEmpty() ? null : new HashSet(children));
			delete(childDirectory); //递归删除子目录
		}
		super.delete(record);
		//删除自己
		exchangeClient.synchDelete(record, null, 2000);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		templateService.clearCachedTemplate();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.service.SceneService#getSceneService(long)
	 */
	public com.yuanluesoft.cms.scene.pojo.SceneService getSceneService(long sceneServiceId) throws ServiceException {
		return (com.yuanluesoft.cms.scene.pojo.SceneService)load(com.yuanluesoft.cms.scene.pojo.SceneService.class, sceneServiceId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.service.SceneService#getSceneServiceByDirectoryId(long)
	 */
	public com.yuanluesoft.cms.scene.pojo.SceneService getSceneServiceByDirectoryId(long sceneDirectoryId) throws ServiceException {
		List parentDirectories = listParentSceneDirectories(sceneDirectoryId);
		return (com.yuanluesoft.cms.scene.pojo.SceneService)parentDirectories.get(parentDirectories.size() - 1);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.service.SceneService#getSceneDirectory(long)
	 */
	public SceneDirectory getSceneDirectory(long sceneDirectoryId) throws ServiceException {
		return (SceneDirectory)load(SceneDirectory.class, sceneDirectoryId);
	}
	
	/**
	 * 生成目录优先级
	 * @param parentDirectoryId
	 * @return
	 * @throws ServiceException
	 */
	private float generateSceneDirectoryPriority(long parentDirectoryId) throws ServiceException {
		//获取同级目录中最小优先级
		String hql = "select min(SceneDirectory.priority)" +
					 " from SceneDirectory SceneDirectory" +
					 " where SceneDirectory.parentDirectoryId=" + parentDirectoryId;
		Number priority = (Number)getDatabaseService().findRecordByHql(hql);
		//设置优先级
		if(priority==null || priority.intValue()==0) {
			return 50;
		}
		else {
			return priority.floatValue() - 0.1f;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.service.SceneService#copySceneDirectory(long, long)
	 */
	public SceneDirectory copySceneDirectory(long fromDirectoryId, long toDirectoryId) throws ServiceException {
		SceneDirectory fromSceneDirectory = getSceneDirectory(fromDirectoryId);
		try {
			return copySceneDirectory(fromSceneDirectory, toDirectoryId, true, new ArrayList());
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 拷贝目录
	 * @param fromSceneDirectory
	 * @param toDirectoryId
	 * @param resetPriority
	 * @param copiedDirectories 用来检查是否循环拷贝
	 * @return
	 * @throws Exception
	 */
	private SceneDirectory copySceneDirectory(SceneDirectory fromSceneDirectory, long toDirectoryId, boolean resetPriority, List copiedDirectories) throws Exception {
		SceneDirectory newSceneDirectory = (SceneDirectory)fromSceneDirectory.getClass().newInstance();
		PropertyUtils.copyProperties(newSceneDirectory, fromSceneDirectory);
		newSceneDirectory.setId(UUIDLongGenerator.generateId()); //ID
		newSceneDirectory.setParentDirectoryId(toDirectoryId); //父目录
		if(resetPriority) { //需要重设优先级
			newSceneDirectory.setPriority(generateSceneDirectoryPriority(toDirectoryId));
		}
		copiedDirectories.add(newSceneDirectory);
		save(newSceneDirectory);
		if(!fromSceneDirectory.getDirectoryType().equals("scene")) { //不是场景
			return newSceneDirectory;
		}
		//复制下级目录
		String hql = "from SceneDirectory SceneDirectory where SceneDirectory.parentDirectoryId=" + fromSceneDirectory.getId();
		List childDirectories = getDatabaseService().findRecordsByHql(hql);
		if(childDirectories==null) {
			return newSceneDirectory;
		}
		for(Iterator iterator = childDirectories.iterator(); iterator.hasNext();) {
			SceneDirectory sceneDirectory = (SceneDirectory)iterator.next();
			if(ListUtils.findObjectByProperty(copiedDirectories, "id", new Long(sceneDirectory.getId()))==null) {
				copySceneDirectory(sceneDirectory, newSceneDirectory.getId(), false, copiedDirectories);
			}
		}
		return newSceneDirectory;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.service.SceneService#listSceneServices(long)
	 */
	public List listSceneServices(long siteId) throws ServiceException {
		return getDatabaseService().findRecordsByHql("from SceneService SceneService where SceneService.siteId=" + siteId + " order by SceneService.name");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.service.SceneService#listChildSceneDirectories(long)
	 */
	public List listChildSceneDirectories(long parentDirectoryId) throws ServiceException {
		String hql = "from SceneDirectory SceneDirectory" +
					 " where SceneDirectory.parentDirectoryId=" + parentDirectoryId +
					 " order by SceneDirectory.priority desc";
		return getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("childDirectories", ","));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.service.SceneService#listParentSceneDirectories(long)
	 */
	public List listParentSceneDirectories(long scentDirectoryId) throws ServiceException {
		List parentDirectories = new ArrayList();
		long directoryId = scentDirectoryId;
		for(;;) {
			SceneDirectory sceneDirectory = (SceneDirectory)getDatabaseService().findRecordById(SceneDirectory.class.getName(), directoryId);
			if(sceneDirectory==null) {
				break;
			}
			if(scentDirectoryId!=directoryId) {
				parentDirectories.add(sceneDirectory);
			}
			directoryId = sceneDirectory.getParentDirectoryId();
		}
		parentDirectories.add(getSceneService(directoryId));
		return parentDirectories;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("scenes".equals(itemsName)) {
			long parentDirectoryId = RequestUtils.getParameterLongValue(request, "parentDirectoryId");
			if(parentDirectoryId<=0) {
				return null;
			}
			String hql = "select SceneDirectory.directoryName, SceneDirectory.id" +
						 " from SceneDirectory SceneDirectory" +
						 " where SceneDirectory.parentDirectoryId=" + parentDirectoryId +
						 " order by SceneDirectory.priority desc";
			return getDatabaseService().findRecordsByHql(hql);
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	/**
	 * @return the templateService
	 */
	public TemplateService getTemplateService() {
		return templateService;
	}

	/**
	 * @param templateService the templateService to set
	 */
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}
}

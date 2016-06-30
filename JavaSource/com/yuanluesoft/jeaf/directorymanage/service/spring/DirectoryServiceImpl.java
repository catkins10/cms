package com.yuanluesoft.jeaf.directorymanage.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.applicationtree.ApplicationTreeNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomConfig;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryLink;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryPopedom;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectorySubjection;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public abstract class DirectoryServiceImpl extends BusinessServiceImpl implements DirectoryService {
	private PageService pageService; //页面服务
	private ExchangeClient exchangeClient; //数据交换服务
	private TemplateService templateService; //模板服务,清除模板缓存用

	/**
	 * 获取目录类
	 * @return
	 * @throws ServiceException
	 */
	public abstract Class getBaseDirectoryClass();
	
	/**
	 * 获取目录引用类
	 * @return
	 */
	public abstract Class getDirectoryLinkClass();
	
	/**
	 * 获取目录类型列表,按级别排序
	 * @return
	 * @throws ServiceException
	 */
	public abstract DirectoryType[] getDirectoryTypes();

	/**
	 * 获取目录权限类型列表
	 * @param popedomName
	 * @return
	 * @throws ServiceException
	 */
	public abstract DirectoryPopedomType[] getDirectoryPopedomTypes();
	
	/**
	 * 获取目录树的根节点图标
	 * @return
	 */
	public abstract String getTreeRootNodeIcon();
	
	/**
	 * 应用导航:节点对应数据链接
	 * @param directoryId
	 * @return
	 */
	public abstract String getNavigatorDataUrl(long directoryId, String directoryType);
	
	/**
	 * 在删除目录时删除数据
	 * @param directory
	 * @return
	 * @throws ServiceException
	 */
	public abstract void deleteDataInDirectory(Directory directory) throws ServiceException;
	
	/**
	 * 重设被拷贝的目录
	 * @param sourceDirectory
	 * @param newDirectory
	 * @return
	 * @throws ServiceException
	 */
	public abstract Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException;
	
	/**
	 * 获取子目录列表时,排序方式
	 * @param directoryId
	 * @return
	 */
	protected String listChildDirectoriesOrderBy(long directoryId) {
		return getShortPojoName() + ".directoryName"; //默认按名称排序
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(!(record instanceof Directory)) {
			return super.save(record);
		}
		Directory directory = (Directory)record;
		//根据类查找对应的目录类型
		if(directory.getDirectoryType()==null || directory.getDirectoryType().isEmpty()) {
			directory.setDirectoryType(getDirectoryTypeByClass(record.getClass()).getType());
		}
		//更新父组织表
		updateDirectorySubjection(directory.getId(), directory.getParentDirectoryId(), true);
		super.save(record);
		//数据交换
		if(exchangeClient!=null) {
			exchangeClient.synchUpdate(record, null, 2000); //同步更新
		}
		//重建静态页面
		if(pageService!=null) {
			pageService.rebuildStaticPageForModifiedObject(directory, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //新建记录
			pageService.rebuildStaticPageForModifiedObject(getDirectory(directory.getParentDirectoryId()), DIRECTORY_ACTION_ADD_CHILD); //添加了一个子目录
		}
		//清空缓存的模板
		if(templateService!=null) {
			templateService.clearCachedTemplate();
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if(!(record instanceof Directory)) {
			return super.update(record);
		}
		synchronized(this) { //只允许同时更新一个目录
			Directory directory = (Directory)record;
			String pojoClassName = getShortPojoName();
			//获取原来的记录
			Directory oldDirectory = (Directory)load(directory.getClass(), directory.getId());
			directory = (Directory)super.update(record);
			if(oldDirectory.getParentDirectoryId()!=directory.getParentDirectoryId()) { //父目录已经更改了
				updateDirectorySubjection(directory.getId(), directory.getParentDirectoryId(), false); //更新父目录表
				//重新继承上级目录的权限
				List directoryPopedomTypes = listDirectoryPopedomTypes(directory.getDirectoryType());
				for(Iterator iterator = directoryPopedomTypes==null ? null : directoryPopedomTypes.iterator(); iterator!=null && iterator.hasNext();) {
					DirectoryPopedomType directoryPopedomType = (DirectoryPopedomType)iterator.next();
					//获取上级目录的权限
					String hql = "from " + pojoClassName + "Popedom " + pojoClassName + "Popedom" +
						  		 " where " + pojoClassName + "Popedom.directoryId=" + directory.getParentDirectoryId() +
						  		 " and " + pojoClassName + "Popedom.popedomName='" + JdbcUtils.resetQuot(directoryPopedomType.getName()) + "'";
					inheritDirectoryPopedoms(directory.getParentDirectoryId(), getDatabaseService().findRecordsByHql(hql), directoryPopedomType.getName());
				}
				//重建静态页面
				if(pageService!=null) {
					pageService.rebuildStaticPageForModifiedObject(getDirectory(oldDirectory.getParentDirectoryId()), DIRECTORY_ACTION_REMOVE_CHILD); //重建原来的父目录相关页面
					pageService.rebuildStaticPageForModifiedObject(getDirectory(directory.getParentDirectoryId()), DIRECTORY_ACTION_MOVE_CHILD); //重建现在的父目录相关页面
					pageService.rebuildStaticPageForModifiedObject(directory, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //由于位置变化,故需要更新相关的静态页面
				}
			}
			else if(rebuildStaticPageForUpdatedDirectory(directory, oldDirectory)) { //父目录没有调整,检查是否需要更新静态页面
				if(pageService!=null) {
					pageService.rebuildStaticPageForModifiedObject(directory, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重建静态页面
				}
			}
			//更新目录引用
			List directoryLinks = listDirectoryLinks(directory);
			for(Iterator iterator = directoryLinks==null ? null : directoryLinks.iterator(); iterator!=null && iterator.hasNext(); ) {
				Directory directoryLink = (Directory)iterator.next();
				if(!directoryLink.getDirectoryName().equals(directory.getDirectoryName()) ||
				   !directoryLink.getDirectoryType().equals(directory.getDirectoryType())) {
					directoryLink.setDirectoryName(directory.getDirectoryName());
					directoryLink.setDirectoryType(directory.getDirectoryType());
					update(directoryLink);
				}
			}
			//同步更新
			if(exchangeClient!=null) {
				exchangeClient.synchUpdate(record, null, 2000);
			}
			//清空缓存的模板
			if(templateService!=null) {
				templateService.clearCachedTemplate();
			}
			return directory;
		}
	}
	
	/**
	 * 目录更新后,判断是否要更新静态页面
	 * @param newDirectory
	 * @param oldDirectory
	 * @return
	 * @throws ServiceException
	 */
	protected boolean rebuildStaticPageForUpdatedDirectory(Directory newDirectory, Directory oldDirectory) throws ServiceException {
		return !StringUtils.isEquals(newDirectory.getDirectoryName(), oldDirectory.getDirectoryName()) || //名称变更
			   newDirectory.getPriority()!=oldDirectory.getPriority(); //优先级变更
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		if(!(record instanceof Directory)) {
			super.delete(record);
			return;
		}
		synchronized(this) { //只允许同时删除一个目录
			Directory directory = (Directory)record;
			//删除下级目录
			String pojoClassName = getShortPojoName();
			String hql = "select " + pojoClassName +
						 " from " + pojoClassName + " " + pojoClassName + ", " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
				  		 " where " + pojoClassName + ".id=" + pojoClassName + "Subjection.directoryId" +
				  		 " and " + pojoClassName + "Subjection.parentDirectoryId=" + directory.getId();
			List children = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iterator = children==null ? null : children.iterator(); iterator!=null && iterator.hasNext(); ) {
				Directory childDirectory = (Directory)iterator.next();
				deleteDataInDirectory(childDirectory); //删除数据
				getDatabaseService().deleteRecord(childDirectory); //删除目录
				//重建静态页面
				if(pageService!=null) {
					pageService.rebuildStaticPageForModifiedObject(childDirectory, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE); //删除一个记录
				}
			}
			//删除目录引用
			List directoryLinks = listDirectoryLinks(directory);
			for(Iterator iterator = directoryLinks==null ? null : directoryLinks.iterator(); iterator!=null && iterator.hasNext(); ) {
				Directory directoryLink = (Directory)iterator.next();
				delete(directoryLink);
			}
			//同步删除
			if(exchangeClient!=null) {
				exchangeClient.synchDelete(directory, null, 2000);
			}
			//重建父目录静态页面
			if(pageService!=null) {
				pageService.rebuildStaticPageForModifiedObject(getDirectory(directory.getParentDirectoryId()), DIRECTORY_ACTION_REMOVE_CHILD); //子目录删除
			}
			//清空缓存的模板
			if(templateService!=null) {
				templateService.clearCachedTemplate();
			}
		}
	}
	
	/**
	 * 获取目录引用列表
	 * @param directory
	 * @return
	 * @throws ServiceException
	 */
	private List listDirectoryLinks(Directory directory) throws ServiceException {
		if(directory instanceof DirectoryLink) {
			return null;
		}
		Class directoryLinkClass = getDirectoryLinkClass();
		if(directoryLinkClass==null) {
			return null;
		}
		String pojoClassName = directoryLinkClass.getSimpleName();
		String hql = "from " + pojoClassName + " " + pojoClassName +
				 	 " where " + pojoClassName + ".linkedDirectoryId=" + directory.getId();
		return getDatabaseService().findRecordsByHql(hql, listLazyLoadProperties(directoryLinkClass));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(java.lang.Object)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		if(record instanceof Directory) {
			Directory directory = (Directory)record;
			String pojoClassName = getShortPojoName();
			String hql = "select " + pojoClassName + ".id" +
						 " from " + pojoClassName + " " + pojoClassName +
				  		 " where " + pojoClassName + ".id!=" + directory.getId() +
				  		 " and " + pojoClassName + ".parentDirectoryId=" + directory.getParentDirectoryId() +
				  		 " and " + pojoClassName + ".directoryName='" + JdbcUtils.resetQuot(directory.getDirectoryName()) + "'" +
				  		 " and " + pojoClassName + ".id!=0";
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				List errors = new ArrayList();
				errors.add("同名目录已经存在");
				return errors;
			}
		}
		return super.validateBusiness(record, isNew);
	}
	
	/**
	 * 更新父目录隶属关系
	 * @param directoryId
	 * @parsam parentId
	 * @param newDirectory
	 * @throws ServiceException
	 */
	protected void updateDirectorySubjection(long directoryId, long parentDirectoryId, boolean newDirectory) throws ServiceException {
		if(directoryId==0 && !newDirectory) { //根
			return;
		}
		String pojoClassName = getShortPojoName();
		if(!newDirectory) {
			//删除原来的目录隶属关系
			String hql = "from " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
						 " where " + pojoClassName + "Subjection.directoryId=" + directoryId;
			getDatabaseService().deleteRecordsByHql(hql);
			getDatabaseService().flush();
		}
		//添加自己
		addDirectorySubjection(directoryId, directoryId);
		if(directoryId>0) { //不是根目录
			//获取上级的上级
			String hql = "from " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
						 " where " + pojoClassName + "Subjection.directoryId=" + parentDirectoryId +
						 " order by " + pojoClassName + "Subjection.id";
			List parentSubjections = getDatabaseService().findRecordsByHql(hql);
			//添加上级的上级
			for(Iterator iterator = parentSubjections==null ? null : parentSubjections.iterator(); iterator!=null && iterator.hasNext();) {
				DirectorySubjection directorySubjection = (DirectorySubjection)iterator.next();
				addDirectorySubjection(directoryId, directorySubjection.getParentDirectoryId());
			}
		}
		if(!newDirectory) {
			//递归更新下级组织的上级组织
			String hql = "select " + pojoClassName + ".id" +
						 " from " + pojoClassName + " " + pojoClassName +
						 " where " + pojoClassName + ".parentDirectoryId=" + directoryId;
			List childIds = getDatabaseService().findRecordsByHql(hql);
			if(childIds!=null && !childIds.isEmpty()) {
				for(Iterator iterator = childIds.iterator(); iterator.hasNext();) {
					updateDirectorySubjection(((Number)iterator.next()).longValue(), directoryId, false); //递归
				}
			}
		}
	}
	
	/**
	 * 添加目录隶属关系
	 * @param directoryId
	 * @param parentDirectoryId
	 * @throws ServiceException
	 */
	private void addDirectorySubjection(long directoryId, long parentDirectoryId) throws ServiceException {
		DirectorySubjection directorySubjection;
		try {
			directorySubjection = (DirectorySubjection)Class.forName(getBaseDirectoryClass().getName() + "Subjection").newInstance();
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		directorySubjection.setId(UUIDLongGenerator.generateId());
		directorySubjection.setDirectoryId(directoryId);
		directorySubjection.setParentDirectoryId(parentDirectoryId);
		getDatabaseService().saveRecord(directorySubjection);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#updateLinkedDirectories(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void updateLinkedDirectories(long directoryId, String linkedDirectoryIds, SessionInfo sessionInfo) throws ServiceException {
		List childDirectories = listChildDirectories(directoryId, null, null, null, true, false, sessionInfo, 0, 0);
		for(Iterator iterator = childDirectories==null ? null : childDirectories.iterator(); iterator!=null && iterator.hasNext();) {
			Directory directory = (Directory)iterator.next();
			if((directory instanceof DirectoryLink) &&
			   ("," + linkedDirectoryIds + ",").indexOf("," + ((DirectoryLink)directory).getLinkedDirectoryId() + ",")==-1) {
				delete(getDirectory(directory.getId()));
				iterator.remove();
			}
		}
		if(linkedDirectoryIds==null || linkedDirectoryIds.isEmpty()) {
			return;
		}
		if(childDirectories==null) {
			childDirectories = new ArrayList();
		}
		Class directoryLinkClass = getDirectoryLinkClass();
		String[] ids = linkedDirectoryIds.split(",");
		for(int i=0; i<ids.length; i++) {
			long id = Long.parseLong(ids[i]);
			//判断是否已经添加过
			if(ListUtils.findObjectByProperty(childDirectories, "linkedDirectoryId", new Long(id))!=null) {
				continue;
			}
			//获取目录
			Directory directory = getDirectory(id);
			Directory directoryLink;
			try {
				directoryLink = (Directory)directoryLinkClass.newInstance();
			}
			catch(Exception e) {
				throw new ServiceException(e);
			}
			directoryLink.setId(-1); //ID
			directoryLink.setDirectoryName(directory.getDirectoryName()); //目录名称
			directoryLink.setParentDirectoryId(directoryId); //上级目录ID
			directoryLink.setDirectoryType(directory.getDirectoryType()); //目录类型
			directoryLink.setCreator(sessionInfo.getUserName()); //创建人
			directoryLink.setCreatorId(sessionInfo.getUserId()); //创建人ID
			directoryLink.setCreated(DateTimeUtils.now()); //创建时间
			//directoryLink.setRemark(remark); //备注
			((DirectoryLink)directoryLink).setLinkedDirectoryId(directory.getId());
			childDirectories.add(directoryLink);
		}
		//排序
		childDirectories = ListUtils.sortByProperty(childDirectories, "linkedDirectoryId", linkedDirectoryIds);
		float priority = childDirectories.size();
		for(Iterator iterator = childDirectories.iterator(); iterator.hasNext();) {
			Directory directory = (Directory)iterator.next();
			if(directory.getId()==-1) {
				directory.setId(UUIDLongGenerator.generateId());
				directory.setPriority(priority);
				createDirectory(directory);
			}
			else if(directory.getPriority()!=priority) {
				directory = getDirectory(directory.getId());
				directory.setPriority(priority);
				update(directory);
			}
			priority--;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectory(long)
	 */
	public Directory getDirectory(long directoryId) throws ServiceException {
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName + ".directoryType" +
					 " from " + pojoClassName + " " + pojoClassName + " " +
					 " where " + pojoClassName + ".id=" + directoryId;
		String directoryType = (String)getDatabaseService().findRecordByHql(hql);
		if(directoryType==null) {
			return null;
		}
		Directory directory = (Directory)load(getDirectoryType(directoryType).getDirectoryClass(), directoryId);
		return directory!=null ? directory : (Directory)load(getBaseDirectoryClass(), directoryId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#createDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory createDirectory(Directory directory) throws ServiceException {
		save(directory); //保存
		//授权
		List directoryPopedomTypes = listDirectoryPopedomTypes(directory.getDirectoryType());
		for(Iterator iterator = directoryPopedomTypes==null ? null : directoryPopedomTypes.iterator(); iterator!=null && iterator.hasNext();) {
			DirectoryPopedomType directoryPopedomType = (DirectoryPopedomType)iterator.next();
			authorize(directory, true, directoryPopedomType.getName(), null, null, false, null);
		}
		return directory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#createDirectory(long, java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public Directory createDirectory(long directoryId, long parentDirectoryId, String directoryName, String directoryType, String remark, long creatorId, String creatorName) throws ServiceException {
		try {
			//检查目录是否存在
			String pojoClassName = getShortPojoName();
			String hql = "from " + pojoClassName + " " + pojoClassName + " " +
						 " where " + pojoClassName + ".parentDirectoryId=" + parentDirectoryId +
						 " and " + pojoClassName + ".directoryName='" + JdbcUtils.resetQuot(directoryName) + "'";
			Directory directory = (Directory)getDatabaseService().findRecordByHql(hql);
			if(directory!=null) {
				return directory;
			}
			DirectoryType directoryTypeDefine = getDirectoryType(directoryType);
			directory = (Directory)directoryTypeDefine.getDirectoryClass().newInstance();
			directory.setDirectoryType(directoryType);
			directory.setId(directoryId==-1 ? UUIDLongGenerator.generateId() : directoryId); //ID
			directory.setCreator(creatorName); //创建人
			directory.setCreatorId(creatorId); //创建人ID
			directory.setCreated(DateTimeUtils.now()); //创建时间
			directory.setDirectoryName(directoryName); //名称
			directory.setParentDirectoryId(parentDirectoryId); //父目录ID
			directory.setRemark(remark); //备注
			return createDirectory(directory);
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectoryTypeTitle(java.lang.String)
	 */
	public String getDirectoryTypeTitle(String directoryType) throws ServiceException {
		DirectoryType directoryTypeModel = getDirectoryType(directoryType);
		return directoryTypeModel==null ? null : directoryTypeModel.getTitle();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectoryPopedomConfigs(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, java.lang.String)
	 */
	public List getDirectoryPopedomConfigs(Directory directory, String directoryClassName) throws ServiceException {
		//获取目录类型对应的权限列表
		DirectoryType directoryType;
		try {
			directoryType = getDirectoryTypeByClass(Class.forName(directoryClassName));
		} 
		catch (ClassNotFoundException e) {
			throw new ServiceException(e.getMessage());
		}
		List popedomConfigs = new ArrayList();
		List directoryPopedomTypes = listDirectoryPopedomTypes(directoryType.getType());
		for(Iterator iterator = directoryPopedomTypes==null ? null : directoryPopedomTypes.iterator(); iterator!=null && iterator.hasNext();) {
			DirectoryPopedomType directoryPopedomType = (DirectoryPopedomType)iterator.next();
			DirectoryPopedomConfig popedomConfig = new DirectoryPopedomConfig();
			popedomConfig.setPopedomName(directoryPopedomType.getName()); //名称
			popedomConfig.setPopedomTitle(directoryPopedomType.getTitle()); //标题
			if(directory!=null) {
				//获取非继承的用户列表
				List popedoms = ListUtils.getSubListByProperty(directory.getDirectoryPopedoms(), "popedomName", directoryPopedomType.getName());
				List visitors = ListUtils.getSubListByProperty(popedoms, "isInherit", new Character('0'));
				popedomConfig.setUserIds(ListUtils.join(visitors, "userId", ",", false)); //有权限的用户ID列表
				popedomConfig.setUserNames(ListUtils.join(visitors, "userName", ",", false)); //有权限的用户姓名列表
				popedomConfig.setInheritUserNames(ListUtils.join(ListUtils.getSubListByProperty(popedoms, "isInherit", new Character('1')), "userName", ",", false)); //继承的用户姓名列表
			}
			popedomConfigs.add(popedomConfig);
		}
		return popedomConfigs;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#saveDirectoryPopedomConfigs(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean saveDirectoryPopedomConfigs(Directory directory, List directoryPopedomConfigs, boolean isNewDirectory, SessionInfo sessionInfo) throws ServiceException {
		if(directoryPopedomConfigs==null || directoryPopedomConfigs.isEmpty()) {
			return false;
		}
		synchronized(this) { //只允许同时更新一个目录
			boolean changed = false;
			for(Iterator iterator = directoryPopedomConfigs.iterator(); iterator.hasNext();) {
				DirectoryPopedomConfig popedomConfig = (DirectoryPopedomConfig)iterator.next();
				boolean keepMyselfPopedom = getDirectoryPopedomType(popedomConfig.getPopedomName()).isKeepMyselfPopedom();
				if(authorize(directory, isNewDirectory, popedomConfig.getPopedomName(), popedomConfig.getUserIds(), popedomConfig.getUserNames(), keepMyselfPopedom, sessionInfo)) {
					changed = true;
				}
			}
			return changed;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#authorize(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, java.lang.String, java.lang.String, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean authorize(Directory directory, boolean isNewDirectory, String popedomName, String userIds, String userNames, boolean keepMyselfPopedom, SessionInfo sessionInfo) throws ServiceException {
		synchronized(this) { //只允许同时更新一个目录
			//获取原来的权限配置
			List directoryPopedoms = ListUtils.getSubListByProperty(directory.getDirectoryPopedoms(), "popedomName", popedomName);
			//检查配置是否有变动
			if(!isNewDirectory) { //不是新目录
				String oldUserIds = ListUtils.join(ListUtils.getSubListByProperty(directoryPopedoms, "isInherit", new Character('0')), "userId", ",", false);
				if(oldUserIds==null) {
					oldUserIds = "";
				}
				if(userIds==null) {
					userIds = "";
				}
				if(oldUserIds.equals(userIds)) { //没有变动
					return false;
				}
				if(keepMyselfPopedom && sessionInfo!=null) { //保留当前用户的权限
					if(("," + oldUserIds + ",").indexOf("," + sessionInfo.getUserId() + ",")!=-1 && //原来有当前用户
					   ("," + userIds + ",").indexOf("," + sessionInfo.getUserId() + ",")==-1) { //现在没有了
						if(userIds.equals("")) {
							userIds = "" + sessionInfo.getUserId();
							userNames = "" + sessionInfo.getUserName();
						}
						else {
							userIds += "," + sessionInfo.getUserId();
							userNames += "," + sessionInfo.getUserName();
						}
					}
				}
			}
			//删除原来的权限配置
			if(directoryPopedoms!=null && !directoryPopedoms.isEmpty()) {
				for(Iterator iterator = directoryPopedoms.iterator(); iterator.hasNext();) {
					DirectoryPopedom directoryPopedom = (DirectoryPopedom)iterator.next();
					getDatabaseService().deleteRecord(directoryPopedom);
				}
			}
			//添加新权限
			directoryPopedoms = new ArrayList();
			if(userIds!=null && !userIds.equals("")) {
				String[] ids = userIds.split(",");
				String[] names = userNames.split(",");
				for(int i=0; i<ids.length; i++) {
					DirectoryPopedom directoryPopedom;
					try {
						directoryPopedom = (DirectoryPopedom)Class.forName(getBaseDirectoryClass().getName() + "Popedom").newInstance();
					}
					catch (Exception e) {
						throw new ServiceException(e.getMessage());
					}
					directoryPopedom.setId(UUIDLongGenerator.generateId()); //ID
					directoryPopedom.setDirectoryId(directory.getId()); //目录ID
					directoryPopedom.setUserId(Long.parseLong(ids[i])); //用户ID
					directoryPopedom.setUserName(names[i]); //用户名
					directoryPopedom.setPopedomName(popedomName); //访问级别
					directoryPopedom.setIsInherit('0'); //不是继承的
					getDatabaseService().saveRecord(directoryPopedom);
					directoryPopedoms.add(directoryPopedom);
				}
			}
			//继承上级目录的权限
			int inheritFromParent = getDirectoryPopedomType(popedomName).isInheritFromParent();
			if(directory.getId()>0 && 
			   (inheritFromParent==DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS || //总是继承
			    (inheritFromParent==DirectoryPopedomType.INHERIT_FROM_PARENT_WHEN_EMPTY && (userIds==null || userIds.equals(""))))) { //当自己没有配置时继承
				String pojoClassName = getShortPojoName();
				String hql = "from " + pojoClassName + "Popedom " + pojoClassName + "Popedom" +
					  		 " where " + pojoClassName + "Popedom.directoryId=" + directory.getParentDirectoryId() +
					  		 " and " + pojoClassName + "Popedom.popedomName='" + JdbcUtils.resetQuot(popedomName) + "'";
				List parentDirectoryPopedoms = getDatabaseService().findRecordsByHql(hql);
				if(parentDirectoryPopedoms!=null && !parentDirectoryPopedoms.isEmpty()) {
					for(Iterator iterator = parentDirectoryPopedoms.iterator(); iterator.hasNext();) {
						DirectoryPopedom parentDirectoryPopedom = (DirectoryPopedom)iterator.next();
						//检查是否已经配置过
						if(ListUtils.findObjectByProperty(directoryPopedoms, "userId", new Long(parentDirectoryPopedom.getUserId()))!=null) {
							continue;
						}
						try {
							DirectoryPopedom directoryPopedom = (DirectoryPopedom)Class.forName(getBaseDirectoryClass().getName() + "Popedom").newInstance();
							PropertyUtils.copyProperties(directoryPopedom, parentDirectoryPopedom);
							directoryPopedom.setId(UUIDLongGenerator.generateId()); //ID
							directoryPopedom.setIsInherit('1');
							directoryPopedom.setDirectoryId(directory.getId()); //目录ID
							getDatabaseService().saveRecord(directoryPopedom);
							directoryPopedoms.add(directoryPopedom);
						}
						catch (Exception e) {
							
						}
					}
				}
			}
			//更新目录权限
			if(directory.getDirectoryPopedoms()==null) {
				directory.setDirectoryPopedoms(new LinkedHashSet(directoryPopedoms));
			}
			else {
				ListUtils.removeObjectsByProperty(directory.getDirectoryPopedoms(), "popedomName", popedomName);
				directory.getDirectoryPopedoms().addAll(directoryPopedoms);
			}
			//更新下级目录的权限
			inheritDirectoryPopedoms(directory.getId(), directoryPopedoms, popedomName);
			return true;
		}
	}

	/**
	 * 递归函数:更新继承的权限
	 * @param parentDirectoryId
	 * @param parentDirectoryPopedoms
	 * @throws ServiceException
	 */
	private void inheritDirectoryPopedoms(long parentDirectoryId, List parentDirectoryPopedoms, final String popedomName) throws ServiceException {
		int inheritFromParent = getDirectoryPopedomType(popedomName).isInheritFromParent();
		if(inheritFromParent==DirectoryPopedomType.INHERIT_FROM_PARENT_NO) { //不继承
			return; //不继承上级目录权限
		}
		//获取下级目录
		String pojoClassName = getShortPojoName();
		String hql = "from " + pojoClassName + " " + pojoClassName +
					 " where " + pojoClassName + ".id!=0" +
					 " and " + pojoClassName + ".parentDirectoryId=" + parentDirectoryId;
		List childDirectories = getDatabaseService().findRecordsByHql(hql);
		if(childDirectories==null || childDirectories.isEmpty()) {
			return;
		}
		for(Iterator iterator = childDirectories.iterator(); iterator.hasNext();) {
			Directory directory = (Directory)iterator.next();
			if(ListUtils.findObjectByProperty(listDirectoryPopedomTypes(directory.getDirectoryType()), "name", popedomName)==null) { //下级目录不需要继承本权限
				continue;
			}
			//获取目录的权限配置
			hql = "from " + pojoClassName + "Popedom " + pojoClassName + "Popedom" +
				  " where " + pojoClassName + "Popedom.directoryId=" + directory.getId() +
				  " and " + pojoClassName + "Popedom.popedomName='" + JdbcUtils.resetQuot(popedomName) + "'";
			List directoryPopedoms = getDatabaseService().findRecordsByHql(hql);
			if(inheritFromParent==DirectoryPopedomType.INHERIT_FROM_PARENT_WHEN_EMPTY && //没有配置时继承
			   ListUtils.findObjectByProperty(directoryPopedoms, "isInherit", new Character('0'))!=null) { //且目录已经配置过自己的权限
				continue;
			}
			//删除原来继承的权限
			if(directoryPopedoms==null) {
				directoryPopedoms = new ArrayList();
			}
			else if(!directoryPopedoms.isEmpty()) {
				for(Iterator iteratorPopedom = directoryPopedoms.iterator(); iteratorPopedom.hasNext();) {
					DirectoryPopedom directoryPopedom = (DirectoryPopedom)iteratorPopedom.next();
					if(directoryPopedom.getIsInherit()=='1') {
						//删除继承的权限
						getDatabaseService().deleteRecord(directoryPopedom);
						iteratorPopedom.remove();
					}
				}
			}
			//重新继承权限
			if(parentDirectoryPopedoms!=null && !parentDirectoryPopedoms.isEmpty()) {
				for(Iterator iteratorPopedom = parentDirectoryPopedoms.iterator(); iteratorPopedom.hasNext();) {
					DirectoryPopedom parentDirectoryPopedom = (DirectoryPopedom)iteratorPopedom.next();
					//检查是否已经配置过
					if(ListUtils.findObjectByProperty(directoryPopedoms, "userId", new Long(parentDirectoryPopedom.getUserId()))!=null) {
						continue;
					}
					try {
						DirectoryPopedom directoryPopedom = (DirectoryPopedom)Class.forName(getBaseDirectoryClass().getName() + "Popedom").newInstance();
						PropertyUtils.copyProperties(directoryPopedom, parentDirectoryPopedom);
						directoryPopedom.setId(UUIDLongGenerator.generateId()); //ID
						directoryPopedom.setIsInherit('1');
						directoryPopedom.setDirectoryId(directory.getId()); //目录ID
						getDatabaseService().saveRecord(directoryPopedom);
						directoryPopedoms.add(directoryPopedom);
					}
					catch (Exception e) {
						
					}
				}
			}
			inheritDirectoryPopedoms(directory.getId(), directoryPopedoms, popedomName); //递归更新下一级
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#copyDirectory(long, long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Directory copyDirectory(long directoryId, long toDirectoryId, String newDirectoryName, String userName, long userId) throws ServiceException {
		synchronized(this) { //只允许同时更新一个目录
			try {
				Directory newDirectory = copyDirectory(getDirectory(directoryId), toDirectoryId, newDirectoryName, new ArrayList(), userName, userId);
				//授权
				List directoryPopedomTypes = listDirectoryPopedomTypes(newDirectory.getDirectoryType());
				for(Iterator iterator = directoryPopedomTypes==null ? null : directoryPopedomTypes.iterator(); iterator!=null && iterator.hasNext();) {
					DirectoryPopedomType directoryPopedomType = (DirectoryPopedomType)iterator.next();
					authorize(newDirectory, true, directoryPopedomType.getName(), null, null, false, null);
				}
				return newDirectory;
			}
			catch(Exception e) {
				throw new ServiceException(e);
			}
		}
	}
	
	/**
	 * 复制目录,递归函数
	 * @param directory
	 * @param toDirectoryId
	 * @param newDirectoryName
	 * @param newDirectoryIds
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	private Directory copyDirectory(Directory directory, long toDirectoryId, String newDirectoryName, List newDirectoryIds, String userName, long userId) throws Exception {
		if(Logger.isDebugEnabled()) {
			Logger.debug("Copy " + directory.getDirectoryName() + ".");
		}
		//拷贝
		Directory newDirectory = (Directory)directory.getClass().newInstance();
		PropertyUtils.copyProperties(newDirectory, directory);
		newDirectory.setId(UUIDLongGenerator.generateId()); //ID
		newDirectory.setCreator(userName); //创建人
		newDirectory.setCreatorId(userId); //创建人ID
		newDirectory.setCreated(DateTimeUtils.now()); //创建时间
		newDirectory.setChildSubjections(null);
		newDirectory.setSubjections(null);
		newDirectory.setDirectoryPopedoms(null);
		if(newDirectoryName!=null && !newDirectoryName.equals("")) {
			newDirectory.setDirectoryName(newDirectoryName);
		}
		newDirectory.setParentDirectoryId(toDirectoryId); //设置父目录ID
		newDirectory = resetCopiedDirectory(directory, newDirectory); //重设目录
		save(newDirectory); //保存
		
		//添加到新目录列表,避免死循环
		newDirectoryIds.add(new Long(newDirectory.getId()));
		//复制下级目录
		String pojoClassName = getShortPojoName();
		String hql = "from " + pojoClassName + " " + pojoClassName + "" +
					 " where " + pojoClassName + ".parentDirectoryId=" + directory.getId() +
					 " and " + pojoClassName + ".id!=0" +
					 " order by " + pojoClassName + ".priority DESC, " + listChildDirectoriesOrderBy(directory.getId());
		List childDirectories = getDatabaseService().findRecordsByHql(hql);
		if(childDirectories==null || childDirectories.isEmpty()) {
			return newDirectory;
		}
		for(Iterator iterator = childDirectories.iterator(); iterator.hasNext();) {
			directory = (Directory)iterator.next();
			if(newDirectoryIds.indexOf(new Long(directory.getId()))!=-1) {
				continue;
			}
			//递归调用
			copyDirectory(directory, newDirectory.getId(), null, newDirectoryIds, userName, userId);
		}
		return newDirectory;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectoryName(long)
	 */
	public String getDirectoryName(long directoryId) throws ServiceException {
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName + ".directoryName" +
 		 			 " from " + pojoClassName + " " + pojoClassName +
 		 			 " where " + pojoClassName + ".id=" + directoryId;
		return (String)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectoryFullName(long, java.lang.String, java.lang.String)
	 */
	public String getDirectoryFullName(long directoryId, String delimiter, String traceToDirectoryTypes) throws ServiceException {
		if(delimiter==null) {
			delimiter = "/";
		}
		String name = null;
		String pojoClassName = getShortPojoName();
		//获取上级录的名称列表
		String hql = "select " + pojoClassName + ".directoryName, " + pojoClassName + ".directoryType" +
			  		 " from " + pojoClassName + " " + pojoClassName + ", " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
			  		 " where " + pojoClassName + "Subjection.directoryId=" + directoryId +
			  		 " and " + pojoClassName + ".id=" + pojoClassName + "Subjection.parentDirectoryId" +
			  		 " order by " + pojoClassName + "Subjection.id";
		List parentDiectoryValues = getDatabaseService().findRecordsByHql(hql);
		if(parentDiectoryValues==null) {
			return null;
		}
		for(Iterator iterator = parentDiectoryValues.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			name = values[0] + (name==null ? "" : delimiter + name);
			if(traceToDirectoryTypes!=null && ("," + traceToDirectoryTypes + ",").indexOf("," + values[1] + ",")!=-1) { //目录本身就是指定的目录类型
				break;
			}
		}
		return name;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectoryFullNames(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getDirectoryFullNames(String directoryIds, String delimiter, String namesDelimiter, String traceToDirectoryTypes) throws ServiceException {
		if(directoryIds==null || directoryIds.equals("")) {
			return null;
		}
		String[] ids = directoryIds.split(",");
		String names = null;
		for(int i=0; i<ids.length; i++) {
			names = (names==null ? "" : names + namesDelimiter) + getDirectoryFullName(Long.parseLong(ids[i]), delimiter, traceToDirectoryTypes);
		}
		return names;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectoryPopedoms(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listDirectoryPopedoms(long directoryId, SessionInfo sessionInfo) {
		String pojoClassName = getShortPojoName();
		//获取用户对目录的权限
		String hql = "select distinct " + pojoClassName + "Popedom.popedomName" +
					 " from " + pojoClassName + "Popedom " + pojoClassName + "Popedom" +
					 " where " + pojoClassName + "Popedom.directoryId=" + directoryId + 
					 " and " + pojoClassName + "Popedom.userId in (" + sessionInfo.getUserIds() + ")";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#checkPopedom(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean checkPopedom(long directoryId, String popedomNames, SessionInfo sessionInfo) {
		if(getDirectoryPopedomTypes()==null) {
			return true;
		}
		String pojoClassName = getShortPojoName();
		//获取用户对目录的权限
		String hql = "select distinct " + pojoClassName + "Popedom.popedomName" +
					 " from " + pojoClassName + "Popedom " + pojoClassName + "Popedom" +
					 " where " + pojoClassName + "Popedom.directoryId=" + directoryId + 
					 (popedomNames==null || "all".equals(popedomNames) ? "" :  " and " + pojoClassName + "Popedom.popedomName in ('" + JdbcUtils.resetQuot(popedomNames).replaceAll(",", "','") + "')") + 
					 " and " + pojoClassName + "Popedom.userId in (" + sessionInfo.getUserIds() + ")";
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#checkParentDirectoryPopedom(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean checkParentDirectoryPopedom(long directoryId, String popedomNames, SessionInfo sessionInfo) {
		if(directoryId==0) {
			return checkPopedom(directoryId, popedomNames, sessionInfo);
		}
		String pojoClassName = getShortPojoName();
		//获取用户对目录的权限
		String hql = "select distinct " + pojoClassName + "Popedom.popedomName" +
					 " from " + pojoClassName + "Popedom " + pojoClassName + "Popedom, " + pojoClassName + "Subjection " + pojoClassName + "Subjection"+
					 " where " + pojoClassName + "Popedom.directoryId=" + pojoClassName + "Subjection.parentDirectoryId" +
					 " and " + pojoClassName + "Subjection.directoryId=" + directoryId + 
					 (popedomNames==null || "all".equals(popedomNames) ? "" :  " and " + pojoClassName + "Popedom.popedomName in ('" + JdbcUtils.resetQuot(popedomNames).replaceAll(",", "','") + "')") + 
					 " and " + pojoClassName + "Popedom.userId in (" + sessionInfo.getUserIds() + ")";
		return getDatabaseService().findRecordByHql(hql)!=null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#hasChildDirectories(long)
	 */
	public boolean hasChildDirectories(long directoryId) throws ServiceException {
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName + ".id" +
					 " from " + pojoClassName + " " + pojoClassName +
					 " where " + pojoClassName + ".parentDirectoryId=" + directoryId;
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getChildDirectoryPopedoms(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listChildDirectoryPopedoms(long directoryId, SessionInfo sessionInfo) {
		String pojoClassName = getShortPojoName();
		//获取用户对下级目录的权限列表
		String hql = "select distinct " + pojoClassName + "Popedom.popedomName" +
			  		 " from " + pojoClassName + "Popedom " + pojoClassName + "Popedom, " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
			  		 " where " + pojoClassName + "Subjection.parentDirectoryId=" + directoryId +
			  		 " and " + pojoClassName + "Subjection.directoryId!=" + directoryId +
			  		 " and " + pojoClassName + "Popedom.directoryId=" + pojoClassName + "Subjection.directoryId" +
			  		 " and " + pojoClassName + "Popedom.userId in (" + sessionInfo.getUserIds() + ")";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getChildDirectoryIds(java.lang.String, java.lang.String)
	 */
	public String getChildDirectoryIds(String directoryIds, String childDirectoryTypes) {
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName + ".id," + pojoClassName + ".directoryType" +
					 " from " + pojoClassName + " " + pojoClassName + "" +
					 " where " + pojoClassName + ".id in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		List directories = getDatabaseService().findRecordsByHql(hql);
		if(directories==null) {
			return null;
		}
		String matchDirectoryIds = null; //类型匹配的目录ID
		String nomatchDirectoryIds = null; //类型不匹配的目录ID
		for(Iterator iterator = directories.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			if(("," + childDirectoryTypes + ",").indexOf("," + values[1] + ",")!=-1) { //子目录的类型包含当前目录类型
				matchDirectoryIds = (matchDirectoryIds==null ? "" : matchDirectoryIds + ",") + values[0];
			}
			else {
				nomatchDirectoryIds = (nomatchDirectoryIds==null ? "" : nomatchDirectoryIds + ",") + values[0];
			}
		}
		if(nomatchDirectoryIds==null) {
			return matchDirectoryIds;
		}
	    //获取直接下级中的类型匹配的目录ID
		hql = "select " + pojoClassName + ".id" +
			  " from " + pojoClassName + " " + pojoClassName + "" +
			  " where " + pojoClassName + ".parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(nomatchDirectoryIds) + ")" +
			  " and " + pojoClassName + ".directoryType in ('" + childDirectoryTypes.replaceAll(",", "','") + "')";
		nomatchDirectoryIds = ListUtils.join(getDatabaseService().findRecordsByHql(hql), ",", false);
		if(nomatchDirectoryIds==null) {
			return matchDirectoryIds;
		}
		return matchDirectoryIds==null ? nomatchDirectoryIds : matchDirectoryIds + "," + nomatchDirectoryIds;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getAllChildDirectoryIds(java.lang.String, java.lang.String)
	 */
	public String getAllChildDirectoryIds(String directoryIds, String childDirectoryTypes) {
		String pojoClassName = getShortPojoName();
		String hql = "select distinct " + pojoClassName + ".id" + 
					 " from " + pojoClassName + " " + pojoClassName + "," + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
				 	 " where " + pojoClassName + ".id=" + pojoClassName + "Subjection.directoryId"  + 
				 	 " and " + pojoClassName + "Subjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
				 	 (childDirectoryTypes==null ? "" : " and " + pojoClassName + ".directoryType in ('" + childDirectoryTypes.replaceAll(",", "','") + "')");
		return ListUtils.join(getDatabaseService().findRecordsByHql(hql), ",", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listDirectories(java.lang.String)
	 */
	public List listDirectories(String directoryIds) throws ServiceException {
		String pojoClassName = getShortPojoName();
		String hql = "from " + pojoClassName + " " + pojoClassName +
				 	 " where " + pojoClassName + ".id in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		return ListUtils.sortByProperty(getDatabaseService().findRecordsByHql(hql), "id", directoryIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listChildDirectories(long, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listChildDirectories(long directoryId, String directoryTypeFilters, String popedomFilters, String otherFilter, boolean keepDirectoryLink, boolean readLazyLoadProperties, SessionInfo sessionInfo, int offset, int limit) throws ServiceException {
		String pojoClassName = getShortPojoName();
		///获取子目录列表
		String hql = "select " + pojoClassName + ".id" +
					 " from " + pojoClassName + " " + pojoClassName +
					 " where " + pojoClassName + ".parentDirectoryId=" + directoryId +
					 (otherFilter==null ? "" : " and (" + otherFilter + ")") +
					 (directoryId==0 ? " and " + pojoClassName + ".id!=0" : "") +
					 (directoryTypeFilters==null || "all".equals(directoryTypeFilters) ? "" : " and " + pojoClassName + ".directoryType in ('" + directoryTypeFilters.replaceAll(",", "','") + "')");
		List directoryIds = getDatabaseService().findRecordsByHql(hql);
		if(directoryIds==null || directoryIds.isEmpty()) {
			return null;
		}
		if(popedomFilters!=null && //需要过滤
		   sessionInfo!=null && !SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //会话有效
		   childDirectoriesFilter(directoryId, directoryIds, directoryTypeFilters, popedomFilters, sessionInfo);
		}
		String ids = ListUtils.join(directoryIds, ",", false);
		if(ids==null) {
			return null;
		}
		hql = (readLazyLoadProperties ? "select " + pojoClassName + ".id, " + pojoClassName + ".directoryType" : "") +
			  " from " + pojoClassName + " " + pojoClassName +
		 	  " where " + pojoClassName + ".id in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
		 	  " order by " + pojoClassName + ".directoryType, " + pojoClassName + ".priority DESC, " + listChildDirectoriesOrderBy(directoryId);
		List directories = getDatabaseService().findRecordsByHql(hql, offset, limit);
		if(readLazyLoadProperties) {
			for(int i=0; i<directories.size(); i++) {
				Object[] values = (Object[])directories.get(i);
				long id = ((Number)values[0]).longValue();
				String directoryType = (String)values[1];
				Directory directory = (Directory)load(getDirectoryType(directoryType).getDirectoryClass(), id);
				directories.set(i, directory!=null ? directory : load(getBaseDirectoryClass(), id));
			}
		}
		//按目录类型调整顺序
		DirectoryType[] directoryTypes = getDirectoryTypes();
		List newDirectories = new ArrayList();
		for(int i=0; i<directoryTypes.length; i++) {
			List subList = ListUtils.getSubListByProperty(directories, "directoryType", directoryTypes[i].getType());
			if(subList!=null) {
				newDirectories.addAll(subList);
			}
		}
		if(!keepDirectoryLink) {
			//替换目录引用为目录
			ids = ListUtils.join(ListUtils.getSubListByType(newDirectories, DirectoryLink.class, true), "linkedDirectoryId", ",", false);
			if(directoryIds!=null && !directoryIds.isEmpty()) {
				List linkedDirectories = listDirectories(ids);
				for(int i=0; i<newDirectories.size(); i++) {
					Directory directory = (Directory)newDirectories.get(i);
					if(!(directory instanceof DirectoryLink)) {
						continue;
					}
					Directory targetDirectory = (Directory)ListUtils.findObjectByProperty(linkedDirectories, "id", new Long(((DirectoryLink)directory).getLinkedDirectoryId()));
					if(targetDirectory!=null) {
						newDirectories.set(i, targetDirectory);
					}
				}
			}
		}
		return newDirectories;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listDirectoryIds(java.lang.String, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, int, int)
	 */
	public List listDirectoryIds(String directoryTypeFilters, String popedomFilters, boolean popedomInheritDisabled, SessionInfo sessionInfo, int offset, int limit) throws ServiceException {
		String pojoClassName = getShortPojoName();
		String hql = "select distinct " + pojoClassName + ".id" +
					 " from " + pojoClassName + " " + pojoClassName + " left join " + pojoClassName + ".directoryPopedoms directoryPopedoms" +
					 " where directoryPopedoms.popedomName in ('" + popedomFilters.replaceAll(",", "','") + "')" +
					 (popedomInheritDisabled ? " and directoryPopedoms.isInherit='0'" : "") +
					 (directoryTypeFilters==null || "all".equals(directoryTypeFilters) ? "" : " and " + pojoClassName + ".directoryType in ('" + directoryTypeFilters.replaceAll(",", "','") + "')") +
					 " and directoryPopedoms.userId in (" + sessionInfo.getUserIds() + ")";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listChildDirectoriesByName(long, java.lang.String)
	 */
	public List listChildDirectoriesByName(long parentDirectoryId, String names) throws ServiceException {
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName +
					 " from " + pojoClassName + " " + pojoClassName + ", " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
					 " where " + pojoClassName + ".id=" + pojoClassName + "Subjection.directoryId" +
					 " and " + pojoClassName + "Subjection.parentDirectoryId=" + parentDirectoryId +
					 " and " + pojoClassName + ".directoryName in ('" + JdbcUtils.resetQuot(names).replaceAll(",", "','") + "')";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/**
	 * 目录过滤
	 * @param parentDierctoryId
	 * @param directoryIds
	 * @param directoryTypeFilters
	 * @param popedomFilters
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	protected void childDirectoriesFilter(long parentDierctoryId, List directoryIds, String directoryTypeFilters, String popedomFilters, SessionInfo sessionInfo) throws ServiceException {
		if(getDirectoryPopedomTypes()==null) {
			return;
		}
		String ids = ListUtils.join(directoryIds, ",", false);
		String pojoClassName = getShortPojoName();
		//获取用户有访问权限的目录列表
		String hql = "select distinct " + pojoClassName + "Popedom.directoryId" +
			  		 " from " + pojoClassName + "Popedom " + pojoClassName + "Popedom" +
			  		 " where " + pojoClassName + "Popedom.directoryId in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
			  		 (popedomFilters.equals("all") ? "" : " and " + pojoClassName + "Popedom.popedomName in ('" + popedomFilters.replaceAll(",", "','") + "')") +
			  		 " and " + pojoClassName + "Popedom.userId in (" + sessionInfo.getUserIds() + ")";
		List permitIds = getDatabaseService().findRecordsByHql(hql);
		if(permitIds==null || permitIds.size()<directoryIds.size()) { //不是对所有目录都有权限
			if(permitIds!=null && !permitIds.isEmpty()) {
				ids = null;
				//剔除掉有权限的目录ID
				for(Iterator iterator = directoryIds.iterator(); iterator.hasNext();) {
					Long id = (Long)iterator.next();
					if(permitIds.indexOf(id)==-1) {
						ids = (ids==null ? "" : ids + ",") + id;
					}
				}
			}
			//获取用户对下级目录有访问权限的目录列表
			hql = "select distinct " + pojoClassName + "Subjection.parentDirectoryId" +
				  " from " + pojoClassName + "Popedom " + pojoClassName + "Popedom, " + pojoClassName + "Subjection " + pojoClassName + "Subjection" + (directoryTypeFilters==null || "all".equals(directoryTypeFilters) ? "" : ", " + pojoClassName + " " + pojoClassName) +
				  " where " + pojoClassName + "Subjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
				  " and " + pojoClassName + "Subjection.directoryId not in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
				  (directoryTypeFilters==null || "all".equals(directoryTypeFilters) ? "" : " and " + pojoClassName + "Subjection.directoryId=" + pojoClassName + ".id and " + pojoClassName + ".directoryType in ('" + directoryTypeFilters.replaceAll(",", "','") + "')") +
				  (popedomFilters.equals("all") ? "" : " and " + pojoClassName + "Popedom.popedomName in ('" + popedomFilters.replaceAll(",", "','") + "')") +
				  " and " + pojoClassName + "Popedom.directoryId=" + pojoClassName + "Subjection.directoryId" +
				  " and " + pojoClassName + "Popedom.userId in (" + sessionInfo.getUserIds() + ")";
			List childPermitIds = getDatabaseService().findRecordsByHql(hql);
			ids = null;
			for(Iterator iterator = directoryIds.iterator(); iterator.hasNext();) {
				Long id = (Long)iterator.next();
				if((permitIds==null || permitIds.indexOf(id)==-1) && //对目录本身没有权限
				   (childPermitIds==null || childPermitIds.indexOf(id)==-1)) { //对下级目录没有权限
					iterator.remove();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listParentDirectories(long, java.lang.String)
	 */
	public List listParentDirectories(long directoryId, String traceToDirectoryType) throws ServiceException {
		if(directoryId==0) {
			return null;
		}
		String pojoClassName = getShortPojoName();
		//获取上级目录列表
		String hql = "select " + pojoClassName +
					 " from " + pojoClassName + " " + pojoClassName + ", " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
					 " where " + pojoClassName + ".id=" + pojoClassName + "Subjection.parentDirectoryId" +
					 " and " + pojoClassName + "Subjection.directoryId=" + directoryId +
					 " and " + pojoClassName + "Subjection.parentDirectoryId!=(" + directoryId + ")" +
					 " order by " + pojoClassName + "Subjection.id DESC";
		List directories = getDatabaseService().findRecordsByHql(hql);
		if(traceToDirectoryType!=null) {
			boolean found = false;
			for(int i=directories.size()-1; i>=0; i--) {
				Directory directory = (Directory)directories.get(i);
				if(found) {
					directories.remove(i);
				}
				else if(traceToDirectoryType.equals(directory.getDirectoryType())) {
					found = true;
				}
			}
		}
		return directories;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getParentDirectories(long, java.lang.String)
	 */
	public Directory getParentDirectory(long directoryId, String directoryTypes) throws ServiceException {
		if(directoryId==0) {
			return null;
		}
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName +
					 " from " + pojoClassName + " " + pojoClassName + ", " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
					 " where " + pojoClassName + ".id=" + pojoClassName + "Subjection.parentDirectoryId" +
					 " and " + pojoClassName + "Subjection.directoryId=" + directoryId +
					 " and " + pojoClassName + "Subjection.parentDirectoryId!=" + directoryId +
					 (directoryTypes==null ? "" : " and " + pojoClassName + ".directoryType in ('" + directoryTypes.replaceAll(",", "','") + "')") +
					 " order by " + pojoClassName + "Subjection.id";
		return (Directory)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#isChildDirectory(long, long)
	 */
	public boolean isChildDirectory(long childDirectoryId, long parentDirectoryId) throws ServiceException {
		if(parentDirectoryId==0) {
			return true;
		}
		if(childDirectoryId==0 || childDirectoryId==parentDirectoryId) {
			return false;
		}
		String pojoClassName = getShortPojoName() + "Subjection";
		String hql = "select  " + pojoClassName + ".id" +
					 " from " + pojoClassName + " " + pojoClassName +
					 " where " + pojoClassName + ".directoryId=" + childDirectoryId +
					 " and " + pojoClassName + ".parentDirectoryId=" + parentDirectoryId;
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listChildDirectoryIds(java.lang.String, long)
	 */
	public String filterChildDirectoryIds(String childDirectoryIds, long parentDirectoryId) throws ServiceException {
		if(parentDirectoryId==0) {
			return childDirectoryIds;
		}
		String pojoClassName = getShortPojoName() + "Subjection";
		String hql = "select  " + pojoClassName + ".directoryId" +
					 " from " + pojoClassName + " " + pojoClassName +
					 " where " + pojoClassName + ".directoryId in (" + JdbcUtils.validateInClauseNumbers(childDirectoryIds) + ")" +
					 " and " + pojoClassName + ".parentDirectoryId=" + parentDirectoryId;
		List ids = getDatabaseService().findRecordsByHql(hql);
		if(ids==null || ids.isEmpty()) {
			return null;
		}
		ids = ListUtils.sortByProperty(ids, null, childDirectoryIds);
		return ListUtils.join(ids, ",", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#createDirectoryTree(long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Tree createDirectoryTree(long rootDirectoryId, String directoryTypeFilters, String popedomFilters, String otherFilters, String extendPropertyNames, SessionInfo sessionInfo) throws ServiceException {
		List childDirectories = listChildDirectories(rootDirectoryId, directoryTypeFilters, popedomFilters, otherFilters, false, false, sessionInfo, 0, 0);
    	Directory rootDirectory = null;
		//获取需要缩进的目录类型
		String indentDirectoryTypes = null;
		DirectoryType[] directoryTypes = getDirectoryTypes();
		for(int i=0; i<directoryTypes.length; i++) {
			if(directoryTypes[i].isNavigatorIndent()) {
				indentDirectoryTypes = (indentDirectoryTypes==null ? "" : indentDirectoryTypes + ",") + directoryTypes[i].getType();
			}
		}
    	if(indentDirectoryTypes!=null && //需要缩进
    	   sessionInfo!=null && !SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //有会话且不是匿名用户
    		for(; childDirectories!=null && childDirectories.size()==1; rootDirectoryId=rootDirectory.getId()) {
    			Directory directory = (Directory)childDirectories.get(0);
    			if(("," + indentDirectoryTypes + ",").indexOf("," + directory.getDirectoryType() + ",")==-1) { //不属于需要缩进的目录类型
    				break;
    			}
	    		if(checkPopedom(rootDirectoryId, popedomFilters, sessionInfo)) { //用户有上级目录的访问权限
	    			break;
	    		}
	    		rootDirectory = directory;
	    		//获取下级目录
	    		childDirectories = listChildDirectories(rootDirectory.getId(), directoryTypeFilters, popedomFilters, otherFilters, false, false, sessionInfo, 0, 0);
	    	}
    	}
    	if(rootDirectory==null) {
    		rootDirectory = getDirectory(rootDirectoryId);
    	}
    	//转换为目录树
    	Tree tree = new Tree();
    	TreeNode rootNode = convertTreeNode(rootDirectory, childDirectories!=null && !childDirectories.isEmpty(), extendPropertyNames);
    	rootNode.setNodeIcon(Environment.getWebApplicationUrl() + getTreeRootNodeIcon());
    	rootNode.setNodeExpandIcon(rootNode.getNodeIcon());
    	if(rootNode.isHasChildNodes()) {
    		rootNode.setChildNodes(convertTreeNodes(childDirectories, directoryTypeFilters, popedomFilters, extendPropertyNames));
    	}
    	tree.setRootNode(rootNode); //设置根节点
    	return tree;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listChildTreeNodes(long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listChildTreeNodes(long parentDirectoryId, String directoryTypeFilters, String popedomFilters, String otherFilters, String extendPropertyNames, SessionInfo sessionInfo) throws ServiceException {
		List childDirectories = listChildDirectories(parentDirectoryId, directoryTypeFilters, popedomFilters, otherFilters, false, false, sessionInfo, 0, 0);
		return convertTreeNodes(childDirectories, directoryTypeFilters, popedomFilters, extendPropertyNames);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#appendParentDirectoryTypes(java.lang.String)
	 */
	public String appendParentDirectoryTypes(String directoryTypes) {
		if(directoryTypes==null || directoryTypes.isEmpty()) {
			return null;
		}
		if("all".equals(directoryTypes)) {
			return directoryTypes;
		}
		//追加父目录类型到directoryTypeFilters中
		String[] types = directoryTypes.split(",");
		directoryTypes = "," + directoryTypes + ",";
		for(int i=0; i<types.length; i++) {
			List parentTypes = getParentTypes(types[i], new ArrayList());
			for(Iterator iterator = parentTypes==null ? null : parentTypes.iterator(); iterator!=null && iterator.hasNext();) {
				String type = (String)iterator.next();
				if(directoryTypes.indexOf("," + type + ",")==-1) {
					directoryTypes += type + ",";
				}
			}
		}
		return directoryTypes.substring(1, directoryTypes.length()-1);
	}
	
	/**
	 * 递归函数:获取父目录类型,包括上级的上级
	 * @param directoryType
	 * @return
	 */
	private List getParentTypes(String directoryType, List processedTypes) {
		DirectoryType directoryTypeDefine = getDirectoryType(directoryType);
		if(directoryTypeDefine==null ||
		   directoryTypeDefine.getParentTypes()==null ||
		   directoryTypeDefine.getParentTypes().isEmpty() ||
		   directoryTypeDefine.getParentTypes().equals(directoryType)) {
			return null;
		}
		processedTypes.add(directoryType);
		List parentTypes = new ArrayList();
		String[] types = directoryTypeDefine.getParentTypes().split(",");
		for(int i=0; i<types.length; i++) {
			if(parentTypes.indexOf(types[i])!=-1) {
				continue;
			}
			parentTypes.add(types[i]);
			if(processedTypes.indexOf(types[i])!=-1) {
				continue;
			}
			//递归获取上级的上级
			List parents = getParentTypes(types[i], processedTypes);
			for(Iterator iterator = parents==null ? null : parents.iterator(); iterator!=null && iterator.hasNext();) {
				String type = (String)iterator.next();
				if(parentTypes.indexOf(type)==-1) {
					parentTypes.add(type);
				}
			}
		}
		return parentTypes;
	}
	
	/**
	 * 把目录转换为树节点
	 * @param directory
	 * @param childDirectories
	 * @param extendPropertyNames
	 * @return
	 * @throws ServiceException
	 */
	protected List convertTreeNodes(List childDirectories, String directoryTypeFilters, String popedomFilters, String extendPropertyNames) throws ServiceException {
		//设置子节点列表
		if(childDirectories==null || childDirectories.isEmpty()) {
			return null;
		}
		//检查目录是否有下级目录
		String directoryIds = ListUtils.join(childDirectories, "id", ",", false);
		String pojoClassName = getShortPojoName();
		String hql;
		if(directoryTypeFilters==null || "all".equals(directoryTypeFilters)) {
			hql = "select " + pojoClassName + "Subjection.parentDirectoryId" +
			   	  " from " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
			   	  " where " + pojoClassName + "Subjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
			   	  " and " + pojoClassName + "Subjection.directoryId not in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
		}
		else {
			hql = "select " + pojoClassName + "Subjection.parentDirectoryId" +
	   		 	  " from " + pojoClassName + "Subjection " + pojoClassName + "Subjection, " + pojoClassName + " " + pojoClassName +
	   		 	  " where " + pojoClassName + "Subjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
	   		 	  " and " + pojoClassName + "Subjection.directoryId not in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
	   		 	  " and " + pojoClassName + "Subjection.directoryId=" + pojoClassName + ".id" +
	   		 	  " and " + pojoClassName + ".directoryType in ('" + directoryTypeFilters.replaceAll(",", "','") + "')";
		}
		List hasChildrenDirectoryIds = getDatabaseService().findRecordsByHql(hql);
		List treeNodes = new ArrayList();
		for(Iterator iterator = childDirectories.iterator(); iterator.hasNext();) {
			Directory childDirectory = (Directory)iterator.next();
			treeNodes.add(convertTreeNode(childDirectory, hasChildrenDirectoryIds!=null && hasChildrenDirectoryIds.indexOf(new Long(childDirectory.getId()))!=-1, extendPropertyNames));
		}
		return treeNodes;
	}
	
	/**
	 * 把目录转换为树节点
	 * @param directory
	 * @param hasChildNodes
	 * @param extendPropertyNames
	 * @return
	 */
	protected TreeNode convertTreeNode(Directory directory, boolean hasChildNodes, String extendPropertyNames) {
		TreeNode treeNode = new TreeNode();
		treeNode.setNodeId("" + directory.getId()); //节点ID
		treeNode.setNodeText(StringUtils.slice(directory.getDirectoryName(), 100, "...")); //节点文本
		treeNode.setNodeType(directory.getDirectoryType()); //节点类型
		DirectoryType directoryType = getDirectoryType(directory.getDirectoryType());
		treeNode.setNodeIcon(Environment.getWebApplicationUrl() + directoryType.getIcon()); //节点图标
		treeNode.setNodeExpandIcon(directoryType.getExpandIcon()==null ? null : Environment.getWebApplicationUrl() + directoryType.getExpandIcon()); //节点展开时的图标
		treeNode.setHasChildNodes(hasChildNodes); //是否有子节点
		treeNode.setExpandTree(false); //是否需要展开
		if(extendPropertyNames!=null) {
			String[] propertyNames = extendPropertyNames.split(",");
			List properties = new ArrayList();
			for(int i=0; i<propertyNames.length; i++) {
				try {
					String value = StringUtils.format(PropertyUtils.getProperty(directory, propertyNames[i]), null, "");
					properties.add(new Attribute(propertyNames[i], value));
				}
				catch(Exception e) {
					
				}
			}
			treeNode.setExtendNodeProperties(properties.isEmpty() ? null : properties); //扩展属性列表
		}
		return treeNode;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#getApplicationNavigator(java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ApplicationNavigator getApplicationNavigator(String applicationName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		Tree tree = createDirectoryTree(0, null, getNavigatorPopedomFilters(), null, null, sessionInfo);
		retrieveApplicationTreeNodeDataURL(tree.getRootNode()); //设置获取数据的URL
		return new ApplicationTreeNavigator(tree);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#listApplicationNavigatorTreeNodes(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listApplicationNavigatorTreeNodes(String applicationPath, String parentTreeNodeId, SessionInfo sessionInfo) throws ServiceException {
		List treeNodes = listChildTreeNodes(Long.parseLong(parentTreeNodeId), null, getNavigatorPopedomFilters(), null, null, sessionInfo);
		if(treeNodes==null || treeNodes.isEmpty()) {
			return null;
		}
		for(Iterator iterator = treeNodes.iterator(); iterator.hasNext();) {
			TreeNode node = (TreeNode)iterator.next();
			retrieveApplicationTreeNodeDataURL(node);
		}
		return treeNodes;
	}
	
	/**
	 * 递归函数:设置获取数据的URL
	 * @param treeNode
	 * @return
	 */
	private void retrieveApplicationTreeNodeDataURL(TreeNode treeNode) {
		//设置获取数据URL
		String dataUrl = getNavigatorDataUrl(Long.parseLong(treeNode.getNodeId()), treeNode.getNodeType());
		if(dataUrl.startsWith("/")) {
			dataUrl = Environment.getWebApplicationUrl() + dataUrl;
		}
		treeNode.setExtendPropertyValue("dataUrl", dataUrl);
		//设置子节点的获取数据URL
		if(treeNode.getChildNodes()!=null) {
			for(Iterator iterator = treeNode.getChildNodes().iterator(); iterator.hasNext();) {
				TreeNode childNode = (TreeNode)iterator.next();
				retrieveApplicationTreeNodeDataURL(childNode);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectoryByFullName(java.lang.String, java.lang.String)
	 */
	public Directory getDirectoryByFullName(String fullName, String delimiter) throws ServiceException {
		String[] names = fullName.split(delimiter);
		String pojoClassName = getShortPojoName();
		Number id = new Long(0);
		for(int i = 1; i<names.length; i++) {
			String hql = "select " + pojoClassName + ".id" +
						 " from " + pojoClassName + " " + pojoClassName + "" +
						 " where " + pojoClassName + ".parentDirectoryId=" + id +
						 " and " + pojoClassName + ".directoryName='" + JdbcUtils.resetQuot(names[i]) + "'";
			id = (Number)getDatabaseService().findRecordByHql(hql);
			if(id==null) {
				throw new ServiceException("unknown directory");
			}
		}
		return getDirectory(id.longValue());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#getDirectoryByName(long, java.lang.String)
	 */
	public Directory getDirectoryByName(long parentDirectoryId, String directoryName, boolean sonDirectoryOnly) throws ServiceException {
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName + ".id" +
					 " from " + pojoClassName + " " + pojoClassName + "" + (sonDirectoryOnly || parentDirectoryId==0 ? "" : "," + pojoClassName + "Subjection " + pojoClassName + "Subjection") +
					 " where " + pojoClassName + ".directoryName='" + JdbcUtils.resetQuot(directoryName) + "'" +
					 (!sonDirectoryOnly ? "" : " and " + pojoClassName + ".parentDirectoryId=" + parentDirectoryId) +
					 (sonDirectoryOnly || parentDirectoryId==0 ? "" : " and " + pojoClassName + ".id=" + pojoClassName + "Subjection.directoryId and " + pojoClassName + "Subjection.parentDirectoryId=" + parentDirectoryId);
		Number id = (Number)getDatabaseService().findRecordByHql(hql);
		return id==null ? null : getDirectory(id.longValue());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listDirectoriesByNames(long, java.lang.String, boolean)
	 */
	public List listDirectoriesByNames(long parentDirectoryId, String directoryNames, boolean sonDirectoryOnly) throws ServiceException {
		if(directoryNames==null || directoryNames.isEmpty()) {
			return null;
		}
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName + ".id" +
					 " from " + pojoClassName + " " + pojoClassName + "" + (sonDirectoryOnly || parentDirectoryId==0 ? "" : "," + pojoClassName + "Subjection " + pojoClassName + "Subjection") +
					 " where " + pojoClassName + ".directoryName in ('" + JdbcUtils.resetQuot(directoryNames).replaceAll(",", "','") + "')" +
					 (!sonDirectoryOnly ? "" : " and " + pojoClassName + ".parentDirectoryId=" + parentDirectoryId) +
					 (sonDirectoryOnly || parentDirectoryId==0 ? "" : " and " + pojoClassName + ".id=" + pojoClassName + "Subjection.directoryId and " + pojoClassName + "Subjection.parentDirectoryId=" + parentDirectoryId);
		List directoryIds = getDatabaseService().findRecordsByHql(hql);
		List directories = listDirectories(ListUtils.join(directoryIds, ",", false));
		return ListUtils.sortByProperty(directories, "directoryName", directoryNames);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listAllChildDirectories(long, boolean)
	 */
	public List listAllChildDirectories(long directoryId, String childDirectoryTypes, boolean keepDirectoryLink) throws ServiceException {
		List directories = new ArrayList();
		listAllChildDirectories(directoryId, childDirectoryTypes, directories, keepDirectoryLink);
		return directories.isEmpty() ? null : directories;
	}
	
	/**
	 * 获取所有的子目录
	 * @param directoryId
	 * @param directories
	 * @param keepDirectoryLink
	 * @throws ServiceException
	 */
	private void listAllChildDirectories(long directoryId, String childDirectoryTypes, List allDirectories, boolean keepDirectoryLink) throws ServiceException {
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName +
					 " from " + pojoClassName + " " + pojoClassName + "," + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
				 	 " where " + pojoClassName + ".id=" + pojoClassName + "Subjection.directoryId"  + 
				 	 " and " + pojoClassName + "Subjection.parentDirectoryId=" + directoryId +
				 	 " and " + pojoClassName + "Subjection.directoryId!=" + directoryId + 
				 	 (childDirectoryTypes==null ? "" : " and " + pojoClassName + ".directoryType in ('" + JdbcUtils.resetQuot(childDirectoryTypes).replaceAll(",", "','") + "')");
		List directories = getDatabaseService().findRecordsByHql(hql);
		//如果不保留目录引用,则从目录列表中剔除
		List directoryLinks = keepDirectoryLink ? null : ListUtils.removeObjectsByType(directories, DirectoryLink.class, true);
		for(Iterator iterator = directories==null ? null : directories.iterator(); iterator!=null && iterator.hasNext();) {
			Directory directory = (Directory)iterator.next();
			if(ListUtils.findObjectByProperty(allDirectories, "id", new Long(directory.getId()))==null) {
				allDirectories.add(directory);
			}
		}
		for(Iterator iterator = directoryLinks==null ? null : directoryLinks.iterator(); iterator!=null && iterator.hasNext();) {
			DirectoryLink directoryLink = (DirectoryLink)iterator.next();
			//检查目录是否已经在列表中
			if(ListUtils.findObjectByProperty(allDirectories, "id", new Long(directoryLink.getLinkedDirectoryId()))!=null) {
				continue;
			}
			//获取目录
			allDirectories.add(getDatabaseService().findRecordById(getBaseDirectoryClass().getName(), directoryLink.getLinkedDirectoryId()));
			//获取下级目录
			listAllChildDirectories(directoryLink.getLinkedDirectoryId(), childDirectoryTypes, allDirectories, keepDirectoryLink);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listDirectoryIdsByName(long, java.lang.String, java.lang.String)
	 */
	public List listDirectoryIdsByName(long parentDirectoryId, String directoryNames, String childDirectoryTypes) throws ServiceException {
		String directoryIds;
		if(childDirectoryTypes!=null && (childDirectoryTypes.isEmpty() || "all".equals(childDirectoryTypes))) {
			childDirectoryTypes = null;
		}
		if(childDirectoryTypes==null) {
			directoryIds = "" + parentDirectoryId;
		}
		else {
			directoryIds = getChildDirectoryIds(parentDirectoryId + "", childDirectoryTypes); //获取下级目录中的栏目
		}
		if(directoryIds==null || directoryIds.isEmpty()) {
			return null;
		}
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName + ".id" +
					 " from " + pojoClassName + " " + pojoClassName + ", " + pojoClassName + "Subjection " + pojoClassName + "Subjection" +
					 " where " + pojoClassName + ".id=" + pojoClassName + "Subjection.directoryId" +
					 " and " + pojoClassName + "Subjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
					 " and " + pojoClassName + ".directoryName in ('" + JdbcUtils.resetQuot(directoryNames).replaceAll(",", "','") + "')" +
					 (childDirectoryTypes==null ? "" : " and " + pojoClassName + ".directoryType in ('" + childDirectoryTypes.replaceAll(",", "','") + "')");
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#isDirectoryExists(long)
	 */
	public boolean isDirectoryExists(long directoryId) {
		String pojoClassName = getShortPojoName();
		String hql = "select " + pojoClassName + ".id" +
					 " from " + pojoClassName + " " + pojoClassName +
					 " where " + pojoClassName + ".id=" + directoryId;
		return getDatabaseService().findRecordsByHql(hql)!=null;
	}

	/**
	 * 从权限类型配置中生成需要被过滤的权限类型列表
	 * @return
	 */
	private String getNavigatorPopedomFilters() {
		String popedomFilters = null;
		DirectoryPopedomType[] popedomTypes = getDirectoryPopedomTypes();
		if(popedomTypes==null) {
			return null;
		}
		for(int i=0; i<popedomTypes.length; i++) {
			if(popedomTypes[i].isNavigatorFilter()) {
				popedomFilters = (popedomFilters==null ? "" : popedomFilters + ",") + popedomTypes[i].getName();
			}
		}
		return popedomFilters;
	}

	/**
	 * 获取pojo类名称
	 * @return
	 */
	private String getShortPojoName() {
		return getBaseDirectoryClass().getSimpleName();
	}
	
	/**
	 * 获取目录类型定义
	 * @param directoryType
	 * @return
	 * @throws ServiceException
	 */
	private DirectoryType getDirectoryType(String directoryType) {
		DirectoryType[] directoryTypes = getDirectoryTypes();
		for(int i=0; i<directoryTypes.length; i++) {
			if(directoryType.equals(directoryTypes[i].getType())) {
				return directoryTypes[i];
			}
		}
		return null;
	}
	
	/**
	 * 按类来查找目录类型
	 * @param directoryClass
	 * @return
	 */
	private DirectoryType getDirectoryTypeByClass(Class directoryClass) {
		DirectoryType[] directoryTypes = getDirectoryTypes();
		for(int i=0; i<directoryTypes.length; i++) {
			if(directoryClass.equals(directoryTypes[i].getDirectoryClass())) {
				return directoryTypes[i];
			}
		}
		return null;
	}
	
	/**
	 * 获取权限类型定义
	 * @param directoryType
	 * @return
	 * @throws ServiceException
	 */
	private DirectoryPopedomType getDirectoryPopedomType(String popedomName) {
		DirectoryPopedomType[] popedomTypes = getDirectoryPopedomTypes();
		if(popedomTypes==null) {
			return null;
		}
		for(int i=0; i<popedomTypes.length; i++) {
			if(popedomName.equals(popedomTypes[i].getName())) {
				return popedomTypes[i];
			}
		}
		return null;
	}
	
	/**
	 * 根据目录类型获取权限类型列表
	 * @param directoryType
	 * @return
	 */
	private List listDirectoryPopedomTypes(String directoryType) {
		DirectoryPopedomType[] popedomTypes = getDirectoryPopedomTypes();
		if(popedomTypes==null) {
			return null;
		}
		List directoryPopedomTypes = new ArrayList();
		for(int i=0; i<popedomTypes.length; i++) {
			if(popedomTypes[i].getDirectoryTypes()==null ||
			   "all".equals(popedomTypes[i].getDirectoryTypes()) ||
			   ("," + popedomTypes[i].getDirectoryTypes() + ",").indexOf("," + directoryType + ",")!=-1) {
				directoryPopedomTypes.add(popedomTypes[i]);
			}
		}
		return directoryPopedomTypes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.DirectoryService#listDirectoryVisitors(long, java.lang.String, boolean, boolean, int)
	 */
	public List listDirectoryVisitors(long directoryId, String popedomName, boolean myVisitorsOnly, boolean asPerson, int max) throws ServiceException {
		Directory directory = (Directory)getDirectory(directoryId);
		//获取站点编辑或管理员
		List visitors = myVisitorsOnly ? directory.getMyVisitors(popedomName) : ListUtils.getSubListByProperty(directory.getDirectoryPopedoms(), "popedomName", popedomName);
		if(visitors==null || visitors.isEmpty()) { //没有配置人员,递归获取上级的设置
			return directoryId==0 || myVisitorsOnly ? null : listDirectoryVisitors(directory.getParentDirectoryId(), popedomName, myVisitorsOnly, asPerson, max);
		}
		if(!asPerson) { //不转换为用户列表
			return visitors;
		}
		//转换为用户列表
		PersonService personService = (PersonService)Environment.getService("personService"); //用户服务
		OrgService orgService = (OrgService)Environment.getService("orgService"); //组织机构服务
		RoleService roleService = (RoleService)Environment.getService("roleService"); //角色服务
		List persons = new ArrayList();
		for(Iterator iterator = visitors.iterator(); iterator.hasNext() && (max==0 || persons.size()<max);) {
			DirectoryPopedom popedom  = (DirectoryPopedom)iterator.next();
			Person person = personService.getPerson(popedom.getUserId());
			if(person!=null) { //个人
				persons.add(person);
				continue;
			}
			List orgPersons = orgService.listOrgPersons("" + popedom.getUserId(), null, true, false, 0, (max==0 ? 0 : max-persons.size()));
			if(orgPersons!=null && !orgPersons.isEmpty()) { //部门
				persons.addAll(orgPersons);
				continue;
			}
			//角色
			List roleMembers = roleService.listRoleMembers("" + popedom.getUserId());
			if(roleMembers!=null && !roleMembers.isEmpty()) {
				persons.addAll(roleMembers.size()<max-persons.size() ? roleMembers : roleMembers.subList(0, max-persons.size()));
			}
		}
		return persons;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#saveApplicationNavigatorDefinition(java.lang.String, com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition)
	 */
	public void saveApplicationNavigatorDefinition(String applicationName, ApplicationNavigatorDefinition navigatorDefinition) throws ServiceException {
		
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
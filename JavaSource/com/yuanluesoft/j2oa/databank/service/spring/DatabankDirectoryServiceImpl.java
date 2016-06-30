package com.yuanluesoft.j2oa.databank.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.j2oa.databank.pojo.DatabankData;
import com.yuanluesoft.j2oa.databank.pojo.DatabankDirectory;
import com.yuanluesoft.j2oa.databank.pojo.DatabankDirectoryPopedom;
import com.yuanluesoft.j2oa.databank.pojo.DatabankDirectorySubjection;
import com.yuanluesoft.j2oa.databank.service.DatabankDataService;
import com.yuanluesoft.j2oa.databank.service.DatabankDirectoryService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.services.InitializeService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class DatabankDirectoryServiceImpl extends DirectoryServiceImpl implements DatabankDirectoryService, InitializeService {
	private AttachmentService attachmentService; //附件管理
	private DatabankDataService databankDataService; //资料服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDirectory(0)!=null) {
			return false;
		}
		DatabankDirectory directory = new DatabankDirectory();
		directory.setId(0);
		directory.setDirectoryName("资料库");
		directory.setDirectoryType("directory");
		getDatabaseService().saveRecord(directory);
		
		//更新隶属关系
		DatabankDirectorySubjection subjection = new DatabankDirectorySubjection();
		subjection.setId(UUIDLongGenerator.generateId()); //ID
		subjection.setDirectoryId(0); //目录ID
		subjection.setParentDirectoryId(0); //上级ID
		getDatabaseService().saveRecord(subjection);
		
		//授权,设置管理员
		DatabankDirectoryPopedom popedom = new DatabankDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("manager"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		
		//设置所有人为访问者
		popedom = new DatabankDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(0); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(systemName); //用户名
		popedom.setPopedomName("visitor"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return DatabankDirectory.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryLinkClass()
	 */
	public Class getDirectoryLinkClass() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryTypes()
	 */
	public DirectoryType[] getDirectoryTypes() {
		return new DirectoryType[] {
				new DirectoryType("directory", "directory", "目录", DatabankDirectory.class, "/j2oa/databank/icons/directory.gif", null, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedomTypes()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		return new DirectoryPopedomType[] {
				new DirectoryPopedomType("manager", "管理员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, true, true),
				new DirectoryPopedomType("visitor", "访问者", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_WHEN_EMPTY, false, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/j2oa/databank/dataView.shtml?directoryId=" + directoryId;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/j2oa/databank/icons/root.gif";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		return newDirectory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		for(int i=0; ; i+=100) {
			List dataList = getDatabaseService().findRecordsByKey(DatabankData.class.getName(), "directoryId", new Long(directory.getId()));
	        if(dataList==null || dataList.isEmpty()) {
	        	break;
	        }
	        for(Iterator iterator = dataList.iterator(); iterator.hasNext();) {
	        	DatabankData data = (DatabankData)iterator.next();
	        	//删除
	        	databankDataService.delete(data);
	        }
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#childDirectoriesFilter(long, java.util.List, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void childDirectoriesFilter(long parentDierctoryId, List directoryIds, String directoryTypeFilters, String popedomFilters, SessionInfo sessionInfo) throws ServiceException {
		List directoryIdsBak = new ArrayList(directoryIds);
		super.childDirectoriesFilter(parentDierctoryId, directoryIds, directoryTypeFilters, popedomFilters, sessionInfo);
		if(directoryIds.size()==directoryIdsBak.size()) { //没有目录被过滤
			return;
		}
		//有目录被过滤,检查用户对目录中的文件的访问权限
		//获取被删除目录
		for(Iterator iterator = directoryIdsBak.iterator(); iterator.hasNext();) {
			Long directoryId = (Long)iterator.next();
			if(directoryIds.indexOf(directoryId)!=-1) { //目录ID没有被删除
				iterator.remove();
			}
		}
		String ids = ListUtils.join(directoryIdsBak, ",", false);
		//获取有资料访问访问权限的子目录
		String hql = "select distinct DatabankDirectory.id" +
					 " from DatabankDirectory DatabankDirectory, DatabankData DatabankData, DatabankDataPrivilege DatabankDataPrivilege" +
					 " where DatabankDirectory.id in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
					 " and DatabankData.directoryId=DatabankDirectory.id" +
					 " and DatabankDataPrivilege.recordId=DatabankData.id" +
					 " and DatabankDataPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")";
		List dataAccessDirectoryIds = getDatabaseService().findRecordsByHql(hql);
		if(dataAccessDirectoryIds!=null && !dataAccessDirectoryIds.isEmpty()) {
			directoryIds.addAll(dataAccessDirectoryIds);
			if(dataAccessDirectoryIds.size()==directoryIdsBak.size()) {
				return;
			}
			//剔除有资料访问访问权限的子目录
			for(Iterator iterator = directoryIdsBak.iterator(); iterator.hasNext();) {
				Long directoryId = (Long)iterator.next();
				if(dataAccessDirectoryIds.indexOf(directoryId)!=-1) {
					iterator.remove();
				}
			}
			ids = ListUtils.join(directoryIdsBak, ",", false);
		}
		//获取有下级目录资料访问访问权限的子目录
		hql = "select distinct DatabankDirectorySubjection.parentDirectoryId" +
			  " from DatabankDirectorySubjection DatabankDirectorySubjection, DatabankData DatabankData, DatabankDataPrivilege DatabankDataPrivilege" +
			  " where DatabankDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
			  " and DatabankDirectorySubjection.directoryId not in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
			  " and DatabankData.directoryId=DatabankDirectorySubjection.id" +
			  " and DatabankDataPrivilege.recordId=DatabankData.id" +
			  " and DatabankDataPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")";
		dataAccessDirectoryIds = getDatabaseService().findRecordsByHql(hql);
		if(dataAccessDirectoryIds!=null) {
			directoryIds.addAll(dataAccessDirectoryIds);
		}
	}

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(
			AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	/**
	 * @return the databankDataService
	 */
	public DatabankDataService getDatabankDataService() {
		return databankDataService;
	}

	/**
	 * @param databankDataService the databankDataService to set
	 */
	public void setDatabankDataService(DatabankDataService databankDataService) {
		this.databankDataService = databankDataService;
	}
}
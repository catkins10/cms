package com.yuanluesoft.cms.onlineservice.service.spring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectoryPopedom;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectorySubjection;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceMainDirectory;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.services.InitializeService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceDirectoryServiceImpl extends DirectoryServiceImpl implements OnlineServiceDirectoryService, InitializeService {
	private OnlineServiceItemService onlineServiceItemService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDirectory(0)!=null) {
			return false;
		}
		OnlineServiceMainDirectory directory = new OnlineServiceMainDirectory();
		directory.setId(0);
		directory.setDirectoryName("网上办事");
		directory.setDirectoryType("mainDirectory");
		save(directory);
		
		//更新隶属关系
		OnlineServiceDirectorySubjection subjection = new OnlineServiceDirectorySubjection();
		subjection.setId(UUIDLongGenerator.generateId()); //ID
		subjection.setDirectoryId(0); //目录ID
		subjection.setParentDirectoryId(0); //上级ID
		getDatabaseService().saveRecord(subjection);
		
		//授权
		OnlineServiceDirectoryPopedom popedom = new OnlineServiceDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("manager"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		
		popedom = new OnlineServiceDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("transactor"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return OnlineServiceDirectory.class;
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
				new DirectoryType("mainDirectory", "mainDirectory", "主目录", OnlineServiceMainDirectory.class, "/cms/onlineservice/icons/mainDirectory.gif", null, true),
				new DirectoryType("directory", "mainDirectory,directory", "目录", OnlineServiceDirectory.class, "/cms/onlineservice/icons/directory.gif", null, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedomTypes()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		return new DirectoryPopedomType[] {
				new DirectoryPopedomType("manager", "管理员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, true, true),
				new DirectoryPopedomType("transactor", "经办人", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_WHEN_EMPTY, false, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/cms/onlineservice/admin/serviceItemView.shtml?directoryId=" + directoryId;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/cms/onlineservice/icons/root.gif";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		OnlineServiceDirectory serviceDirectory = (OnlineServiceDirectory)newDirectory;
		if(serviceDirectory instanceof OnlineServiceMainDirectory) {
			((OnlineServiceMainDirectory)serviceDirectory).setSiteId(0); //隶属站点ID,仅对根目录有效
			((OnlineServiceMainDirectory)serviceDirectory).setSiteName(null); //隶属站点名称
		}
		serviceDirectory.setAcceptWorkflowName(null); //在线受理流程
		serviceDirectory.setAcceptWorkflowId(null); //在线受理流程ID
		serviceDirectory.setComplaintWorkflowName(null); //在线投诉流程
		serviceDirectory.setComplaintWorkflowId(null); //在线投诉流程ID
		serviceDirectory.setConsultWorkflowName(null); //在线咨询流程
		serviceDirectory.setConsultWorkflowId(null); //在线咨询流程ID
		return newDirectory;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		String hql = "select OnlineServiceItem" +
					 " from OnlineServiceItem OnlineServiceItem" +
					 " left join OnlineServiceItem.subjections OnlineServiceItemSubjection" +
					 " where OnlineServiceItemSubjection.directoryId=" + directory.getId();
		List items = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections", ","));
		if(items==null || items.isEmpty()) {
			return;
		}
		for(Iterator iterator = items.iterator(); iterator.hasNext(); ) {
			OnlineServiceItem item = (OnlineServiceItem)iterator.next();
			if(item.getSubjections().size()<2) { //办理事项只属于一个栏目
				onlineServiceItemService.delete(item); //删除信息
			}
			else { //办理事项只属于多个栏目
				//更新隶属关系
				Set subjections = new HashSet(item.getSubjections());
				ListUtils.removeObjectByProperty(subjections, "directoryId", new Long(directory.getId()));
				onlineServiceItemService.updateServiceItemSubjectios(item, false, ListUtils.join(subjections, "directoryId", ",", false));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#rebuildStaticPageForUpdatedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	protected boolean rebuildStaticPageForUpdatedDirectory(Directory newDirectory, Directory oldDirectory) throws ServiceException {
		OnlineServiceDirectory newDir = (OnlineServiceDirectory)newDirectory;
		OnlineServiceDirectory oldDir = (OnlineServiceDirectory)oldDirectory;
		if(newDir.getHalt()!=oldDir.getHalt()) {
			return true;
		}
		return super.rebuildStaticPageForUpdatedDirectory(newDirectory, oldDirectory);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof OnlineServiceDirectory) { //目录
			OnlineServiceDirectory directory = (OnlineServiceDirectory)record;
			if(directory.getItemSynchSiteIds()!=null && directory.getItemSynchSiteIds().isEmpty()) {
				directory.setItemSynchSiteIds(null);
			}
			if(directory.getComplaintSynchSiteIds()!=null && directory.getComplaintSynchSiteIds().isEmpty()) {
				directory.setComplaintSynchSiteIds(null);
			}
			if(directory.getConsultSynchSiteIds()!=null && directory.getConsultSynchSiteIds().isEmpty()) {
				directory.setConsultSynchSiteIds(null);
			}
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(!(record instanceof OnlineServiceDirectory)) { //不是网上办事目录
			return super.update(record);
		}
		OnlineServiceDirectory directory = (OnlineServiceDirectory)record;
		if(directory.getItemSynchSiteIds()!=null && directory.getItemSynchSiteIds().isEmpty()) {
			directory.setItemSynchSiteIds(null);
		}
		if(directory.getComplaintSynchSiteIds()!=null && directory.getComplaintSynchSiteIds().isEmpty()) {
			directory.setComplaintSynchSiteIds(null);
		}
		if(directory.getConsultSynchSiteIds()!=null && directory.getConsultSynchSiteIds().isEmpty()) {
			directory.setConsultSynchSiteIds(null);
		}
		//获取目录原来需要同步的站点ID列表
		String hql = "select OnlineServiceDirectory.parentDirectoryId, OnlineServiceDirectory.itemSynchSiteIds, OnlineServiceDirectory.complaintSynchSiteIds, OnlineServiceDirectory.consultSynchSiteIds" +
					 " from OnlineServiceDirectory OnlineServiceDirectory" +
					 " where OnlineServiceDirectory.id=" + directory.getId();
		Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
		//更新目录
		record = super.update(record);
		boolean parentDirectoryChanged = ((Number)values[0]).longValue()!=directory.getParentDirectoryId(); //父目录是否已经改变
		if(parentDirectoryChanged || //父目录已经修改
		   !StringUtils.isEquals(values[1]==null || ((String)values[1]).isEmpty() ? null : (String)values[1], directory.getItemSynchSiteIds())) { //同步栏目已经改变
			onlineServiceItemService.resynchServiceItems(directory.getId()); //重新同步办理事项
		}
		if(parentDirectoryChanged || //父目录已经修改
		   !StringUtils.isEquals(values[2]==null || ((String)values[2]).isEmpty() ? null : (String)values[2], directory.getItemSynchSiteIds())) { //同步栏目已经改变
			//onlineServiceItemService.resynchServiceItems(directory.getId()); //重新同步办理事项
		}
		if(parentDirectoryChanged || //父目录已经修改
		   !StringUtils.isEquals(values[3]==null || ((String)values[3]).isEmpty() ? null : (String)values[3], directory.getItemSynchSiteIds())) { //同步栏目已经改变
			//onlineServiceItemService.resynchServiceItems(directory.getId()); //重新同步办理事项
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService#getMainDirectory(long)
	 */
	public OnlineServiceMainDirectory getMainDirectory(long directoryId) throws ServiceException {
		String hql = "select OnlineServiceMainDirectory" +
					 " from OnlineServiceMainDirectory OnlineServiceMainDirectory, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
					 " where OnlineServiceDirectorySubjection.directoryId=" + directoryId + 
					 " and OnlineServiceMainDirectory.id=OnlineServiceDirectorySubjection.parentDirectoryId" +
					 " order by OnlineServiceDirectorySubjection.id";
		return (OnlineServiceMainDirectory)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService#getDirectoryBySiteId(long)
	 */
	public OnlineServiceDirectory getDirectoryBySiteId(long siteId) throws ServiceException {
		//按站点获取根目录
		String hql = "from OnlineServiceDirectory OnlineServiceDirectory" +
					 " where OnlineServiceDirectory.siteId=" + siteId +
					 " and OnlineServiceDirectory.siteName is not null" +
					 " order by OnlineServiceDirectory.id";
		return (OnlineServiceDirectory)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		String serviceItemId = RequestUtils.getParameterStringValue(notifyRequest, "serviceItemId"); //获取办理事项ID
		String target = RequestUtils.getParameterStringValue(notifyRequest, "target");
		String workflowName = null;
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程已删除
			workflowId = null;
		}
		else {
			workflowName = workflowPackage.getName();
		}
		if(serviceItemId!=null) { //办理事项
			OnlineServiceItem onlineServiceItem = onlineServiceItemService.getOnlineServiceItem(Long.parseLong(serviceItemId));
			PropertyUtils.setProperty(onlineServiceItem, target + "WorkflowId", workflowId);
			PropertyUtils.setProperty(onlineServiceItem, target + "WorkflowName", workflowName);
			onlineServiceItemService.update(onlineServiceItem);
		}
		else { //办事目录
			OnlineServiceDirectory directory = (OnlineServiceDirectory)getDirectory(RequestUtils.getParameterLongValue(notifyRequest, "directoryId"));
			PropertyUtils.setProperty(directory, target + "WorkflowId", workflowId);
			PropertyUtils.setProperty(directory, target + "WorkflowName", workflowName);
			update(directory);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService#getSynchSiteIds(long, java.lang.String)
	 */
	public String getSynchSiteIds(String directoryIds, String dataType) throws ServiceException {
		String hql = "select OnlineServiceDirectory." + dataType + "SynchSiteIds" +
			  		 " from OnlineServiceDirectory OnlineServiceDirectory, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
			  		 " where OnlineServiceDirectorySubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
			  		 " and OnlineServiceDirectory.id=OnlineServiceDirectorySubjection.parentDirectoryId" +
			  		 " and not (OnlineServiceDirectory." + dataType + "SynchSiteIds is null)" +
			  		 " order by OnlineServiceDirectorySubjection.id";
		List records = getDatabaseService().findRecordsByHql(hql);
		if(records==null || records.isEmpty()) {
			return null;
		}
		//合并同步的站点ID列表
		String synchSiteIds = null;
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
			String ids = (String)iterator.next();
			if(ids==null || ids.equals("")) {
				continue;
			}
			if(synchSiteIds==null) {
				synchSiteIds = ids;
			}
			else {
				String[] idArray = ids.split(",");
				for(int i=0; i<idArray.length; i++) {
					if(("," + synchSiteIds + ",").indexOf("," + idArray[i] + ",")==-1) {
						synchSiteIds += "," + idArray[i];
					}
				}
			}
		}
		return synchSiteIds;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService#getWorkflowId(long, java.lang.String)
	 */
	public String getWorkflowId(long directoryId, String worflowType) throws ServiceException {
		String hql = "select OnlineServiceDirectory." + worflowType + "WorkflowId" +
			 		 " from OnlineServiceDirectory OnlineServiceDirectory, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
			 		 " where OnlineServiceDirectorySubjection.directoryId=" + directoryId +
			 		 " and OnlineServiceDirectory.id=OnlineServiceDirectorySubjection.parentDirectoryId" +
			 		 " and not OnlineServiceDirectory." + worflowType + "WorkflowId is null" +
			 		 " order by OnlineServiceDirectorySubjection.id";
		return (String)getDatabaseService().findRecordByHql(hql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("childDirectories".equals(itemsName)) { //子目录
			String parentDirectoryIds = StringUtils.getPropertyValue((String)request.getAttribute("fieldExtendProperties"), "parentDirectoryIds");
			if(parentDirectoryIds==null || parentDirectoryIds.isEmpty()) {
				parentDirectoryIds = "" + RequestUtils.getParameterLongValue(request, "directoryId");
			}
			String hql = "select OnlineServiceDirectory.directoryName, OnlineServiceDirectory.id" +
						 " from OnlineServiceDirectory OnlineServiceDirectory" +
						 " where OnlineServiceDirectory.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(parentDirectoryIds) + ")" +
						 " and OnlineServiceDirectory.halt!='1'" +
						 " order by OnlineServiceDirectory.priority DESC, OnlineServiceDirectory.directoryName";
			return getDatabaseService().findRecordsByHql(hql);
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * @return the onlineServiceItemService
	 */
	public OnlineServiceItemService getOnlineServiceItemService() {
		return onlineServiceItemService;
	}

	/**
	 * @param onlineServiceItemService the onlineServiceItemService to set
	 */
	public void setOnlineServiceItemService(
			OnlineServiceItemService onlineServiceItemService) {
		this.onlineServiceItemService = onlineServiceItemService;
	}
}
package com.yuanluesoft.cms.infopublic.service.spring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.infopublic.pojo.PublicArticleDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectoryCategory;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectoryPopedom;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectorySubjection;
import com.yuanluesoft.cms.infopublic.pojo.PublicGuide;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 目录服务
 * @author yuanluesoft
 *
 */
public class PublicDirectoryServiceImpl extends DirectoryServiceImpl implements PublicDirectoryService {
	private SiteService siteService; //站点服务
	private PublicInfoService publicInfoService; //信息服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return PublicDirectory.class;
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
				new DirectoryType("main", "main", "主目录", PublicMainDirectory.class, "/cms/infopublic/icons/main.gif", null, true),
				new DirectoryType("article", "main", "文章目录", PublicArticleDirectory.class, "/cms/infopublic/icons/etc.gif", null, false),
				new DirectoryType("category", "main,category", "信息分类", PublicDirectoryCategory.class, "/cms/infopublic/icons/category.gif", null, false),
				new DirectoryType("info", "main,category", "信息目录", PublicInfoDirectory.class, "/cms/infopublic/icons/info.gif", null, false)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedomTypes()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		return new DirectoryPopedomType[] {
				new DirectoryPopedomType("manager", "管理员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, true, true),
				new DirectoryPopedomType("editor", "信息编辑", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/cms/infopublic/icons/root.gif";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/cms/infopublic/admin/publicInfoView.shtml?directoryId=" + directoryId;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		if(newDirectory instanceof PublicMainDirectory) {
			PublicMainDirectory mainDirectory = (PublicMainDirectory)newDirectory;
			mainDirectory.setCode("请修改机构代码"); //类目代码,当类型为根目录时，填写机构代码，否则是目录代码
			mainDirectory.setDescription(null); //描述
			mainDirectory.setSynchSiteIds(null); //同步更新到网站栏目列表
			mainDirectory.setWorkflowId(0); //绑定的流程ID,添加信息时,查找当前目录绑定的流程，如果没有则查询上级目录
			mainDirectory.setWorkflowName(null); //绑定的流程名称
			mainDirectory.setSiteId(0); //隶属站点ID,仅对根目录有效
			mainDirectory.setSiteName(null); //隶属站点名称,仅对根目录有效
			mainDirectory.setManualCodeEnabled('0'); //允许手工编制索引号,仅对根目录有效,当手工编制的索引号存在时,就不再自动编号
		}
		else if(newDirectory instanceof PublicDirectoryCategory) {
			PublicDirectoryCategory directoryCategory = (PublicDirectoryCategory)newDirectory;
			//directoryCategory.setCode(null); //类目代码,当类型为根目录时，填写机构代码，否则是目录代码
			directoryCategory.setDescription(null); //描述
			directoryCategory.setSynchSiteIds(null); //同步更新到网站栏目列表
			directoryCategory.setWorkflowId(0); //绑定的流程ID,添加信息时,查找当前目录绑定的流程，如果没有则查询上级目录
			directoryCategory.setWorkflowName(null); //绑定的流程名称
		}
		else if(newDirectory instanceof PublicInfoDirectory) {
			PublicInfoDirectory infoDirectory = (PublicInfoDirectory)newDirectory;
			//infoDirectory.setCode(null); //类目代码,当类型为根目录时，填写机构代码，否则是目录代码
			infoDirectory.setDescription(null); //描述
			infoDirectory.setSynchSiteIds(null); //同步更新到网站栏目列表
			infoDirectory.setWorkflowId(0); //绑定的流程ID,添加信息时,查找当前目录绑定的流程，如果没有则查询上级目录
			infoDirectory.setWorkflowName(null); //绑定的流程名称
		}
		return newDirectory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		String hql = "select PublicInfo" +
					 " from PublicInfo PublicInfo" +
					 " left join PublicInfo.subjections PublicInfoSubjection" +
					 " where PublicInfoSubjection.directoryId=" + directory.getId();
		List infos = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections", ","));
		if(infos==null || infos.isEmpty()) {
			return;
		}
		for(Iterator iterator = infos.iterator(); iterator.hasNext(); ) {
			PublicInfo info = (PublicInfo)iterator.next();
			if(info.getSubjections().size()<2) { //信息只属于一个栏目
				publicInfoService.delete(info); //删除信息
			}
			else { //信息只属于多个栏目
				//删除信息隶属记录
				Set subjections = new HashSet(info.getSubjections());
				ListUtils.removeObjectByProperty(subjections, "directoryId", new Long(directory.getId()));
				publicInfoService.updateInfoSubjections(info, false, ListUtils.join(subjections, "directoryId", ",", false));
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#rebuildStaticPageForUpdatedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	protected boolean rebuildStaticPageForUpdatedDirectory(Directory newDirectory, Directory oldDirectory) throws ServiceException {
		if(newDirectory instanceof PublicArticleDirectory) { //文章目录
			PublicArticleDirectory newArticleDirectory = (PublicArticleDirectory)newDirectory;
			PublicArticleDirectory oldArticleDirectory = (PublicArticleDirectory)oldDirectory;
			//检查重定向地址是否有变化
			if(!(newArticleDirectory.getRedirectUrl()==null ? "" : newArticleDirectory.getRedirectUrl()).equals(oldArticleDirectory.getRedirectUrl()==null ? "" : oldArticleDirectory.getRedirectUrl())) {
				return true;
			}
		}
		return super.rebuildStaticPageForUpdatedDirectory(newDirectory, oldDirectory);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDirectory(0)!=null) {
			return false;
		}
		PublicMainDirectory directory = new PublicMainDirectory();
		directory.setId(0);
		directory.setCode("填写机构代码");
		directory.setDirectoryName("信息公开目录");
		directory.setDirectoryType("main");
		directory.setEditorDeleteable('0');
		directory.setEditorReissueable('0');
		directory.setManualCodeEnabled('0');
		save(directory);
		
		//更新隶属关系
		PublicDirectorySubjection subjection = new PublicDirectorySubjection();
		subjection.setId(UUIDLongGenerator.generateId()); //ID
		subjection.setDirectoryId(0); //目录ID
		subjection.setParentDirectoryId(0); //上级ID
		getDatabaseService().saveRecord(subjection);
		
		//授权
		PublicDirectoryPopedom popedom = new PublicDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("manager"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		
		popedom = new PublicDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("editor"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof PublicDirectory) { //目录
			PublicDirectory directory = (PublicDirectory)record;
			if(directory.getSynchSiteIds()!=null && directory.getSynchSiteIds().isEmpty()) {
				directory.setSynchSiteIds(null);
			}
		}
		if(!(record instanceof PublicMainDirectory)) { //非主目录
			super.save(record);
		}
		else { //主目录
			PublicMainDirectory mainDirectory = (PublicMainDirectory)record;
			if(mainDirectory.getSiteName()!=null && "".equals(mainDirectory.getSiteName())) {
				mainDirectory.setSiteName(null);
			}
			mainDirectory = (PublicMainDirectory)super.save(record);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if(!(record instanceof PublicDirectory)) {
			return super.update(record);
		}
		PublicDirectory directory = (PublicDirectory)record;
		if(directory.getSynchSiteIds()!=null && directory.getSynchSiteIds().isEmpty()) {
			directory.setSynchSiteIds(null);
		}
		//获取目录原来需要同步的站点ID列表
		String oldSynchSiteIds = getDirectorySynchSiteIds(directory.getId(), true);
		if(!(record instanceof PublicMainDirectory)) { //不是主目录
			directory = (PublicDirectory)super.update(record);
			publicInfoService.resynchPublicInfos(directory.getId(), oldSynchSiteIds, getDirectorySynchSiteIds(directory.getId(), true)); //重新同步信息
		}
		else {
			PublicMainDirectory mainDirectory = (PublicMainDirectory)record;
			if(mainDirectory.getSiteName()!=null && "".equals(mainDirectory.getSiteName())) {
				mainDirectory.setSiteName(null);
			}
			mainDirectory = (PublicMainDirectory)super.update(record);
			publicInfoService.resynchPublicInfos(directory.getId(), oldSynchSiteIds, getDirectorySynchSiteIds(directory.getId(), true)); //重新同步信息
		}
		return record;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#updateGuide(long, java.lang.String)
	 */
	public void updateGuide(long directoryId, String guide) throws ServiceException {
		PublicGuide publicGuide = (PublicGuide)getDatabaseService().findRecordByHql("from PublicGuide PublicGuide where PublicGuide.directoryId=" + directoryId);
		if(publicGuide!=null) {
			publicGuide.setGuide(guide);
			getDatabaseService().updateRecord(publicGuide);
			getPageService().rebuildStaticPageForModifiedObject(publicGuide, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //新建记录
		}
		else {
			publicGuide = new PublicGuide();
			publicGuide.setId(UUIDLongGenerator.generateId());
			publicGuide.setDirectoryId(directoryId);
			publicGuide.setGuide(guide);
			getDatabaseService().saveRecord(publicGuide);
			getPageService().rebuildStaticPageForModifiedObject(publicGuide, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //新建记录
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#getDirectorySynchSiteIds(long, boolean)
	 */
	public String getDirectorySynchSiteIds(long directoryId, boolean inherit) throws ServiceException {
		if(directoryId==0 || !inherit) { //根、或者不继承上级目录设置
			//获取当前目录的同步设置
			String hql = "select PublicDirectory.synchSiteIds" +
						 " from PublicDirectory PublicDirectory" +
						 " where PublicDirectory.id=" + directoryId;
			return (String)getDatabaseService().findRecordByHql(hql);
		}
		else {
			String hql = "select PublicDirectory.synchSiteIds" +
				  		 " from PublicDirectory PublicDirectory, PublicDirectorySubjection PublicDirectorySubjection" +
				  		 " where PublicDirectorySubjection.directoryId=" + directoryId +
				  		 " and PublicDirectory.id=PublicDirectorySubjection.parentDirectoryId" +
				  		 " and not (PublicDirectory.synchSiteIds is null)" +
				  		 " order by PublicDirectorySubjection.id";
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
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#getApprovalWorkflowId(long)
	 */
	public long getApprovalWorkflowId(long directoryId) throws ServiceException {
		//查找上级站点关联的流程配置
		String hql = "select PublicDirectory.workflowId" +
			  		 " from PublicDirectory PublicDirectory left join PublicDirectory.childSubjections PublicDirectorySubjection" +
			  		 " where PublicDirectorySubjection.directoryId=" + directoryId +
			  		 " and PublicDirectory.workflowId>0" +
			  		 " order by PublicDirectorySubjection.id";
		Number workflowId = (Number)getDatabaseService().findRecordByHql(hql);
		return workflowId==null ? 0 : workflowId.longValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#getMainDirectory(long)
	 */
	public PublicMainDirectory getMainDirectory(long directoryId) throws ServiceException {
		//从上级目录中查找主目录
		String hql = "select PublicMainDirectory" +
					 " from PublicMainDirectory PublicMainDirectory, PublicDirectorySubjection PublicDirectorySubjection" +
					 " where PublicDirectorySubjection.directoryId=" + directoryId + 
					 " and PublicMainDirectory.id=PublicDirectorySubjection.parentDirectoryId" +
					 " order by PublicDirectorySubjection.id";
		return (PublicMainDirectory)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#getMainDirectoryBySite(long)
	 */
	public PublicMainDirectory getMainDirectoryBySite(long siteId) throws ServiceException {
		String hql = "select PublicMainDirectory" +
					 " from PublicMainDirectory PublicMainDirectory, WebDirectorySubjection WebDirectorySubjection" +
					 " where PublicMainDirectory.siteId=WebDirectorySubjection.parentDirectoryId" +
					 " and (not PublicMainDirectory.siteName is null)" +
					 " and WebDirectorySubjection.directoryId=" + siteId +
					 " order by WebDirectorySubjection.id";
		PublicMainDirectory directory = (PublicMainDirectory)getDatabaseService().findRecordByHql(hql);
		return (directory==null ? (PublicMainDirectory)getDirectory(0) : directory);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#getPublicDirectoryByName(long, java.lang.String)
	 */
	public PublicDirectory getPublicDirectoryByName(long mainDirectoryId, String directoryName) throws ServiceException {
		String hql = "select PublicDirectory" +
					 " from PublicDirectory PublicDirectory, PublicDirectorySubjection PublicDirectorySubjection" +
					 " where PublicDirectory.id=PublicDirectorySubjection.directoryId" +
					 " and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(getChildDirectoryIds("" + mainDirectoryId, "category,info,article")) + ")" +
					 " and PublicDirectory.directoryName='" + JdbcUtils.resetQuot(directoryName) + "'";
		return (PublicDirectory)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#getPublicGuide(long)
	 */
	public PublicGuide getPublicGuide(long directoryId) throws ServiceException {
		String hql = "select PublicGuide" +
			  		 " from PublicGuide PublicGuide, PublicDirectorySubjection PublicDirectorySubjection" +
			  		 " where PublicGuide.directoryId=PublicDirectorySubjection.parentDirectoryId" +
			  		 " and PublicDirectorySubjection.directoryId=" + directoryId +
			  		 " order by PublicDirectorySubjection.id";
		return (PublicGuide)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#isEditorDeleteable(long)
	 */
	public boolean isEditorDeleteable(long directoryId) throws ServiceException {
		return checkEditorPrivilege(directoryId, "editorDeleteable");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#isEditorReissueable(long)
	 */
	public boolean isEditorReissueable(long directoryId) throws ServiceException {
		return checkEditorPrivilege(directoryId, "editorReissueable");
	}
	
	/**
	 * 检查编辑的权限
	 * @param webDirectoryId
	 * @param privilegeType
	 * @return
	 * @throws ServiceException
	 */
	private boolean checkEditorPrivilege(long directoryId, String privilegeType) throws ServiceException {
		String hql = "select PublicDirectory." + privilegeType +
					 " from PublicDirectory PublicDirectory, PublicDirectorySubjection PublicDirectorySubjection" +
					 " where PublicDirectorySubjection.directoryId=" + directoryId +
					 " and PublicDirectory.id=PublicDirectorySubjection.parentDirectoryId" +
					 " and PublicDirectory." + privilegeType + ">'0'" +
					 " order by PublicDirectorySubjection.id";
		Character enable = (Character)getDatabaseService().findRecordByHql(hql);
		return (enable!=null && enable.charValue()=='1');
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long directoryId = RequestUtils.getParameterLongValue(notifyRequest, "directoryId"); //目录ID
		PublicDirectory directory = (PublicDirectory)getDirectory(directoryId);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程删除操作
			directory.setWorkflowId(0); //流程ID 
			directory.setWorkflowName(null); //流程名称
		}
		else { //新建或更新流程
			directory.setWorkflowId(Long.parseLong(workflowId)); //流程ID 
			directory.setWorkflowName(workflowPackage.getName()); //流程名称
		}
		update(directory);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#createRecentUsenTreeNode(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, int)
	 */
	public TreeNode createRecentUsenTreeNode(String extendPropertyNames, SessionInfo sessionInfo, int maxDirectory) throws ServiceException {
		String hqlPrefix = "select PublicDirectory" +
						   " from PublicDirectory PublicDirectory, PublicInfo PublicInfo, PublicInfoSubjection PublicInfoSubjection" +
						   " where PublicDirectory.id=PublicInfoSubjection.directoryId" +
						   " and PublicInfoSubjection.infoId=PublicInfo.id" +
						   " and PublicInfo.creatorId=" + sessionInfo.getUserId() +
						   " and PublicInfoSubjection.id=(" +
						   "  select min(PublicInfoSubjection.id)" +
						   "   from PublicInfoSubjection PublicInfoSubjection" +
						   "   where PublicInfoSubjection.infoId=PublicInfo.id" +
						   " )";
		List treeNodes = new ArrayList();
		while(treeNodes.size()<maxDirectory) {
			String hql = hqlPrefix +
						(treeNodes.isEmpty() ? "" : " and not PublicDirectory.id in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(treeNodes, "nodeId", ",", false)) + ")") +
						" order by PublicInfo.created DESC";
			List directories = getDatabaseService().findRecordsByHql(hql, 0, maxDirectory);
			if(directories==null || directories.isEmpty()) {
				break;
			}
			for(Iterator iterator = directories.iterator(); iterator.hasNext();) {
				PublicDirectory publicDirectory = (PublicDirectory)iterator.next();
				if(ListUtils.findObjectByProperty(treeNodes, "nodeId", "" + publicDirectory.getId())!=null) {
					continue;
				}
				TreeNode node = convertTreeNode(publicDirectory, false, extendPropertyNames);
				node.setNodeText(getDirectoryFullName(publicDirectory.getId(), "/", "main"));
				treeNodes.add(node);
				if(treeNodes.size()>=maxDirectory) {
					break;
				}
			}
		}
		if(treeNodes.isEmpty()) {
			return null;
		}
		TreeNode recentUseNode = new TreeNode("recentUse", "最近使用", "recentUse", Environment.getContextPath() + "/cms/infopublic/icons/category.gif", true);
		recentUseNode.setExpandTree(true); //自动展开
		recentUseNode.setChildNodes(treeNodes);
		return recentUseNode;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#listDirectoryEditors(long, boolean, boolean, int)
	 */
	public List listDirectoryEditors(long directoryId, boolean myEditorsOnly, boolean asPerson, int max) throws ServiceException {
		return listDirectoryVisitors(directoryId, "editor", myEditorsOnly, asPerson, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicDirectoryService#listDirectoryManagers(long, boolean, boolean, int)
	 */
	public List listDirectoryManagers(long directoryId, boolean myManagersOnly, boolean asPerson, int max) throws ServiceException {
		return listDirectoryVisitors(directoryId, "manager", myManagersOnly, asPerson, max);
	}
	
	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	/**
	 * @return the publicInfoService
	 */
	public PublicInfoService getPublicInfoService() {
		return publicInfoService;
	}

	/**
	 * @param publicInfoService the publicInfoService to set
	 */
	public void setPublicInfoService(PublicInfoService publicInfoService) {
		this.publicInfoService = publicInfoService;
	}
}
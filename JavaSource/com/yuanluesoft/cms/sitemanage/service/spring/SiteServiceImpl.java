/*
 * Created on 2007-7-1
 *
 */
package com.yuanluesoft.cms.sitemanage.service.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.LRUMap;

import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.sitemanage.model.RebuildPageByResourceSiteIds;
import com.yuanluesoft.cms.sitemanage.pojo.Headline;
import com.yuanluesoft.cms.sitemanage.pojo.SiteTemplateTheme;
import com.yuanluesoft.cms.sitemanage.pojo.WebColumn;
import com.yuanluesoft.cms.sitemanage.pojo.WebColumnRelationLink;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectoryPopedom;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectorySubjection;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectorySynch;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.pojo.WebViewReference;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateThemeUsage;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.exception.FileTransferException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;
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
public class SiteServiceImpl extends DirectoryServiceImpl implements SiteService {
	private SiteResourceService siteResourceService;
	private String webApplicationLocalUrl; //本地访问时的网站路径
	private String staticPageUrl; //静态页面URL
	private AttachmentService attachmentService; //附件服务
	private FileDownloadService fileDownloadService; //文件下载服务
	private StaticPageBuilder staticPageBuilder; //静态页面生成器
	private Cache recordCache; //记录缓存,需要缓存时配置

	/**
	 * 启动
	 *
	 */
	public void startup() {
		//更新主机名页面映射表
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				updateHostPageMapping();
				timer.cancel();
			}
		}, 20000); //等待20s
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#updateHostPageMapping()
	 */
	public void updateHostPageMapping() {
		if(Logger.isDebugEnabled()) {
			Logger.debug("SiteService: update host name and page mapping.");
		}
		int index = webApplicationLocalUrl.indexOf('/', webApplicationLocalUrl.indexOf("://") + 3);
		String contextPath = (index==-1 ? "" : webApplicationLocalUrl.substring(index));
		String localURL = index==-1 ? webApplicationLocalUrl : webApplicationLocalUrl.substring(0, index);
		
		//获取主机信息
		HashMap hostInfos = null;
		try {
			hostInfos = loadHostInfos();
		} 
		catch(ServiceException e) {
			
		}
		//构造主机名页面映射表
		HashMap hostPageMapping = new HashMap();
		for(Iterator iterator = hostInfos==null ? null : hostInfos.keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String hostName = (String)iterator.next();
			String[] infos = (String[])hostInfos.get(hostName); //{站点ID, 静态页面URL, 静态页面路径}
			hostPageMapping.put(hostName, (infos[2]==null ? "" : infos[2]) + "\0" + webApplicationLocalUrl + "/cms/sitemanage/index.shtml" + ("0".equals(infos[0]) ? "" : "?siteId=" + infos[0]));
		}
		//添加根站点映射,当主机名不在映射表中时,显示根站点
		try {
			String staticPagePath = staticPageBuilder.getStaticPagePath(contextPath + "/cms/sitemanage/index.shtml");
			hostPageMapping.put("ROOT", (staticPagePath==null ? "" : staticPagePath) + "\0" + webApplicationLocalUrl + "/cms/sitemanage/index.shtml");
		}
		catch(Exception e) {
			
		}
		//发送主机名页面映射表
		try {
			int responseCode = HttpUtils.doPostObject(localURL + "/index.jsp?updateHostPageMapping=true", null, hostPageMapping, 20000);
			if(responseCode!=200) {
				throw new Exception("response code is " + responseCode);
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return WebDirectory.class;
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
				new DirectoryType("site", "site", "站点", WebSite.class, "/cms/sitemanage/icons/site.gif", null, true),
				new DirectoryType("column", "site,column", "栏目", WebColumn.class, "/cms/sitemanage/icons/column.gif", null, false),
				new DirectoryType("viewReference", "site,column", "引用", WebViewReference.class, "/cms/sitemanage/icons/reference.gif", null, false)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedomTypes()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		return new DirectoryPopedomType[] {
				new DirectoryPopedomType("manager", "管理员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, true, true),
				new DirectoryPopedomType("editor", "网站编辑", "site,column", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/cms/sitemanage/icons/root.gif";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/cms/siteresource/admin/resourceView.shtml?siteId=" + directoryId;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		if(newDirectory instanceof WebSite) {
			WebSite site = (WebSite)newDirectory;
			site.setHostName(null); //主机名,仅对站点有效
			site.setWorkflowId(0); //绑定的流程ID,添加信息时,查找当前目录绑定的流程，如果没有则查询上级目录
			site.setWorkflowName(null); //绑定的流程名称
		}
		else if(newDirectory instanceof WebColumn) {
			WebColumn column = (WebColumn)newDirectory;
			column.setWorkflowId(0); //绑定的流程ID,添加信息时,查找当前目录绑定的流程，如果没有则查询上级目录
			column.setWorkflowName(null); //绑定的流程名称
		}
		return newDirectory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		String hql = "select SiteResource" +
					 " from SiteResource SiteResource" +
					 " left join SiteResource.subjections SiteResourceSubjection" +
					 " where SiteResourceSubjection.siteId=" + directory.getId();
		List resources = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections", ","));
		if(resources!=null && !resources.isEmpty()) {
			for(Iterator iterator = resources.iterator(); iterator.hasNext(); ) {
				SiteResource resource = (SiteResource)iterator.next();
				if(resource.getSubjections().size()<2) { //资源只属于一个栏目
					siteResourceService.delete(resource); //删除资源
				}
				else { //资源只属于多个栏目
					Set subjections = new HashSet(resource.getSubjections());
					ListUtils.removeObjectByProperty(subjections, "siteId", new Long(directory.getId()));
					siteResourceService.updateSiteResourceSubjections(resource, false, ListUtils.join(subjections, "siteId", ",", false)); //更新隶属关系
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDirectory(0)!=null) {
			return false;
		}
		//创建根站点
		WebSite site = new WebSite();
		site.setDirectoryName(systemName);
		site.setEditorDeleteable('0');
		site.setEditorReissueable('0');
		site.setUseSiteTemplate('0');
		save(site);
		
		//更新隶属关系
		WebDirectorySubjection subjection = new WebDirectorySubjection();
		subjection.setId(UUIDLongGenerator.generateId()); //ID
		subjection.setDirectoryId(0); //目录ID
		subjection.setParentDirectoryId(0); //上级ID
		getDatabaseService().saveRecord(subjection);
		
		//授权
		WebDirectoryPopedom popedom = new WebDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("manager"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		
		popedom = new WebDirectoryPopedom();
		popedom.setId(UUIDLongGenerator.generateId()); //ID
		popedom.setDirectoryId(0); //目录ID
		popedom.setUserId(managerId); //用户ID,用户、部门、角色和网上注册用户
		popedom.setUserName(managerName); //用户名
		popedom.setPopedomName("editor"); //权限
		popedom.setIsInherit('0'); //是否从上级目录继承
		getDatabaseService().saveRecord(popedom);
		
		//创建模板主题
		SiteTemplateTheme siteTemplateTheme = new SiteTemplateTheme();
		siteTemplateTheme.setId(UUIDLongGenerator.generateId()); //ID
		siteTemplateTheme.setName("默认"); //名称,如：节假日、手机
		siteTemplateTheme.setType(TemplateThemeService.THEME_TYPE_COMPUTER); //类型,电脑|0/智能手机或平板电脑|1/WAP|2
		siteTemplateTheme.setPageWidth(0); //页面宽度,类型为“智能手机或平板电脑”时有效
		siteTemplateTheme.setFlashSupport(1); //是否含Flash,iphine、ipad不支持flash
		siteTemplateTheme.setLastModified(DateTimeUtils.now()); //最后修改时间
		siteTemplateTheme.setLastModifierId(managerId); //最后修改人ID
		siteTemplateTheme.setLastModifier(managerName); //最后修改人姓名
		siteTemplateTheme.setSiteId(0); //站点ID
		getDatabaseService().saveRecord(siteTemplateTheme);
		//创建使用记录
		TemplateThemeUsage themeUsage = new TemplateThemeUsage();
		themeUsage.setId(UUIDLongGenerator.generateId());
		themeUsage.setSiteId(0); //站点/用户ID,如果站点没有配置自己主题,则使用父站点的配置
		themeUsage.setThemeId(siteTemplateTheme.getId()); //主题ID,iphine、ipad不支持flash
		themeUsage.setIsDefault(1); //是否默认主题,默认主题修改后重新生成本站的全部静态页面
		themeUsage.setTemporaryOpening(0); //是否临时启用
		getDatabaseService().saveRecord(themeUsage);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#rebuildStaticPageForUpdatedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	protected boolean rebuildStaticPageForUpdatedDirectory(Directory newDirectory, Directory oldDirectory) throws ServiceException {
		WebDirectory newWebDirectory = (WebDirectory)newDirectory;
		WebDirectory oldWebDirectory = (WebDirectory)oldDirectory;
		if(newWebDirectory.getHalt()!=oldWebDirectory.getHalt()) {
			return true;
		}
		if(!(newWebDirectory.getRedirectUrl()==null ? "" : newWebDirectory.getRedirectUrl()).equals(oldWebDirectory.getRedirectUrl()==null ? "" : oldWebDirectory.getRedirectUrl())) {
			return true;
		}
		if(!(newWebDirectory.getDescription()==null ? "" : newWebDirectory.getDescription()).equals(oldWebDirectory.getDescription()==null ? "" : oldWebDirectory.getDescription())) {
			return true;
		}
		if(!(newWebDirectory.getRemark()==null ? "" : newWebDirectory.getRemark()).equals(oldWebDirectory.getRemark()==null ? "" : oldWebDirectory.getRemark())) {
			return true;
		}
		return super.rebuildStaticPageForUpdatedDirectory(newDirectory, oldDirectory);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof Headline) { //头版头条
			record = super.save(record);
			//重建静态页面
			getStaticPageBuilder().rebuildPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		else if(record instanceof WebViewReference) { //视图引用
			WebViewReference reference = (WebViewReference)record;
			if(reference.getReferenceParameter()!=null && reference.getReferenceParameter().isEmpty()) {
				reference.setReferenceParameter(null);
			}
			record = super.save(record);
		}
		else if(record instanceof WebSite) { //站点
			WebSite site = (WebSite)record;
			if(site.getHostName()!=null) {
				site.setHostName(site.getHostName().trim());
				if("".equals(site.getHostName())) {
					site.setHostName(null);
				}
			}
			record = super.save(record);
			updateHostPageMapping(); //更新主机名和页面的对应关系
		}
		else if(record instanceof WebColumnRelationLink) { //相关链接
			record = super.save(record);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		else {
			record = super.save(record);
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof WebDirectory) {
			try {
				recordCache.clear();
			}
			catch(Exception e) {
				
			}
		}
		if(record instanceof Headline) { //头版头条
			super.update(record);
			//重建静态页面
			getStaticPageBuilder().rebuildPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		else if(record instanceof WebViewReference) { //视图引用
			WebViewReference reference = (WebViewReference)record;
			if(reference.getReferenceParameter()!=null && reference.getReferenceParameter().isEmpty()) {
				reference.setReferenceParameter(null);
			}
			record = super.update(record);
		}
		else if(record instanceof WebSite) {
			WebSite site = (WebSite)record;
			if(site.getHostName()!=null) {
				site.setHostName(site.getHostName().trim());
				if("".equals(site.getHostName())) {
					site.setHostName(null);
				}
			}
			super.update(record);
			updateHostPageMapping(); //生成index.jsp
		}
		else if(record instanceof WebColumn) { //栏目
			super.update(record);
			updateRelatedLinks((WebColumn)record, false);
		}
		else if(record instanceof WebColumnRelationLink) { //相关链接
			super.update(record);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		else {
			super.update(record);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof WebDirectory) {
			try {
				recordCache.clear();
			}
			catch(Exception e) {
				
			}
		}
		if(record instanceof WebColumn) { //栏目
			updateRelatedLinks((WebColumn)record, true);
		}
		else if(record instanceof WebColumnRelationLink) { //相关链接
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectory(long)
	 */
	public Directory getDirectory(long directoryId) throws ServiceException {
		try {
			Directory directory = (Directory)recordCache.get("webdirectory:" + directoryId);
			if(directory!=null) {
				return directory;
			}
			directory = (Directory)super.getDirectory(directoryId);
			if(directory!=null) {
				recordCache.put("webdirectory:" + directoryId, directory);
			}
			return directory;
		}
		catch(CacheException e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getParentSite(long)
	 */
	public WebSite getParentSite(long columnId) throws ServiceException {
		try {
			WebSite parentSite = (WebSite)recordCache.get("parentsite:" + columnId);
			if(parentSite!=null) {
				return parentSite;
			}
			String hql = "select WebDirectory" +
				  		 " from WebDirectory WebDirectory, WebDirectorySubjection WebDirectorySubjection" +
				  		 " where WebDirectorySubjection.directoryId=" + columnId + 
				  		 " and WebDirectory.id=WebDirectorySubjection.parentDirectoryId" +
				  		 " and WebDirectory.directoryType='site'" +
				  		 " order by WebDirectorySubjection.id";
			parentSite = (WebSite)getDatabaseService().findRecordByHql(hql);
			if(parentSite!=null) {
				recordCache.put("parentsite:" + columnId, parentSite);
			}
			return parentSite;
		}
		catch(CacheException e) {
			throw new ServiceException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getApprovalWorkflowId(long)
	 */
	public long getApprovalWorkflowId(long siteId) throws ServiceException {
		String hql = "select WebDirectory.workflowId" +
			  		 " from WebDirectory WebDirectory left join WebDirectory.childSubjections WebDirectorySubjection" +
			  		 " where WebDirectorySubjection.directoryId=" + siteId +
			  		 " and WebDirectory.workflowId>0" +
			  		 " order by WebDirectorySubjection.id";
		Number workflowId = (Number)getDatabaseService().findRecordByHql(hql);
		return (workflowId==null ? 0 : workflowId.longValue());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#saveSynchDirectories(com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, java.lang.String, java.lang.String)
	 */
	public void saveSynchDirectories(WebDirectory directory, boolean isNewDirectory, String synchToDirectoryIds, String synchFromDirectoryIds) throws ServiceException {
		List addedToDirectoryIds = retrieveAddedDirectoryIds(synchToDirectoryIds, directory.getSynchToDirectories(), false); //新增的目标目录
		List deletedToDirectoryIds = retrieveDeletedDirectoryIds(synchToDirectoryIds, directory.getSynchToDirectories(), false); //已经被删除的目标目录
		List addedFromDirectoryIds = retrieveAddedDirectoryIds(synchFromDirectoryIds, directory.getSynchFromDirectories(), true); //新增的源目录
		List deletedFromDirectoryIds = retrieveDeletedDirectoryIds(synchFromDirectoryIds, directory.getSynchFromDirectories(), true); //已经被删除的源目录
		//检查配置是否变动过
		if(addedToDirectoryIds==null && deletedToDirectoryIds==null && addedFromDirectoryIds==null && deletedFromDirectoryIds==null) {
			return; //没有改变
		}
		String resourceSiteIds = null;
		if(addedToDirectoryIds!=null) {
			resourceSiteIds = ListUtils.join(addedToDirectoryIds, ",", false);
		}
		if(deletedToDirectoryIds!=null) {
			resourceSiteIds = (resourceSiteIds==null ? "" : resourceSiteIds + ",") + ListUtils.join(deletedToDirectoryIds, ",", false);
		}
		if(addedFromDirectoryIds!=null || deletedFromDirectoryIds!=null) {
			resourceSiteIds = (resourceSiteIds==null ? "" : resourceSiteIds + ",") + directory.getId();
		}
								
		//获取目录原来需要同步的栏目ID列表
		List oldSynchColumnIds = listSynchColumnIds(directory.getId() + "");
		//删除原来的同步设置
		if(directory.getSynchToDirectories()!=null) {
			for(Iterator iterator = directory.getSynchToDirectories().iterator(); iterator.hasNext();) {
				WebDirectorySynch directorySynch = (WebDirectorySynch)iterator.next();
				getDatabaseService().deleteRecord(directorySynch);
			}
		}
		if(directory.getSynchFromDirectories()!=null) {
			for(Iterator iterator = directory.getSynchFromDirectories().iterator(); iterator.hasNext();) {
				WebDirectorySynch directorySynch = (WebDirectorySynch)iterator.next();
				getDatabaseService().deleteRecord(directorySynch);
			}
		}
		//更新同步设置
		if(synchToDirectoryIds!=null && !synchToDirectoryIds.equals("")) {
			String[] ids = synchToDirectoryIds.split(",");
			for(int i=0; i<ids.length; i++) {
				WebDirectorySynch directorySynch = new WebDirectorySynch();
				directorySynch.setId(UUIDLongGenerator.generateId()); //ID
				directorySynch.setDirectoryId(directory.getId()); //目录ID
				directorySynch.setSynchDirectoryId(Long.parseLong(ids[i])); //同步的目录ID
				getDatabaseService().saveRecord(directorySynch);
			}
		}
		if(synchFromDirectoryIds!=null && !synchFromDirectoryIds.equals("")) {
			String[] ids = synchFromDirectoryIds.split(",");
			for(int i=0; i<ids.length; i++) {
				WebDirectorySynch directorySynch = new WebDirectorySynch();
				directorySynch.setId(UUIDLongGenerator.generateId()); //ID
				directorySynch.setDirectoryId(Long.parseLong(ids[i])); //目录ID
				directorySynch.setSynchDirectoryId(directory.getId()); //同步的目录ID
				getDatabaseService().saveRecord(directorySynch);
			}
		}
		//获取目录现在需要同步的栏目ID列表
		List newSynchColumnIds = listSynchColumnIds(directory.getId() + "");
		
		//对所有隶属于deletedFromDirectoryIds的资源,删除对oldSynchColumnIds的同步
		updateResourceSubjection(ListUtils.join(deletedFromDirectoryIds, ",", false), null, oldSynchColumnIds);
		
		//比较oldSynchColumnIds和newSynchColumnIds,得到新增的栏目ID列表个减少的栏目ID列表,为所有隶属于directory.getId()的资源,更新栏目同步
		addedToDirectoryIds = new ArrayList();
		deletedToDirectoryIds = new ArrayList();
		for(Iterator iterator = newSynchColumnIds.iterator(); iterator.hasNext();) {
			Long directoryId = (Long)iterator.next();
			if(oldSynchColumnIds.indexOf(directoryId)==-1) {
				addedToDirectoryIds.add(directoryId);
			}
		}
		for(Iterator iterator = oldSynchColumnIds.iterator(); iterator.hasNext();) {
			Long directoryId = (Long)iterator.next();
			if(newSynchColumnIds.indexOf(directoryId)==-1) {
				deletedToDirectoryIds.add(directoryId);
			}
		}
		updateResourceSubjection(directory.getId() + "", addedToDirectoryIds.isEmpty() ? null : addedToDirectoryIds, deletedToDirectoryIds.isEmpty() ? null : deletedToDirectoryIds);
		
		//为addedFromDirectoryIds栏目中的资源增加和newSynchColumnIds的同步
		updateResourceSubjection(ListUtils.join(addedFromDirectoryIds, ",", false), newSynchColumnIds, null);
		
		//更新静态页面
		RebuildPageByResourceSiteIds rebuildPageByResourceSiteIds = new RebuildPageByResourceSiteIds();
		rebuildPageByResourceSiteIds.setSiteIds(resourceSiteIds);
		getPageService().rebuildStaticPageForModifiedObject(rebuildPageByResourceSiteIds, null);
	}
	
	/**
	 * 获取新增的同步目录ID列表
	 * @param newDirectoryIds
	 * @param directorySynchs
	 * @param isFrom
	 * @return
	 */
	private List retrieveAddedDirectoryIds(String newDirectoryIds, Set directorySynchs, boolean isFrom) {
		if(newDirectoryIds==null || newDirectoryIds.equals("")) {
			return null;
		}
		String[] ids =  newDirectoryIds.split(",");
		List added = new ArrayList();
		for(int i=0; i<ids.length; i++) {
			Long directoryId = new Long(ids[i]);
			if(ListUtils.findObjectByProperty(directorySynchs, (isFrom ? "directoryId" : "synchDirectoryId"), directoryId)==null) {
				added.add(directoryId);
			}
		}
		return added.isEmpty() ? null : added;
	}
	
	/**
	 * 获取被删除的同步目录ID列表
	 * @param newDirectoryIds
	 * @param directorySynchs
	 * @param isFrom
	 * @return
	 */
	private List retrieveDeletedDirectoryIds(String newDirectoryIds, Set directorySynchs, boolean isFrom) {
		if(directorySynchs==null || directorySynchs.isEmpty()) {
			return null; //原来就没有
		}
		newDirectoryIds = "," + newDirectoryIds + ",";
		List deleted = new ArrayList();
		for(Iterator iterator = directorySynchs.iterator(); iterator.hasNext();) {
			WebDirectorySynch directorySynch = (WebDirectorySynch)iterator.next();
			long directoryId = (isFrom ? directorySynch.getDirectoryId() : directorySynch.getSynchDirectoryId());
			if(newDirectoryIds.indexOf("," + directoryId + ",")==-1) {
				deleted.add(new Long(directoryId));
			}
		}
		return deleted.isEmpty() ? null : deleted;
	}
	
	/**
	 * 更新资源的隶属关系
	 * @param directoryIds
	 * @param addedSynchDirectoryIds
	 * @param deletedSynchDirectoryIds
	 * @throws ServiceException
	 */
	private void updateResourceSubjection(String directoryIds, List addedSynchDirectoryIds, List deletedSynchDirectoryIds) throws ServiceException {
		if(directoryIds==null || directoryIds.equals("")) {
			return;
		}
		if(addedSynchDirectoryIds==null && deletedSynchDirectoryIds==null) {
			return;
		}
		Map synchDeleteMap = new LRUMap(200);
		for(int i=0; ; i+=200) { //每次处理200条记录
			List resources = siteResourceService.listSiteResources(directoryIds, "all", true, false, i, 200);
			if(resources==null || resources.isEmpty()) {
				break;
			}
			for(Iterator iterator = resources.iterator(); iterator.hasNext();) {
				SiteResource resource = (SiteResource)iterator.next();
				//获取隶属目录
				String hql = "from SiteResourceSubjection SiteResourceSubjection" +
							 " where SiteResourceSubjection.resourceId=" + resource.getId() +
							 " order by SiteResourceSubjection.id";
				List subjections = getDatabaseService().findRecordsByHql(hql);
				SiteResourceSubjection firstSubjection = (SiteResourceSubjection)subjections.get(0);
				//添加隶属目录
				if(addedSynchDirectoryIds!=null) {
					long id = UUIDLongGenerator.generateId();
					//检查id是否小于第一个隶属目录的ID
					if(id<firstSubjection.getId()) {
						//重建第一个隶属关系
						long siteId = firstSubjection.getSiteId();
						getDatabaseService().deleteRecord(firstSubjection);
						firstSubjection = new SiteResourceSubjection();
						firstSubjection.setId(id);
						firstSubjection.setResourceId(resource.getId());
						firstSubjection.setSiteId(siteId);
						getDatabaseService().saveRecord(firstSubjection);
					}
					for(Iterator iteratorAdded = addedSynchDirectoryIds.iterator(); iteratorAdded.hasNext();) {
						Long addedDirectoryId = (Long)iteratorAdded.next();
						if(ListUtils.findObjectByProperty(subjections, "siteId", addedDirectoryId)==null) { //不在原来的隶属目录中
							SiteResourceSubjection subjection = new SiteResourceSubjection();
							subjection.setId(UUIDLongGenerator.generateId());
							subjection.setResourceId(resource.getId());
							subjection.setSiteId(addedDirectoryId.longValue());
							getDatabaseService().saveRecord(subjection);
						}
					}
				}
				//删除隶属目录
				if(deletedSynchDirectoryIds!=null) {
					//从需要删除的目录中剔除资源所在目录需要同步的目录
					List synchDeleted = (List)synchDeleteMap.get(new Long(firstSubjection.getSiteId())); //检查缓存
					if(synchDeleted==null) {
						List synchColumnIds = listSynchColumnIds(firstSubjection.getSiteId() + ""); //获取资源所在栏目需要同步的栏目ID列表
						synchDeleted = new ArrayList();
						for(Iterator iteratorDeleted = deletedSynchDirectoryIds.iterator(); iteratorDeleted.hasNext();) {
							Long directoryId = (Long)iteratorDeleted.next();
							if(synchColumnIds.indexOf(directoryId)==-1) { //不在资源栏目的同步目录中
								synchDeleted.add(directoryId);
							}
						}
						//加入到缓存
						synchDeleteMap.put(new Long(firstSubjection.getSiteId()), synchDeleted);
					}
					for(Iterator iteratorDeleted = synchDeleted.iterator(); iteratorDeleted.hasNext();) {
						Long directoryId = (Long)iteratorDeleted.next();
						SiteResourceSubjection subjection = (SiteResourceSubjection)ListUtils.findObjectByProperty(subjections, "siteId", directoryId);
						if(subjection!=null) {
							getDatabaseService().deleteRecord(subjection);
						}
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#listSynchDirectoryIds(java.lang.String)
	 */
	public List listSynchColumnIds(String columnIds) throws ServiceException {
		List synchColumnIds = new ArrayList(); //隶属的ID列表
		String[] ids = columnIds.split(",");
		for(int i=0; i<ids.length; i++) { //添加自己
			synchColumnIds.add(new Long(ids[i]));
		}
		//获取当前栏目及其上级栏目的同步的栏目ID列表
		String hql = "select distinct WebDirectorySynch.synchDirectoryId" +
					 " from WebDirectorySynch WebDirectorySynch, WebDirectorySubjection WebDirectorySubjection" +
					 " where WebDirectorySynch.directoryId=WebDirectorySubjection.parentDirectoryId" + //当前栏目自己的设置
					 " and WebDirectorySubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(columnIds) + ")"; //当前栏目的上级
		//获取同步栏目自己的同步设置
		retrieveSynchColumnIds(synchColumnIds, getDatabaseService().findRecordsByHql(hql));
		return synchColumnIds;
	}
	
	/**
	 * 递归函数：追加新的需要同步的栏目ID
	 * @param synchDirectoryIds 已找到的同步栏目ID列表
	 * @param newSynchDirectoryIds 最近找到的同步栏目ID列表
	 * @throws ServiceException
	 */
	private void retrieveSynchColumnIds(final List synchColumnIds, List newSynchColumnIds) throws ServiceException {
		if(newSynchColumnIds==null || newSynchColumnIds.isEmpty()) { //没有新增的目录
			return;
		}
		//把新增的加入到现有的目录ID列表中
		for(Iterator iterator = newSynchColumnIds.iterator(); iterator.hasNext();) {
			Object newSynchColumnId = iterator.next();
			//检查是否已经在同步的栏目ID中
			if(synchColumnIds.indexOf(newSynchColumnId)!=-1) { //已经添加过
				iterator.remove();
			}
			else { //没有添加过
				synchColumnIds.add(newSynchColumnId);
			}
		}
		if(newSynchColumnIds.isEmpty()) { //没有新增的目录
			return;
		}
		//获取上一级的同步设置
		String hql = "select distinct WebDirectorySynch.synchDirectoryId" +
					 " from WebDirectorySynch WebDirectorySynch" +
					 " where WebDirectorySynch.directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(newSynchColumnIds, ",", false)) + ")"; //当前栏目的上级
		//递归处理上一级的同步设置
		retrieveSynchColumnIds(synchColumnIds, getDatabaseService().findRecordsByHql(hql));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.formula.service.FormulaSupport#executeFormula(java.lang.String, java.lang.String[], java.lang.String, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Object executeFormula(String formulaName, String[] parameters, String applicationName, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws ServiceException {
		if(formulaName.equals("isNotSiteManager") || formulaName.equals("isNotSiteEditor")) { //是否某个站点的管理员、编辑
			String hql = "select WebDirectoryPopedom.id" +
						 " from WebDirectoryPopedom WebDirectoryPopedom, WebDirectory WebDirectory" +
						 " where WebDirectoryPopedom.directoryId=WebDirectory.id" +
						 " and WebDirectory.directoryType='site'" +
						 " and WebDirectoryPopedom.popedomName='" + (formulaName.equals("isNotSiteManager") ? "manager" : "editor") + "'" +
						 " and WebDirectoryPopedom.userId in (" + sessionInfo.getUserIds() + ")";
			return new Boolean(getDatabaseService().findRecordByHql(hql)==null);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getFirstManagedSite(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public WebSite getFirstManagedSite(SessionInfo sessionInfo) throws ServiceException {
		String hql = "select WebDirectory" +
					 " from WebDirectoryPopedom WebDirectoryPopedom, WebDirectory WebDirectory" +
					 " where WebDirectoryPopedom.directoryId=WebDirectory.id" +
					 " and WebDirectory.directoryType='site'" +
					 " and WebDirectoryPopedom.popedomName='manager'" +
					 " and WebDirectoryPopedom.userId in (" + sessionInfo.getUserIds() + ")" +
					 " order by WebDirectory.id";
		return (WebSite)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getFirstEditabledSite(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public WebSite getFirstEditabledSite(SessionInfo sessionInfo) throws ServiceException {
		String hql = "select WebDirectory" +
					 " from WebDirectoryPopedom WebDirectoryPopedom, WebDirectory WebDirectory" +
					 " where WebDirectoryPopedom.directoryId=WebDirectory.id" +
					 " and WebDirectory.directoryType='site'" +
					 " and WebDirectoryPopedom.popedomName='editor'" +
					 " and WebDirectoryPopedom.userId in (" + sessionInfo.getUserIds() + ")" +
					 " order by WebDirectory.id";
		return (WebSite)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getHeadline(long)
	 */
	public Headline getHeadline(long webDirectoryId) throws ServiceException {
		String hql = "select Headline" +
					 " from Headline Headline, WebDirectorySubjection WebDirectorySubjection" +
					 " where WebDirectorySubjection.directoryId=" + webDirectoryId +
					 " and Headline.directoryId=WebDirectorySubjection.parentDirectoryId" +
					 " order by WebDirectorySubjection.id";
		return  (Headline)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#setHeadline(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setHeadline(long webDirectoryId, String headlineName, String headlineURL, String summarize) throws ServiceException {
		Headline headline = (Headline)getDatabaseService().findRecordByHql("from Headline Headline where Headline.directoryId=" + webDirectoryId);
		boolean isNew = (headline==null);
		if(isNew) {
			headline = new Headline();
			headline.setId(UUIDLongGenerator.generateId()); //ID
			headline.setDirectoryId(webDirectoryId); //目录ID
		}
		headline.setHeadlineName(headlineName); //名称
		headline.setHeadlineURL(headlineURL); //链接地址
		headline.setSummarize(summarize); //概述
		headline.setLastModified(DateTimeUtils.now()); //最后修改时间
		if(isNew) {
			getDatabaseService().saveRecord(headline);
		}
		else {
			getDatabaseService().updateRecord(headline);
		}
		//同步更新
		if(getExchangeClient()!=null) {
			getExchangeClient().synchUpdate(headline, null, 2000);
		}
		//重建静态页面
		getStaticPageBuilder().rebuildPageForModifiedObject(headline, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#isEditorDeleteable(long)
	 */
	public boolean isEditorDeleteable(long webDirectoryId) throws ServiceException {
		Character enable = getPrivilegeLevel(webDirectoryId, "editorDeleteable");
		return (enable!=null && enable.charValue()=='1');
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#isEditorReissueable(long)
	 */
	public boolean isEditorReissueable(long webDirectoryId) throws ServiceException {
		Character enable = getPrivilegeLevel(webDirectoryId, "editorReissueable");
		return (enable!=null && enable.charValue()=='1');
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#listReissueableDirectories(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listReissueableDirectories(String columnIds, SessionInfo sessionInfo) throws ServiceException {
		String[] ids = columnIds.split(",");
		List reissueableDirectories = new ArrayList(); //有权限的目录ID列表
		for(int i=0; i<ids.length; i++) {
			long columnId = Long.parseLong(ids[i]);
			List directories = listParentDirectories(columnId, "site");
			if(directories==null) {
				directories = new ArrayList();
			}
			directories.add(getDirectory(columnId));
			for(Iterator iterator = directories.iterator(); iterator.hasNext();) {
				WebDirectory directory = (WebDirectory)iterator.next();
				if(ListUtils.findObjectByProperty(reissueableDirectories, "id", new Long(directory.getId()))==null) { //不在列表中
					//获取用户权限
					List popedoms = listDirectoryPopedoms(directory.getId(), sessionInfo);
					if(popedoms==null || popedoms.isEmpty()) {
						continue;
					}
					if(!popedoms.contains("manager") && //不是管理员
					   (!popedoms.contains("editor") || !isEditorReissueable(directory.getId()))) { //不是编辑,或者不允许编辑撤销发布
						continue;
					}
				}
				if(ListUtils.findObjectByProperty(reissueableDirectories, "id", new Long(directory.getId()))==null) {
					reissueableDirectories.add(directory);
				}
				for(; iterator.hasNext();) {
					directory = (WebDirectory)iterator.next();
					if(ListUtils.findObjectByProperty(reissueableDirectories, "id", new Long(directory.getId()))==null) {
						reissueableDirectories.add(directory);
					}
				}
				break;
			}
		}
		return reissueableDirectories.isEmpty() ? null : reissueableDirectories;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getAnonymousLevel(long)
	 */
	public char getAnonymousLevel(long webDirectoryId) throws ServiceException {
		Character level = getPrivilegeLevel(webDirectoryId, "anonymousLevel");
		return (level==null || level.charValue()<SiteResourceService.ANONYMOUS_LEVEL_NONE ? SiteResourceService.ANONYMOUS_LEVEL_ALL : level.charValue());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getSynchIssueOption(long)
	 */
	public char getSynchIssueOption(long webDirectoryId) throws ServiceException {
		Character level = getPrivilegeLevel(webDirectoryId, "synchIssue");
		return (level==null || level.charValue()<SiteResourceService.SYNCH_ISSUE_ALL ? SiteResourceService.SYNCH_ISSUE_ALL : level.charValue());
	}

	/**
	 * 检查编辑的文章删除或撤销发布权限以及匿名用户访问级别
	 * @param webDirectoryId
	 * @param privilegeType
	 * @return
	 * @throws ServiceException
	 */
	private Character getPrivilegeLevel(long webDirectoryId, String privilegeType) throws ServiceException {
		String hql = "select WebDirectory." + privilegeType +
					 " from WebDirectory WebDirectory, WebDirectorySubjection WebDirectorySubjection" +
					 " where WebDirectorySubjection.directoryId=" + webDirectoryId +
					 " and WebDirectory.id=WebDirectorySubjection.parentDirectoryId" +
					 " and WebDirectory." + privilegeType + ">'0'" +
					 " order by WebDirectorySubjection.id";
		return (Character)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long siteId = RequestUtils.getParameterLongValue(notifyRequest, "siteId"); //站点ID
		WebDirectory directory = (WebDirectory)getDirectory(siteId);
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
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#createRecentUsenTreeNode(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, int)
	 */
	public TreeNode createRecentUsenTreeNode(String extendPropertyNames, SessionInfo sessionInfo, int maxColumn) throws ServiceException {
		String hqlPrefix = "select WebDirectory" +
					 	   " from WebDirectory WebDirectory, SiteResource SiteResource, SiteResourceSubjection SiteResourceSubjection" +
					 	   " where WebDirectory.id=SiteResourceSubjection.siteId" +
					 	   " and SiteResourceSubjection.resourceId=SiteResource.id" +
					 	   " and SiteResource.editorId=" + sessionInfo.getUserId() +
					 	   " and SiteResourceSubjection.id=(" +
					 	   "  select min(SiteResourceSubjection.id)" +
					 	   "   from SiteResourceSubjection SiteResourceSubjection" +
					 	   "   where SiteResourceSubjection.resourceId=SiteResource.id" +
					 	   " )";
		List treeNodes = new ArrayList();
		while(treeNodes.size()<maxColumn) {
			String hql = hqlPrefix +
						 (treeNodes.isEmpty() ? "" : " and not WebDirectory.id in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(treeNodes, "nodeId", ",", false)) + ")") +
						 " order by SiteResource.created DESC";
			List columns = getDatabaseService().findRecordsByHql(hql, 0, maxColumn);
			if(columns==null || columns.isEmpty()) {
				break;
			}
			for(Iterator iterator = columns==null ? null : columns.iterator(); iterator!=null && iterator.hasNext();) {
				WebDirectory webDirectory = (WebDirectory)iterator.next();
				if(ListUtils.findObjectByProperty(treeNodes, "nodeId", "" + webDirectory.getId())!=null) {
					continue;
				}
				TreeNode node = convertTreeNode(webDirectory, false, extendPropertyNames);
				node.setNodeText(getDirectoryFullName(webDirectory.getId(), "/", "site"));
				treeNodes.add(node);
				if(treeNodes.size()>=maxColumn) {
					break;
				}
			}
		}
		if(treeNodes.isEmpty()) {
			return null;
		}
		TreeNode recentUseNode = new TreeNode("recentUse", "最近使用", "recentUse", Environment.getContextPath() + "/cms/sitemanage/icons/site.gif", true);
		recentUseNode.setExpandTree(true); //自动展开
		recentUseNode.setChildNodes(treeNodes);
		return recentUseNode;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#listSiteEditors(long, boolean, boolean, int)
	 */
	public List listSiteEditors(long siteId, boolean myEditorsOnly, boolean asPerson, int max) throws ServiceException {
		return listDirectoryVisitors(siteId, "editor", myEditorsOnly, asPerson, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#listSiteManagers(long, boolean, boolean, int)
	 */
	public List listSiteManagers(long siteId, boolean myManagersOnly, boolean asPerson, int max) throws ServiceException {
		return listDirectoryVisitors(siteId, "manager", myManagersOnly, asPerson, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getWaterMark(long)
	 */
	public WaterMark getWaterMark(long columnId) throws ServiceException {
		WebSite site = getParentSite(columnId);
		List attachments = attachmentService.list("cms/sitemanage", "waterMark", site.getId(), false, 0, null);
		if(attachments==null || attachments.isEmpty()) {
			return null;
		}
		Attachment attachment = (Attachment)attachments.get(0);
		return new WaterMark(attachment.getFilePath(), site.getWaterMarkAlign(), site.getWaterMarkXMargin(), site.getWaterMarkYMargin());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#downloadVideoPlayerLogo(long, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void downloadVideoPlayerLogo(long siteId, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		List attachments = attachmentService.list("cms/sitemanage", "videoPlayerLogo", siteId, false, 0, null);
		if(attachments==null || attachments.isEmpty()) {
			return;
		}
		Attachment attachment = (Attachment)attachments.get(0);
		try {
			fileDownloadService.httpDownload(request, response, attachment.getFilePath(), null, false, null);
		} 
		catch (FileTransferException e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#addRelationColumns(com.yuanluesoft.cms.sitemanage.pojo.WebColumn, java.lang.String)
	 */
	public void addRelationColumns(WebColumn webColumn, String relationColumnIds) throws ServiceException {
		if(relationColumnIds==null || relationColumnIds.isEmpty()) {
			return;
		}
		String hql = "from WebColumn WebColumn" +
					 " where WebColumn.id in (" + JdbcUtils.validateInClauseNumbers(relationColumnIds) + ")";
		List columns = getDatabaseService().findRecordsByHql(hql);
		String[] ids = relationColumnIds.split(",");
		for(int i=0; i<ids.length; i++) {
			if(webColumn.getId()==Long.parseLong(ids[i]) || ListUtils.findObjectByProperty(webColumn.getRelationLinks(), "relationColumnId", new Long(ids[i]))!=null) {
				continue;
			}
			WebColumn relationColumn = (WebColumn)ListUtils.findObjectByProperty(columns, "id", new Long(ids[i]));
			WebColumnRelationLink relationLink = new WebColumnRelationLink();
			relationLink.setId(UUIDLongGenerator.generateId()); //ID
			relationLink.setColumnId(webColumn.getId()); //资源ID
			relationLink.setRelationColumnId(relationColumn.getId()); //关联栏目ID
			relationLink.setLinkName(relationColumn.getDirectoryName()); //链接名称
			relationLink.setLinkUrl(Environment.getContextPath() + "/cms/sitemanage/index.shtml?siteId=" + relationColumn.getId()); //链接地址
			relationLink.setLinkTime(relationColumn.getCreated()); //发布时间
			//relationLink.setPriority(priority); //优先级
			relationLink.setHalt(relationColumn.getHalt()=='1' ? 1 : 0); //是否停用
			save(relationLink);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#getSiteByOwnerUnitId(long)
	 */
	public WebSite getSiteByOwnerUnitId(long ownerUnitId) throws ServiceException {
		String hql = "from WebSite WebSite" +
					 " where WebSite.ownerUnitId=" + ownerUnitId;
		return (WebSite)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("reissueableDirectories".equals(itemsName)) {
			List reissueableDirectories = listReissueableDirectories(request.getParameter("columnIds"), sessionInfo);
			for(int i=0; i<(reissueableDirectories==null ? 0 : reissueableDirectories.size()); i++) {
				WebDirectory directory = (WebDirectory)reissueableDirectories.get(i);
				reissueableDirectories.set(i, new Object[]{directory.getDirectoryName(), new Long(directory.getId())});
			}
			return reissueableDirectories;
		}
		else if("childColumns".equals(itemsName)) { //子栏目
			String parentSiteIds = StringUtils.getPropertyValue((String)request.getAttribute("fieldExtendProperties"), "parentSiteIds");
			if(parentSiteIds==null || parentSiteIds.isEmpty()) {
				parentSiteIds = "" + RequestUtils.getParameterLongValue(request, "siteId");
			}
			String hql = "select WebDirectory.directoryName, WebDirectory.id" +
						 " from WebDirectory WebDirectory" +
						 " where WebDirectory.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(parentSiteIds) + ")" +
						 " and WebDirectory.directoryType!='viewReference'" +
						 " and WebDirectory.halt!='1'" +
						 " and WebDirectory.redirectUrl is null" +
						 " order by WebDirectory.priority DESC, WebDirectory.directoryName";
			return getDatabaseService().findRecordsByHql(hql);
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteService#validateHostName(javax.servlet.http.HttpServletRequest)
	 */
	public boolean validateHostName(HttpServletRequest request) throws ServiceException {
		String uri = request.getRequestURI();
		if(uri==null || uri.isEmpty() || uri.equals("/")) {
			return true;
		}
		String hostName = request.getServerName().toLowerCase();
		if(hostName.equals("localhost") || hostName.equals("127.0.0.1")) {
			return true;
		}
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		if(sessionInfo!=null && !sessionInfo.isAnonymous()) {
			return true;
		}
		HashMap hostInfos = loadHostInfos(); //获取主机信息
		String[] infos = (String[])hostInfos.get(hostName); //{站点ID, 静态页面URL, 静态页面路径}
		if(infos==null || "0".equals(infos[0])) { //主机名没有和任何站点绑定,或者主机名配置在根站点
			return true;
		}
		String siteId = null;
		if(!uri.startsWith(staticPageUrl)) { //不是静态页面
			siteId = StringUtils.getPropertyValue(request.getQueryString(), "siteId");
			if(siteId==null || siteId.isEmpty()) {
				return true;
			}
			WebSite parentSite = getParentSite(Long.parseLong(siteId));
			if(parentSite==null) {
				return true;
			}
			//检查站点是否配置了主机名
			boolean hostBinded = false;
			for(Iterator iterator = hostInfos.keySet().iterator(); !hostBinded && iterator.hasNext();) {
				hostName = (String)iterator.next();
				long hostSiteId = Long.parseLong(((String[])hostInfos.get(hostName))[0]); //{站点ID, 静态页面URL, 静态页面路径}
				hostBinded = hostSiteId==parentSite.getId() || hostSiteId==Long.parseLong(siteId);
			}
			if(!hostBinded) { //站点没有配置自己的域名
				return true;
			}
		}
		else { //静态页面
			if(infos[1]==null || !infos[1].endsWith("/index.html") || uri.startsWith(infos[1].substring(0, infos[1].lastIndexOf('/') + 1))) {
				return true;
			}
			//获取页面所在站点
			for(Iterator iterator = hostInfos.keySet().iterator(); iterator!=null && iterator.hasNext();) {
				hostName = (String)iterator.next();
				String[] siteInfos = (String[])hostInfos.get(hostName); //{站点ID, 静态页面URL, 静态页面路径}
				if(!siteInfos[0].equals("0") && siteInfos[1]!=null && siteInfos[1].endsWith("/index.html") && uri.startsWith(siteInfos[1].substring(0, siteInfos[1].lastIndexOf('/') + 1))) {
					siteId = siteInfos[0];
					break;
				}
			}
		}
		return siteId==null || siteId.isEmpty() || //站点ID为空
			   siteId.equals(infos[0]) || //站点ID和主机所在站点相同
			   isChildDirectory(Long.parseLong(siteId), Long.parseLong(infos[0])); //主机对应站点的子站点
	}
	
	/**
	 * 获取主机信息,Map数据格式:{站点ID, 静态页面URL, 静态页面路径}
	 * @return
	 * @throws ServiceException
	 */
	private HashMap loadHostInfos() throws ServiceException {
		HashMap hostPageInfos = null;
		try {
			hostPageInfos = (HashMap)recordCache.get("HostInfos");
		} 
		catch(CacheException e) {
			
		}
		if(hostPageInfos!=null) {
			return hostPageInfos;
		}
		hostPageInfos = new HashMap();
		List sites = getDatabaseService().findRecordsByHql("from WebSite WebSite where not WebSite.hostName is null");
		for(Iterator iterator = sites==null ? null : sites.iterator(); iterator!=null && iterator.hasNext();) {
			WebSite site = (WebSite)iterator.next();
			String siteUrl = Environment.getContextPath() + "/cms/sitemanage/index.shtml" + (site.getId()==0 ? "" : "?siteId=" + site.getId());
			//获取静态页面URL
			String staticPageURL = staticPageBuilder.getStaticPageURL(siteUrl);
			//获取静态页面路径
			String staticPagePath = staticPageBuilder.getStaticPagePath(siteUrl);
			//添加到映射表
			String[] hosts = site.getHostName().toLowerCase().replaceAll("[\" ]", "").replaceAll("，", ",").split(",");
			for(int i=0; i<hosts.length; i++) {
				hostPageInfos.put(hosts[i].toLowerCase(), new String[]{"" + site.getId(), staticPageURL, staticPagePath});
			}
		}
		try {
			recordCache.put("HostInfos", hostPageInfos);
		} 
		catch (CacheException e) {
			
		}
		return hostPageInfos;
	}

	/**
	 * 更新被引用的记录
	 * @param webColumn
	 * @param isDeleted
	 * @throws ServiceException
	 */
	private void updateRelatedLinks(WebColumn webColumn, boolean isDeleted) throws ServiceException {
		String hql = "from WebColumnRelationLink WebColumnRelationLink where WebColumnRelationLink.relationColumnId=" + webColumn.getId();
		List relationLinks = getDatabaseService().findRecordsByHql(hql);
		if(relationLinks==null || relationLinks.isEmpty()) {
			return;
		}
		for(Iterator iterator = relationLinks.iterator(); iterator.hasNext();) {
			WebColumnRelationLink relationLink = (WebColumnRelationLink)iterator.next();
			if(!isDeleted) {
				relationLink.setHalt(webColumn.getHalt()=='1' ? 1 : 0); //是否停用
				relationLink.setLinkName(webColumn.getDirectoryName()); //链接名称
				relationLink.setLinkTime(webColumn.getCreated()); //发布时间
				getDatabaseService().updateRecord(relationLink);
			}
			//重建静态页面
			getPageService().rebuildStaticPageForModifiedObject(relationLink, isDeleted ? StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE : StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
	}

	/**
	 * @return Returns the siteResourceService.
	 */
	public SiteResourceService getSiteResourceService() {
		return siteResourceService;
	}
	/**
	 * @param siteResourceService The siteResourceService to set.
	 */
	public void setSiteResourceService(SiteResourceService siteResourceService) {
		this.siteResourceService = siteResourceService;
	}

	/**
	 * @return the webApplicationLocalUrl
	 */
	public String getWebApplicationLocalUrl() {
		return webApplicationLocalUrl;
	}

	/**
	 * @param webApplicationLocalUrl the webApplicationLocalUrl to set
	 */
	public void setWebApplicationLocalUrl(String webApplicationLocalUrl) {
		this.webApplicationLocalUrl = webApplicationLocalUrl;
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
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	/**
	 * @return the staticPageBuilder
	 */
	public StaticPageBuilder getStaticPageBuilder() {
		return staticPageBuilder;
	}

	/**
	 * @param staticPageBuilder the staticPageBuilder to set
	 */
	public void setStaticPageBuilder(StaticPageBuilder staticPageBuilder) {
		this.staticPageBuilder = staticPageBuilder;
	}

	/**
	 * @return the fileDownloadService
	 */
	public FileDownloadService getFileDownloadService() {
		return fileDownloadService;
	}

	/**
	 * @param fileDownloadService the fileDownloadService to set
	 */
	public void setFileDownloadService(FileDownloadService fileDownloadService) {
		this.fileDownloadService = fileDownloadService;
	}

	/**
	 * @return the recordCache
	 */
	public Cache getRecordCache() {
		return recordCache;
	}

	/**
	 * @param recordCache the recordCache to set
	 */
	public void setRecordCache(Cache recordCache) {
		this.recordCache = recordCache;
	}

	/**
	 * @return the staticPageUrl
	 */
	public String getStaticPageUrl() {
		return staticPageUrl;
	}

	/**
	 * @param staticPageUrl the staticPageUrl to set
	 */
	public void setStaticPageUrl(String staticPageUrl) {
		this.staticPageUrl = staticPageUrl;
	}
}
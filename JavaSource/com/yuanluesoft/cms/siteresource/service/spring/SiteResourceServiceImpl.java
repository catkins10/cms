/*
 * Created on 2007-7-3
 *
 */
package com.yuanluesoft.cms.siteresource.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectoryPopedom;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.model.Article;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourcePhoto;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceRelationLink;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceTop;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceVideo;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.cms.smssubscription.model.SmsContentDefinition;
import com.yuanluesoft.cms.smssubscription.service.SmsContentService;
import com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.model.HTMLBodyInfo;
import com.yuanluesoft.jeaf.htmlparser.util.HTMLBodyUtils;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.instance.WorkItem;
import com.yuanluesoft.workflow.client.model.runtime.ActivityExit;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowExit;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 * 
 * @author linchuan
 * 
 */
public class SiteResourceServiceImpl extends BusinessServiceImpl implements SiteResourceService {
	private SiteService siteService; //站点服务
	private SmsSubscriptionService smsSubscriptionService; //短信订阅服务
	private ExchangeClient exchangeClient; //数据交换服务
	private AttachmentService attachmentService; //附件服务
	private boolean exchangeInternalSiteResource = false; //是否交换内部网站的资源,默认不交换
	private PageService pageService; //页面服务
	private SessionService sessionService; //会话服务
	private WorkflowExploitService workflowExploitService; //工作流利用
	private PersonService personService; //用户服务
	private RecordControlService recordControlService; //记录控制服务
	private String firstImageSelectMode; //记录图片选取方式: auto/自动(默认), manuallyOnly/手工选取, manuallyFirst/手工选取优先
	private boolean logicalDelete = false; //是否逻辑删除

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#synchUpdate(java.lang.Object)
	 */
	public void synchUpdate(Object object, String senderName) throws ServiceException {
		if(object instanceof Article) { //数据接口发送来的对象
			Article remoteArticle = (Article)object;
			if(remoteArticle.getSubject()==null || remoteArticle.getSubject().isEmpty()) {
				throw new ServiceException("subject is empty");
			}
			if(remoteArticle.getBody()==null || remoteArticle.getBody().isEmpty()) {
				throw new ServiceException("body is empty");
			}
			if(remoteArticle.getColumnIds()==null || remoteArticle.getColumnIds().isEmpty()) {
				throw new ServiceException("column ids is empty");
			}
			//删除相同源记录ID的资源
			deleteResourceBySourceRecordId(remoteArticle.getArticleId());
			//创建文章
			SiteResource article = new SiteResource();
			try {
				PropertyUtils.copyProperties(article, remoteArticle);
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new ServiceException();
			}
			article.setId(UUIDLongGenerator.generateId()); //ID
			article.setType(RESOURCE_TYPE_ARTICLE); //类型
			article.setEditor(remoteArticle.getCreatorName()); //创建人
			article.setOrgName(remoteArticle.getCreatorDepartmentName()); //创建人所在部门
			article.setUnitName(remoteArticle.getCreatorUnitName()); //创建人所在单位
			article.setSourceRecordId(remoteArticle.getArticleId()); //源记录ID
			article.setSourceRecordUrl(remoteArticle.getArticleUrl()); //源记录URL
			//把接收到的文件保存为附件
			attachmentService.uploadFiles("cms/siteresource", "attachments", null, article.getId(), remoteArticle.getAttachmentFilePaths());
			attachmentService.uploadFiles("cms/siteresource", "images", null, article.getId(), remoteArticle.getImageFilePaths());
			attachmentService.uploadFiles("cms/siteresource", "videos", null, article.getId(), remoteArticle.getVideoFilePaths());
			//重设正文
			article.setBody(HTMLBodyUtils.resetExchangeHtmlBody(remoteArticle.getBody(), remoteArticle.getCharset(), article));
			//保存
			createResource(article, remoteArticle.getColumnIds(), remoteArticle.isDirectIssue());
			return;
		}
		super.synchUpdate(object, senderName);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#synchDelete(java.lang.Object)
	 */
	public void synchDelete(Object object, String senderName) throws ServiceException {
		if(object instanceof Article) { //数据接口发送来的对象
			Article article = (Article)object;
			deleteResourceBySourceRecordId(article.getArticleId());
			return;
		}
		super.synchDelete(object, senderName);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#issue(com.yuanluesoft.cms.siteresource.pojo.SiteResource, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issue(SiteResource resource, SessionInfo sessionInfo) throws ServiceException {
		//发布到其它栏目
		if(resource.getOtherColumnIds()!=null && !resource.getOtherColumnIds().isEmpty()) {
			String otherColumnIds = null;
			String otherColumnNames = null;
			String[] columnIds = resource.getOtherColumnIds().split(",");
			String[] columnNames = resource.getOtherColumnNames().split(",");
			long siteId = siteService.getParentSite(((SiteResourceSubjection)resource.getSubjections().iterator().next()).getSiteId()).getId();
			for(int i=0; i<columnIds.length; i++) {
				long columnId = Long.parseLong(columnIds[i]);
				//检查栏目是否存在
				if(!siteService.isDirectoryExists(columnId)) { //栏目已经不存在
					continue;
				}
				otherColumnIds = (otherColumnIds==null ? "" : otherColumnIds + ",") + columnIds[i];
				otherColumnNames = (otherColumnNames==null ? "" : otherColumnNames + ",") + columnNames[i];
				//获取文章同步发布选项
				char synchIssueOption = siteService.getSynchIssueOption(columnId);
				if(SYNCH_ISSUE_ALL==synchIssueOption || //直接发布
				   (SYNCH_ISSUE_SAME_SITE==synchIssueOption && (siteService.getParentSite(columnId).getId()==siteId))) { //相同站点的直接发布且站点相同
					//增加资源隶属
					if(ListUtils.findObjectByProperty(resource.getSubjections(), "siteId", new Long(columnId))==null) {
						SiteResourceSubjection subjection = new SiteResourceSubjection();
						subjection.setId(UUIDLongGenerator.generateId()); //ID
						subjection.setResourceId(resource.getId()); //资源ID
						subjection.setSiteId(columnId); //栏目ID
						getDatabaseService().saveRecord(subjection);
						resource.getSubjections().add(subjection);
					}
				}
				else if(getSiteResourceCopy(resource.getId(), columnId)==null) { //不直接发布且没有复制过
					SiteResource copiedResource;
					try {
						copiedResource = (SiteResource)resource.clone(); //复制一个新的文章/链接
					}
					catch(CloneNotSupportedException e) {
						Logger.exception(e);
						throw new ServiceException(e.getMessage());
					}
					copiedResource.setSubjections(null);
					copiedResource.setWorkItems(null);
					copiedResource.setWorkflowInstanceId(null);
					copiedResource.setOtherColumnIds(null);
					copiedResource.setOtherColumnNames(null);
					copiedResource.setIssueTime(null);
					copiedResource.setIssuePersonId(0);
					copiedResource.setSourceRecordId("" + resource.getId());
					copiedResource.setSourceRecordClassName(SiteResource.class.getName());
					copiedResource.setSourceRecordUrl(Environment.getContextPath() + "/cms/siteresource/admin/" + RESOURCE_TYPE_NAMES[resource.getType()] + ".shtml?act=edit&id=" + resource.getId());
					if(copiedResource.getEditorId()==0 && sessionInfo!=null) {
						copiedResource.setEditorId(sessionInfo.getUserId());
						copiedResource.setEditor(sessionInfo.getUserName());
					}
					//复制附件
					String attachmentPath = attachmentService.getSavePath("cms/siteresource", null, resource.getId(), false);
					if(FileUtils.isExists(attachmentPath)) { //附件目录存在
						String destPath = FileUtils.createDirectory(attachmentPath.substring(0, attachmentPath.lastIndexOf('/', attachmentPath.length()-2)) + "/" + copiedResource.getId());
						FileUtils.copyDirectory(attachmentPath, destPath, true, true);
						String body = copiedResource.getBody();
						if(body!=null) {
							copiedResource.setBody(body.replaceAll("/" + resource.getId() + "/", "/" + copiedResource.getId() + "/"));
						}
					}
					createResource(copiedResource, "" + columnId, false);
				}
			}
			//更新其它栏目信息
			resource.setOtherColumnIds(otherColumnIds);
			resource.setOtherColumnNames(otherColumnNames);
		}
		resource.setIssuePersonId(sessionInfo==null ? 100 : sessionInfo.getUserId());
		resource.setStatus(SiteResourceService.RESOURCE_STATUS_ISSUE);
		if(resource.getIssueTime()==null) {
			resource.setIssueTime(DateTimeUtils.now());
		}
		getDatabaseService().updateRecord(resource);

		//发送短信订阅
		sendMessageToSubscriber(resource);
	
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(resource, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		
		//更新相关链接,并重建静态页面
		updateRelatedLinks(resource);
		
		//同步更新
		if(isExchangeable(resource)) {
			exchangeClient.synchUpdate(resource, null, 2000);
		}
	}
	
	/**
	 * 发送短信订阅
	 * @param resource
	 * @throws ServiceException
	 */
	private void sendMessageToSubscriber(SiteResource resource) throws ServiceException {
		if(resource.getType()==SiteResourceService.RESOURCE_TYPE_ARTICLE) { //文章
			SiteResourceSubjection subjection = (SiteResourceSubjection)resource.getSubjections().iterator().next();
			//发送短信订阅
			String message = "新闻:" + resource.getSubject() + "\r\n" + StringUtils.filterHtmlElement(resource.getBody(), false);
			smsSubscriptionService.sendMessageToSubscriber("siteResourceService", "新闻", siteService.getParentSite(subjection.getSiteId()).getId(), message, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#unissue(com.yuanluesoft.cms.siteresource.pojo.SiteResource)
	 */
	public void unissue(SiteResource resource) throws ServiceException {
		resource.setStatus(SiteResourceService.RESOURCE_STATUS_UNISSUE);
		//删除资源隶属关系
		Iterator iterator = resource.getSubjections().iterator();
		iterator.next();
		while(iterator.hasNext()) {
			SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
			if(siteService.isDirectoryExists(subjection.getSiteId())) { //栏目存在
				appendOtherColumn(resource, subjection.getSiteId());
			}
			else { //栏目已经删除
				removeOtherColumn(resource, subjection.getSiteId());
			}
			getDatabaseService().deleteRecord(subjection);
			iterator.remove();
			//重新生成静态页面
			pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
		getDatabaseService().updateRecord(resource);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(resource, StaticPageBuilder.OBJECT_MODIFY_ACTION_LOGICAL_DELETE);
		//同步删除
		if(isExchangeable(resource)) {
			exchangeClient.synchDelete(resource, null, 2000);
		}
		//更新相关链接,并重建静态页面
		updateRelatedLinks(resource);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#unissueTimeoutResources()
	 */
	public void unissueTimeoutResources() throws ServiceException {
		if(Logger.isTraceEnabled()) {
			Logger.trace("SiteResourceService: unissue timeout resources.");
		}
		List lazyLoadProperties = listLazyLoadProperties(SiteResource.class);
		String hql = "from SiteResource SiteResource" +
					 " where SiteResource.status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'" + //已发布的
					 " and SiteResource.issueEndTime is not null" + //截止时间不为空
					 " and SiteResource.issueEndTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")"; //大于当前时间
		for(int i=0; i<1000; i++) {
			List resources = getDatabaseService().findRecordsByHql(hql, lazyLoadProperties, 0, 200);
			for(Iterator iterator = resources==null ? null : resources.iterator(); iterator!=null && iterator.hasNext();) {
				SiteResource resource = (SiteResource)iterator.next();
				try {
					unissue(resource);
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
			if(resources==null || resources.size()<200) {
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteResourceService#getSiteResource(long)
	 */
	public SiteResource getSiteResource(long id) throws ServiceException {
		return (SiteResource)load(SiteResource.class, id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#getSiteResourceCopy(long, long)
	 */
	public SiteResource getSiteResourceCopy(long id, long columnId) throws ServiceException {
		String hql = "select SiteResource" +
					 " from SiteResource SiteResource left join SiteResource.subjections SiteResourceSubjection" +
					 " where SiteResource.sourceRecordId='" + id + "'" +
					 " and SiteResourceSubjection.siteId=" + columnId;
		return (SiteResource)getDatabaseService().findRecordByHql(hql, listLazyLoadProperties(SiteResource.class));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof SiteResource) {
			SiteResource resource = (SiteResource)record;
			try {
				updateAttachmentInfo(resource, true);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			if(resource.getAnonymousLevel()<ANONYMOUS_LEVEL_NONE) { //匿名访问级别
				resource.setAnonymousLevel(ANONYMOUS_LEVEL_ALL);
			}
			//如果文章是已发布的(如：导入的文章),重新生成静态页面
			if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE && resource.getSubjections()!=null && !resource.getSubjections().isEmpty()) {
				pageService.rebuildStaticPageForModifiedObject(resource, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
			//同步更新
			if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE && isExchangeable(resource)) {
				exchangeClient.synchUpdate(resource, null, 2000);
			}
		}
		return super.save(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof SiteResource) {
			SiteResource resource = (SiteResource)record;
			try {
				updateAttachmentInfo(resource, false);
			}
			catch(Exception e) {
				
			}
			//如果文章是已发布的(如：导入的文章),重新生成静态页面
			if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE) {
				if(resource.getSubjections()!=null && !resource.getSubjections().isEmpty()) {
					pageService.rebuildStaticPageForModifiedObject(resource, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
				}
				//同步更新
				if(isExchangeable(resource)) {
					exchangeClient.synchUpdate(resource, null, 2000);
				}
			}
			else if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_DELETED + (SiteResourceService.RESOURCE_STATUS_ISSUE - '0')) { //逻辑删除
				if(resource.getSubjections()!=null && !resource.getSubjections().isEmpty()) {
					pageService.rebuildStaticPageForModifiedObject(resource, StaticPageBuilder.OBJECT_MODIFY_ACTION_LOGICAL_DELETE);
				}
				//同步删除
				if(isExchangeable(resource)) {
					exchangeClient.synchDelete(resource, null, 2000);
				}
				//更新相关链接,并重建静态页面
				updateRelatedLinks(resource);
			}
		}
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof SiteResource) {
			SiteResource resource = (SiteResource)record;
			if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE) {
				pageService.rebuildStaticPageForModifiedObject(resource, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE); //重新生成静态页面
			}
			if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE && isExchangeable(resource)) {
				exchangeClient.synchDelete(resource, null, 2000); //同步删除外网的数据
			}
			//如果源记录就是站点资源,从源资源中删除当前栏目
			if(resource.getSourceRecordId()!=null && !resource.getSourceRecordId().isEmpty() && SiteResource.class.getName().equals(resource.getSourceRecordClassName())) {
				SiteResource sourceResource = (SiteResource)load(SiteResource.class, Long.parseLong(resource.getSourceRecordId()));
				if(sourceResource!=null && removeOtherColumn(sourceResource, ((SiteResourceSubjection)resource.getSubjections().iterator().next()).getSiteId())) {
					getDatabaseService().updateRecord(sourceResource); //更新资源
					if(sourceResource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE && isExchangeable(sourceResource)) {
						exchangeClient.synchUpdate(sourceResource, null, 2000); //同步更新外网的数据
					}
				}
			}
			if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE) {
				resource.setStatus((char)(SiteResourceService.RESOURCE_STATUS_DELETED + (SiteResourceService.RESOURCE_STATUS_ISSUE - '0')));
				//更新相关链接,并重建静态页面
				updateRelatedLinks(resource);
			}
		}
		super.delete(record);
	}
		
	/**
	 * 判断站点资源是否需要被交换
	 * @param resource
	 * @return
	 */
	private boolean isExchangeable(SiteResource resource) {
		if(exchangeInternalSiteResource) { //内部网站的资源也需要交换
			return true;
		}
		try {
			if(resource.getSubjections()==null || resource.getSubjections().isEmpty()) { //没有隶属关系
				return false;
			}
			//检查资源是否属于内部网站
			for(Iterator iterator = resource.getSubjections().iterator(); iterator.hasNext();) {
				SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
				if(siteService.getParentSite(subjection.getSiteId()).getIsInternal()!='1') { //不是内部网站
					return true;
				}
			}
		}
		catch(Exception e) {
			
		}
		return false;
	}

	/**
	 * 更新图片和视频数量
	 * @param resource
	 */
	private void updateAttachmentInfo(SiteResource resource, boolean isNew) throws Exception {
		//更新实际上传的图片数量
		List images = (List)FieldUtils.getFieldValue(resource, "images", null);
		resource.setUploadImageCount(images==null ? 0 : images.size());
		//更新实际上传的视频数量
		List videos = (List)FieldUtils.getFieldValue(resource, "videos", null);
		resource.setUploadVideoCount(videos==null ? 0 : videos.size());
		//更新实际上传的附件数量
		List attachments = (List)FieldUtils.getFieldValue(resource, "attachments", null);
		resource.setUploadAttachmentCount(attachments==null ? 0 : attachments.size());

		String body = resource.getBody();
		if(body==null) {
			return;
		}
		//分析正文
		HTMLBodyInfo htmlBodyInfo = HTMLBodyUtils.analysisHTMLBody(resource, body, null);
		//设置记录图片
		resource.setImageCount(htmlBodyInfo.getImageCount());
		if("manuallyOnly".equals(firstImageSelectMode) || "manuallyFirst".equals(firstImageSelectMode)) { //记录图片选取方式: auto/自动(默认), manuallyOnly/手工选取, manuallyFirst/手工选取优先
			if(resource.getImageCount()==0 && resource.getFirstImageName()!=null && !resource.getFirstImageName().isEmpty()) {
				resource.setImageCount(1);
			}
		}
		if(firstImageSelectMode==null || "auto".equals(firstImageSelectMode) || //自动设置默认图片
		   ("manuallyFirst".equals(firstImageSelectMode) && (resource.getFirstImageName()==null || resource.getFirstImageName().isEmpty()))) { //手工优先,且没有设置过
		  	if(resource.getResourcePhotos()==null || resource.getResourcePhotos().isEmpty()) {
				resource.setFirstImageName(htmlBodyInfo.getFirstImageName());
			}
		  	else {
				resource.setFirstImageName(((SiteResourcePhoto)resource.getResourcePhotos().iterator().next()).getName());
				resource.setImageCount(resource.getResourcePhotos().size());
			}
		}
		if(resource.getResourceVideos()!=null && !resource.getResourceVideos().isEmpty()) {
			resource.setFirstVideoName(((SiteResourceVideo)resource.getResourceVideos().iterator().next()).getName());
			resource.setVideoCount(resource.getResourceVideos().size());
		}
		else {
			resource.setFirstVideoName(htmlBodyInfo.getFirstVideoName());
			resource.setVideoCount(htmlBodyInfo.getVideoCount());
		}
		resource.setAttachmentCount(htmlBodyInfo.getAttachmentCount());
		if(htmlBodyInfo.isBodyChanged()) {
			resource.setBody(htmlBodyInfo.getNewBody());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteResourceService#updateSiteResourceSubjections(com.yuanluesoft.cms.sitemanage.pojo.SiteResource, boolean, java.lang.String)
	 */
	public void updateSiteResourceSubjections(SiteResource siteResource, boolean isNew, String subjectionDirectoryIds) throws ServiceException {
		if(subjectionDirectoryIds==null || subjectionDirectoryIds.equals("")) {
			return;
		}
		String[] ids = subjectionDirectoryIds.split(",");
		long firstDirectoryId = Long.parseLong(ids[0]);
		boolean firstDirectoryChanged = true;
		boolean otherColumnChanged = false;
		String oldSubjectionDirectoryIds = null; //旧的目录隶属关系
		if(!isNew) {
			//检查隶属栏目是否发生变化
			oldSubjectionDirectoryIds = ListUtils.join(siteResource.getSubjections(), "siteId", ",", false);
			if(subjectionDirectoryIds.equals(oldSubjectionDirectoryIds)) { //没有变化
				return;
			}
			firstDirectoryChanged = siteResource.getSubjections()==null || siteResource.getSubjections().isEmpty() || firstDirectoryId!=((SiteResourceSubjection)siteResource.getSubjections().iterator().next()).getSiteId();
			//删除旧的隶属关系
			boolean firstColumn = true;
			for(Iterator iterator = siteResource.getSubjections()==null ? null : siteResource.getSubjections().iterator(); iterator!=null && iterator.hasNext();) {
				SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
				getDatabaseService().deleteRecord(subjection);
				if(siteResource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE && //已经发布
				   ("," + subjectionDirectoryIds + ",").indexOf("," + subjection.getSiteId() + ",")==-1) { //已经被删除了
					if(!firstColumn && removeOtherColumn(siteResource, subjection.getSiteId())) {
						otherColumnChanged = true;
					}
					pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
				}
				firstColumn = false;
			}
		}
		//保存新的隶属关系
		siteResource.setSubjections(new HashSet());
		boolean firstColumn = true;
		for(int i=0; i<ids.length; i++) {
			if(ListUtils.findObjectByProperty(siteResource.getSubjections(), "siteId", new Long(ids[i]))!=null) { //重复
				continue;
			}
			SiteResourceSubjection subjection = null;
			if(firstColumn || siteResource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE) { //第一个栏目,或者文章已经发布
				subjection = new SiteResourceSubjection();
				subjection.setId(UUIDLongGenerator.generateId());
				subjection.setResourceId(siteResource.getId());
				subjection.setSiteId(Long.parseLong(ids[i]));
				getDatabaseService().saveRecord(subjection);
				siteResource.getSubjections().add(subjection);
			}
			if(("," + oldSubjectionDirectoryIds + ",").indexOf("," + ids[i] + ",")==-1) { //新增的
				if(!firstColumn && appendOtherColumn(siteResource, Long.parseLong(ids[i]))) {
					otherColumnChanged = true;
				}
				if(siteResource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE && !firstDirectoryChanged && !otherColumnChanged) {  //已经发布
					pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
				}
			}
			firstColumn = false;
		}
		if(firstDirectoryChanged || otherColumnChanged) {
			if(firstDirectoryChanged) { //第一个目录改变了
				siteResource.setColumnName(siteService.getDirectory(firstDirectoryId).getDirectoryName()); //设置所在栏目名称
			}
			update(siteResource);
		}
		else { //同步更新
			if(siteResource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE && isExchangeable(siteResource)) {
				exchangeClient.synchUpdate(siteResource, null, 2000);
			}
		}
	}
	
	/**
	 * 删除其它栏目
	 * @param siteResource
	 * @param removedColumnId
	 * @return
	 */
	private boolean removeOtherColumn(SiteResource siteResource, long removedColumnId) {
		if(siteResource.getOtherColumnIds()==null || siteResource.getOtherColumnIds().isEmpty()) { //原来就没有
			return false;
		}
		List otherColumnIds = ListUtils.generateList(siteResource.getOtherColumnIds(), ",");
		List otherColumnNames = ListUtils.generateList(siteResource.getOtherColumnNames(), ",");
		int index = otherColumnIds.indexOf("" + removedColumnId);
		if(index==-1) {
			return false;
		}
		otherColumnIds.remove(index);
		otherColumnNames.remove(index);
		siteResource.setOtherColumnIds(otherColumnIds.isEmpty() ? null : ListUtils.join(otherColumnIds, ",", false));
		siteResource.setOtherColumnNames(otherColumnIds.isEmpty() ? null : ListUtils.join(otherColumnNames, ",", false));
		return true;
	}
	
	/**
	 * 添加其它栏目
	 * @param siteResource
	 * @param apendColumnId
	 * @return
	 */
	private boolean appendOtherColumn(SiteResource siteResource, long apendColumnId) throws ServiceException {
		if(("," + siteResource.getOtherColumnIds() + ",").indexOf("," + apendColumnId + ",")!=-1) { //已经存在
			return false;
		}
		siteResource.setOtherColumnIds((siteResource.getOtherColumnIds()==null || siteResource.getOtherColumnIds().isEmpty() ? "" : siteResource.getOtherColumnIds() + ",") + apendColumnId);
		siteResource.setOtherColumnNames((siteResource.getOtherColumnNames()==null || siteResource.getOtherColumnNames().isEmpty() ? "" : siteResource.getOtherColumnNames() + ",") + siteService.getDirectoryFullName(apendColumnId, "/", "site"));
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#listSiteResources(java.lang.String, java.lang.String, boolean, boolean, int, int)
	 */
	public List listSiteResources(String siteIds, String resouceTypes, boolean containChildren, boolean issuedOnly, int offset, int max) throws ServiceException {
		String where = null;
		String join = null;
		if(!containChildren) { //不显示子站、子栏目
			where = " where subjections.siteId in (" + JdbcUtils.validateInClauseNumbers(siteIds) + ")";
		}
		else if(("," + siteIds + ",").indexOf(",0,")==-1) { //不包括主站
			join = ", WebDirectorySubjection WebDirectorySubjection";
			where = " where subjections.siteId=WebDirectorySubjection.directoryId" +
					 " and WebDirectorySubjection.parentDirectoryId in (" +  JdbcUtils.validateInClauseNumbers(siteIds) + ")";
	    }
		if(issuedOnly) {
			where = (where==null ? " where" : where + " and") + " SiteResource.status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'";
		}
		//解析资源类型
		if(!resouceTypes.equals("all")) { //是否显示全部资源
			for(int i=0; i<RESOURCE_TYPE_NAMES.length; i++) {
				resouceTypes = resouceTypes.replaceFirst(RESOURCE_TYPE_NAMES[i], "" + i);
			}
			where = (where==null ? " where" : where + " and") + " SiteResource.type in (" + resouceTypes + ")";
		}
		String hql = "select distinct SiteResource" +
					 " from SiteResource SiteResource" +
				     " left join SiteResource.subjections subjections" + (join==null ? "" : join) +
				     where +
				     " order by SiteResource.issueTime DESC, SiteResource.created DESC";
		return getDatabaseService().findRecordsByHql(hql, offset, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#addResource(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, char, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String, long, java.lang.String, long, java.lang.String)
	 */
	public SiteResource addResource(String siteIds, int type, String subject, String subhead, String source, String author, String keyword, String mark, char anonymousLevel, String link, Timestamp created, Timestamp issueTime, boolean isIssue, String body, String sourceRecordId, String sourceRecordClassName, String sourceRecordUrl, long issuePersonId, String issuePersonName, long issuePersonOrgId, String issuePersonOrgName, long issuePersonUnitId, String issuePersonUnitName) throws ServiceException {
		SiteResource resource = null;
		//获取相同源记录ID的资源
		if(sourceRecordId!=null && !sourceRecordId.isEmpty()) {
			Number resourceId = (Number)getDatabaseService().findRecordByHql("select SiteResource.id from SiteResource SiteResource where SiteResource.sourceRecordId='" + JdbcUtils.resetQuot(sourceRecordId) + "' order by SiteResource.id DESC");
			if(resourceId!=null && resourceId.longValue()>0) {
				resource = (SiteResource)load(SiteResource.class, resourceId.longValue());
			}
		}
		List directories = siteService.listDirectories(siteIds);
		if(directories==null || directories.isEmpty()) {
			return null;
		}
		siteIds = ListUtils.join(directories, "id", ",", true);
		if(anonymousLevel<=ANONYMOUS_LEVEL_AUTO) {
			anonymousLevel = siteService.getAnonymousLevel(Long.parseLong(siteIds.split(",")[0]));
		}
		//创建新的站点资源
		boolean isNew = resource==null;
		if(isNew) {
			resource = new SiteResource();
			resource.setId(UUIDLongGenerator.generateId());
		}
		resource.setType(type);
		resource.setSubject(subject);
		resource.setSubhead(subhead);
		resource.setSource(source);
		resource.setAuthor(author);
		resource.setKeyword(keyword);
		resource.setMark(mark);
		resource.setAnonymousLevel(anonymousLevel);
		resource.setCreated(created);
		resource.setIssueTime(issueTime);
		resource.setIssuePersonId(isIssue ? (issuePersonId<=0 ? 100 : issuePersonId) : 0);
		resource.setBody(body==null || body.equals("") ? "　" : body);
		resource.setSourceRecordId(sourceRecordId); //源记录ID
		resource.setSourceRecordClassName(sourceRecordClassName); //源记录类名称
		resource.setSourceRecordUrl(sourceRecordUrl); //源记录URL
		resource.setLink(type==SiteResourceService.RESOURCE_TYPE_LINK ? sourceRecordUrl : null); //链接地址
		resource.setEditorId(issuePersonId);
		resource.setEditor(issuePersonName);
		resource.setOrgId(issuePersonOrgId);
		resource.setOrgName(issuePersonOrgName);
		resource.setUnitId(issuePersonUnitId);
		resource.setUnitName(issuePersonUnitName);
		if(isNew) {
			createResource(resource, siteIds, isIssue);
		}
		else {
			update(resource);
			updateSiteResourceSubjections(resource, false, siteIds);
		}
		return resource;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#deleteResourceBySourceRecordId(java.lang.String)
	 */
	public SiteResource deleteResourceBySourceRecordId(String sourceRecordId) throws ServiceException {
		if(sourceRecordId==null || sourceRecordId.isEmpty()) {
			return null;
		}
		Number resourceId = (Number)getDatabaseService().findRecordByHql("select SiteResource.id from SiteResource SiteResource where SiteResource.sourceRecordId='" + JdbcUtils.resetQuot(sourceRecordId) + "' order by SiteResource.id DESC");
		if(resourceId!=null && resourceId.longValue()>0) {
			SiteResource siteResource = (SiteResource)load(SiteResource.class, resourceId.longValue());
			delete(siteResource);
			return siteResource;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#listColumnIdsBySourceRecordId(java.lang.String)
	 */
	public List listColumnIdsBySourceRecordId(String sourceRecordId) throws ServiceException {
		String hql = "select SiteResourceSubjection.siteId" +
					 " from SiteResourceSubjection SiteResourceSubjection, SiteResource SiteResource" +
					 " where SiteResourceSubjection.resourceId=SiteResource.id" +
					 " and SiteResource.sourceRecordId='" + JdbcUtils.resetQuot(sourceRecordId) + "'" +
					 " order by SiteResourceSubjection.id";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsContentService#listSmsContentDefinitions()
	 */
	public List listSmsContentDefinitions() throws ServiceException {
		List contentDefinitions = new ArrayList();
		contentDefinitions.add(new SmsContentDefinition("新闻", null, SmsContentService.SEND_MODE_NEWS, null));
		return contentDefinitions;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsContentService#getSmsReplyContent(java.lang.String, java.util.Map, java.lang.String, java.lang.String, long)
	 */
	public String getSmsReplyContent(String contentName, Map fieldValueMap, String message, String senderNumber, long siteId) throws ServiceException {
		return null; //不需要实时回复
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsContentService#getContentDescription(java.lang.String, java.lang.String, long)
	 */
	public String getContentDescription(String contentName, String subscribeParameter, long siteId) throws ServiceException {
		return contentName;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureRecordService#processCapturedRecord(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask, com.yuanluesoft.cms.capture.model.CaptureRecord)
	 */
	public void processCapturedRecord(CmsCaptureTask captureTask, CapturedRecord capturedRecord) throws ServiceException {
		SiteResource resource = (SiteResource)capturedRecord.getRecord();
		if(resource==null || resource.getSubject()==null || resource.getSubject().trim().isEmpty()) { //标题不能为空
			return;
		}
		if(resource.getBody()==null || resource.getBody().isEmpty()) { //正文为空
			return;
		}
		resource.setType(RESOURCE_TYPE_ARTICLE); //类型
		resource.setSourceRecordUrl(capturedRecord.getUrl()); //抓取的URL

		//解析参数配置,获取栏目列表
		String issueColumnIds = null; //直接发布的栏目列表
		String notIssueColumnIds = null; //不直接发布的栏目列表
		for(int i=0; i<100; i++) {
			String key = StringUtils.getPropertyValue(captureTask.getExtendedParameters(), "key" + (i==0 ? "" : "_" + i));
			if(StringUtils.isMatch(resource.getSubject(), key)) { //检查是否匹配
				String columnIds = StringUtils.getPropertyValue(captureTask.getExtendedParameters(), "columnIds" + (i==0 ? "" : "_" + i));
				if(columnIds==null || columnIds.isEmpty()) {
					continue;
				}
				if("true".equals(StringUtils.getPropertyValue(captureTask.getExtendedParameters(), "issue" + (i==0 ? "" : "_" + i)))) { //直接发布
					issueColumnIds = (issueColumnIds==null ? "" : issueColumnIds + ",") + columnIds;
				}
				else { //不直接发布
					notIssueColumnIds = (notIssueColumnIds==null ? "" : notIssueColumnIds + ",") + columnIds;
				}
			}
		}
		if(issueColumnIds!=null) {
			if(notIssueColumnIds!=null) {
				try {
					resource = (SiteResource)resource.clone();
				} 
				catch (CloneNotSupportedException e) {
					Logger.exception(e);
					throw new ServiceException();
				}
			}
			resource.setAnonymousLevel(siteService.getAnonymousLevel(Long.parseLong(issueColumnIds.split(",")[0]))); //匿名用户访问级别,不能访问|1\0仅标题|2\0完全访问|3
			//获取同步的栏目
			List synchColumnIds = siteService.listSynchColumnIds(issueColumnIds);
			createResource(resource, issueColumnIds + (synchColumnIds==null || synchColumnIds.isEmpty() ? "" : "," + ListUtils.join(synchColumnIds, ",", false)), true); //保存
		}
		if(notIssueColumnIds!=null) {
			resource = (SiteResource)capturedRecord.getRecord();
			resource.setAnonymousLevel(siteService.getAnonymousLevel(Long.parseLong(notIssueColumnIds.split(",")[0]))); //匿名用户访问级别,不能访问|1\0仅标题|2\0完全访问|3
			//获取同步的栏目
			List synchColumnIds = siteService.listSynchColumnIds(notIssueColumnIds);
			createResource(resource, notIssueColumnIds + (synchColumnIds==null || synchColumnIds.isEmpty() ? "" : "," + ListUtils.join(synchColumnIds, ",", false)), false); //保存
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#listCaptureTasksByColumnId(long)
	 */
	public List listCaptureTasksByColumnId(long columnId) throws ServiceException {
		String hql = "from CmsCaptureTask CmsCaptureTask" +
					 " where CmsCaptureTask.enabled=1" +
					 " and CmsCaptureTask.businessClassName='" + SiteResource.class.getName() + "'" +
					 " and (CmsCaptureTask.extendedParameters like '%=" + columnId + "&%'" +
					 " or CmsCaptureTask.extendedParameters like '%=" + columnId + ",%'" +
					 " or CmsCaptureTask.extendedParameters like '%," + columnId + "&%'" +
					 " or CmsCaptureTask.extendedParameters like '%," + columnId + ",%')";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#getResourceTypes(java.lang.String)
	 */
	public Map getResourceTypes(String resourceIds) throws ServiceException {
		List types = getDatabaseService().findRecordsByHql("select SiteResource.id, SiteResource.type from SiteResource SiteResource where SiteResource.id in (" + JdbcUtils.validateInClauseNumbers(resourceIds) + ")");
		HashMap map = new HashMap();
		for(Iterator iterator = types.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			map.put(values[0], values[1]);
		}
		return map;
	}

	/**
	 * 保存一个站点资源
	 * @param resource
	 * @param columnIds
	 * @param issue
	 * @throws ServiceException
	 */
	private void createResource(SiteResource resource, String columnIds, boolean issue) throws ServiceException {
		if(columnIds==null || columnIds.isEmpty()) {
			return;
		}
		if(resource.getSubject()==null || resource.getSubject().isEmpty()) {
			throw new ServiceException("subject is null");
		}
		if(resource.getBody()==null || resource.getBody().isEmpty()) {
			throw new ServiceException("body is null");
		}
		if(resource.getCreated()==null) {
			resource.setCreated(DateTimeUtils.now());
		}
		resource.setStatus(SiteResourceService.RESOURCE_STATUS_TODO);
		SessionInfo sessionInfo = null;
		final long columnId = Long.parseLong(columnIds.split(",")[0]);
		if(issue) { //直接发布
			resource.setStatus(SiteResourceService.RESOURCE_STATUS_ISSUE);
			save(resource); //保存主记录
			updateSiteResourceSubjections(resource, true, columnIds); //设置隶属站点
			//添加查看权限
			WebDirectory column = (WebDirectory)siteService.getDirectory(columnId);
			List userIds = ListUtils.generatePropertyList(column.getDirectoryPopedoms(), "userId");
			if(userIds==null) {
				userIds = new ArrayList();
			}
			if(resource.getEditorId()>0) {
				userIds.add(new Long(resource.getEditorId())); //添加文章编辑自己
			}
			if(resource.getIssuePersonId()>100 && resource.getIssuePersonId()!=resource.getEditorId()) {
				userIds.add(new Long(resource.getIssuePersonId())); //添加发布人
			}
			if(userIds.isEmpty()) {
				userIds.add(new Long(0));
			}
			for(Iterator iterator = userIds.iterator(); iterator.hasNext();) {
				Number userId = (Number)iterator.next();
				recordControlService.appendVisitor(resource.getId(), SiteResource.class.getName(), userId.longValue(), RecordControlService.ACCESS_LEVEL_READONLY);
			}
			issue(resource, null); //发布
		}
		else { //不直接发布，随机指定一个作为编辑
			//创建流程实例
			long workflowId = siteService.getApprovalWorkflowId(columnId);
			if(workflowId<=0) {
				throw new ServiceException("流程未定义");
			}
			WorkflowEntry workflowEntry = null;
			if(resource.getEditorId()>0) { //有指定编辑
				try {
					sessionInfo = sessionService.getSessionInfo(personService.getPerson(resource.getEditorId()).getLoginName());
					if(siteService.checkPopedom(columnId, "editor", sessionInfo)) { //检查用户的编辑权限
						workflowEntry = workflowExploitService.getWorkflowEntry("" + workflowId, null, resource, sessionInfo);
					}
				}
				catch(Exception e) {
					
				}
			}
			List siteEditors = null;
			if(workflowEntry==null) { //没有找到流程入口
				siteEditors = siteService.listSiteEditors(columnId, true, true, 20); //获取站点编辑列表,最多20个
				if(siteEditors==null || siteEditors.isEmpty()) {
					siteEditors = siteService.listSiteEditors(columnId, false, true, 20); //获取上级栏目编辑列表,最多20个
				}
				if(siteEditors==null || siteEditors.isEmpty()) {
					throw new ServiceException("站点编辑未配置");
				}
				Person person = (Person)siteEditors.get(0);
				try {
					sessionInfo = sessionService.getSessionInfo(person.getLoginName());
				}
				catch(SessionException e) {
					Logger.exception(e);
					throw new ServiceException(e.getMessage());
				}
				workflowEntry = workflowExploitService.getWorkflowEntry("" + workflowId, null, resource, sessionInfo);
				if(workflowEntry==null) {
					throw new ServiceException("流程定义不正确");
				}
			}
			resource.setStatus(RESOURCE_STATUS_TODO);
			resource.setIssuePersonId(0);
			if(resource.getEditorId()==0) {
				resource.setEditorId(sessionInfo.getUserId());
			}
			if(resource.getEditor()==null || resource.getEditor().isEmpty()) {
				resource.setEditor(sessionInfo.getUserName());
			}
			resource.setOrgId(sessionInfo.getDepartmentId());
			resource.setOrgName(sessionInfo.getDepartmentName());
			resource.setUnitId(sessionInfo.getUnitId());
			resource.setUnitName(sessionInfo.getUnitName());
		
			//创建流程实例
			Element activity = (Element)workflowEntry.getActivityEntries().get(0);
			WorkflowParticipantCallback participantCallback = new WorkflowParticipantCallback() {
				public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					return "siteEditor".equals(programmingParticipantId) ? siteService.listSiteEditors(columnId, false, false, 0) : siteService.listSiteManagers(columnId, false, false, 0);
				}
				public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					return participants;
				}
				public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					return false;
				}
			};
			try {
				String workflowInstanceId = workflowExploitService.createWorkflowInstance(workflowEntry.getWorkflowId(), activity.getId(), false, resource, participantCallback, sessionInfo);
				List workItems = workflowExploitService.listRunningWorkItems(workflowInstanceId, false, sessionInfo);
				WorkItem workItem = (WorkItem)workItems.get(0);
				if(siteEditors==null) { //没有获取站点编辑,说明是以文章编辑自己的身份处理
					//检查流程出口,如果仅有一个环节出口且自动发送,则自动发送
					WorkflowExit workflowExit = workflowExploitService.getWorkflowExit(workflowInstanceId, workItem.getId(), false, resource, null, participantCallback, sessionInfo);
					if(workflowExit!=null && workflowExit.getExits()!=null && workflowExit.getExits().size()==1 && (workflowExit.getExits().get(0) instanceof ActivityExit)) {
						ActivityExit activityExit = (ActivityExit)workflowExit.getExits().get(0);
						if(activityExit.isAutoSend()) {
							workflowExploitService.lockWorkflowInstance(workflowInstanceId, sessionInfo);
							WorkflowMessage workflowMessage = new WorkflowMessage(RESOURCE_TYPE_TITLES[resource.getType()], resource.getSubject(), Environment.getContextPath() + "/cms/siteresource/admin/" + RESOURCE_TYPE_NAMES[resource.getType()] + ".shtml?act=edit&id=" + resource.getId());
							workflowExploitService.completeWorkItem(workflowInstanceId, workItem.getId(), false, workflowMessage, workflowExit, resource, null, participantCallback, sessionInfo);
							workflowExploitService.unlockWorkflowInstance(workflowInstanceId, sessionInfo);
						}
					}
					else { //流程只有一个环节
						//获取编辑,加为办理人
						siteEditors = siteService.listSiteEditors(columnId, true, true, 20); //获取站点编辑列表,最多20个
					}
				}
				if(siteEditors!=null && siteEditors.size()>1) { //把其他编辑加为办理人
					siteEditors = siteEditors.subList(1, siteEditors.size());
					workflowExploitService.lockWorkflowInstance(workflowInstanceId, sessionInfo);
					workflowExploitService.addParticipants(workflowInstanceId, workItem.getId(), false, ListUtils.join(siteEditors, "id", ",", false), ListUtils.join(siteEditors, "name", ",", false), null, resource, sessionInfo);
					if(resource.getEditorId()!=sessionInfo.getUserId()) { //指定了编辑
						//把编辑设置为可读
						workflowExploitService.addVisitor(workflowInstanceId, workItem.getId(), "" + resource.getEditorId(), resource.getEditor(), null, resource, sessionInfo);
					}
					workflowExploitService.unlockWorkflowInstance(workflowInstanceId, sessionInfo);
				}
				resource.setWorkflowInstanceId(workflowInstanceId); //流程实例ID
				//保存主记录
				save(resource);
				//添加隶属站点
				updateSiteResourceSubjections(resource, true, columnIds);
			}
			catch(Exception e) {
				Logger.exception(e);
				if(resource.getWorkflowInstanceId()!=null) {
					workflowExploitService.removeWorkflowInstance(resource.getWorkflowInstanceId(), resource, sessionInfo);
				}
				throw new ServiceException(e.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if(itemsName.equals("searchColumns")) { //搜索栏目列表
			String searchSiteId = (String)request.getAttribute("searchSiteId");
			String searchSiteName = (String)request.getAttribute("searchSiteName");
			//设置hql以获取栏目列表
			String hql = "select WebDirectory.directoryName, WebDirectory.id" +
						 " from WebDirectory WebDirectory" +
						 " where WebDirectory.id!=0" +
						 " and WebDirectory.parentDirectoryId=" + searchSiteId;
			List columns = getDatabaseService().findRecordsByHql(hql);
			if(columns==null) {
				columns = new ArrayList();
			}
			columns.add(0, new String[]{searchSiteName, searchSiteId});
			return columns;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#modifyReaders(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void modifyReaders(View view, String currentCategories, String searchConditions, String selectedResourceIds, String modifyMode, boolean selectedResourceOnly, boolean deleteNotColumnVisitor, String readerIds, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String[] userIds = null;
		if(!"synchColumnVisitor".equals(modifyMode)) { //不是同步栏目访问者
			if(readerIds==null || readerIds.isEmpty()) { //用户ID为空
				return;
			}
			userIds = readerIds.split(",");
		}
		if(selectedResourceOnly) { //选中的文章
			if(selectedResourceIds==null || selectedResourceIds.isEmpty()) {
				return;
			}
			List ids = ListUtils.generateList(selectedResourceIds, ",");
			for(int i=0; i<ids.size() ; i+=100) {
				String hql = "from SiteResource SiteResource" +
							 " where SiteResource.id in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(ids.subList(i, Math.min(i+100, ids.size())), ",", false)) + ")";
				List resources = getDatabaseService().findRecordsByHql(hql, i, 100);
				doModifyReaders(resources, modifyMode, deleteNotColumnVisitor, userIds);
			}
			return;
		}
		try {
			view = (View)view.clone();
		}
		catch(CloneNotSupportedException e) {
			
		}
		view.setFilter(null); //不过滤,处理所有的记录
		view.setPageRows(100);
		ViewService viewService = ViewUtils.getViewService(view);
		//构造视图包
		ViewPackage viewPackage = new ViewPackage();
		viewPackage.setView(view);
		viewPackage.setSearchConditions(searchConditions);
		viewPackage.setSearchMode(searchConditions!=null);
		viewPackage.setCategories(currentCategories);
		for(int page=1; ; page++) {
			//设置当前页
			viewPackage.setCurPage(page);
			try {
				viewService.retrieveViewPackage(viewPackage, view, 0, true, false, false, request, sessionInfo);
			}
			catch (PrivilegeException e) {
				
			}
			//获取记录
			if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) {
				break;
			}
			//更新访问者
			doModifyReaders(viewPackage.getRecords(), modifyMode, deleteNotColumnVisitor, userIds);
			if(page>=viewPackage.getPageCount()) {
				break;
			}
		}
	}
	
	/**
	 * 修改文章的访问者
	 * @param resources
	 * @param modifyMode
	 * @param deleteNotColumnVisitor
	 * @param readerIds
	 * @throws ServiceException
	 */
	private void doModifyReaders(List resources, String modifyMode, boolean deleteNotColumnVisitor, String[] readerIds) throws ServiceException {
		for(Iterator iterator = resources.iterator(); iterator.hasNext(); ) {
			SiteResource resource = (SiteResource)iterator.next();
			if("addUser".equals(modifyMode)) { //添加用户
				for(int i=0; i<readerIds.length; i++) {
					recordControlService.appendVisitor(resource.getId(), SiteResource.class.getName(), Long.parseLong(readerIds[i]), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
			else if("deleteUser".equals(modifyMode)) { //删除用户
				for(int i=0; i<readerIds.length; i++) {
					recordControlService.removeVisitor(resource.getId(), SiteResource.class.getName(), Long.parseLong(readerIds[i]), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
			else if("synchColumnVisitor".equals(modifyMode)) { //同步栏目访问者
				//获取隶属栏目ID
				String hql = "select SiteResourceSubjection.siteId" +
							 " from SiteResourceSubjection SiteResourceSubjection" +
							 " where SiteResourceSubjection.resourceId=" + resource.getId() +
							 " order by SiteResourceSubjection.id";
				long columnId = ((Number)getDatabaseService().findRecordByHql(hql)).longValue();
				//获取访问者
				hql = "from WebDirectoryPopedom WebDirectoryPopedom where WebDirectoryPopedom.directoryId=" + columnId;
				List columnPopedoms = getDatabaseService().findRecordsByHql(hql);
				if(columnPopedoms==null || columnPopedoms.isEmpty()) {
					continue;
				}
				if(deleteNotColumnVisitor) { //删除非栏目访问者
					recordControlService.removeVisitors(resource.getId(), resource.getClass().getName(), RecordControlService.ACCESS_LEVEL_READONLY);
				}
				for(Iterator iteratorPopedom = columnPopedoms.iterator(); iteratorPopedom.hasNext();) {
					WebDirectoryPopedom popedom = (WebDirectoryPopedom)iteratorPopedom.next();
					recordControlService.appendVisitor(resource.getId(), SiteResource.class.getName(), popedom.getUserId(), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#addRelationLinks(com.yuanluesoft.cms.siteresource.pojo.SiteResource, java.lang.String)
	 */
	public void addRelationLinks(SiteResource siteResource, String resourceIds) throws ServiceException {
		if(resourceIds==null || resourceIds.isEmpty()) {
			return;
		}
		String hql = " from SiteResource SiteResource" +
					 " where SiteResource.id in (" + JdbcUtils.validateInClauseNumbers(resourceIds) + ")";
		List resources = getDatabaseService().findRecordsByHql(hql);
		String[] ids = resourceIds.split(",");
		for(int i=0; i<ids.length; i++) {
			if(siteResource.getId()==Long.parseLong(ids[i]) || ListUtils.findObjectByProperty(siteResource.getRelationLinks(), "relationResourceId", new Long(ids[i]))!=null) {
				continue;
			}
			SiteResource relationResource = (SiteResource)ListUtils.findObjectByProperty(resources, "id", new Long(ids[i]));
			if(relationResource.getStatus()>=SiteResourceService.RESOURCE_STATUS_DELETED) {
				continue;
			}
			SiteResourceRelationLink relationLink = new SiteResourceRelationLink();
			relationLink.setId(UUIDLongGenerator.generateId()); //ID
			relationLink.setResourceId(siteResource.getId()); //资源ID
			relationLink.setRelationResourceId(relationResource.getId()); //关联资源ID
			relationLink.setLinkName(relationResource.getSubject()); //链接名称
			relationLink.setLinkUrl(relationResource.getType()==SiteResourceService.RESOURCE_TYPE_LINK ? relationResource.getLink() : Environment.getContextPath() + "/cms/siteresource/article.shtml?id=" + relationResource.getId()); //链接地址
			relationLink.setLinkTime(relationResource.getIssueTime()); //发布时间
			//relationLink.setPriority(priority); //优先级
			relationLink.setHalt(relationResource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE ? 0 : 1); //是否停用
			getDatabaseService().saveRecord(relationLink);
		}
	}
	
	/**
	 * 更新被引用的记录
	 * @param siteResource
	 * @param isDeleted
	 * @throws ServiceException
	 */
	private void updateRelatedLinks(SiteResource siteResource) throws ServiceException {
		String hql = "from SiteResourceRelationLink SiteResourceRelationLink where SiteResourceRelationLink.relationResourceId=" + siteResource.getId();
		List relationLinks = getDatabaseService().findRecordsByHql(hql);
		if(relationLinks==null || relationLinks.isEmpty()) {
			return;
		}
		for(Iterator iterator = relationLinks.iterator(); iterator.hasNext();) {
			SiteResourceRelationLink relationLink = (SiteResourceRelationLink)iterator.next();
			relationLink.setHalt(siteResource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE ? 0 : 1); //是否停用
			relationLink.setLinkName(siteResource.getSubject()); //链接名称
			relationLink.setLinkTime(siteResource.getIssueTime()); //发布时间
			getDatabaseService().updateRecord(relationLink);
			//重建静态页面
			pageService.rebuildStaticPageForModifiedObject(load(SiteResource.class, relationLink.getResourceId()), StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#countSiteResources(long, char, java.sql.Date, java.sql.Date)
	 */
	public int countSiteResources(long columnId, char status, Date beginDate, Date endDate) throws ServiceException {
		String hql = "select count(distinct SiteResource.id)" +
					 " from SiteResource SiteResource";
		if(columnId!=0) { //不是根站点
	    	hql += " left join SiteResource.subjections SiteResourceSubjection, WebDirectorySubjection WebDirectorySubjection" +
				   " where SiteResourceSubjection.siteId=WebDirectorySubjection.directoryId" +
				   " and WebDirectorySubjection.parentDirectoryId=" + columnId;
		}
		if(status!='a') { //不是全部
			hql += (hql.indexOf(" where ")==-1 ? " where" : " and") + " SiteResource.status='" + status + "'";
		}
		String dateFieldName = status==RESOURCE_STATUS_ISSUE ? "issueTime" : "created";
		if(beginDate!=null) {
			hql += (hql.indexOf(" where ")==-1 ? " where" : " and") + " SiteResource." + dateFieldName + ">=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")";
		}
		if(endDate!=null) {
			hql += (hql.indexOf(" where ")==-1 ? " where" : " and") + " SiteResource." + dateFieldName + "<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")";
		}
		Number count = (Number)getDatabaseService().findRecordByHql(hql);
		return count==null ? 0 : count.intValue();
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.service.SiteResourceService#setTop(long, java.lang.String, long[], java.sql.Date, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void setTop(long resourceId, String columnIds, long[] topDirectoryIds, Date expire, SessionInfo sessionInfo) throws ServiceException {
		SiteResource resource = (SiteResource)load(SiteResource.class, resourceId);
		//获取用户有撤销发布权限的栏目或站点
		List reissueableDirectories = siteService.listReissueableDirectories(columnIds, sessionInfo);
		if(reissueableDirectories==null || reissueableDirectories.isEmpty()) {
			return;
		}
		boolean changed = false;
		//删除原来的置顶记录
		for(Iterator iterator = resource.getResourceTops()==null ? null : resource.getResourceTops().iterator(); iterator!=null && iterator.hasNext();) {
			SiteResourceTop top = (SiteResourceTop)iterator.next();
			if(ListUtils.findObjectByProperty(reissueableDirectories, "id", new Long(top.getSiteId()))!=null) {
				getDatabaseService().deleteRecord(top);
				iterator.remove();
				changed = true;
			}
		}
		if(topDirectoryIds!=null && topDirectoryIds.length>0) {
			if(resource.getResourceTops()==null) {
				resource.setResourceTops(new LinkedHashSet());
			}
			changed = true;
			for(int i=0; i<topDirectoryIds.length; i++) {
				SiteResourceTop top = new SiteResourceTop();
				top.setId(UUIDLongGenerator.generateId()); //ID
				top.setResourceId(resourceId); //资源ID
				top.setSiteId(topDirectoryIds[i]); //站点/栏目ID
				top.setExpire(expire); //有效期
				top.setCreated(DateTimeUtils.now()); //创建时间
				getDatabaseService().saveRecord(top);
				resource.getResourceTops().add(top);
			}
		}
		if(!changed) {
			return;
		}
		//同步更新
		if(isExchangeable(resource)) {
			exchangeClient.synchUpdate(resource, null, 2000);
		}
		//重新生成静态页面
		if(resource.getStatus()==RESOURCE_STATUS_ISSUE) {
			pageService.rebuildStaticPageForModifiedObject(resource, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
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
	 * @return the smsSubscriptionService
	 */
	public SmsSubscriptionService getSmsSubscriptionService() {
		return smsSubscriptionService;
	}

	/**
	 * @param smsSubscriptionService the smsSubscriptionService to set
	 */
	public void setSmsSubscriptionService(
			SmsSubscriptionService smsSubscriptionService) {
		this.smsSubscriptionService = smsSubscriptionService;
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
	 * @return the exchangeInternalSiteResource
	 */
	public boolean isExchangeInternalSiteResource() {
		return exchangeInternalSiteResource;
	}

	/**
	 * @param exchangeInternalSiteResource the exchangeInternalSiteResource to set
	 */
	public void setExchangeInternalSiteResource(boolean exchangeInternalSiteResource) {
		this.exchangeInternalSiteResource = exchangeInternalSiteResource;
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
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}

	/**
	 * @return the logicalDelete
	 */
	public boolean isLogicalDelete() {
		return logicalDelete;
	}

	/**
	 * @param logicalDelete the logicalDelete to set
	 */
	public void setLogicalDelete(boolean logicalDelete) {
		this.logicalDelete = logicalDelete;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @return the recordControlService
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}

	/**
	 * @param recordControlService the recordControlService to set
	 */
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}

	/**
	 * @return the firstImageSelectMode
	 */
	public String getFirstImageSelectMode() {
		return firstImageSelectMode;
	}

	/**
	 * @param firstImageSelectMode the firstImageSelectMode to set
	 */
	public void setFirstImageSelectMode(String firstImageSelectMode) {
		this.firstImageSelectMode = firstImageSelectMode;
	}
}
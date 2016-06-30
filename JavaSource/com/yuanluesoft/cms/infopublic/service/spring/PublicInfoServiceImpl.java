package com.yuanluesoft.cms.infopublic.service.spring;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.WritableSheet;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfWriter;
import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.infopublic.model.Info;
import com.yuanluesoft.cms.infopublic.model.InfoCategoryStat;
import com.yuanluesoft.cms.infopublic.model.InfoStat;
import com.yuanluesoft.cms.infopublic.model.MonitoringReport;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectoryPopedom;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoPrivilege;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoSequence;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoSubjection;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicUnitCode;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.cms.smssubscription.model.SmsContentDefinition;
import com.yuanluesoft.cms.smssubscription.service.SmsContentCallback;
import com.yuanluesoft.cms.smssubscription.service.SmsContentService;
import com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.model.HTMLBodyInfo;
import com.yuanluesoft.jeaf.htmlparser.util.HTMLBodyUtils;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.ZipUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.instance.WorkItem;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author yuanluesoft
 *
 */
public class PublicInfoServiceImpl extends BusinessServiceImpl implements PublicInfoService {
	private HTMLParser htmlParser; //网页解析
	private FileDownloadService fileDownloadService; //文件传输服务
	private AttachmentService attachmentService;
	private PublicDirectoryService publicDirectoryService;
	private OrgService orgService; //组织机构服务
	private SiteResourceService siteResourceService;
	private String temporaryDirectory;
	private SmsSubscriptionService smsSubscriptionService; //短信订阅服务
	private TemporaryFileManageService temporaryFileManageService; //临时文件管理服务
	private ExchangeClient exchangeClient; //数据交换客户端
	private PageService pageService; //政府信息页面服务
	private SessionService sessionService; //会话服务
	private WorkflowExploitService workflowExploitService; //工作流利用
	private RecordControlService recordControlService; //记录控制服务
	private boolean logicalDelete = false; //是否逻辑删除
	private boolean indexByIssueTime = false; //是否按发布时间编号

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#synchUpdate(java.lang.Object)
	 */
	public void synchUpdate(Object object, String senderName) throws ServiceException {
		if(object instanceof Info) { //数据接口发送来的对象
			Info remoteInfo = (Info)object;
			if(remoteInfo.getSubject()==null || remoteInfo.getSubject().isEmpty()) {
				throw new ServiceException("subject is null");
			}
			if(remoteInfo.getBody()==null || remoteInfo.getBody().isEmpty()) {
				throw new ServiceException("body is null");
			}
			if(remoteInfo.getDirectoryId()<=0) {
				throw new ServiceException("directory id is null");
			}
			//删除相同源记录ID的资源
			PublicInfo deletedInfo = deleteInfoBySourceRecordId(remoteInfo.getInfoId());
			//创建信息
			PublicInfo info = new PublicInfo();
			try {
				PropertyUtils.copyProperties(info, remoteInfo);
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new ServiceException();
			}
			info.setId(UUIDLongGenerator.generateId()); //ID
			info.setCreator(remoteInfo.getCreatorName()); //创建人
			info.setOrgName(remoteInfo.getCreatorDepartmentName()); //创建人所在部门
			info.setUnitName(remoteInfo.getCreatorUnitName()); //创建人所在单位
			info.setSourceRecordId(remoteInfo.getInfoId()); //源记录ID
			info.setSourceRecordUrl(remoteInfo.getInfoUrl()); //源记录URL
			if(deletedInfo!=null) {
				info.setInfoIndex(deletedInfo.getInfoIndex()); //保留旧的索引号
			}
			//把接收到的文件保存为附件
			attachmentService.uploadFiles("cms/siteresource", "attachments", null, info.getId(), remoteInfo.getAttachmentFilePaths());
			attachmentService.uploadFiles("cms/siteresource", "images", null, info.getId(), remoteInfo.getImageFilePaths());
			attachmentService.uploadFiles("cms/siteresource", "videos", null, info.getId(), remoteInfo.getVideoFilePaths());
			//重设正文
			info.setBody(HTMLBodyUtils.resetExchangeHtmlBody(remoteInfo.getBody(), remoteInfo.getCharset(), info));
			//保存
			saveInfo(info, remoteInfo.getDirectoryId(), remoteInfo.isDirectIssue());
			return;
		}
		super.synchUpdate(object, senderName);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#synchDelete(java.lang.Object)
	 */
	public void synchDelete(Object object, String senderName) throws ServiceException {
		if(object instanceof Info) { //数据接口发送来的对象
			Info info = (Info)object;
			deleteInfoBySourceRecordId(info.getInfoId());
			return;
		}
		super.synchDelete(object, senderName);
	}

	/**
	 * 按源记录ID删除信息
	 * @param sourceRecordId
	 * @throws ServiceException
	 */
	public PublicInfo deleteInfoBySourceRecordId(String sourceRecordId) throws ServiceException {
		if(sourceRecordId==null || sourceRecordId.isEmpty()) {
			return null;
		}
		Number publicInfoId = (Number)getDatabaseService().findRecordByHql("select PublicInfo.id from PublicInfo PublicInfo where PublicInfo.sourceRecordId='" + JdbcUtils.resetQuot(sourceRecordId) + "' order by PublicInfo.id DESC");
		if(publicInfoId!=null && publicInfoId.longValue()>0) {
			PublicInfo publicInfo = (PublicInfo)load(PublicInfo.class, publicInfoId.longValue());
			delete(publicInfo);
			return publicInfo;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof PublicInfo) {
			PublicInfo info = (PublicInfo)record;
			try {
				updateAttachmentInfo(info);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			generateIndexIfNecessary(info); //当信息处于发布状态且索引号为空时，自动生成索引号
			//如果已发布的(如：数据导入),重新生成静态页面
			if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE && info.getSubjections()!=null && !info.getSubjections().isEmpty()) {
				pageService.rebuildStaticPageForModifiedObject(info, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
			//同步更新
			if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) {
				exchangeClient.synchUpdate(info, null, 2000);
			}
		}
		return super.save(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof PublicInfo) {
			PublicInfo info = (PublicInfo)record;
			try {
				updateAttachmentInfo(info);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			generateIndexIfNecessary(info); //当信息处于发布状态且索引号为空时，自动生成索引号
			
			if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) {
				//如果已发布的(如：数据导入),重新生成静态页面
				if(info.getSubjections()!=null && !info.getSubjections().isEmpty()) {
					pageService.rebuildStaticPageForModifiedObject(info, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
				}
				//同步更新
				exchangeClient.synchUpdate(info, null, 2000);
			}
			else if(info.getStatus()==PublicInfoService.INFO_STATUS_DELETED + (PublicInfoService.INFO_STATUS_ISSUE - '0')) { //逻辑删除
				if(info.getSubjections()!=null && !info.getSubjections().isEmpty()) {
					pageService.rebuildStaticPageForModifiedObject(info, StaticPageBuilder.OBJECT_MODIFY_ACTION_LOGICAL_DELETE);
				}
				//同步删除
				exchangeClient.synchDelete(info, null, 2000);
			}
			if(info.getStatus()>=PublicInfoService.INFO_STATUS_DELETED) { //逻辑删除
				//重新生成索引号
				regenerateIndexIfNecessary(info);
			}
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		PublicInfo info =  (PublicInfo)record;
		//从网站撤销
		siteResourceService.deleteResourceBySourceRecordId("" + info.getId());
		super.delete(record);
		if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) {
			exchangeClient.synchDelete(info, null, 2000); //同步删除
			pageService.rebuildStaticPageForModifiedObject(info, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE); //重新生成静态页面
		}
		//重新生成索引号
		regenerateIndexIfNecessary(info);
	}
	
	/**
	 * 重新生成索引号
	 * @param info
	 */
	private void regenerateIndexIfNecessary(final PublicInfo info) throws ServiceException {
		if(info.getType()!=INFO_TYPE_NORMAL || info.getInfoIndex()==null || info.getInfoIndex().isEmpty()) { //索引号为空
			return;
		}
		//获取主目录
		final PublicMainDirectory mianDirectory = publicDirectoryService.getMainDirectory(((PublicInfoSubjection)info.getSubjections().iterator().next()).getDirectoryId());
		if(mianDirectory.getRecodeEnabled()==0) { //不允许重建索引号
			return;
		}
		final String infoIndex = info.getInfoIndex();
		info.setInfoIndex(null); //置空
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					String[] values = infoIndex.split("-"); //机构代码、目录编号、年度、流水号
					List lazyLoadProperties = listLazyLoadProperties(PublicInfo.class);
					DecimalFormat formatter = new DecimalFormat("00000");
					int sn = Integer.parseInt(values[3])-1;
					for(int i=0; ; i+=2) {
						String hql = "from PublicInfo PublicInfo" +
									 " where PublicInfo.id!=" + info.getId() +
									 " and PublicInfo.status<'" + INFO_STATUS_DELETED + "'" +
									 " and PublicInfo.infoIndex like '" + values[0] + "-" + (mianDirectory.getSequenceByDirectory()==1 ? values[1] : "%") + "-" + values[2] + "-%'" +
									 " and substr(PublicInfo.infoIndex, length(PublicInfo.infoIndex)-4)>'" + formatter.format(sn) + "'" +
									 " order by substr(PublicInfo.infoIndex, length(PublicInfo.infoIndex)-4)";
						List infos = getDatabaseService().findRecordsByHql(hql, lazyLoadProperties, 0, 2);
						for(Iterator iterator = (infos==null ? null : infos.iterator()); iterator!=null && iterator.hasNext();) {
							PublicInfo otherInfo = (PublicInfo)iterator.next();
							//更新索引号
							sn++;
							otherInfo.setInfoIndex(otherInfo.getInfoIndex().substring(0, otherInfo.getInfoIndex().lastIndexOf('-') + 1) + formatter.format(sn));
							getDatabaseService().updateRecord(otherInfo);
							pageService.rebuildStaticPageForModifiedObject(otherInfo, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重新生成静态页面
							exchangeClient.synchUpdate(otherInfo, null, 2000); //同步更新
						}
						if(infos==null || infos.size()<2) {
							break;
						}
					}
					//更新序号表
					String hql = "from PublicInfoSequence PublicInfoSequence" +
						  		 " where PublicInfoSequence.year=" + values[2] +
						  		 " and PublicInfoSequence.category='" + values[0] + (mianDirectory.getSequenceByDirectory()==1 ? "-" + values[1] : "") + "'";
					PublicInfoSequence publicInfoSequence = (PublicInfoSequence)getDatabaseService().findRecordByHql(hql);
					if(publicInfoSequence!=null) {
						publicInfoSequence.setSequence(sn);
						getDatabaseService().updateRecord(publicInfoSequence);
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				timer.cancel();
			}
		}, 3000); //等待3s
	}

	/**
	 * 更新图片和视频数量
	 * @param resource
	 */
	private void updateAttachmentInfo(PublicInfo info) throws Exception {
		HTMLBodyInfo htmlBodyInfo = HTMLBodyUtils.analysisHTMLBody(info, info.getBody(), null);
		if(htmlBodyInfo.isBodyChanged()) {
			info.setBody(htmlBodyInfo.getNewBody());
		}
	}
	
	/**
	 * 当信息处于发布状态且索引号为空时，自动生成索引号
	 * @param info
	 * @throws ServiceException
	 */
	private boolean generateIndexIfNecessary(PublicInfo info) throws ServiceException {
		try {
			if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE && //已经发布的
			   (info.getInfoIndex()==null || info.getInfoIndex().trim().isEmpty()) && //索引号为空
			   info.getSubjections()!=null && !info.getSubjections().isEmpty()) { //配置了隶属关系
				//设置索引号
				long directoryId = ((PublicInfoSubjection)info.getSubjections().iterator().next()).getDirectoryId();
				generateInfoIndex(info, directoryId, info.getInfoIndex());
				return true;
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#getPublicInfo(long)
	 */
	public PublicInfo getPublicInfo(long id) throws ServiceException {
		return (PublicInfo)load(PublicInfo.class, id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#issue(com.yuanluesoft.cms.infopublic.pojo.PublicInfo, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issue(PublicInfo info, SessionInfo sessionInfo) throws ServiceException {
		//设置索引号
		final long directoryId = ((PublicInfoSubjection)info.getSubjections().iterator().next()).getDirectoryId();
		generateInfoIndex(info, directoryId, info.getInfoIndex());
		if(info.getIssueTime()==null) { //设置发布日期
			info.setIssueTime(DateTimeUtils.now());
		}
		info.setStatus(PublicInfoService.INFO_STATUS_ISSUE);
		info.setIssuePersonId(sessionInfo==null ? 100 : sessionInfo.getUserId());
		getDatabaseService().updateRecord(info);
		addArticle(info); //同步到网站
		
		//发送短信订阅
		final List parentDirectories = publicDirectoryService.listParentDirectories(directoryId, null); //获取父目录列表
		SmsContentCallback callback = new SmsContentCallback() {
			public boolean isSendable(String subscribeParameter) {
				String subscribeDirectoryId = StringUtils.getPropertyValue(subscribeParameter, "directoryId");
				return (subscribeDirectoryId==null ||
					   subscribeDirectoryId.isEmpty() ||
					   Long.parseLong(subscribeDirectoryId)==directoryId ||
					   ListUtils.findObjectByProperty(parentDirectories, "id", new Long(subscribeDirectoryId))!=null);
			}
		};
		String message = "信息:" + info.getSubject() + "\r\n" + (info.getSummarize()!=null && !info.getSummarize().isEmpty() ? info.getSummarize() : StringUtils.filterHtmlElement(info.getBody(), false));
		smsSubscriptionService.sendMessageToSubscriber("publicInfoService", "信息公开", publicDirectoryService.getMainDirectory(directoryId).getSiteId(), message, callback);
		
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(info, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		
		//同步更新
		exchangeClient.synchUpdate(info, null, 2000);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#unissue(com.yuanluesoft.cms.infopublic.pojo.PublicInfo)
	 */
	public void unissue(PublicInfo info) throws ServiceException {
		info.setStatus(PublicInfoService.INFO_STATUS_UNISSUE);
		getDatabaseService().updateRecord(info);
		//从网站撤销
		siteResourceService.deleteResourceBySourceRecordId("" + info.getId());
		//同步删除
		exchangeClient.synchDelete(info, null, 2000);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(info, StaticPageBuilder.OBJECT_MODIFY_ACTION_LOGICAL_DELETE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#resynchAllInfos()
	 */
	public void resynchAllInfos(boolean forceDirectorySynchSiteIds) throws ServiceException {
		String hql = "from PublicInfo PublicInfo";
		for(int i=0; ; i+=100) { //每次更新100条
			List infos = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections,opinions,lazyBody", ","), i, 100);
			if(infos==null || infos.isEmpty()) {
				break;
			}
			for(Iterator iterator = infos.iterator(); iterator.hasNext();) {
				PublicInfo info = (PublicInfo)iterator.next();
				try {
					//获取所在目录
					long directoryId = ((PublicInfoSubjection)info.getSubjections().iterator().next()).getDirectoryId();
					//获取目录需要同步的栏目列表
					String synchSiteIds = publicDirectoryService.getDirectorySynchSiteIds(directoryId, true);
					if(forceDirectorySynchSiteIds) {
						info.setIssueSiteIds(synchSiteIds);
						getDatabaseService().updateRecord(info);
					}
					else if(synchSiteIds!=null && !"".equals(synchSiteIds)) {
						boolean changed = false;
						String[] siteIds = synchSiteIds.split(",");
						//检查栏目ID是否在现在的同步栏目列表中
						String issueIds = info.getIssueSiteIds()==null ? "" : info.getIssueSiteIds();
						for(int j=0; j<siteIds.length; j++) {
							if(issueIds.indexOf(siteIds[j])==-1) { //目前没有加入
								issueIds += (issueIds.equals("") ? "" : ",") + siteIds[j];
								changed = true;
							}
						}
						if(changed) {
							info.setIssueSiteIds(issueIds);
							getDatabaseService().updateRecord(info);
						}
					}
					if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) {
						addArticle(info); //同步到网站
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#resynchPublicInfos(long, java.lang.String, java.lang.String)
	 */
	public void resynchPublicInfos(long directoryId, String oldSynchSiteIds, String newSynchSiteIds) throws ServiceException {
		//获取新增的、删除掉的栏目ID
		List oldIds = ListUtils.generateList(oldSynchSiteIds, ",");
		List newIds = ListUtils.generateList(newSynchSiteIds, ",");
		List addedSynchSiteIds = ListUtils.getNotInsideSubList(oldIds, null, newIds, null);
		List deletedSynchSiteIds = ListUtils.getNotInsideSubList(newIds, null, oldIds, null);
		if(addedSynchSiteIds==null && deletedSynchSiteIds==null) {
			return;
		}
		//获取目录下的信息
		String hql = "select distinct PublicInfo from PublicInfo PublicInfo";
		if(directoryId>0) { //根目录显示全部
			hql += " left join PublicInfo.subjections PublicInfoSubjection, PublicDirectorySubjection PublicDirectorySubjection" +
				   " where PublicInfoSubjection.directoryId=PublicDirectorySubjection.directoryId" +
				   " and PublicDirectorySubjection.parentDirectoryId=" +  directoryId;
	    }
		for(int i=0; ; i+=100) { //每次更新100条
			List infos = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections,opinions,lazyBody", ","), i, 100);
			if(infos==null || infos.isEmpty()) {
				break;
			}
			for(Iterator iterator = infos.iterator(); iterator.hasNext();) {
				PublicInfo info = (PublicInfo)iterator.next();
				try {
					resynchPublicInfo(info, ListUtils.join(addedSynchSiteIds, ",", false), ListUtils.join(deletedSynchSiteIds, ",", false));
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#resynchPublicInfo(com.yuanluesoft.cms.infopublic.pojo.PublicInfo, java.lang.String, java.lang.String)
	 */
	public void resynchPublicInfo(PublicInfo info, String addedSynchSiteIds, String deletedSynchSiteIds) throws ServiceException {
		if(info.getIssueSite()!='1') { //不需要同步
			return;
		}
		List addedSiteIds = ListUtils.generateList(addedSynchSiteIds, ",");
		List deletedSiteIds = ListUtils.generateList(deletedSynchSiteIds, ",");
		boolean changed = false;
		List issueSiteIds = null;
		if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) { //已发布
			info.setIssueSiteIds(ListUtils.join(siteResourceService.listColumnIdsBySourceRecordId("" + info.getId()), ",", false)); //获取实际同步的栏目ID列表
		}
		issueSiteIds = ListUtils.generateList(info.getIssueSiteIds(), ",");
		if(issueSiteIds==null) {
			issueSiteIds = new ArrayList();
		}
		//处理增加的栏目ID列表
		List toAdd = ListUtils.getNotInsideSubList(issueSiteIds, null, addedSiteIds, null);
		if(toAdd!=null) {
			changed = true;
			issueSiteIds.addAll(toAdd);
		}
		//处理删除的栏目ID列表
		if(deletedSiteIds!=null && !deletedSiteIds.isEmpty() && !issueSiteIds.isEmpty()) {
			int index;
			for(Iterator iteratorId = deletedSiteIds.iterator(); iteratorId.hasNext();) {
				String deletedId = (String)iteratorId.next();
				if((index=issueSiteIds.indexOf(deletedId))!=-1) {
					issueSiteIds.remove(index);
					changed = true;
				}
			}
		}
		if(changed) {
			info.setIssueSiteIds(ListUtils.join(issueSiteIds, ",", false));
			getDatabaseService().updateRecord(info);
			if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) {
				addArticle(info); //同步到网站
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#resynchPublicInfo(com.yuanluesoft.cms.infopublic.pojo.PublicInfo)
	 */
	public void resynchPublicInfo(PublicInfo info) throws ServiceException {
		getDatabaseService().updateRecord(info);
		addArticle(info);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#listInfosByIds(java.lang.String)
	 */
	public List listInfosByIds(String ids, boolean loadBody) throws ServiceException {
		if(ids==null || ids.equals("")) {
			return null;
		}
		String hql = "from PublicInfo PublicInfo" +
					 " where PublicInfo.id in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
					 " order by PublicInfo.generateDate";
		return getDatabaseService().findRecordsByHql(hql, (loadBody ? ListUtils.generateList("lazyBody", ",") : null));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#exportPublicInfo(long, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void exportPublicInfo(long rootDirectoryId, String ids, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		if(ids==null || ids.equals("")) {
			return;
		}
		String path = FileUtils.createDirectory(getTemporaryDirectory() + "信息公开");
		FileUtils.copyFile(Environment.getWebinfPath() + "cms/infopublic/template/信息公开目录.dbf", path + "/信息公开目录.dbf", true, true);
		String url = "jdbc:odbc:DRIVER={Microsoft dBase Driver (*.dbf)};DBQ=" + new File(path).getAbsolutePath() + ";";    
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  
			connection = DriverManager.getConnection(url, "", "");
			statement = connection.createStatement();
		}
		catch(Exception e) {
			Logger.exception(e);
			try {
				statement.close();
			}
			catch(Exception ex) {

			}
			try {
				connection.close();
			}
			catch(Exception ex) {

			}
			throw new ServiceException();
		}
		//导出正文
		String hql = "from PublicInfo PublicInfo" +
					 " where PublicInfo.id in (" + JdbcUtils.validateInClauseNumbers(ids) + ")" +
					 " order by PublicInfo.generateDate";
		for(int i=0; ; i+=100) {
			List infos = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("lazyBody,subjections", ","), i, 100); //每次获取100条
			if(infos==null || infos.isEmpty()) {
				break;
			}
			for(Iterator iterator = infos.iterator(); iterator.hasNext();) {
				PublicInfo info = (PublicInfo)iterator.next();
				//插入目录,格式:索引号,名称 ,发布机构,内容概述,生成日期,备注_文号,所属目录
				try {
					String sql = "insert into 信息公开目录 values(" + 
								 "'" + info.getInfoIndex() + "'," +
								 "'" + info.getSubject().replaceAll("[\\x0d\\x0a\\x27\\x22\\x3a]", "") + "'," +
								 "'" + info.getInfoFrom() + "'," +
								 "'" + (info.getSummarize()==null ? "" : info.getSummarize().replaceAll("[\\x0d\\x0a\\x27\\x22\\x3a]", "")) + "'," +
								 "'" + DateTimeUtils.formatDate(info.getGenerateDate(), null) +  "'," +
								 "'" + info.getMark() + "'," +
								 "'" + publicDirectoryService.getDirectoryFullName(((PublicInfoSubjection)info.getSubjections().iterator().next()).getDirectoryId(), "/", "main") + "')";
					statement.executeUpdate(sql);
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				//String infoPath = FileUtils.createDirectory(path, info.getInfoIndex());
				//输出正文,PDF格式
				writeInfoToPdfFile(info, path + "/" + info.getInfoIndex() + "_" + info.getSubject().replaceAll("[\\x0d\\x0a\\x27\\x22\\x3a\\x09]", "") + ".pdf");
				/**
				//获取附件列表
				List attachments = attachmentService.listAttachments(APPLICATION_NAME,  "attachments", info.getId(), null);
				if(attachments!=null && !attachments.isEmpty()) {
					for(Iterator iteratorAttachment=attachments.iterator(); iteratorAttachment.hasNext();) {
						Attachment attachment = (Attachment)iteratorAttachment.next();
						FileUtils.copyFile(attachment.getFilePath(), infoPath + "/" + attachment.getName(), false, true);
					}
				}
				*/
			}
		}
		try {
			statement.close();
		}
		catch(Exception ex) {

		}
		try {
			connection.close();
		}
		catch(Exception ex) {

		}
		String zipFileName = getTemporaryDirectory() + "/信息公开.zip";
		try {
			ZipUtils.zip(zipFileName, path);
			fileDownloadService.httpDownload(request, response, zipFileName, null, true, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			FileUtils.deleteFile(zipFileName);
			FileUtils.deleteDirectory(path);
		}
	}
	
	/**
	 * 把政务信息写到pdf文件中
	 * @param info
	 * @param filePath
	 * @throws ServiceException
	 */
	private void writeInfoToPdfFile(PublicInfo info, String filePath) throws ServiceException {
		try {
			//创建字体:宋体
			//BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			BaseFont bf = BaseFont.createFont(Environment.getWebinfPath() + "jeaf/fonts/宋体.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); 
			Font font = new Font(bf, 12, Font.NORMAL); //当前小四,9为五号
			//创建PDF
			Document document = new Document();
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));
			
			//打开PDF文档,添加内容
			document.open();
			//名称
			Paragraph paragraph = new Paragraph(info.getSubject(), new Font(bf, 18, Font.BOLD));
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setLeading(30);
			document.add(paragraph);
			
			//索引号,发布机构
			paragraph = new Paragraph("索引号:" + info.getInfoIndex() + "  发布机构:" + info.getInfoFrom(), font);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setLeading(20);
			document.add(paragraph);

			//生成日期,备注/文号
			paragraph = new Paragraph("生成日期:" + DateTimeUtils.formatDate(info.getGenerateDate(), null) + "   备注/文号:" + info.getMark(), font);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setLeading(20);
			document.add(paragraph);
			
			//概述
			/*paragraph = new Paragraph("概述:" + info.getSummarize(), font);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setLeading(20);
			document.add(paragraph);*/
			
			//正文
			HTMLDocument bodyDocument = htmlParser.parseHTMLString(info.getBody(), "utf-8");
			paragraph = new Paragraph(htmlParser.getTextContent(bodyDocument), font);
			paragraph.setAlignment(Element.ALIGN_LEFT);
			paragraph.setLeading(20);
			document.add(paragraph);
			
			//输出附件
			List attachments = attachmentService.list("cms/infopublic",  "attachments", info.getId(), false, 0, null);
			if(attachments!=null && !attachments.isEmpty()) {
				int left = 10;
				for(Iterator iteratorAttachment=attachments.iterator(); iteratorAttachment.hasNext();) {
					Attachment attachment = (Attachment)iteratorAttachment.next();
					PdfFileSpecification pf = PdfFileSpecification.fileEmbedded(pdfWriter, attachment.getFilePath(), attachment.getName(), null);
					PdfAnnotation annotation = PdfAnnotation.createFileAttachment(pdfWriter, new Rectangle(left, 10, left + 10, 20), attachment.getName(), pf);
					pdfWriter.addAnnotation(annotation);
					left += 30;
				}
			}
			document.close();
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 添加到网站栏目
	 * @param info
	 * @throws ServiceException
	 */
	private void addArticle(PublicInfo info) throws ServiceException {
		if(info.getIssueSite()!='1' || info.getStatus()!=PublicInfoService.INFO_STATUS_ISSUE) {
			return;
		}
		//获取目录
		String issueSiteIds = info.getIssueSiteIds();
		if(issueSiteIds==null || "".equals(issueSiteIds)) {
			siteResourceService.deleteResourceBySourceRecordId("" + info.getId());
			return;
		}
		siteResourceService.addResource(issueSiteIds,
										SiteResourceService.RESOURCE_TYPE_ARTICLE,
										info.getSubject(), //标题
										info.getMark(), //副标题
										info.getInfoFrom(), //来源
										null, //作者
										null, //关键字
										info.getMark(),
										SiteResourceService.ANONYMOUS_LEVEL_ALL,
										null,
										info.getCreated(), //创建时间
										info.getIssueTime(), //发布时间
										true, //设置为已发布
										info.getBody(),
										"" + info.getId(),
										info.getClass().getName(),
										Environment.getContextPath() + "/cms/infopublic/admin/publicInfo.shtml?id=" + info.getId(),
										info.getCreatorId(),
										info.getCreator(),
										info.getOrgId(),
										info.getOrgName(),
										info.getUnitId(),
										info.getUnitName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#updateInfoSubjections(com.yuanluesoft.cms.infopublic.pojo.PublicInfo, boolean, java.lang.String)
	 */
	public void updateInfoSubjections(PublicInfo info, boolean isNew, String subjectionDirectoryIds) throws ServiceException {
		if(subjectionDirectoryIds==null || subjectionDirectoryIds.equals("")) {
			return;
		}
		String[] ids = subjectionDirectoryIds.split(",");
		long firstDirectoryId = Long.parseLong(ids[0]);
		boolean firstDirectoryChanged = true;
		String oldSubjectionDirectoryIds = null; //旧的目录隶属关系
		if(!isNew) {
			oldSubjectionDirectoryIds = ListUtils.join(info.getSubjections(), "directoryId", ",", false);
			//检查隶属栏目是否发生变化
			if(subjectionDirectoryIds.equals(oldSubjectionDirectoryIds)) {
				return;
			}
			firstDirectoryChanged = info.getSubjections()==null || info.getSubjections().isEmpty() || (firstDirectoryId!=((PublicInfoSubjection)info.getSubjections().iterator().next()).getDirectoryId());
			//删除旧的隶属关系
			for(Iterator iterator = info.getSubjections()==null ? null : info.getSubjections().iterator(); iterator!=null && iterator.hasNext();) {
				PublicInfoSubjection subjection = (PublicInfoSubjection)iterator.next();
				getDatabaseService().deleteRecord(subjection);
				//生成静态页面
				if(info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE && //已经发布
				   ("," + subjectionDirectoryIds + ",").indexOf("," + subjection.getDirectoryId() + ",")==-1) { //已经被删除了
					pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
				}
			}
		}
		//保存所属栏目列表
		info.setSubjections(new HashSet());
		for(int i=0; i<ids.length; i++) {
			if(ListUtils.findObjectByProperty(info.getSubjections(), "directoryId", new Long(ids[i]))!=null) { //重复
				continue;
			}
			PublicInfoSubjection subjection = new PublicInfoSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setInfoId(info.getId());
			subjection.setDirectoryId(Long.parseLong(ids[i]));
			getDatabaseService().saveRecord(subjection);
			info.getSubjections().add(subjection);
			//生成静态页面
			if(!firstDirectoryChanged && info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE && //已经发布
			   ("," + oldSubjectionDirectoryIds + ",").indexOf("," + subjection.getDirectoryId() + ",")==-1) { //新增的
				pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
		}
		generateIndexIfNecessary(info); //当信息处于发布状态且索引号为空时，自动生成索引号
		if(firstDirectoryChanged) {
			try {
				info.setDirectoryName(publicDirectoryService.getDirectory(firstDirectoryId).getDirectoryName()); //设置所在栏目名称
				update(info);
			}
			catch(Exception e) {
				
			}
		}
		else {
			//同步更新
			exchangeClient.synchUpdate(info, null, 2000);
		}
	}
	
	/**
	 * 获取索引号
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	private synchronized void generateInfoIndex(PublicInfo info, long directoryId, String currentIndex) throws ServiceException {
		if(info.getType()!=INFO_TYPE_NORMAL) {
			return;
		}
		List parentDirectories = publicDirectoryService.listParentDirectories(directoryId, "main");
		parentDirectories.add(publicDirectoryService.getDirectory(directoryId));
		String prefix = null;
		String unitCode = null;
		PublicMainDirectory mainDirectory = null;
		for(int i=parentDirectories.size() - 1; i>=0; i--) {
			PublicDirectory publicDirectory = (PublicDirectory)parentDirectories.get(i);
			if(publicDirectory.getDirectoryType().equals("main")) { //主目录
				if(((PublicMainDirectory)publicDirectory).getManualCodeEnabled()=='1' && info.getInfoIndex()!=null && !info.getInfoIndex().trim().equals("")) {
					//允许手工编号,且编号不为空
					String[] values = info.getInfoIndex().split("-");
					try {
						info.setInfoYear(Integer.parseInt(values[values.length-2]));
					}
					catch(Exception e) {
						info.setInfoYear(DateTimeUtils.getYear(info.getGenerateDate()));
					}
					try {
						info.setInfoSequence(Integer.parseInt(values[values.length-1]));
					}
					catch(Exception e) {
						
					}
					return;
				}
				//按发布机构ID来获取机构代码
				mainDirectory = (PublicMainDirectory)publicDirectoryService.getDirectory(publicDirectory.getId());
				PublicUnitCode publicUnitCode = (PublicUnitCode)ListUtils.findObjectByProperty(mainDirectory.getUnitCodes(), "unitId", new Long(info.getInfoFromUnitId()));
				if(publicUnitCode!=null) { //有直接为当前发布机构配置的代码
					unitCode = publicUnitCode.getCode();
				}
				else {
					//获取父组织ID列表
					String parentOrgIds = orgService.listParentOrgIds(info.getInfoFromUnitId());
					if(parentOrgIds!=null && !parentOrgIds.isEmpty()) {
						String[] ids = parentOrgIds.split(",");
						for(int j=0; j<ids.length; j++) {
							publicUnitCode = (PublicUnitCode)ListUtils.findObjectByProperty(mainDirectory.getUnitCodes(), "unitId", new Long(ids[j]));
							if(publicUnitCode!=null) {
								unitCode = publicUnitCode.getCode();
								break;
							}
						}
					}
				}
				prefix = unitCode + "-";
				String directoryCode = null;
				for(i++; i<parentDirectories.size(); i++) {
					publicDirectory = (PublicDirectory)parentDirectories.get(i);
					directoryCode = (directoryCode==null ? "" : directoryCode) + publicDirectory.getCode();
				}
				if(directoryCode==null) {
					directoryCode = "0000";
				}
				else if(directoryCode.length()<4) {
					directoryCode += "0000".substring(0, 4 - directoryCode.length());
				}
				else {
					directoryCode = directoryCode.substring(directoryCode.length()-4);
				}
				prefix += directoryCode;
				break;
			}
		}
		int year = indexByIssueTime ? DateTimeUtils.getYear(info.getIssueTime()==null ? DateTimeUtils.now() : info.getIssueTime()) : DateTimeUtils.getYear(info.getGenerateDate()==null ? DateTimeUtils.date() : info.getGenerateDate());
		if(currentIndex!=null && currentIndex.startsWith(prefix + "-" + year)) { //当前索引号不为空,且当前索引号和目录、时间匹配
			return;
		}
		String category = mainDirectory.getSequenceByDirectory()==1 ? prefix : unitCode;
		String hql = "from PublicInfoSequence PublicInfoSequence" +
					 " where PublicInfoSequence.year=" + year +
					 " and PublicInfoSequence.category" + (category==null || category.isEmpty() ? " is null" : "='" + JdbcUtils.resetQuot(category) + "'");
		PublicInfoSequence sequence = (PublicInfoSequence)getDatabaseService().findRecordByHql(hql);
		int sn = 1;
		if(sequence==null) {
			sequence = new PublicInfoSequence();
			sequence.setId(UUIDLongGenerator.generateId());
			sequence.setYear(year);
			sequence.setCategory(category);
			sequence.setSequence(sn);
			getDatabaseService().saveRecord(sequence);
		}
		else {
			if(year==sequence.getYear()) {
				sn = sequence.getSequence() + 1;
			}
			sequence.setSequence(sn);
			getDatabaseService().updateRecord(sequence);
		}
		DecimalFormat formatter = new DecimalFormat("00000");
		info.setInfoIndex(prefix + "-" + year + "-" + formatter.format(sn));
		info.setInfoYear(year);
		info.setInfoSequence(sn);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#stat(java.sql.Date, java.sql.Date, long)
	 */
	public List stat(Date beginDate, Date endDate, long directoryId) throws ServiceException {
		List stats = new ArrayList();
		String childDirectoryIds = publicDirectoryService.getChildDirectoryIds(directoryId + "", "category,info");
		InfoStat stat = new InfoStat();
		stats.add(stat);
		stat.setName("公开信息数");
		int total = infoTotal(childDirectoryIds, beginDate, endDate, null);
		stat.setCount(total);
		stat.setPercent(-1);
		if(total==0) {
			return stats;
		}
		//获取一级目录
		List directories = publicDirectoryService.listChildDirectories(directoryId, "category,info", null, null, false, false, null, 0, 0);
		if(directories==null) {
			return stats;
		}
		for(Iterator iterator = directories.iterator(); iterator.hasNext();) {
			PublicDirectory directory = (PublicDirectory)iterator.next();
			stat = new InfoStat();
			stats.add(stat);
			stat.setName(directory.getDirectoryName());
			stat.setCount(infoTotal(directory.getId() + "", beginDate, endDate, null));
			stat.setPercent((stat.getCount() + 0.0f)/total);
		}
		return stats;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#statByCategory(java.sql.Date, java.sql.Date, long)
	 */
	public List statByCategory(Date beginDate, Date endDate, long directoryId) throws ServiceException {
		if(endDate==null) {
			endDate = DateTimeUtils.date();
		}
		List stats = new ArrayList();
		Date seasonBegin = DateTimeUtils.set(endDate==null ? DateTimeUtils.date() : endDate, Calendar.DAY_OF_MONTH, 1);
		int month = DateTimeUtils.getMonth(seasonBegin);
		seasonBegin = DateTimeUtils.set(seasonBegin, Calendar.MONTH, month-month%3);
		Date seasonEnd = DateTimeUtils.add(DateTimeUtils.add(seasonBegin, Calendar.MONTH, 3), Calendar.DAY_OF_MONTH, -1);
		Date previousSeasonBegin = DateTimeUtils.add(seasonBegin, Calendar.MONTH, -3);
		Date previousSeasonEnd = DateTimeUtils.add(seasonBegin, Calendar.DAY_OF_MONTH, -1);
		String childDirectoryIds = publicDirectoryService.getChildDirectoryIds(directoryId + "", "category,info");
		InfoCategoryStat stat = new InfoCategoryStat();
		stats.add(stat);
		stat.setName("全部电子化的主动公开信息数");
		Date yearBegin = DateTimeUtils.set(DateTimeUtils.set(endDate, Calendar.MONTH, 0), Calendar.DAY_OF_MONTH, 1);
		Date yearEnd = endDate; //DateTimeUtils.add(DateTimeUtils.add(yearBegin, Calendar.YEAR, 1), Calendar.DAY_OF_MONTH, -1);
		int total = infoTotal(childDirectoryIds, yearBegin, yearEnd, null);
		stat.setYearTotal(total);
		if(total==0) {
			return null;
		}
		stat.setCurrentSeasonTotal(infoTotal(childDirectoryIds, seasonBegin, seasonEnd, null));
		stat.setPreviousSeasonTotal(infoTotal(childDirectoryIds, previousSeasonBegin, previousSeasonEnd, null));
		
		//获取分类列表 
		boolean categorySupport = false;
		List categories = FieldUtils.listSelectItems(FieldUtils.getRecordField(PublicInfo.class.getName(), "category", null), null, null);
		for(Iterator iterator = categories.iterator(); iterator.hasNext();) {
			String[] values = (String[])iterator.next();
			stat = new InfoCategoryStat();
			stats.add(stat);
			stat.setName(values[1]);
			total = infoTotal(childDirectoryIds, yearBegin, yearEnd, values[1]);
			stat.setYearTotal(total);
			if(total>0) {
				stat.setCurrentSeasonTotal(infoTotal(childDirectoryIds, seasonBegin, seasonEnd, values[1]));
				stat.setPreviousSeasonTotal(infoTotal(childDirectoryIds, previousSeasonBegin, previousSeasonEnd, values[1]));
				categorySupport = true;
			}
		}
		return categorySupport ? stats : null;
	}
	
	/**
	 * 信息统计
	 * @param parentDirectoryIds
	 * @param beginDate
	 * @param endDate
	 * @param category
	 * @return
	 * @throws ServiceException
	 */
	private int infoTotal(String parentDirectoryIds, Date beginDate, Date endDate, String category) throws ServiceException {
		String hql =  "select count(PublicInfo.id)" +
			 		  " from PublicInfo PublicInfo left join PublicInfo.subjections PublicInfoSubjection, PublicDirectorySubjection PublicDirectorySubjection" +
			 		  " where PublicInfo.status='" + PublicInfoService.INFO_STATUS_ISSUE + "'" +
			 		  (beginDate==null ? "" : " and PublicInfo.issueTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")") +
			 		  (endDate==null ? "" : " and PublicInfo.issueTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")") +
			 		  " and PublicInfoSubjection.directoryId=PublicDirectorySubjection.directoryId" +
			 		  " and PublicDirectorySubjection.parentDirectoryId in (" +  JdbcUtils.validateInClauseNumbers(parentDirectoryIds) + ")" +
			 		  (category!=null ? " and PublicInfo.category='" + JdbcUtils.resetQuot(category) + "'" : "");
		Number count = (Number)getDatabaseService().findRecordByHql(hql);
		return count==null ? 0 : count.intValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsContentService#listSmsContentDefinitions()
	 */
	public List listSmsContentDefinitions() throws ServiceException {
		List contentDefinitions = new ArrayList();
		contentDefinitions.add(new SmsContentDefinition("信息公开", null, SmsContentService.SEND_MODE_NEWS, null));
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
		//从订阅参数中获取目录ID
		String directoryId = StringUtils.getPropertyValue(subscribeParameter, "directoryId");
		if(directoryId==null || directoryId.isEmpty()) {
			return contentName;
		}
		//获取目录名称
		String directryName = publicDirectoryService.getDirectoryFullName(Long.parseLong(directoryId), "/", "main");
		return directryName==null ? contentName : contentName + "(" + directryName + ")";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#getInfosCount(java.lang.String, boolean)
	 */
	public int getInfosCount(String directoryIds, boolean issuedOnly) throws ServiceException {
		directoryIds = publicDirectoryService.getChildDirectoryIds(directoryIds, "category,info");
		if(directoryIds==null || directoryIds.isEmpty()) {
			return 0;
		}
		String hql = "select count(distinct PublicInfo.id)" +
					  " from PublicInfo PublicInfo left join PublicInfo.subjections PublicInfoSubjection, PublicDirectorySubjection PublicDirectorySubjection" +
					  " where PublicInfoSubjection.directoryId=PublicDirectorySubjection.directoryId" +
					  " and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
					  (issuedOnly ? " and  PublicInfo.status='" + PublicInfoService.INFO_STATUS_ISSUE + "'" : "");
		Object count = getDatabaseService().findRecordByHql(hql);
		return count==null ? 0 : ((Number)count).intValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#listInfos(java.lang.String, boolean, boolean, boolean, int, int)
	 */
	public List listInfos(String directoryIds, boolean issuedOnly, boolean containsChildDirectory, boolean sortByGenerateDate, int offset, int max) throws ServiceException {
		if(!containsChildDirectory) { //不包括子目录
			String hql = "select distinct PublicInfo" +
						  " from PublicInfo PublicInfo left join PublicInfo.subjections PublicInfoSubjection" +
						  " where PublicInfoSubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
						  (issuedOnly ? " and  PublicInfo.status='" + PublicInfoService.INFO_STATUS_ISSUE + "'" : "") +
						  " order by " + (sortByGenerateDate ? "PublicInfo.generateDate" : "PublicInfo.subject");
			return getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections,lazyBody", ","), offset, max);
		}
		directoryIds = publicDirectoryService.getChildDirectoryIds(directoryIds, "category,info");
		if(directoryIds==null || directoryIds.isEmpty()) {
			return null;
		}
		String hql = "select distinct PublicInfo" +
					  " from PublicInfo PublicInfo left join PublicInfo.subjections PublicInfoSubjection, PublicDirectorySubjection PublicDirectorySubjection" +
					  " where PublicInfoSubjection.directoryId=PublicDirectorySubjection.directoryId" +
					  " and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
					  (issuedOnly ? " and  PublicInfo.status='" + PublicInfoService.INFO_STATUS_ISSUE + "'" : "") +
					  " order by " + (sortByGenerateDate ? "PublicInfo.generateDate" : "PublicInfo.subject");
		return getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections,lazyBody", ","), offset, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#writeGuangzeInfoReport(java.sql.Date, java.sql.Date, javax.servlet.http.HttpServletResponse)
	 */
	public void writeInfoReport(Date beginDate, Date endDate, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//创建临时文件目录
		String temporaryDirectory = temporaryFileManageService.createTemporaryDirectory(null);
		//县政府网站ID
		//long mainDirectoryId = ((Number)getDatabaseService().findObjectByHql("select PublicDirectory.id from PublicDirectory PublicDirectory where PublicDirectory.directoryType='main' and PublicDirectory.directoryName like '%县政府%'")).longValue();
		long mainSiteId = ((Number)getDatabaseService().findRecordByHql("select WebDirectory.id from WebDirectory WebDirectory where WebDirectory.directoryType='site' and WebDirectory.directoryName like '%县政府%'")).longValue();
		//输出乡镇报表
		jxl.write.WritableWorkbook wwb = null;
		jxl.Workbook rw = null;
		try {
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "cms/infopublic/template/光泽网站栏目保障考评表(乡镇).xls"));

			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(new File(temporaryDirectory + "光泽网站栏目保障考评表(乡镇).xls"), rw);
			WritableSheet ws = wwb.getSheet(0);
			for(int row=5; row<200; row+=2) {
				String unitName = ws.getCell(0, row).getContents(); //获取单位名称
				if(unitName==null || unitName.equals("")) {
					break;
				}
				try {
					writeGemeindeInfoReport(unitName, mainSiteId, beginDate, endDate, ws, row);
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
			wwb.write();
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			//关闭Excel工作薄对象
			try {
				wwb.close();
			}
			catch(Exception e) {
				
			}
			//关闭只读的Excel对象
			rw.close();
		}
		
		//输出各部门报告
		List units = getDatabaseService().findRecordsByHql("from Unit Unit");
		for(Iterator iterator = units.iterator(); iterator.hasNext();) {
			Unit unit = (Unit)iterator.next();
			try {
				//读入报表模板
				rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "cms/infopublic/template/光泽部门网站保障考评.xls"));
				//创建可写入的Excel工作薄对象
				wwb = Workbook.createWorkbook(new File(temporaryDirectory + unit.getDirectoryName() + ".xls"), rw);
				WritableSheet ws = wwb.getSheet(0);
				//统计单位信息量
				writeUnitReport(unit, mainSiteId, beginDate, endDate, ws);
				wwb.write();
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new ServiceException();
			}
			finally {
				//关闭Excel工作薄对象
				try {
					wwb.close();
				}
				catch(Exception e) {
					
				}
				//关闭只读的Excel对象
				rw.close();
			}
		}
		
		//创建临时压缩文件目录
		String zipFileName = temporaryFileManageService.createTemporaryDirectory(null) + "光泽网站栏目保障考评表.zip";
		try {
			ZipUtils.zip(zipFileName, temporaryDirectory);
			fileDownloadService.httpDownload(request, response, zipFileName, null, true, null);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 输出乡镇统计数据
	 * @param unitName
	 * @param mainSiteId
	 * @param beginDate
	 * @param endDate
	 * @param ws
	 * @param row
	 * @throws Exception
	 */
	private void writeGemeindeInfoReport(String unitName, long mainSiteId, Date beginDate, Date endDate, WritableSheet ws, int row) throws Exception {
		final String mainDirectoryName = "县政府"; //主目录关键字
		//获取主目录ID
		long unitDirectoryId = ((Number)getDatabaseService().findRecordByHql("select PublicDirectory.id from PublicDirectory PublicDirectory where PublicDirectory.directoryType='main' and PublicDirectory.directoryName like '%" + JdbcUtils.resetQuot(unitName) + "%'")).longValue();
		//获取单位ID
		long unitId = ((Number)getDatabaseService().findRecordByHql("select Org.id from Org Org where Org.directoryType='unit' and Org.directoryName like '%" + JdbcUtils.resetQuot(unitName) + "%'")).longValue();
		boolean unitInfo = true; //是否单位自己的信息
		for(int col=2; col<200; col++) {
			if("合计".equals(ws.getCell(col, 1).getContents())) {
				break;
			}
			String value = ws.getCell(col, 2).getContents();
			if(value!=null && value.indexOf(mainDirectoryName)!=-1) {
				unitInfo = false;
			}
			value = ws.getCell(col, 4).getContents();
			if(value==null || value.equals("")) {
				value = ws.getCell(col, 3).getContents();
			}
			//解析目录名称、总分值、当条分值, 如：深入学习实践科学发展观(1.5分，每条0.3分)
			String[] values = value.split("\\x28");
			String directoryName = values[0];
			values = values[1].split("分，每条");
			double totalScore = Double.parseDouble(values[0]);
			double score = Double.parseDouble(values[1].substring(0, values[1].indexOf("分")));
			
			Number count = null;
			if(unitInfo) { //单位自己的信息
				//获取目录
				String hql = "select PublicDirectory.id" +
							 " from PublicDirectory PublicDirectory, PublicDirectorySubjection PublicDirectorySubjection" +
							 " where PublicDirectorySubjection.directoryId=PublicDirectory.id" +
							 " and PublicDirectorySubjection.parentDirectoryId=" + (unitInfo ? unitDirectoryId : mainSiteId) +
							 " and PublicDirectorySubjection.directoryId!=" + (unitInfo ? unitDirectoryId : mainSiteId) +
							 " and PublicDirectory.directoryName='" + JdbcUtils.resetQuot(directoryName) + "'";
				Number directoryId = (Number)getDatabaseService().findRecordByHql(hql);
				if(directoryId!=null) {
					//统计信息数量
					hql = "select count(PublicInfo.id)" +
						  " from PublicInfo PublicInfo, PublicInfoSubjection PublicInfoSubjection, PublicDirectorySubjection PublicDirectorySubjection" +
						  " where PublicInfoSubjection.infoId=PublicInfo.id" +
						  " and PublicInfoSubjection.directoryId=PublicDirectorySubjection.directoryId" +
						  " and PublicDirectorySubjection.parentDirectoryId=" + directoryId  +
						  (unitInfo ? "" : " and PublicInfo.unitId=" + unitId) +
						  (beginDate==null ? "" : " and PublicInfo.generateDate>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")") +
						  (endDate==null ? "" : " and PublicInfo.generateDate<=DATE(" + DateTimeUtils.formatDate(endDate, null) + ")");
					count = (Number)getDatabaseService().findRecordByHql(hql);
				}
			}
			else { //县政府网站
				//获取栏目
				String hql = "select WebDirectory.id" +
							 " from WebDirectory WebDirectory, WebDirectorySubjection WebDirectorySubjection" +
							 " where WebDirectorySubjection.directoryId=WebDirectory.id" +
							 " and WebDirectorySubjection.parentDirectoryId=" + (unitInfo ? unitDirectoryId : mainSiteId) +
							 " and WebDirectorySubjection.directoryId!=" + (unitInfo ? unitDirectoryId : mainSiteId) +
							 " and WebDirectory.directoryName='" + JdbcUtils.resetQuot(directoryName) + "'";
				Number directoryId = (Number)getDatabaseService().findRecordByHql(hql);
				if(directoryId!=null) {
					//统计文章数量
					hql = "select count(SiteResource.id)" +
						  " from SiteResource SiteResource, SiteResourceSubjection SiteResourceSubjection, WebDirectorySubjection WebDirectorySubjection" +
						  " where SiteResourceSubjection.resourceId=SiteResource.id" +
						  " and SiteResourceSubjection.siteId=WebDirectorySubjection.directoryId" +
						  " and WebDirectorySubjection.parentDirectoryId=" + directoryId +
						  (unitInfo ? "" : " and SiteResource.unitId=" + unitId) +
						  (beginDate==null ? "" : " and SiteResource.issueTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")") +
						  (endDate==null ? "" : " and SiteResource.issueTime<=DATE(" + DateTimeUtils.formatDate(endDate, null) + ")");
					count = (Number)getDatabaseService().findRecordByHql(hql);
				}
			}
			//写到电子表格
			jxl.write.Number labelNumber = new jxl.write.Number(col, row, (count==null ? 0 : count.intValue()), ws.getCell(col, row).getCellFormat());
			ws.addCell(labelNumber);
			
			labelNumber = new jxl.write.Number(col, row+1, (count==null ? 0 : Math.min(score * count.intValue(), totalScore)), ws.getCell(col, row+1).getCellFormat());
			ws.addCell(labelNumber);
		}
	}
	
	/**
	 * 输出单位报表
	 * @param unit
	 * @param mainSiteId
	 * @param beginDate
	 * @param endDate
	 * @param ws
	 * @throws Exception
	 */
	private void writeUnitReport(Unit unit, long mainSiteId, Date beginDate, Date endDate, WritableSheet ws) throws Exception {
		//输出单位名称
		jxl.write.Label label = new jxl.write.Label(0, 0, unit.getDirectoryName(), ws.getCell(0, 0).getCellFormat());
		ws.addCell(label);

		//获取信息公开主目录ID
		Number unitDirectoryId = (Number)getDatabaseService().findRecordByHql("select PublicDirectory.id from PublicDirectory PublicDirectory where PublicDirectory.directoryType='main' and PublicDirectory.directoryName like '%" + JdbcUtils.resetQuot(unit.getDirectoryName()) + "%'");
		int rowIndex = 3;
		if(unitDirectoryId!=null) {
			//输出信息公开统计
			rowIndex = writeUnitInfoReport(beginDate, endDate, ws, unitDirectoryId.longValue(), null, 3);
			ws.removeRow(2); //删除样本行
			rowIndex--;
		}
		//统计县政府栏目,直到“公众留言”
		for(; rowIndex<2000; rowIndex++) {
			String columnName = ws.getCell(0, rowIndex).getContents();
			if("公众留言".equals(columnName)) {
				break;
			}
			//获取栏目
			String hql = "select WebDirectory.id" +
						 " from WebDirectory WebDirectory, WebDirectorySubjection WebDirectorySubjection" +
						 " where WebDirectorySubjection.directoryId=WebDirectory.id" +
						 " and WebDirectorySubjection.parentDirectoryId=" + mainSiteId +
						 " and WebDirectory.directoryName='" + JdbcUtils.resetQuot(columnName) + "'";
			Number directoryId = (Number)getDatabaseService().findRecordByHql(hql);
			Number count = null;
			if(directoryId!=null) {
				//统计文章数量
				hql = "select count(SiteResource.id)" +
					  " from SiteResource SiteResource, SiteResourceSubjection SiteResourceSubjection" +
					  " where SiteResourceSubjection.resourceId=SiteResource.id" +
					  " and (SiteResourceSubjection.siteId=" + directoryId +
					  " or SiteResourceSubjection.siteId in (" +
					  "   select WebDirectorySubjection.directoryId" +
					  "    from WebDirectorySubjection WebDirectorySubjection" +
					  "    where WebDirectorySubjection.parentDirectoryId=" + directoryId + "))" +
					  " and SiteResource.unitId=" + unit.getId() +
					  (beginDate==null ? "" : " and SiteResource.issueTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")") +
					  (endDate==null ? "" : " and SiteResource.issueTime<=DATE(" + DateTimeUtils.formatDate(endDate, null) + ")");
				count = (Number)getDatabaseService().findRecordByHql(hql);
			}
			//写到电子表格
			jxl.write.Number labelNumber = new jxl.write.Number(1, rowIndex, (count==null ? 0 : count.intValue()), ws.getCell(1, rowIndex).getCellFormat());
			ws.addCell(labelNumber);
		}
	}
	
	/**
	 * 递归函数:输出政府信息数量
	 * @param beginDate
	 * @param endDate
	 * @param ws
	 * @param parentInfoDirectoryId
	 * @param parentInfoDirectoryName
	 * @param rowIndex
	 * @return
	 * @throws Exception
	 */
	private int writeUnitInfoReport(Date beginDate, Date endDate, WritableSheet ws, long parentInfoDirectoryId, String parentInfoDirectoryName, int rowIndex) throws Exception {
		//获取下级目录
		List childDirectories = getDatabaseService().findRecordsByHql("from PublicDirectory PublicDirectory where PublicDirectory.parentDirectoryId=" + parentInfoDirectoryId + " order by PublicDirectory.priority DESC, PublicDirectory.directoryName");
		if(childDirectories==null || childDirectories.isEmpty()) {
			return rowIndex;
		}
		for(Iterator iterator = childDirectories.iterator(); iterator.hasNext();) {
			PublicDirectory publicDirectory = (PublicDirectory)iterator.next();
			//统计信息数量
			String hql = "select count(PublicInfo.id)" +
				  " from PublicInfo PublicInfo, PublicInfoSubjection PublicInfoSubjection" +
				  " where PublicInfoSubjection.infoId=PublicInfo.id" +
				  " and PublicInfoSubjection.directoryId=" + publicDirectory.getId() +
				  (beginDate==null ? "" : " and PublicInfo.generateDate>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")") +
				  (endDate==null ? "" : " and PublicInfo.generateDate<=DATE(" + DateTimeUtils.formatDate(endDate, null) + ")");
			Number count = (Number)getDatabaseService().findRecordByHql(hql);
			
			//添加行
			ws.insertRow(rowIndex);
			//写到电子表格
			String infoDirectoryName = (parentInfoDirectoryName==null ? "" : parentInfoDirectoryName + "/") + publicDirectory.getDirectoryName();
			jxl.write.Label label = new jxl.write.Label(0, rowIndex, infoDirectoryName, ws.getCell(0, 2).getCellFormat());
			ws.addCell(label);
			jxl.write.Number labelNumber = new jxl.write.Number(1, rowIndex, (count==null ? 0 : count.intValue()), ws.getCell(1, 2).getCellFormat());
			ws.addCell(labelNumber);
			rowIndex++;
			
			//递归调用下一级
			rowIndex = writeUnitInfoReport(beginDate, endDate, ws, publicDirectory.getId(), infoDirectoryName, rowIndex);
		}
		return rowIndex;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureRecordService#processCapturedRecord(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask, com.yuanluesoft.cms.capture.model.CaptureRecord)
	 */
	public void processCapturedRecord(CmsCaptureTask captureTask, CapturedRecord capturedRecord) throws ServiceException {
		long directoryId = StringUtils.getPropertyLongValue(captureTask.getExtendedParameters(), "directoryId", 0);
		boolean issue = "true".equals(StringUtils.getPropertyValue(captureTask.getExtendedParameters(), "issue"));
		PublicInfo info = (PublicInfo)capturedRecord.getRecord();
		info.setSourceRecordUrl(capturedRecord.getUrl()); //抓取的URL
		info.setIssueSite('1'); //允许同步到网站
		saveInfo(info, directoryId, issue); //保存
	}
	
	/**
	 * 保存一个站点资源
	 * @param resource
	 * @param columnIds
	 * @param issue
	 * @throws ServiceException
	 */
	private void saveInfo(PublicInfo info, final long directoryId, boolean issue) throws ServiceException {
		if(directoryId<=0) {
			return;
		}
		if(info.getSubject()==null || info.getSubject().isEmpty()) {
			throw new ServiceException("subject is null");
		}
		if(info.getBody()==null || info.getBody().isEmpty()) {
			throw new ServiceException("body is null");
		}
		if(info.getCreated()==null) {
			info.setCreated(DateTimeUtils.now());
		}
		info.setStatus(PublicInfoService.INFO_STATUS_TODO);
		SessionInfo sessionInfo = null;
		if(issue) { //直接发布
			info.setIssuePersonId(100);
			save(info); //保存主记录
			updateInfoSubjections(info, true, directoryId + ""); //添加隶属目录
			//添加查看权限
			PublicInfoPrivilege privilege = new PublicInfoPrivilege();
			privilege.setId(UUIDLongGenerator.generateId());
			privilege.setAccessLevel(RecordControlService.ACCESS_LEVEL_READONLY);
			privilege.setRecordId(info.getId());
			privilege.setVisitorId(0);
			getDatabaseService().saveRecord(privilege);
			//发布
			issue(info, sessionInfo);
		}
		else { //不直接发布，随机指定一个作为编辑
			List editors = publicDirectoryService.listDirectoryEditors(directoryId, true, true, 10); //获取编辑列表,最多10个
			Person person = (Person)editors.get(0);
			try {
				sessionInfo = sessionService.getSessionInfo(person.getLoginName());
			}
			catch (SessionException e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
			info.setIssuePersonId(0);
			info.setCreatorId(sessionInfo.getUserId());
			info.setCreator(sessionInfo.getUserName());
			info.setOrgId(sessionInfo.getDepartmentId());
			info.setOrgName(sessionInfo.getDepartmentName());
			info.setUnitId(sessionInfo.getUnitId());
			info.setUnitName(sessionInfo.getUnitName());
			
			//创建流程实例
			long workflowId = publicDirectoryService.getApprovalWorkflowId(directoryId);
			if(workflowId<=0) {
				throw new ServiceException("流程未定义");
			}
			WorkflowEntry workflowEntry = workflowExploitService.getWorkflowEntry("" + workflowId, null, info, sessionInfo);
			if(workflowEntry==null) {
				throw new ServiceException("流程未定义");
			}
			//创建流程实例
			com.yuanluesoft.jeaf.base.model.Element activity = (com.yuanluesoft.jeaf.base.model.Element)workflowEntry.getActivityEntries().get(0);
			WorkflowParticipantCallback participantCallback = new WorkflowParticipantCallback() {
				public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					return "directoryEditor".equals(programmingParticipantId) ? publicDirectoryService.listDirectoryEditors(directoryId, false, false, 0) : publicDirectoryService.listDirectoryManagers(directoryId, false, false, 0);
				}
				public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					return participants;
				}
				public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					return false;
				}
			};
			try {
				String workflowInstanceId = workflowExploitService.createWorkflowInstance(workflowEntry.getWorkflowId(), activity.getId(), false, info, participantCallback, sessionInfo);
				info.setWorkflowInstanceId(workflowInstanceId); //流程实例ID
				//把其他编辑加为办理人
				if(editors.size()>1) {
					editors = editors.subList(1, editors.size());
					List workItems = workflowExploitService.listRunningWorkItems(workflowInstanceId, false, sessionInfo);
					WorkItem workItem = (WorkItem)workItems.get(0);
					workflowExploitService.lockWorkflowInstance(workflowInstanceId, sessionInfo);
					workflowExploitService.addParticipants(workflowInstanceId, workItem.getId(), false, ListUtils.join(editors, "id", ",", false), ListUtils.join(editors, "name", ",", false), null, info, sessionInfo);
					workflowExploitService.unlockWorkflowInstance(workflowInstanceId, sessionInfo);
				}
				//保存主记录
				save(info);
				//添加隶属站点
				updateInfoSubjections(info, true, directoryId + "");
			}
			catch(Exception e) {
				Logger.exception(e);
				if(info.getWorkflowInstanceId()!=null) {
					workflowExploitService.removeWorkflowInstance(info.getWorkflowInstanceId(), info, sessionInfo);
				}
				throw new ServiceException(e.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#writeMonitoringReport(long, java.sql.Date, java.sql.Date)
	 */
	public MonitoringReport writeMonitoringReport(long directoryId, Date beginDate, Date endDate) throws ServiceException {
		MonitoringReport monitoringReport = new MonitoringReport();
		String hqlPrefix = "select sum(DayAccessStat.times)" +
						   " from DayAccessStat DayAccessStat";
		String hqlDate = "DayAccessStat.accessDate>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
						 " and DayAccessStat.accessDate<=DATE(" + DateTimeUtils.formatDate(endDate, null) + ")";
		//政府信息公开指南
		String hql = "select PublicGuide.id from PublicGuide PublicGuide where PublicGuide.directoryId=" + directoryId;
		Number guideId = (Number)getDatabaseService().findRecordByHql(hql);
		if(guideId!=null) {
			hql = hqlPrefix +
				  " where DayAccessStat.applicationName='cms/infopublic'" +
				  " and DayAccessStat.pageName='guide'" +
				  " and DayAccessStat.recordId=" + guideId.longValue() +
				  " and " + hqlDate;
			Number visits = (Number)getDatabaseService().findRecordByHql(hql);
			monitoringReport.setGuideVisits(visits==null ? 0 : visits.intValue()); //政府信息公开指南
		}
		
		//政府信息公开目录
		String directoryIds = publicDirectoryService.getChildDirectoryIds("" + directoryId, "category,info");
		hql = hqlPrefix +
			  " where DayAccessStat.applicationName='cms/infopublic'" +
			  " and DayAccessStat.pageName='info'" +
			  " and DayAccessStat.recordId in (" +
			  "  select PublicInfoSubjection.infoId" +
			  "   from PublicInfoSubjection PublicInfoSubjection, PublicDirectorySubjection PublicDirectorySubjection" +
			  "   where PublicInfoSubjection.directoryId=PublicDirectorySubjection.directoryId" +
 			  "   and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")" +
 			  " )" +
			  " and " + hqlDate;
		Number visits = (Number)getDatabaseService().findRecordByHql(hql);
		monitoringReport.setPublicDirectoryVisits(visits==null ? 0 : visits.intValue()); //政府信息公开目录
		
		//依申请公开
		hql = hqlPrefix +
			  " where DayAccessStat.applicationName='cms/infopublic/request'" +
			  //" and DayAccessStat.pageName='index'" +
			  //" and DayAccessStat.recordId=" + mainDirectory.getId() +
			  " and " + hqlDate;
		visits = (Number)getDatabaseService().findRecordByHql(hql);
		monitoringReport.setPublicRequestVisits(visits==null ? 0 : visits.intValue()); //依申请公开

		//政府信息公开年度报告
		PublicDirectory articleDirectory = (PublicDirectory)publicDirectoryService.getDirectoryByName(directoryId, "信息公开年度报告", false);
		if(articleDirectory==null) {
			articleDirectory = (PublicDirectory)publicDirectoryService.getDirectoryByName(directoryId, "政府信息公开年度报告", false);
		}
		if(articleDirectory==null) {
			monitoringReport.setReportVisits(0);
		}
		else {
			hql = hqlPrefix +
				  " where DayAccessStat.applicationName='cms/infopublic'" +
				  " and DayAccessStat.pageName='article'" +
				  " and DayAccessStat.recordId in (" +
				  "  select PublicInfoSubjection.infoId from PublicInfoSubjection PublicInfoSubjection where directoryId=" + articleDirectory.getId()  +
				  " )" +
				  " and " + hqlDate;
			visits = (Number)getDatabaseService().findRecordByHql(hql);
			monitoringReport.setReportVisits(visits==null ? 0 : visits.intValue()); //政府信息公开年度报告
		}
		
		//政府信息公开制度规定
		articleDirectory = (PublicDirectory)publicDirectoryService.getDirectoryByName(directoryId, "信息公开制度", false);
		if(articleDirectory==null) {
			articleDirectory = (PublicDirectory)publicDirectoryService.getDirectoryByName(directoryId, "政府信息公开制度规定", false);
		}
		if(articleDirectory==null) {
			monitoringReport.setLawsVisits(0);
		}
		else {
			hql = hqlPrefix +
				  " where DayAccessStat.applicationName='cms/infopublic'" +
				  " and DayAccessStat.pageName='article'" +
				  " and DayAccessStat.recordId in (" +
				  "  select PublicInfoSubjection.infoId from PublicInfoSubjection PublicInfoSubjection where directoryId=" + articleDirectory.getId()  +
				  " )" +
				  " and " + hqlDate;
			visits = (Number)getDatabaseService().findRecordByHql(hql);
			monitoringReport.setLawsVisits(visits==null ? 0 : visits.intValue()); //政府信息公开年度报告
		}
		
		//政府信息公开意见箱
		hql = hqlPrefix +
			  " where DayAccessStat.applicationName='cms/infopublic/opinion'" +
			  //" and DayAccessStat.pageName='index'" +
			  //" and DayAccessStat.recordId=" + mainDirectory.getId() +
			  " and " + hqlDate;
		visits = (Number)getDatabaseService().findRecordByHql(hql);
		monitoringReport.setPublicOpinionVisits(visits==null ? 0 : visits.intValue()); //政府信息公开意见箱
		return monitoringReport;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#modifyReaders(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void modifyReaders(View view, String currentCategories, String searchConditions, String selectedIds, String modifyMode, boolean selectedOnly, boolean deleteNotDirectoryVisitor, String readerIds, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String[] userIds = null;
		if(!"synchDirectoryVisitor".equals(modifyMode)) { //不是同步目录访问者
			if(readerIds==null || readerIds.isEmpty()) { //用户ID为空
				return;
			}
			userIds = readerIds.split(",");
		}
		if(selectedOnly) { //选中的信息
			if(selectedIds==null || selectedIds.isEmpty()) {
				return;
			}
			List ids = ListUtils.generateList(selectedIds, ",");
			for(int i=0; i<ids.size(); i+=100) {
				String hql = "from PublicInfo PublicInfo" +
							 " where PublicInfo.id in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(ids.subList(i, Math.min(i+100, ids.size())), ",", false)) + ")";
				List resources = getDatabaseService().findRecordsByHql(hql, i, 100);
				doModifyReaders(resources, modifyMode, deleteNotDirectoryVisitor, userIds);
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
			catch(PrivilegeException e) {
				
			}
			//获取记录
			if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) {
				break;
			}
			//更新访问者
			doModifyReaders(viewPackage.getRecords(), modifyMode, deleteNotDirectoryVisitor, userIds);
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
	private void doModifyReaders(List resources, String modifyMode, boolean deleteNotDirectoryVisitor, String[] readerIds) throws ServiceException {
		for(Iterator iterator = resources.iterator(); iterator.hasNext(); ) {
			PublicInfo info = (PublicInfo)iterator.next();
			if("addUser".equals(modifyMode)) { //添加用户
				for(int i=0; i<readerIds.length; i++) {
					recordControlService.appendVisitor(info.getId(), PublicInfo.class.getName(), Long.parseLong(readerIds[i]), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
			else if("deleteUser".equals(modifyMode)) { //删除用户
				for(int i=0; i<readerIds.length; i++) {
					recordControlService.removeVisitor(info.getId(), PublicInfo.class.getName(), Long.parseLong(readerIds[i]), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
			else if("synchDirectoryVisitor".equals(modifyMode)) { //同步目录访问者
				//获取隶属栏目ID
				String hql = "select PublicInfoSubjection.directoryId" +
							 " from PublicInfoSubjection PublicInfoSubjection" +
							 " where PublicInfoSubjection.infoId=" + info.getId() +
							 " order by PublicInfoSubjection.id";
				long directoryId = ((Number)getDatabaseService().findRecordByHql(hql)).longValue();
				//获取访问者
				hql = "from PublicDirectoryPopedom PublicDirectoryPopedom where PublicDirectoryPopedom.directoryId=" + directoryId;
				List popedoms = getDatabaseService().findRecordsByHql(hql);
				if(popedoms==null || popedoms.isEmpty()) {
					continue;
				}
				if(deleteNotDirectoryVisitor) { //删除非栏目访问者
					recordControlService.removeVisitors(info.getId(), info.getClass().getName(), RecordControlService.ACCESS_LEVEL_READONLY);
				}
				for(Iterator iteratorPopedom = popedoms.iterator(); iteratorPopedom.hasNext();) {
					PublicDirectoryPopedom popedom = (PublicDirectoryPopedom)iteratorPopedom.next();
					recordControlService.appendVisitor(info.getId(), PublicInfo.class.getName(), popedom.getUserId(), RecordControlService.ACCESS_LEVEL_READONLY);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoService#regenerateIndex(long)
	 */
	public void regenerateIndex(long mainDirectoryId) throws ServiceException {
		//获取单位编码
    	String hql = "from PublicUnitCode PublicUnitCode" +
    				 " where PublicUnitCode.directoryId=" + mainDirectoryId;
    	List unitCodes = getDatabaseService().findRecordsByHql(hql);
    	for(Iterator iterator = unitCodes.iterator(); iterator.hasNext();) {
    		PublicUnitCode unitCode = (PublicUnitCode)iterator.next();
    		//清空编号
    		hql = "from PublicInfoSequence PublicInfoSequence" +
    			  " where PublicInfoSequence.category='" + unitCode.getCode() + "'" +
    			  " or PublicInfoSequence.category like '" + unitCode.getCode() + "-%'";
    		getDatabaseService().deleteRecordsByHql(hql);
    	}
    	//获取信息列表
    	for(int i=0; ; i+=200) {
	    	List infos = listInfos("" + mainDirectoryId, true, true, true, i, 200);
	    	for(Iterator iterator = infos==null ? null : infos.iterator(); iterator!=null && iterator.hasNext();) {
	    		PublicInfo info = (PublicInfo)iterator.next();
	    		String infoIndex = info.getInfoIndex();
	    		info.setInfoIndex(null); //清空索引号
	    		update(info); //更新记录
	    		Logger.info("PublicInfoService: regenerate info that subject is " + info.getSubject() + " from index " + infoIndex + " to " + info.getInfoIndex() + ".");
	    	}
	    	if(infos==null || infos.size()<200) {
	    		break;
	    	}
    	}
	}

	/**
	 * @return the publicDirectoryService
	 */
	public PublicDirectoryService getPublicDirectoryService() {
		return publicDirectoryService;
	}

	/**
	 * @param publicDirectoryService the publicDirectoryService to set
	 */
	public void setPublicDirectoryService( PublicDirectoryService publicDirectoryService) {
		this.publicDirectoryService = publicDirectoryService;
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}

	/**
	 * @return the temporaryDirectory
	 */
	public String getTemporaryDirectory() {
		if(temporaryDirectory==null) {
			temporaryDirectory = Environment.getWebinfPath();
		}
		return temporaryDirectory;
	}

	/**
	 * @param temporaryDirectory the temporaryDirectory to set
	 */
	public void setTemporaryDirectory(String temporaryDirectory) {
		this.temporaryDirectory = temporaryDirectory;
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
	 * @return the siteResourceService
	 */
	public SiteResourceService getSiteResourceService() {
		return siteResourceService;
	}

	/**
	 * @param siteResourceService the siteResourceService to set
	 */
	public void setSiteResourceService(SiteResourceService siteResourceService) {
		this.siteResourceService = siteResourceService;
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
	 * @return the temporaryFileManageService
	 */
	public TemporaryFileManageService getTemporaryFileManageService() {
		return temporaryFileManageService;
	}

	/**
	 * @param temporaryFileManageService the temporaryFileManageService to set
	 */
	public void setTemporaryFileManageService(
			TemporaryFileManageService temporaryFileManageService) {
		this.temporaryFileManageService = temporaryFileManageService;
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

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
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
	 * @return the indexByIssueTime
	 */
	public boolean isIndexByIssueTime() {
		return indexByIssueTime;
	}

	/**
	 * @param indexByIssueTime the indexByIssueTime to set
	 */
	public void setIndexByIssueTime(boolean indexByIssueTime) {
		this.indexByIssueTime = indexByIssueTime;
	}
}
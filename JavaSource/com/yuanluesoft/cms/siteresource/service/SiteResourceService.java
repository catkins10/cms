/*
 * Created on 2007-7-3
 *
 */
package com.yuanluesoft.cms.siteresource.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.capture.service.CaptureRecordService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.smssubscription.service.SmsContentService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 * 
 */
public interface SiteResourceService extends BusinessService, SmsContentService, CaptureRecordService {
	//资源类型
	public final static int RESOURCE_TYPE_ARTICLE = 0; //文章
	public final static int RESOURCE_TYPE_SERICALIZATION = 1; //连载 
	public final static int RESOURCE_TYPE_LINK = 2; //链接
	
	public final static String[] RESOURCE_TYPE_NAMES = {"article", "serialization", "link"};
	public final static String[] RESOURCE_TYPE_TITLES = {"文章", "连载", "链接"};
	
	//匿名访问级别
	public final static char ANONYMOUS_LEVEL_AUTO = '0'; //自动
	public final static char ANONYMOUS_LEVEL_NONE = '1'; //不能访问
	public final static char ANONYMOUS_LEVEL_SUBJECT = '2'; //仅主题
	public final static char ANONYMOUS_LEVEL_ALL = '3'; //完全访问
	
	//同步的文章发布设置
	public final static char SYNCH_ISSUE_ALL = '1'; //直接发布
	public final static char SYNCH_ISSUE_SAME_SITE = '2'; //相同站点的直接发布
	public final static char SYNCH_ISSUE_NONE = '3'; //不直接发布
	
	//状态
	public final static char RESOURCE_STATUS_UNISSUE = '0'; //撤销发布
	public final static char RESOURCE_STATUS_UNDO = '1'; //退回、取回修改
	public final static char RESOURCE_STATUS_TODO = '2'; //待处理
	public final static char RESOURCE_STATUS_ISSUE = '3'; //已发布
	public final static char RESOURCE_STATUS_NOPASS = '4'; //办结未发布
	public final static char RESOURCE_STATUS_DELETED = '5'; //已删除
	
	/**
	 * 发布
	 * @param resource
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issue(SiteResource resource, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 撤销发布
	 * @param resource
	 * @throws ServiceException
	 */
	public void unissue(SiteResource resource) throws ServiceException;
	
	/**
	 * 定时撤销超时的站点资源
	 * @throws ServiceException
	 */
	public void unissueTimeoutResources() throws ServiceException;
	
	/**
	 * 获取站点资源
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public SiteResource getSiteResource(long id) throws ServiceException;
	
	/**
	 * 获取站点资源在指定栏目中的拷贝
	 * @param id
	 * @param columnId
	 * @return
	 * @throws ServiceException
	 */
	public SiteResource getSiteResourceCopy(long id, long columnId) throws ServiceException;
	
	/**
	 * 更新资源的隶属关系
	 * @param siteResource
	 * @param isNew
	 * @param subjectionDirectoryIds
	 * @throws ServiceException
	 */
	public void updateSiteResourceSubjections(SiteResource siteResource, boolean isNew, String subjectionDirectoryIds) throws ServiceException;
	
	/**
	 * 添加站点资源
	 * @param siteIds 发布的栏目列表,用逗号分隔
	 * @param type
	 * @param subject
	 * @param subhead
	 * @param source
	 * @param author
	 * @param keyword
	 * @param mark 文号
	 * @param anonymousLevel 匿名用户访问级别
	 * @param link
	 * @param created
	 * @param issueTime
	 * @param isIssue
	 * @param body
	 * @param sourceRecordId
	 * @param sourceRecordUrl
	 * @param issuePersonId
	 * @param issuePersonName
	 * @param issuePersonOrgId
	 * @param issuePersonOrgName
	 * @param issuePersonUnitId
	 * @param issuePersonUnitName
	 * @param resourceClassName
	 * @return
	 * @throws ServiceException
	 */
	public SiteResource addResource(String siteIds,
									int type,
									String subject,
									String subhead,
									String source,
									String author,
									String keyword,
									String mark,
									char anonymousLevel,
									String link,
									Timestamp created,
									Timestamp issueTime,
									boolean isIssue,
									String body,
									String sourceRecordId,
									String sourceRecordClassName,
									String sourceRecordUrl,
									long issuePersonId,
									String issuePersonName,
									long issuePersonOrgId,
									String issuePersonOrgName,
									long issuePersonUnitId,
									String issuePersonUnitName) throws ServiceException;
	
	/**
	 * 按源记录ID获取资源所属栏目ID
	 * @param sourceRecordId
	 * @return
	 * @throws ServiceException
	 */
	public List listColumnIdsBySourceRecordId(String sourceRecordId) throws ServiceException;
		
	/**
	 * 按源记录ID删除资源
	 * @param sourceRecordId
	 * @throws ServiceException
	 */
	public SiteResource deleteResourceBySourceRecordId(String sourceRecordId) throws ServiceException;
	
	/**
	 * 获取站点资源
	 * @param siteIds
	 * @param resouceTypes
	 * @param containChildren
	 * @param issuedOnly
	 * @param offset
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listSiteResources(String siteIds, String resouceTypes, boolean containChildren, boolean issuedOnly, int offset, int max) throws ServiceException;

	/**
	 * 获取站点资源类型列表
	 * @param resourceIds
	 * @return
	 * @throws ServiceException
	 */
	public Map getResourceTypes(String resourceIds) throws ServiceException;
	
	/**
	 * 是否逻辑删除
	 * @return
	 */
	public boolean isLogicalDelete();
	
	/**
	 * 修改文章的访问者
	 * @param view
	 * @param currentCategories
	 * @param searchConditions
	 * @param selectedResourceIds
	 * @param modifyMode
	 * @param selectedResourceOnly
	 * @param deleteNotColumnVisitor
	 * @param readerIds
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void modifyReaders(View view, String currentCategories, String searchConditions, String selectedResourceIds, String modifyMode, boolean selectedResourceOnly, boolean deleteNotColumnVisitor, String readerIds, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 按栏目ID获取抓取任务
	 * @param columnId
	 * @return
	 * @throws ServiceException
	 */
	public List listCaptureTasksByColumnId(long columnId) throws ServiceException;
	
	/**
	 * 添加相关链接
	 * @param siteResource
	 * @param resourceIds
	 * @throws ServiceException
	 */
	public void addRelationLinks(SiteResource siteResource, String resourceIds) throws ServiceException;
	
	/**
	 * 站点资源统计
	 * @param columnId
	 * @param status 状态,a/全部,0/撤销发布,1/待处理,2/退回、取回修改,3/已发布,4/办结未发布,5/已删除
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	public int countSiteResources(long columnId, char status, Date beginDate, Date endDate) throws ServiceException;
	
	/**
	 * 置顶
	 * @param resourceId
	 * @param columnIds
	 * @param topDirectoryIds
	 * @param expire
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void setTop(long resourceId, String columnIds, long[] topDirectoryIds, Date expire, SessionInfo sessionInfo) throws ServiceException;
}
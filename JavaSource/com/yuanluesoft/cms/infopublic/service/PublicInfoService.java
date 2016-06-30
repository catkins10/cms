package com.yuanluesoft.cms.infopublic.service;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.capture.service.CaptureRecordService;
import com.yuanluesoft.cms.infopublic.model.MonitoringReport;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.smssubscription.service.SmsContentService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author yuanluesoft
 *
 */
public interface PublicInfoService extends BusinessService, SmsContentService, CaptureRecordService {
	//状态
	public final static char INFO_STATUS_UNISSUE = '0'; //撤销发布
	public final static char INFO_STATUS_UNDO = '1'; //退回、取回修改
	public final static char INFO_STATUS_TODO = '2'; //待处理
	public final static char INFO_STATUS_ISSUE = '3'; //已发布
	public final static char INFO_STATUS_NOPASS = '4'; //办结未发布
	public final static char INFO_STATUS_DELETED = '5'; //已删除
	//类型
	public final static int INFO_TYPE_NORMAL = 0; //一般信息
	public final static int INFO_TYPE_ARTICLE = 1; //文章,年度报告\制度等

	public final static String[] INFO_TYPE_NAMES = {"publicInfo", "publicArticle"};
	public final static String[] INFO_TYPE_TITLES = {"信息", "文章"};

	/**
	 * 获取政务信息
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public PublicInfo getPublicInfo(long id) throws ServiceException;
	
	/**
	 * 更新信息隶属目录列表
	 * @param info
	 * @param isNew
	 * @param subjectionDirectoryIds
	 * @throws ServiceException
	 */
	public void updateInfoSubjections(PublicInfo info, boolean isNew, String subjectionDirectoryIds) throws ServiceException;
	
	/**
	 * 发布信息
	 * @param info
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void issue(PublicInfo info, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 撤销发布
	 * @param info
	 * @throws ServiceException
	 */
	public void unissue(PublicInfo info) throws ServiceException;
	
	/**
	 * 按ID获取信息列表
	 * @param ids
	 * @param loadBody
	 * @return
	 * @throws ServiceException
	 */
	public List listInfosByIds(String ids, boolean loadBody) throws ServiceException;
	
	/**
	 * 重新同步所有的信息到网站
	 * @param forceDirectorySynchSiteIds 强制用目录配置的同步栏目ID列表替换信息的同步栏目列表
	 * @throws ServiceException
	 */
	public void resynchAllInfos(boolean forceDirectorySynchSiteIds) throws ServiceException;
	
	/**
	 * 重新同步信息
	 * @param directoryId
	 * @param oldSynchSiteIds 原来需要同步的栏目ID列表
	 * @param newSynchSiteIds 现在需要同步的栏目ID列表
	 * @throws ServiceException
	 */
	public void resynchPublicInfos(long directoryId, String oldSynchSiteIds, String newSynchSiteIds) throws ServiceException;
	
	/**
	 * 重新同步单个信息
	 * @param info
	 * @param addedSynchSiteIds 需要增加的栏目ID列表
	 * @param deletedSynchSiteIds 需要减少的栏目ID列表
	 * @throws ServiceException
	 */
	public void resynchPublicInfo(PublicInfo info, String addedSynchSiteIds, String deletedSynchSiteIds) throws ServiceException;
	
	/**
	 * 按ID导出政务信息
	 * @param rootDirectoryId
	 * @param ids
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void exportPublicInfo(long rootDirectoryId, String ids, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 统计,返回InfoStat列表,其中第一个为合计
	 * @param beginDate
	 * @param endDate
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public List stat(Date beginDate, Date endDate, long directoryId) throws ServiceException;
	
	/**
	 * 按分类统计,返回InfoCategoryStat列表,其中第一个为合计
	 * @param beginDate
	 * @param endDate
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	public List statByCategory(Date beginDate, Date endDate, long directoryId) throws ServiceException;
	
	/**
	 * 输出信息公开报表(光泽)
	 * @param beginDate
	 * @param endDate
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void writeInfoReport(Date beginDate, Date endDate, HttpServletRequest request, HttpServletResponse response) throws ServiceException;

	/**
	 * 获取信息数量
	 * @param directoryIds
	 * @param issuedOnly
	 * @return
	 * @throws ServiceException
	 */
	public int getInfosCount(String directoryIds, boolean issuedOnly) throws ServiceException;
	
	/**
	 * 获取信息列表
	 * @param directoryIds
	 * @param issuedOnly
	 * @param containsChildDirectory 是否包括子目录中的信息
	 * @param sortByGenerateDate 是否按生产日期排序,默认按名称排序
	 * @param offset
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listInfos(String directoryIds, boolean issuedOnly, boolean containsChildDirectory, boolean sortByGenerateDate, int offset, int max) throws ServiceException;
	
	/**
	 * 是否逻辑删除
	 * @return
	 */
	public boolean isLogicalDelete();
	
	/**
	 * 输出监察报表
	 * @param directoryId
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	public MonitoringReport writeMonitoringReport(long directoryId, Date beginDate, Date endDate) throws ServiceException;
	
	/**
	 * 修改信息的访问者
	 * @param view
	 * @param currentCategories
	 * @param searchConditions
	 * @param selectedIds
	 * @param modifyMode
	 * @param selectedOnly
	 * @param deleteNotDirectoryVisitor
	 * @param readerIds
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void modifyReaders(View view, String currentCategories, String searchConditions, String selectedIds, String modifyMode, boolean selectedOnly, boolean deleteNotDirectoryVisitor, String readerIds, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 重建指定目录中的信息索引号
	 * @param mainDirectoryId
	 * @throws ServiceException
	 */
	public void regenerateIndex(long mainDirectoryId) throws ServiceException;
}
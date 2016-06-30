package com.yuanluesoft.cms.pagebuilder;

import java.io.Serializable;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;


/**
 * 静态页面服务
 * TODO: 静态页面编辑,手工编辑过的页面和自动更新的关系,建议区分编辑过的部分和未编辑的部分,编辑过的不更新,编辑过的页面应该包含模板的配置信息,并作为模板使用,允许编辑的区域在鼠标移到后显示为编辑状态(如:加红边框),过去已经编辑过的区域,要提醒是否恢复自动更新
 * @author linchuan
 *
 */
public interface StaticPageBuilder extends BusinessService {
	//对象修改操作类型
	public static final String OBJECT_MODIFY_ACTION_UPDATE = "update";
	public static final String OBJECT_MODIFY_ACTION_PHYSICAL_DELETE = "delete";
	public static final String OBJECT_MODIFY_ACTION_LOGICAL_DELETE = "logicalDelete";
	
	/**
	 * 重建所有的静态页面
	 * @throws ServiceException
	 */
	public void reduildAllStaticPages() throws ServiceException;

	/**
	 * 根据URL生成静态页面
	 * @param url
	 * @return
	 * @throws ServiceException
	 */
	public void buildStaticPage(String dynamicUrl) throws ServiceException;
	
	/**
	 * 获取静态页面的URL
	 * @param dynamicUrl
	 * @return
	 * @throws ServiceException
	 */
	public String getStaticPageURL(String dynamicUrl) throws ServiceException;
	
	/**
	 * 获取静态页面的文件路径
	 * @param dynamicUrl
	 * @return
	 * @throws ServiceException
	 */
	public String getStaticPagePath(String dynamicUrl) throws ServiceException;
	
	/**
	 * 数据变动后,重新生成静态页面
	 * @param object
	 * @param modifyAction 修改操作,包括OBJECT_MODIFY_ACTION_NEW、OBJECT_MODIFY_ACTION_UPDATE、OBJECT_MODIFY_ACTION_DELETE,也可以是其他自定义的值
	 * @throws ServiceException
	 */
	public void rebuildPageForModifiedObject(Serializable object, String modifyAction) throws ServiceException;
	
	/**
	 * 模板变动后,重新生成静态页面
	 * @param applicationName 不指定时(如子模板),更新所有应用的页面
	 * @param pageName 不指定时(如子模板),更新所有页面
	 * @param templateId 静态页面当前使用的模板ID,如果没有模板或者是预置模板,templateId=0
	 * @param webDirectoryId 页面隶属的站点/栏目ID
	 * @param includeSubdirectory 是否包含子目录
	 * @throws ServiceException
	 */
	public void rebuildPageForTemplate(String applicationName, String pageName, long templateId, long webDirectoryId, boolean includeSubdirectory) throws ServiceException;

	/**
	 * 按主题重建静态页面
	 * @param themeId
	 * @param siteId
	 * @param notUseThemePage 重建没有使用当前主题的页面
	 * @param realtimeStaticPageOnly 是否只更新需要实时重建的静态页面
	 * @throws ServiceException
	 */
	public void rebuildPageForTheme(long themeId, long siteId, boolean notUseThemePage, boolean realtimeStaticPageOnly) throws ServiceException;
}
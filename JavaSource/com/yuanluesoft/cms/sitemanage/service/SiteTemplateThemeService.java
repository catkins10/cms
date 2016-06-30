package com.yuanluesoft.cms.sitemanage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.model.PhonePageConfig;
import com.yuanluesoft.cms.sitemanage.pojo.SiteTemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 站点模板主题服务
 * @author linchuan
 *
 */
public interface SiteTemplateThemeService extends TemplateThemeService {
	
	/**
	 * 设为默认主题
	 * @param themeId
	 * @param siteId
	 * @param isDefault 是否默认主题
	 * @param isTemporaryOpening 是否临时启用
	 * @throws ServiceException
	 */
	public void setAsDefaultTheme(long themeId, long siteId, boolean isDefault, boolean isTemporaryOpening) throws ServiceException;
	
	/**
	 * 获取模板主题,并根据siteId检查是否默认主题、临时启用主题
	 * @param themeId
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public SiteTemplateTheme getSiteTemplateTheme(long themeId, long siteId) throws ServiceException;

	/**
	 * 获取默认主题
	 * @param siteId
	 * @param themeType 主题类型(电脑|0/智能手机或平板电脑|1/WAP|2)
	 * @param pageWidth -1表示不限制
	 * @param flashSupport -1:不限制, 0:只输出不支持flash的, 1:只输出支持flash的
	 * @return
	 * @throws ServiceException
	 */
	public SiteTemplateTheme getDefaultTheme(long siteId, int themeType, int pageWidth, int flashSupport) throws ServiceException;
	
	/**
	 * 是否启用了临时主题
	 * @param siteId
	 * @param themeType
	 * @param pageWidth -1表示不限制
	 * @param flashSupport -1:不限制, 0:只输出不支持flash的, 1:只输出支持flash的
	 * @return
	 * @throws ServiceException
	 */
	public boolean isTemporaryOpeningThemeUsed(long siteId, int themeType, int pageWidth, int flashSupport) throws ServiceException;
	
	/**
	 * 获取主题列表,按主题类型、页面宽度排序,并根据siteId检查是否默认主题、临时启用主题
	 * @param siteId 站点ID
	 * @param themeTypes 主题类型,null表示全部
	 * @param pageWidth -1表示不限制
	 * @param flashSupport -1:不限制, 0:只输出不支持flash的, 1:只输出支持flash的
	 * @return
	 * @throws ServiceException
	 */
	public List listSiteThemes(long siteId, int[] themeTypes, int pageWidth, int flashSupport) throws ServiceException;
	
	/**
	 * 初始化手机页面配置
	 * @param screenWidth
	 * @param screenHeight
	 * @param siteId
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public PhonePageConfig initPhonePageConfig(int screenWidth, int screenHeight, long siteId, HttpServletRequest request) throws ServiceException;
}
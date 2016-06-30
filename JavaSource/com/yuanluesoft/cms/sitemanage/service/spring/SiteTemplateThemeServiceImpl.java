package com.yuanluesoft.cms.sitemanage.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.model.PhonePageConfig;
import com.yuanluesoft.cms.sitemanage.model.SynchSetDefaultTheme;
import com.yuanluesoft.cms.sitemanage.pojo.SiteTemplateTheme;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateThemeUsage;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.cms.templatemanage.service.spring.TemplateThemeServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class SiteTemplateThemeServiceImpl extends TemplateThemeServiceImpl implements SiteTemplateThemeService {
	private PageDefineService pageDefineService; //页面定义服务
	private StaticPageBuilder staticPageBuilder; //静态页面服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#synchUpdate(java.lang.Object, java.lang.String)
	 */
	public void synchUpdate(Object object, String senderName) throws ServiceException {
		if(object instanceof SynchSetDefaultTheme) {
			SynchSetDefaultTheme setDefaultTheme = (SynchSetDefaultTheme)object;
			setAsDefaultTheme(setDefaultTheme.getThemeId(), setDefaultTheme.getSiteId(), setDefaultTheme.isDefault(), setDefaultTheme.isTemporaryOpening());
			return;
		}
		super.synchUpdate(object, senderName);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateThemeService#setAsDefaultTheme(long, boolean, boolean)
	 */
	public void setAsDefaultTheme(long themeId, long siteId, boolean isDefault, boolean isTemporaryOpening) throws ServiceException {
		//获取同类模板
		List themes = listCongenerThemes(themeId, siteId);
		SiteTemplateTheme theme = (SiteTemplateTheme)ListUtils.findObjectByProperty(themes, "id", new Long(themeId));
		if(theme.isDefaultTheme()) { //已经是默认模板
			return;
		}
		if(isTemporaryOpening && theme.isTemporaryOpening()) { //已经是临时启用模板
			return;
		}
		SiteTemplateTheme temporaryOpeningTheme = isTemporaryOpening ? (SiteTemplateTheme)ListUtils.findObjectByProperty(themes, "temporaryOpening", Boolean.TRUE) : null;
		//找到其他主题的使用记录,并删除
		for(Iterator iterator = themes.iterator(); iterator.hasNext();) {
			SiteTemplateTheme templateTheme = (SiteTemplateTheme)iterator.next();
			if(templateTheme==theme) {
				continue;
			}
			TemplateThemeUsage usage = (TemplateThemeUsage)ListUtils.findObjectByProperty(templateTheme.getUsages(), "siteId", new Long(siteId));
			if(usage==null) { //没找到主题使用记录
				continue;
			}
			if((isDefault && usage.getIsDefault()==1) || (isTemporaryOpening && usage.getTemporaryOpening()==1)) {
				getDatabaseService().deleteRecord(usage);
			}
		}
		TemplateThemeUsage usage = (TemplateThemeUsage)ListUtils.findObjectByProperty(theme.getUsages(), "siteId", new Long(siteId));
		if(usage!=null) { //找到主题使用记录
			if(isDefault) { //设为默认
				usage.setIsDefault(1);
				usage.setTemporaryOpening(0);
				getDatabaseService().updateRecord(usage);
			}
			else if(isTemporaryOpening) { //临时启用
				usage.setTemporaryOpening(1);
				getDatabaseService().updateRecord(usage);
			}
			else { //取消临时启用
				getDatabaseService().deleteRecord(usage);
			}
		}
		else if(!isDefault && !isTemporaryOpening) {
			return;
		}
		else {
			//创建新记录
			usage = new TemplateThemeUsage();
			usage.setId(UUIDLongGenerator.generateId()); //ID
			usage.setSiteId(siteId); //站点/用户ID,如果站点没有配置自己主题,则使用父站点的配置
			usage.setThemeId(themeId); //主题ID,iphine、ipad不支持flash
			usage.setIsDefault(isDefault ? 1 : 0); //是否默认主题,默认主题修改后重新生成本站的全部静态页面
			usage.setTemporaryOpening(isTemporaryOpening ? 1 : 0); //是否临时启用
			getDatabaseService().saveRecord(usage);
		}
		//清除模板缓存
		getTemplateService().clearCachedTemplate();
		//重建静态页面
		int themeAction = (isDefault ? THEME_ACTION_SET_DEFAULT : (isTemporaryOpening ? THEME_ACTION_TEMPORARY_OPENING : THEME_ACTION_CANCEL_TEMPORARY_OPENING)) ; //主题操作
		theme.setDefaultTheme(isDefault);
		theme.setTemporaryOpening(isTemporaryOpening);
		rebuildStaticPageForTheme(theme, themeAction, siteId);
		if(themeAction==THEME_ACTION_TEMPORARY_OPENING && temporaryOpeningTheme!=null) { //临时启用,并且原来就有临时模板
			temporaryOpeningTheme.setTemporaryOpening(false);
			rebuildStaticPageForTheme(temporaryOpeningTheme, THEME_ACTION_CANCEL_TEMPORARY_OPENING, siteId);
		}
		//同步更新
		getExchangeClient().synchUpdate(new SynchSetDefaultTheme(themeId, siteId, isDefault, isTemporaryOpening), null, 2000); //同步更新
	}

	/**
	 * 重建静态页面
	 * @param theme
	 * @param themeAction
	 * @throws ServiceException
	 */
	private void rebuildStaticPageForTheme(TemplateTheme theme, int themeAction, long siteId) throws ServiceException {
		if(theme.getType()!=THEME_TYPE_COMPUTER) { //主题类型不是电脑,不需要重建模板
			return;
		}
		SiteTemplateTheme siteTemplateTheme = (SiteTemplateTheme)theme;
		if(themeAction==THEME_ACTION_CANCEL_TEMPORARY_OPENING) { //取消临时启用
			staticPageBuilder.rebuildPageForTheme(siteTemplateTheme.getId(), siteId, false, true);
			return;
		}
		//临时启用的主题或者主题下的模板数量少于20个,直接按模板来更新静态页面,否则更新未使用当前主题的页面
		String hql = "from Template Template" +
					 " where Template.themeId=" + theme.getId() +
					 " and Template.isSelected='1'" +
					 " and Template.pageName!='subTemplate'";
		Number count = (Number)getDatabaseService().findRecordByHql("select count(Template.id) " + hql);
		if(count==null || count.intValue()==0) {
			return;
		}
		if(theme.isDefaultTheme() && count.intValue()>20) { //默认模板,且模板数超过20个
			staticPageBuilder.rebuildPageForTheme(siteTemplateTheme.getId(), siteId, true, false); //更新未使用当前主题的页面
			return;
		}
		hql += " order by Template.id";
		//获取主题下的模板
		for(int i=0; ; i+=200) {
			List templates = getDatabaseService().findRecordsByHql(hql, i, 200); //每次处理200个模板
			for(Iterator iterator = templates==null ? null : templates.iterator(); iterator!=null && iterator.hasNext();) {
				Template template = (Template)iterator.next();
				SitePage sitePage = pageDefineService.getSitePage(template.getApplicationName(), template.getPageName());
				if(sitePage==null) {
					continue;
				}
				if(theme.isTemporaryOpening() && !sitePage.isRealtimeStaticPage()) {  //临时启用的主题,并且不需要实时重建静态页面
					continue;
				}
				//重建静态页面
				try {
					TemplateService templateService = (TemplateService)Environment.getService(getBusinessDefineService().getBusinessObject(template.getClass()).getBusinessServiceName());
					templateService.rebuildStaticPage(template, null, siteId);
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
			if(templates==null || templates.size()<200) {
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService#getSiteTemplateTheme(long, long)
	 */
	public SiteTemplateTheme getSiteTemplateTheme(long themeId, long siteId) throws ServiceException {
		return (SiteTemplateTheme)ListUtils.findObjectByProperty(listCongenerThemes(themeId, siteId), "id", new Long(themeId));
	}
	
	/**
	 * 获取同类的主题,并根据siteId检查是否默认模板、临时启用模板
	 * @param themeId
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	private List listCongenerThemes(long themeId, long siteId) throws ServiceException {
		String hql = "select SiteTemplateTheme.type, SiteTemplateTheme.pageWidth, SiteTemplateTheme.flashSupport" +
					 " from SiteTemplateTheme SiteTemplateTheme" +
					 " where SiteTemplateTheme.id=" + themeId;
		Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
		return listSiteThemes(siteId, new int[]{((Number)values[0]).intValue()}, ((Number)values[1]).intValue(), ((Number)values[2]).intValue());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService#getDefaultTheme(long, int, int, int)
	 */
	public SiteTemplateTheme getDefaultTheme(long siteId, int themeType, int pageWidth, int flashSupport) throws ServiceException {
		List themes = listSiteThemes(siteId, new int[]{themeType}, pageWidth, flashSupport);
		if(themes==null || themes.isEmpty()) {
			return null;
		}
		SiteTemplateTheme defaultTheme = (SiteTemplateTheme)ListUtils.findObjectByProperty(themes, "defaultTheme", Boolean.TRUE);
		return defaultTheme==null ? (SiteTemplateTheme)themes.get(0) : defaultTheme;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService#isTemporaryOpeningThemeUsed(long, int, int, int)
	 */
	public boolean isTemporaryOpeningThemeUsed(long siteId, int themeType, int pageWidth, int flashSupport) throws ServiceException {
		List themes = listSiteThemes(siteId, new int[]{themeType}, pageWidth, flashSupport);
		return ListUtils.findObjectByProperty(themes, "temporaryOpening", Boolean.TRUE)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService#initPhonePageConfig(int, int, long, javax.servlet.http.HttpServletRequest)
	 */
	public PhonePageConfig initPhonePageConfig(int screenWidth, int screenHeight, long siteId, HttpServletRequest request) throws ServiceException {
		PhonePageConfig phonePageConfig = new PhonePageConfig();
		String terminalType = RequestUtils.getTerminalType(request);
		phonePageConfig.setSystemName(terminalType); //系统名称
		phonePageConfig.setScreenWidth(screenWidth); //屏幕宽度
		phonePageConfig.setScreenHeight(screenHeight); //屏幕高度
		//设置主题列表
		phonePageConfig.setRecommendedThemes(new ArrayList());
		phonePageConfig.setOtherThemes(new ArrayList());
		List themes = listSiteThemes(siteId, new int[]{TemplateThemeService.THEME_TYPE_COMPUTER, TemplateThemeService.THEME_TYPE_SMART_PHONE}, -1, -1); //获取主题列表
		SiteTemplateTheme recommendedTheme = null; //推荐主题
		for(Iterator iterator = themes==null ? null : themes.iterator(); iterator!=null && iterator.hasNext();) {
			SiteTemplateTheme theme = (SiteTemplateTheme)iterator.next();
			if(!theme.isDefaultTheme()) { //不是默认主题
				continue;
			}
			if(theme.getType()==TemplateThemeService.THEME_TYPE_SMART_PHONE) { //手机页面
				if(theme.getPageWidth()<=screenWidth || theme.getPageWidth()<=screenHeight) {
					if(recommendedTheme==null || recommendedTheme.getFlashSupport()==1 || theme.getFlashSupport()!=1) { //默认推荐无flash页面
						recommendedTheme = theme;
					}
				}
				else if(recommendedTheme==null) {
					recommendedTheme = theme;
				}
			}
			phonePageConfig.getOtherThemes().add(theme);
		}
		if(recommendedTheme!=null) {
			phonePageConfig.getOtherThemes().remove(recommendedTheme);
			phonePageConfig.getRecommendedThemes().add(recommendedTheme);
		}
		return phonePageConfig;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService#listSiteThemes(long, int[], int, int)
	 */
	public List listSiteThemes(long siteId, int[] themeTypes, int pageWidth, int flashSupport) throws ServiceException {
		//获取主题
		String hql = "select SiteTemplateTheme" +
			  		 " from SiteTemplateTheme SiteTemplateTheme, WebDirectorySubjection WebDirectorySubjection" +
			  		 " where SiteTemplateTheme.siteId=WebDirectorySubjection.parentDirectoryId" +
			  		 " and WebDirectorySubjection.directoryId=" + siteId +
			  		 (themeTypes==null ? "" : " and SiteTemplateTheme.type in (" + ListUtils.join(themeTypes, ",", false) + ")") +
			  		 (pageWidth==-1 ? "" : " and SiteTemplateTheme.pageWidth=" + pageWidth) +
			  		 (flashSupport==-1 ? "" : " and SiteTemplateTheme.flashSupport=" + flashSupport) +
			  		 " order by SiteTemplateTheme.type, SiteTemplateTheme.pageWidth, SiteTemplateTheme.flashSupport, SiteTemplateTheme.name";
		List themes = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("usages"));
		if(themes==null || themes.isEmpty()) {
			return null;
		}
		List siteIds = getSiteIds(siteId);
		//分组检查默认主题和临时启用主题
		List congenerThemes = new ArrayList();
		for(Iterator iterator = themes.iterator(); iterator.hasNext();) {
			SiteTemplateTheme theme = (SiteTemplateTheme)iterator.next();
			if(congenerThemes.isEmpty()) {
				congenerThemes.add(theme);
				continue;
			}
			SiteTemplateTheme congenerTheme = (SiteTemplateTheme)congenerThemes.get(0);
			if(congenerTheme.getType()==theme.getType() && congenerTheme.getPageWidth()==theme.getPageWidth() && congenerTheme.getFlashSupport()==theme.getFlashSupport()) {
				congenerThemes.add(theme);
				continue;
			}
			//主题类型已经改变,检查默认主题和临时启用主题
			checkDefaultTheme(siteIds, congenerThemes);
			//开始新的一组主题
			congenerThemes.clear();
			congenerThemes.add(theme);
		}
		//检查最后一组默认主题和临时启用主题
		checkDefaultTheme(siteIds, congenerThemes);
		return themes;
	}
	
	/**
	 * 获取获取站点的父目录ID列表,从低到高排列,包括自己
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	private List getSiteIds(long siteId) throws ServiceException {
		String hql = "select WebDirectorySubjection.parentDirectoryId" +
					 " from WebDirectorySubjection WebDirectorySubjection" +
					 " where WebDirectorySubjection.directoryId=" + siteId +
					 " order by WebDirectorySubjection.id";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/**
	 * 检查一组主题中的默认主题和临时启用主题
	 * @param siteIds 从低到高排序的站点ID列表
	 * @param themes
	 * @throws ServiceException
	 */
	private void checkDefaultTheme(List siteIds, List themes) throws ServiceException {
		boolean foundDefaultTheme = false; //默认主题
		boolean foundTemporaryOpeningTheme = false; //临时启用主题
		for(Iterator iterator = siteIds.iterator(); !foundDefaultTheme && iterator.hasNext();) {
			Number siteId = (Number)iterator.next();
			for(Iterator iteratorTheme = themes.iterator(); (!foundDefaultTheme || !foundTemporaryOpeningTheme) && iteratorTheme.hasNext();) {
				SiteTemplateTheme theme = (SiteTemplateTheme)iteratorTheme.next();
				TemplateThemeUsage themeUsage = (TemplateThemeUsage)ListUtils.findObjectByProperty(theme.getUsages(), "siteId", siteId);
				if(themeUsage==null) {
					continue;
				}
				if(!foundDefaultTheme && themeUsage.getIsDefault()==1) {
					foundDefaultTheme = true;
					theme.setDefaultTheme(true);
				}
				if(!foundTemporaryOpeningTheme && themeUsage.getTemporaryOpening()==1) {
					foundTemporaryOpeningTheme = true;
					theme.setTemporaryOpening(true);
				}
			}
		}
	}
	

	/**
	 * @return the pageDefineService
	 */
	public PageDefineService getPageDefineService() {
		return pageDefineService;
	}

	/**
	 * @param pageDefineService the pageDefineService to set
	 */
	public void setPageDefineService(PageDefineService pageDefineService) {
		this.pageDefineService = pageDefineService;
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
}
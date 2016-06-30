package com.yuanluesoft.cms.scene.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.scene.pojo.SceneTemplate;
import com.yuanluesoft.cms.scene.service.SceneTemplateService;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SceneTemplateServiceImpl extends TemplateServiceImpl implements SceneTemplateService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.scene.service.SceneTemplateService#getTemplateHTMLDocument(java.lang.String, long, long, long, int, int, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public HTMLDocument getTemplateHTMLDocument(String pageName, long sceneDirectoryId, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException {
		HTMLDocument templateDocument = null;
		String key = "scene/" + pageName + "/" + sceneDirectoryId + "/" + siteId + "/" + themeId + "/" + themeType + "/" + pageWidth + "/" + (flashSupport ? 1 : 0) + "/" + (temporaryOpeningFirst ? 1 : 0);
		//从缓存中读取模板
		try {
			templateDocument = (HTMLDocument)getCache().get(key);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		if(templateDocument==null) {
			//获取模板
			SceneTemplate template = getSceneTemplate(sceneDirectoryId, pageName, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst);
			if(template!=null) { //有直接为场景配置的模板
				templateDocument = getTemplateHTMLDocument(template.getId(), siteId, false, request);
			}
			if(templateDocument==null) {
				return null;
			}
			//写入缓存
			try {
				getCache().put(key, templateDocument);
			}
			catch (CacheException e) {
				
			}
		}
		return (HTMLDocument)templateDocument.cloneNode(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#getParentTemplate(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected Template getParentTemplate(Template template, TemplateTheme theme) throws ServiceException {
		SceneTemplate sceneTemplate = (SceneTemplate)template;
		if(sceneTemplate==null || sceneTemplate.getDirectoryId()==0) {
			return null;
		}
		//获取上级目录ID
		String hql = "select SceneDirectory.parentDirectoryId" +
			 		 " from SceneDirectory SceneDirectory" +
			 		 " where SceneDirectory.id=" + sceneTemplate.getDirectoryId();
		Number parentDirectoryId = (Number)getDatabaseService().findRecordByHql(hql);
		if(parentDirectoryId==null) { //没有上级目录
			return null;
		}
		return getSceneTemplate(parentDirectoryId.longValue(), sceneTemplate.getPageName(), sceneTemplate.getSiteId(), theme.getId(), theme.getType(), theme.getPageWidth(), theme.getFlashSupport()==1, false);
	}
	
	/**
	 * 获取模板记录
	 * @param sceneDirectoryId
	 * @param pageName
	 * @param siteId
	 * @param themeId
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @return
	 * @throws ServiceException
	 */
	protected SceneTemplate getSceneTemplate(long sceneDirectoryId, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst) throws ServiceException {
		//获取直接为场景配置的模板
		String extraHqlWhere = "SceneTemplate.directoryId=" + sceneDirectoryId;
		SceneTemplate template = (SceneTemplate)retrieveTemplate(SceneTemplate.class.getName(), null, extraHqlWhere, null, true, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
		if(template!=null) {
			return template;
		}
		for(;;) {
			//获取上级目录ID
			String hql = "select SceneDirectory.parentDirectoryId" +
				  		 " from SceneDirectory SceneDirectory" +
				  		 " where SceneDirectory.id=" + sceneDirectoryId;
			Number parentDirectoryId = (Number)getDatabaseService().findRecordByHql(hql);
			if(parentDirectoryId==null) { //没有上级目录
				break;
			}
			//获取上级目录的模板
			extraHqlWhere = "SceneTemplate.directoryId=" + parentDirectoryId.longValue();
			template = (SceneTemplate)retrieveTemplate(SceneTemplate.class.getName(), null, extraHqlWhere, null, true, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
			if(template!=null) { //上级目录有配置的模板
				if(pageName.equals("sceneContent")) { //服务内容页面
					return template;
				}
				//场景页面,检查是否配置了场景选项列表,如果没有配置,继续向上查找
				HTMLDocument document = getTemplateHTMLDocument(template.getId(), template.getSiteId(), false, null);
				NodeList anchors = document.getElementsByTagName("a");
				for(int i=anchors==null ? -1 : anchors.getLength()-1; i>=0; i--) {
					HTMLAnchorElement anchor = (HTMLAnchorElement)anchors.item(i);
					if("recordList".equals(anchor.getId())) {
						String urn = anchor.getAttribute("urn");
						if(urn!=null && urn.indexOf("sceneDirectories")!=-1) {
							return template;
						}
					}
				}
			}
			//继续获取上级目录
			sceneDirectoryId = parentDirectoryId.longValue();
		}
		//没有针对当前场景服务的模板,获取站点的模板
		extraHqlWhere = "SceneTemplate.directoryId=0";
		return (SceneTemplate)retrieveTemplate(SceneTemplate.class.getName(), null, extraHqlWhere, null, false, null, pageName, siteId, themeId, themeType, false, pageWidth, flashSupport, temporaryOpeningFirst);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.spring.TemplateServiceImpl#listCongenerTemplates(com.yuanluesoft.cms.templatemanage.pojo.Template)
	 */
	protected List listCongenerTemplates(Template template, String themeIds) throws ServiceException {
		SceneTemplate sceneTemplate = (SceneTemplate)template;
		//获取同一个站点的模板列表
		String hql = "from SceneTemplate SceneTemplate" +
					 " where SceneTemplate.themeId in (" + JdbcUtils.validateInClauseNumbers(themeIds) + ")" +
					 " and SceneTemplate.applicationName='" + JdbcUtils.resetQuot(template.getApplicationName()) + "'" +
					 " and SceneTemplate.pageName='" + JdbcUtils.resetQuot(sceneTemplate.getPageName()) + "'" +
					 " and SceneTemplate.siteId=" + sceneTemplate.getSiteId() +
					 " and SceneTemplate.directoryId=" + sceneTemplate.getDirectoryId() +
					 " and SceneTemplate.id!=" + template.getId();
		return getDatabaseService().findRecordsByHql(hql);
	}
}
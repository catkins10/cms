/*
 * Created on 2007-7-7
 *
 */
package com.yuanluesoft.cms.templatemanage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.templatemanage.model.NormalTemplate;
import com.yuanluesoft.cms.templatemanage.pojo.CssFile;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface TemplateService extends BusinessService {
	
	/**
	 * 获取模板
	 * @param applicationName
	 * @param pageName
	 * @param siteId
	 * @param themeId 指定的主题ID,0为不指定,如:触摸屏可以绑定主题
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getTemplateHTMLDocument(String applicationName, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 按ID获取模板
	 * @param templateId
	 * @param siteId 
	 * @param editMode
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getTemplateHTMLDocument(long templateId, long siteId, boolean editMode, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 按模版路径获取模版
	 * @param templatePath
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getTemplateHTMLDocument(String templatePath) throws ServiceException;

	/**
	 * 获取初始化的模版
	 * @param themeId
	 * @param siteId
	 * @param editMode
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getInitializeTemplateHTMLDocument(long themeId, long siteId, boolean editMode, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 获取系统预置的模板
	 * @param applicationName
	 * @param pageName
	 * @param themeType
	 * @return
	 * @throws ServiceException
	 */
	public NormalTemplate getNormalTemplateHTMLDocument(String applicationName, String pageName, int themeType) throws ServiceException;
	
	/**
	 * 获取预置子页面HTML
	 * @param applicationName
	 * @param subPageName
	 * @param themeType
	 * @return
	 * @throws ServiceException
	 */
	public String getSubPageHTML(String applicationName, String subPageName, int themeType) throws ServiceException;
	
	/**
	 * 获取预置表单HTML
	 * @param applicationName
	 * @param formName
	 * @param themeType
	 * @return
	 * @throws ServiceException
	 */
	public String getNormalFormHTML(String applicationName, String formName, int themeType) throws ServiceException;
	
	/**
	 * 获取子模板(Template)列表
	 * @param siteId
	 * @param themeId
	 * @return
	 * @throws ServiceException
	 */
	public List listSubTemplates(long siteId, long themeId) throws ServiceException;
	
	/**
	 * 保存模板HTML
	 * @param templateId
	 * @param html
	 * @param request
	 * @throws ServiceException
	 */
	public void saveTemplateHTML(long templateId, String html, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 还原模板HTML
	 * @param templateId
	 * @throws ServiceException
	 */
	public void restoreTemplateHTML(long templateId) throws ServiceException;
	
	/**
	 * 上传模板
	 * @param templateId
	 * @throws ServiceException
	 */
	public void uploadTemplate(long templateId) throws ServiceException;
	
	/**
	 * 导出模板
	 * @param request
	 * @param response
	 * @param templateId
	 * @throws ServiceException
	 */
	public void exportTemplate(HttpServletRequest request, HttpServletResponse response, long templateId) throws ServiceException;
	
	/**
	 * 设置为默认模板
	 * @param templateId
	 * @throws ServiceException
	 */
	public void setDefaultTemplate(long templateId) throws ServiceException;
	
	/**
	 * 获取模板
	 * @param templateId
	 * @return
	 * @throws ServiceException
	 */
	public Template getTemplate(long templateId) throws ServiceException;
	
	/**
	 * 加载系统预置模板
	 * @param applicationName
	 * @param pageName
	 * @param templateId
	 * @throws ServiceException
	 */
	public void loadNormalTemplate(String applicationName, String pageName, long templateId) throws ServiceException;
	
	/**
	 * 拷贝模板
	 * @param toTemplateId
	 * @param fromTemplateId
	 * @throws ServiceException
	 */
	public void copyTemplate(long toTemplateId, long fromTemplateId) throws ServiceException;
	
	/**
	 * 加载CSS文件
	 * @param cssUrl
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public CssFile loadCssFile(String cssUrl) throws ServiceException;
	
	/**
	 * 获取CSS文本
	 * @param cssUrl
	 * @return
	 * @throws ServiceException
	 */
	public String loadCssText(String cssUrl) throws ServiceException;
	
	/**
	 * 保存CSS
	 * @param cssFile
	 * @throws ServiceException
	 */
	public void saveCssFile(long id, String cssName, String cssUrl, String fromCssFile, long siteId, String cssText) throws ServiceException;
	
	/**
	 * 删除CSS
	 * @param cssFileId
	 * @throws ServiceException
	 */
	public void deleteCssFile(long cssFileId) throws ServiceException;
	
	/**
	 * 清空缓存的模板
	 * @throws ServiceException
	 */
	public void clearCachedTemplate() throws ServiceException;
	
	/**
	 * 批量复制模板
	 * @param sourceTemplateId
	 * @param batchCopyPageIds
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void batchCopyTemplate(long sourceTemplateId, String targetPageNames, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 模板变动后,重建静态页面
	 * @param currentTemplate
	 * @param referenceTemplate
	 * @throws ServiceException
	 */
	public void rebuildStaticPage(Template currentTemplate, Template referenceTemplate, long siteId) throws ServiceException;
}
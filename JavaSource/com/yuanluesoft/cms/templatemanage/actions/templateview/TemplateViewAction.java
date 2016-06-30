/*
 * Created on 2007-7-5
 *
 */
package com.yuanluesoft.cms.templatemanage.actions.templateview;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteApplication;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteTemplateView;
import com.yuanluesoft.cms.sitemanage.pojo.SiteTemplateTheme;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService;
import com.yuanluesoft.cms.templatemanage.forms.TemplateView;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.eai.client.EAIClient;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;

/**
 *
 * @author LinChuan
 *
 */
public class TemplateViewAction extends ViewFormAction {

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		TemplateView templateView = (TemplateView)viewForm;
		//获取站点应用
		SiteApplication siteApplication = getSiteApplication(templateView, request);
		//获取页面
		SitePage sitePage = getSitePage(templateView, request);
		if(sitePage.getTemplateView()==null && (siteApplication==null || siteApplication.getTemplateView()==null)) { //没有配置模板视图
			return "cms/templatemanage";
		}
		else {
			SiteTemplateView siteTemplateView = (sitePage.getTemplateView()==null ? siteApplication.getTemplateView() : sitePage.getTemplateView());
			return (siteTemplateView.getViewApplication()==null ? siteApplication.getName() : siteTemplateView.getViewApplication());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		TemplateView templateView = (TemplateView)viewForm;
		//获取站点应用
		SiteApplication siteApplication = getSiteApplication(templateView, request);
		//获取页面
		SitePage sitePage = getSitePage(templateView, request);
		if(sitePage.getTemplateView()==null && (siteApplication==null || siteApplication.getTemplateView()==null)) { //没有配置模板视图
			return "template";
		}
		else {
			SiteTemplateView siteTemplateView = (sitePage.getTemplateView()==null ? siteApplication.getTemplateView() : sitePage.getTemplateView());
			return siteTemplateView.getViewName();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		TemplateView templateView = (TemplateView)viewForm;
		String pojoName = view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf(".") + 1);
		String where =  pojoName + ".applicationName='" + JdbcUtils.resetQuot(templateView.getApplicationName()) + "'" +
						" and " + pojoName + ".pageName='" + JdbcUtils.resetQuot(templateView.getPageName()) + "'" +
						" and " + pojoName + ".siteId=" + templateView.getSiteId() +
						" and " + pojoName + ".themeId=" + templateView.getThemeId();
		view.addWhere(where);
		
		if(view.getActions()==null) {
			view.setActions(new ArrayList());
		}
		
		//如果没有主题,删除原有的视图按钮
		if(templateView.getTheme()==null) {
			view.getActions().clear();
		}
		else if(ListUtils.findObjectByProperty(view.getActions(), "title", "新建模板")==null) {
			ViewAction viewAction = new ViewAction();
			viewAction.setTitle("新建模板");
			viewAction.setExecute("PageUtils.openurl('" + request.getContextPath() + "" +
								  "/" + view.getApplicationName() + "/" + view.getForm() + ".shtml" +
								  "?act=create" +
								  "&applicationName=" + templateView.getApplicationName() +
								  "&pageName=" + templateView.getPageName() +
								  "&themeId=" + templateView.getTheme().getId() +
								  "&siteId=" + templateView.getSiteId() + "'," +
								  "'mode=fullscreen'," +
								  "'template')");
			view.getActions().add(viewAction);
		}
		//添加"切换主题"按钮
		SiteService siteService = (SiteService)getService("siteService");
		ViewAction viewAction = new ViewAction();
		viewAction.setTitle("切换主题");
		long parentSiteId = siteService.getParentSite(templateView.getSiteId()).getId();
		String script = "location.href='" +
						request.getContextPath() + "/cms/templatemanage/templateView.shtml" +
						"?siteId=" + templateView.getSiteId() +
						"&applicationName=" + templateView.getApplicationName() +
						"&pageName=" + templateView.getPageName() +
						"&themeId={id}'";
		viewAction.setExecute("DialogUtils.openDialog('" + request.getContextPath() +
							  "/cms/sitemanage/selectSiteTemplateTheme.shtml" +
							  "?pageSiteId=" + parentSiteId +
							  "&script=" + URLEncoder.encode(script, "utf-8") + "'," +
							  "600, 400)");
		view.getActions().add(viewAction);
		
		viewAction = new ViewAction();
	    viewAction.setTitle("浏览主页");
	    viewAction.setExecute("window.open('" + Environment.getContextPath() + "/cms/sitemanage/index.shtml?siteId=" + templateView.getSiteId() + "')");
	    view.getActions().add(viewAction);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		TemplateView templateView = (TemplateView)viewForm;
		SiteService siteService = (SiteService)getService("siteService");
		location.clear();
		String[] names = siteService.getDirectoryFullName(templateView.getSiteId(), "/", "site").split("/");
		for(int i=0; i<names.length; i++) {
			location.add(names[i]);
		}
		location.add("模板配置" + (templateView.getTheme()==null ? "" : "[" + templateView.getTheme().getName() + "]"));
		String applicationTitle = ((EAIClient)getService("eaiClient")).getApplicationTitle(((SiteApplication)request.getAttribute("siteApplication")).getName());
		location.add(applicationTitle); //应用名称
    	location.add(((SitePage)request.getAttribute("sitePage")).getTitle()); //表单名称
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.initForm(viewForm, request, sessionInfo);
		PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
		TemplateView templateView = (TemplateView)viewForm;
		SiteService siteService = (SiteService)getService("siteService");
		//获取用户对站点的权限
		if(!siteService.checkPopedom(templateView.getSiteId(), "manager", sessionInfo)) {
			throw new PrivilegeException();
		}
		WebDirectory directory = (WebDirectory)siteService.getDirectory(templateView.getSiteId());
		//设置页面目录树
		templateView.setSitePageTree(pageDefineService.createPageTree(templateView.getApplicationName(), true, !(directory instanceof WebSite), false, false, false, sessionInfo));
		//设置模板主题
		SiteTemplateThemeService siteTemplateThemeService = (SiteTemplateThemeService)getService("siteTemplateThemeService");
		if(templateView.getThemeId()>0) {
			templateView.setTheme((SiteTemplateTheme)siteTemplateThemeService.load(SiteTemplateTheme.class, templateView.getThemeId()));
		}
		else {
			templateView.setTheme(siteTemplateThemeService.getDefaultTheme(templateView.getSiteId(), TemplateThemeService.THEME_TYPE_COMPUTER, -1, -1));
			if(templateView.getTheme()!=null) {
				templateView.setThemeId(templateView.getTheme().getId());
			}
		}
	}

	/**
	 * 获取站点应用
	 * @param templateView
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private SiteApplication getSiteApplication(TemplateView templateView, HttpServletRequest request) {
		SiteApplication siteApplication = (SiteApplication)request.getAttribute("siteApplication");
		if(siteApplication!=null) {
			return siteApplication;
		}
		try {
			PageDefineService pageDefineService = (PageDefineService)getService("pageDefineService");
			//获取站点应用
			siteApplication = pageDefineService.getSiteApplication(templateView.getApplicationName());
		}
		catch (Exception e) {
			
		}
		//设置属性
		request.setAttribute("siteApplication", siteApplication);
		return siteApplication;
	}
	
	/**
	 * 获取站点页面
	 * @param templateView
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private SitePage getSitePage(TemplateView templateView, HttpServletRequest request) {
		SitePage sitePage = (SitePage)request.getAttribute("sitePage");
		if(sitePage!=null) {
			return sitePage;
		}
		SiteApplication siteApplication = getSiteApplication(templateView, request);
		//获取页面
		sitePage = (SitePage)ListUtils.findObjectByProperty(siteApplication.getPages(), "name", templateView.getPageName());
		//设置属性
		request.setAttribute("sitePage", sitePage);
		return sitePage;
	}
}
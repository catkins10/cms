package com.yuanluesoft.cms.sitemanage.actions.siteapplicationconfigview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.forms.SiteApplicationConfigView;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class SiteApplicationConfigViewAction extends ViewFormAction {

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		SiteApplicationConfigView configView = (SiteApplicationConfigView)viewForm;
		boolean shareView = configView.getViewApplicationName()!=null && !configView.getViewApplicationName().equals(""); //是否共享视图
		return shareView ? configView.getViewApplicationName() : configView.getApplicationName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		SiteApplicationConfigView configView = (SiteApplicationConfigView)viewForm;
		return configView.getViewName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		SiteApplicationConfigView configView = (SiteApplicationConfigView)viewForm;
		boolean shareView = configView.getViewApplicationName()!=null && !configView.getViewApplicationName().equals(""); //是否共享视图
		String pojoName = view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf(".") + 1);
		if(!configView.isShowChildSiteData()) { //不显示子站数据
			view.addWhere(pojoName + ".siteId=" + configView.getSiteId());
		}
		else if(configView.getSiteId()>0) { //显示子站数据,且站点ID>0
			SiteService siteService = (SiteService)getService("siteService");
			view.addWhere(pojoName + ".siteId in (" + siteService.getAllChildDirectoryIds("" + configView.getSiteId(), "site") + ")");
		}
    	if(shareView) { //视图是共享的
    		view.addWhere(pojoName + ".applicationName='" + JdbcUtils.resetQuot(configView.getApplicationName()) + "'"); //添加应用过滤
    	}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		SiteApplicationConfigView configView = (SiteApplicationConfigView)viewForm;
		SiteService siteService = (SiteService)getService("siteService");
		//用户不是当前站点的管理员
		WebSite currentSite = (WebSite)siteService.getDirectory(configView.getSiteId());	
		location.add("站点：" + currentSite.getDirectoryName()); //添加当前站点的名称
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.initForm(viewForm, request, sessionInfo);
		SiteApplicationConfigView configView = (SiteApplicationConfigView)viewForm;
		List acl = getAcl(configView.getApplicationName(), sessionInfo);
		SiteService siteService = (SiteService)getService("siteService");
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //应用程序管理员
			//创建站点目录树
			configView.setSiteTree(siteService.createDirectoryTree(0, "site", null, null, null, sessionInfo)); //不过滤
			return;
		}
		WebSite currentSite = null;
		if(!siteService.checkPopedom(configView.getSiteId(), "manager,editor", sessionInfo)) { //用户不是当前站点的管理员、编辑
			currentSite = siteService.getFirstManagedSite(sessionInfo); //获取用户有管理权限的第一个站点
			if(currentSite==null) {
				currentSite = siteService.getFirstEditabledSite(sessionInfo); //获取用户有编辑权限的第一个站点
			}
			if(currentSite!=null) {
				configView.setSiteId(currentSite.getId());
			}
		}
		//创建站点目录树
		configView.setSiteTree(siteService.createDirectoryTree(0, "site", "manager,editor", null, null, sessionInfo));
	}
}
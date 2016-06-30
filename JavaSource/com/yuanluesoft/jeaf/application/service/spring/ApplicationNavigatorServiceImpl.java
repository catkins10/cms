/*
 * Created on 2005-9-19
 *
 */
package com.yuanluesoft.jeaf.application.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.application.model.navigator.definition.Link;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ViewLink;
import com.yuanluesoft.jeaf.application.parser.ApplicationNavigatorDefinitionParser;
import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.formula.service.FormulaService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationNavigatorServiceImpl implements ApplicationNavigatorService {
	private AccessControlService accessControlService; //权限服务
	private ViewDefineService viewDefineService; //视图定义服务
	private FormulaService formulaService; //公式服务
	private Cache cache; //导航定义缓存
	private ApplicationNavigatorDefinitionParser applicationNavigatorDefinitionParser; //应用导航定义解析器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#getApplicationNavigator(java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ApplicationNavigator getApplicationNavigator(String applicationName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取导航栏配置
		ApplicationNavigatorDefinition navigatorDefinition = getApplicationNavigatorDefinition(applicationName);
		if(navigatorDefinition==null) {
			return null;
		}
		ApplicationNavigator applicationNavigator = new ApplicationNavigator();
		if(navigatorDefinition.getNavigatorURL()!=null) { //自定义导航地址
			applicationNavigator.setRedirect(navigatorDefinition.getNavigatorURL());
			return applicationNavigator;
		}
		if(navigatorDefinition.getNavigatorServiceName()!=null) { //自定义导航服务
			return ((ApplicationNavigatorService)Environment.getService(navigatorDefinition.getNavigatorServiceName())).getApplicationNavigator(applicationName, request, sessionInfo);
		}
    	//根据权限调整系统定义
		List acl = accessControlService.getAcl(applicationName, sessionInfo);
		if(acl==null || !acl.contains("application_visitor")) { //当前用户没有系统访问权限
			return null;
		}
		List links = new ArrayList();
		//根据用户权限调整链接列表
		boolean selected = false;
		String currentViewName = request.getParameter("viewName");
		for(Iterator iterator = navigatorDefinition.getLinks().iterator(); iterator.hasNext();) {
			Object link = iterator.next();
			if(link instanceof Link) {
				Link linkDefinition = (Link)link;
				if(!formulaService.checkCondition(linkDefinition.getHideCondition(), null, applicationName, null, acl, sessionInfo)) {
					links.add(new com.yuanluesoft.jeaf.application.model.navigator.Link(linkDefinition.getTitle(), linkDefinition.getHref(), linkDefinition.getTarget(), Environment.getWebApplicationUrl() + (linkDefinition.getIconURL()==null ? "/jeaf/application/icons/link.gif" : linkDefinition.getIconURL()), false, false));
				}
			}
			else if(link instanceof ViewLink) {
				ViewLink viewLink = (ViewLink)link;
				View view = null;
				try {
					if(viewLink.getLocal()!=null) {
						view = viewDefineService.getView(applicationName, viewLink.getLocal(), sessionInfo);
					}
					else {
						view = viewDefineService.getView(viewLink.getApplication(), viewLink.getView(), sessionInfo);
					}
					//检查加载权限
					ViewService viewService = (ViewService)Environment.getService(view.getViewServiceName()==null ? "viewService" : view.getViewServiceName());
					if(!viewService.checkLoadPrivilege(view, request, sessionInfo)) {
						continue;
					}
					boolean viewSelected = viewLink.getView().equals(currentViewName);
					if(viewSelected) {
						selected = true;
					}
					links.add(new com.yuanluesoft.jeaf.application.model.navigator.Link(viewLink.getTitle(), ViewUtils.getViewURL(view, request), null, Environment.getWebApplicationUrl() + (viewLink.getIconURL()==null ? "/jeaf/application/icons/view.gif" : viewLink.getIconURL()), true, viewSelected));
				}
				catch(Exception e) {
					
				}
			}
		}
		if(!selected) { //没有视图被选中
			for(Iterator iterator = links.iterator(); iterator.hasNext();) {
				com.yuanluesoft.jeaf.application.model.navigator.Link link = (com.yuanluesoft.jeaf.application.model.navigator.Link)iterator.next();
				if(link.getTarget()==null || link.getTarget().equals("")) { //没有指定目标窗口
					link.setSelected(true);
					break;
				}
			}
		}
		applicationNavigator.setLinks(links);
    	return applicationNavigator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#listApplicationNavigatorTreeNodes(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listApplicationNavigatorTreeNodes(String applicationName, String parentTreeNodeId, SessionInfo sessionInfo) throws ServiceException {
		//获取导航栏配置
		ApplicationNavigatorDefinition navigatorDefinition = getApplicationNavigatorDefinition(applicationName);
		return ((ApplicationNavigatorService)Environment.getService(navigatorDefinition.getNavigatorServiceName())).listApplicationNavigatorTreeNodes(applicationName, parentTreeNodeId, sessionInfo);
	}
	
	/**
	 * 获取应用导航定义
	 * @param applicationPath
	 * @return
	 * @throws ServiceException
	 */
	private ApplicationNavigatorDefinition getApplicationNavigatorDefinition(String applicationName) throws ServiceException {
		ApplicationNavigatorDefinition navigatorDefinition;
    	try {
    		navigatorDefinition = (ApplicationNavigatorDefinition)cache.get("applicationNavigator." + applicationName);
			if(navigatorDefinition==null) {
				navigatorDefinition = applicationNavigatorDefinitionParser.parse(Environment.getWebinfPath() + applicationName + "/navigator-config.xml");
				cache.put("applicationNavigator." + applicationName, navigatorDefinition);
			}
			return (ApplicationNavigatorDefinition)navigatorDefinition.clone(); //克隆
		}
    	catch (Exception e) {
    		if(!FileUtils.isExists(Environment.getWebinfPath() + applicationName + "/navigator-config.xml")) {
    			return null;
    		}
			throw new ServiceException(e);
		}
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#saveApplicationNavigatorDefinition(java.lang.String, com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition)
	 */
	public void saveApplicationNavigatorDefinition(String applicationName, ApplicationNavigatorDefinition navigatorDefinition) throws ServiceException {
		try {
			applicationNavigatorDefinitionParser.saveApplicationNavigatorDefinition(navigatorDefinition, FileUtils.createDirectory(Environment.getWebinfPath() + applicationName) + "navigator-config.xml");
		}
		catch (ParseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * @return Returns the accessControlService.
	 */
	public AccessControlService getAccessControlService() {
		return accessControlService;
	}
	/**
	 * @param accessControlService The accessControlService to set.
	 */
	public void setAccessControlService(
			AccessControlService accessControlService) {
		this.accessControlService = accessControlService;
	}
	/**
	 * @return Returns the viewDefineService.
	 */
	public ViewDefineService getViewDefineService() {
		return viewDefineService;
	}
	/**
	 * @param viewDefineService The viewDefineService to set.
	 */
	public void setViewDefineService(ViewDefineService viewDefineService) {
		this.viewDefineService = viewDefineService;
	}

	/**
	 * @return the formulaService
	 */
	public FormulaService getFormulaService() {
		return formulaService;
	}

	/**
	 * @param formulaService the formulaService to set
	 */
	public void setFormulaService(FormulaService formulaService) {
		this.formulaService = formulaService;
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * @return the applicationNavigatorDefinitionParser
	 */
	public ApplicationNavigatorDefinitionParser getApplicationNavigatorDefinitionParser() {
		return applicationNavigatorDefinitionParser;
	}

	/**
	 * @param applicationNavigatorDefinitionParser the applicationNavigatorDefinitionParser to set
	 */
	public void setApplicationNavigatorDefinitionParser(
			ApplicationNavigatorDefinitionParser applicationNavigatorDefinitionParser) {
		this.applicationNavigatorDefinitionParser = applicationNavigatorDefinitionParser;
	}
}
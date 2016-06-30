/*
 * Created on 2005-10-28
 *
 */
package com.yuanluesoft.jeaf.view.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.formula.service.FormulaService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.calendarview.model.CalendarView;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewActionGroup;
import com.yuanluesoft.jeaf.view.parser.ViewDefineParser;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;

/**
 *
 * @author linchuan
 *
 */
public class ViewDefineServiceImpl implements ViewDefineService {
	private Cache cache; //缓存
	private ViewDefineParser viewDefineParser; //视图定义解析器
	private FormulaService formulaService; //公式服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.ViewDefineService#getView(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public View getView(String applicationName, String viewName, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		View view = (View)ListUtils.findObjectByProperty(listOriginalViews(applicationName), "name", viewName);
		if(view==null) {
			throw new ServiceException("view " + viewName + " of " + applicationName + " not found");
		}
		//检查用户权限
		if(sessionInfo!=null && formulaService.checkCondition(view.getHideCondition(), null, view.getApplicationName(), null, null, sessionInfo)) {
			throw new PrivilegeException();
		}
	    try {
	    	view = (View)view.clone();
		}
	    catch (CloneNotSupportedException e) {
		    Logger.exception(e);
			throw new ServiceException("view clone exception");
		}
	    checkActionHideCondition(view, sessionInfo); //检查操作的隐藏条件
		return view;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.ViewDefineService#listViews(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listViews(String applicationName, SessionInfo sessionInfo) throws ServiceException {
		List originalViews = listOriginalViews(applicationName);
		if(originalViews==null) {
			return originalViews;
		}
		ArrayList views = new ArrayList();
		for(Iterator iterator = originalViews.iterator(); iterator.hasNext();) {
			View view = (View)iterator.next();
			//检查用户权限
			if(sessionInfo!=null && formulaService.checkCondition(view.getHideCondition(), null, view.getApplicationName(), null, null, sessionInfo)) {
				continue;
			}
			try {
				view = (View)view.clone(); //克隆
				checkActionHideCondition(view, sessionInfo); //检查操作的隐藏条件
				views.add(view);
			} 
			catch (CloneNotSupportedException e) {
				
			}
		}
		return views;
	}
	
	/**
	 * 获取视图定义
	 * @param applicationName
	 * @return
	 * @throws ServiceException
	 */
	private List listOriginalViews(String applicationName) throws ServiceException {
		try {
			List views = (List)cache.get("viewDefine." + applicationName);
    		if(views==null) {
				try {
					views = viewDefineParser.parse(applicationName, Environment.getWebinfPath() + applicationName + "/view-config.xml");
				}
				catch (Exception e) {
					return null;
				}
				cache.put("viewDefine." + applicationName, views);
			}
    		return views;
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException("cache exception");
		}
	}
	
	/**
	 * 检查操作的隐藏条件
	 * @param view
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void checkActionHideCondition(View view, SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null) {
			return;
		}
		if(view instanceof CalendarView) {
            CalendarView calendarView = (CalendarView)view;
            if(formulaService.checkCondition(calendarView.getCalendarActionHideCondition(), null, view.getApplicationName(), null, null, sessionInfo)) {
                calendarView.setCalendarAction(null);
            }
        }
		if(view.getActions()==null) {
    		return;
    	}
    	for(int i=view.getActions().size()-1; i>=0; i--) {
    	    Object viewAction = view.getActions().get(i);
    	    if(viewAction instanceof ViewAction) {
	    	    if(formulaService.checkCondition(((ViewAction)viewAction).getHideCondition(), null, view.getApplicationName(), null, null, sessionInfo)) {
	    	    	view.getActions().remove(i);
	    	    }
    	    }
    	    else if(viewAction instanceof ViewActionGroup) { //操作分组
    	    	ViewActionGroup actionGroup = (ViewActionGroup)viewAction;
    	    	for(Iterator iteratorGroup = actionGroup.getActions().iterator(); iteratorGroup.hasNext();) {
    	    		ViewAction action = (ViewAction)iteratorGroup.next();
    	    		if(formulaService.checkCondition(action.getHideCondition(), null, view.getApplicationName(), null, null, sessionInfo)) {
    	    			iteratorGroup.remove();
    	    	    }
    	    	}
    	    	if(actionGroup.getActions().isEmpty()) {
    	    		view.getActions().remove(i);
    	    	}
    	    }
    	}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.ViewDefineService#saveViewDefine(java.lang.String, java.util.List)
	 */
	public void saveViewDefine(String applicationName, List views) throws ServiceException {
		try {
			viewDefineParser.saveViewDefine(views, FileUtils.createDirectory(Environment.getWebinfPath() + applicationName) + "view-config.xml");
		} 
		catch (ParseException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * @return Returns the cache.
	 */
	public Cache getCache() {
		return cache;
	}
	/**
	 * @param cache The cache to set.
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	/**
	 * @return Returns the viewDefineParser.
	 */
	public ViewDefineParser getViewDefineParser() {
		return viewDefineParser;
	}
	/**
	 * @param viewDefineParser The viewDefineParser to set.
	 */
	public void setViewDefineParser(ViewDefineParser viewDefineParser) {
		this.viewDefineParser = viewDefineParser;
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
}
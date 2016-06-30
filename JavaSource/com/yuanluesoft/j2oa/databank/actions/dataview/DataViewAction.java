/*
 * Created on 2006-4-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.j2oa.databank.actions.dataview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.databank.forms.DataView;
import com.yuanluesoft.j2oa.databank.service.DatabankDirectoryService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;

/**
 *
 * @author linchuan
 *
 */
public class DataViewAction extends ViewFormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "j2oa/databank";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "data";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		DataView dataViewForm = (DataView)viewForm;
		DatabankDirectoryService databankDirectoryService = (DatabankDirectoryService)getService("databankDirectoryService");
		//获取用户对目录的权限
		List popedoms = databankDirectoryService.listDirectoryPopedoms(dataViewForm.getDirectoryId(), sessionInfo);
		if(popedoms==null) {
			popedoms = new ArrayList();
		}
		//设置数据访问条件
		String where = generateHqlWhere(popedoms, dataViewForm.getDirectoryId(), view, sessionInfo);
		view.addWhere(where);
		
		//设置操作列表
		List actions = new ArrayList();
		if(popedoms.contains("manager")) {
			ViewAction viewAction = new ViewAction();
    	    viewAction.setTitle("添加资料");
    	    viewAction.setExecute("PageUtils.openurl('data.shtml?act=create&directoryId=" + dataViewForm.getDirectoryId() + "', 'width=780,height=550', 'data')");
    	    actions.add(viewAction);

    	    //检查用户是否上级目录的管理员
	       viewAction = new ViewAction();
			viewAction.setTitle("修改目录");
			viewAction.setExecute("PageUtils.openurl('directory.shtml?act=edit&id=" + dataViewForm.getDirectoryId() + "', 'width=760,height=520', '" + dataViewForm.getDirectoryId() + "');");
			actions.add(viewAction);
	    	
			viewAction = new ViewAction();
			viewAction.setTitle("新建子目录");
			viewAction.setExecute("PageUtils.openurl('directory.shtml?parentDirectoryId=" + dataViewForm.getDirectoryId() + "', 'width=760,height=520', 'directory');");
			actions.add(viewAction);
			
			viewAction = new ViewAction();
			viewAction.setTitle("调整子目录优先级");
			viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/j2oa/databank/adjustDirectoryPriority.shtml?parentDirectoryId=" + dataViewForm.getDirectoryId() + "', 640, 400)");
			actions.add(viewAction);
			
			if(dataViewForm.getDirectoryId()==0) { //根目录
				viewAction = new ViewAction();
				viewAction.setTitle("目录导入");
				viewAction.setExecute("PageUtils.openurl('import.shtml', 'width=760,height=520', 'import');");
				actions.add(viewAction);
			}
		}
		List viewActions = view.getActions();
		if(viewActions!=null && !viewActions.isEmpty()) {
			actions.addAll(viewActions);
		}
		view.setActions(actions);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		DataView dataViewForm = (DataView)viewForm;
		DatabankDirectoryService databankDirectoryService = (DatabankDirectoryService)getService("databankDirectoryService");
		location.remove(location.size()-1);
		String fullName = databankDirectoryService.getDirectoryFullName(dataViewForm.getDirectoryId(), "/", null);
		if(fullName!=null) {
			String[] names = fullName.split("/");
			for(int i=0; i<names.length; i++) {
				location.add(names[i]);
			}
		}
	}
	
	/**
	 * 生成WHERE子句
	 * @param directoryPopedoms
	 * @param directoryId
	 * @param view
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	private String generateHqlWhere(List directoryPopedoms, long directoryId, View view, SessionInfo sessionInfo) throws Exception {
		if(directoryPopedoms.contains("manager") && directoryId==0) { //管理员,且是根目录,显示全部
			return null;
		}
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		
		//获取全部子目录ID列表
		String hql = "select DatabankDirectorySubjection.directoryId" +
				    " from DatabankDirectorySubjection DatabankDirectorySubjection" +
				    " where DatabankDirectorySubjection.parentDirectoryId=" +  directoryId +
				    " order by DatabankDirectorySubjection.directoryId";
		List directoryIds = databaseService.findRecordsByHql(hql, 0, 1000); //oracle in 最大1000个值
		if(directoryIds==null) {
			directoryIds = new ArrayList();
		}
		if(directoryPopedoms.contains("manager")) { //管理员
			return "DatabankData.directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(directoryIds, ",", false)) + ")";
		}
		
		//获取用户有访问权限的子目录ID列表
		List popedomDirectoryIds = null;
		hql = "select distinct DatabankDirectorySubjection.directoryId" +
			  " from DatabankDirectorySubjection DatabankDirectorySubjection, DatabankDirectoryPopedom DatabankDirectoryPopedom" +
			  " where DatabankDirectorySubjection.parentDirectoryId=" +  directoryId +
			  " and DatabankDirectorySubjection.directoryId=DatabankDirectoryPopedom.directoryId" +
			  " and DatabankDirectoryPopedom.userId in (" + sessionInfo.getUserIds() + ")" +
			  " order by DatabankDirectorySubjection.directoryId";
		popedomDirectoryIds = databaseService.findRecordsByHql(hql, 0, 1000);
		if(popedomDirectoryIds==null) {
			popedomDirectoryIds = new ArrayList();
		}
		//获取用户没有有访问权限的子目录ID列表
		List noPopedomDirectoryIds;
		if(popedomDirectoryIds.isEmpty()) {
			noPopedomDirectoryIds = directoryIds;
		}
		else {
			noPopedomDirectoryIds = new ArrayList();
			for(Iterator iterator = directoryIds.iterator(); iterator.hasNext();) {
				Object id = iterator.next();
				if(popedomDirectoryIds.indexOf(id)==-1) {
					noPopedomDirectoryIds.add(id);
				}
			}
		}
		
		String where = (popedomDirectoryIds.isEmpty() ? null : "DatabankData.directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(popedomDirectoryIds, ",", false)) + ")");
		if(!noPopedomDirectoryIds.isEmpty()) {
			//有特定文件访问权限的文件
			where = (where==null ? "" : where + " or ") +
					"(DatabankData.directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(noPopedomDirectoryIds, ",", false)) + ")" +
					" and visitors.visitorId in (" + sessionInfo.getUserIds() + "))";
		}
		return where;
	}
}
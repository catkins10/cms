package com.yuanluesoft.enterprise.iso.actions.documentview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.iso.forms.DocumentView;
import com.yuanluesoft.enterprise.iso.service.IsoDirectoryService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;

/**
 * 
 * @author linchuan
 * 
 */
public class DocumentViewAction extends ViewFormAction {
     
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "enterprise/iso";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "document";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		DocumentView documentViewForm = (DocumentView)viewForm;
		IsoDirectoryService isoDirectoryService = (IsoDirectoryService)getService("isoDirectoryService");
		//获取用户对目录的权限
		List popedoms = isoDirectoryService.listDirectoryPopedoms(documentViewForm.getDirectoryId(), sessionInfo);
		if(popedoms==null) {
			popedoms = new ArrayList();
		}
		//设置数据访问条件
		String where = generateHqlWhere(popedoms, documentViewForm.getDirectoryId(), view, sessionInfo);
		view.addWhere(where);
		//设置操作列表
		List actions = new ArrayList();

		//获取文件创建流程ID
		String createWorkflowId = isoDirectoryService.getWorkflowId(documentViewForm.getDirectoryId(), "create");
		//获取流程入口
		WorkflowExploitService workflowExploitService = (WorkflowExploitService)getService("workflowExploitService");
		if(workflowExploitService.getWorkflowEntry(createWorkflowId, null, null, sessionInfo)!=null) { //有流程入口
			ViewAction viewAction = new ViewAction();
    	    viewAction.setTitle("添加文件");
    	    viewAction.setExecute("PageUtils.openurl('document.shtml?act=create&directoryId=" + documentViewForm.getDirectoryId() + "', 'width=780,height=550', 'document')");
    	    actions.add(viewAction);
		}
		
		if(popedoms.contains("manager")) {
			//检查用户是否上级目录的管理员
	    	ViewAction viewAction = new ViewAction();
			viewAction.setTitle("修改目录");
			viewAction.setExecute("PageUtils.openurl('directory.shtml?act=edit&id=" + documentViewForm.getDirectoryId() + "', 'mode=fullscreen', '" + documentViewForm.getDirectoryId() + "');");
			actions.add(viewAction);
			
    	    viewAction = new ViewAction();
			viewAction.setTitle("新建子目录");
			viewAction.setExecute("PageUtils.openurl('directory.shtml?parentDirectoryId=" + documentViewForm.getDirectoryId() + "', 'mode=fullscreen', 'directory');");
			actions.add(viewAction);
			
			viewAction = new ViewAction();
			viewAction.setTitle("调整子目录优先级");
			viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/enterprise/iso/adjustDirectoryPriority.shtml?parentDirectoryId=" + documentViewForm.getDirectoryId() + "', 640, 400)");
			actions.add(viewAction);
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
		IsoDirectoryService isoDirectoryService = (IsoDirectoryService)getService("isoDirectoryService");
		DocumentView documentViewForm = (DocumentView)viewForm;
		location.remove(location.size()-1);
		String fullName = isoDirectoryService.getDirectoryFullName(documentViewForm.getDirectoryId(), "/", null);
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
		//判断用户是否有访问全部文件的权限,即管理员、审核人、审批人
		boolean readAll = !directoryPopedoms.isEmpty() && (directoryPopedoms.size()>1 || !directoryPopedoms.contains("reader"));
		if(directoryId==0 && readAll) { //管理员,且是根目录,显示全部
			return null;
		}
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		
		//获取全部子目录ID列表
		String hql = "select IsoDirectorySubjection.directoryId" +
				     " from IsoDirectorySubjection IsoDirectorySubjection" +
				     " where IsoDirectorySubjection.parentDirectoryId=" +  directoryId +
				     " order by IsoDirectorySubjection.directoryId";
		List directoryIds = databaseService.findRecordsByHql(hql, 0, 1000); //oracle in 最大1000个值
		if(directoryIds==null) {
			directoryIds = new ArrayList();
		}
		if(readAll) { //管理员、审核人、审批人
			return "subjections.directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(directoryIds, ",", false)) + ")";
		}
		
		//获取用户有访问权限的子目录ID列表
		List popedomDirectoryIds = null;
		hql = "select distinct IsoDirectorySubjection.directoryId" +
			  " from IsoDirectorySubjection IsoDirectorySubjection, IsoDirectoryPopedom IsoDirectoryPopedom" +
			  " where IsoDirectorySubjection.parentDirectoryId=" +  directoryId +
			  " and IsoDirectorySubjection.directoryId=IsoDirectoryPopedom.directoryId" +
			  " and IsoDirectoryPopedom.userId in (" + sessionInfo.getUserIds() + ")" +
			  " order by IsoDirectorySubjection.directoryId";
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
		String where = (popedomDirectoryIds.isEmpty() ? null : "subjections.directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(popedomDirectoryIds, ",", false)) + ")");
		if(!noPopedomDirectoryIds.isEmpty()) {
			//有特定文件访问权限的文件
			where = (where==null ? "" : where + " or ") +
					"(subjections.directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(noPopedomDirectoryIds, ",", false)) + ")" +
					" and visitors.visitorId in (" + sessionInfo.getUserIds() + "))";
		}
		return where;
	}
}
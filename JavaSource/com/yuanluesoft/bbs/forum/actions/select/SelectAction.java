package com.yuanluesoft.bbs.forum.actions.select;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class SelectAction extends TreeDialogAction {
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryService(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected DirectoryService getDirectoryService(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws SystemUnregistException {
		return (ForumService)getService("forumService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		String directoryTypes = dialogForm.getSelectNodeTypes();
		if(directoryTypes==null || directoryTypes.equals("") || directoryTypes.equals("all") || directoryTypes.indexOf("forum")!=-1) {
    		directoryTypes = "bbs,category,forum";
    	}
   		else if(directoryTypes.indexOf("category")==-1 && directoryTypes.indexOf("bbs")==-1) {
   			directoryTypes += ",bbs";
    	}
    	boolean anonymous = sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName());
    	if(anonymous) {
    		directoryTypes = directoryTypes.replaceAll("bbs,", "").replaceAll(",bbs", ""); //匿名用户,不允许浏览其他论坛
    	}
    	return directoryTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getPopedomFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getPopedomFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "manager";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "论坛选择";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listChildren.shtml";
	}
}
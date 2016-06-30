package com.yuanluesoft.cms.onlineservice.actions.selectdirectory;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.forms.SelectDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SelectDirectoryAction extends TreeDialogAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryService(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected DirectoryService getDirectoryService(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws SystemUnregistException {
		return (DirectoryService)getService("onlineServiceDirectoryService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		String directoryTypes = dialogForm.getSelectNodeTypes();
		if(directoryTypes==null || directoryTypes.equals("") || directoryTypes.equals("all") || directoryTypes.indexOf("directory")!=-1) {
    		directoryTypes = "mainDirectory,directory";
    	}
    	boolean anonymous = sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName());
    	if(anonymous) {
    		directoryTypes = directoryTypes.replaceAll("mainDirectory,", "").replaceAll(",mainDirectory", ""); //匿名用户,不允许浏览其他主目录
    	}
		return directoryTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getOtherFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getOtherFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "OnlineServiceDirectory.halt!='1'";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "网上办事目录选择";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listChildDirectories.shtml";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名访问
			SelectDirectory selectForm = (SelectDirectory)dialog;
			if(selectForm.getParentNodeId()==null || selectForm.getParentNodeId().isEmpty()) { //没有指定父目录
				SiteService siteService = (SiteService)getService("siteService");
				OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
				WebSite site = siteService.getParentSite(RequestUtils.getParameterLongValue(request, "siteId"));
				OnlineServiceDirectory directory = onlineServiceDirectoryService.getDirectoryBySiteId(site.getId());
				if(directory!=null) {
					selectForm.setParentNodeId("" + directory.getId());
				}
			}
		}
		super.initDialog(dialog, sessionInfo, request);
	}
}
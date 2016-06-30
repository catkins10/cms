package com.yuanluesoft.cms.onlineservice.actions.selectserviceitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.actions.selectdirectory.SelectDirectoryAction;
import com.yuanluesoft.cms.onlineservice.forms.SelectServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemSubjection;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SelectServiceItemAction extends SelectDirectoryAction {
	
	public SelectServiceItemAction() {
		super();
		anonymousEnable = true; //不允许匿名
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		boolean anonymous = sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName());
    	return anonymous ? "directory" : "mainDirectory,directory"; //主目录、目录都需要获取
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#needExtendTree()
	 */
	protected boolean needExtendTree() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#listExtendTreeNodes(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listExtendTreeNodes(TreeDialog dialogForm, long parentDirectoryId, SessionInfo sessionInfo) throws Exception {
		//获取办事事项列表
		OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
		List serviceItems = onlineServiceItemService.listOnlineServiceItems(parentDirectoryId, false);
		if(serviceItems==null || serviceItems.isEmpty()) {
			return null;
		}
		SelectServiceItem selectForm = (SelectServiceItem)dialogForm;
		List extendTreeNodes = new ArrayList();
		for(Iterator iterator=serviceItems.iterator(); iterator.hasNext();) {
			OnlineServiceItem serviceItem = (OnlineServiceItem)iterator.next();
			if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名访问
				if(serviceItem.getIsPublic()!='1') { //未发布的
					continue;
				}
			}
			//检查是否支持受理、投诉和咨询
			if((selectForm.isAcceptSupportOnly() && serviceItem.getAcceptSupport()!='1') ||
			   (selectForm.isComplaintSupportOnly() && serviceItem.getComplaintSupport()!='1') ||
			   (selectForm.isConsultSupportOnly() && serviceItem.getConsultSupport()!='1') ||
			   (selectForm.isQuerySupportOnly() && serviceItem.getQuerySupport()!='1')) {
				continue;
			}
			//检查是否属于要过滤的目录
			if(ListUtils.findObjectByProperty(serviceItem.getSubjections(), "directoryId", new Long(selectForm.getFilterDirectoryId()))!=null) {
				continue;
			}
			TreeNode node = new TreeNode();
			node.setNodeId("" + serviceItem.getId()); //节点ID
			node.setNodeText(serviceItem.getName()); //节点文本
			node.setNodeType("serviceItem"); //节点类型
			node.setNodeIcon(Environment.getWebApplicationUrl() + "/cms/onlineservice/icons/item.gif"); //节点图标
			node.setNodeExpandIcon(null); //节点展开时的图标
			node.setHasChildNodes(false); //是否有子节点
			node.setExpandTree(false); //是否需要展开
			String url = Environment.getContextPath() + "/cms/onlineservice/guide.shtml" +
			  			 "?itemId=" + serviceItem.getId() +
			  			 "&directoryId=" + ((OnlineServiceItemSubjection)serviceItem.getSubjections().iterator().next()).getDirectoryId();
			node.setExtendPropertyValue("url", url);
			//node.setExtendNodeProperties(null); //扩展属性(Attribute模型)列表*/
			extendTreeNodes.add(node);
		}
		return extendTreeNodes.isEmpty() ? null : extendTreeNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "办事事项选择";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		SelectServiceItem selectForm = (SelectServiceItem)dialogForm;
		return "listServiceItems.shtml" +
			   "?filterDirectoryId=" + selectForm.getFilterDirectoryId() +
			   (selectForm.isAcceptSupportOnly() ? "&acceptSupportOnly=true" : "") +
			   (selectForm.isComplaintSupportOnly() ? "&complaintSupportOnly=true" : "") +
			   (selectForm.isConsultSupportOnly() ? "&consultSupportOnly=true" : "") +
			   (selectForm.isQuerySupportOnly() ? "&querySupportOnly=true" : "");
	}
}
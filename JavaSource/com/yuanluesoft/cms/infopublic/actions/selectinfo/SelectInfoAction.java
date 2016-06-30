package com.yuanluesoft.cms.infopublic.actions.selectinfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.infopublic.actions.selectdirectory.SelectDirectoryAction;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class SelectInfoAction extends SelectDirectoryAction {
	
	public SelectInfoAction() {
		super();
		anonymousEnable = false; //不允许匿名
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "publicInfo".equals(dialogForm.getSelectNodeTypes()) ? "info,main,category" : ("article".equals(dialogForm.getSelectNodeTypes()) ? "main,category,article" : "all"); //不过滤目录
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
		//获取信息列表
		PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
		List infos = publicInfoService.listInfos(parentDirectoryId + "", false, false, false, 0, 500);
		if(infos==null || infos.isEmpty()) {
			return null;
		}
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		long siteId = publicDirectoryService.getMainDirectory(parentDirectoryId).getSiteId();
		List extendTreeNodes = new ArrayList();
		for(Iterator iterator=infos.iterator(); iterator.hasNext();) {
			PublicInfo info = (PublicInfo)iterator.next();
			TreeNode node = new TreeNode();
			node.setNodeId("" + info.getId()); //节点ID
			node.setNodeText(info.getSubject().trim()); //节点文本
			node.setNodeType(PublicInfoService.INFO_TYPE_NAMES[info.getType()]); //节点类型
			node.setNodeIcon(Environment.getWebApplicationUrl() + "/cms/infopublic/icons/publicInfo.gif"); //节点图标
			node.setNodeExpandIcon(null); //节点展开时的图标
			node.setHasChildNodes(false); //是否有子节点
			node.setExpandTree(false); //是否需要展开
			node.setExtendPropertyValue("url", Environment.getContextPath() + "/cms/infopublic/" + (info.getType()==1 ? "article" : "publicInfo") + ".shtml?id=" + info.getId() + (siteId==0 ? "" : "&siteId=" + siteId));
			//node.setExtendNodeProperties(null); //扩展属性(Attribute模型)列表*/
			extendTreeNodes.add(node);
		}
		return extendTreeNodes.isEmpty() ? null : extendTreeNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "信息选择";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listInfos.shtml";
	}
}
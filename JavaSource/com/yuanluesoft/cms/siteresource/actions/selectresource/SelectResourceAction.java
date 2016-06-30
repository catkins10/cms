package com.yuanluesoft.cms.siteresource.actions.selectresource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class SelectResourceAction extends SelectSiteAction {
	
	public SelectResourceAction() {
		super();
		anonymousEnable = false; //不允许匿名
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "site,column"; //站点和栏目都需要获取
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
		//获取资源列表
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		List resources = siteResourceService.listSiteResources(parentDirectoryId + "", dialogForm.getSelectNodeTypes(), false, true, 0, 500);
		if(resources==null || resources.isEmpty()) {
			return null;
		}
		List extendTreeNodes = new ArrayList();
		for(Iterator iterator=resources.iterator(); iterator.hasNext();) {
			SiteResource resource = (SiteResource)iterator.next();
			TreeNode node = new TreeNode();
			node.setNodeId("" + resource.getId()); //节点ID
			node.setNodeText(resource.getSubjectTrim()); //节点文本
			node.setNodeType(SiteResourceService.RESOURCE_TYPE_NAMES[resource.getType()]); //节点类型
			node.setNodeIcon(Environment.getWebApplicationUrl() + "/cms/siteresource/icons/" + node.getNodeType() + ".gif"); //节点图标
			node.setNodeExpandIcon(null); //节点展开时的图标
			node.setHasChildNodes(false); //是否有子节点
			node.setExpandTree(false); //是否需要展开
			node.setExtendPropertyValue("url", resource.getType()==SiteResourceService.RESOURCE_TYPE_LINK ? resource.getLink() : Environment.getContextPath() + "/cms/siteresource/article.shtml?id=" + resource.getId());
			//node.setExtendNodeProperties(null); //扩展属性(Attribute模型)列表*/
			extendTreeNodes.add(node);
		}
		return extendTreeNodes.isEmpty() ? null : extendTreeNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "文章选择";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listResources.shtml";
	}
}
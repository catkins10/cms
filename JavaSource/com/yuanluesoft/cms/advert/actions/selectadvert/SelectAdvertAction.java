package com.yuanluesoft.cms.advert.actions.selectadvert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.advert.forms.SelectAdvert;
import com.yuanluesoft.cms.advert.pojo.AdvertSpace;
import com.yuanluesoft.cms.advert.service.AdvertService;
import com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class SelectAdvertAction extends SelectSiteAction {
	
	public SelectAdvertAction() {
		super();
		anonymousEnable = false; //不允许匿名
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "site"; //站点和栏目都需要获取
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
		SelectAdvert selectAdvertForm = (SelectAdvert)dialogForm;
		AdvertService advertService = (AdvertService)getService("advertService");
		List advertSpaces = advertService.listAdvertSpaces(parentDirectoryId, selectAdvertForm.isFloatOnly(), selectAdvertForm.isFixedOnly());
		if(advertSpaces==null || advertSpaces.isEmpty()) {
			return null;
		}
		List extendTreeNodes = new ArrayList();
		for(Iterator iterator=advertSpaces.iterator(); iterator.hasNext();) {
			AdvertSpace advertSpace = (AdvertSpace)iterator.next();
			TreeNode node = new TreeNode();
			node.setNodeId("" + advertSpace.getId()); //节点ID
			node.setNodeText(advertSpace.getName()); //节点文本
			node.setNodeType("advert"); //节点类型
			node.setNodeIcon(Environment.getWebApplicationUrl() + "/cms/advert/icons/advert.gif"); //节点图标
			node.setNodeExpandIcon(null); //节点展开时的图标
			node.setHasChildNodes(false); //是否有子节点
			node.setExpandTree(false); //是否需要展开
			node.setExtendPropertyValue("width", advertSpace.getWidth()); //扩展属性,宽度
			node.setExtendPropertyValue("height", advertSpace.getHeight()); //扩展属性,高度
			extendTreeNodes.add(node);
		}
		return extendTreeNodes.isEmpty() ? null : extendTreeNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "广告选择";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		SelectAdvert selectAdvertForm = (SelectAdvert)dialogForm;
		return "listAdverts.shtml?floatOnly=" + selectAdvertForm.isFloatOnly() + "&fixedOnly=" + selectAdvertForm.isFixedOnly();
	}
}
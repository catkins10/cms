package com.yuanluesoft.cms.onlineservice.faq.actions.selectfaq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.actions.selectdirectory.SelectDirectoryAction;
import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq;
import com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class SelectFaqAction extends SelectDirectoryAction {
	
	public SelectFaqAction() {
		super();
		anonymousEnable = false; //不允许匿名
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
		//获取问题列表
		OnlineServiceFaqService onlineServiceFaqService = (OnlineServiceFaqService)getService("onlineServiceFaqService");
		List faqs = onlineServiceFaqService.listFaqs(parentDirectoryId, false);
		if(faqs==null || faqs.isEmpty()) {
			return null;
		}
		List extendTreeNodes = new ArrayList();
		for(Iterator iterator=faqs.iterator(); iterator.hasNext();) {
			OnlineServiceFaq faq = (OnlineServiceFaq)iterator.next();
			if(sessionInfo==null || SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //匿名访问
				if(faq.getStatus()!=OnlineServiceFaqService.FAQ_STATUS_ISSUE) { //未发布的
					continue;
				}
			}
			TreeNode node = new TreeNode();
			node.setNodeId("" + faq.getId()); //节点ID
			node.setNodeText(faq.getQuestion()); //节点文本
			node.setNodeType("faq"); //节点类型
			node.setNodeIcon(Environment.getWebApplicationUrl() + "/cms/onlineservice/icons/item.gif"); //节点图标
			node.setNodeExpandIcon(null); //节点展开时的图标
			node.setHasChildNodes(false); //是否有子节点
			node.setExpandTree(false); //是否需要展开
			extendTreeNodes.add(node);
		}
		return extendTreeNodes.isEmpty() ? null : extendTreeNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "常见问题选择";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listFaqs.shtml";
	}
}
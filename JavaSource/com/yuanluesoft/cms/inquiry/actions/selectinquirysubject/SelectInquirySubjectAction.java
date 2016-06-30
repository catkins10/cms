package com.yuanluesoft.cms.inquiry.actions.selectinquirysubject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
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
public class SelectInquirySubjectAction extends SelectSiteAction {

	public SelectInquirySubjectAction() {
		super();
		anonymousEnable = false; //禁止匿名访问
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "site"; //仅站点
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
		//获取调查主题
		InquiryService inquiryService = (InquiryService)getService("inquiryService");
    	List inquirySubjects = inquiryService.listInquirySubjects(parentDirectoryId);
    	if(inquirySubjects==null) {
			return null;
		}
		List extendTreeNodes = new ArrayList();
		for(Iterator iterator=inquirySubjects.iterator(); iterator.hasNext();) {
			InquirySubject inquirySubject = (InquirySubject)iterator.next();
			TreeNode node = new TreeNode();
			node.setNodeId("" + inquirySubject.getId()); //节点ID
			node.setNodeText(inquirySubject.getSubject()); //节点文本
			node.setNodeType("inquirySubject"); //节点类型
			node.setNodeIcon(Environment.getWebApplicationUrl() + "/cms/inquiry/icons/inquirySubject.gif"); //节点图标
			node.setNodeExpandIcon(null); //节点展开时的图标
			node.setHasChildNodes(false); //是否有子节点
			node.setExpandTree(false); //是否需要展开
			node.setExtendPropertyValue("url", Environment.getContextPath() + "/cms/inquiry/inquirySubject.shtml?id=" + inquirySubject.getId() + (inquirySubject.getSiteId()==0 ? "" : "&siteId=" + inquirySubject.getSiteId()));
			//node.setExtendNodeProperties(null); //扩展属性(Attribute模型)列表*/
			extendTreeNodes.add(node);
		}
		return extendTreeNodes.isEmpty() ? null : extendTreeNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listInquirySubjects.shtml";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "调查服务选择";
	}
}
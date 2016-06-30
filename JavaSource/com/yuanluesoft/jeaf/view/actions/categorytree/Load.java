package com.yuanluesoft.jeaf.view.actions.categorytree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.forms.CategoryTree;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends CategoryTreeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	CategoryTree categoryTreeForm = (CategoryTree)form;
		SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			categoryTreeForm.setActionResult("NOSESSIONINFO");
			return mapping.findForward("load");
		}
		//获取视图
		View view = getView(categoryTreeForm.getApplicationName(), categoryTreeForm.getViewName(), sessionInfo);
		String[] categories = categoryTreeForm.getCurrentCategories()==null ? new String[]{view.getCategoryRoot() + "|ROOT"} : categoryTreeForm.getCurrentCategories().split(",");
		//创建树
		String currentCategories = categories[0];
		categoryTreeForm.setTree(new Tree(currentCategories, view.getCategoryRoot(), null, request.getContextPath() + "/jeaf/view/images/categoryTree.gif"));
		TreeNode parentNode = categoryTreeForm.getTree().getRootNode();
    	//获取第一级子节点列表
		parentNode.setChildNodes(listChildTreeNodes(view, currentCategories, request, sessionInfo));
		for(int i=1; i<categories.length - 1; i++) {
			currentCategories += "," + categories[i];
			parentNode = (TreeNode)ListUtils.findObjectByProperty(parentNode.getChildNodes(), "nodeId", currentCategories); //找到当前分类
			if(parentNode==null) {
				break;
			}
			parentNode.setExpandTree(true); //展开
	    	parentNode.setChildNodes(listChildTreeNodes(view, currentCategories, request, sessionInfo)); //获取子节点列表
		}
    	//设置获取子节点的URL
		categoryTreeForm.setListChildNodesUrl("listChildCategories.shtml?" + request.getQueryString());
		categoryTreeForm.setActionResult("SUCCESS");
		return mapping.findForward("load");
    }
}
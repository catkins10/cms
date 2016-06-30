package com.yuanluesoft.eai.client.actions.select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.eai.client.EAIClient;
import com.yuanluesoft.eai.client.forms.Select;
import com.yuanluesoft.eai.client.model.Application;
import com.yuanluesoft.eai.client.model.EAI;
import com.yuanluesoft.eai.client.model.Group;
import com.yuanluesoft.eai.client.model.Link;
import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.SelectDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends SelectDialogAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeOpenDialogAction(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogAction#initDialog(com.yuanluesoft.jeaf.dialog.forms.SelectDialog, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initDialog(SelectDialog dialog, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initDialog(dialog, sessionInfo, request);
		Select selectForm = (Select)dialog;
   		selectForm.setTitle("选择");
   		//获取EAI导航
   		EAIClient eaiClient = (EAIClient)getService("eaiClient");
   		EAI eai = eaiClient.getEAI(sessionInfo.getLoginName());
    	//转换为树
    	Tree tree = new Tree("0", eai.getName(), "root", Environment.getContextPath() + "/jeaf/application/icons/root.gif");
    	tree.getRootNode().setChildNodes(convertTreeNodes(selectForm, eai.getChildren()));
    	selectForm.setTree(tree);
    }
	
	/**
	 * 转换为树节点
	 * @param selectForm
	 * @param navigatorChildren
	 * @return
	 */
	protected List convertTreeNodes(Select selectForm, List eaiChildren) {
		if(eaiChildren==null) {
			return null;
		}
		List treeNodes = new ArrayList();
		for(Iterator iterator = eaiChildren.iterator(); iterator.hasNext();) {
    		Object element = iterator.next();
    		if(element instanceof Group) { //分组
    			Group group = (Group)element;
    			List childNodes = convertTreeNodes(selectForm, group.getChildren());
    			if(childNodes!=null) {
    				TreeNode node = new TreeNode("group_" + group.getName(), group.getName(), "group", Environment.getContextPath() + "/jeaf/application/icons/group.gif", true);
    				node.setChildNodes(childNodes);
    				treeNodes.add(node);
    			}
    		}
    		else if(element instanceof Application) { //应用
    			if(selectForm.getSelectNodeTypes()==null || selectForm.getSelectNodeTypes().equals("all") || selectForm.getSelectNodeTypes().indexOf("application")!=-1) {
    				Application application = (Application)element;
    				TreeNode node = new TreeNode(application.getName(), application.getTitle(), "application", Environment.getContextPath() + "/jeaf/application/icons/application.gif", false);
    				treeNodes.add(node);
    			}
    		}
    		else if(element instanceof Link) { //链接
    			if(selectForm.getSelectNodeTypes()==null || selectForm.getSelectNodeTypes().equals("all") || selectForm.getSelectNodeTypes().indexOf("link")!=-1) {
    				Link link = (Link)element;
    				TreeNode node = new TreeNode("link_" + link.getName(), link.getName(), "link", Environment.getContextPath() + "/jeaf/application/icons/link.gif", false);
    				treeNodes.add(node);
    			}
    		}
    	}
		Collections.sort(treeNodes, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				TreeNode node0 = (TreeNode)arg0;
				TreeNode node1 = (TreeNode)arg1;
				if(node0.getNodeType().equals(node1.getNodeType())) {
					return 0;
				}
				if(node0.getNodeType().equals("group")) {
					return -1;
				}
				if(node1.getNodeType().equals("group")) {
					return 1;
				}
				return  0;
			}
		});
		return treeNodes.isEmpty() ? null : treeNodes;
	}
}
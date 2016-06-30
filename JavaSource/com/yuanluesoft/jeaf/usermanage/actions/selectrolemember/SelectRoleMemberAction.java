/**
 * 
 */
package com.yuanluesoft.jeaf.usermanage.actions.selectrolemember;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.usermanage.actions.selectrole.SelectRoleAction;
import com.yuanluesoft.jeaf.usermanage.forms.Select;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.util.Environment;

/**
 *
 * @author LinChuan
 *
 */
public class SelectRoleMemberAction extends SelectRoleAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.selectrole.SelectRoleAction#listExtendTreeNodes(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listExtendTreeNodes(TreeDialog dialogForm, long parentDirectoryId, SessionInfo sessionInfo) throws Exception {
		List extendTreeNodes = super.listExtendTreeNodes(dialogForm, parentDirectoryId, sessionInfo);
		if(extendTreeNodes==null) {
			extendTreeNodes = new ArrayList();
		}
		else {
			for(Iterator iterator = extendTreeNodes.iterator(); iterator.hasNext();) {
				TreeNode treeNode = (TreeNode)iterator.next();
				treeNode.setHasChildNodes(true); //设置为有子节点
			}
		}
		RoleService roleService = (RoleService)getService("roleService");
		List roleMembers = roleService.listRoleMembers("" + parentDirectoryId);
		for(Iterator iterator=roleMembers==null ? null : roleMembers.iterator(); iterator!=null && iterator.hasNext();) {
			Person person = (Person)iterator.next();
			TreeNode node = new TreeNode();
			node.setNodeId("" + person.getId()); //节点ID
			node.setNodeText(person.getName()); //节点文本
			node.setNodeType(PersonService.PERSON_TYPE_NAMES[person.getType()]); //节点类型
			node.setNodeIcon(Environment.getWebApplicationUrl() + "/jeaf/usermanage/icons/" + node.getNodeType() + ".gif"); //节点图标
			node.setNodeExpandIcon(null); //节点展开时的图标
			node.setHasChildNodes(false); //是否有子节点
			node.setExpandTree(false); //是否需要展开
			//扩展属性(Attribute模型)列表
			List extendNodeProperties = new ArrayList();
			extendNodeProperties.add(new Attribute("mailAddress", person.getMailAddress()));
			extendNodeProperties.add(new Attribute("mobile", person.getMobile()));
			extendNodeProperties.add(new Attribute("loginName", person.getLoginName()));
			node.setExtendNodeProperties(extendNodeProperties);
			extendTreeNodes.add(node);
		}
		return extendTreeNodes.isEmpty() ? null : extendTreeNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "用户选择";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		Select selectForm = (Select)dialogForm;
		return "listRoleMembers.shtml" + (selectForm.isPostOnly() ? "?postOnly=true" : "");
	}
}
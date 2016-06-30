/**
 * 
 */
package com.yuanluesoft.jeaf.usermanage.actions.selectperson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.base.model.Attribute;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.usermanage.actions.selectorg.SelectOrgAction;
import com.yuanluesoft.jeaf.usermanage.forms.Select;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.Environment;

/**
 *
 * @author LinChuan
 *
 */
public class SelectPersonAction extends SelectOrgAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		Select selectForm = (Select)dialogForm;
		String personTypes = selectForm.getSelectNodeTypes();
		String orgTypes; 
		if(personTypes==null || personTypes.equals("") || personTypes.indexOf("all")!=-1) {
			orgTypes = "all"; 
		}
		else {
			personTypes = "," + personTypes + ",";
			boolean unitEnable = false;
			boolean schoolEnable = false;
			boolean schoolDepartmentEnable = false;
			boolean unitDepartmentEnable = false;
			boolean classEnable = false;
			//判断是否包括普通用户
			if(personTypes.indexOf(",employee,")!=-1) {
				unitEnable = true;
				schoolEnable = true;
				schoolDepartmentEnable = true;
				unitDepartmentEnable = true;
			}
			if(personTypes.indexOf(",teacher,")!=-1) {
				schoolEnable = true;
				schoolDepartmentEnable = true;
			}
			if(personTypes.indexOf(",student,")!=-1) {
				schoolEnable = true;
				classEnable = true;
			}
			if(personTypes.indexOf(",genearch,")!=-1) {
				schoolEnable = true;
				classEnable = true;
			}
			orgTypes = "root,area,category";
			if(unitEnable) {
				orgTypes += ",unit";
			}
			if(schoolEnable) {
				orgTypes += ",school";
			}
			if(unitDepartmentEnable) {
				orgTypes += ",unitDepartment";
			}
			if(schoolDepartmentEnable) {
				orgTypes += ",schoolDepartment";
			}
			if(classEnable) {
				orgTypes += ",schoolClass";
			}
		}
		return orgTypes;
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
		//获取用户列表
		List persons = getOrgService().listOrgPersons("" + parentDirectoryId, dialogForm.getSelectNodeTypes(), false, false, 0, 300);
		if(persons==null || persons.isEmpty()) {
			return null;
		}
		List extendTreeNodes = new ArrayList();
		for(Iterator iterator=persons.iterator(); iterator.hasNext();) {
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
			extendNodeProperties.add(new Attribute("mailFullAddress", person.getMailFullAddress()));
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
		return "listPersons.shtml";
	}
}
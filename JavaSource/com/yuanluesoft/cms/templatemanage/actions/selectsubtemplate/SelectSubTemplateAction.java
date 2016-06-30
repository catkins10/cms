package com.yuanluesoft.cms.templatemanage.actions.selectsubtemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction;
import com.yuanluesoft.cms.templatemanage.forms.SelectSubTemplate;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class SelectSubTemplateAction extends SelectSiteAction {

	public SelectSubTemplateAction() {
		super();
		anonymousEnable = false; //禁止匿名访问
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "site,column"; //仅站点
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
		//获取子模板列表
		TemplateService templateService = (TemplateService)getService("templateService");
		SelectSubTemplate selectForm = (SelectSubTemplate)dialogForm;
    	List subTemplates = templateService.listSubTemplates(parentDirectoryId, selectForm.getThemeId());
    	if(subTemplates==null) {
			return null;
		}
		List extendTreeNodes = new ArrayList();
		for(Iterator iterator=subTemplates.iterator(); iterator.hasNext();) {
			Template template = (Template)iterator.next();
			TreeNode node = new TreeNode();
			node.setNodeId("" + template.getId()); //节点ID
			node.setNodeText(template.getTemplateName()); //节点文本
			node.setNodeType("subTemplate"); //节点类型
			node.setNodeIcon(Environment.getWebApplicationUrl() + "/cms/templatemanage/images/template.gif"); //节点图标
			node.setNodeExpandIcon(null); //节点展开时的图标
			node.setHasChildNodes(false); //是否有子节点
			node.setExpandTree(false); //是否需要展开
			node.setExtendNodeProperties(null); //扩展属性(Attribute模型)列表*/
			extendTreeNodes.add(node);
		}
		return extendTreeNodes.isEmpty() ? null : extendTreeNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		SelectSubTemplate selectForm = (SelectSubTemplate)dialogForm;
		return "listSubTemplates.shtml?themeId=" + selectForm.getThemeId();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "子模板选择";
	}
}
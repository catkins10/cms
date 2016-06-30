package com.yuanluesoft.bidding.enterprise.actions.selectenterprise;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class SelectEnterpriseAction extends TreeDialogAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "企业选择";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryService(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected DirectoryService getDirectoryService(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws SystemUnregistException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDirectoryTypeFilters(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDirectoryTypeFilters(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listChildren.shtml";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#createTree(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected Tree createTree(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		EnterpriseService enterpriseService = (EnterpriseService)getService("enterpriseService");
    	//设置分类列表
    	List enterpriseCategories = enterpriseService.listEnterpriseCategories();
    	Tree tree = new Tree("0", "企业选择", "root", Environment.getContextPath() + "/jeaf/usermanage/icons/root.gif");
    	for(Iterator iterator = enterpriseCategories.iterator(); iterator.hasNext();) {
    		String category = (String)iterator.next();
    		tree.appendChildNode("category_" + category, category, "category", Environment.getContextPath() + "/jeaf/usermanage/icons/category.gif", true);
   	}
    	return tree;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#listChildNodes(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listChildNodes(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		EnterpriseService enterpriseService = (EnterpriseService)getService("enterpriseService");
    	if(dialogForm.getParentNodeId().startsWith("category_")) {
    		//获取地区列表
    		List areas = enterpriseService.listEnterpriseAreas(dialogForm.getParentNodeId().substring("category_".length()));
    		for(int i=0; i<(areas==null ? 0 : areas.size()); i++) {
    			areas.set(i, new TreeNode("area_" + areas.get(i) + "_" + dialogForm.getParentNodeId(), (String)areas.get(i), "area", Environment.getContextPath() + "/jeaf/usermanage/icons/area.gif", true));
    		}
    		return areas;
    	}
    	else {
    		String[] values = dialogForm.getParentNodeId().split("_");
    		//获取企业列表
    		List enterprises = enterpriseService.listEnterprises(values[3], values[1]);
    		for(int i=0; i<(enterprises==null ? 0 : enterprises.size()); i++) {
    			BiddingEnterprise enterprise = (BiddingEnterprise)enterprises.get(i);
    			TreeNode node = new TreeNode("" + enterprise.getId(), enterprise.getName(), "enterprise", Environment.getContextPath() + "/jeaf/usermanage/icons/unit.gif", false);
    			node.setExtendPropertyValue("kind", enterprise.getKind());
    			node.setExtendPropertyValue("legalRepresentative", enterprise.getLegalRepresentative());
    			node.setExtendPropertyValue("linkman", enterprise.getLinkman());
    			node.setExtendPropertyValue("linkmanIdNumber", enterprise.getLinkmanIdNumber());
    			node.setExtendPropertyValue("tel", enterprise.getTel());
    			node.setExtendPropertyValue("mobile", enterprise.getMobile());
    			node.setExtendPropertyValue("fax", enterprise.getFax());
    			enterprises.set(i, node);
    		}
    		return enterprises;
    	}
	}
}
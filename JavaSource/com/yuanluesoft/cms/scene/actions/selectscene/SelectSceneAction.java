package com.yuanluesoft.cms.scene.actions.selectscene;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.scene.forms.SelectScene;
import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.cms.sitemanage.actions.selectsite.SelectSiteAction;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class SelectSceneAction extends SelectSiteAction {

	public SelectSceneAction() {
		super();
		anonymousEnable = false; //禁止匿名访问
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#createTree(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected Tree createTree(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(dialogForm.getParentNodeId()!=null) { //指定了父目录
			//检查父目录是否是场景服务
			SceneService sceneService = (SceneService)getService("sceneService");
			com.yuanluesoft.cms.scene.pojo.SceneService service = sceneService.getSceneService(Long.parseLong(dialogForm.getParentNodeId()));
			if(service!=null) { //是场景服务
				//创建场景服务目录树
				Tree tree = new Tree(service.getId() + "", service.getName(), "service", Environment.getContextPath() + "/cms/scene/icons/service.gif");
				//获取子节点列表
				tree.getRootNode().setChildNodes(listExtendTreeNodes(dialogForm, service.getId(), sessionInfo));
				return tree;
			}
		}
		Tree tree = super.createTree(dialogForm, request, sessionInfo);
		SelectScene selectSceneForm = (SelectScene)dialogForm;
		if(selectSceneForm.isOneSiteOnly() && tree.getRootNode().getChildNodes()!=null) { //只显示一个站点
			//从子节点中删除其他站点
			for(Iterator iterator = tree.getRootNode().getChildNodes().iterator(); iterator.hasNext();) {
				TreeNode node = (TreeNode)iterator.next();
				if(node.getNodeType().equals("site")) {
					iterator.remove();
				}
			}
		}
		return tree;
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
		SceneService sceneService = (SceneService)getService("sceneService");
	    SiteService siteService = (SiteService)getService("siteService");
	    String selectTypes = dialogForm.getSelectNodeTypes();
	    boolean selectServiceOnly = "service".equals(selectTypes); //是否只选择场景服务
	    if(selectServiceOnly || siteService.getDirectory(parentDirectoryId)!=null) { //是站点
			//获取场景服务列表
			List sceneServices = sceneService.listSceneServices(parentDirectoryId);
	    	if(sceneServices==null) {
				return null;
			}
			for(int i=0; i<sceneServices.size(); i++) {
				com.yuanluesoft.cms.scene.pojo.SceneService service = (com.yuanluesoft.cms.scene.pojo.SceneService)sceneServices.get(i);
				TreeNode node = new TreeNode(service.getId() + "", service.getName(), "service", Environment.getContextPath() + "/cms/scene/icons/service.gif", !selectServiceOnly);
				node.setExtendPropertyValue("url", Environment.getContextPath() + "/cms/scene/sceneService.shtml?id=" + service.getId() + (service.getSiteId()==0 ? "" : "&siteId=" + service.getSiteId()));
				sceneServices.set(i, node);
			}
			return sceneServices;
	    }
	    else { //是场景服务或者场景目录
	    	//获取场景子目录列表
	    	List childDirectories = sceneService.listChildSceneDirectories(parentDirectoryId);
			if(childDirectories==null || childDirectories.isEmpty()) {
				return null;
			}
			for(int i=0; i<childDirectories.size(); i++) {
				SceneDirectory sceneDirectory = (SceneDirectory)childDirectories.get(i);
				if(sceneDirectory.getDirectoryType().equals("scene") || //是场景
				   ("," + selectTypes + ",").indexOf(sceneDirectory.getDirectoryType())!=-1) { //在被选择的类型中
					childDirectories.set(i, new TreeNode(sceneDirectory.getId() + "", sceneDirectory.getDirectoryName(), sceneDirectory.getDirectoryType(), Environment.getContextPath() + "/cms/scene/icons/" + sceneDirectory.getDirectoryType() + ".gif", sceneDirectory.getChildDirectories()!=null && !sceneDirectory.getChildDirectories().isEmpty()));
				}
				else {
					childDirectories.remove(i);
					i--;
				}
			}
			return childDirectories;
	    }
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "listScenes.shtml";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.treedialog.TreeDialogAction#getDialogTitle(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getDialogTitle(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return "场景服务选择";
	}
}
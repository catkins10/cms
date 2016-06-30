package com.yuanluesoft.cms.scene.actions.scenetree.admin;

import java.util.List;

import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.cms.scene.pojo.SceneLink;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class SceneTreeAction extends BaseAction {

	/**
	 * 获取树的子节点列表
	 * @param parentDirectoryId
	 * @return
	 * @throws Exception
	 */
	protected List listChildTreeNodes(long parentDirectoryId) throws Exception {
		SceneService sceneService = (SceneService)getService("sceneService");
		List childDirectories = sceneService.listChildSceneDirectories(parentDirectoryId);
		if(childDirectories==null || childDirectories.isEmpty()) {
			return null;
		}
		for(int i=0; i<childDirectories.size(); i++) {
			SceneDirectory sceneDirectory = (SceneDirectory)childDirectories.get(i);
			TreeNode node = new TreeNode(sceneDirectory.getId() + "", sceneDirectory.getDirectoryName(), sceneDirectory.getDirectoryType(), Environment.getContextPath() + "/cms/scene/icons/" + sceneDirectory.getDirectoryType() + ".gif", sceneDirectory.getChildDirectories()!=null && !sceneDirectory.getChildDirectories().isEmpty());
			if(sceneDirectory instanceof SceneLink) { //链接
				node.setExtendPropertyValue("url", ((SceneLink)sceneDirectory).getUrl());
			}
			childDirectories.set(i, node);
		}
		return childDirectories;
	}
}
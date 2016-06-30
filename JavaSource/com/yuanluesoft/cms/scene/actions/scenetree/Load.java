package com.yuanluesoft.cms.scene.actions.scenetree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.scene.actions.scenetree.admin.SceneTreeAction;
import com.yuanluesoft.cms.scene.forms.admin.SceneTree;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 场景目录树
 * @author linchuan
 *
 */
public class Load extends SceneTreeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	anonymousAlways = true;
    	SceneService sceneService = (SceneService)getService("sceneService");
    	SceneTree sceneTree = (SceneTree)form;
    	if(sceneTree.getId()>0) {
	        //获取场景服务
	    	com.yuanluesoft.cms.scene.pojo.SceneService service = sceneService.getSceneService(sceneTree.getId());
	    	//创建树
	    	sceneTree.setTree(new Tree(service.getId() + "", service.getName(), "service", Environment.getContextPath() + "/cms/scene/icons/service.gif"));
	    	//获取子节点列表
	    	sceneTree.getTree().getRootNode().setChildNodes(listChildTreeNodes(service.getId()));
	    	//设置获取子节点的URL
	    	sceneTree.setListChildNodesUrl("admin/listChildNodes.shtml?anonymousAlways=true");
    	}
    	return mapping.findForward("load");
    }
}
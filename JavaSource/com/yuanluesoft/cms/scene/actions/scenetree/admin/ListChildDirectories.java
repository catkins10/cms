package com.yuanluesoft.cms.scene.actions.scenetree.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.scene.forms.admin.SceneTree;

/**
 * 
 * @author linchuan
 *
 */
public class ListChildDirectories extends SceneTreeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SceneTree sceneTree = (SceneTree)form;
    	//获取子节点列表
    	sceneTree.setChildNodes(listChildTreeNodes(Long.parseLong(sceneTree.getParentNodeId())));
    	sceneTree.setActionResult("SUCCESS");
    	return mapping.findForward("load");
    }
}
package com.yuanluesoft.jeaf.view.actions.categorytree;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;

/**
 * 
 * @author linchuan
 *
 */
public class CategoryTreeAction extends BaseAction {

	/**
	 * 获取树的子节点列表
	 * @param view
	 * @param currentCaetgories
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected List listChildTreeNodes(View view, String currentCaetgories, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		ViewService viewService = (ViewService)getService(view.getViewServiceName()==null ? "viewService" : view.getViewServiceName());
    	List childCategories = viewService.listChildCategories(view, currentCaetgories, request, sessionInfo, 0);
		if(childCategories==null || childCategories.isEmpty()) {
			return null;
		}
		//转换为节点
		for(int i=0; i<childCategories.size(); i++) {
			Object[] values = (Object[])childCategories.get(i);
			TreeNode node = new TreeNode(currentCaetgories + "," + values[0] + "|" + values[1], "" + values[0], null, request.getContextPath() + "/jeaf/view/images/category.gif", view.isCategoryLoop() || currentCaetgories.split(",").length<view.getCategories().size());
			childCategories.set(i, node);
		}
		return childCategories;
	}
	
	/**
	 * 获取视图
	 * @param applicationName
	 * @param viewName
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected View getView(String applicationName, String viewName, SessionInfo sessionInfo) throws Exception {
		return ((ViewDefineService)getService("viewDefineService")).getView(applicationName, viewName, sessionInfo);
	}
}
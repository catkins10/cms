package com.yuanluesoft.bbs.forum.actions.forum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bbs.base.actions.BbsViewFormAction;
import com.yuanluesoft.bbs.forum.forms.Forum;
import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Load extends BbsViewFormAction {
	
	public Load() {
		super();
		anonymousEnable = true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "bbs/article";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "article";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		//设置帖子所在版块
		Forum forumForm = (Forum)viewForm;
		String where = "subjections.forumId=" + forumForm.getId();
		view.addWhere(where);
		
		ForumService forumService = (ForumService)getService("forumService");
		//检查用户的访问权限
		List popedoms = forumService.listDirectoryPopedoms(forumForm.getId(), sessionInfo);
		if(popedoms==null || popedoms.isEmpty()) {
			List childPopedoms = forumService.listChildDirectoryPopedoms(forumForm.getId(), sessionInfo); //检查是否有下级目录的权限
			if(childPopedoms==null || childPopedoms.isEmpty()) {
				throw new PrivilegeException();
			}
		}
		//获取版块模型
		forumForm.setForum(forumService.getForum(forumForm.getId(), sessionInfo));
		//获取父目录列表
		forumForm.setParentDirectories(forumService.listParentDirectories(forumForm.getId(), "bbs"));
		if(popedoms==null || popedoms.isEmpty()) {
			view.setWhere("subjections.forumId=-1"); //用户没有当前论坛的访问权限,不获取主题列表
		}
		/*/检查是否管理员
    	boolean isManager = forumService.isManager(forumForm.getId(), sessionInfo.getLoginName());
    	if(isManager) {
    		List actions = forumForm.getViewPackage().getView().getActions();
        	if(actions==null) {
        		actions = new ArrayList();
        	}
        	//添加"修改"按钮
        	ViewAction viewAction = new ViewAction();
		    viewAction.setTitle("修改版块信息");
		    viewAction.setExecute("PageUtils.editrecord('bbs/forum', 'admin/forum', " + forumForm.getId() + ", 'width=720,height=480')");
		    actions.add(viewAction);
		    //添加"新建子版块"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("新建子版块");
		    viewAction.setExecute("PageUtils.newrecord('bbs/forum', 'admin/forum', 'width=720,height=480', 'parentId=" + forumForm.getId() + "')");
		    actions.add(viewAction);
		    view.setActions(actions);
    	}*/
	}
}
package com.yuanluesoft.bbs.article.actions.admin.articleview;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bbs.article.forms.admin.ArticleView;
import com.yuanluesoft.bbs.forum.pojo.BbsDirectory;
import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ArticleViewAction extends ViewFormAction {
	
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
		return "admin/article";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		//设置帖子所在目录,帖子在当前目录中或者在用户有访问权限的下级版块中
		ArticleView articleViewForm = (ArticleView)viewForm;
		view.addJoin(", BbsDirectorySubjection BbsDirectorySubjection, BbsDirectoryPopedom BbsDirectoryPopedom");
		String where = "subjections.forumId=BbsDirectorySubjection.directoryId" +
					   " and BbsDirectorySubjection.parentDirectoryId=" + articleViewForm.getDirectoryId() +
					   " and BbsDirectoryPopedom.directoryId=BbsDirectorySubjection.directoryId" +
					   " and BbsDirectoryPopedom.userId in (" + sessionInfo.getUserIds() + ")";
		view.addWhere(where);
		
		ForumService forumService = (ForumService)getService("forumService");
		//获取类型
		BbsDirectory directory = (BbsDirectory)forumService.getDirectory(articleViewForm.getDirectoryId());
		String directoryType = directory.getDirectoryType();
    	//检查是否管理员
    	if(forumService.checkPopedom(directory.getId(), "manager", sessionInfo)) {
    		List actions = new ArrayList();
			//添加"修改"按钮
    		 ViewAction	viewAction = new ViewAction();
		    viewAction.setTitle("修改" + forumService.getDirectoryTypeTitle(directoryType)+ "信息");
		    viewAction.setExecute("PageUtils.editrecord('bbs/forum', 'admin/" + directoryType + "', " + articleViewForm.getDirectoryId() + ", 'width=720,height=480')");
		    actions.add(viewAction);
		    //添加"新建论坛"按钮
		    if("bbs".equals(directoryType)) {
		    	viewAction = new ViewAction();
			    viewAction.setTitle("新建论坛");
			    viewAction.setExecute("PageUtils.newrecord('bbs/forum', 'admin/bbs', 'width=720,height=480', 'parentDirectoryId=" + articleViewForm.getDirectoryId() + "')");
			    actions.add(viewAction);
		    }
		    //添加"新建版块分类"按钮
		    if("bbs".equals(directoryType) || "category".equals(directoryType)) {
		    	viewAction = new ViewAction();
			    viewAction.setTitle("新建版块分类");
			    viewAction.setExecute("PageUtils.newrecord('bbs/forum', 'admin/category', 'width=720,height=480', 'parentDirectoryId=" + articleViewForm.getDirectoryId() + "')");
			    actions.add(viewAction);
		    }
		    //添加"新建版块"按钮
		    if("category".equals(directoryType) || "forum".equals(directoryType)) {
		    	viewAction = new ViewAction();
			    viewAction.setTitle("新建版块");
			    viewAction.setExecute("PageUtils.newrecord('bbs/forum', 'admin/forum', 'width=720,height=480', 'parentDirectoryId=" + articleViewForm.getDirectoryId() + "')");
			    actions.add(viewAction);
		    }
		    
		    //添加"调整目录优先级"按钮
		    viewAction = new ViewAction();
			viewAction.setTitle("调整目录优先级");
			viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/bbs/forum/admin/adjustDirectoryPriority.shtml?parentDirectoryId=" + articleViewForm.getDirectoryId() + "', 640, 400)");
			actions.add(viewAction);
		    
		    List normalActions = articleViewForm.getViewPackage().getView().getActions();
        	if(normalActions!=null) {
        		actions.addAll(normalActions);
        	}
        	view.setActions(actions);
    	}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		ForumService forumService = (ForumService)getService("forumService");
		ArticleView articleViewForm = (ArticleView)viewForm;
		//重设当前位置
		String directoryFullName = forumService.getDirectoryFullName(articleViewForm.getDirectoryId(), "/", null);
		location.clear();
		String[] names = directoryFullName.split("/");
		for(int i=0; i<names.length; i++) {
			location.add(names[i]);
		}
		location.add("主题列表");
	}
}
package com.yuanluesoft.cms.infopublic.actions.admin.publicinfoview;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.infopublic.forms.admin.PublicInfoView;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectoryCategory;
import com.yuanluesoft.cms.infopublic.pojo.PublicArticleDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewActionGroup;

/**
 * 
 * @author yuanluesoft
 *
 */
public class PublicInfoViewAction extends ViewFormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "cms/infopublic";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "admin/publicInfo";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
	 	PublicInfoView publicInfoViewForm = (PublicInfoView)viewForm;
	    if(publicInfoViewForm.getDirectoryId()!=0) { //根目录显示全部
	    	view.addJoin(", PublicDirectorySubjection PublicDirectorySubjection");
	    	String where = "subjections.directoryId=PublicDirectorySubjection.directoryId" +
	    				   " and PublicDirectorySubjection.parentDirectoryId=" +  publicInfoViewForm.getDirectoryId();
	    	view.addWhere(where);
	    }
	    PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		PublicDirectory directory = (PublicDirectory)publicDirectoryService.getDirectory(publicInfoViewForm.getDirectoryId());
		//获取用户对目录的权限
		List popedoms = publicDirectoryService.listDirectoryPopedoms(publicInfoViewForm.getDirectoryId(), sessionInfo);
		if(popedoms==null) {
			popedoms = new ArrayList();
		}
		List actions = new ArrayList();
		
		ViewActionGroup newRecordActionGroup = new ViewActionGroup("新建");
		actions.add(newRecordActionGroup);
		if(popedoms.contains("editor") && (directory instanceof PublicInfoDirectory || directory instanceof PublicArticleDirectory)) {
			 ViewAction viewAction = new ViewAction();
			 String type = (directory instanceof PublicArticleDirectory ? "文章" : "信息");
			 viewAction.setTitle("新增" + type);
			 viewAction.setGroupTitle(type);
			 viewAction.setExecute("PageUtils.newrecord('cms/infopublic', 'admin/publicInfo', 'mode=fullscreen', 'directoryId=" + publicInfoViewForm.getDirectoryId() + "');");
			 newRecordActionGroup.addViewAction(viewAction);
		}
		
		ViewActionGroup batchActionGroup = new ViewActionGroup("批量处理");
		actions.add(batchActionGroup);
	
    	ViewAction viewAction = new ViewAction();
	    viewAction.setTitle("批量办理");
	    viewAction.setGroupTitle("办理信息");
	    viewAction.setExecute("batchSend('admin/runInfoApproval')");
	    batchActionGroup.addViewAction(viewAction);
		
	    if(popedoms.contains("editor") || popedoms.contains("manager")) { //用户是编辑或管理员
		    //添加"移动信息"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("移动信息");
		    //viewAction.setExecute("selectDirectory(600, 400, true, \"{id},{name|选择目录|100%}\", \"FormUtils.doAction('moveInfos', 'from=" + directory.getId() + "&to={id}')\", \"\", \"\", \"\", \"info\")");
		    viewAction.setExecute("selectDirectory(600, 400, false, \"{id},{name}\", \"FormUtils.doAction('moveInfos', 'from=" + directory.getId() + "&to={id}')\", \"\", \"\", \"\", \"info\")");
		    batchActionGroup.addViewAction(viewAction);

		    //添加"批量删除"按钮
	    	viewAction = new ViewAction();
		    viewAction.setTitle("移除信息");
		    viewAction.setExecute("DialogUtils.openMessageDialog('移除信息', '是否确定要移除？', '确定,取消', 'warn', 200, 60, 'FormUtils.doAction(\\'moveInfos\\', \\'from=" + directory.getId() + "&remove=true\\')')");
		    batchActionGroup.addViewAction(viewAction);

		    if(popedoms.contains("manager") && publicDirectoryService.checkPopedom(0, "manager", sessionInfo)) { //只允许根目录管理员彻底删除
			    //添加"彻底删除"按钮
		    	viewAction = new ViewAction();
			    viewAction.setTitle("彻底删除");
			    viewAction.setExecute("DialogUtils.openMessageDialog('彻底删除', '您选中的信息将从所有隶属目录（包括同步的网站栏目）中删除且不可恢复，是否确定要删除？', '确定,取消', 'warn', 200, 60, 'FormUtils.doAction(\\'moveInfos\\', \\'physicalDelete=true\\')')");
			    batchActionGroup.addViewAction(viewAction);
		    }
		    
		    //添加"引用信息"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("引用信息");
		    viewAction.setExecute("selectSite(600, 400, true, \"{id},{name|选择栏目|100%}\", \"FormUtils.doAction('importResources', 'to={id}')\", \"\", \"\", \"column\")");
		    batchActionGroup.addViewAction(viewAction);
		}
		
		if(popedoms.contains("manager")) {
			//检查用户是否上级目录的管理员
	    	String formName = null;
	    	if(directory instanceof PublicMainDirectory) {
	    		formName = "mainDirectory";
	    	}
	    	else if(directory instanceof PublicDirectoryCategory) {
	    		formName = "directoryCategory";
	    	}
	    	else if(directory instanceof PublicInfoDirectory) {
	    		formName = "infoDirectory";
	    	}
	    	else if(directory instanceof PublicArticleDirectory) {
	    		formName = "articleDirectory";
	    	}
	    	viewAction = new ViewAction();
	    	viewAction.setTitle("修改" + publicDirectoryService.getDirectoryTypeTitle(directory.getDirectoryType()));
	    	viewAction.setExecute("PageUtils.editrecord('cms/infopublic', 'admin/" + formName + "', '" + publicInfoViewForm.getDirectoryId() + "', 'mode=fullscreen');");
	    	actions.add(viewAction);
	   
			if(directory instanceof PublicMainDirectory) {
				viewAction = new ViewAction();
				viewAction.setTitle("新建主目录");
				viewAction.setGroupTitle("主目录");
				viewAction.setExecute("PageUtils.newrecord('cms/infopublic', 'admin/mainDirectory', 'mode=fullscreen', 'parentDirectoryId=" + publicInfoViewForm.getDirectoryId() + "');");
				newRecordActionGroup.addViewAction(viewAction);

				viewAction = new ViewAction();
				viewAction.setTitle("新建文章目录");
				viewAction.setGroupTitle("文章目录");
				viewAction.setExecute("PageUtils.newrecord('cms/infopublic', 'admin/articleDirectory', 'mode=fullscreen', 'parentDirectoryId=" + publicInfoViewForm.getDirectoryId() + "');");
				newRecordActionGroup.addViewAction(viewAction);
			}
			if(directory instanceof PublicMainDirectory || directory instanceof PublicDirectoryCategory) {
				viewAction = new ViewAction();
				viewAction.setTitle("新建信息分类");
				viewAction.setGroupTitle("信息分类");
				viewAction.setExecute("PageUtils.newrecord('cms/infopublic', 'admin/directoryCategory', 'mode=fullscreen', 'parentDirectoryId=" + publicInfoViewForm.getDirectoryId() + "');");
				newRecordActionGroup.addViewAction(viewAction);
			}
			if(!(directory instanceof PublicArticleDirectory)) {
				viewAction = new ViewAction();
				viewAction.setTitle("新建信息目录");
				viewAction.setGroupTitle("信息目录");
				viewAction.setExecute("PageUtils.newrecord('cms/infopublic', 'admin/infoDirectory', 'mode=fullscreen', 'parentDirectoryId=" + publicInfoViewForm.getDirectoryId() + "');");
				newRecordActionGroup.addViewAction(viewAction);
			}
			
			viewAction = new ViewAction();
			viewAction.setTitle("调整优先级");
			viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/infopublic/admin/adjustDirectoryPriority.shtml?parentDirectoryId=" + publicInfoViewForm.getDirectoryId() + "', 640, 400)");
			actions.add(viewAction);
			
			if(directory instanceof PublicMainDirectory) {
				//添加导出按钮
				viewAction = new ViewAction();
				viewAction.setTitle("信息导出");
				viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/infopublic/admin/exportPublicInfo.shtml?directoryId=" + directory.getId() + "&directoryName=" + URLEncoder.encode(directory.getDirectoryName(), "utf-8") + "', '90%', '90%')");
				actions.add(viewAction);
				
				//添加信息公开统计按钮
				viewAction = new ViewAction();
				viewAction.setTitle("信息统计");
				viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/infopublic/admin/infoPublicStat.shtml?directoryId=" + directory.getId() + "&directoryName=" + URLEncoder.encode(directory.getDirectoryName(), "utf-8") + "', '80%', '80%')");
				actions.add(viewAction);
				
				//添加"监察统计"按钮
				viewAction = new ViewAction();
				viewAction.setTitle("监察统计");
				viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/infopublic/admin/monitoringReport.shtml?directoryId=" + directory.getId() + "&directoryName=" + URLEncoder.encode(directory.getDirectoryName(), "utf-8") + "', 600, 360)");
				actions.add(viewAction);
			}
			
		    //添加"修改文章访问者"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("信息授权");
		    viewAction.setGroupTitle("信息授权");
		    viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/infopublic/admin/readersModifyOption.shtml?directoryId=" + directory.getId() + "&directoryName=" + URLEncoder.encode(directory.getDirectoryName(), "utf-8") + "', 430, 200)");
		    batchActionGroup.addViewAction(viewAction);

		    if(directory instanceof PublicMainDirectory) {
			    //添加"重建索引号"按钮
			    viewAction = new ViewAction();
			    viewAction.setTitle("重建索引号");
			    viewAction.setGroupTitle("重建索引号");
			    viewAction.setExecute("if(confirm('重建后索引号不可恢复，是否确定重建索引号？'))DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/infopublic/admin/regenerateIndex.shtml?mainDirectoryId=" + directory.getId() + "&directoryName=" + URLEncoder.encode(directory.getDirectoryName(), "utf-8") + "', 430, 200)");
			    batchActionGroup.addViewAction(viewAction);
		    }
		}
		List viewActions = viewForm.getViewPackage().getView().getActions();
		if(viewActions!=null && !viewActions.isEmpty()) { //注：非根目录,清除原有的模板配置按钮
			actions.addAll(viewActions);
		}
		view.setActions(actions);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		PublicInfoView publicInfoViewForm = (PublicInfoView)viewForm;
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		location.remove(location.size()-1);
		String fullName = publicDirectoryService.getDirectoryFullName(publicInfoViewForm.getDirectoryId(), "/", "main");
		if(fullName!=null) {
			String[] names = fullName.split("/");
			for(int i=0; i<names.length; i++) {
				location.add(names[i]);
			}
		}
	}
}
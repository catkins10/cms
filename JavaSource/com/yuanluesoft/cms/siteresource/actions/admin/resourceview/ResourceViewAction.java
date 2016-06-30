package com.yuanluesoft.cms.siteresource.actions.admin.resourceview;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.cms.sitemanage.pojo.WebColumn;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.pojo.WebViewReference;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.forms.ResourceView;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewActionGroup;

/**
 *
 * @author linchuan
 * 
 */
public class ResourceViewAction extends ViewFormAction {
     
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		WebDirectory directory = getWebDirectory(viewForm, request); //获取目录
		return directory instanceof WebViewReference ? ((WebViewReference)directory).getApplicationName() : "cms/sitemanage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		WebDirectory directory = getWebDirectory(viewForm, request); //获取目录
		return directory instanceof WebViewReference ? ((WebViewReference)directory).getViewName() : "admin/resource";
	}
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		int actionIndex = 0;
		if(view.getActions()==null) {
	        view.setActions(new ArrayList());
	    }
		ResourceView resourceViewForm = (ResourceView)viewForm;
	    SiteService siteService = (SiteService)getService("siteService");
	    //获取用户对站点/栏目的权限
	    List popedoms = siteService.listDirectoryPopedoms(resourceViewForm.getSiteId(), sessionInfo);
	    if(popedoms==null) {
	    	popedoms = new ArrayList();
	    }
	    WebDirectory directory = getWebDirectory(viewForm, request); //获取目录
	    if(directory instanceof WebViewReference) { //视图引用
	    	WebViewReference reference = (WebViewReference)directory;
	    	if(popedoms.contains("manager")) {
	    		//添加"修改引用"按钮
		    	ViewAction viewAction = new ViewAction();
    		    viewAction.setTitle("修改引用");
			    viewAction.setExecute("PageUtils.editrecord('cms/sitemanage', 'reference', '" + directory.getId() + "', 'mode=fullscreen')");
			    view.getActions().add(actionIndex++, viewAction);
			    
			    //添加"模板设置"按钮
		    	viewAction = new ViewAction();
			    viewAction.setTitle("模板设置");
			    viewAction.setExecute("location.href='" + Environment.getContextPath() + "/cms/templatemanage/templateView.shtml?siteId=" + directory.getId() + "&applicationName=cms/sitemanage" + "&pageName=index'");
			    view.getActions().add(actionIndex++, viewAction);
			    
		    	//添加"修改主页"按钮
			    viewAction = new ViewAction();
			    viewAction.setTitle("浏览主页");
			    viewAction.setExecute("window.open('" + Environment.getContextPath() + "/cms/sitemanage/index.shtml?siteId=" + directory.getId() + "')");
			    view.getActions().add(actionIndex++, viewAction);
	    	}
	    	//设置属性
	    	request.setAttribute("referenceParameter", reference.getReferenceParameter());
	    	//处理新增的join
	    	String join = (String)view.getExtendParameter("siteReferenceJoin");
	    	if(join!=null && !join.isEmpty()) {
	    		view.addJoin(join);
	    	}
	    	//处理新增的where
	    	String where = (String)view.getExtendParameter("siteReferenceWhere");
	    	if(where!=null && !where.isEmpty()) {
	    		view.addWhere(where);
	    	}
		    return;
	    }
	    
	    if(resourceViewForm.getSiteId()!=0) { //主站显示全部
	    	view.addJoin(", WebDirectorySubjection WebDirectorySubjection");
			String where = "subjections.siteId=WebDirectorySubjection.directoryId" +
						   " and WebDirectorySubjection.parentDirectoryId=" +  resourceViewForm.getSiteId();
			view.addWhere(where);
		}
		
    	ViewActionGroup newRecordActionGroup = new ViewActionGroup("新建");
    	view.getActions().add(actionIndex++, newRecordActionGroup);
	    if(popedoms.contains("editor") && (directory instanceof WebColumn)) { //用户是站点/栏目编辑,且当前是栏目
	    	//添加"添加文章"按钮
		    ViewAction viewAction = new ViewAction();
		    viewAction.setTitle("添加文章");
		    viewAction.setGroupTitle("文章");
		    viewAction.setExecute("PageUtils.newrecord('cms/siteresource', 'admin/article', 'mode=fullscreen', 'columnId=" + directory.getId() + "')");
		    newRecordActionGroup.addViewAction(viewAction);
		    //添加"添加连载"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("添加连载");
		    viewAction.setGroupTitle("连载");
		    viewAction.setExecute("PageUtils.newrecord('cms/siteresource', 'admin/serialization', 'mode=fullscreen', 'columnId=" + directory.getId() + "')");
		    //newRecordActionGroup.addViewAction(viewAction);
		    //添加"添加链接"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("添加链接");
		    viewAction.setGroupTitle("链接");
		    viewAction.setExecute("PageUtils.newrecord('cms/siteresource', 'admin/link', 'mode=fullscreen', 'columnId=" + directory.getId() + "')");
		    newRecordActionGroup.addViewAction(viewAction);
		}
	    
	    ViewActionGroup batchActionGroup = new ViewActionGroup("批量处理");
    	view.getActions().add(actionIndex++, batchActionGroup);
	    ViewAction viewAction = new ViewAction();
	    viewAction.setTitle("办理文章");
	    viewAction.setExecute("batchSend('../siteresource/admin/runArticleApproval')");
	    batchActionGroup.addViewAction(viewAction);
	    
	    if(popedoms.contains("editor") || popedoms.contains("manager")) { //用户是站点/栏目编辑或管理员
	    	//添加"修改主页"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("浏览主页");
		    viewAction.setExecute("window.open('" + Environment.getContextPath() + "/cms/sitemanage/index.shtml" + (directory.getId()==0 ? "" : "?siteId=" + directory.getId()) + "')");
		    view.getActions().add(actionIndex++, viewAction);
		    
	        //添加"移动文章"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("移动文章");
		    viewAction.setExecute("selectSite(600, 400, true, \"{id},{name|选择栏目|100%}\", \"FormUtils.doAction('moveResources', 'from=" + directory.getId() + "&to={id}')\", \"\", \"\", \"column\")");
		    batchActionGroup.addViewAction(viewAction);

		    //添加"引用文章"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("同步文章");
		    viewAction.setExecute("selectSite(600, 400, true, \"{id},{name|选择栏目|100%}\", \"FormUtils.doAction('importResources', 'from=" + directory.getId() + "&to={id}')\", \"\", \"\", \"column\")");
		    batchActionGroup.addViewAction(viewAction);
		    
		    //添加"批量移除"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("移除文章");
		    viewAction.setExecute("DialogUtils.openMessageDialog('移除文章', '是否确定要移除？', '确定,取消', 'warn', 200, 60, 'FormUtils.doAction(\\'moveResources\\', \\'from=" + directory.getId() + "&remove=true\\')')");
		    batchActionGroup.addViewAction(viewAction);

		    if(popedoms.contains("manager") && siteService.checkPopedom(0, "manager", sessionInfo)) { //根目录的管理员
			    //添加"彻底删除"按钮
			    viewAction = new ViewAction();
			    viewAction.setTitle("彻底删除");
			    viewAction.setExecute("DialogUtils.openMessageDialog('彻底删除', '您选中的文章将从所有隶属栏目中删除且不可恢复，是否确定要彻底删除？', '确定,取消', 'warn', 200, 60, 'FormUtils.doAction(\\'moveResources\\', \\'physicalDelete=true\\')')");
			    batchActionGroup.addViewAction(viewAction);
		    }
		    
		    if(directory instanceof WebColumn) {
		    	//添加"启动抓取任务"按钮
		    	SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		    	List tasks = siteResourceService.listCaptureTasksByColumnId(directory.getId());
		    	if(tasks!=null && !tasks.isEmpty()) {
		    		viewAction = new ViewAction();
				    viewAction.setTitle("启动抓取任务");
				    request.getSession().setAttribute(CaptureService.ATTRIBUTE_STARTUP_TASK_IDS, ListUtils.join(tasks, "id", ",", false));
				    viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/capture/startupTasks.shtml', 500, 260)");
				    view.getActions().add(actionIndex++, viewAction);
		    	}
		    }
		}
	    
	    //检查用户是否站点/栏目管理员
	    if(popedoms.contains("manager")) {
	       	//添加"修改站点/栏目信息"按钮
    		viewAction = new ViewAction();
		    if(directory instanceof WebSite) {
			    viewAction.setTitle("修改站点");
			    viewAction.setExecute("PageUtils.editrecord('cms/sitemanage', 'site', '" + directory.getId() + "', 'mode=fullscreen')");
		    }
		    else if(directory instanceof WebColumn) {
		    	viewAction.setTitle("修改栏目");
			    viewAction.setExecute("PageUtils.editrecord('cms/sitemanage', 'column', '" + directory.getId() + "', 'mode=fullscreen')");
		    }
		    view.getActions().add(actionIndex++, viewAction);
	    
			//添加"新建子站"按钮
		    if(directory instanceof WebSite) {
			    viewAction = new ViewAction();
			    viewAction.setTitle("新建子站");
			    viewAction.setGroupTitle("子站");
			    viewAction.setExecute("PageUtils.newrecord('cms/sitemanage', 'site', 'mode=fullscreen', 'parentDirectoryId=" + directory.getId() + "')");
			    newRecordActionGroup.addViewAction(viewAction);
		    }
		    
		    if(!(directory instanceof WebViewReference)) {
			    //添加"新建栏目"按钮
			    viewAction = new ViewAction();
			    viewAction.setTitle("新建栏目");
			    viewAction.setGroupTitle("栏目");
			    viewAction.setExecute("PageUtils.newrecord('cms/sitemanage', 'column', 'mode=fullscreen', 'parentDirectoryId=" + directory.getId() + "')");
			    newRecordActionGroup.addViewAction(viewAction);
		    }
		    
		    //添加"新建引用"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("新建引用");
		    viewAction.setGroupTitle("引用");
		    viewAction.setExecute("PageUtils.newrecord('cms/sitemanage', 'reference', 'mode=fullscreen', 'parentDirectoryId=" + directory.getId() + "')");
		    newRecordActionGroup.addViewAction(viewAction);
		    
		    if(!(directory instanceof WebViewReference)) {
			    //添加"头版头条设置"按钮
			    viewAction = new ViewAction();
			    viewAction.setTitle("头版头条");
			    viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/sitemanage/headline.shtml?directoryIds=" + directory.getId() + "', 470, 260)");
			    view.getActions().add(actionIndex++, viewAction);
		    }
		    
		    //添加"调整优先级"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("调整优先级");
		    viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/sitemanage/adjustSitePriority.shtml?parentSiteId=" + directory.getId() + "', 640, 400)");
		    view.getActions().add(actionIndex++, viewAction);
		    
		    //添加"PORTAL配置"按钮
		    if(directory instanceof WebSite) {
			    viewAction = new ViewAction();
			    viewAction.setTitle("PORTAL配置");
			    viewAction.setExecute("location.href='" + Environment.getContextPath() + "/portal/portletEntityView.shtml?orgId=-1&siteId=" + directory.getId() + "'");
			    view.getActions().add(actionIndex++, viewAction);
		    }

		    //添加"模板设置"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("模板设置");
		    viewAction.setExecute("location.href='" + Environment.getContextPath() + "/cms/templatemanage/templateView.shtml?siteId=" + directory.getId() + "&applicationName=cms/sitemanage" + "&pageName=index'");
		    view.getActions().add(actionIndex++, viewAction);
		    
		    //添加"修改文章访问者"按钮
		    viewAction = new ViewAction();
		    viewAction.setTitle("文章授权");
		    viewAction.setGroupTitle("文章授权");
		    viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/siteresource/admin/readersModifyOption.shtml?siteId=" + directory.getId() + "', 430, 200)");
		    batchActionGroup.addViewAction(viewAction);
	    }
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		SiteService siteService = (SiteService)getService("siteService");
		ResourceView resourceViewForm = (ResourceView)viewForm;
		location.remove(location.size()-1);
		String siteFullName = siteService.getDirectoryFullName(resourceViewForm.getSiteId(), "/", "site");
		if(siteFullName!=null) {
			String[] orgFullNames = siteFullName.split("/");
			for(int i=0; i<orgFullNames.length; i++) {
				location.add(orgFullNames[i]);
			}
		}
	}
	
	/**
	 * 获取WEB目录
	 * @param viewForm
	 * @param request
	 * @return
	 */
	private WebDirectory getWebDirectory(ViewForm viewForm, HttpServletRequest request) {
		WebDirectory directory = (WebDirectory)request.getAttribute("webDirectory");
		if(directory==null) {
			try {
				ResourceView resourceViewForm = (ResourceView)viewForm;
				SiteService siteService = (SiteService)getService("siteService");
				directory = (WebDirectory)siteService.getDirectory(resourceViewForm.getSiteId()); //获取站点信息
				request.setAttribute("webDirectory", directory);
			}
			catch (Exception e) {
				throw new Error(e.getMessage());
			}
		}
		return directory;
	}
}
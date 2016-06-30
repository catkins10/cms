package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitemview;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItemView;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
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
public class ServiceItemViewAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "cms/onlineservice";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "admin/serviceItem";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
	    ServiceItemView serviceItemViewForm = (ServiceItemView)viewForm;
	    if(serviceItemViewForm.getDirectoryId()!=0) { //根目录显示全部
	    	view.addJoin(", OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection");
	    	String where = "subjections.directoryId=OnlineServiceDirectorySubjection.directoryId" +
	    				   "  and OnlineServiceDirectorySubjection.parentDirectoryId=" +  serviceItemViewForm.getDirectoryId();
	    	view.addWhere(where);
	    }
	    OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		OnlineServiceDirectory directory = (OnlineServiceDirectory)onlineServiceDirectoryService.getDirectory(serviceItemViewForm.getDirectoryId());
		//获取用户对目录的权限
		List popedoms = onlineServiceDirectoryService.listDirectoryPopedoms(serviceItemViewForm.getDirectoryId(), sessionInfo);
		if(popedoms==null) {
			popedoms = new ArrayList();
		}
		List actions = new ArrayList();
		
		ViewActionGroup newItemActionGroup = new ViewActionGroup("新增行政职权");
		ViewActionGroup newDirectoryActionGroup = new ViewActionGroup("新建目录");
    	ViewActionGroup batchActionGroup = new ViewActionGroup("批量处理");

    	if(!sessionInfo.getLoginName().startsWith("ck_")) { //for 漳州行政服务中心,禁止窗口人员操作
    		actions.add(newItemActionGroup);
    		actions.add(newDirectoryActionGroup);
        	actions.add(batchActionGroup);
		}
    	
		if((popedoms.contains("manager") || popedoms.contains("transactor")) && directory.getDirectoryType().equals("directory")) { // && !onlineServiceDirectoryService.hasChildDirectories(directory.getId())
			List itemTypes = FieldUtils.listSelectItems(FieldUtils.getRecordField(OnlineServiceItem.class.getName(), "itemType", request), null, request);
			for(Iterator iterator = itemTypes.iterator(); iterator.hasNext();) {
				Object[] values = (Object[])iterator.next();
				ViewAction viewAction = new ViewAction();
				viewAction.setTitle("新增" + values[0]);
				viewAction.setGroupTitle("" + values[0]);
				viewAction.setExecute("PageUtils.newrecord('cms/onlineservice', 'admin/serviceItem', 'mode=fullscreen', 'directoryId=" + serviceItemViewForm.getDirectoryId() + "&itemType=" + URLEncoder.encode("" + values[0], "utf-8") + "')");
				newItemActionGroup.addViewAction(viewAction);
			}
			ViewAction viewAction = new ViewAction();
			viewAction.setTitle("引用办理事项");
			viewAction.setGroupTitle("引用办理事项");
			viewAction.setExecute("selectOnlineServiceItem(640, 400, true, 'importServiceItemIds{id},importServiceItemNames{name|办事事项|100%}', 'FormUtils.doAction(\"importServiceItems\")', '', ',', '" + serviceItemViewForm.getDirectoryId() + "')");
			batchActionGroup.addViewAction(viewAction);
			
			viewAction = new ViewAction();
			viewAction.setTitle("导入行政职权目录");
			viewAction.setExecute("DialogUtils.openDialog('" + request.getContextPath() + "/cms/onlineservice/admin/importAuthority.shtml?directoryId=" + serviceItemViewForm.getDirectoryId() + "', 400, 200)");
			actions.add(viewAction);
		}
		if((popedoms.contains("manager") || popedoms.contains("transactor"))) {
			ViewAction viewAction = new ViewAction();
			viewAction.setTitle("移除办理事项");
			viewAction.setGroupTitle("移除办理事项");
			viewAction.setExecute("DialogUtils.openMessageDialog('移除办理事项', '是否确定要移除？', '确定,取消', 'warn', 200, 60, 'FormUtils.doAction(\\'moveServiceItems\\', \\'from=" + serviceItemViewForm.getDirectoryId() + "&remove=true\\')')");
			batchActionGroup.addViewAction(viewAction);
		}
		if(popedoms.contains("manager") && onlineServiceDirectoryService.checkPopedom(0, "manager", sessionInfo)) { //根目录的管理员
		    //添加"彻底删除"按钮
			ViewAction viewAction = new ViewAction();
		    viewAction.setTitle("彻底删除");
		    viewAction.setExecute("DialogUtils.openMessageDialog('彻底删除', '您选中的事项将从所有隶属目录中删除且不可恢复，是否确定要彻底删除？', '确定,取消', 'warn', 200, 60, 'FormUtils.doAction(\\'moveServiceItems\\', \\'physicalDelete=true\\')')");
		    batchActionGroup.addViewAction(viewAction);
	    }
		if(popedoms.contains("manager")) {
			//检查用户是否上级目录的管理员
			ViewAction viewAction = new ViewAction();
			viewAction.setTitle("修改" + onlineServiceDirectoryService.getDirectoryTypeTitle(directory.getDirectoryType()));
			viewAction.setExecute("PageUtils.editrecord('cms/onlineservice', 'admin/" + directory.getDirectoryType() + "', '" + serviceItemViewForm.getDirectoryId() + "', 'mode=fullscreen')");
			actions.add(viewAction);
	
			if(directory.getDirectoryType().equals("mainDirectory")) { //主目录
				viewAction = new ViewAction();
				viewAction.setTitle("新建主目录");
				viewAction.setGroupTitle("主目录");
				viewAction.setExecute("PageUtils.newrecord('cms/onlineservice', 'admin/mainDirectory', 'mode=fullscreen', 'parentDirectoryId=" + serviceItemViewForm.getDirectoryId() + "')");
				newDirectoryActionGroup.addViewAction(viewAction);
			}
			
			viewAction = new ViewAction();
			viewAction.setTitle("新建子目录");
			viewAction.setGroupTitle("子目录");
			viewAction.setExecute("PageUtils.newrecord('cms/onlineservice', 'admin/directory', 'mode=fullscreen', 'parentDirectoryId=" + serviceItemViewForm.getDirectoryId() + "')");
			newDirectoryActionGroup.addViewAction(viewAction);
			
			viewAction = new ViewAction();
			viewAction.setTitle("调整优先级");
			viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/cms/onlineservice/admin/adjustDirectoryPriority.shtml?parentDirectoryId=" + serviceItemViewForm.getDirectoryId() + "', 640, 400)");
			actions.add(viewAction);
			
			//添加"事项编号规则配置"按钮
			if(serviceItemViewForm.getDirectoryId()==0) {
	            viewAction = new ViewAction();
	            viewAction.setTitle("事项编号规则配置");
	            viewAction.setExecute("location.href='" + Environment.getContextPath() + "/jeaf/application/applicationView.shtml?applicationName=cms/onlineservice&viewName=admin/serviceItemCodeRule'");
	            actions.add(viewAction);
			}
		}
		if(view.getActions()!=null && !view.getActions().isEmpty()) {
			actions.addAll(view.getActions());
		}
		view.setActions(actions);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		ServiceItemView serviceItemViewForm = (ServiceItemView)viewForm;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		location.remove(location.size()-1);
		String fullName = onlineServiceDirectoryService.getDirectoryFullName(serviceItemViewForm.getDirectoryId(), "/", null);
		if(fullName!=null) {
			String[] names = fullName.split("/");
			for(int i=0; i<names.length; i++) {
				location.add(names[i]);
			}
		}
	}
}
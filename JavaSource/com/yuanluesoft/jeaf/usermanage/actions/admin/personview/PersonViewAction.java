package com.yuanluesoft.jeaf.usermanage.actions.admin.personview;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.forms.admin.PersonView;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
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
public class PersonViewAction extends ViewFormAction {
   
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "jeaf/usermanage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "person";
	}
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		PersonView personViewForm = (PersonView)viewForm;
		//设置URL
		String webApplicationSafeUrl = (String)getBean("webApplicationSafeUrl");
		personViewForm.setWebApplicationSafeUrl(webApplicationSafeUrl);
		if(view.getActions()==null) {
	        view.setActions(new ArrayList());
	    }
		personViewForm.getViewPackage().getView();
		String where = null;
		if(personViewForm.getOrgId()!=0) { //非根组织
			view.addJoin(", OrgSubjection OrgSubjection");
			where = "subjections.orgId=OrgSubjection.directoryId" +
				    " and OrgSubjection.parentDirectoryId=" +  personViewForm.getOrgId();
		}
		OrgService orgService = getOrgService();

	    //获取组织机构类型
    	String orgType = orgService.getOrg(personViewForm.getOrgId()).getDirectoryType();
    	
    	//获取用户对该组织的注册权限
    	List popedoms = orgService.listDirectoryPopedoms(personViewForm.getOrgId(), sessionInfo);
    	if(popedoms==null) {
    		popedoms = new ArrayList();
    	}
	    //检查用户是否有本组织机构的完全控制权限
	    if(popedoms.contains("manager")) {
			//检查用户是否上级目录的管理员
    		//添加"修改目录信息"按钮
    		ViewAction viewAction = new ViewAction();
        	viewAction.setTitle("修改" + orgService.getDirectoryTypeTitle(orgType));
    		viewAction.setExecute("PageUtils.editrecord('jeaf/usermanage', 'admin/" + orgType + "', '" + personViewForm.getOrgId() + "', 'mode=fullscreen')");
    		view.getActions().add(viewAction);
	    }
	    else if(personViewForm.getOrgId()==0) { //根组织,且用户没有完全控制权限
			where = (where==null ? "" : "(" + where + ") and ") + "Person.preassign!='1'"; //不允许查看系统预置用户
	    }
	    view.addWhere(where);

	    ViewActionGroup registActionGroup = new ViewActionGroup("注册");
    	view.getActions().add(registActionGroup);
    	
	    String orgTypes = "," + orgService.getOrgTypes() + ","; 
	    if(popedoms.contains("manager")) { //判断是否有注册下级机构的权限
	    	//添加"注册分类"按钮
	    	if(orgType.equals("root") || orgType.equals("area") || orgType.equals("category")) {
	    		registActionGroup.addViewAction(createOrgAction(personViewForm.getOrgId(), "category", "注册分类", "分类"));
	    	}
	    	//添加"注册地区"按钮
	    	if(orgTypes.indexOf(",area,")!=-1 &&
	    	   (orgType.equals("root") || orgType.equals("area") || orgType.equals("category"))) {
	    		registActionGroup.addViewAction(createOrgAction(personViewForm.getOrgId(), "area", "注册区域", "区域"));
	    	}
	    	//添加"注册单位"按钮
	    	if(orgTypes.indexOf(",unit,")!=-1 &&
	    	   (orgType.equals("root") || orgType.equals("area") || orgType.equals("unit") || orgType.equals("category"))) {
	    		registActionGroup.addViewAction(createOrgAction(personViewForm.getOrgId(), "unit", "注册单位", "单位"));
	    	}
	    	//添加"注册学校"按钮
	    	if(orgTypes.indexOf(",school,")!=-1 &&
	    	   (orgType.equals("root") || orgType.equals("area") || orgType.equals("category"))) {
	    		registActionGroup.addViewAction(createOrgAction(personViewForm.getOrgId(), "school", "注册学校", "学校"));
	    	}
	    	//添加"注册部门"按钮
	    	if(orgTypes.indexOf(",unitDepartment,")!=-1 &&
	    	   (orgType.equals("unit")|| orgType.equals("unitDepartment"))) {
	    		registActionGroup.addViewAction(createOrgAction(personViewForm.getOrgId(), "unitDepartment", "注册部门", "部门"));
	    	}
	    	if(orgTypes.indexOf(",schoolDepartment,")!=-1 &&
	 	       (orgType.equals("school")|| orgType.equals("schoolDepartment"))) {
	    		registActionGroup.addViewAction(createOrgAction(personViewForm.getOrgId(), "schoolDepartment", "注册部门", "部门"));
	 	    }
	    	//添加"注册年级"按钮
	    	if(orgTypes.indexOf(",school,")!=-1 && orgType.equals("school")) {
	    		registActionGroup.addViewAction(createOrgAction(personViewForm.getOrgId(), "schoolGrade", "注册年级", "年级"));
	    	}
	    	//添加"注册班级"按钮
	    	if(orgTypes.indexOf(",school,")!=-1 && orgType.equals("school")) {
	    		registActionGroup.addViewAction(createOrgAction(personViewForm.getOrgId(), "schoolClass", "注册班级", "班级"));
	    	}
		}
	    if(orgTypes.indexOf(",school,")!=-1) { //支持学校管理
		    //添加"注册教师"按钮
		    if((popedoms.contains("manager") || popedoms.contains("registTeacher")) && //有注册教师的权限
		       (orgType.equals("unit")|| orgType.equals("unitDepartment") || //单位或部门
		        orgType.equals("school") || orgType.equals("schoolDepartment"))) { //学校或学校的部门
		    	registActionGroup.addViewAction(createPersonAction(personViewForm.getOrgId(), "teacher", "注册教师", "教师"));
		    }
		    //添加"注册学生"按钮
		    if((popedoms.contains("manager") || popedoms.contains("registStudent")) && //注册学生得权限
		       orgType.equals("schoolClass")) { //班级
		    	registActionGroup.addViewAction(createPersonAction(personViewForm.getOrgId(), "student", "注册学生", "学生"));
		    }
	    }
	    //添加"注册用户"按钮
	    if((popedoms.contains("manager") || popedoms.contains("registEmployee")) && //有注册工作人员的权限
	 	   (orgType.equals("school") || orgType.equals("unit")|| orgType.equals("schoolDepartment") || orgType.equals("unitDepartment"))) {
	    	registActionGroup.addViewAction(createPersonAction(personViewForm.getOrgId(), "employee", "注册用户", "用户"));
		    
		    ViewAction viewAction = new ViewAction();
            viewAction.setTitle("批量注册用户");
            viewAction.setGroupTitle("用户批量注册");
            viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/jeaf/usermanage/admin/batchRegistEmployees.shtml?orgId=" +  personViewForm.getOrgId() + "', 400, 150)");
            registActionGroup.addViewAction(viewAction);
        }
	    if(popedoms.contains("manager")) {
	    	//添加"角色管理"按钮
            ViewAction viewAction = new ViewAction();
            viewAction.setTitle("角色管理");
            viewAction.setExecute("location.href='roleView.shtml?orgId=" + personViewForm.getOrgId() + "'");
            view.getActions().add(viewAction);
            
            //添加"调整组织机构优先级"按钮
	    	viewAction = new ViewAction();
            viewAction.setTitle("调整机构优先级");
            viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/jeaf/usermanage/admin/adjustOrgPriority.shtml?orgId=" +  personViewForm.getOrgId() + "', 720,480)");
            view.getActions().add(viewAction);

            //添加"调整用户优先级"按钮
            viewAction = new ViewAction();
            viewAction.setTitle("调整用户优先级");
            viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/jeaf/usermanage/admin/adjustPersonPriority.shtml?orgId=" +  personViewForm.getOrgId() + "', 720, 480)");
            view.getActions().add(viewAction);
            
            if(personViewForm.getOrgId()==0) { //根目录
            	  viewAction = new ViewAction();
            	  viewAction.setTitle("用户复制");
            	  viewAction.setExecute("DialogUtils.openDialog('" + Environment.getContextPath() + "/jeaf/usermanage/admin/userReplicate.shtml', 400, 150)");
            	  view.getActions().add(viewAction);
            }
            
		    //添加"PORTAL配置"按钮
	        viewAction = new ViewAction();
		    viewAction.setTitle("PORTAL配置");
		    viewAction.setExecute("location.href='" + Environment.getContextPath() + "/portal/portletEntityView.shtml?orgId=" + personViewForm.getOrgId() + "&siteId=-1'");
		    view.getActions().add(viewAction);
	                
            //添加“模板配置”按钮
            viewAction = new ViewAction();
            viewAction.setTitle("模板配置");
            viewAction.setExecute("DialogUtils.userPageTemplateConfigure('" + personViewForm.getOrgId() + "')");
            view.getActions().add(viewAction);
	    }
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		PersonView personViewForm = (PersonView)viewForm;
		location.remove(location.size()-1);
    	//获取组织全称
	    String orgFullName = getOrgService().getDirectoryFullName(personViewForm.getOrgId(), "/", "unit,school");
		String[] orgFullNames = orgFullName.split("/");
		for(int i=0; i<orgFullNames.length; i++) {
			location.add(orgFullNames[i]);
		}
	}
	
	/**
	 * 创建注册组织机构按钮
	 * @param parentOrgId
	 * @param orgType
	 * @param actionTitle
	 * @param groupTitle
	 * @return
	 * @throws Exception
	 */
	private ViewAction createOrgAction(long parentOrgId, String orgType, String actionTitle, String groupTitle) throws Exception {
		ViewAction viewAction = new ViewAction();
	    viewAction.setTitle(actionTitle);
	    viewAction.setGroupTitle(groupTitle);
	    viewAction.setExecute("PageUtils.openurl('" + orgType + ".shtml?act=create&parentDirectoryId=" + parentOrgId + "', 'width=720,height=480', '" + orgType + "')");
	    return viewAction;
	}
	
	/**
	 * 创建注册用户按钮
	 * @param orgId
	 * @param personType
	 * @param actionTitle
	 * @param groupTitle
	 * @return
	 * @throws Exception
	 */
	private ViewAction createPersonAction(long orgId, String personType, String actionTitle, String groupTitle) throws Exception {
	    ViewAction viewAction = new ViewAction();
	    viewAction.setTitle(actionTitle);
	    viewAction.setGroupTitle(groupTitle);
	    viewAction.setExecute("PageUtils.openurl('" + getBean("webApplicationSafeUrl") + "/jeaf/usermanage/admin/" + personType + ".shtml?act=create&orgId=" + orgId + "', 'width=720,height=480', '" + personType + "')");
	    return viewAction;
	}
}
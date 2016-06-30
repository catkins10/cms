package com.yuanluesoft.jeaf.usermanage.actions.admin.orgrelationconfigview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.forms.admin.OrgRelationConfigView;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class OrgRelationConfigViewAction extends ViewFormAction {

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		OrgRelationConfigView configView = (OrgRelationConfigView)viewForm;
		boolean shareView = configView.getViewApplicationName()!=null && !configView.getViewApplicationName().equals(""); //是否共享视图
		return shareView ? configView.getViewApplicationName() : configView.getApplicationName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		OrgRelationConfigView configView = (OrgRelationConfigView)viewForm;
		return configView.getViewName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		OrgRelationConfigView configView = (OrgRelationConfigView)viewForm;
		boolean shareView = configView.getViewApplicationName()!=null && !configView.getViewApplicationName().equals(""); //是否共享视图
		List fields = FieldUtils.listRecordFields(view.getPojoClassName(), null, null, null, null, true, false, false, true, 1);
		String pojoName = view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf(".") + 1);
		String where = null;
		String[] orgIdFieldNames = {"unitId", "orgId", "departmentId", "categroyId", "areaId", "schoolId"};
		for(int i=0; i<orgIdFieldNames.length; i++) {
			if(ListUtils.findObjectByProperty(fields, "name", orgIdFieldNames[i])!=null) {
				where = pojoName + "." + orgIdFieldNames[i] + "=" + configView.getOrgId();
			}
		}
    	if(shareView) { //视图是共享的
    		where += " and " + pojoName + ".applicationName='" + JdbcUtils.resetQuot(configView.getApplicationName()) + "'"; //添加应用过滤
    	}
		view.addWhere(where);
		
		//如果当前机构类型不在列表中,删除所有的视图操作
		Org currentOrg = getCurrentOrg(configView, request);
		if(configView.getOrgTypes()!=null && configView.getOrgTypes().indexOf(currentOrg.getDirectoryType())==-1) {
			view.setActions(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		OrgRelationConfigView configView = (OrgRelationConfigView)viewForm;
		Org currentOrg = getCurrentOrg(configView, request);
		location.add(currentOrg.getDirectoryName()); //添加当前机构的名称
	}
	
	/**
	 * 获取当前机构
	 * @param configView
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Org getCurrentOrg(OrgRelationConfigView configView, HttpServletRequest request) throws Exception {
		Org currentOrg = (Org)request.getAttribute("currentOrg");
		if(currentOrg==null) {
			currentOrg = (Org)getOrgService().getDirectory(configView.getOrgId());
			request.setAttribute("currentOrg", currentOrg);
		}
		return currentOrg;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.initForm(viewForm, request, sessionInfo);
		OrgRelationConfigView configView = (OrgRelationConfigView)viewForm;
		List acl = getAcl(configView.getApplicationName(), sessionInfo);
		OrgService orgService = getOrgService();
		String directoryTypeFilters = orgService.appendParentDirectoryTypes(configView.getOrgTypes());
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //应用程序管理员
			//创建目录树
			configView.setOrgTree(orgService.createDirectoryTree(0, directoryTypeFilters, null, null, null, sessionInfo)); //不过滤
		}
		else {
			Org currentOrg = null;
			String popedomNames = configView.getPopedomNames()==null || configView.getPopedomNames().isEmpty() ? "manager" : configView.getPopedomNames();
			if(!orgService.checkPopedom(configView.getOrgId(), popedomNames, sessionInfo)) { //用户不是当前机构的管理员
				currentOrg = orgService.getFirstAccessibleOrg(configView.getOrgTypes(), popedomNames, sessionInfo); //获取用户有管理权限的第一个机构
				if(currentOrg!=null) {
					configView.setOrgId(currentOrg.getId());
				}
			}
			//创建目录树
			configView.setOrgTree(orgService.createDirectoryTree(0, directoryTypeFilters, popedomNames, null, null, sessionInfo)); //按管理权限过滤
		}
	}
}
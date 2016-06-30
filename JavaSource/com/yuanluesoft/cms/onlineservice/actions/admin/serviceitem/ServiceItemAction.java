package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItem;
import com.yuanluesoft.cms.onlineservice.interactive.services.OnlineserviceInteractiveService;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemSubjection;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceMainDirectory;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ServiceItemAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		OnlineServiceItem serviceItem = (OnlineServiceItem)record;
		ServiceItem serviceItemForm = (ServiceItem)form;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		//获取用户的资源发布权限,只判断第一个目录
		long directoryId;
		if(serviceItemForm.getDirectoryName()!=null) {
			directoryId = serviceItemForm.getDirectoryId();
		}
		else {
			if(OPEN_MODE_CREATE.equals(openMode)) {
				directoryId = serviceItemForm.getDirectoryId();
			}
			else {
				directoryId = ((OnlineServiceItemSubjection)serviceItem.getSubjections().iterator().next()).getDirectoryId();
			}
		}
		//获取用户对目录的权限
		List popedoms = onlineServiceDirectoryService.listDirectoryPopedoms(directoryId, sessionInfo);
		List acl = new ArrayList();
		if(popedoms==null) {
			return acl;
		}
		if(popedoms.contains("manager")) {
			acl.add("manager");
			serviceItemForm.setManager(true);
		}
		if(popedoms.contains("transactor")) {
			acl.add("transactor");
		}
		return acl;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manager") || acl.contains("transactor")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manager")) { //只允许管理员删除
			return;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		OnlineServiceItem serviceItem = (OnlineServiceItem)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(serviceItem!=null) {
			//获取近期投诉、咨询列表
			OnlineserviceInteractiveService onlineserviceInteractiveService = (OnlineserviceInteractiveService)getService("onlineserviceInteractiveService");
			serviceItem.setComplaints(onlineserviceInteractiveService.listRecentCompaints(serviceItem.getId(), 50));
			serviceItem.setConsults(onlineserviceInteractiveService.listRecentConsults(serviceItem.getId(), 50));
		}
		return serviceItem;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		ServiceItem serviceItemForm = (ServiceItem)form;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		boolean first = true;
		String otherNames = null;
		String otherIds = null;
		for(Iterator iterator = serviceItemForm.getSubjections().iterator(); iterator.hasNext();) {
			OnlineServiceItemSubjection subjection = (OnlineServiceItemSubjection)iterator.next();
			try {
				String directoryName = onlineServiceDirectoryService.getDirectoryFullName(subjection.getDirectoryId(), "/", "mainDirectory");
				if(first) {
					first = false;
					//设置所在目录名称
					serviceItemForm.setDirectoryName(directoryName);
					serviceItemForm.setDirectoryId(subjection.getDirectoryId());
				}
				else {
					otherIds = (otherIds==null ? "" : otherIds + ",") + subjection.getDirectoryId();
					otherNames = (otherNames==null ? "" : otherNames + ",") + directoryName;
				}
			}
			catch(Exception e) {
				
			}
		}
		//设置所在的其他目录名称
		serviceItemForm.setOtherDirectoryIds(otherIds);
		serviceItemForm.setOtherDirectoryNames(otherNames);
		
		//获取目录所属站点
		OnlineServiceMainDirectory mainDirectory = onlineServiceDirectoryService.getMainDirectory(serviceItemForm.getDirectoryId());
		serviceItemForm.setSiteId(mainDirectory==null ? 0 : mainDirectory.getSiteId());
		
		serviceItemForm.setServiceItemUnitIds(ListUtils.join(serviceItemForm.getUnits(), "unitId", ",", false)); //办理机构ID列表
		serviceItemForm.setServiceItemUnitNames(ListUtils.join(serviceItemForm.getUnits(), "unitName", ",", false)); //办理机构名称列表
		serviceItemForm.setServiceItemTransactorIds(ListUtils.join(serviceItemForm.getTransactors(), "userId", ",", false)); //办理人ID列表
		serviceItemForm.setServiceItemTransactorNames(ListUtils.join(serviceItemForm.getTransactors(), "userName", ",", false)); //办理人列表
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ServiceItem serviceItemForm = (ServiceItem)form;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService");
		serviceItemForm.setCreated(DateTimeUtils.now());
		//设置所在目录
		serviceItemForm.setDirectoryName(onlineServiceDirectoryService.getDirectoryFullName(serviceItemForm.getDirectoryId(), "/", "mainDirectory"));
		if(serviceItemForm.getIsPublic()==0) {
			serviceItemForm.setIsPublic('1'); //是否公开
		}
		if(serviceItemForm.getAcceptSupport()==0) {
			serviceItemForm.setAcceptSupport('1'); //是否支持在线受理,网上预审，同时提供查询功能
		}
		if(serviceItemForm.getComplaintSupport()==0) {
			serviceItemForm.setComplaintSupport('1'); //是否支持在线投诉,同时提供查询功能
		}
		if(serviceItemForm.getConsultSupport()==0) {
			serviceItemForm.setConsultSupport('1'); //是否支持在线咨询,同时提供查询功能
		}
		serviceItemForm.setCreator(sessionInfo.getUserName()); //创建人
		serviceItemForm.setCreated(DateTimeUtils.now()); //创建时间
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		ServiceItem serviceItemForm = (ServiceItem)form;
		OnlineServiceItem serviceItem = (OnlineServiceItem)record;
		if(form.getSubForm().indexOf("Edit")!=-1) {
			request.setAttribute("editabled", "true");
		}
		//设置页签
		form.getTabs().addTab(-1, "basic", "基本信息", null, true);
		//form.getTabs().addTab(-1, "according", "法定依据", null, false);
		//form.getTabs().addTab(-1, "condition", "申报条件", null, false);
		//form.getTabs().addTab(-1, "program", "办理流程", null, false);
		//form.getTabs().addTab(-1, "legalRight", "申请人法律权利", null, false);
		//form.getTabs().addTab(-1, "charge", "收费", null, false);
		form.getTabs().addTab(-1, "materials", "申报材料", null, false);
		//form.getTabs().addTab(-1, "faqs", "常见问题解答", null, false);
//		if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
//			form.getTabs().addTab(-1, "complaints", "近期投诉", null, false);
//			form.getTabs().addTab(-1, "consults", "近期咨询", null, false);
//		}
		//设置窗口标题
		String itemType = serviceItem==null ? serviceItemForm.getItemType() : serviceItem.getItemType();
		form.setFormTitle(serviceItem==null ? itemType : serviceItem.getName() + " - " + itemType);
		if("行政处罚".equals(itemType)) {
			form.getTabs().removeTab("condition");
			form.getTabs().removeTab("legalRight");
			form.getTabs().removeTab("materials");
			form.getTabs().removeTab("charge");
			form.setSubForm(form.getSubForm().replaceFirst("serviceItem", "punishment"));
		}
		else if("行政征收".equals(itemType)) {
			form.getTabs().removeTab("condition");
			form.getTabs().removeTab("legalRight");
			form.getTabs().removeTab("materials");
			form.getTabs().removeTab("charge");
			form.getTabs().removeTab("program");
			form.setSubForm(form.getSubForm().replaceFirst("serviceItem", "levy"));
		}
		else if("行政强制".equals(itemType)) {
			form.getTabs().removeTab("condition");
			form.getTabs().removeTab("legalRight");
			form.getTabs().removeTab("materials");
			form.getTabs().removeTab("charge");
			form.getTabs().removeTab("program");
			form.setSubForm(form.getSubForm().replaceFirst("serviceItem", "coercive"));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		OnlineServiceItem serviceItem = (OnlineServiceItem)record;
		ServiceItem serviceItemForm = (ServiceItem)form;
		OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(serviceItem.getCreated()==null) {
				serviceItem.setCreated(DateTimeUtils.now());
			}
			serviceItem.setCreatorId(sessionInfo.getUserId()); //创建人ID
			serviceItem.setCreator(sessionInfo.getUserName()); //创建人
			//编号
			onlineServiceItemService.generateItemCode(serviceItem, serviceItemForm.getDirectoryId());
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存所属目录
		if(serviceItemForm.getDirectoryName()!=null) {
			String directoryIds = serviceItemForm.getOtherDirectoryIds();
			directoryIds = serviceItemForm.getDirectoryId() + (directoryIds==null || directoryIds.equals("") ? "" : "," + directoryIds);
			onlineServiceItemService.updateServiceItemSubjectios(serviceItem, OPEN_MODE_CREATE.equals(openMode), directoryIds);
		}
		//保存办事指南
		onlineServiceItemService.updateServiceItemGuide(serviceItem, serviceItemForm.getServiceItemGuide());
		
		//保存办理机构
		if(serviceItemForm.getServiceItemUnitNames()!=null) {
			onlineServiceItemService.updateServiceItemUnits(serviceItem, OPEN_MODE_CREATE.equals(openMode), serviceItemForm.getServiceItemUnitIds(), serviceItemForm.getServiceItemUnitNames());
		}
		//保存办理人
		if(serviceItemForm.getServiceItemTransactorNames()!=null) {
			onlineServiceItemService.updateServiceItemTransactors(serviceItem, OPEN_MODE_CREATE.equals(openMode), serviceItemForm.getServiceItemTransactorIds(), serviceItemForm.getServiceItemTransactorNames());
		}
		return record;
	}
}
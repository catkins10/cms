package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemSubjection;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author yuanluesoft
 *
 */
public class UpdateSameNames extends FormAction {
	
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
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
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
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, false, null, null, null);
    }
    public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
    	// TODO 自动生成方法存根
    	long sourceId = form.getId();
    	ServiceItem serviceItemForm = (ServiceItem)form;
    	OnlineServiceItem serviceItem = (OnlineServiceItem)record;
    	DatabaseService databaseService = (DatabaseService)getService("databaseService");
    	OnlineServiceItem onlineServiceItem = (OnlineServiceItem)databaseService.findRecordById(OnlineServiceItem.class.getName(), serviceItemForm.getId());
    	String hql = "select OnlineServiceItem.id from OnlineServiceItem OnlineServiceItem where name = '"+onlineServiceItem.getName()+"'";
		List serviceItemIds = databaseService.findRecordsByHql(hql);
		if(serviceItemIds!=null && serviceItemIds.size()>0){
			for(int i = 0;i<serviceItemIds.size();i++){
				long id = Long.valueOf(serviceItemIds.get(i).toString()).longValue();
				serviceItem.setId(id);
				serviceItemForm.setId(id);
				super.saveRecord(form, record, openMode, request, response, sessionInfo);
				//更新申报材料
				serviceItem.getMaterials();
				for (Iterator material = serviceItem.getMaterials().iterator(); material.hasNext();) {
					if(sourceId == id){//本条记录不需要更新申报材料
						break;
					}
					OnlineServiceItemMaterial onlineServiceItemMaterial = (OnlineServiceItemMaterial) material.next();
					hql = "delete OnlineServiceItemMaterial OnlineServiceItemMaterial where OnlineServiceItemMaterial.itemId = "+id;//删除之前的申报材料
					onlineServiceItemMaterial.setId(UUIDLongGenerator.generateId());
					onlineServiceItemMaterial.setItemId(id);
					databaseService.saveRecord(onlineServiceItemMaterial);
				}
			}
		}
    	return record;
    }
}
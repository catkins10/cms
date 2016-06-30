package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem.material;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author zyh
 *
 */
public class UpdateSameMaterial extends ServiceItemMaterialAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String model = RequestUtils.getParameterStringValue(request, "model");
		if(model!=null && model.equals("save")){
			return executeSaveComponentAction(mapping, form, "material", "materials", "itemId", "refreshServiceItem", false, request, response);
		}
		return executeDeleteComponentAction(mapping, form, "material", "materials", "refreshServiceItem", request, response);
	}
	
	public void saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		// TODO 自动生成方法存根
		OnlineServiceItem onlineServiceItem = (OnlineServiceItem)mainRecord;
		OnlineServiceItemMaterial onlineServiceItemMaterial = (OnlineServiceItemMaterial)component;
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		String hql = "from OnlineServiceItem OnlineServiceItem where name = '"+onlineServiceItem.getName()+"'";
		List onlineServiceItems = databaseService.findRecordsByHql(hql);
		OnlineServiceItemMaterial data = (OnlineServiceItemMaterial)databaseService.findRecordById(OnlineServiceItemMaterial.class.getName(), onlineServiceItemMaterial.getId());
		if(data==null){//新增的申报材料
			OnlineServiceItemMaterial newMaterial = new OnlineServiceItemMaterial();
			for(int i = 0;i<onlineServiceItems.size();i++){
				OnlineServiceItem item = (OnlineServiceItem)onlineServiceItems.get(i);
				if(onlineServiceItem.getId()==item.getId()){//当前记录不处理
					continue;
				}
				PropertyUtils.copyProperties(newMaterial, onlineServiceItemMaterial);
				newMaterial.setId(UUIDLongGenerator.generateId());
				newMaterial.setItemId(item.getId());
				newMaterial.setItem(item);
				databaseService.saveRecord(newMaterial);
			}
		}else{//修改申报材料
			for(int i = 0;i<onlineServiceItems.size();i++){
				OnlineServiceItem item = (OnlineServiceItem)onlineServiceItems.get(i);
				if(onlineServiceItem.getId()==item.getId()){//当前记录不处理
					continue;
				}
				hql = "from OnlineServiceItemMaterial OnlineServiceItemMaterial where OnlineServiceItemMaterial.itemId="+item.getId()+" and OnlineServiceItemMaterial.name='"+data.getName()+"'";
				OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)databaseService.findRecordByHql(hql);
				if(material!=null){
					long id = material.getId();
					PropertyUtils.copyProperties(material, onlineServiceItemMaterial);
					material.setId(id);
					material.setItem(item);
					material.setItemId(item.getId());
					databaseService.updateRecord(material);
				}
			}
		}
		super.saveComponentRecord(form, mainRecord, component, componentName,
				foreignKeyProperty, sessionInfo, request);
		
	}
	
	public void deleteComponentRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		// TODO 自动生成方法存根
		OnlineServiceItem onlineServiceItem = (OnlineServiceItem)mainRecord;
		OnlineServiceItemMaterial onlineServiceItemMaterial = (OnlineServiceItemMaterial)PropertyUtils.getProperty(form, componentName);
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		String hql = "from OnlineServiceItem OnlineServiceItem where name = '"+onlineServiceItem.getName()+"'";
		List onlineServiceItems = databaseService.findRecordsByHql(hql);
		for(int i = 0;i<onlineServiceItems.size();i++){
			OnlineServiceItem item = (OnlineServiceItem)onlineServiceItems.get(i);
			if(onlineServiceItem.getId() == item.getId()){
				continue;
			}
			hql = "from OnlineServiceItemMaterial OnlineServiceItemMaterial where OnlineServiceItemMaterial.itemId = "+item.getId()+" and OnlineServiceItemMaterial.name = '"+onlineServiceItemMaterial.getName()+"'";
			databaseService.deleteRecordsByHql(hql);
		}
		super.deleteComponentRecord(form, mainRecord, componentName, sessionInfo,
				request);
	}
}
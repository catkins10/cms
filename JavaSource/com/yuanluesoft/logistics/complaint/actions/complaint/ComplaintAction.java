package com.yuanluesoft.logistics.complaint.actions.complaint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.logistics.complaint.forms.Complaint;
import com.yuanluesoft.logistics.complaint.pojo.LogisticsComplaint;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.logistics.supply.pojo.LogisticsSupply;
import com.yuanluesoft.logistics.supply.service.LogisticsSupplyService;
import com.yuanluesoft.logistics.usermanage.pojo.LogisticsUser;
import com.yuanluesoft.logistics.usermanage.service.LogisticsUserService;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleSupply;
import com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService;

/**
 * 
 * @author linchuan
 *
 */
public class ComplaintAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Complaint complaintForm = (Complaint)form;
		fillBean(complaintForm, complaintForm.getSupplyId(), complaintForm.getVehicleSupplyId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			LogisticsComplaint complaint = (LogisticsComplaint)record;
			fillBean(complaint, complaint.getSupplyId(), complaint.getVehicleSupplyId());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/**
	 * 更新货源/车源描述、被投诉公司/个人ID和名称
	 * @param bean
	 * @param supplyId
	 * @param vehicleSupplyId
	 */
	private void fillBean(Object bean, long supplyId, long vehicleSupplyId) throws Exception {
		long userId;
		if(supplyId>0) {
			LogisticsSupplyService logisticsSupplyService = (LogisticsSupplyService)getService("logisticsSupplyService");
			LogisticsSupply supply = (LogisticsSupply)logisticsSupplyService.load(LogisticsSupply.class, supplyId);
			//货源描述
			PropertyUtils.setProperty(bean, "supplyDescription", supply.getGoodsName() + "，" + supply.getDepartureAreas() + "->" + supply.getDestinationAreas());
			userId = supply.getUserId(); //用户ID
		}
		else {
			LogisticsVehicleService logisticsVehicleService = (LogisticsVehicleService)getService("logisticsVehicleService");
			LogisticsVehicleSupply supply = (LogisticsVehicleSupply)logisticsVehicleService.load(LogisticsVehicleSupply.class, vehicleSupplyId);
			//车源描述
			PropertyUtils.setProperty(bean, "supplyDescription", supply.getPlateNumbers() + "，" + supply.getDepartureAreas() + "->" + supply.getDestinationAreas());
			userId = supply.getUserId(); //用户ID
		}
		//设置公司/个人ID和名称
		LogisticsUserService logisticsUserService = (LogisticsUserService)getService("logisticsUserService");
		LogisticsUser user = (LogisticsUser)logisticsUserService.load(LogisticsUser.class, userId);
		if(user!=null) {
			PropertyUtils.setProperty(bean, "userId", new Long(userId));
			PropertyUtils.setProperty(bean, "userName", user.getName());
		}
	}
}
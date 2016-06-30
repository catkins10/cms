package com.yuanluesoft.logistics.complaint.actions.complaint.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.logistics.complaint.forms.admin.Complaint;
import com.yuanluesoft.logistics.supply.pojo.LogisticsSupply;
import com.yuanluesoft.logistics.supply.service.LogisticsSupplyService;
import com.yuanluesoft.logistics.usermanage.service.LogisticsUserService;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleSupply;
import com.yuanluesoft.logistics.vehicle.service.LogisticsVehicleService;

/**
 * 
 * @author linchuan
 *
 */
public class DoSanction extends ComplaintAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeRunAction(mapping, form, request, response, false, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		Complaint complaintForm = (Complaint)workflowForm;
		//删除被投诉的货源/车源
		if(complaintForm.getDeleteSupply()=='1') {
			if(complaintForm.getSupplyId()>0) { //货源
				LogisticsSupplyService logisticsSupplyService = (LogisticsSupplyService)getService("logisticsSupplyService");
				logisticsSupplyService.delete(logisticsSupplyService.load(LogisticsSupply.class, complaintForm.getSupplyId()));
			}
			else { //车源
				LogisticsVehicleService logisticsVehicleService = (LogisticsVehicleService)getService("logisticsVehicleService");
				logisticsVehicleService.delete(logisticsVehicleService.load(LogisticsVehicleSupply.class, complaintForm.getVehicleSupplyId()));
			}
		}
		//将被投诉的公司/个人列入黑名单
		if(complaintForm.getAddBlacklist()=='1') {
			LogisticsUserService logisticsUserService = (LogisticsUserService)getService("logisticsUserService");
			logisticsUserService.addToBlacklist(complaintForm.getUserId(), (complaintForm.getSupplyId()>0 ? "货源" : "车源") + "“" + complaintForm.getSupplyDescription() + "”被投诉");
		}
	}
}
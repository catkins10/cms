package com.yuanluesoft.msa.fees.actions.fees;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.base.model.user.Person;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.msa.fees.pojo.MsaFees;
import com.yuanluesoft.msa.fees.pojo.MsaFeesItem;
import com.yuanluesoft.workflow.client.model.user.ParticipantDepartment;
import com.yuanluesoft.workflow.client.model.user.ParticipantRole;

/**
 * 
 * @author linchuan
 *
 */
public class SendToUnits extends FeesAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, false, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#resetParticipants(java.util.List, boolean, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List resetParticipants(List participants, boolean anyoneParticipate, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		participants = super.resetParticipants(participants, anyoneParticipate, workflowForm, record, request, sessionInfo);
		MsaFees fees = (MsaFees)record;
		if(fees.getItems()==null || fees.getItems().isEmpty()) {
			return participants;
		}
		List unitIds = new ArrayList();
		for(Iterator iteratorItem = fees.getItems().iterator(); iteratorItem.hasNext();) {
    		MsaFeesItem item = (MsaFeesItem)iteratorItem.next();
    		if(unitIds.indexOf(new Long(item.getUnitId()))==-1) {
    			unitIds.add(new Long(item.getUnitId()));
    		}
		}
		OrgService orgService = (OrgService)getService("orgService");
		RoleService roleService = (RoleService)getService("roleService");
		//根据细项的受理单位,自动过滤办理人
		for(Iterator iterator = participants.iterator(); iterator.hasNext();) {
			Object participant = iterator.next();
			boolean match = false;
	        if(participant instanceof Person) { //个人
	        	//检查用户是否隶属于受理单位
	        	for(Iterator iteratorUnit = unitIds.iterator(); iteratorUnit.hasNext();) {
	        		long unitId = ((Long)iteratorUnit.next()).longValue();
	        		if(orgService.isMemberOfOrg(Long.parseLong(((Person)participant).getId()), unitId)) {
	        			match = true;
	        			break;
	        		}
	        	}
	        }
	        else if(participant instanceof ParticipantDepartment) { //部门办理人
	        	ParticipantDepartment participantDepartment = (ParticipantDepartment)participant;
	        	for(Iterator iteratorUnit = unitIds.iterator(); iteratorUnit.hasNext();) {
	        		long unitId = ((Long)iteratorUnit.next()).longValue();
	        		if(Long.parseLong(participantDepartment.getId())==unitId ||
	        		   orgService.isChildDirectory(Long.parseLong(participantDepartment.getId()), unitId)) {
	        			match = true;
	        			break;
	        		}
	        	}
	        }
	        else if(participant instanceof ParticipantRole) { //角色办理人
	        	ParticipantRole participantRole = (ParticipantRole)participant;
	        	Role role = roleService.getRole(Long.parseLong(participantRole.getId()));
	        	for(Iterator iteratorUnit = unitIds.iterator(); iteratorUnit.hasNext();) {
	        		long unitId = ((Long)iteratorUnit.next()).longValue();
	        		if(role.getOrgId()==unitId ||
	        		   orgService.isChildDirectory(role.getOrgId(), unitId)) {
	        			match = true;
	        			break;
	        		}
	        	}
	        }
	        if(!match) {
	        	iterator.remove();
	        }
		}
		return participants;
	}
}
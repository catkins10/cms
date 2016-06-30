package com.yuanluesoft.cms.situation.actions.situation.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.situation.pojo.Situation;
import com.yuanluesoft.cms.situation.service.SituationService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.user.ParticipantDepartment;

/**
 * 
 * @author chuan
 *
 */
public class Coordinate extends SituationAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	com.yuanluesoft.cms.situation.forms.admin.Situation situationForm = (com.yuanluesoft.cms.situation.forms.admin.Situation)form;
    	return executeSaveAction(mapping, form, request, response, false, null, situationForm.getCoordinate().getIsHigher()==1 ? "完成上报" : "协调完成", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.situation.actions.situation.admin.SituationAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Situation situation = (Situation)record;
		com.yuanluesoft.cms.situation.forms.admin.Situation situationForm = (com.yuanluesoft.cms.situation.forms.admin.Situation)form;
		SituationService situationService = (SituationService)getService("situationService");
		WorkflowExploitService workflowExploitService = (WorkflowExploitService)getService("workflowExploitService");
		
		//获取获取上级受理单位
		long unitId = situation.getCoordinateUnitId()==0 ? situation.getUnitId() : situation.getCoordinateUnitId();
		String unitName = situation.getCoordinateUnitId()==0 ? situation.getUnitName() : situation.getCoordinateUnitName();
		if(situationForm.getCoordinate().getIsHigher()==1) { //上报
			Org higherUnit = situationService.getHigherSituationUnit(unitId);
			if(higherUnit==null) {
				throw new Exception("no higher unit found");
			}
			situationForm.getCoordinate().setCoordinateUnitId(higherUnit.getId()); //协调单位ID
			situationForm.getCoordinate().setCoordinateUnitName(higherUnit.getDirectoryName()); //协调单位名称
		}
		situation.setCoordinateUnitId(situationForm.getCoordinate().getCoordinateUnitId());
		situation.setCoordinateUnitName(situationForm.getCoordinate().getCoordinateUnitName());
		situation = (Situation)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		
		//转办
		ParticipantDepartment participantDepartment = situationService.getSituationTransactorParticipant(situationForm.getCoordinate().getCoordinateUnitId(), situationForm.getCoordinate().getCoordinateUnitName());
		workflowExploitService.transmitToDepartment(situationForm.getWorkflowInstanceId(), situationForm.getWorkItemId(), false, participantDepartment.getId(), participantDepartment.getName(), participantDepartment.getPersonIds(), participantDepartment.getPersonNames(), createWorklfowMessage(situation, situationForm), situation, sessionInfo);
		
		//记录上报情况
		situationForm.getCoordinate().setId(UUIDLongGenerator.generateId()); //ID
		situationForm.getCoordinate().setSituationId(situation.getId()); //民情ID
		situationForm.getCoordinate().setIsHigher(1); //是否上报
		situationForm.getCoordinate().setUnitId(unitId); //单位ID
		situationForm.getCoordinate().setUnitName(unitName); //单位名称
		situationForm.getCoordinate().setCoordinateTime(DateTimeUtils.now()); //协调时间
		situationService.save(situationForm.getCoordinate());
		return situation;
	}
}
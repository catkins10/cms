package com.yuanluesoft.cms.situation.actions.situation.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.cms.situation.pojo.Situation;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author chuan
 *
 */
public class Feedback extends SituationAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeRunAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.situation.actions.situation.admin.SituationAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Situation situation = (Situation)record;
		PublicService publicService = (PublicService)getService("publicService");
		situation.setFeedbackNumber(publicService.getSN());
		situation.setFeedbackTime(DateTimeUtils.now());
		situation.setFeedbackUnit(situation.getCoordinateUnitId()==0 ? situation.getUnitName() : situation.getCoordinateUnitName());
		situation.setFeedbackUnitId(situation.getCoordinateUnitId()==0 ? situation.getUnitId() : situation.getCoordinateUnitId());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
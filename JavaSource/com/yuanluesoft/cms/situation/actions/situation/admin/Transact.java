package com.yuanluesoft.cms.situation.actions.situation.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.situation.pojo.Situation;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author chuan
 *
 */
public class Transact extends SituationAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeRunAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.situation.actions.situation.admin.SituationAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Situation situation = (Situation)record;
		com.yuanluesoft.cms.situation.forms.admin.Situation situationForm = (com.yuanluesoft.cms.situation.forms.admin.Situation)form;
		situation.setCoordinateUnitId(0);
		situation.setCoordinateUnitName(null);
		situation.setTransactorId(sessionInfo.getUserId());
		situation.setTransactor(sessionInfo.getUserName());
		situation.setTransactTime(DateTimeUtils.now());
		if(!situation.getUnitName().endsWith("村") && !situation.getUnitName().endsWith("社区")) {
			if(situationForm.isCoordinateOtherUnit()) { //协调其它部门
				situation.setTransactOpinion("协调其它部门处理（具体部门：" + situationForm.getCoordinateUnitName() + "）");
				situation.setCoordinateUnitId(situationForm.getCoordinateUnitId());
				situation.setCoordinateUnitName(situationForm.getCoordinateUnitName());
			}
		}
		else { //村或社区
			if("village".equals(situationForm.getVillageTransact())) { //村级办理选项
				situation.setTransactOpinion("由村、社区处理");
			}
			else if("town".equals(situationForm.getVillageTransact())) { //村级办理选项
				situation.setTransactOpinion("提交镇党委、政府研究决定");
				Org parentUnit = (Org)getOrgService().getParentDirectory(situation.getUnitId(), "unit");
				if(parentUnit!=null) {
					situation.setCoordinateUnitId(parentUnit.getId());
					situation.setCoordinateUnitName(parentUnit.getDirectoryName());
				}
			}
			else if("coordinate".equals(situationForm.getVillageTransact())) { //村级办理选项
				situation.setTransactOpinion("由挂村领导协调职能部门处理（具体部门：" + situationForm.getCoordinateUnitName() + "）");
				situation.setCoordinateUnitId(situationForm.getCoordinateUnitId());
				situation.setCoordinateUnitName(situationForm.getCoordinateUnitName());
			}
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
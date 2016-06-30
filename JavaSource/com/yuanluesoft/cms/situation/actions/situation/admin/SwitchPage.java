package com.yuanluesoft.cms.situation.actions.situation.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.situation.forms.admin.Situation;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author chuan
 *
 */
public class SwitchPage extends SituationAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Situation situationForm = (Situation)form;
		String pageName = situationForm.getPageName();
		situationForm.setInnerDialog(pageName + ".jsp");
		situationForm.getFormActions().addFormAction(-1, "完成", "FormUtils.doAction('" + pageName + "')", true);
		if("transact".equals(pageName)) {
			situationForm.setFormTitle("民情办理");
		}
		else if("sendHigher".equals(pageName)) {
			situationForm.setFormTitle("上报");
			situationForm.getFormActions().addFormAction(-1, "完成", "FormUtils.doAction('coordinate', 'coordinate.isHigher=1')", true);
		}
		else if("coordinate".equals(pageName)) {
			situationForm.setFormTitle("协调其它部门");
		}
		else if("feedback".equals(pageName)) {
			situationForm.setFormTitle("民情回应");
		}
		else if("feedbackLetter".equals(pageName)) {
			situationForm.setFormTitle("回应函");
		}
		addReloadAction(situationForm, "取消", request, -1, false); //取消
	}
}
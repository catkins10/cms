package com.yuanluesoft.appraise.actions.appraiseunitsms.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.appraise.pojo.AppraiseUnitSms;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Send extends AppraiseUnitSmsAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "发送成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		AppraiseUnitSms appraiseUnitSms = (AppraiseUnitSms)record;
		AppraiseService appraiseService = (AppraiseService)getService("appraiseService");
		appraiseService.sendAppraiseUnitSms(appraiseUnitSms.getAreaId(), appraiseUnitSms.getUnitIds(), appraiseUnitSms.getSmsContent(), sessionInfo);
		appraiseUnitSms.setSendTime(DateTimeUtils.now());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
package com.yuanluesoft.telex.receive.cryptic.actions.receivecryptictelegram;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.telex.receive.base.pojo.ReceiveTelegram;

/**
 * 
 * @author linchuan
 *
 */
public class Archive extends ReceiveCrypticTelegramAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "完成归档！", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.actions.receivetelegram.ReceiveTelegramAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ReceiveTelegram telegram = (ReceiveTelegram)record;
		telegram.setArchiveTime(DateTimeUtils.now());
		telegram.setArchivePersonId(sessionInfo.getUserId());
		telegram.setArchivePersonName(sessionInfo.getUserName());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
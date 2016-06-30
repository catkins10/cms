package com.yuanluesoft.telex.receive.cryptic.actions.receivecryptictelegram;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.opinionmanage.service.OpinionService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.base.service.TelexService;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSignOpinion;
import com.yuanluesoft.telex.receive.cryptic.forms.ReceiveCrypticTelegram;

/**
 * 
 * @author linchuan
 *
 */
public class SaveOpinion extends ReceiveCrypticTelegramAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, "signOpinion", null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.actions.receivetelegram.ReceiveTelegramAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		ReceiveCrypticTelegram receiveTelegramForm = (ReceiveCrypticTelegram)form;
		//保存意见
		OpinionService opinionService = (OpinionService)getService("opinionService");
		opinionService.saveOpinion(TelegramSignOpinion.class.getName(), receiveTelegramForm.getOpinionId(), receiveTelegramForm.getId(), receiveTelegramForm.getOpinion(), receiveTelegramForm.getOpinionType(), 0, receiveTelegramForm.getOpinionCreator(), sessionInfo.getUserId(), sessionInfo.getUserName(), null, null, null, (receiveTelegramForm.getOpinionCreated()==null ? null : new Timestamp(receiveTelegramForm.getOpinionCreated().getTime())));
		//保存意见填写人
		TelexService telexService = (TelexService)getService("telexService");
		telexService.saveOpinionPerson(receiveTelegramForm.getOpinionCreator());
		//清空字段
		receiveTelegramForm.setOpinionId(0);
		receiveTelegramForm.setOpinion(null);
		receiveTelegramForm.setOpinionType(null);
		receiveTelegramForm.setOpinionCreator(null);
		receiveTelegramForm.setOpinionCreated(null);
		return record;
	}
}
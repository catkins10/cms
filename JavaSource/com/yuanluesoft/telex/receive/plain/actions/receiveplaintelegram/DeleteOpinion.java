package com.yuanluesoft.telex.receive.plain.actions.receiveplaintelegram;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.opinionmanage.service.OpinionService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSignOpinion;
import com.yuanluesoft.telex.receive.plain.forms.ReceivePlainTelegram;

/**
 * 
 * @author linchuan
 *
 */
public class DeleteOpinion extends ReceivePlainTelegramAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, "signOpinion", null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.actions.receivetelegram.ReceiveTelegramAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		ReceivePlainTelegram receiveTelegramForm = (ReceivePlainTelegram)form;
		//删除意见
		OpinionService opinionService = (OpinionService)getService("opinionService");
		opinionService.deleteOpinion(TelegramSignOpinion.class.getName(), receiveTelegramForm.getOpinionId());
		//清空字段
		receiveTelegramForm.setOpinionId(0);
		receiveTelegramForm.setOpinion(null);
		receiveTelegramForm.setOpinionType(null);
		receiveTelegramForm.setOpinionCreator(null);
		receiveTelegramForm.setOpinionCreated(null);
		return record;
	}
}
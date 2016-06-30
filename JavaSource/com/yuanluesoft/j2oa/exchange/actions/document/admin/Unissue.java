package com.yuanluesoft.j2oa.exchange.actions.document.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument;
import com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Unissue extends DocumentAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "撤销成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.exchange.actions.document.admin.DocumentAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getService("exchangeDocumentService");
		ExchangeDocument document = (ExchangeDocument)record;
		com.yuanluesoft.j2oa.exchange.forms.admin.ExchangeDocument documentForm = (com.yuanluesoft.j2oa.exchange.forms.admin.ExchangeDocument)form;
		exchangeDocumentService.unissueDocument(document, documentForm.getUndoReason(), documentForm.getResign()=='1', sessionInfo);
		return record;
	}
}
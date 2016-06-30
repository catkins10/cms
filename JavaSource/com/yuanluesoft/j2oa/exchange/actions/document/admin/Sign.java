package com.yuanluesoft.j2oa.exchange.actions.document.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument;
import com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Sign extends DocumentAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(isSecureAction && !isSecureURL(request)){
        	redirectToSecureLink(request, response);
        	return null;
        }
		try {
    		load(form, request, response);
    	}
		catch(Exception e) {
			return transactException(e, mapping, form, request, response, false);
        }
        return mapping.getInputForward();
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(com.yuanluesoft.jeaf.form.ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		ExchangeDocument document = (ExchangeDocument)super.loadRecord(form, formDefine, id, sessionInfo, request);
		ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getService("exchangeDocumentService");
		exchangeDocumentService.signDocument(document, sessionInfo);
		return document;
	}
}
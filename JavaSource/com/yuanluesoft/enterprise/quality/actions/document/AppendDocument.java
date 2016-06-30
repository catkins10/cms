package com.yuanluesoft.enterprise.quality.actions.document;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.quality.forms.Document;
import com.yuanluesoft.enterprise.quality.pojo.QualityDocument;
import com.yuanluesoft.enterprise.quality.pojo.QualityDocumentBody;
import com.yuanluesoft.enterprise.quality.service.QualityDocumentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AppendDocument extends DocumentAction {
     
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.quality.actions.document.DocumentAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		QualityDocument document = (QualityDocument)record;
		QualityDocumentService qualityDocumentService = (QualityDocumentService)getBusinessService(form.getFormDefine());
		qualityDocumentService.appendDocument(document, RequestUtils.getParameterLongValue(request, "qualityTemplateId"), request, sessionInfo);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.quality.actions.document.DocumentAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		QualityDocument document = (QualityDocument)record;
		QualityDocumentBody lastBody = null;
		if(document.getBodies()!=null) {
			for(Iterator iterator = document.getBodies().iterator(); iterator.hasNext();) {
				lastBody = (QualityDocumentBody)iterator.next();
			}
			Document documentForm = (Document)form;
			documentForm.getTabs().setTabSelected("body" + "_" + lastBody.getId());
		}
	}
}
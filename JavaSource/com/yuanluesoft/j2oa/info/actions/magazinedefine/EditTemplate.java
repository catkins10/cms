package com.yuanluesoft.j2oa.info.actions.magazinedefine;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.info.forms.MagazineDefine;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author chuan
 *
 */
public class EditTemplate extends MagazineDefineAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeOpenRemoteDocumentAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#writeRemoteDocument(java.lang.String, java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.util.List, com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, char, java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeRemoteDocument(String documentApplicationName, String documentCommand, Map documentCommandParameter, String documentRelatedApplicationName, String documentRelatedFormName, List documentFiles, com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		//获取模板
		MagazineDefine magazineDefineForm = (MagazineDefine)form;
		InfoService infoService = (InfoService)getService("infoService");
		Attachment template = infoService.getWordTemplate(form.getId());
		if(magazineDefineForm.getName()!=null && !magazineDefineForm.getName().isEmpty()) {
			template.setName(magazineDefineForm.getName() + ".doc");
		}
		documentFiles = ListUtils.generateList(template);
		documentRelatedFormName = "magazine";
		super.writeRemoteDocument(RemoteDocumentService.APPLICATION_WORD, RemoteDocumentService.COMMAND_EDIT_TEMPLATE, documentCommandParameter, documentRelatedApplicationName, documentRelatedFormName, documentFiles, form, request, response, sessionInfo);
	}
}
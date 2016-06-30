package com.yuanluesoft.cms.sitemanage.actions.webdirectory.column;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class SelectAttachment extends ColumnAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeAttachmentAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(com.yuanluesoft.jeaf.form.ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		return getBusinessService(formDefine).load(WebDirectory.class, id);
	}
}
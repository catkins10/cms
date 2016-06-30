package com.yuanluesoft.jeaf.usermanage.actions.admin.batchregistemployees;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.actions.admin.person.PersonAction;
import com.yuanluesoft.jeaf.usermanage.forms.admin.BatchRegistEmployees;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 * 
 * @author linchuan
 *
 */
public class BatchRegistEmployeesAction extends PersonAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.admin.person.PersonAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		BatchRegistEmployees batchRegistEmployeesForm = (BatchRegistEmployees)form;
		AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
		List files = attachmentService.list("jeaf/usermanage", "data", batchRegistEmployeesForm.getId(), false, 1, request);
		if(files==null || files.isEmpty()) {
			return null;
		}
		try {
			PersonService personService = (PersonService)getService("personService");
			personService.batchRegistEmployees(((Attachment)files.get(0)).getFilePath(), batchRegistEmployeesForm.getPassword(), batchRegistEmployeesForm.getOrgId(), sessionInfo.getUserId(), sessionInfo.getUserName());
		}
		catch(Exception e) {
			
		}
		finally {
			attachmentService.deleteAll("jeaf/usermanage", "data", batchRegistEmployeesForm.getId());
		}
		return null;
	}
}